<%-- 
    Document   : managerDashboard
    Created on : Jun 26, 2025, 2:53:16 PM
    Author     : Minh Thu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<jsp:include page="layout/adminHeader.jsp" />
<style>
    .widget-card .wc-stats {
        position: static !important;
        right: auto !important;
        top: auto !important;
        text-align: center;
        width: 100%;
        display: block;
    }

    .chart-container {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        padding: 20px;
        margin-bottom: 30px;
    }

    .table-container {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        padding: 20px;
    }

    .teacher-rank-table {
        width: 100%;
        border-collapse: collapse;
    }

    .teacher-rank-table th,
    .teacher-rank-table td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid #eee;
    }

    .teacher-rank-table th {
        background-color: #f8f9fa;
        font-weight: 600;
        color: #333;
    }

    .teacher-rank-table tr:hover {
        background-color: #f8f9fa;
    }

    .rank-badge {
        display: inline-block;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        text-align: center;
        line-height: 30px;
        font-weight: bold;
        color: white;
    }

    .rank-1 {
        background: #ffd700;
        color: #333;
    }
    .rank-2 {
        background: #c0c0c0;
        color: #333;
    }
    .rank-3 {
        background: #cd7f32;
        color: white;
    }
    .rank-other {
        background: #6c757d;
    }

    .revenue-amount {
        font-weight: 600;
        color: #28a745;
    }

    .section-title {
        font-size: 1.25rem;
        font-weight: 600;
        margin-bottom: 20px;
        color: #333;
    }

    .filter-controls select {
        min-width: 120px;
    }

    .quarterly-stats {
        padding: 15px;
        background: #f8f9fa;
        border-radius: 6px;
        height: 100%;
    }

    .quarter-item {
        padding: 10px 0;
        border-bottom: 1px solid #dee2e6;
    }

    .quarter-item:last-child {
        border-bottom: none;
    }

    .quarter-label {
        font-weight: 600;
        color: #495057;
    }

    .quarter-value {
        font-weight: 700;
        color: #28a745;
    }

    .quarter-comparison {
        font-size: 0.875rem;
    }

    .btn-group-sm .btn {
        padding: 0.25rem 0.5rem;
        font-size: 0.875rem;
    }

      .monthly-bar-list {
        padding: 10px 0;
    }

    .month-bar {
        background: #f8f9fa;
        padding: 12px 16px;
        border-radius: 6px;
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }

    .progress {
        height: 20px;
        background-color: #e9ecef;
        border-radius: 10px;
        overflow: hidden;
    }

    .progress-bar {
        height: 100%;
        color: white;
        text-align: right;
        padding-right: 8px;
        font-size: 0.85rem;
        font-weight: bold;
        line-height: 20px;
        transition: width 0.3s ease;
    }

    .text-muted {
        font-size: 0.8rem;
        display: inline-block;
        margin-top: 4px;
    }
</style>
<div class="container-fluid">
    <div class="db-breadcrumb">
        <h4 class="breadcrumb-title">Bảng điều khiển</h4>
<ul class="db-breadcrumb-list">
            <li><a href="bang-dieu-khien"><i class="fa fa-home"></i>Bảng điều khiển</a></li>
            <li>Quản Lý</li>
        </ul>
    </div>	
    <!-- Card -->
    <div class="row">
        <div class="col-md-6 col-lg-3 col-xl-3 col-sm-6 col-12">
            <div class="widget-card widget-bg1 d-flex flex-column justify-content-between" style="height: 180px;">
                <div>
                    <h4 class="wc-title text-center">Tổng doanh thu</h4>
                </div>
                <div class="d-flex flex-column align-items-center justify-content-center" style="flex:1;">
                    <span class="wc-stats d-block" style="font-size: 2.5rem; line-height: 1;">
                        <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true"/>
                    </span>
                    <span class="wc-des d-block mt-1">VNĐ</span>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3 col-xl-3 col-sm-6 col-12">
            <div class="widget-card widget-bg3 d-flex flex-column justify-content-between" style="height: 180px;">
                <div>
                    <h4 class="wc-title text-center">Tổng khóa học</h4>
                </div>
                <div class="d-flex flex-column align-items-center justify-content-center" style="flex:1;">
                    <span class="wc-stats d-block" style="font-size: 2.5rem; line-height: 1;">
                        ${totalCourses}
                    </span>
                    <span class="wc-des d-block mt-1">Khóa học</span>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3 col-xl-3 col-sm-6 col-12">
            <div class="widget-card widget-bg4 d-flex flex-column justify-content-between" style="height: 180px;">
                <div>
                    <h4 class="wc-title text-center">Tổng giáo viên</h4>
                </div>
                <div class="d-flex flex-column align-items-center justify-content-center" style="flex:1;">
                    <span class="wc-stats d-block" style="font-size: 2.5rem; line-height: 1;">
                        ${totalTeachers}
                    </span>
<span class="wc-des d-block mt-1">Giáo viên</span>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3 col-xl-3 col-sm-6 col-12">
            <div class="widget-card widget-bg4 d-flex flex-column justify-content-between" style="height: 180px;">
                <div>
                    <h4 class="wc-title text-center">Tổng học sinh</h4>
                </div>
                <div class="d-flex flex-column align-items-center justify-content-center" style="flex:1;">
                    <span class="wc-stats d-block" style="font-size: 2.5rem; line-height: 1;">
                        ${totalStudents}
                    </span>
<span class="wc-des d-block mt-1">Học Sinh</span>
                </div>
            </div>
        </div>
    </div>
    <!-- Charts and Tables Section -->
    <div class="chart-container">
    <h5 class="section-title mb-4">
        <i class="fa fa-bar-chart mr-2"></i>
        Doanh thu theo tháng (2025)
    </h5>

    <div class="monthly-bar-list">
    <c:forEach var="item" items="${monthlyRevenue}">
        <c:if test="${item.year == 2025}">
            <c:set var="percent" value="${item.revenue / 100000}" />
            <c:choose>
                <c:when test="${item.growthRate > 0}">
                    <c:set var="color" value="#28a745" />
                </c:when>
                <c:when test="${item.growthRate < 0}">
                    <c:set var="color" value="#dc3545" />
                </c:when>
                <c:otherwise>
                    <c:set var="color" value="#6c757d" />
                </c:otherwise>
            </c:choose>

            <div class="month-bar mb-3">
                <div class="d-flex justify-content-between mb-1">
                    <span>Tháng ${item.month}/${item.year}</span>
                    <span>
                        <fmt:formatNumber value="${item.revenue}" type="number" groupingUsed="true"/> VNĐ
                    </span>
                </div>
                <div class="progress">
                    <div class="progress-bar" style="width: ${growthRate}%; background-color: ${color};">
                        <c:if test="${item.revenue > 0}">
                            ${percent}%
                        </c:if>
                    </div>
                </div>
                <small class="text-muted">Tăng trưởng: ${item.growthRate}%</small>
            </div>
        </c:if>
    </c:forEach>
</div>
</div>
</div>
<!-- Chart.js -->
<script>
$(document).ready(function() {
    // Format currency function
    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN').format(amount);
    }
    
    // Year filter change handler
    $('#yearFilter').on('change', function() {
        const selectedYear = $(this).val();
        showLoading();
        fetchQuarterlyData(selectedYear);
    });
    
    // Period toggle handler
    $('[data-period]').on('click', function() {
        const $this = $(this);
        const period = $this.data('period');
        
        // Update button states
        $('[data-period]').removeClass('active');
        $this.addClass('active');
        
        showLoading();
        togglePeriodData(period);
    });
    
    // Show loading state
    function showLoading() {
        $('#quarterlyTable tbody').html(`
            <tr>
                <td colspan="3" class="text-center py-4">
                    <div class="spinner-border spinner-border-sm text-primary" role="status">
                        <span class="sr-only">Đang tải...</span>
                    </div>
                    <span class="ml-2">Đang tải dữ liệu...</span>
                </td>
            </tr>
        `);
    }
    
    // Fetch quarterly data via AJAX
    function fetchQuarterlyData(year) {
        $.ajax({
            url: '/api/quarterly-revenue',
            method: 'GET',
            data: { year: year },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    updateChart(response.data);
                    updateTable(response.data);
updateSummary(response.summary);
                } else {
                    showError('Không thể tải dữ liệu. Vui lòng thử lại.');
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX Error:', error);
                showError('Lỗi kết nối. Vui lòng kiểm tra lại.');
            }
        });
    }
    
    // Toggle between current and previous year
    function togglePeriodData(period) {
        $.ajax({
            url: '/api/quarterly-revenue',
            method: 'GET',
            data: { period: period },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    updateTable(response.data);
                    updateSummary(response.summary);
                } else {
                    showError('Không thể tải dữ liệu so sánh.');
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX Error:', error);
                showError('Lỗi kết nối. Vui lòng thử lại.');
            }
        });
    }
    
    // Update chart with new data
    function updateChart(data) {
        const labels = data.map(q => `Quý ${q.quarter}`);
        const values = data.map(q => q.revenue);
        
        revenueChart.data.labels = labels;
        revenueChart.data.datasets[0].data = values;
        revenueChart.update('active');
    }
    
    // Update table with new data
    function updateTable(data) {
        const tbody = $('#quarterlyTable tbody');
        tbody.empty();
        
        if (data.length === 0) {
            tbody.html(`
                <tr>
                    <td colspan="3" class="text-center py-4 text-muted">
                        <i class="fa fa-info-circle mr-2"></i>
                        Không có dữ liệu cho khoảng thời gian này
                    </td>
                </tr>
            `);
            return;
        }
        
        data.forEach(function(quarter) {
            const growthBadge = getGrowthBadge(quarter.growthRate);
            const row = `
                <tr class="fade-in">
                    <td>
                        <strong>Quý ${quarter.quarter}</strong>
                        <br>
                        <small class="text-muted">${quarter.year}</small>
                    </td>
                    <td class="text-right revenue-amount">
                       <fmt:formatNumber value="${quarter.revenue}" type="number" groupingUsed="true" /> VND
                    </td>
                    <td class="text-center">
                        ${growthBadge}
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    }
    
    // Get growth rate badge HTML
    function getGrowthBadge(growthRate) {
        if (growthRate > 0) {
            return `<span class="badge badge-success">
<i class="fa fa-arrow-up mr-1"></i>
                        +${growthRate.toFixed(1)}%
                    </span>`;
        } else if (growthRate < 0) {
            return `<span class="badge badge-danger">
                        <i class="fa fa-arrow-down mr-1"></i>
                        ${growthRate.toFixed(1)}%
                    </span>`;
        } else {
            return `<span class="badge badge-secondary">
                        <i class="fa fa-minus mr-1"></i>
                        0%
                    </span>`;
        }
    }
    
    // Update summary cards
    function updateSummary(summary) {
        if (summary) {
            $('.total-year-revenue').text(formatCurrency(summary.totalRevenue) + ' VNĐ');
            $('.average-growth-rate').text(summary.averageGrowth.toFixed(1) + '%')
                .removeClass('text-success text-danger')
                .addClass(summary.averageGrowth >= 0 ? 'text-success' : 'text-danger');
        }
    }
    
    // Show error message
    function showError(message) {
        $('#quarterlyTable tbody').html(`
            <tr>
                <td colspan="3" class="text-center py-4 text-danger">
                    <i class="fa fa-exclamation-triangle mr-2"></i>
                    ${message}
                </td>
            </tr>
        `);
    }
    
    // Refresh data function
    function refreshData() {
        const currentYear = $('#yearFilter').val();
        const currentPeriod = $('[data-period].active').data('period');
        
        showLoading();
        
        if (currentPeriod) {
            togglePeriodData(currentPeriod);
        } else {
            fetchQuarterlyData(currentYear);
        }
    }
    
    // Auto refresh every 5 minutes (optional)
    setInterval(refreshData, 300000);
    
    // Initialize chart on page load
    initChart();
    
    // Handle window resize
    $(window).on('resize', function() {
        if (revenueChart) {
            revenueChart.resize();
        }
    });
});

// CSS animation for fade-in effect
const style = document.createElement('style');
style.textContent = `
    .fade-in {
        animation: fadeIn 0.5s ease-in;
    }
    
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    .spinner-border-sm {
        width: 1rem;
        height: 1rem;
    }
`;
document.head.appendChild(style);
</script>