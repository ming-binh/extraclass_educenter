/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.StudentDAO;
import dto.StudentBasicInfo;
import dto.StudentProfile;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.AccountModal;

/**
 *
 * @author vankh
 */
public class ViewChildServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StudentDAO dao = new StudentDAO();
        String username = (String) request.getAttribute("loggedInUserName");

        // Kiểm tra username
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Không tìm thấy thông tin đăng nhập");
            request.getRequestDispatcher("/views/viewChild.jsp").forward(request, response);
            return;
        }

        AccountDAO acDao = new AccountDAO();
        AccountModal account = null;

        try {
            account = acDao.getAccountByUsername(username);
            if (account == null) {
                request.setAttribute("error", "Không tìm thấy thông tin tài khoản");
                request.getRequestDispatcher("/views/viewChild.jsp").forward(request, response);
                return;
            }
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi khi lấy thông tin tài khoản: " + ex.getMessage());
            request.getRequestDispatcher("/views/viewChild.jsp").forward(request, response);
            return;
        }

        String parentPhone = account.getPhone();

        try {
            List<StudentProfile> childrenProfiles = dao.getChildrenProfilesByParentPhone(parentPhone);
            request.setAttribute("childrenProfiles", childrenProfiles);

            // Tạo map chứa studentId -> dobFormatted
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Map<Integer, String> dobMap = new HashMap<>();

            for (StudentProfile child : childrenProfiles) {
                if (child.getDob() != null) {
                    dobMap.put(child.getId(), child.getDob().format(formatter));
                }
            }

            request.setAttribute("dobMap", dobMap); // gửi sang JSP
            request.setAttribute("parentName", account.getName());
            request.setAttribute("parentPhone", parentPhone);

        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi khi tải dữ liệu con: " + ex.getMessage());
            ex.printStackTrace();
        }

        request.getRequestDispatcher("/views/viewChild.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
