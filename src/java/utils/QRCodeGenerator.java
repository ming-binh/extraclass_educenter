package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    
    private static final int QR_CODE_SIZE = 300;
    
    /**
     * Tạo VietQR code theo chuẩn NAPAS
     */
    public static String generateVietQR(String bankName, String accountNumber, 
                                       BigDecimal amount, String transferContent, 
                                       String accountName) throws WriterException, IOException {
        
        // Chuyển đổi bank name sang bank code
        String bankCode = getBankCodeFromName(bankName);
        
        // Format VietQR theo chuẩn NAPAS
        StringBuilder vietQR = new StringBuilder();
        
        // Payload Format Indicator
        vietQR.append("000201");
        
        // Point of Initiation Method
        vietQR.append("010212");
        
        // Merchant Account Information
        vietQR.append("38"); // Field ID
        String merchantInfo = buildMerchantInfo(bankCode, accountNumber);
        vietQR.append(String.format("%02d", merchantInfo.length()));
        vietQR.append(merchantInfo);
        
        // Transaction Currency (VND = 704)
        vietQR.append("5303704");
        
        // Transaction Amount
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            String amountStr = amount.stripTrailingZeros().toPlainString();
            vietQR.append("54");
            vietQR.append(String.format("%02d", amountStr.length()));
            vietQR.append(amountStr);
        }
        
        // Country Code
        vietQR.append("5802VN");
        
        // Merchant Name (Account Name)
        if (accountName != null && !accountName.isEmpty()) {
            String nameBytes = accountName.getBytes(StandardCharsets.UTF_8).length + "";
            vietQR.append("59");
            vietQR.append(String.format("%02d", Integer.parseInt(nameBytes)));
            vietQR.append(accountName);
        }
        
        // Additional Data Field
        if (transferContent != null && !transferContent.isEmpty()) {
            String additionalData = "08" + String.format("%02d", transferContent.length()) + transferContent;
            vietQR.append("62");
            vietQR.append(String.format("%02d", additionalData.length()));
            vietQR.append(additionalData);
        }
        
        // CRC (sẽ được tính sau)
        vietQR.append("6304");
        
        // Tính CRC16
        String dataForCRC = vietQR.toString();
        String crc = calculateCRC16(dataForCRC);
        vietQR.append(crc);
        
        return generateQRCode(vietQR.toString());
    }
    
    /**
     * Tạo QR code đơn giản với thông tin text
     */
    public static String generateSimpleQR(String content) throws WriterException, IOException {
        return generateQRCode(content);
    }
    
    /**
     * Chuyển đổi tên ngân hàng sang mã ngân hàng
     */
    private static String getBankCodeFromName(String bankName) {
        if (bankName == null) return "970436"; // Default VCB
        
        String name = bankName.toLowerCase().trim();
        
        // Mapping các ngân hàng phổ biến
        if (name.contains("vietcombank") || name.contains("vcb")) {
            return "970436";
        } else if (name.contains("bidv")) {
            return "970418";
        } else if (name.contains("techcombank") || name.contains("tcb")) {
            return "970407";
        } else if (name.contains("vietinbank") || name.contains("ctg")) {
            return "970415";
        } else if (name.contains("agribank") || name.contains("agb")) {
            return "970405";
        } else if (name.contains("sacombank") || name.contains("stb")) {
            return "970403";
        } else if (name.contains("mbbank") || name.contains("mb")) {
            return "970422";
        } else if (name.contains("vpbank")) {
            return "970432";
        } else if (name.contains("acb")) {
            return "970416";
        } else if (name.contains("dongabank") || name.contains("dab")) {
            return "970406";
        } else if (name.contains("eximbank") || name.contains("exb")) {
            return "970431";
        } else if (name.contains("hdbank")) {
            return "970437";
        } else if (name.contains("lienvietpostbank") || name.contains("lpb")) {
            return "970449";
        } else if (name.contains("oceanbank")) {
            return "970414";
        } else if (name.contains("seabank")) {
            return "970440";
        } else if (name.contains("shb")) {
            return "970424";
        } else if (name.contains("tpbank")) {
            return "970423";
        } else if (name.contains("vib")) {
            return "970441";
        } else if (name.contains("bacabank")) {
            return "970409";
        }
        
        // Default trả về Vietcombank nếu không tìm thấy
        return "970436";
    }
    
    /**
     * Tạo merchant info cho VietQR
     */
    private static String buildMerchantInfo(String bankCode, String accountNumber) {
        StringBuilder merchant = new StringBuilder();
        
        // GUID for VietQR
        merchant.append("0010A000000727");
        
        // Service Code
        merchant.append("01");
        String serviceCode = "QRIBFTTA";
        merchant.append(String.format("%02d", serviceCode.length()));
        merchant.append(serviceCode);
        
        // Beneficiary Bank
        merchant.append("02");
        merchant.append(String.format("%02d", bankCode.length()));
        merchant.append(bankCode);
        
        // Beneficiary Account
        merchant.append("03");
        merchant.append(String.format("%02d", accountNumber.length()));
        merchant.append(accountNumber);
        
        return merchant.toString();
    }
    
    /**
     * Tính CRC16 cho VietQR
     */
    private static String calculateCRC16(String data) {
        int crc = 0xFFFF;
        byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);
        
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        
        return String.format("%04X", crc);
    }
    
    /**
     * Tạo QR code image từ content
     */
    private static String generateQRCode(String content) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        hints.put(EncodeHintType.MARGIN, 1);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 
                                                 QR_CODE_SIZE, QR_CODE_SIZE, hints);
        
        BufferedImage image = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, QR_CODE_SIZE, QR_CODE_SIZE);
        graphics.setColor(Color.BLACK);
        
        for (int i = 0; i < QR_CODE_SIZE; i++) {
            for (int j = 0; j < QR_CODE_SIZE; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}