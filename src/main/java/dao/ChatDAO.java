package dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.ChatDTO;



public class ChatDAO {

	private static ChatDAO instance =  null;

	public synchronized static ChatDAO getInstance() {
		if(instance == null) {
			instance = new ChatDAO();
		}
		return instance;
	}

	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");

		return ds.getConnection();
	}

	public int insert(ChatDTO dto) throws Exception {
		String sql = "insert into chat values(chat_seq.nextval, ?, ?, ?, default,?,default)";

		try (
				Connection con = getConnection();
				PreparedStatement stat = con.prepareStatement(sql); ){
			stat.setString(1, dto.getSender());
			stat.setString(2, dto.getReceiver());
			stat.setString(3, dto.getContents());
			stat.setInt(4, dto.getRead());
			int result = stat.executeUpdate();

			return result; }
	}

	// invisibleTo가 nobody인 채팅만 
	public List<ChatDTO> getList(String loginId, String opponentId) throws Exception {
		String sql = "select * from chat where ((sender=? and receiver=?) or (sender=? and receiver=?)) and invisibleto not in(?,'allout') order by writeDate";

		try (Connection con = getConnection();
				PreparedStatement stat = con.prepareStatement(sql); ){
			stat.setString(1, loginId);
			stat.setString(2, opponentId);
			stat.setString(3, opponentId);
			stat.setString(4, loginId);
			stat.setString(5, loginId);
			try (
					ResultSet rs = stat.executeQuery(); ){

				List<ChatDTO> list = new ArrayList<>();

				while(rs.next()) {
					list.add(new ChatDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5),0,rs.getString(7)));
				}

				return list; }}
	}
	
	// 채팅을 나눈 대화를 출력
//	public List<ChatDTO> getList(String loginId, String opponentId) throws Exception {
//		String sql = "select * from chat where (sender=? and receiver=?) or (sender=? and receiver=?) order by writeDate";
//
//		try (Connection con = getConnection();
//				PreparedStatement stat = con.prepareStatement(sql); ){
//			stat.setString(1, loginId);
//			stat.setString(2, opponentId);
//			stat.setString(3, opponentId);
//			stat.setString(4, loginId);
//			try (
//					ResultSet rs = stat.executeQuery(); ){
//
//				List<ChatDTO> list = new ArrayList<>();
//
//				while(rs.next()) {
//					list.add(new ChatDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5),0));
//				}
//
//				return list; }}
//	}


	public List<ChatDTO> getChatWith(String loginId) throws Exception {
		String sql = "select sender, receiver from chat where receiver = ? or sender = ? group by sender, receiver";

		try (Connection con = getConnection();
				PreparedStatement stat = con.prepareStatement(sql); ){
			stat.setString(1, loginId);
			stat.setString(2, loginId);
			try (ResultSet rs = stat.executeQuery(); ){

				List<ChatDTO> list = new ArrayList<>();

				while(rs.next()) {
					list.add(new ChatDTO(rs.getString(1), rs.getString(2)));
				}
				return list;
			}
		}
	}

	// 최신글 하나만 출력(invisibleTo!=loginId만)
	public ChatDTO getRecentMsg(String loginId, String opponentId) throws Exception{
		String sql = "select * from chat where ((sender=? and receiver=?) or (sender=? and receiver=?)) order by seq desc";

		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			pstat.setString(3, opponentId);
			pstat.setString(4, loginId);

			try(ResultSet rs = pstat.executeQuery();){

				rs.next();
				
				int seq = rs.getInt("seq");
				String sender = rs.getString("sender");
				String receiver = rs.getString("receiver");
				String contents = rs.getString("contents");
				Timestamp writeDate = rs.getTimestamp("writeDate");
				int read = rs.getInt("read");
				String invisibleTo = rs.getString("invisibleto");


				ChatDTO dto = new ChatDTO(seq, sender, receiver, contents, writeDate, read, invisibleTo);

				// -> read를 뽑아서 chatList에 가져와서 0 or 1 인 거에 따라서 new 표시
				
				return dto;
			}
		}
	}
	
//	public ChatDTO getRecentMsg(String loginId, String opponentId) throws Exception{
//		String sql = "select * from chat where (sender=? and receiver=?) or (receiver=? and sender=?) order by writeDate desc";
//
//		try(Connection con = getConnection();
//				PreparedStatement pstat = con.prepareStatement(sql);){
//			pstat.setString(1, loginId);
//			pstat.setString(2, opponentId);
//			pstat.setString(3, loginId);
//			pstat.setString(4, opponentId);
//
//			try(ResultSet rs = pstat.executeQuery();){
//
//				rs.next();
//				//return  rs.getString(4);
//				
//				int seq = rs.getInt("seq");
//				String sender = rs.getString("sender");
//				String receiver = rs.getString("receiver");
//				String contents = rs.getString("contents");
//				Timestamp writeDate = rs.getTimestamp("writeDate");
//				int read = rs.getInt("read");
//
//
//				ChatDTO dto = new ChatDTO(seq, sender, receiver, contents, writeDate, read);
//
//				// -> read를 뽑아서 chatList에 가져와서 0 or 1 인 거에 따라서 new 표시
//				
//				return dto;
//			}
//		}
//	}
	
	// 채팅 목록을 클릭하면, read를 1로 바꾼다.
	public int updateRead(String loginId, String opponentId) throws Exception{
		String sql = "update chat set read = 1 where (receiver = ? and sender = ?)";
		
		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			
			int result = pstat.executeUpdate();
			
			con.commit();
			return result;
			
		}
	}
	
	// sender가 나일 때는 read가 무조건 1
	public int isSenderWho(String loginId) throws Exception{
		String sql = "update chat set read = 1 where sender = ?";
		
		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			
			int result = pstat.executeUpdate();
			
			con.commit();
			return result;
			
		}
	}

	public int isAlarmExist(String loginId) throws Exception{
		String sql = "select count(*) from chat where receiver=? and read=0";
		//String sql = "select count(*) from chat where sender != ? and read = 0";
		
		try(Connection con = getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			
			try(ResultSet rs = pstat.executeQuery();){
			
					rs.next();
					return rs.getInt(1);
			}
		}
	}
	
	// invisible
	public int updateChatInvi(String loginId, String opponentId) throws Exception{
		String sql = "update chat set invisibleTo = ? where ((sender=? and receiver=?) or (sender=? and receiver=?)) and invisibleTo='nobody'";
	
		try(Connection con = this.getConnection();
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
	
	// 모두 대화방을 나가면, chat 테이블의 기록을 삭제하는게 아니라 chat 테이블의 invisible을 allout으로 바꾼다.
	// 근데 이게 어려우면 그냥 삭제기능을 만들자.
	
	public int updateAllOut(String loginId, String opponentId) throws Exception{
		String sql = "update chat set invisibleto='allout' where ((sender=? and receiver=?) or (sender=? and receiver=?)) and invisibleTo=?";
		
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, loginId);
			pstat.setString(2, opponentId);
			pstat.setString(3, opponentId);
			pstat.setString(4, loginId);
			pstat.setString(5, opponentId);
			
			int result = pstat.executeUpdate();
			
			con.commit();
			
			return result;
		}
	}
 	



	//	// loginId가 먼저 채팅을 보낼때도 채팅함에 상대 아이디가 떠아한다.
	//	public List<ChatDTO> getOpponentId(String loginId ) throws Exception{
	//		String sql = "select sender, receiver from chat where receiver = ? or sender = ? group by sender, receiver";
	//
	//		try(Connection con = getConnection();
	//				PreparedStatement pstat = con.prepareStatement(sql)){
	//			pstat.setString(1, loginId);
	//			pstat.setString(2, loginId);
	//			try (ResultSet rs = pstat.executeQuery(); ){
	//
	//				List<ChatDTO> list = new ArrayList<>();
	//
	//				while(rs.next()) {
	//					list.add(new ChatDTO(rs.getString(1), rs.getString(2)));
	//				}
	//				return list;
	//			}
	//		}
	//	}

	//	// 내가 보냈든, 내가 받았든 이미 채팅방이 있으면 채팅방 생성X -> 구현 못함
	//	public boolean isChatRoomExist(String loginId) throws Exception{
	//	
	//		String sql = "select * from chat_list where user1 = ? or user2 = ?";
	//		
	//		try(Connection con = this.getConnection();
	//				PreparedStatement pstat = con.prepareStatement(sql)){
	//			pstat.setString(1, loginId);
	//			pstat.setString(2, loginId);
	//			
	//			try(ResultSet rs = pstat.executeQuery();){
	//				return rs.next();
	//			}
	//		}
	//	}

	//	// 채팅 목록
	//	public List<ChatDTO> getChatList(String loginId) throws Exception {
	//		//String sql = "select * from chat where sender=? or receiver=? order by writeDate";
	//		String sql = "select * from chat where receiver=? order by writeDate";
	//
	//		try (Connection con = getConnection();
	//				PreparedStatement stat = con.prepareStatement(sql); ){
	//			stat.setString(1, loginId);
	//			//stat.setString(2, loginId);
	//			try (ResultSet rs = stat.executeQuery(); ){
	//
	//				List<ChatDTO> list = new ArrayList<>();
	//
	//				while(rs.next()) {
	//					list.add(new ChatDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5)));
	//					//list.add(new ChatDTO(rs.getString(1), rs.getString(2)));
	//				}
	//
	//				return list; }}
	//	}


	// 그룹
	//	public List<ChatDTO> getTest(String loginId) throws Exception {
	//		String sql = "select receiver, sender from chat where receiver = ? or sender = ? group by sender, receiver";
	//
	//		try (Connection con = getConnection();
	//				PreparedStatement stat = con.prepareStatement(sql); ){
	//			stat.setString(1, loginId);
	//			stat.setString(2, loginId);
	//			try (ResultSet rs = stat.executeQuery(); ){
	//
	//				List<ChatDTO> list = new ArrayList<>();
	//
	//				while(rs.next()) {
	//					list.add(new ChatDTO(rs.getString(1),rs.getString(2)));
	//				}
	//
	//				return list;
	//			}
	//		}
	//	}

}


