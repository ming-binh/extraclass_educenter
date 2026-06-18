# 🎓 EduCenter — Hệ thống Quản lý Trung tâm Dạy thêm

> Ứng dụng web quản lý toàn diện cho trung tâm dạy thêm ngoài giờ, hỗ trợ nhiều vai trò: **Admin**, **Manager**, **Staff**, **Giáo viên**, **Học sinh** và **Phụ huynh**.

---

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)
- [Cấu hình Database](#cấu-hình-database)
- [Tính năng chính](#tính-năng-chính)
- [Vai trò người dùng](#vai-trò-người-dùng)
- [Ảnh chụp màn hình](#ảnh-chụp-màn-hình)
- [Đóng góp](#đóng-góp)
- [Giấy phép](#giấy-phép)

---

## Tổng quan

**EduCenter** là một ứng dụng web Java Servlet/JSP được thiết kế để quản lý hoạt động của trung tâm dạy thêm ngoài giờ. Hệ thống cung cấp các chức năng toàn diện từ quản lý khóa học, lịch học, điểm danh, thanh toán đến giao bài tập và xử lý yêu cầu.

---

## Công nghệ sử dụng

| Thành phần       | Công nghệ                                       |
| ---------------- | ------------------------------------------------ |
| **Backend**      | Java Servlet (Jakarta EE 6.0)                    |
| **Frontend**     | JSP, JSTL, HTML5, CSS3, JavaScript               |
| **Database**     | Microsoft SQL Server                             |
| **JDBC Driver**  | MySQL Connector/J 8.0.33                         |
| **Build Tool**   | Apache Ant (NetBeans)                            |
| **Server**       | Apache Tomcat / GlassFish                        |
| **Auth**         | JWT (java-jwt 4.4.0, jjwt 0.11.5), BCrypt       |
| **Email**        | Jakarta Mail 2.0.1                               |
| **JSON**         | Gson 2.10.1, Jackson 2.19.0                      |
| **QR Code**      | ZXing (core 3.5.2 + javase 3.5.2)               |
| **IDE**          | Apache NetBeans                                  |

---

## Cấu trúc dự án

```
extraclass_educenter/
├── src/
│   ├── conf/                          # Cấu hình (MANIFEST.MF)
│   └── java/
│       ├── controller/                # 51 Servlet controllers
│       │   ├── HomePageServlet.java
│       │   ├── LoginServlet.java
│       │   ├── DashBoardServlet.java
│       │   ├── ManagerCourseServlet.java
│       │   ├── ScheduleManagementServlet.java
│       │   └── ...
│       ├── dao/                       # 24 Data Access Objects
│       │   ├── AccountDAO.java
│       │   ├── CourseDAO.java
│       │   ├── SectionDAO.java
│       │   ├── StudentDAO.java
│       │   └── ...
│       ├── dto/                       # 14 Data Transfer Objects
│       │   ├── CourseDTO.java
│       │   ├── SectionDTO.java
│       │   ├── StudentProfile.java
│       │   └── ...
│       ├── modal/                     # 27 Model classes
│       │   ├── AccountModal.java
│       │   ├── CourseModal.java
│       │   ├── StudentModal.java
│       │   └── ...
│       ├── filter/                    # Servlet Filters
│       │   ├── AuthFilter.java        # Xác thực & phân quyền
│       │   └── CenterInfoFilter.java  # Load thông tin trung tâm
│       └── utils/                     # Tiện ích
│           ├── DBUtil.java            # Kết nối database
│           ├── EmailUtil.java         # Gửi email
│           ├── HashUtils.java         # Mã hóa mật khẩu
│           ├── JWTUtils.java          # Xử lý JWT token
│           ├── FileUploadUtils.java   # Upload file
│           ├── QRCodeGenerator.java   # Tạo mã QR thanh toán
│           ├── CurrencyFormatter.java # Định dạng tiền tệ
│           └── DateFormat.java        # Định dạng ngày tháng
├── web/
│   ├── META-INF/
│   │   └── context.xml
│   ├── WEB-INF/
│   │   └── web.xml                    # Cấu hình Servlet mappings
│   ├── assets/                        # Tài nguyên tĩnh
│   │   ├── avatars/                   # Ảnh đại diện người dùng
│   │   ├── banners/                   # Banner trang chủ
│   │   ├── banners_course/            # Banner khóa học
│   │   └── certs/                     # Chứng chỉ
│   ├── style/
│   │   ├── style.css                  # CSS chung (trang public)
│   │   └── adminDashboard.css         # CSS dashboard admin/manager
│   ├── views/                         # 58 JSP pages
│   │   ├── layout/                    # Layout dùng chung
│   │   │   ├── header.jsp             # Header trang public
│   │   │   ├── adminHeader.jsp        # Header trang admin
│   │   │   ├── footer.jsp             # Footer
│   │   │   └── error.jsp              # Trang lỗi
│   │   ├── homepage.jsp               # Trang chủ
│   │   ├── login.jsp                  # Đăng nhập
│   │   ├── dashboard.jsp              # Bảng điều khiển
│   │   └── ...
│   └── index.jsp                      # Entry point
├── allowlibs/                         # Thư viện JAR
├── nbproject/                         # Cấu hình NetBeans
├── recreate_database.sql              # Script tạo database (27 bảng)
├── build.xml                          # Build script (Ant)
├── .gitignore
├── LICENSE
└── README.md
```

---

## Yêu cầu hệ thống

- **JDK**: 17 trở lên
- **IDE**: Apache NetBeans 17+ (khuyến nghị)
- **Application Server**: Apache Tomcat 10+ hoặc GlassFish 7+
- **Database**: Microsoft SQL Server 2019+
- **Trình duyệt**: Chrome, Firefox, Edge (phiên bản mới nhất)

---

## Hướng dẫn cài đặt

### 1. Clone dự án

```bash
git clone https://github.com/ming-binh/extraclass_educenter.git
cd extraclass_educenter
```

### 2. Mở dự án trong NetBeans

1. Mở **NetBeans IDE**
2. Chọn **File → Open Project**
3. Duyệt đến thư mục `extraclass_educenter` và mở

### 3. Cấu hình Application Server

1. Trong NetBeans, vào **Services → Servers**
2. Thêm **Apache Tomcat 10+** hoặc **GlassFish 7+**
3. Cấu hình server cho project tại **Project Properties → Run**

### 4. Cấu hình kết nối Database

Chỉnh sửa file [`src/java/utils/DBUtil.java`](src/java/utils/DBUtil.java):

```java
public static Connection getConnection() throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    String url = "jdbc:mysql://localhost:3306/ECS?useSSL=false&serverTimezone=UTC";
    String user = "your_username";
    String pass = "your_password";
    return DriverManager.getConnection(url, user, pass);
}
```

> ⚠️ **Lưu ý**: Thay đổi `url`, `user`, `pass` cho phù hợp với môi trường của bạn.

### 5. Build & Run

- Nhấn **F6** hoặc click **Run Project** trong NetBeans
- Truy cập: `http://localhost:8080/EduCenter/`

---

## Cấu hình Database

### Tạo database

Chạy script SQL để tạo database với 27 bảng:

```bash
# Sử dụng SQL Server Management Studio (SSMS)
# Mở file recreate_database.sql và thực thi
```

Hoặc qua command line:

```bash
sqlcmd -S localhost -U sa -P your_password -i recreate_database.sql
```

### Sơ đồ database (27 bảng)

| STT | Bảng                        | Mô tả                         |
| --- | --------------------------- | ------------------------------ |
| 1   | `account`                   | Tài khoản người dùng           |
| 2   | `school`                    | Trường học                     |
| 3   | `school_class`              | Lớp học tại trường             |
| 4   | `parent`                    | Phụ huynh                      |
| 5   | `teacher`                   | Giáo viên                      |
| 6   | `student`                   | Học sinh                       |
| 7   | `course`                    | Khóa học                       |
| 8   | `section`                   | Buổi học                       |
| 9   | `student_course`            | Đăng ký khóa học               |
| 10  | `student_section`           | Điểm danh theo buổi            |
| 11  | `student_mark_feedback`     | Điểm số và nhận xét            |
| 12  | `student_payment_schedule`  | Lịch thanh toán                |
| 13  | `payments`                  | Lịch sử thanh toán             |
| 14  | `request`                   | Yêu cầu (nghỉ học, đổi lớp…)  |
| 15  | `section_assignment`        | Bài tập                        |
| 16  | `submission_assignment`     | Nộp bài tập                    |
| 17  | `consultation`              | Đăng ký tư vấn                 |
| 18  | `consultation_certificate`  | Chứng chỉ tư vấn               |
| 19  | `teacher_certificate`       | Chứng chỉ giáo viên            |
| 20  | `teacher_achivement`        | Thành tích giáo viên            |
| 21  | `teacher_salary`            | Lương giáo viên                |
| 22  | `notification`              | Thông báo                      |
| 23  | `banners`                   | Banner trang chủ               |
| 24  | `center_info`               | Thông tin trung tâm            |
| 25  | `payment_info`              | Thông tin ngân hàng            |
| 26  | `room`                      | Phòng học                      |
| 27  | `expense`                   | Chi phí                        |

### Tài khoản mặc định

| Vai trò | Username | Password   |
| ------- | -------- | ---------- |
| Admin   | `admin`  | `admin123` |

---

## Tính năng chính

### 🏠 Trang công khai
- Trang chủ với banner động và danh sách khóa học nổi bật
- Xem danh sách & chi tiết khóa học
- Xem danh sách & thông tin giáo viên
- Đăng ký tư vấn trực tuyến
- Giới thiệu trung tâm

### 👨‍💼 Quản lý (Admin / Manager / Staff)
- **Dashboard** thống kê tổng quan
- **Quản lý khóa học**: Thêm / sửa / xóa khóa học, cấu hình phí combo & theo buổi
- **Quản lý lớp học (Section)**: Tạo buổi học, gán phòng, gán giáo viên
- **Quản lý lịch học**: Xếp lịch theo tuần, quản lý phòng học
- **Quản lý tài khoản**: Tạo tài khoản cho GV, HS, PH; duyệt tài khoản
- **Duyệt thanh toán**: Xác nhận thanh toán của học sinh
- **Quản lý tư vấn**: Xem và xử lý đăng ký tư vấn
- **Quản lý đăng ký lớp**: Duyệt yêu cầu tham gia khóa học
- **Xử lý yêu cầu**: Nghỉ học, chuyển lớp, các yêu cầu khác
- **Cấu hình hệ thống**: Banner, thông tin trung tâm, thông tin ngân hàng
- **Báo cáo điểm danh**: Thống kê chuyên cần theo khóa / buổi / học sinh

### 👩‍🏫 Giáo viên
- Xem lịch dạy theo tuần
- Điểm danh học sinh
- Chấm điểm và nhận xét
- Giao bài tập & xem bài nộp
- Gửi yêu cầu (nghỉ dạy, đổi lịch…)
- Xem lớp đang dạy

### 👨‍🎓 Học sinh
- Xem lịch học theo tuần
- Đăng ký khóa học
- Thanh toán (QR code)
- Xem điểm số
- Nộp bài tập
- Xem chuyên cần
- Gửi yêu cầu (nghỉ học, chuyển lớp…)

### 👨‍👩‍👦 Phụ huynh
- Xem lịch học của con
- Xem thông tin con
- Gửi yêu cầu thay cho con

### 🔐 Xác thực & Bảo mật
- Đăng nhập / Đăng xuất
- Quên mật khẩu (xác thực qua email, 3 bước)
- Đổi mật khẩu
- Phân quyền theo vai trò (AuthFilter)
- Mã hóa mật khẩu (BCrypt)
- JWT token

### 🔔 Tiện ích
- Hệ thống thông báo realtime (API polling)
- Upload ảnh đại diện
- Tạo mã QR thanh toán
- Gửi email tự động
- Quản lý hồ sơ cá nhân

---

## Vai trò người dùng

| Vai trò       | Mô tả                                       |
| ------------- | -------------------------------------------- |
| **Admin**     | Toàn quyền hệ thống, cấu hình trung tâm    |
| **Manager**   | Quản lý khóa học, lớp học, lịch, nhân sự    |
| **Staff**     | Hỗ trợ quản lý, xử lý yêu cầu, duyệt TK   |
| **Teacher**   | Giáo viên — dạy, điểm danh, chấm điểm      |
| **Student**   | Học sinh — học, nộp bài, thanh toán          |
| **Parent**    | Phụ huynh — theo dõi con, gửi yêu cầu      |

---

## Ảnh chụp màn hình

> _Thêm ảnh chụp màn hình của ứng dụng vào đây._

<!-- 
![Trang chủ](screenshots/homepage.png)
![Dashboard](screenshots/dashboard.png)
![Quản lý khóa học](screenshots/course-management.png)
-->

---

## Đóng góp

1. **Fork** dự án
2. Tạo branch mới: `git checkout -b feature/ten-tinh-nang`
3. Commit: `git commit -m "Thêm tính năng XYZ"`
4. Push: `git push origin feature/ten-tinh-nang`
5. Tạo **Pull Request**

---

## Giấy phép

Dự án này được phát hành dưới giấy phép [MIT](LICENSE).

---

<p align="center">
  <strong>EduCenter</strong> — Xây dựng bởi đội ngũ phát triển SWP391 🚀
</p>
