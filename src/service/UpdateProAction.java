package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			
			BoardDao bd = BoardDao.getInstance();
			Board board = new Board();
			req.setCharacterEncoding("utf-8");
			
			int num = Integer.parseInt(req.getParameter("num").toString());
			String pageNum = req.getParameter("pageNum");	
			
			String password = req.getParameter("password");
			String subject = req.getParameter("subject");
			String writer = req.getParameter("writer");
			String email = req.getParameter("email");
			String content = req.getParameter("content");
			String ip = req.getRemoteAddr();
			
			board.setNum(num);
			board.setContent(content);
			board.setEmail(email);
			board.setPassword(password);
			board.setSubject(subject);
			board.setWriter(writer);
			board.setIp(ip);
			
			int result =  bd.update(board);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("result", result);
			req.setAttribute("num", num);
			

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return "updatePro.jsp";
	}

}
