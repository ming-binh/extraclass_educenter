/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.BannerDAO;
import dao.CenterInfoDAO;
import dao.PaymentInfoDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.BannerModal;
import modal.CenterInfoModal;
import modal.PaymentInfoModal;
import utils.FileUploadUtils;

/**
 *
 * @author Astersa
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class SystemConfigServlet extends HttpServlet {

    private BannerDAO bannerDAO;
    private CenterInfoDAO centerInfoDAO;
    private PaymentInfoDAO paymentInfoDAO;

    @Override
    public void init() throws ServletException {
        bannerDAO = new BannerDAO();
        centerInfoDAO = new CenterInfoDAO();
        paymentInfoDAO = new PaymentInfoDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }
        
        try {
            switch (action) {
                case "view":
                    viewSystemConfig(request, response);
                    break;
                case "add-banner":
                    addBanner(request, response);
                    break;
                case "edit-banner":
                    editBanner(request, response);
                    break;
                case "delete-banner":
                    deleteBanner(request, response);
                    break;
                case "toggle-banner":
                    toggleBanner(request, response);
                    break;
                case "update-center-info":
                    updateCenterInfo(request, response);
                    break;
                case "add-payment":
                    addPaymentInfo(request, response);
                    break;
                case "edit-payment":
                    editPaymentInfo(request, response);
                    break;
                case "delete-payment":
                    deletePaymentInfo(request, response);
                    break;
                case "set-active-payment":
                    setActivePaymentInfo(request, response);
                    break;
                case "upload-qr":
                    uploadQrCode(request, response);
                    break;
                default:
                    viewSystemConfig(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            viewSystemConfig(request, response);
        }
    }

    private void viewSystemConfig(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        List<BannerModal> banners = bannerDAO.getAllBanners();
        request.setAttribute("banners", banners);
        
        CenterInfoModal centerInfo = centerInfoDAO.getCenterInfo();
        request.setAttribute("centerInfo", centerInfo);
        
        List<PaymentInfoModal> paymentInfos = paymentInfoDAO.getAllPaymentInfo();
        PaymentInfoModal activePayment = paymentInfoDAO.getActivePaymentInfo();
        request.setAttribute("paymentInfos", paymentInfos);
        request.setAttribute("activePayment", activePayment);
        
        request.getRequestDispatcher("/views/systemConfig.jsp").forward(request, response);
    }

    private void addBanner(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String orderIndexStr = request.getParameter("orderIndex");
        String isActiveStr = request.getParameter("isActive");
        
        Part filePart = request.getPart("image");
        String imageUrl = null;
        
        if (filePart != null && filePart.getSize() > 0) {
            imageUrl = FileUploadUtils.uploadFile(filePart, "banners", getServletContext().getRealPath("/"));
        }
        
        if (imageUrl != null) {
            BannerModal banner = new BannerModal();
            banner.setTitle(title);
            banner.setDescription(description);
            banner.setImageUrl(imageUrl);
            banner.setOrderIndex(Integer.parseInt(orderIndexStr));
            banner.setIsActive("on".equals(isActiveStr));
            
            bannerDAO.createBanner(banner);
            request.setAttribute("success", "Thêm banner thành công!");
        } else {
            request.setAttribute("error", "Vui lòng chọn hình ảnh!");
        }
        
        viewSystemConfig(request, response);
    }

    private void editBanner(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException,Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String orderIndexStr = request.getParameter("orderIndex");
        String isActiveStr = request.getParameter("isActive");
        
        BannerModal banner = bannerDAO.getBannerById(id);
        if (banner != null) {
            banner.setTitle(title);
            banner.setDescription(description);
            banner.setOrderIndex(Integer.parseInt(orderIndexStr));
            banner.setIsActive("on".equals(isActiveStr));
            
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String imageUrl = FileUploadUtils.uploadFile(filePart, "banners", getServletContext().getRealPath("/"));
                banner.setImageUrl(imageUrl);
            }
            
            bannerDAO.updateBanner(banner);
            request.setAttribute("success", "Cập nhật banner thành công!");
        }
        
        viewSystemConfig(request, response);
    }

    private void deleteBanner(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        bannerDAO.deleteBanner(id);
        request.setAttribute("success", "Xóa banner thành công!");
        viewSystemConfig(request, response);
    }

    private void toggleBanner(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
        bannerDAO.toggleBannerStatus(id, isActive);
        request.setAttribute("success", "Cập nhật trạng thái banner thành công!");
        viewSystemConfig(request, response);
    }

    private void updateCenterInfo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        String centerName = request.getParameter("centerName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String website = request.getParameter("website");
        String description = request.getParameter("description");
        String workingHours = request.getParameter("workingHours");
        String facebook = request.getParameter("facebook");
        String youtube = request.getParameter("youtube");
        String instagram = request.getParameter("instagram");
        
        CenterInfoModal centerInfo = centerInfoDAO.getCenterInfo();
        if (centerInfo == null) {
            centerInfo = new CenterInfoModal();
            centerInfo.setCenterName(centerName);
            centerInfo.setAddress(address);
            centerInfo.setPhone(phone);
            centerInfo.setEmail(email);
            centerInfo.setWebsite(website);
            centerInfo.setDescription(description);
            centerInfo.setWorkingHours(workingHours);
            centerInfo.setFacebook(facebook);
            centerInfo.setYoutube(youtube);
            centerInfo.setInstagram(instagram);
            
            centerInfoDAO.createCenterInfo(centerInfo);
        } else {
            centerInfo.setCenterName(centerName);
            centerInfo.setAddress(address);
            centerInfo.setPhone(phone);
            centerInfo.setEmail(email);
            centerInfo.setWebsite(website);
            centerInfo.setDescription(description);
            centerInfo.setWorkingHours(workingHours);
            centerInfo.setFacebook(facebook);
            centerInfo.setYoutube(youtube);
            centerInfo.setInstagram(instagram);
            
            centerInfoDAO.updateCenterInfo(centerInfo);
        }
        
        request.setAttribute("success", "Cập nhật thông tin trung tâm thành công!");
        viewSystemConfig(request, response);
    }

    private void addPaymentInfo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        String bankName = request.getParameter("bankName");
        String accountNumber = request.getParameter("accountNumber");
        String accountName = request.getParameter("accountName");
        String branch = request.getParameter("branch");
        String swiftCode = request.getParameter("swiftCode");
        String orderIndexStr = request.getParameter("orderIndex");
        String isActiveStr = request.getParameter("isActive");
        
        Part filePart = request.getPart("qrCode");
        String qrCodeUrl = null;
        
        if (filePart != null && filePart.getSize() > 0) {
            qrCodeUrl = FileUploadUtils.uploadFile(filePart, "qr_codes", getServletContext().getRealPath("/"));
        }
        
        PaymentInfoModal paymentInfo = new PaymentInfoModal();
        paymentInfo.setBankName(bankName);
        paymentInfo.setAccountNumber(accountNumber);
        paymentInfo.setAccountName(accountName);
        paymentInfo.setBranch(branch);
        paymentInfo.setSwiftCode(swiftCode);
        paymentInfo.setQrCodeUrl(qrCodeUrl);
        paymentInfo.setOrderIndex(Integer.parseInt(orderIndexStr));
        paymentInfo.setIsActive("on".equals(isActiveStr));
        

        
        if (paymentInfo.getIsActive()) {
            paymentInfoDAO.setActivePaymentInfo(paymentInfoDAO.createPaymentInfo(paymentInfo));
        } else {
            paymentInfoDAO.createPaymentInfo(paymentInfo);
        }
        
        request.setAttribute("success", "Thêm thông tin thanh toán thành công!");
        viewSystemConfig(request, response);
    }

    private void editPaymentInfo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String bankName = request.getParameter("bankName");
        String accountNumber = request.getParameter("accountNumber");
        String accountName = request.getParameter("accountName");
        String branch = request.getParameter("branch");
        String swiftCode = request.getParameter("swiftCode");
        String orderIndexStr = request.getParameter("orderIndex");
        String isActiveStr = request.getParameter("isActive");
        
        PaymentInfoModal paymentInfo = paymentInfoDAO.getPaymentInfoById(id);
        if (paymentInfo != null) {
            paymentInfo.setBankName(bankName);
            paymentInfo.setAccountNumber(accountNumber);
            paymentInfo.setAccountName(accountName);
            paymentInfo.setBranch(branch);
            paymentInfo.setSwiftCode(swiftCode);
            paymentInfo.setOrderIndex(Integer.parseInt(orderIndexStr));
            paymentInfo.setIsActive("on".equals(isActiveStr));
            Part filePart = request.getPart("qrCode");
            if (filePart != null && filePart.getSize() > 0) {
                String qrCodeUrl = FileUploadUtils.uploadFile(filePart, "qr_codes", getServletContext().getRealPath("/"));
                paymentInfo.setQrCodeUrl(qrCodeUrl);
            }
            
            if (paymentInfo.getIsActive()) {
                paymentInfoDAO.updatePaymentInfo(paymentInfo);
                paymentInfoDAO.setActivePaymentInfo(id);
            } else {
                paymentInfoDAO.updatePaymentInfo(paymentInfo);
            }
            
            request.setAttribute("success", "Cập nhật thông tin thanh toán thành công!");
        }
        
        viewSystemConfig(request, response);
    }

    private void deletePaymentInfo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        paymentInfoDAO.deletePaymentInfo(id);
        request.setAttribute("success", "Xóa thông tin thanh toán thành công!");
        viewSystemConfig(request, response);
    }

    private void setActivePaymentInfo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        paymentInfoDAO.setActivePaymentInfo(id);
        request.setAttribute("success", "Đặt tài khoản thanh toán mặc định thành công!");
        viewSystemConfig(request, response);
    }

    private void uploadQrCode(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Part filePart = request.getPart("qrCode");
        
        if (filePart != null && filePart.getSize() > 0) {
            String qrCodeUrl = FileUploadUtils.uploadFile(filePart, "qr_codes", getServletContext().getRealPath("/"));
            paymentInfoDAO.updatePaymentInfoQrCode(id, qrCodeUrl);
            request.setAttribute("success", "Cập nhật mã QR thành công!");
        }
        
        viewSystemConfig(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SystemConfigServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SystemConfigServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "System Configuration Management Servlet";
    }
} 