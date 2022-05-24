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

import dto.ChatListDTO;


public class ChatListDAO {

	private static ChatListDAO instance = null;
	
	public synchronized static ChatListDAO getInstance() {
		if(instance ==  null) {
			instance = new ChatListDAO();
		}
		return instance;
	}
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		
		return ds.getConnection();
	}

	// chat_list에 user를 각각 저장
	public int insert(ChatListDTO dto) throws Exception{
		String sql = "insert into chat_list values(chat_list_seq.nextval,?,?,default)";

		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, dto.getUser1());
			pstat.setString(2, dto.getUser2());

			int result = pstat.executeUpdate();

			con.commit();

			return result;
		}
	}
	
	// chat_list에 user는 한 번만 저장되도록
//	public boolean isChatExist(String loginId, String opponentId) throws Exception{
//		String sql = "select * from chat_list where (user1 = ? and user2 = ?) or (user1 = ? and user2 = ?)";
//
//		try(Connection con = getConnection();
//				PreparedStatement pstat = con.prepareStatement(sql)){
//			pstat.setString(1, loginId);
//			pstat.setString(2, opponentId);
//			pstat.setString(3, opponentId);
//			pstat.setString(4, loginId);
//			try (ResultSet rs = pstat.executeQuery(); ){
//
//				return rs.next();
//			}
//		}
//	}
	
	public List<ChatListDTO> isChatExist(String loginId, String opponentId) throws Exception{
		String sql = "select * from chat_list where (user1 = ? and user2 = ?) or (user1 = ? and user2 = ?)";

		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			pstat.setString(3, opponentId);
			pstat.setString(4, loginId);
			try (ResultSet rs = pstat.executeQuery(); ){

				List<ChatListDTO> list = new ArrayList<ChatListDTO>();
				
				if(rs.next()) {
					list.add(new ChatListDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
				}else {	// 결과 집합이 없을 때 오류 방지하기 위해서
					list.add(new ChatListDTO(0, null, null, "nothing"));
					
				}
				return list;
			}
		}
	}
	

	// userId를 불러와서 리스트에 띄운다.
	public List<ChatListDTO> selectAll(String loginId) throws Exception{
		String sql = "select * from chat_list where (user1 = ? or user2= ? ) and invisibleto not in ?";

		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, loginId);
			pstat.setString(2, loginId);
			pstat.setString(3, loginId);
			try (ResultSet rs = pstat.executeQuery(); ){
				List<ChatListDTO> list = new ArrayList<ChatListDTO>();
				while(rs.next()) {
					int seq = rs.getInt("seq");
					String user1 = rs.getString("user1");
					String user2 = rs.getString("user2");
					String invisibleTo = rs.getString("invisibleto");
					
					list.add(new ChatListDTO(seq, user1, user2, invisibleTo));
				}
				
				return list;
			}
		}
	}
	
	
//	// userId를 불러와서 리스트에 띄운다.
//	public List<ChatListDTO> getOpponent(String loginId) throws Exception{
//		String sql = "select * from chat_list where (user1 = ? or user2 = ?)";
//
//		try(Connection con = getConnection();
//				PreparedStatement pstat = con.prepareStatement(sql)){
//			pstat.setString(1, loginId);
//			pstat.setString(2, loginId);
//			try (ResultSet rs = pstat.executeQuery(); ){
//				List<ChatListDTO> list = new ArrayList<ChatListDTO>();
//				while(rs.next()) {
//					int seq = rs.getInt("seq");
//					String user1 = rs.getString("user1");
//					String user2 = rs.getString("user2");
//					
//					list.add(new ChatListDTO(seq, user1, user2));
//				}
//				
//				return list;
//			}
//		}
//	}
	
	// 채팅방 나가면 invisibleTo에 해당 아이디 값을 넣어준다.
	public int updateInvisible(String loginId, String opponentId) throws Exception{
		String sql = "update chat_list set invisibleTo = ? where (user1 = ? and user2 = ?) or (user1 = ? and user2 = ?)";
		
		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, loginId);
			pstat.setString(3, opponentId);
			pstat.setString(4, opponentId);
			pstat.setString(5, loginId);
			
			int result = pstat.executeUpdate();
			
			con.commit();
			return result;
			
		}
	}
	
	// invisibleTo의 값이 nobody도 아니고 내 아이디도 아니면(즉, 상대 아이디일 때) 모든 대화 삭제
	public int deleteChatList(String loginId, String opponentId) throws Exception{
		String sql = "delete from chat_list where (user1 = ? and user2 = ?) or (user1 = ? and user2 = ?) ";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			pstat.setString(3, opponentId);
			pstat.setString(4, loginId);
			
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public int setNobody(String loginId, String opponentId) throws Exception{
		String sql = "update chat_list set invisibleTo = default where (user1 = ? and user2 = ?) or (user1 = ? and user2 = ?)";
		
		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			pstat.setString(3, opponentId);
			pstat.setString(4, loginId);
			
			int result = pstat.executeUpdate();
			
			con.commit();
			return result;
			
		}
	}

}
