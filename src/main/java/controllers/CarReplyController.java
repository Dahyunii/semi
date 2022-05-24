package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CarReplyDAO;



@WebServlet("*.carReply")
public class CarReplyController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String uri = request.getRequestURI();
		
		
		try {
			if(uri.equals("/delete.carReply")) {
				int seq = Integer.parseInt(request.getParameter("replyseq"));
				CarReplyDAO replydao=new CarReplyDAO();
				int result=replydao.replydelete(seq);
				
				boolean success=true;  //만약 여기까지 왔다면 성공했을테니 true 넣어준다
				
				
				response.getWriter().append(String.valueOf(success));
				
			}else if(uri.equals("/modify.carReply")) {
//				System.out.println("modifyseq : "+request.getParameter("modifyseq"));
//				System.out.println(request.getParameter("modifyseq"));
//				System.out.println("공백있는지 확인 : "+request.getParameter("modifyseq").trim()+"ㅇㅇㅇㅇㅇ");
				int modifyseq = Integer.parseInt(request.getParameter("modifyseq").trim());
				
				String contents=request.getParameter("conte");
				
//				System.out.println("contents : "+request.getParameter("conte"));
				
				CarReplyDAO replydao=new CarReplyDAO();
				int result=replydao.replymodify(modifyseq,contents);
				

			}else if(uri.equals("/enroll.carReply")) {
				
				int parentSeq=Integer.parseInt(request.getParameter("parent_seq"));
				String commenttext=request.getParameter("commenttext");
				String id = (String)request.getSession().getAttribute("loginID");
				
				CarReplyDAO replydao=new CarReplyDAO();
				while(true) {
					try {
						int result1 = replydao.replyinsert(id, commenttext, parentSeq);
						break;
					}catch(Exception e) {
						break;
					}
				}
				int seq = Integer.parseInt(request.getParameter("parent_seq"));
				
				response.sendRedirect("/detail.carBoard?seq="+seq);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.jsp");
			return;
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
