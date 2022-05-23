package dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MemberDTO {

	private String id;
	private String pw;
	private String name;
	private String phone;
	private String email;
	private String zipcode;
	private String roadAddress;
	private String jibunAddress;
	private String specAddress;
	private Timestamp joinDate;
	private String personalAnswer;
	private int reliability;
	
	public MemberDTO() {

	}

	public MemberDTO(String id, String pw, String name, String phone, String email, String zipcode, String roadAddress,
			String jibunAddress, String specAddress, Timestamp joinDate, String personalAnswer, int reliability) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.zipcode = zipcode;
		this.roadAddress = roadAddress;
		this.jibunAddress = jibunAddress;
		this.specAddress = specAddress;
		this.joinDate = joinDate;
		this.personalAnswer = personalAnswer;
		this.reliability = reliability;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getRoadAddress() {
		return roadAddress;
	}

	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

	public String getJibunAddress() {
		return jibunAddress;
	}

	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}

	public String getSpecAddress() {
		return specAddress;
	}

	public void setSpecAddress(String specAddress) {
		this.specAddress = specAddress;
	}

	public Timestamp getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Timestamp joinDate) {
		this.joinDate = joinDate;
	}

	public String getPersonalAnswer() {
		return personalAnswer;
	}

	public void setPersonalAnswer(String personalAnswer) {
		this.personalAnswer = personalAnswer;
	}

	public int getReliability() {
		return reliability;
	}

	public void setReliability(int reliability) {
		this.reliability = reliability;
	}
	
	// 날짜 형태 변경
	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		return sdf.format(this.joinDate);
	}
	
	
}
