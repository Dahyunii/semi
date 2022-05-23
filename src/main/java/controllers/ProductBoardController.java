package controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.ProductBoardDAO;
import dao.ProductImageDAO;
import dao.ProductReplyDAO;
import dto.ProductBoardDTO;
import dto.ProductImageDTO;

@WebServlet("*.productBoard")
public class ProductBoardController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		ProductBoardDAO dao = ProductBoardDAO.getInstance();
		ProductImageDAO dao2 = ProductImageDAO.getInstance();
		ProductReplyDAO dao3 = ProductReplyDAO.getInstance();
		Gson g = new Gson();
		
		try {
			
		// 게시판으로 이동 (목록 출력)
		if(uri.equals("/list.productBoard")) {
			int currPage = Integer.parseInt(request.getParameter("currPage"));
			int totalPage = dao.getTotalPage();
			List<ProductBoardDTO> list = dao.getBoardList(currPage);
			
			// 특정 페이지 보는 중에 게시글 열람 후 기존에 보고 있던 페이지로 돌아가기 위한 세션 속성
			request.getSession().setAttribute("prevPage", currPage);
			
			// 게시글 목록과 네비를 출력하기 위한 속성
			request.setAttribute("currPage", currPage);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("boardList", list);

			request.getRequestDispatcher("/productBoard/boardList.jsp").forward(request, response);
		
		// 게시글 작성하러 가기
		} else if(uri.equals("/write.productBoard")) {
			response.sendRedirect("/productBoard/boardForm.jsp");
			
		// 게시글 작성 완료
		} else if(uri.equals("/writeConfirm.productBoard")) {
			int maxSize = 1024 * 1024 * 10;
			String savePath = request.getServletContext().getRealPath("file");
			System.out.println(savePath);
			File filePath = new File(savePath);
			if(!filePath.exists()) {filePath.mkdir();}
			MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
			
			String title = multi.getParameter("title");
			String contents = multi.getParameter("contents");
			String writer = (String)request.getSession().getAttribute("loginId");
			String pname = multi.getParameter("pname");
			
			// 물물교환만 선택시 가격 없음
			int price;
			if(multi.getParameter("price").equals("없음")) {
				price = 0;
			} else {
				price = Integer.parseInt(multi.getParameter("price"));
			}
			
			String category = multi.getParameter("category");
			String sellingOption = multi.getParameter("sellingOption");
			// 게시글 작성시 판매상태 = 판매중
			String status = "판매중";
			int viewCount = 0;
			Timestamp writeDate = null;
			
			// 게시글의 seq 와 첨부된 파일의 parent_seq 를 매칭시키기 위해 앞으로 작성될 nextval 값을
//			// 미리 가져와서 게시글과 파일에 동일하게 넣어준다.
			int seq = dao.getParentSeq();
			
			dao.insert(new ProductBoardDTO(seq, title, contents, writer, pname, price, category, sellingOption,
						status, viewCount, writeDate));
			
			
			Enumeration<String> e = multi.getFileNames();
			
			List<String> arr = new ArrayList<>();
			
			while(e.hasMoreElements()) {
				String name = e.nextElement();
				if(name.equals("files") || multi.getOriginalFileName(name) == null) {
					continue;
				}
				
				String oriName = multi.getOriginalFileName(name);
				String sysName = multi.getFilesystemName(name);
				arr.add(oriName);
				arr.add(sysName);
					
			}
			
			if(arr.size() == 2) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			} else if(arr.size() == 4) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(2), arr.get(3)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			} else if(arr.size() == 6) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(4), arr.get(5)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(2), arr.get(3)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			}
			
			
			response.sendRedirect("/list.productBoard?currPage=1");
		
		// 게시글 내용 보기 - 게시글의 내용, 댓글, 첨부파일 가져오기
		} else if(uri.equals("/detail.productBoard")) {
			int seq = Integer.parseInt(request.getParameter("seq"));
			ProductBoardDTO BoardDTO = dao.getBoardDetail(seq);
			int result = dao.updateViewCount(seq);
			request.setAttribute("BoardDTO", BoardDTO);
			
			List<ProductImageDTO> imageList = dao2.getImageList(seq);
			request.setAttribute("imageList", imageList);
			
//			List<ReplyDTO> ReplyList = dao3.getReplyList(seq);
//			request.setAttribute("ReplyList", ReplyList);
			
			request.getRequestDispatcher("/productBoard/boardDetail.jsp").forward(request, response);
			
		// 게시글 삭제하기	
		} else if(uri.equals("/delete.productBoard")) {
			int seq = Integer.parseInt(request.getParameter("seq"));
			int result = dao.delete(seq);
			
			response.sendRedirect("/list.productBoard?currPage=1");
			
		// 게시글 수정하기	
		} else if(uri.equals("/update.productBoard")) {
			int maxSize = 1024 * 1024 * 10;
			String savePath = request.getServletContext().getRealPath("file");
			File filePath = new File(savePath);
			if(!filePath.exists()) {filePath.mkdir();}
			MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
			
			String title = multi.getParameter("title");
			String contents = multi.getParameter("contents");
			String pname = multi.getParameter("pname");
			
			// 물물교환만 선택시 가격 없음
			int price;
			if(multi.getParameter("price").equals("없음")) {
				price = 0;
			} else {
				price = Integer.parseInt(multi.getParameter("price"));
			}
			
			String category = multi.getParameter("category");
			String sellingOption = multi.getParameter("sellingOption");
			// 게시글 작성시 판매상태 = 판매중
			
			// 수정할 게시글 seq
			int seq = Integer.parseInt(multi.getParameter("seq"));
			
			dao.update(new ProductBoardDTO(seq, title, contents, null, pname, price, category, sellingOption,
						null, 0, null));
			
			
			Enumeration<String> e = multi.getFileNames();
			
			List<String> arr = new ArrayList<>();
			
			while(e.hasMoreElements()) {
				String name = e.nextElement();
				if(name.equals("files") || multi.getOriginalFileName(name) == null) {
					continue;
				}
				
				String oriName = multi.getOriginalFileName(name);
				String sysName = multi.getFilesystemName(name);
				arr.add(oriName);
				arr.add(sysName);
					
			}
			
			if(arr.size() == 2) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			} else if(arr.size() == 4) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(2), arr.get(3)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			} else if(arr.size() == 6) {
				dao2.insert(new ProductImageDTO(0, seq, arr.get(4), arr.get(5)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(2), arr.get(3)));
				dao2.insert(new ProductImageDTO(0, seq, arr.get(0), arr.get(1)));
			}
			
			
			response.sendRedirect("/detail.productBoard?seq=" + seq);
			
		// 게시글 수정 과정에서 기존이미지 삭제된 것 찾아서 삭제하기
		} else if(uri.equals("/deleteSavedImage.productBoard")) {
			int imageSeq = Integer.parseInt(request.getParameter("imageSeq"));
			int result = dao2.delete(imageSeq);
			
		// 무한스크롤 이미지 정보 가져오기
		} else if(uri.equals("/scrollImage.productBoard")) {
			List<String> list = dao.getScrollImage();
			
			response.getWriter().append(g.toJson(list));
		
		// 무한스크롤 게시글 정보 가져오기
		} else if(uri.equals("/scrollBoard.productBoard")) {
			List<ProductBoardDTO> list = dao.getBoardAll();
			
			response.getWriter().append(g.toJson(list));
			
		}
		
		
		
		
		
		
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error.jsp");
		}
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
