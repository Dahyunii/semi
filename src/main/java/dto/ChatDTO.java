package dto;

import java.sql.Timestamp;

public class ChatDTO {
	private int seq;
	private String sender;
	private String receiver;
	private String contents;
	private Timestamp writeDate;
	private int read;
	private String invisibleTo;

	public ChatDTO() {

	}

	public ChatDTO( String sender, String receiver) {
		super();
		this.receiver = receiver;
		this.sender = sender;
	}

	public ChatDTO(int seq, String sender, String receiver, String contents, Timestamp writeDate, int read) {
		super();
		this.seq = seq;
		this.sender = sender;
		this.receiver = receiver;
		this.contents = contents;
		this.writeDate = writeDate;
		this.read = read;
	}

	public ChatDTO(int seq, String sender, String receiver, String contents, Timestamp writeDate, int read,
			String invisibleTo) {
		super();
		this.seq = seq;
		this.sender = sender;
		this.receiver = receiver;
		this.contents = contents;
		this.writeDate = writeDate;
		this.read = read;
		this.invisibleTo = invisibleTo;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Timestamp getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Timestamp writeDate) {
		this.writeDate = writeDate;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public String getInvisibleTo() {
		return invisibleTo;
	}

	public void setInvisibleTo(String invisibleTo) {
		this.invisibleTo = invisibleTo;
	}

}
