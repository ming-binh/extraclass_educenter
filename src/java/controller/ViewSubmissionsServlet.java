/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.SubmissionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import modal.CourseModal;
import modal.SubmissionModal;

/**
 *
 * @author HanND
 */
public class ViewSubmissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(request.getParameter("courseId"));

            SubmissionDAO submissionDAO = new SubmissionDAO();
            CourseDAO courseDAO = new CourseDAO();

            List<SubmissionModal> submissions = submissionDAO.getSubmissionsByCourse(courseId);
            CourseModal course = courseDAO.getCourseById(courseId);

            request.setAttribute("submissions", submissions);
            request.setAttribute("course", course);
            request.getRequestDispatcher("/views/viewSubmissions.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID or internal error.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int submissionId = Integer.parseInt(request.getParameter("submissionId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String comment = request.getParameter("comment");
        String gradeStr = request.getParameter("grade");

        Double grade = null;
        if (gradeStr != null && !gradeStr.trim().isEmpty()) {
            grade = Double.valueOf(gradeStr);
        }

        SubmissionDAO dao = new SubmissionDAO();
        try {
            dao.gradeSubmission(submissionId, grade, comment);
            response.sendRedirect("viewSubmissionsServlet?courseId=" + courseId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
