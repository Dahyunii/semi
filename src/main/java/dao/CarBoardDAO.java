package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.CarBoardDTO;

public class CarBoardDAO {
	private static CarBoardDAO instance = null;

	public synchronized static CarBoardDAO getInstance() {
		if (instance == null) {
			instance = new CarBoardDAO();
		}
		return instance;
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	


	public int getSeqNextVal() throws Exception {

		String sql = "select car_seq.nextval from dual";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			rs.next();
			return rs.getInt(1);
		}

	}

	public int insert(CarBoardDTO dto) throws Exception {
		String sql = "insert into car_board values(?,?,?,?,0,sysdate)";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, dto.getSeq());
			pstat.setString(2, dto.getTitle());
			pstat.setString(3, dto.getContents());
			pstat.setString(4, dto.getWriter());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public List<CarBoardDTO> selectAll() throws Exception {
		String sql = "select * from car_board order by seq desc";

		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			List<CarBoardDTO> list = new ArrayList<>();
			while (rs.next()) {
				CarBoardDTO dto = new CarBoardDTO();
				dto.setSeq(rs.getInt("seq"));
				dto.setTitle(rs.getString("title"));
				dto.setContents(rs.getString("contents"));
				dto.setWriter(rs.getString("writer"));
				dto.setViewCount(rs.getInt("viewCount"));
				dto.setWriteDate(rs.getTimestamp("writedate"));
				
				
				list.add(dto);
			}
			return list;
		}
	}

	public CarBoardDTO selectBySeq(int seq) throws Exception {
		String sql = "select * from car_board where seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery();) {
				rs.next();
				CarBoardDTO dto = new CarBoardDTO();
				
				dto.setSeq(rs.getInt("seq"));
				dto.setTitle(rs.getString("title"));
				dto.setContents(rs.getString("contents"));
				dto.setWriter(rs.getString("writer"));
				dto.setViewCount(rs.getInt("viewCount"));
				dto.setWriteDate(rs.getTimestamp("writedate"));
				return dto;
			}
		}
	}

	public int deleteBySeq(int seq) throws Exception {
		String sql = "delete from car_board where seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public int updateBySeq(int seq, String title, String contents) throws Exception {
		String sql = "update car_board set title=?, contents=? where seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, title);
			pstat.setString(2, contents);
			pstat.setInt(3, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public int updateViewCount(int seq) throws Exception {

		String sql = "update car_board set viewCount=viewCount+1 where seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	private int getRecordTotalCount() throws Exception {
		String sql = "select count(*) from car_board";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			rs.next();
			return rs.getInt(1);

		}
	}
	
	public String getPageNavi(int currentPage) throws Exception {
		  
		int recordTotalCount = this.getRecordTotalCount(); // 총 데이터의 개수 -> 향후 실제 데이터베이스의 개수를 세어와야 함.

	      int recordCountPerPage = 10;    // 한 페이지에 몇개의 게시글을 보여 줄 건지
	      int naviCountPerPage = 10;     // 한 페이지에 몇개의 네비를 보여 줄 건지

	      int pageTotalCount = 0;       // 총 몇개의 페이지가 필요한가?

	      if(recordTotalCount % recordCountPerPage > 0) { // 전체 페이지 + 1 해야 함.
	         pageTotalCount = recordTotalCount / recordCountPerPage + 1;
	      }else {
	         pageTotalCount = recordTotalCount / recordCountPerPage;
	      }
	      
	      
	      
	      if(currentPage < 1) {
	         currentPage = 1;
	      }else if(currentPage > pageTotalCount) {
	         currentPage = pageTotalCount;
	      }
	      

	      int startNavi = (currentPage-1) / naviCountPerPage * naviCountPerPage + 1;
	      int endNavi = startNavi + naviCountPerPage - 1;
	      
	      
	      if(endNavi > pageTotalCount) {
	         endNavi = pageTotalCount;
	      }

//	      System.out.println("현재 페이지 : " + currentPage);
//	      System.out.println("네비 시작 값 : " + startNavi);
//	      System.out.println("네비 끝 값 : " + endNavi);

	      boolean needNext = true;
	      boolean needPrev = true;
	      
	      if(startNavi == 1) {
	         needPrev = false;
	      }
	      
	      if(endNavi == pageTotalCount) {
	         needNext = false;
	      }
	      
	      
	      StringBuilder sb = new StringBuilder();
	      
	      if(needPrev) {
	         sb.append("<a href='list.carBoard?cpage="+(startNavi-1)+"'>< </a>");
	      }
	      
	      for(int i = startNavi;i <= endNavi;i++) {
	         if(currentPage == i) {
	            sb.append("<a href='list.carBoard?cpage="+i+"'>[" + i + "] </a>");
	         }else {
	        	sb.append("<a href='list.carBoard?cpage="+i+"'>" + i + " </a>");
	         }
	      }
	      
	      if(needNext) {
	         sb.append("<a href='list.carBoard?cpage="+(endNavi+1)+"'>></a>");
	      }
	      
	      return sb.toString();
	}
	
	public List<CarBoardDTO> selectByPage(int cpage) throws Exception{
		
		int start=cpage*10-9;
		int end = cpage*10;
		
		String sql="select * from (select row_number() over(order by seq desc) line,car_board.* from car_board) where line between ? and ?";
		
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
				pstat.setInt(1, start);
				pstat.setInt(2, end);
				
				try(ResultSet rs = pstat.executeQuery();){
					List<CarBoardDTO> list = new ArrayList<>();
					while(rs.next()) {
						CarBoardDTO dto = new CarBoardDTO();
						dto.setSeq(rs.getInt("seq"));
						dto.setTitle(rs.getString("title"));
						dto.setContents(rs.getString("contents"));
						dto.setWriter(rs.getString("writer"));
						dto.setWriteDate(rs.getTimestamp("writeDate"));
						dto.setViewCount(rs.getInt("viewCount"));
						
						
						list.add(dto);
					}
					return list;
				}
			}
	}
	
	
	public int insertDummy() throws Exception{
	String sql="insert into car_board values(car_seq.nextval,?,?,?,0,sysdate)";
	
	Connection con = this.getConnection();
			
	for(int i=0;i<144;i++) {
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, "title : "+i);
		pstat.setString(2, "contents : "+i);
		pstat.setString(3, "writer : "+i);
		pstat.executeUpdate();
		con.commit();
	}
	con.close();
	return 0;
	
	}

	public static void main(String[] args) throws Exception{
		CarBoardDAO dao=new CarBoardDAO();
		dao.insertDummy();		
	}
	
}
