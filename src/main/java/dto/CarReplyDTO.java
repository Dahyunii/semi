package dto;

import java.sql.Timestamp;

public class CarReplyDTO {
	private int seq;
	private int parentSeq;
	private String writer;
	private String contents;
	private Timestamp writeDate;
	
	
	
	public CarReplyDTO() {}


	public CarReplyDTO(int seq, int parentSeq, String writer, String contents, Timestamp writeDate) {
		this.seq = seq;
		this.parentSeq = parentSeq;
		this.writer = writer;
		this.contents = contents;
		this.writeDate = writeDate;
	}



	public int getSeq() {
		return seq;
	}



	public void setSeq(int seq) {
		this.seq = seq;
	}



	public int getParentSeq() {
		return parentSeq;
	}



	public void setParentSeq(int parentSeq) {
		this.parentSeq = parentSeq;
	}



	public String getWriter() {
		return writer;
	}



	public void setWriter(String writer) {
		this.writer = writer;
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
	
	
	
	
}
