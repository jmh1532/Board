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
		
	

}
