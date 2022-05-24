package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.CarFileDTO;

public class CarFileDAO {

private static CarFileDTO instance=null;
	
	public synchronized static CarFileDTO getInstance() {
		if(instance==null) {
			instance = new CarFileDTO();
		}
		return instance;
	}
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/orcl");
		return ds.getConnection();
	}
	
	
	
	

	public int insert(CarFileDTO dto) throws Exception {
		String sql = "insert into car_file values(carfile_seq.nextval, ?,?,?)";

		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, dto.getParentSeq());
			pstat.setString(2, dto.getOriName());
			pstat.setString(3, dto.getSysName());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public List<CarFileDTO> selectall(int parentseq) throws Exception {
		String sql = "select * from car_file where parentseq=?";

		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, parentseq);
			
			try(ResultSet rs = pstat.executeQuery();){
				List<CarFileDTO> list = new ArrayList<>();
				while(rs.next()) {
					CarFileDTO dto = new CarFileDTO();
					dto.setSeq(rs.getInt("seq"));					
					dto.setParentSeq(rs.getInt("parentSeq"));
					dto.setOriName(rs.getString("oriName"));
					dto.setSysName(rs.getString("sysName"));

					list.add(dto);
				}
				return list;
			}
			
		}		
	}
	
}
