package dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProductBoardDTO {

	private int seq;
	private String title;
	private String contents;
	private String writer;
	private String pname;
	private int price;
	private String category;
	private String sellingOption;
	private String status;
	private int viewCount;
	private Timestamp writeDate;
	
	public ProductBoardDTO() {

	}
	
	public ProductBoardDTO(int seq, String title, String contents, String writer, String pname, int price, String category,
			String sellingOption, String status, int viewCount, Timestamp writeDate) {
		super();
		this.seq = seq;
		this.title = title;
		this.contents = contents;
		this.writer = writer;
		this.pname = pname;
		this.price = price;
		this.category = category;
		this.sellingOption = sellingOption;
		this.status = status;
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

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSellingOption() {
		return sellingOption;
	}

	public void setSellingOption(String sellingOption) {
		this.sellingOption = sellingOption;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
		return sdf.format(this.writeDate);
	}
	
	
}
