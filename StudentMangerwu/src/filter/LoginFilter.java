package filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wu
 * @date 2020/4/20 - 20:14
 */

/**
 * 拦截用户未登录状态下的请求
 */
public class LoginFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        Object user = request.getSession().getAttribute("user");
        if(user == null){
            //未登录
            response.sendRedirect(request.getContextPath()+"/index.jsp");
            return;
        }else{
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
