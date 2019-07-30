package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BoardDao bd = BoardDao.getInstance();
		Board board = new Board();
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");	
			
			board = bd.select(num);
			
			req.setAttribute("board", board);
			req.setAttribute("pageNum", pageNum);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return "updateFrom.jsp";
	}

}
