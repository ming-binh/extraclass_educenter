package utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 ₫";
        }
        
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(vietnam);
        return formatter.format(amount);
    }
} 