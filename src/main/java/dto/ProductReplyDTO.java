package dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProductReplyDTO {

	private int seq;
	private int parentSeq;
	private String writer;
	private String contents;
	private Timestamp writeDate;
	private String formedDate;
	
	public ProductReplyDTO() {
		
	}

	public ProductReplyDTO(int seq, int parentSeq, String writer, String contents, Timestamp writeDate) {
		this.seq = seq;
		this.parentSeq = parentSeq;
		this.writer = writer;
		this.contents = contents;
		this.writeDate = writeDate;
		
		if(writeDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
			this.formedDate = sdf.format(writeDate);
		}
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
	
	public String getFormedDate() {
		return formedDate;
	}

	public void setFormedDate(String formedDate) {
		this.formedDate = formedDate;
	}
	
	
}
