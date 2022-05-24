package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.CarReplyDTO;

public class CarReplyDAO {
	
	private static CarReplyDAO instance=null;
	
	public synchronized static CarReplyDAO getInstance() {
		if(instance==null) {
			instance = new CarReplyDAO();
		}
		return instance;
	}
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	

	
	
	public int replyinsert(String writer,String contents,int parentSeq) throws Exception{
		String sql = "insert into car_reply values(carreply_seq.nextval, ?,?,?,sysdate)";
		
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, parentSeq);
			pstat.setString(2, writer);
			pstat.setString(3, contents);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
			
		}
		
	}
	
	public List<CarReplyDTO> selectreply(int parentSeq) throws Exception{
		String sql = "select * from car_reply order by writedate asc";
		
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();){
			
			List<CarReplyDTO> list = new ArrayList<>();
			while(rs.next()) {
				CarReplyDTO dto = new CarReplyDTO();
				if(rs.getInt("parentSeq")==parentSeq) {
					dto.setSeq(rs.getInt("seq"));
					dto.setParentSeq(parentSeq);
					dto.setWriter(rs.getString("writer"));
					dto.setContents(rs.getString("contents"));
					dto.setWriteDate(rs.getTimestamp("writeDate"));
					
					list.add(dto);
				}
			}
			return list;
		}
		
	}
	
	public int replydelete(int seq) throws Exception{
		String sql = "delete from car_reply where seq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public int replymodify(int seq,String contents) throws Exception{
		String sql="update car_reply set contents=? where seq=?";
		
		try(Connection con=this.getConnection();
				PreparedStatement pstat=con.prepareStatement(sql);){
			pstat.setString(1, contents);
			pstat.setInt(2, seq);
			int result=pstat.executeUpdate();
			con.commit();
			return result;
		}
		
		
	}
}
