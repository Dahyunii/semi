package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.ProductReplyDAO;
import dto.ProductReplyDTO;

@WebServlet("*.productReply")
public class ProductReplyController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		ProductReplyDAO dao = ProductReplyDAO.getInstance();
		Gson g = new Gson();
		
		try {
		
		// 댓글 등록
		if(uri.equals("/insert.productReply")) {
			int parentSeq = Integer.parseInt(request.getParameter("parentSeq"));
			String writer = request.getParameter("writer");
			String contents = request.getParameter("contents");
			
			int result = dao.insert(new ProductReplyDTO(0, parentSeq, writer, contents, null));
			
			response.sendRedirect("/detail.productBoard?seq=" + parentSeq);
			
		// 댓글 삭제	
		} else if(uri.equals("/delete.productReply")) {
			int seq = Integer.parseInt(request.getParameter("seq"));
			int result = dao.delete(seq);
			
		// 댓글 수정
		} else if(uri.equals("/update.productReply")) {
			int seq = Integer.parseInt(request.getParameter("seq"));
			String contents = request.getParameter("contents");
			int result = dao.update(seq, contents);
			
		// 댓글 출력
		} else if(uri.equals("/list.productReply")) {
			int parentSeq = Integer.parseInt(request.getParameter("parentSeq"));
			List<ProductReplyDTO> list = dao.getReplyList(parentSeq);
			
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
