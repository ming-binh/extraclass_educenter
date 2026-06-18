/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import modal.AccountModal;
import utils.EmailUtil;

/**
 *
 * @author vankh
 */
public class AccountApproveServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet accountApproveServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet accountApproveServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getAttribute("loggedInUserRole");
        String roleFilter = request.getParameter("roleFilter");
        String action = request.getParameter("action");
        AccountDAO acDao = new AccountDAO();
        if (action != null) {
            String accountIdStr = request.getParameter("accountId");
            if (accountIdStr != null) {
                int id = Integer.parseInt(accountIdStr);
                try {
                    if (action.equalsIgnoreCase("acept")) {
                        // Lấy thông tin tài khoản trước khi cập nhật
                        AccountModal account = acDao.getAccountById(id);

                        // Cập nhật trạng thái tài khoản
                        acDao.updateStatusById(id, "active");

                        // Gửi email thông báo phê duyệt
                        if (account != null) {
                            boolean emailSent = EmailUtil.sendAccountApprovalNotification(
                                    account.getUsername(),
                                    account.getName(),
                                    account.getRole().toString()
                            );

                            if (emailSent) {
                                System.out.println("Email thông báo đã được gửi đến: " + account.getUsername());
                                // Có thể thêm thông báo thành công vào session
                                request.getSession().setAttribute("successMessage",
                                        "Tài khoản đã được phê duyệt và email thông báo đã được gửi!");
                            } else {
                                System.err.println("Không thể gửi email thông báo đến: " + account.getUsername());
                                // Có thể thêm thông báo lỗi vào session
                                request.getSession().setAttribute("warningMessage",
                                        "Tài khoản đã được phê duyệt nhưng không thể gửi email thông báo!");
                            }
                        }
                    } else {
                        // Lấy thông tin tài khoản trước khi xóa (nếu cần gửi email thông báo từ chối)
                        AccountModal account = acDao.getAccountById(id);

                        acDao.deleteById(id);

                        // Có thể thêm hàm gửi email thông báo từ chối ở đây nếu cần
                        System.out.println("Tài khoản đã bị từ chối và xóa: "
                                + (account != null ? account.getUsername() : "Unknown"));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(AccountApproveServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.getSession().setAttribute("errorMessage",
                            "Đã xảy ra lỗi khi xử lý tài khoản!");
                }
            }
        }
        List<AccountModal> listAc = null;
        try {
            listAc = acDao.getAllAccounts();
        } catch (Exception ex) {
            Logger.getLogger(AccountApproveServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (AccountModal a : listAc) {
            System.out.println(a.getStatus());
        }
        System.out.println("==============");
        if (role.equalsIgnoreCase("manager")) {
            listAc = listAc.stream()
                    .filter(a -> a.getRole().toString().equals("staff") || a.getRole().toString().equals("student") || a.getRole().toString().equals("teacher") || a.getRole().toString().equals("parent"))
                    .collect(Collectors.toList());
        }

        for (AccountModal a : listAc) {
            System.out.println(a.getStatus());
        }

        if (roleFilter != null && !roleFilter.isEmpty() && !roleFilter.equalsIgnoreCase("all")) {
            listAc = acDao.getAllAccountByRoleNotFilter(roleFilter);
        }
        listAc = listAc.stream()
                .filter(a -> a.getStatus().toString().equals("pending"))
                .collect(Collectors.toList());

        for (AccountModal a : listAc) {
            System.out.println(a.getStatus());
        }
        request.setAttribute("roleFilter", roleFilter);
        request.setAttribute("accounts", listAc);
        request.getRequestDispatcher("/views/accountApprove.jsp").forward(request, response);

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
