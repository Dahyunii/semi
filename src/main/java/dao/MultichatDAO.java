package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.MultichatDTO;

public class MultichatDAO {

	private static MultichatDAO instance = null;
	
	public synchronized static MultichatDAO getInstance() {
		if(instance == null) {
			instance = new MultichatDAO();
		}
		return instance;
	}
	
	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	
	public int insert(MultichatDTO dto) throws Exception {
		String sql = "insert into multichat values(multichat_seq.nextval,?,?)";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, dto.getWriter());
		stat.setString(2, dto.getContents());
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	public List<MultichatDTO> getMultichatList() throws Exception {
		String sql = "select * from multichat";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql);
		ResultSet rs = stat.executeQuery(); ){
		
		List<MultichatDTO> list = new ArrayList<>();
		
		for(int i=0; i<100; i++) {
			if(rs.next() == false) {
				break;
			}
			list.add(new MultichatDTO(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		
		return list; }
	}
	
}
