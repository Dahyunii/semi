package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.MemberDTO;

public class MemberDAO {

	private static MemberDAO instance = null;
	
	public synchronized static MemberDAO getInstance() {
		if(instance == null) {
			instance = new MemberDAO();
		}
		return instance;
	}
	
	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	
	// 회원등록
	public int insert(MemberDTO dto) throws Exception {
		String sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?, ?)";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, dto.getId());
		stat.setString(2, dto.getPw());
		stat.setString(3, dto.getName());
		stat.setString(4, dto.getPhone());
		stat.setString(5, dto.getEmail());
		stat.setString(6, dto.getZipcode());
		stat.setString(7, dto.getRoadAddress());
		stat.setString(8, dto.getJibunAddress());
		stat.setString(9, dto.getSpecAddress());
		stat.setString(10, dto.getPersonalAnswer());
		stat.setInt(11, dto.getReliability());
		
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 아이디 중복검사
	public boolean duplCheck(String id) throws Exception {
		String sql = "select * from member where id=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, id);
		try (
		ResultSet rs = stat.executeQuery(); ){
		
		return rs.next(); }}
	}
	
	// 로그인
	public boolean isLoginOk(String id, String pw) throws Exception {
		String sql = "select * from member where id=? and pw=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, id);
		stat.setString(2, pw);
		try (
		ResultSet rs = stat.executeQuery(); ){
		
		return rs.next(); }}
	}
	
	// 회원탈퇴
	public int memberOut(String id) throws Exception {
		String sql = "delete from member where id=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, id);
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 마이페이지
	public MemberDTO myPage(String id) throws Exception {
		String sql = "select * from member where id=?";
		
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql);
		stat.setString(1, id);
		ResultSet rs = stat.executeQuery();
		rs.next();
		
		MemberDTO dto = new MemberDTO(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getString(8), rs.getString(9), rs.getTimestamp(10),
						rs.getString(11), rs.getInt(12));
		
		return dto;
	}
	
	// 개인정보수정
	public int update(MemberDTO dto) throws Exception {
		String sql = "update member set phone=?, email=?, zipcode=?, roadAddress=?, jibunAddress=?, specAddress=?, personalAnswer=? where id=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, dto.getPhone());
		stat.setString(2, dto.getEmail());
		stat.setString(3, dto.getZipcode());
		stat.setString(4, dto.getRoadAddress());
		stat.setString(5, dto.getJibunAddress());
		stat.setString(6, dto.getSpecAddress());
		stat.setString(7, dto.getPersonalAnswer());
		stat.setString(8, dto.getId());
		int result = stat.executeUpdate();
		
		con.commit();
		return result; }
	}
	
	
	
	
}