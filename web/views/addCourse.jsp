<%-- 
    Document   : addCourse
    Created on : Jun 26, 2025, 1:30:35 AM
    Author     : HanND
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/adminHeader.jsp" />

<div class="container mt-4">
    <h4 class="mb-4"><i class="fas fa-plus-circle text-success me-2"></i>Thêm khóa học mới</h4>
    <form id="courseForm" action="quan-ly-khoa-hoc?action=add" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
        <div class="form-group mb-3">
            <label for="courseImg" class="form-label">Ảnh khóa học</label>
            <input type="file" class="form-control form-control-sm" name="course_img" id="courseImg" accept="image/*">
        </div>

        <div class="mb-3">
            <label class="form-label">Tên khoá học</label>
            <input id="courseName" name="name" class="form-control form-control-sm" required>
            <span id="dupMsg" class="text-danger small" style="display:none">
                Tên khóa học đã tồn tại.
            </span>
        </div>

        <div class="mb-3">
            <label class="form-label">Giáo viên phụ trách</label>
            <select name="teacherId" class="form-select form-select-sm" required>
                <c:forEach var="t" items="${teacherList}">
                    <option value="${t.id}">
                        ${t.name}
                        (
                        <c:choose>
                            <c:when test="${t.subject == 'Mathematics'}">Toán</c:when>
                            <c:when test="${t.subject == 'Literature'}">Ngữ văn</c:when>
                            <c:when test="${t.subject == 'English'}">Tiếng Anh</c:when>
                            <c:when test="${t.subject == 'Physics'}">Vật lý</c:when>
                            <c:when test="${t.subject == 'Chemistry'}">Hóa học</c:when>
                            <c:when test="${t.subject == 'Biology'}">Sinh học</c:when>
                            <c:when test="${t.subject == 'History'}">Lịch sử</c:when>
                            <c:when test="${t.subject == 'Geography'}">Địa lý</c:when>
                            <c:when test="${t.subject == 'Civic Education'}">GDCD</c:when>
                            <c:when test="${t.subject == 'Informatics'}">Tin học</c:when>
                            <c:otherwise>Khác</c:otherwise>
                        </c:choose>
                        )
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Môn học</label>
            <select name="subject" class="form-select form-select-sm" required readonly>
                <option value="" selected>--</option>
                <option value="Mathematics">Toán</option>
                <option value="Literature">Ngữ văn</option>
                <option value="English">Tiếng Anh</option>
                <option value="Physics">Vật lý</option>
                <option value="Chemistry">Hóa học</option>
                <option value="Biology">Sinh học</option>
                <option value="History">Lịch sử</option>
                <option value="Geography">Địa lý</option>
                <option value="Civic Education">GDCD</option>
                <option value="Informatics">Tin học</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Khối</label>
            <input type="text" class="form-control form-control-sm" name="grade" required readonly>
        </div>

        <div class="mb-3">
            <label class="form-label">Mô tả</label>
            <textarea name="description" class="form-control form-control-sm" placeholder="Mô tả ngắn gọn..."></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Trạng thái</label>
            <select name="status" class="form-select form-select-sm" required>
                <option value="activated">Đang hoạt động</option>
                <option value="pending">Chờ duyệt</option>
                <option value="upcoming">Sắp diễn ra</option>
                <option value="rejected">Từ chối</option>
                <option value="inactivated">Tạm ngưng</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Loại khóa học</label>
            <select name="courseType" id="courseType" class="form-select form-select-sm" onchange="toggleFeeFields()" required>
                <option value="combo">Trọn gói</option>
                <option value="daily">Theo buổi</option>
            </select>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Học phí trọn gói</label>
                <input type="number" class="form-control form-control-sm" name="feeCombo" id="feeCombo" min="0" step="0.01" placeholder="VD: 500000">
            </div>
            <div class="col-md-6">
                <label class="form-label">Học phí theo buổi</label>
                <input type="number" class="form-control form-control-sm" name="feeDaily" id="feeDaily" min="0" step="0.01" placeholder="VD: 80000">
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Ngày bắt đầu</label>
                <input type="date" class="form-control form-control-sm" name="startDate" id="startDate" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Ngày kết thúc</label>
                <input type="date" class="form-control form-control-sm" name="endDate" id="endDate" required readonly>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Số tuần học</label>
                <input type="number" class="form-control form-control-sm" name="weekAmount" id="weekAmount" min="1" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Sĩ số hiện tại</label>
                <input type="number" class="form-control form-control-sm" name="studentEnrollment" id="studentEnrollment" min="0" value="0" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Sĩ số tối đa</label>
                <input type="number" class="form-control form-control-sm" name="maxStudents" id="maxStudents" min="1" max="31" required>
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Trình độ</label>
            <select name="level" class="form-select form-select-sm" required>
                <option value="Foundation">Nhập môn</option>
                <option value="Basic">Cơ bản</option>
                <option value="Advanced">Nâng cao</option>
                <option value="Excellent">Xuất sắc</option>
                <option value="Topics_Exam">Chuyên đề / Luyện thi</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Khóa học nổi bật</label>
            <select name="isHot" class="form-select form-select-sm" required>
                <option value="true">Có</option>
                <option value="false" selected>Không</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Giảm giá (%)</label>
            <input type="number" class="form-control form-control-sm" name="discountPercentage" id="discountPercentage" min="0" max="100" step="0.01" placeholder="VD: 10" onblur="formatDiscount()">
        </div>
        <hr class="my-4">
        <h5>
            <i class="fas fa-calendar-plus text-primary me-2"></i>
            Lịch học (Tuần 1 buổi)
            <button type="button" class="btn btn-sm btn-outline-primary" onclick="toggleScheduleForm()">
                <i class="fas fa-plus"></i>
            </button>
        </h5>

        <div id="scheduleForm">
            <div class="row mb-3">
                <div class="col-md-4">
                    <label class="form-label">Ngày học</label>
                    <select name="dayOfWeek" class="form-select form-select-sm" required>
                        <option value="Monday">Thứ 2</option>
                        <option value="Tuesday">Thứ 3</option>
                        <option value="Wednesday">Thứ 4</option>
                        <option value="Thursday">Thứ 5</option>
                        <option value="Friday">Thứ 6</option>
                        <option value="Saturday">Thứ 7</option>
                        <option value="Sunday">Chủ nhật</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Giờ bắt đầu</label>
                    <input type="time" class="form-control form-control-sm" name="startTime" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Giờ kết thúc</label>
                    <input type="time" class="form-control form-control-sm" name="endTime" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Phòng học</label>
                <select name="classroom" class="form-select form-select-sm" required>
                    <c:forEach var="room" items="${roomList}">
                        <option value="${room.id}">
                            ${room.roomName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-success">Thêm khóa học</button>
            <a href="quan-ly-khoa-hoc" class="btn btn-secondary">Hủy</a>
    </form>
</div>
<script>
    let tenBiTrung = false;

    document.addEventListener('DOMContentLoaded', () => {
        document.getElementById('courseName').addEventListener('blur', kiemTraTrung);
        document.getElementById('courseName').addEventListener('input', goiYNoiDung);
        document.getElementById('startDate').addEventListener('change', calculateEndDate);
        document.getElementById('weekAmount').addEventListener('input', calculateEndDate);
        document.getElementById('maxStudents').addEventListener('input', function () {
            const value = parseInt(this.value);
            if (value > 31) {
                alert('Sĩ số tối đa không được vượt quá 31 học sinh.');
                this.value = 31;
            }
        });
        //số học sinh hiện tại
        document.getElementById('studentEnrollment').addEventListener('input', function () {
            const value = parseInt(this.value);
            if (value !== 0) {
                alert('Sĩ số hiện tại chỉ được là 0.');
                this.value = 0;
            }
        });
        // phần trăm giảm giá
        document.getElementById('discountPercentage').addEventListener('input', function () {
            let val = parseFloat(this.value);
            if (val > 100) {
                alert('Giảm giá tối đa là 100%');
                this.value = 100;
            } else if (val < 0) {
                alert('Giảm giá không thể nhỏ hơn 0%');
                this.value = 0;
            }
        });

        toggleFeeFields();
    });
    function kiemTraTrung() {
        const oTen = document.getElementById('courseName');
        const canhBao = document.getElementById('dupMsg');
        const ten = oTen.value.trim();
        if (ten === '') {
            canhBao.style.display = 'none';
            tenBiTrung = false;
            return;
        }

        fetch('quan-ly-khoa-hoc?action=checkDuplicate&name=' + encodeURIComponent(ten))
                .then(res => res.json())
                .then(data => {
                    tenBiTrung = data.exists;
                    canhBao.style.display = tenBiTrung ? 'block' : 'none';
                });
    }

    function validateForm() {
        if (tenBiTrung) {
            alert('Tên khóa học đã tồn tại, vui lòng nhập tên khác.');
            return false;
        }

        const courseType = document.getElementById('courseType').value;
        const feeCombo = parseFloat(document.getElementById('feeCombo').value) || 0;
        const feeDaily = parseFloat(document.getElementById('feeDaily').value) || 0;
        const startDate = new Date(document.getElementById('startDate').value);
        const endDate = new Date(document.getElementById('endDate').value);
        const weekAmount = parseInt(document.getElementById('weekAmount').value);
        const studentEnrollment = parseInt(document.getElementById('studentEnrollment').value);
        const maxStudents = parseInt(document.getElementById('maxStudents').value);
        const discount = parseFloat(document.getElementById('discountPercentage').value);
        if (courseType === 'combo' && feeCombo <= 0) {
            alert('Vui lòng nhập học phí trọn gói hợp lệ.');
            document.getElementById('feeCombo').focus();
            return false;
        }

        if (courseType === 'daily' && feeDaily <= 0) {
            alert('Vui lòng nhập học phí theo buổi hợp lệ.');
            document.getElementById('feeDaily').focus();
            return false;
        }

        if (startDate >= endDate) {
            alert("Ngày kết thúc phải sau ngày bắt đầu.");
            return false;
        }

        if (isNaN(weekAmount) || weekAmount < 1 || isNaN(maxStudents) || maxStudents < 1 || maxStudents > 31 || studentEnrollment < 0) {
            alert("Thông tin sĩ số hoặc số tuần không hợp lệ. (Tối đa 31 học sinh)");
            return false;
        }

        if (studentEnrollment > maxStudents) {
            alert("Sĩ số hiện tại không được vượt quá sĩ số tối đa.");
            return false;
        }

        if (!isNaN(discount) && (discount < 0 || discount > 100)) {
            alert("Phần trăm giảm giá phải từ 0 đến 100.");
            return false;
        }


        return true;
    }

    function toggleFeeFields() {
        const type = document.getElementById('courseType').value;
        document.getElementById('feeCombo').disabled = type !== 'combo';
        document.getElementById('feeDaily').disabled = type !== 'daily';
    }

    function formatDiscount() {
        const input = document.getElementById('discountPercentage');
        if (input.value && !input.value.includes('.')) {
            input.value = input.value + '.0';
        }
    }

    function calculateEndDate() {
        const startDate = new Date(document.getElementById('startDate').value);
        const weeks = parseInt(document.getElementById('weekAmount').value);
        if (!isNaN(weeks)) {
            const endDate = new Date(startDate);
            endDate.setDate(startDate.getDate() + (weeks * 7));
            document.getElementById('endDate').value = endDate.toISOString().split('T')[0];
        }
    }

    function toggleScheduleForm() {
        const form = document.getElementById('scheduleForm');
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
    }

    function goiYNoiDung() {
        const name = this.value.toLowerCase();
        const subjectSelect = document.querySelector('select[name="subject"]');
        const gradeInput = document.querySelector('input[name="grade"]');
        const subjectMap = {
            'toán': 'Mathematics',
            'ngữ văn': 'Literature',
            'văn': 'Literature',
            'tiếng anh': 'English',
            'anh': 'English',
            'vật lý': 'Physics',
            'lý': 'Physics',
            'hóa': 'Chemistry',
            'sinh': 'Biology',
            'lịch sử': 'History',
            'địa': 'Geography',
            'gdcd': 'Civic Education',
            'tin': 'Informatics'
        };
        for (const key in subjectMap) {
            if (name.includes(key)) {
                subjectSelect.value = subjectMap[key];
                break;
            }
        }

        const gradeMatch = name.match(/lớp\s*(\d{1,2})/);
        if (gradeMatch) {
            gradeInput.value = gradeMatch[1];
        }
    }
</script>

