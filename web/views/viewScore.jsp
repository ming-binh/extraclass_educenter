<%-- 
    Document   : viewScore
    Created on : Jun 23, 2025, 9:06:04 PM
    Author     : vankh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<% request.setAttribute("pageCSS", "/style/headerProfile.css");%>
<jsp:include page="layout/header.jsp" />
<style>
    .container-fluid {
        max-width: 900px;
        margin: 100px auto 30px auto;
        padding: 0 15px;
    }

    .widget-box {
        background: #fff;
        border-radius: 10px;
        padding: 25px 30px;
        box-shadow: 0 0 15px rgba(0,0,0,0.05);
    }

    .wc-title h4 {
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 25px;
        color: #333;
        text-align: center;
    }

    .score {
        background-color: #e0e0e0;
        border-radius: 6px;
        padding: 12px 20px;
        display: flex;
        justify-content: space-between;
        font-weight: bold;
        font-size: 17px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .score:hover {
        background-color: #d5d5d5;
    }

    .score-table-container {
        max-height: 250px;
        overflow-y: auto;
        margin-top: 15px;
        display: none; /* Ẩn mặc định */
    }

    .score-table {
        width: 100%;
        border-collapse: collapse;
    }

    .score-table th, .score-table td {
        padding: 8px 12px;
        border-bottom: 1px solid #ddd;
        text-align: center;
    }

    .score-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }
</style>

<script>
    function toggleTable(id) {
        const table = document.getElementById(id);
        if (table.style.display === "none") {
            table.style.display = "block";
        } else {
            table.style.display = "none";
        }
    }
</script>

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12 m-b30">
            <div class="widget-box">
                <div class="wc-title">
                    <h4>Điểm môn học</h4>
                </div>
                <div class="widget-inner" style="margin-top: 50px; margin-bottom: 100px">
                    <div class="row">
                        <c:forEach var="entry" items="${scoreMap}" varStatus="loop">
                            <div class="col-12" style="margin-top: 30px">
                                <div class="score" onclick="toggleTable('table${loop.index}')">
                                    <span>${entry.key}</span>
                                    <span>▼</span>
                                </div>

                                <div id="table${loop.index}" class="score-table-container">
                                    <table class="score-table">
                                        <thead>
                                            <tr>
                                                <th>Loại điểm</th>
                                                <th>Điểm</th>
                                                <th>Ngày</th>
                                                <th>Nhận xét</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="score" items="${entry.value}">
                                                <tr>
                                                    <td>${score.type}</td>
                                                    <td>${score.mark}</td>
                                                    <td>${score.formattedDate}</td> 
                                                    <td>${score.feedback}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
