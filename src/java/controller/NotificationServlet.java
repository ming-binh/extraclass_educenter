/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.NotificationDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import modal.NotificationModal;

/**
 *
 * @author Minh Thu
 */
public class NotificationServlet extends HttpServlet {
    private NotificationDao notificationDao;
    @Override
    public void init() {
        notificationDao = new NotificationDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession(false);
        Integer accountId = (Integer) session.getAttribute("accountId");
        String role = (String) session.getAttribute("role");

        if (accountId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "view";

        switch (action) {
            case "delete":
                if ("admin".equals(role)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    notificationDao.deleteNotification(id);
                }
                response.sendRedirect("manage-notification");
                break;

            case "markAllRead":
                notificationDao.markAllAsReadByAccountId(accountId);
                response.sendRedirect("manage-notification");
                break;

            default:
                List<NotificationModal> list = notificationDao.getNotificationsByAccountId(accountId);
                request.setAttribute("notifications", list);
                request.getRequestDispatcher("/views/notification.jsp").forward(request, response);
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
         HttpSession session = request.getSession(false);
        Integer accountId = (Integer) session.getAttribute("accountId");
        String role = (String) session.getAttribute("role");

        if (accountId == null || role == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("create".equals(action) && "admin".equals(role)) {
            String description = request.getParameter("description");
            String[] userIds = request.getParameterValues("userIds");

            if (userIds != null && userIds.length > 0) {
                List<Integer> targets = new ArrayList<>();
                for (String id : userIds) {
                    targets.add(Integer.parseInt(id));
                }
                notificationDao.addNotificationToMultipleUsers(targets, description);
            } else {
                notificationDao.addNotification(accountId, description);
            }
            response.sendRedirect("manage-notification");
        } else if ("markRead".equals(action)) {
            int id = Integer.parseInt(request.getParameter("notificationId"));
            notificationDao.markAsRead(id);
            response.sendRedirect("manage-notification");
        } else {
            response.sendRedirect("manage-notification");
        }
    }
    }
