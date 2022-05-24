package dto;

public class ChatListDTO {
	private int seq;
	private String user1;
	private String user2;
	private String invisibleTo; 
	
	public ChatListDTO() {
		super();
	}
	public ChatListDTO(int seq, String user1, String user2) {
		super();
		this.seq = seq;
		this.user1 = user1;
		this.user2 = user2;
	}


	public ChatListDTO(int seq, String user1, String user2, String invisibleTo) {
		super();
		this.seq = seq;
		this.user1 = user1;
		this.user2 = user2;
		this.invisibleTo = invisibleTo;
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public String getInvisibleTo() {
		return invisibleTo;
	}
	public void setInvisibleTo(String invisibleTo) {
		this.invisibleTo = invisibleTo;
	}

	
	
}
