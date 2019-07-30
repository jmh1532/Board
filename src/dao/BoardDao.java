package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDao {
	//private static BoardDao instance;
	private BoardDao() {}
	
	public static BoardDao getInstance() { return LazyHolder.INSTANCE; }
	private static class LazyHolder { private static final BoardDao INSTANCE = new BoardDao(); }

	
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	public int getTotalCnt() throws SQLException {
		Connection conn = null;
		int tot = 0;
		ResultSet rs = null;
		Statement stmt = null;
		
		String sql = "select count(*) from board";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())tot = rs.getInt(1);
			
			System.out.println(rs);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(stmt != null)stmt.close();
			if(conn != null)conn.close();
			
		}
		return tot;
	}
	
	
	public List<Board> list(int startRow, int endRow) throws SQLException{
		Connection conn = null;
		List<Board> list = new ArrayList<Board>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		String sql = "select * from (select rownum rn, a.* from (select * from board order by ref desc, re_step) a )"
				+ "where rn between ? and ?";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, startRow);
			stmt.setInt(2, endRow);
			rs = stmt.executeQuery();
			while(rs.next()) {
			
				Board bo = new Board();
				bo.setNum(rs.getInt("num"));
				bo.setWriter(rs.getString("writer"));
				bo.setSubject(rs.getString("subject"));
				bo.setEmail(rs.getString("email"));
				bo.setReadcount(rs.getInt("readcount"));
				bo.setIp(rs.getString("ip"));
				bo.setRef(rs.getInt("ref"));
				bo.setRe_level(rs.getInt("re_level"));
				bo.setRe_step(rs.getInt("re_step"));
				bo.setReg_date(rs.getDate("reg_date"));
				
				list.add(bo);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(stmt != null)stmt.close();
			if(conn != null)conn.close();
			
		}
		return list;
	
	}
	
	public Board select(int num) throws SQLException{
		Connection conn = null;
		Board bo = new Board();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		String sql = "select * from board where num=?";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, num);
			rs = stmt.executeQuery();
			if(rs.next()) {
				bo.setNum(rs.getInt("num"));
				bo.setWriter(rs.getString("writer"));
				bo.setSubject(rs.getString("subject"));
				bo.setEmail(rs.getString("email"));
				bo.setReadcount(rs.getInt("readcount"));
				bo.setIp(rs.getString("ip"));
				bo.setRef(rs.getInt("ref"));
				bo.setContent(rs.getString("content"));
				bo.setPassword(rs.getString("passwd"));
				bo.setRe_level(rs.getInt("re_level"));
				bo.setRe_step(rs.getInt("re_step"));
				bo.setReg_date(rs.getDate("reg_date"));
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(stmt != null)stmt.close();
			if(conn != null)conn.close();
		}
		return bo;
	
	}
	
	public void addReadCount(int num) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		String sql = "update board set readcount = readcount+1 where num=?";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, num);
			stmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally {
			if(stmt != null)stmt.close();
			if(conn != null)conn.close();
			
		}
	}
		
	
	public int update(Board bo) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		String sql = "update board set email=?,passwd=?,subject=?,writer=?,content=?,ip=? where num=?";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bo.getEmail());
			stmt.setString(2, bo.getPassword());
			stmt.setString(3, bo.getSubject());
			stmt.setString(4, bo.getWriter());
			stmt.setString(5, bo.getContent());
			stmt.setString(6, bo.getIp());
			stmt.setInt(7, bo.getNum());
			
			result = stmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally {
			if(stmt != null)stmt.close();
			if(conn != null)conn.close();
			
		}
		return result;
	}
	

	public int insert(Board board) throws SQLException{
		int num = board.getNum();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql1 = "select nvl(max(num),0) from board";
		String sql = "insert into board values(?,?,?,?,?,?,?,?,?,?,?,sysdate)";
		int result = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql1);
			rs = pstmt.executeQuery();
			rs.next();
			//key인 num 1씩 증가, mysql auto_increment 또는 oracle sequence
			//sequence를 사용 : values(시퀀스명(board_seq).nextval,?,?...)
			int number = rs.getInt(1) + 1;
			rs.close(); pstmt.close();
			if(num == 0) board.setRef(number);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number);
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getContent());
			pstmt.setString(5, board.getEmail());
			pstmt.setInt(6, board.getReadcount());
			pstmt.setString(7, board.getPassword());
			pstmt.setInt(8, board.getRef());
			pstmt.setInt(9, board.getRe_step());
			pstmt.setInt(10, board.getRe_level());
			pstmt.setString(11, board.getIp());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return result;
	}

}
