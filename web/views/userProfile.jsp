<%-- 
    Document   : userProfile
    Created on : Jun 25, 2025, 11:32:11 AM
    Author     : vankhoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<%
    String role = (String) request.getAttribute("loggedInUserRole");
    request.setAttribute("role", role);
    String name = (String) request.getAttribute("loggedInUserName");
    request.setAttribute("userName", name);
%> 

<c:choose>
    <c:when test="${role=='student' or role == 'parent' or role=='teacher'}">
        <jsp:include page="layout/header.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="layout/adminHeader.jsp" />
    </c:otherwise>
</c:choose>

<div class="container-fluid">
    <c:if test="${role != 'student' and role!= 'parent' and role !='teacher'}">
        <div class="db-breadcrumb">
            <h4 class="breadcrumb-title">Thông tin cá nhân</h4>
            <ul class="db-breadcrumb-list">
                <li><a href="bang-dieu-khien"><i class="fa fa-home"></i>Bảng điều khiển</a></li>
                <li>Thông tin cá nhân</li>
            </ul>
        </div>
    </c:if>

    <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 m-b30">
            <div class="widget-box form-wrapper">
                <div class="wc-title">
                    <h4>Thông tin cá nhân</h4>
                </div>
                <div class="widget-inner">
                    <div class="edit-profile">
                        <c:if test="${loggedInUserRole != 'admin'}">
<!--                            <div class="row">
                                <div class="col-12">
                                    <div class="text-center mb-4">
                                        <div class="position-relative d-inline-block">
                                             Avatar hiện tại 
                                            <c:set var="avatarPath" value="${empty account.avatarURL ? '/assets/ava.png' : account.avatarURL}" />
                                            <img id="avatarPreview" src="${pageContext.request.contextPath}${avatarPath}" 
                                                 class="rounded-circle shadow" alt="Avatar"
                                                 style="width: 150px; height: 150px; object-fit: cover;">

                                             Loading khi đang upload 
                                            <div id="avatarLoading" style="display: none; position: absolute; top: 0; left: 0; right: 0; bottom: 0;
                                                 background: rgba(255,255,255,0.7); display: flex; justify-content: center; align-items: center;">
                                                <div class="spinner-border text-primary" role="status">
                                                    <span class="visually-hidden">Loading...</span>
                                                </div>
                                            </div>
                                        </div>

                                         Nút chọn ảnh 
                                        <form id="avatarForm" method="post" action="${pageContext.request.contextPath}/upload-avatar"
                                              enctype="multipart/form-data" class="mt-3">
                                            <input type="file" name="avatar" id="avatarInput" class="d-none" accept="image/jpeg,image/png">
                                            <button type="button" class="btn btn-outline-primary btn-sm" onclick="document.getElementById('avatarInput').click();">
                                                <i class="fas fa-camera me-2"></i> Đổi ảnh
                                            </button>
                                        </form>
                                        <div class="small text-muted mt-1">Chỉ hỗ trợ JPG, PNG (tối đa 2MB)</div>
                                    </div>
                                </div>
                            </div>-->

                        </c:if>
                        <form class="userProfile" id="profileForm" method="post" action="userProfile">
                            <input type="hidden" name="action" value="updateProfile">
                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Họ và tên</label>
                                        <div class="col-sm-9">
                                            <input class="form-control" type="text" name="fullName" value="${currentAccount.getName()}" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Số điện thoại</label>
                                        <div class="col-sm-9">
                                            <input class="form-control" type="text" name="phone" value="${currentAccount.getPhone()}" >
                                        </div>
                                    </div>
                                </div>

                                <c:if test="${role=='student'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Lớp</label>
                                            <div class="col-sm-9">
                                                <input class="form-control" type="number" name="grade" min="1" max="12" value="${user.getCurrentGrade()}">
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${role=='student'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Trường đang học</label>
                                            <div class="col-sm-9">
                                                <input class="form-control" type="text" name="schoolName" value="${user.getSchoolName()}" readonly>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${role=='teacher'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Trường đang dạy</label>
                                            <div class="col-sm-9">
                                                <input class="form-control" type="text" name="schoolName" value="${user.getSchoolName()}" readonly>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${role!='admin'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Ngày sinh</label>
                                            <div class="col-sm-9">
                                                <input class="form-control" type="date" name="dateOfBirth"
                                                       value="${currentAccount.dob != null ? currentAccount.dob.toLocalDate() : ''}">

                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${role=='teacher'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Kinh nghiệm</label>
                                            <div class="col-sm-9">
                                                <input class="form-control" type="text" name="experience" value="${user.getExperience()}" >

                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <div class="col-12">
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Địa chỉ</label>
                                        <div class="col-sm-9">
                                            <input class="form-control" type="text" name="address" value="${currentAccount.getAddress()}">

                                        </div>
                                    </div>
                                </div>
                                <c:if test="${role=='teacher'}">
                                    <div class="col-12">
                                        <div class="form-group row">
                                            <label class="col-sm-3 col-form-label">Tiểu sử </label>
                                            <div class="col-sm-9">
                                                <textarea class="form-control" name="bio" rows="2">${user.getBio()}</textarea>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${role == 'student' or role == 'teacher'}">
                                    <input type="hidden" name="schoolId" value="${user.getSchoolId()}">
                                </c:if>


                                <div class="col-12">
                                    <div class="form-group row">
                                        <div class="col-sm-9 offset-sm-3 d-flex justify-content-between">
                                            <button type="button" class="btn btn-primary" id="updateProfileBtn">Cập nhật thông tin</button>
                                            <a href="doi-mat-khau"><button type="button" class="btn btn-secondary" id="">Đổi mật khẩu</button></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>



                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    /* ========== MODAL STYLES ========== */
    .custom-modal {
        display: none;
        position: fixed;
        z-index: 1050;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgba(0, 0, 0, 0.4);
    }

    .custom-modal-content {
        position: relative;
        width: 90%;
        max-width: 500px;
        margin: 10% auto;
        padding: 2rem;
        background-color: #fff;
        border-radius: 1rem;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
    }

    .custom-close {
        position: absolute;
        top: 15px;
        right: 20px;
        font-size: 24px;
        font-weight: bold;
        color: #aaa;
        cursor: pointer;
        transition: color 0.2s ease;
    }

    .custom-close:hover {
        color: #ff0000;
    }

    /* ========== FORM CONTAINER ========== */
    .form-wrapper {
        padding: 2rem;
        margin-top: 2rem;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
    }

    /* ========== FORM ELEMENTS ========== */
    .form-group {
        margin-bottom: 1.5rem;
    }

    .form-control {
        padding: 10px 15px;
        border-radius: 5px;
        transition: all 0.3s ease;
    }

    .form-control:focus {
        border-color: #ff8814;
        box-shadow: 0 0 0 0.2rem rgba(255, 136, 20, 0.25);
    }

    /* Readonly fields */
    .edit-profile-form input[readonly],
    .edit-profile-form textarea[readonly] {
        background-color: #f8f9fa;
        cursor: not-allowed;
    }

    /* Editable fields */
    .edit-profile-form input:not([readonly]),
    .edit-profile-form textarea:not([readonly]) {
        background-color: #fff;
        border-color: #80bdff;
        box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
    }

    /* ========== VALIDATION STYLES ========== */
    .error-message {
        display: block;
        margin-top: 5px;
        font-size: 0.875rem;
        color: #dc3545;
    }

    .is-invalid {
        border-color: #dc3545 !important;
        background-color: #fff5f5 !important;
    }

    .is-invalid:focus {
        box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
    }

    /* ========== BUTTON STYLES ========== */
    .btn {
        transition: all 0.3s ease;
    }

    .btn-primary {
        color: #fff;
        background-color: #ff8814;
        border-color: #ff8814;
    }

    .btn-primary:hover {
        background-color: #db4b24;
        border-color: #db4b24;
    }

    .btn:disabled {
        opacity: 0.65;
        cursor: not-allowed;
    }

    /* ========== PROFILE PICTURE ========== */
    .profile-pic {
        text-align: center;
        margin-bottom: 1.5rem;
    }

    .profile-pic img {
        width: 150px;
        height: 150px;
        object-fit: cover;
        border: 3px solid #fff;
        border-radius: 50%;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
    }

    .profile-pic:hover img {
        transform: scale(1.03);
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
    }

    #changeAvatarBtn {
        margin-top: 10px;
        padding: 0.375rem 0.75rem;
        background-color: #f8f9fa;
        border: 1px solid #dee2e6;
        color: #495057;
        border-radius: 0.25rem;
    }

    #changeAvatarBtn:hover {
        background-color: #e9ecef;
    }
    .avatar-container {
        display: inline-block;
        position: relative; /* Cần thiết để loading indicator căn giữa */
    }

    /* Style cho loading indicator */
    .avatar-loading {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(255,255,255,0.7); /* Màu nền mờ */
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%; /* Bo tròn để match với ảnh */
    }

    /* Hiệu ứng khi hover vào ảnh */
    #avatarPreview {
        transition: all 0.3s ease;
        cursor: pointer;
    }

    #avatarPreview:hover {
        transform: scale(1.05); /* Phóng nhẹ ảnh */
        box-shadow: 0 0 15px rgba(0,0,0,0.2); /* Đổ bóng */
    }
</style>

<script>
//    const avatarInput = document.getElementById('avatarInput');
//    const avatarForm = document.getElementById('avatarForm');
//    const avatarLoading = document.getElementById('avatarLoading');
//
//    avatarInput.addEventListener('change', function () {
//        if (this.files && this.files[0]) {
//            avatarLoading.style.display = 'flex';
//            avatarForm.submit();
//        }
//    });
    document.addEventListener('DOMContentLoaded', function () {
        // DOM Elements
        const profileForm = document.getElementById('profileForm');
        const updateProfileBtn = document.getElementById('updateProfileBtn');
        const changeAvatarBtn = document.getElementById('changeAvatarBtn');
        const avatarInput = document.getElementById('avatarInput');
        // Get user role from JSTL
        const userRole = "${role}";
        // State
        let isEditMode = false;
        // ========== VALIDATION FUNCTIONS ==========

        // Phone number validation
        const validatePhoneNumber = (phone) => {
            if (!phone)
                return {isValid: false, message: "Vui lòng nhập số điện thoại"};
            if (!/^0\d{9}$/.test(phone))
                return {isValid: false, message: "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số"};
            return {isValid: true};
        };
        // Date of birth validation
        const validateDateOfBirth = (dobString, minAge) => {
            if (!dobString)
                return {isValid: false, message: "Vui lòng nhập ngày sinh"};
            const dob = new Date(dobString);
            const today = new Date();
            let age = today.getFullYear() - dob.getFullYear();
            const monthDiff = today.getMonth() - dob.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dob.getDate())) {
                age--;
            }

            if (age < minAge) {
                return {
                    isValid: false,
                    message: minAge === 18
                            ? "Giáo viên phải từ 18 tuổi trở lên"
                            : "Học sinh phải từ 6 tuổi trở lên"
                };
            }

            return {isValid: true};
        };
        // Grade validation (1-12)
        const validateGrade = (grade) => {
            if (!grade)
                return {isValid: false, message: "Vui lòng nhập lớp"};
            if (grade < 1 || grade > 12)
                return {isValid: false, message: "Lớp phải từ 1 đến 12"};
            return {isValid: true};
        };
        // Experience validation
        const validateExperience = (exp, dobString) => {
            const expValue = parseInt(exp) || 0;
            if (isNaN(expValue))
                return {isValid: false, message: "Vui lòng nhập số năm kinh nghiệm hợp lệ"};
            if (expValue <= 0)
                return {isValid: false, message: "Kinh nghiệm phải lớn hơn 0 năm"};
            if (dobString) {
                const dob = new Date(dobString);
                const today = new Date();
                let age = today.getFullYear() - dob.getFullYear();
                const monthDiff = today.getMonth() - dob.getMonth();
                if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dob.getDate())) {
                    age--;
                }

                if (age < 18 + expValue) {
                    return {
                        isValid: false,
                        message: `Kinh nghiệm không phù hợp với năm sinh`
                    };
                }
            }

            if (expValue > 50)
                return {isValid: false, message: "Kinh nghiệm không thể quá 50 năm"};
            return {isValid: true};
        };
        // ========== ERROR HANDLING ==========

        // Show error message
        const showError = (input, message) => {
            const formGroup = input.closest('.form-group');
            if (!formGroup)
                return;
            let errorElement = formGroup.querySelector('.error-message');
            if (!errorElement) {
                errorElement = document.createElement('small');
                errorElement.className = 'error-message';
                formGroup.appendChild(errorElement);
            }

            errorElement.textContent = message;
            input.classList.add('is-invalid');
        };
        // Remove error message
        const removeError = (input) => {
            const formGroup = input.closest('.form-group');
            if (!formGroup)
                return;
            const errorElement = formGroup.querySelector('.error-message');
            if (errorElement) {
                formGroup.removeChild(errorElement);
            }

            input.classList.remove('is-invalid');
        };
        // ========== FORM VALIDATION ===========

        const validateForm = () => {
            let isFormValid = true;
            // Common validations for all roles
            const phoneInput = profileForm?.querySelector('[name="phone"]');
            if (phoneInput && !phoneInput.readOnly) {
                const validation = validatePhoneNumber(phoneInput.value);
                if (!validation.isValid) {
                    showError(phoneInput, validation.message);
                    isFormValid = false;
                } else {
                    removeError(phoneInput);
                }
            }

            // Role-specific validations
            if (userRole === 'student') {
                // Validate grade
                const gradeInput = profileForm?.querySelector('[name="grade"]');
                if (gradeInput && !gradeInput.readOnly) {
                    const validation = validateGrade(gradeInput.value);
                    if (!validation.isValid) {
                        showError(gradeInput, validation.message);
                        isFormValid = false;
                    } else {
                        removeError(gradeInput);
                    }
                }

                // Validate date of birth
                const dobInput = profileForm?.querySelector('[name="dateOfBirth"]');
                if (dobInput && !dobInput.readOnly) {
                    const validation = validateDateOfBirth(dobInput.value, 6);
                    if (!validation.isValid) {
                        showError(dobInput, validation.message);
                        isFormValid = false;
                    } else {
                        removeError(dobInput);
                    }
                }
            }

            if (userRole === 'teacher') {
                // Validate date of birth
                const dobInput = profileForm?.querySelector('[name="dateOfBirth"]');
                const experienceInput = profileForm?.querySelector('[name="experience"]');
                if (dobInput && !dobInput.readOnly) {
                    const validation = validateDateOfBirth(dobInput.value, 18);
                    if (!validation.isValid) {
                        showError(dobInput, validation.message);
                        isFormValid = false;
                    } else {
                        removeError(dobInput);
                        // Validate experience
                        if (experienceInput && !experienceInput.readOnly) {
                            const validationExp = validateExperience(experienceInput.value, dobInput.value);
                            if (!validationExp.isValid) {
                                showError(experienceInput, validationExp.message);
                                isFormValid = false;
                            } else {
                                removeError(experienceInput);
                            }
                        }
                    }
                }
            }

            // Update button text if form is valid
            if (isFormValid) {
                updateProfileBtn.textContent = 'Lưu thay đổi';
            } else {
                updateProfileBtn.textContent = 'Cập nhật thông tin';
            }

            return isFormValid;
        };
        // ========== EVENT HANDLERS ==========

        const toggleEditMode = () => {
            isEditMode = !isEditMode;
            // Fields that are editable based on role
            const editableFields = {
                'student': ['[name="grade"]', '[name="dateOfBirth"]', '[name="address"]'],
                'teacher': ['[name="dateOfBirth"]', '[name="address"]', '[name="bio"]'],
                'parent': ['[name="dateOfBirth"]', '[name="address"]'],
                'admin': ['[name="address"]']
            };
            // Toggle readonly for relevant fields
            (editableFields[userRole] || []).forEach(selector => {
                const input = profileForm?.querySelector(selector);
                if (input) {
                    input.readOnly = !isEditMode;
                    removeError(input);
                }
            });
            // Reset button state
            updateProfileBtn.textContent = 'Cập nhật thông tin';
            updateProfileBtn.disabled = false;
            if (isEditMode) {
                validateForm();
            }
        };
        // ========== EVENT LISTENERS ==========

        updateProfileBtn?.addEventListener('click', function () {
            if (!isEditMode) {
                toggleEditMode();
            } else {
                const isValid = validateForm();
                if (isValid) {
                    profileForm.submit();
                } else {
                    alert('Vui lòng kiểm tra lại các thông tin chưa hợp lệ trước khi cập nhật');
                }
            }
        });
        // Real-time validation for date of birth changes
        const dobInput = profileForm?.querySelector('[name="dateOfBirth"]');
        if (dobInput) {
            dobInput.addEventListener('change', function () {
                if (userRole === 'teacher' && isEditMode) {
                    validateForm();
                }
            });
        }

        // Real-time validation for experience changes
        const experienceInput = profileForm?.querySelector('[name="bio"]');
        if (experienceInput) {
            experienceInput.addEventListener('input', function () {
                if (userRole === 'teacher' && isEditMode) {
                    validateForm();
                }
            });
        }

        changeAvatarBtn?.addEventListener('click', () => avatarInput.click());
        // Initialize
        validateForm();
    });
</script>