<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Điểm Danh Học Sinh</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .error-message {
                color: red;
                margin-bottom: 1rem;
            }
            .success-message {
                color: green;
                margin-bottom: 1rem;
            }
            .table-container {
                max-height: 500px;
                overflow-y: auto;
            }
            textarea.form-control {
                resize: vertical;
                min-height: 80px;
                max-height: 200px;
            }
            
        </style>
    </head>
    <jsp:include page="layout/header.jsp" />
    <body class="bg-light py-4">
        <div class="container">
            <h2 class="mb-4">Điểm Danh Học Sinh</h2>
            <!-- Error/Success Messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger error-message">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success success-message">${success}</div>
            </c:if>

            <!-- Filter Form -->
            <form method="get" action="chuyen-can" class="row g-3 mb-4" id="filterForm">
                <div class="col-md-5">
                    <label for="courseId" class="form-label">Chọn Khóa Học</label>
                    <select class="form-select" id="courseId" name="courseId" required onchange="this.form.submit()">
                        <option value="">-- Chọn khóa học --</option>
                        <c:forEach var="c" items="${courses}">
                            <option value="${c.id}" <c:if test="${c.id == courseId}">selected</c:if>>${c.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-5">
                    <label for="sectionId" class="form-label">Chọn Buổi Học</label>
                    <select class="form-select" id="sectionId" name="sectionId" required>
                        <option value="">-- Chọn buổi học --</option>
                        <c:if test="${not empty sections}">
                            <c:forEach var="s" items="${sections}">
                                <option value="${s.id}" <c:if test="${s.id == sectionId}">selected</c:if>>
                                    ${s.formattedDate} - ${s.dayOfWeek.displayName} - ${s.formattedStartTime} - ${s.formattedEndTime}
                                </option>
                            </c:forEach>
                        </c:if>
                    </select>
                    <c:if test="${empty sections and not empty courseId}">
                        <small class="text-muted">Không có buổi học nào cho khóa học này.</small>
                    </c:if>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">Xem danh sách</button>
                </div>
            </form>

            <!-- Attendance Form -->
            <c:if test="${not empty students}">
                <form method="post" action="chuyen-can" onsubmit="return confirm('Bạn có chắc muốn lưu điểm danh?');">
                    <input type="hidden" name="action" value="submit">
                    <input type="hidden" name="courseId" value="${courseId}">
                    <input type="hidden" name="sectionId" value="${sectionId}">

                    <div class="table-container">
                        <table class="table table-bordered table-hover">
                            <thead class="table-light">
                                <tr>
                                    <th>STT</th>
                                    <th>Họ tên</th>
                                    <th>Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="student" items="${students}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>${student.studentName}</td>
                                        <td>
                                            <input type="hidden" name="studentIds" value="${student.studentId}" />
                                            <div class="btn-group" role="group">

                                                <input type="radio" class="btn-check" name="attendance_${student.studentId}"
                                                       id="present_${student.studentId}" value="present"
                                                       <c:if test="${attendanceMap[student.studentId] == 'present'}">checked</c:if> required>
                                                <label class="btn btn-outline-success" for="present_${student.studentId}">Có mặt</label>

                                                <input type="radio" class="btn-check" name="attendance_${student.studentId}"
                                                       id="absent_${student.studentId}" value="absent"
                                                       <c:if test="${attendanceMap[student.studentId] == 'absent'}">checked</c:if> required>
                                                <label class="btn btn-outline-danger" for="absent_${student.studentId}">Vắng</label>

                                                <input type="radio" class="btn-check" name="attendance_${student.studentId}"
                                                       id="excused_${student.studentId}" value="excused"
                                                       <c:if test="${attendanceMap[student.studentId] == 'excused'}">checked</c:if> required>
                                                <label class="btn btn-outline-warning" for="excused_${student.studentId}">Có phép</label>

                                                <input type="radio" class="btn-check" name="attendance_${student.studentId}"
                                                       id="notyet_${student.studentId}" value="notyet"
                                                       <c:if test="${attendanceMap[student.studentId] == 'notyet'}">checked</c:if> required>
                                                <label class="btn btn-outline-secondary" for="notyet_${student.studentId}">Chưa điểm danh</label>

                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="mb-3">
                        <label for="note" class="form-label">Ghi chú buổi học:</label>
                        <textarea name="note" id="note" class="form-control" rows="3" maxlength="500" placeholder="Nhập ghi chú cho buổi học (tối đa 500 ký tự)">${note}</textarea>
                    </div>
                    <div class="text-end mt-3">
                        <button type="submit" class="btn btn-primary">Lưu điểm danh</button>
                    </div>
                </form>

            </c:if>
            <c:if test="${empty students and not empty courseId and not empty sectionId}">
                <div class="alert alert-info">Không có học sinh nào trong buổi học này.</div>
            </c:if>
        </div>
        <c:if test="${not empty relatedRequests}">
            <h5 class="mt-4">Các yêu cầu liên quan đến buổi học</h5>
            <table class="table table-bordered table-striped">
                <thead class="table-light">
                    <tr>
                        <th>Người gửi</th>
                        <th>Loại yêu cầu</th>
                        <th>Nội dung</th>
                        <th>Trạng thái</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${relatedRequests}">
                        <tr>
                            <td>${r.requesterName}</td>
                            <td>${r.type}</td>
                            <td>${r.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${r.status == 'accepted'}">Chấp nhận</c:when>
                                    <c:when test="${r.status == 'rejected'}">Từ chối</c:when>
                                    <c:when test="${r.status == 'pending'}">Đang chờ</c:when>
                                    <c:otherwise>Không rõ</c:otherwise>
                                </c:choose>
                            </td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

    </body>
</html>
