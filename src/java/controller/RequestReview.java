package controller;

import dao.CourseDAO;
import dao.RequestDAO;
import dao.SectionDAO;
import dao.StudentCourseDAO;
import dao.StudentDAO;
import dao.StudentPaymentScheduleDAO;
import dao.StudentSectionDAO;
import dto.RequestDTO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import modal.CourseModal;
import modal.RequestModal;
import modal.SectionModal;
import modal.StudentModal;

public class RequestReview extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            RequestDAO dao = new RequestDAO();
            List<RequestDTO> allRequests = dao.getAllRequests();

            int currentPage = getCurrentPage(request);
            int totalRecords = allRequests.size();
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            int startIdx = (currentPage - 1) * PAGE_SIZE;
            int endIdx = Math.min(startIdx + PAGE_SIZE, totalRecords);

            List<RequestDTO> paginatedRequests = allRequests.subList(startIdx, endIdx);

            request.setAttribute("requests", paginatedRequests);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);

            request.getRequestDispatcher("views/request_list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading requests");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idStr = request.getParameter("requestId");
        int requestId = Integer.parseInt(idStr);

        int currentPage = getCurrentPage(request);

        RequestDAO dao = new RequestDAO();
        CourseDAO cdao = new CourseDAO();

        try {

            if ("openModal".equals(action)) {
                RequestModal req = dao.getRequestById(requestId);

                // Get paginated requests for display
                List<RequestDTO> allRequests = dao.getAllRequests();
                int totalPages = (int) Math.ceil((double) allRequests.size() / PAGE_SIZE);
                List<RequestDTO> paginatedRequests = paginate(allRequests, currentPage, PAGE_SIZE);

                request.setAttribute("requests", paginatedRequests);
                request.setAttribute("selectedRequest", req);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);

                if (req != null && req.getType().name().equals("STUDENT_CHANGE_COURSE")) {
                    try {
                        Integer fromCourseId = req.getFromCourseId();
                        Integer toCourseId = req.getToCourseId();

                        if (fromCourseId != null) {
                            System.out.println(fromCourseId);
                            CourseModal from = cdao.getCourseById(fromCourseId);
                            request.setAttribute("fromCourse", from);
                        }
                        if (toCourseId != null) {
                            CourseModal to = cdao.getCourseById(toCourseId);
                            request.setAttribute("toCourse", to);
                        }

                    } catch (Exception e) {
                        System.out.println("Error loading course info: " + e.getMessage());
                        // Continue without course info - modal will still show basic request details
                    }
                }

                request.getRequestDispatcher("views/request_list.jsp").forward(request, response);

            } else if ("confirm".equals(action)) {
                String decision = request.getParameter("decision");
                if ("accepted".equals(decision)) {
                    dao.updateStatus(requestId, RequestModal.Status.accepted);
                    request.setAttribute("message", "Đã đồng ý yêu cầu");

                    RequestModal req = dao.getRequestById(requestId);
                    if (req != null && req.getType().name().equals("STUDENT_CHANGE_COURSE")) {
                        Integer accountId = req.getRequestBy();
                        StudentModal student = new StudentDAO().getStudentByAccountId(accountId);
                        Integer toCourseId = req.getToCourseId();

                        if (student.getId() != null && toCourseId != null) {
                            new StudentSectionDAO().deleteAllByStudentId(student.getId());
                            new StudentPaymentScheduleDAO().deleteUnpaidByStudentId(student.getId());
                            new StudentCourseDAO().deleteByStudentId(student.getId());

                            new StudentCourseDAO().createStudentCourse(student.getId(), toCourseId);

                            SectionDAO sectionDAO = new SectionDAO();
                            List<SectionModal> sections = sectionDAO.getSectionsByCourseId(toCourseId);
                            StudentSectionDAO studentSectionDAO = new StudentSectionDAO();
                            StudentPaymentScheduleDAO paymentDAO = new StudentPaymentScheduleDAO();

                            for (SectionModal section : sections) {
                                int studentSectionId = studentSectionDAO.createStudentSection(student.getId(), section.getId());

                                paymentDAO.createPaymentSchedule(studentSectionId, BigDecimal.valueOf(100000), section.getStartTime());
                            }
                        }
                    }
                } else {
                    dao.updateStatus(requestId, RequestModal.Status.rejected);
                    request.setAttribute("message", "Đã từ chối yêu cầu");
                }

                List<RequestDTO> allRequests = dao.getAllRequests();
                int totalPages = (int) Math.ceil((double) allRequests.size() / PAGE_SIZE);
                List<RequestDTO> paginatedRequests = paginate(allRequests, currentPage, PAGE_SIZE);

                request.setAttribute("requests", paginatedRequests);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);

                request.getRequestDispatcher("views/request_list.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xử lý lỗi");
        }
    }

    private int getCurrentPage(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        int page = 1;
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {
        }
        return page;
    }

    private List<RequestDTO> paginate(List<RequestDTO> list, int page, int pageSize) {
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        return list.subList(start, end);
    }

    @Override
    public String getServletInfo() {
        return "Xử lý danh sách yêu cầu với modal chi tiết và xử lý xác nhận.";
    }
}
