package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.ProductImageDTO;

public class ProductImageDAO {

	private static ProductImageDAO instance = null;
	
	public synchronized static ProductImageDAO getInstance() {
		if(instance == null) {
			instance = new ProductImageDAO();
		}
		
		return instance;
	}
	
	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	
	// 이미지 정보 입력
	public int insert(ProductImageDTO dto) throws Exception {
		String sql = "insert into image values(image_seq.nextval,?,?,?)";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, dto.getParentSeq());
		stat.setString(2, dto.getOriName());
		stat.setString(3, dto.getSysName());
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 이미지 정보 출력
	public List<ProductImageDTO> getImageList(int parentSeq) throws Exception {
		String sql = "select * from image where parentSeq=? order by seq";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, parentSeq);
		try (
		ResultSet rs = stat.executeQuery(); ){
		
		List<ProductImageDTO> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new ProductImageDTO(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
		}
		
		return list; }}
	}
	
	// 이미지 정보 삭제
	public int delete(int seq) throws Exception {
		String sql = "delete from image where seq=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, seq);
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 특정 게시굴 이미지 정보 전부 삭제
	public int deleteAll(int parentSeq) throws Exception {
		String sql = "delete from image where parentSeq=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, parentSeq);
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 이미지 정보 수정
	public int update(ProductImageDTO dto) throws Exception {
		String sql = "update image set oriName=?, sysName=? where seq=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, dto.getOriName());
		stat.setString(2, dto.getOriName());
		stat.setInt(3, dto.getSeq());
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	
}
