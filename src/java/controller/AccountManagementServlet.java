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
import utils.HashUtils;

/**
 *
 * @author vankh
 */
public class AccountManagementServlet extends HttpServlet {

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
            out.println("<title>Servlet AccountManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AccountManagementServlet at " + request.getContextPath() + "</h1>");
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
        AccountDAO acDao = new AccountDAO();
        String action = request.getParameter("action");
        String role = (String) request.getAttribute("loggedInUserRole");
        String statusFilter = request.getParameter("statusFilter");
        String roleFilter = request.getParameter("roleFilter");
        String status = request.getParameter("status");
        String accountIdStr = request.getParameter("accountId");
        if (action != null) {
            if (action.equalsIgnoreCase("changeStatus")) {
                int accountId = Integer.parseInt(accountIdStr);
                if (status.equalsIgnoreCase("active")) {
                    try {
                        acDao.updateStatusById(accountId, "inactive");
                        AccountModal ac = acDao.getAccountById(accountId);
                        request.setAttribute("succes", "Đổi trạng thái người dùng " + ac.getName() + " thành công!");

                        request.setAttribute("error", "Đổi trạng thái thất bại!");

                        request.getRequestDispatcher("/views/accountManagement.jsp").forward(request, response);
                    } catch (Exception ex) {
                        Logger.getLogger(AccountManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        acDao.updateStatusById(accountId, "active");
                        AccountModal ac = acDao.getAccountById(accountId);
                        request.setAttribute("succes", "Đổi trạng thái người dùng " + ac.getName() + " thành công!");

                        request.setAttribute("error", "Đổi trạng thái thất bại!");
                        request.getRequestDispatcher("/views/accountManagement.jsp").forward(request, response);
                    } catch (Exception ex) {
                        Logger.getLogger(AccountManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int accountId = Integer.parseInt(accountIdStr);
                if (acDao.deleteById(accountId)) {
                    request.setAttribute("succes", "Xóa tài khoản thành công!");
                } else {
                    request.setAttribute("error", "Xóa tài khoản thất bại!");
                }
            } else if (action.equalsIgnoreCase("reset")) {
                int accountId = Integer.parseInt(accountIdStr);
                String newHashed = HashUtils.hashPassword("123456");
                try {
                    AccountModal ac = acDao.getAccountById(accountId);
                    try {
                        boolean success = acDao.updatePasswordById(accountId, newHashed);
                        if (success) {
                            request.setAttribute("succes", "Đặt lại mật khẩu người dùng " + ac.getName() + " thành công!");
                        } else {
                            request.setAttribute("error", "Đặt lại mật khẩu người dùng " + ac.getName() + " thất bại!");
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AccountManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(AccountManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        List<AccountModal> listAc = new ArrayList<>();
        if (role.equalsIgnoreCase("manager")) {
            List<AccountModal> listAc1 = acDao.getAllAccountByRoleNotFilter("teacher");
            List<AccountModal> listAc2 = acDao.getAllAccountByRoleNotFilter("student");
            List<AccountModal> listAc3 = acDao.getAllAccountByRoleNotFilter("staff");
            List<AccountModal> listAc4 = acDao.getAllAccountByRoleNotFilter("parent");
            listAc.addAll(listAc1);
            listAc.addAll(listAc2);
            listAc.addAll(listAc3);
            listAc.addAll(listAc4);
        }

        if (roleFilter != null && !roleFilter.isEmpty() && !roleFilter.equalsIgnoreCase("all")) {
            listAc = acDao.getAllAccountByRoleNotFilter(roleFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equalsIgnoreCase("all")) {
            listAc = listAc.stream()
                    .filter(a -> a.getStatus().toString().equals(statusFilter))
                    .collect(Collectors.toList());
        }

        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("roleFilter", roleFilter);
        request.setAttribute("accounts", listAc);
        request.getRequestDispatcher("/views/accountManagement.jsp").forward(request, response);

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
        processRequest(request, response);
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
