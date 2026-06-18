/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dto.ClassBeingTaughtDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.AccountModal;

/**
 *
 * @author vankh
 */
public class ClassBeingTaughtServlet extends HttpServlet {

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
            out.println("<title>Servlet ClassBeingTaught</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClassBeingTaught at " + request.getContextPath() + "</h1>");
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

        String usname = (String) request.getAttribute("loggedInUserName");
        AccountDAO acdao = new AccountDAO();
        AccountModal ac = null;
        try {
            ac = acdao.getAccountByUsername(usname);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ViewScoreServlet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        String userName = ac.getPhone();
        String role = (String) request.getAttribute("loggedInUserRole");
        if (userName == null || role == null) {
            response.sendRedirect("dang-nhap");
            return;
        }

        StudentDAO studentDao = new StudentDAO();
        AccountDAO accountDao = new AccountDAO();
        TeacherDAO teacherDao = new TeacherDAO();

        try {
            List<ClassBeingTaughtDTO> listClass = teacherDao.getClassesByTeacherPhone(userName);
            request.setAttribute("listClass", listClass);
            request.getRequestDispatcher("/views/classBeingTaught.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ClassBeingTaughtServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

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
