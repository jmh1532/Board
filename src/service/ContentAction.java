package service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class ContentAction implements CommandProcess{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BoardDao bd = BoardDao.getInstance();
		Board board = new Board();
		
		try {
			int num = Integer.parseInt(req.getParameter("num").toString());
			String pageNum = req.getParameter("pageNum");	
			
			bd.addReadCount(num);
			board = bd.select(num);

			req.setAttribute("board", board);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("num", num);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return "content.jsp";
	}

	
}
