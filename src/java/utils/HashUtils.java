/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtils {

    // Mã hóa mật khẩu
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // So sánh mật khẩu
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String rawPw = "123456";
        String hashed = HashUtils.hashPassword(rawPw);
        System.out.println("Hashed password: " + hashed);

        // Test check
        System.out.println("Match? " + HashUtils.checkPassword("fptedu", hashed));
    }

}
