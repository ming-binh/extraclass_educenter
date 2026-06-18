/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.StudentDAO;
import dto.StudentBasicInfo;
import dto.StudentScoreView;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.AccountModal;

/**
 *
 * @author vankh
 */
public class ViewScoreServlet extends HttpServlet {

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
            out.println("<title>Servlet ViewScore</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewScore at " + request.getContextPath() + "</h1>");
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
        StudentDAO dao = new StudentDAO();
        String usname = (String) request.getAttribute("loggedInUserName");
        AccountDAO acdao = new AccountDAO();
        AccountModal ac = null;
        try {
            ac = acdao.getAccountByUsername(usname);
        } catch (Exception ex) {
            Logger.getLogger(ViewScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String userName = ac.getPhone();
        Map<String, List<StudentScoreView>> scoreMap;
        try {
            scoreMap = dao.getStudentScoresGroupedByCourse(userName);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            scoreMap.forEach((course, scores) -> {
                scores.forEach(score -> {
                    if (score.getDate() != null) {
                        score.setFormattedDate(score.getDate().format(formatter));
                    } else {
                        score.setFormattedDate("N/A");
                    }
                });
            });
            request.setAttribute("scoreMap", scoreMap);
        } catch (Exception ex) {
            Logger.getLogger(ViewScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("/views/viewScore.jsp").forward(request, response);
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
        String studentId = request.getParameter("studentId");
        int stId = Integer.parseInt(studentId);

        StudentDAO dao = new StudentDAO();
        Map<String, List<StudentScoreView>> scoreMap;
        try {
            scoreMap = dao.getStudentScoresGroupedByCourse(stId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            scoreMap.forEach((course, scores) -> {
                scores.forEach(score -> {
                    if (score.getDate() != null) {
                        score.setFormattedDate(score.getDate().format(formatter));
                    } else {
                        score.setFormattedDate("N/A");
                    }
                });
            });
            request.setAttribute("scoreMap", scoreMap);
        } catch (Exception ex) {
            Logger.getLogger(ViewScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("/views/viewScore.jsp").forward(request, response);
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
