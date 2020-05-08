package servlet;
import utils.CheckCode;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author wu
 * @date 2020/4/19 - 14:06
 */
@WebServlet("/checkcodeservlet")
public class CheckCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse reponse) throws ServletException, IOException{
        String method = request.getParameter("method");
        if("loginCapcha".equals(method)){
            generateLoginCpacha(request, reponse);
            return;
        }
        reponse.getWriter().write("error method");
    }
    private void generateLoginCpacha(HttpServletRequest request, HttpServletResponse reponse) throws IOException{
        CheckCode checkCode = new CheckCode();
        String generatorVCode = checkCode.generatorVCode();
        request.getSession().setAttribute("loginCapcha", generatorVCode);
        BufferedImage generatorRotateVCodeImage = checkCode.generatorRotateVCodeImage(generatorVCode, true);
        ImageIO.write(generatorRotateVCodeImage, "gif", reponse.getOutputStream());
    }
}

