-- =============================================
-- DATABASE: ECS (ExtraClass EduCenter System)
-- Engine: Microsoft SQL Server
-- Tái tạo từ Model/DAO files của dự án Java
-- =============================================

-- Tạo database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'ECS')
BEGIN
    CREATE DATABASE ECS;
END
GO

USE ECS;
GO

-- =============================================
-- 1. BẢNG ACCOUNT (Tài khoản người dùng)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'account')
CREATE TABLE account (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    name            NVARCHAR(255)   NOT NULL,
    username        NVARCHAR(100)   NULL,
    phone           NVARCHAR(20)    NULL,
    password        NVARCHAR(255)   NOT NULL,
    dob             DATETIME2       NULL,
    address         NVARCHAR(500)   NULL,
    avatarURL       NVARCHAR(500)   NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'active',       -- active, inactive, banned
    role            NVARCHAR(20)    NOT NULL DEFAULT 'student',      -- admin, manager, staff, student, teacher, parent
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- 2. BẢNG SCHOOL (Trường học)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'school')
CREATE TABLE school (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    name            NVARCHAR(255)   NOT NULL,
    address         NVARCHAR(500)   NULL,
    phone           NVARCHAR(20)    NULL,
    schoolType      NVARCHAR(20)    NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NULL
);
GO

-- =============================================
-- 3. BẢNG SCHOOL_CLASS (Lớp học tại trường)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'school_class')
CREATE TABLE school_class (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    schoolId        INT             NOT NULL,
    className       NVARCHAR(100)   NOT NULL,
    grade           NVARCHAR(20)    NULL,
    academic_year   NVARCHAR(20)    NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_school_class_school FOREIGN KEY (schoolId) REFERENCES school(id)
);
GO

-- =============================================
-- 4. BẢNG PARENT (Phụ huynh)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'parent')
CREATE TABLE parent (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    accountId       INT             NOT NULL,
    relationship    NVARCHAR(20)    NULL,
    job             NVARCHAR(255)   NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_parent_account FOREIGN KEY (accountId) REFERENCES account(id)
);
GO

-- =============================================
-- 5. BẢNG TEACHER (Giáo viên)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'teacher')
CREATE TABLE teacher (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    accountId       INT             NOT NULL,
    schoolId        INT             NULL,
    schoolClassId   INT             NULL,
    experience      NVARCHAR(500)   NULL,
    subject         NVARCHAR(50)    NULL,       -- Math, Physics, Chemistry, Biology, English, Literature, History, Geography, IT
    bio             NVARCHAR(MAX)   NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_teacher_account FOREIGN KEY (accountId) REFERENCES account(id),
    CONSTRAINT FK_teacher_school FOREIGN KEY (schoolId) REFERENCES school(id)
);
GO

-- =============================================
-- 6. BẢNG STUDENT (Học sinh)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'student')
CREATE TABLE student (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    accountId       INT             NOT NULL,
    schoolId        INT             NULL,
    schoolClassId   INT             NULL,
    parentId        INT             NULL,
    currentGrade    NVARCHAR(20)    NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_student_account FOREIGN KEY (accountId) REFERENCES account(id),
    CONSTRAINT FK_student_school FOREIGN KEY (schoolId) REFERENCES school(id),
    CONSTRAINT FK_student_school_class FOREIGN KEY (schoolClassId) REFERENCES school_class(id),
    CONSTRAINT FK_student_parent FOREIGN KEY (parentId) REFERENCES parent(id)
);
GO

-- =============================================
-- 7. BẢNG COURSE (Khóa học)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'course')
CREATE TABLE course (
    id                  INT IDENTITY(1,1) PRIMARY KEY,
    course_img          NVARCHAR(500)   NULL,
    teacherId           INT             NULL,
    name                NVARCHAR(255)   NOT NULL,
    description         NVARCHAR(MAX)   NULL,
    status              NVARCHAR(20)    NOT NULL DEFAULT 'pending',     -- pending, activated, upcoming, deactivated, completed
    courseType           NVARCHAR(20)    NOT NULL DEFAULT 'daily',       -- combo, daily
    feeCombo            DECIMAL(18,2)   NULL,
    feeDaily            DECIMAL(18,2)   NULL,
    startDate           DATETIME2       NULL,
    endDate             DATETIME2       NULL,
    weekAmount          INT             DEFAULT 0,
    studentEnrollment   INT             DEFAULT 0,
    maxStudents         INT             DEFAULT 0,
    level               NVARCHAR(20)    NULL,       -- basic, intermediate, advanced
    isHot               BIT             DEFAULT 0,
    subject             NVARCHAR(50)    NULL,       -- Math, Physics, Chemistry, Biology, English, Literature, History, Geography, IT
    grade               NVARCHAR(20)    NULL,
    discountPercentage  DECIMAL(5,2)    NULL,
    created_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_course_teacher FOREIGN KEY (teacherId) REFERENCES teacher(id)
);
GO

-- =============================================
-- 8. BẢNG SECTION (Buổi học)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'section')
CREATE TABLE section (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    courseId         INT             NOT NULL,
    dayOfWeek       NVARCHAR(20)    NOT NULL,       -- Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    startTime       TIME            NOT NULL,
    endTime         TIME            NOT NULL,
    classroom       NVARCHAR(100)   NULL,
    dateTime        DATETIME2       NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'inactive',    -- active, inactive, completed, cancelled
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    note            NVARCHAR(MAX)   NULL,
    teacherId       INT             NULL,
    CONSTRAINT FK_section_course FOREIGN KEY (courseId) REFERENCES course(id),
    CONSTRAINT FK_section_teacher FOREIGN KEY (teacherId) REFERENCES teacher(id)
);
GO

-- =============================================
-- 9. BẢNG STUDENT_COURSE (Đăng ký khóa học)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'student_course')
CREATE TABLE student_course (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    studentId       INT             NOT NULL,
    courseId         INT             NOT NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'pending',     -- pending, accepted, rejected
    isPaid          BIT             DEFAULT 0,
    enrollment_date DATETIME2       NULL DEFAULT GETDATE(),
    CONSTRAINT FK_student_course_student FOREIGN KEY (studentId) REFERENCES student(id),
    CONSTRAINT FK_student_course_course FOREIGN KEY (courseId) REFERENCES course(id)
);
GO

-- =============================================
-- 10. BẢNG STUDENT_SECTION (Điểm danh theo buổi)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'student_section')
CREATE TABLE student_section (
    id                  INT IDENTITY(1,1) PRIMARY KEY,
    studentId           INT             NOT NULL,
    sectionId           INT             NOT NULL,
    isPaid              BIT             DEFAULT 0,
    attendanceStatus    NVARCHAR(20)    NULL DEFAULT 'notyet',      -- present, absent, notyet, late
    created_at          DATETIME2       NULL DEFAULT GETDATE(),
    CONSTRAINT FK_student_section_student FOREIGN KEY (studentId) REFERENCES student(id),
    CONSTRAINT FK_student_section_section FOREIGN KEY (sectionId) REFERENCES section(id)
);
GO

-- =============================================
-- 11. BẢNG STUDENT_MARK_FEEDBACK (Điểm số và nhận xét)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'student_mark_feedback')
CREATE TABLE student_mark_feedback (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    studentId       INT             NOT NULL,
    courseId         INT             NOT NULL,
    takeBy          INT             NOT NULL,       -- accountId của người chấm điểm
    mark            DECIMAL(5,2)    NULL,
    feedback        NVARCHAR(MAX)   NULL,
    date            DATETIME2       NULL,
    type            NVARCHAR(50)    NULL,           -- midterm, final, quiz, homework...
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_mark_student FOREIGN KEY (studentId) REFERENCES student(id),
    CONSTRAINT FK_mark_course FOREIGN KEY (courseId) REFERENCES course(id),
    CONSTRAINT FK_mark_takeby FOREIGN KEY (takeBy) REFERENCES account(id)
);
GO

-- =============================================
-- 12. BẢNG STUDENT_PAYMENT_SCHEDULE (Lịch thanh toán)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'student_payment_schedule')
CREATE TABLE student_payment_schedule (
    id                  INT IDENTITY(1,1) PRIMARY KEY,
    student_section_id  INT             NULL,
    amount              DECIMAL(18,2)   NOT NULL,
    due_date            DATETIME2       NULL,
    markPaying          BIT             DEFAULT 0,
    isPaid              BIT             DEFAULT 0,
    paid_date           DATETIME2       NULL,
    courseId             INT             NULL,
    created_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_payment_schedule_student_section FOREIGN KEY (student_section_id) REFERENCES student_section(id),
    CONSTRAINT FK_payment_schedule_course FOREIGN KEY (courseId) REFERENCES course(id)
);
GO

-- =============================================
-- 13. BẢNG PAYMENTS (Lịch sử thanh toán)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'payments')
CREATE TABLE payments (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    amount          DECIMAL(18,2)   NOT NULL,
    payment_date    DATETIME2       NOT NULL,
    student_id      INT             NOT NULL,
    course_id       INT             NOT NULL,
    section_id      INT             NULL,
    payment_type    NVARCHAR(20)    NOT NULL,       -- combo, daily
    description     NVARCHAR(MAX)   NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'pending',     -- pending, approved, rejected
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_payments_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT FK_payments_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT FK_payments_section FOREIGN KEY (section_id) REFERENCES section(id)
);
GO

-- =============================================
-- 14. BẢNG REQUEST (Yêu cầu: nghỉ học, đổi lớp...)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'request')
CREATE TABLE request (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    requestBy       INT             NOT NULL,
    processedBy     INT             NULL,
    type            NVARCHAR(30)    NOT NULL,       -- leave, change_course, other
    description     NVARCHAR(MAX)   NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'pending',     -- pending, accepted, rejected
    sectionId       INT             NULL,
    courseId         INT             NULL,
    fromCourseId    INT             NULL,
    toCourseId      INT             NULL,
    createdAt       DATETIME2       NOT NULL DEFAULT GETDATE(),
    updatedAt       DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_request_requestby FOREIGN KEY (requestBy) REFERENCES account(id),
    CONSTRAINT FK_request_processedby FOREIGN KEY (processedBy) REFERENCES account(id)
);
GO

-- =============================================
-- 15. BẢNG SECTION_ASSIGNMENT (Bài tập)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'section_assignment')
CREATE TABLE section_assignment (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    courseId         INT             NOT NULL,
    title           NVARCHAR(255)   NOT NULL,
    description     NVARCHAR(MAX)   NULL,
    file_path       NVARCHAR(500)   NULL,
    teacher_id      INT             NULL,
    uploaded_at     DATETIME2       NULL DEFAULT GETDATE(),
    due_at          DATETIME2       NULL,
    CONSTRAINT FK_assignment_course FOREIGN KEY (courseId) REFERENCES course(id),
    CONSTRAINT FK_assignment_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);
GO

-- =============================================
-- 16. BẢNG SUBMISSION_ASSIGNMENT (Nộp bài tập)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'submission_assignment')
CREATE TABLE submission_assignment (
    id                      INT IDENTITY(1,1) PRIMARY KEY,
    section_assignment_id   INT             NOT NULL,
    course_id               INT             NULL,
    student_id              INT             NOT NULL,
    file_path               NVARCHAR(500)   NULL,
    submitted_at            DATETIME2       NULL DEFAULT GETDATE(),
    comment                 NVARCHAR(MAX)   NULL,
    grade                   FLOAT           NULL,
    CONSTRAINT FK_submission_assignment FOREIGN KEY (section_assignment_id) REFERENCES section_assignment(id),
    CONSTRAINT FK_submission_student FOREIGN KEY (student_id) REFERENCES student(id)
);
GO

-- =============================================
-- 17. BẢNG CONSULTATION (Tư vấn tuyển dụng)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'consultation')
CREATE TABLE consultation (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    name            NVARCHAR(255)   NOT NULL,
    email           NVARCHAR(255)   NULL,
    dob             DATETIME2       NULL,
    phone           NVARCHAR(20)    NULL,
    status          NVARCHAR(20)    NOT NULL DEFAULT 'pending',     -- pending, approved, rejected
    address         NVARCHAR(500)   NULL,
    subject         NVARCHAR(50)    NULL,       -- Math, Physics, Chemistry...
    experience      NVARCHAR(500)   NULL,
    schoolId        INT             NULL,
    schoolClassId   INT             NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_consultation_school FOREIGN KEY (schoolId) REFERENCES school(id),
    CONSTRAINT FK_consultation_school_class FOREIGN KEY (schoolClassId) REFERENCES school_class(id)
);
GO

-- =============================================
-- 18. BẢNG CONSULTATION_CERTIFICATE (Chứng chỉ tư vấn)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'consultation_certificate')
CREATE TABLE consultation_certificate (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    consultationId  INT             NOT NULL,
    imageURL        NVARCHAR(500)   NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_consultation_cert FOREIGN KEY (consultationId) REFERENCES consultation(id)
);
GO

-- =============================================
-- 19. BẢNG TEACHER_CERTIFICATE (Chứng chỉ giáo viên)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'teacher_certificate')
CREATE TABLE teacher_certificate (
    id                  INT IDENTITY(1,1) PRIMARY KEY,
    teacherId           INT             NOT NULL,
    certificate_name    NVARCHAR(255)   NULL,
    imageUrl            NVARCHAR(500)   NULL,
    issued_date         DATETIME2       NULL,
    created_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_teacher_cert FOREIGN KEY (teacherId) REFERENCES teacher(id)
);
GO

-- =============================================
-- 20. BẢNG TEACHER_ACHIVEMENT (Thành tích giáo viên)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'teacher_achivement')
CREATE TABLE teacher_achivement (
    id                  INT IDENTITY(1,1) PRIMARY KEY,
    teacherId           INT             NOT NULL,
    imageURL            NVARCHAR(500)   NULL,
    achivement_name     NVARCHAR(255)   NULL,
    issued_date         DATETIME2       NULL,
    created_at          DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_teacher_achivement FOREIGN KEY (teacherId) REFERENCES teacher(id)
);
GO

-- =============================================
-- 21. BẢNG TEACHER_SALARY (Lương giáo viên)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'teacher_salary')
CREATE TABLE teacher_salary (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    teacherId       INT             NOT NULL,
    taxCode         NVARCHAR(50)    NULL,
    salary          DECIMAL(18,2)   NULL,
    effectiveDate   DATETIME2       NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_teacher_salary FOREIGN KEY (teacherId) REFERENCES teacher(id)
);
GO

-- =============================================
-- 22. BẢNG NOTIFICATION (Thông báo)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'notification')
CREATE TABLE notification (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    accountId       INT             NOT NULL,
    description     NVARCHAR(MAX)   NULL,
    is_read         BIT             DEFAULT 0,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_notification_account FOREIGN KEY (accountId) REFERENCES account(id)
);
GO

-- =============================================
-- 23. BẢNG BANNERS (Banner trang chủ)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'banners')
CREATE TABLE banners (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    title           NVARCHAR(255)   NULL,
    description     NVARCHAR(MAX)   NULL,
    image_url       NVARCHAR(500)   NULL,
    order_index     INT             DEFAULT 0,
    is_active       BIT             DEFAULT 1,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- 24. BẢNG CENTER_INFO (Thông tin trung tâm)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'center_info')
CREATE TABLE center_info (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    center_name     NVARCHAR(255)   NULL,
    address         NVARCHAR(500)   NULL,
    phone           NVARCHAR(20)    NULL,
    email           NVARCHAR(255)   NULL,
    website         NVARCHAR(255)   NULL,
    description     NVARCHAR(MAX)   NULL,
    logo_url        NVARCHAR(500)   NULL,
    working_hours   NVARCHAR(255)   NULL,
    facebook        NVARCHAR(255)   NULL,
    youtube         NVARCHAR(255)   NULL,
    instagram       NVARCHAR(255)   NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- 25. BẢNG PAYMENT_INFO (Thông tin ngân hàng)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'payment_info')
CREATE TABLE payment_info (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    bank_name       NVARCHAR(255)   NULL,
    account_number  NVARCHAR(50)    NULL,
    account_name    NVARCHAR(255)   NULL,
    branch          NVARCHAR(255)   NULL,
    swift_code      NVARCHAR(20)    NULL,
    qr_code_url     NVARCHAR(500)   NULL,
    is_active       BIT             DEFAULT 1,
    order_index     INT             DEFAULT 0,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2       NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- 26. BẢNG ROOM (Phòng học)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'room')
CREATE TABLE room (
    id              NVARCHAR(50)    PRIMARY KEY,
    roomName        NVARCHAR(255)   NULL,
    location        NVARCHAR(255)   NULL
);
GO

-- =============================================
-- 27. BẢNG EXPENSE (Chi phí)
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'expense')
CREATE TABLE expense (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    description     NVARCHAR(MAX)   NULL,
    amount          DECIMAL(18,2)   NULL,
    approvedBy      INT             NULL,
    approval_date   DATETIME2       NULL,
    created_at      DATETIME2       NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_expense_approvedby FOREIGN KEY (approvedBy) REFERENCES account(id)
);
GO

-- =============================================
-- DỮ LIỆU MẶC ĐỊNH
-- =============================================

-- Tạo tài khoản admin mặc định (password: admin123)
IF NOT EXISTS (SELECT 1 FROM account WHERE username = 'admin')
BEGIN
    INSERT INTO account (name, username, phone, password, status, role, created_at, updated_at)
    VALUES (N'Administrator', 'admin', '0000000000', 'admin123', 'active', 'admin', GETDATE(), GETDATE());
END
GO

PRINT N'=== Database ECS đã được tạo thành công với 27 bảng ===';
GO
