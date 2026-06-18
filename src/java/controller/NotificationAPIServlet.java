package api;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */



import dao.NotificationDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import modal.NotificationModal;

/**
 *
 * @author Minh Thu
 */
public class NotificationAPIServlet extends HttpServlet {
   private NotificationDao dao;

    @Override
    public void init() {
        dao = new NotificationDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
    Integer accountId = (Integer) session.getAttribute("accountId");

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    if (accountId == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print("{\"error\":\"Unauthorized\"}");
        return;
    }

    try {
        List<NotificationModal> notis = dao.getNotificationsByAccountId(accountId);
        int unreadCount = dao.countUnreadNotifications(accountId);

        // Bắt đầu JSON object
        out.print("{");
        out.print("\"notifications\":[");

        for (int i = 0; i < notis.size(); i++) {
            NotificationModal n = notis.get(i);
            out.print("{");
            out.print("\"id\":" + n.getId() + ",");
            out.print("\"description\":\"" + n.getDescription().replace("\"", "\\\"") + "\",");
            out.print("\"createdAt\":\"" + n.getCreatedAt() + "\",");
            out.print("\"read\":" + n.getIsRead());
            out.print("}");

            if (i < notis.size() - 1) {
                out.print(",");
            }
        }

        out.print("],");
        out.print("\"unreadCount\":" + unreadCount);
        out.print("}");

    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"error\":\"Server error\"}");
    }
    
    } 

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }

    

}
