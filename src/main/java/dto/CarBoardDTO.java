package dto;

import java.sql.Timestamp;

public class CarBoardDTO {
	private int seq;
	private String title;
	private String contents;
	private String writer;
	private int viewCount;
	private Timestamp writeDate;
	
	public CarBoardDTO() {
		
	}
	
	
	public CarBoardDTO(int seq, String title, String contents, String writer, int viewCount, Timestamp writeDate) {
		
		this.seq = seq;
		this.title = title;
		this.contents = contents;
		this.writer = writer;
		this.viewCount = viewCount;
		this.writeDate = writeDate;
	}


	public int getSeq() {
		return seq;
	}


	public void setSeq(int seq) {
		this.seq = seq;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public String getWriter() {
		return writer;
	}


	public void setWriter(String writer) {
		this.writer = writer;
	}


	public int getViewCount() {
		return viewCount;
	}


	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}


	public Timestamp getWriteDate() {
		return writeDate;
	}


	public void setWriteDate(Timestamp writeDate) {
		this.writeDate = writeDate;
	}
	
	
	
	
	
	
	
	
	
	
}
