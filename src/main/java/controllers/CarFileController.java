package controllers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.CarFileDAO;
import dto.CarFileDTO;


@WebServlet("*.carFile")
public class CarFileController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		CarFileDAO carfiledao = new CarFileDAO();
		CarFileDTO carfiledto = new CarFileDTO();
		Gson g=new Gson();

		try {

			if (uri.equals("/upload.carFile")) {

				int maxSize = 1024 * 1024 * 10; // 10MB 로 파일의 최대크기를 설정
				
				String savaPath = request.getServletContext().getRealPath("carfiles"); //파일이 저장되는 경로를 정해준다.carfiles는 폴더명
				
				System.out.println(savaPath);
				
				File filePath = new File(savaPath);
				
				if (!filePath.exists()) { // 파일 업로드 할 폴더가 존재하지 않는다면 새로 생성.exists는 파일이 존재하는지 안하는지 확인
					filePath.mkdir();    // 존재하지 않으면 파일 생성
				}
				
				MultipartRequest multi = new MultipartRequest(request, savaPath, maxSize, "UTF8",new DefaultFileRenamePolicy());
				
				//리퀘스트 업그레이드,어디에 저장할지 경로,최대사이즈,한글깨짐이 생기는걸 감안해서 UTF8 사용,그리고 이 객체를 new DefaultFileRenamePolicy() 넣어준다.
				//마지막 객체는 파일이름이 중복될때 해결해주는 부분이다.파일이 중복되면 파일이름뒤에 1 2 3 를 붙여준다.
				
				// 클라이언트가 전송한 Multipart/form-data 는 httpServletRequest가 분석할 수 없기때문에,
				// MultipartRequest로 업그레이드 한다
				// 동시에 업로드 된 파일을 지정된 경로에 지정된 사이즈 제한 안에서 저장한다.

				// 파일 관련된 정보
				// 파일 sequence
				// 클라리언트가 업로드하는 원본파일 이름
//				String oriName = multi.getOriginalFileName("file"); // 이거는 index.jsp 에 있는 name="file" 을 의미한다
//				String sysName = multi.getFilesystemName("file");   //여러 파일을 올리고 싶으면 여기 file 이라고 써져있는걸 파일마다 다 다르게 써줘야 한다.
//				int parent_seq = 0;
//
//				carfiledao.insert(new FilesDTO(0, oriName, sysName, parent_seq));
				
				int parentSeq = 0;

				Enumeration<String> e =multi.getFileNames();
				
				while(e.hasMoreElements()) {
					String name=e.nextElement();
//					System.out.println(name);
					String oriName = multi.getOriginalFileName(name); // 이거는 index.jsp 에 있는 name="file" 을 의미한다
					String sysName = multi.getFilesystemName(name);   //여러 파일을 올리고 싶으면 여기 file 이라고 써져있는걸 파일마다 다 다르게 써줘야 한다.
					carfiledao.insert(new CarFileDTO(0, parentSeq, oriName, sysName));
					
				}
				
				
				
				// 파일을 Database 에 넣는게 아닌가요? 가능하지만 넣지 않습니다.
				// 파일을 보관하지 하지만 파일에 관련된 정보를 보관해야합니다.
				// 파일은 폴더에 저장을 함.이 폴더에 파일이 저장이 되어있다라고 하는 파일이름같은 것들이 첨부되면 파일이 이름이 뭔지 ,파일이 업로드된
				// 게시글번호 ,업로드 날짜 등 db에 보관을 해야함
				//

				response.sendRedirect("/index.jsp");
			}else if(uri.equals("/list.carFile")) {
				response.setCharacterEncoding("utf8");
				PrintWriter pw=response.getWriter();
				
				int parentSeq = Integer.parseInt(request.getParameter("dtoseq"));
				
//				System.out.println("부모게시판 번호 : "+parentseq);
				
				List<CarFileDTO> list = new ArrayList<>();
				list=carfiledao.selectall(parentSeq);
				
				String result=g.toJson(list);
				pw.append(result);
				
			}else if(uri.equals("/download.carFile")){
				
				String filePath=request.getServletContext().getRealPath("carfiles");
				String oriName=request.getParameter("oriName");
				String sysName=request.getParameter("sysName");
				
//				System.out.println("경로: "+filePath+"/"+sysname);
				
				File target=new File(filePath+"/"+sysName);
				//클라이언트로부터 받아온 이름으로 Target(다운로드받을) 파일을 저장
				
				oriName=new String(oriName.getBytes("utf8"),"ISO-8859-1");
				
				response.reset();
				response.setHeader("Content-Disposition", "attachment;filename=\""+oriName+"\"");  //ori_name은 다운로드를 받는 파일의 이름이다.
				// HTML/CSS/JS 를 전달하는 일반적인 Response의 속성을 제거하고,파일을 전송 설정을 세팅
				
				
				byte[] fileContents = new byte[(int)target.length()];
				//램으로 로딩 된 파일의 내용을 보관할 저장소
				try(DataInputStream dis=new DataInputStream(new FileInputStream(target));
						ServletOutputStream sos=response.getOutputStream();){
					//하드디스크에 있는 파일의 내용을 RAM으로 로딩하기 위한 스트림
					//RAM으로 로딩 된 파일의 내용을 네트워크(response)로 전송하기 위한 스트림
					
					
					dis.readFully(fileContents);  //하드디스크의 파일 내용을 모두 fileContents로 로딩
					
					sos.write(fileContents);
					sos.flush(); // response에 파일 내용을 담아 전송.
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.jsp");
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
