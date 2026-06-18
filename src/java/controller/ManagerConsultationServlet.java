/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ConsultationDAO;
import dto.ConsultationDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Servlet quản lý các yêu cầu tư vấn của người dùng. Hỗ trợ các thao tác: xem
 * chi tiết, phê duyệt, tạo tài khoản từ yêu cầu, và hiển thị danh sách có lọc
 * theo tên và trạng thái.
 *
 * URL mapping dự kiến: /quan-ly-tu-van
 *
 * Các tham số hỗ trợ: - action = detail | approve | create | list - id: ID của
 * yêu cầu tư vấn khi dùng cho detail/approve/create - name: Tên người tư vấn
 * (dùng cho tìm kiếm) - filter: Trạng thái tư vấn (accepted, pending, rejected,
 * ...)
 *
 * Các view liên quan: - views/detailConsultation.jsp -
 * views/managerConsultation.jsp
 *
 * @author HanND
 */
public class ManagerConsultationServlet extends HttpServlet {

    /**
     * Xử lý các yêu cầu HTTP GET. Phân nhánh theo tham số "action" để thực hiện
     * các chức năng: - detail: hiển thị chi tiết yêu cầu tư vấn. - approve: cập
     * nhật trạng thái tư vấn thành "accepted". - create: chuyển hướng sang
     * trang tạo tài khoản với ID tư vấn. - list (mặc định): hiển thị danh sách
     * tư vấn kèm tìm kiếm/lọc theo tên và trạng thái.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        ConsultationDAO dao = new ConsultationDAO();

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "detail":
                int detailId = Integer.parseInt(request.getParameter("id"));
                ConsultationDTO c = dao.getConsultationById(detailId);
                request.setAttribute("consultation", c);
                request.getRequestDispatcher("views/detailConsultation.jsp").forward(request, response);
                return;

            case "approve":
                int approveId = Integer.parseInt(request.getParameter("id"));
                boolean updated = dao.updateStatus(approveId, ConsultationDTO.Status.accepted);
                response.sendRedirect("quan-ly-tu-van");
                return;

            case "create":
                int createId = Integer.parseInt(request.getParameter("id"));
                response.sendRedirect("tao-tai-khoan?consultationId=" + createId);
                return;
            case "list":
            default:
                String name = request.getParameter("name");
                String status = request.getParameter("status");

                List<ConsultationDTO> allList = dao.listAndSearchConsultations(name, status, null);

                List<ConsultationDTO> teacherList = allList.stream()
                        .filter(item -> item.getRole() == ConsultationDTO.Role.teacher)
                        .toList();

                List<ConsultationDTO> studentParentList = allList.stream()
                        .filter(item -> item.getRole() == ConsultationDTO.Role.parent )
                        .toList();

                request.setAttribute("teacherList", teacherList);
                request.setAttribute("studentParentList", studentParentList);
                request.setAttribute("name", name != null ? name : "");
                request.setAttribute("status", status != null ? status : "");

                String msg = request.getParameter("message");
                if ("success".equals(msg)) {
                    request.setAttribute("message", "Tạo tài khoản thành công.");
                }

                request.getRequestDispatcher("views/managerConsultation.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Mô tả ngắn gọn về servlet.
     *
     * @return Chuỗi mô tả servlet
     */
    @Override
    public String getServletInfo() {
        return "Servlet quản lý các yêu cầu tư vấn (ManagerConsultation)";
    }
}
