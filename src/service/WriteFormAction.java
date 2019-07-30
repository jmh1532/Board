package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class WriteFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			int num = 0, ref = 0, re_level=0,re_step=0;
			String pageNum = req.getParameter("pageNum");
			if(pageNum == null)pageNum ="1";
			if(req.getParameter("num") !=null) {
				num=Integer.parseInt(req.getParameter("num").toString());
				BoardDao bd = BoardDao.getInstance();
				Board board = bd.select(num);
				
				ref = board.getRef();
				re_level = board.getRe_level();
				re_step = board.getRe_step();
			}
				
			req.setAttribute("num", num);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("re_level", re_level);
			req.setAttribute("re_step", re_step);
			req.setAttribute("ref", ref);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return "writeForm.jsp";
	}

}
