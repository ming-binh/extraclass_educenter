/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import dao.CenterInfoDAO;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import modal.CenterInfoModal;

/**
 *
 * @author Astersa
 */
public class CenterInfoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        
        try {
            // Chỉ load centerInfo nếu chưa có trong request
            if (request.getAttribute("centerInfo") == null) {
                CenterInfoDAO centerInfoDAO = new CenterInfoDAO();
                CenterInfoModal centerInfo = centerInfoDAO.getCenterInfo();
                request.setAttribute("centerInfo", centerInfo);
            }
        } catch (Exception e) {
            // Nếu có lỗi, set centerInfo = null để footer hiển thị thông tin mặc định
            request.setAttribute("centerInfo", null);
        }

        chain.doFilter(req, res);
    }
} 