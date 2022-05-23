package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.ProductReplyDTO;

public class ProductReplyDAO {

	private static ProductReplyDAO instance = null;
	
	public synchronized static ProductReplyDAO getInstance() {
		if(instance == null) {
			instance = new ProductReplyDAO();
		}
		
		return instance;
	}
	
	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	
	// 댓글 등록
	public int insert(ProductReplyDTO dto) throws Exception {
		String sql = "insert into reply values(reply_seq.nextval,?,?,?,default)";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, dto.getParentSeq());
		stat.setString(2, dto.getWriter());
		stat.setString(3, dto.getContents());
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 댓글 출력
	public List<ProductReplyDTO> getReplyList(int parentSeq) throws Exception {
		String sql = "select * from reply where parentSeq=? order by seq";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, parentSeq);
		try (
		ResultSet rs = stat.executeQuery(); ){
		
		List<ProductReplyDTO> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new ProductReplyDTO(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5)));
		}
		
		return list; }}
	}
	
	// 댓글 삭제
	public int delete(int seq) throws Exception {
		String sql = "delete from reply where seq=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setInt(1, seq);
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
	// 댓글 수정
	public int update(int seq, String contents) throws Exception {
		String sql = "update reply set contents=?, writeDate=default where seq=?";
		
		try (
		Connection con = getConnection();
		PreparedStatement stat = con.prepareStatement(sql); ){
		stat.setString(1, contents);
		stat.setInt(2, seq);
		int result = stat.executeUpdate();
		con.commit();
		
		return result; }
	}
	
}
