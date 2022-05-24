package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.MemberDAO;
import dao.MultichatDAO;
import dto.MemberDTO;
import dto.MultichatDTO;
import utils.EncryptUtils;

@WebServlet("*.member")
public class MemberController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		MemberDAO dao = MemberDAO.getInstance();
		MultichatDAO dao2 = MultichatDAO.getInstance();
		Gson g = new Gson();
		
		try {
		
		// 회원가입 페이지로 이동
		if(uri.equals("/join.member")) {
			response.sendRedirect("/member/joinform.jsp");
			
		// 아이디 중복검사
		} else if(uri.equals("/duplCheck.member")) {
			String id = request.getParameter("id");
			boolean result = dao.duplCheck(id);
			response.getWriter().append(g.toJson(result));
			
		// 회원가입 양식 제출
		} else if(uri.equals("/insert.member")) {
			String id = request.getParameter("id");
			String pw = EncryptUtils.SHA512(request.getParameter("pw"));
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String email = request.getParameter("email");
			String zipcode = request.getParameter("zipcode");
			String roadAddress = request.getParameter("roadAddress");
			String jibunAddress = request.getParameter("jibunAddress");
			String specAddress = request.getParameter("specAddress");
			String personalAnswer = request.getParameter("personalAnswer");
			int reliability = 0; // 회원 신뢰도 초기값
			
			int result = dao.insert(new MemberDTO(id,pw,name,phone,email,zipcode,roadAddress,
									jibunAddress,specAddress,null,personalAnswer,reliability));
			
			response.sendRedirect("/index.jsp");
		
		// 로그인
		} else if(uri.equals("/login.member")) {
			String id = request.getParameter("id");
			String pw = EncryptUtils.SHA512(request.getParameter("pw"));
			
			boolean result = dao.isLoginOk(id, pw);
			
			// 다중채팅창에 최근 100개 채팅을 띄우기 위해 리스트 받아오기
			List<MultichatDTO> list = dao2.getMultichatList();
			System.out.println(list.size());
			
			if(result) {
				request.getSession().setAttribute("isLoginOk", 1);
				request.getSession().setAttribute("loginId", id);
				request.getSession().setAttribute("multichatList", list);
				
			} else {
				request.getSession().setAttribute("isLoginOk", 0);
			}
			
			response.sendRedirect("/index.jsp");
			
		// 로그아웃	
		} else if(uri.equals("/logout.member")) {
			request.getSession().invalidate();
			
			response.sendRedirect("/index.jsp");
			
		// 회원탈퇴	
		} else if(uri.equals("/memberOut.member")) {
			String id = (String)request.getSession().getAttribute("loginId");
			int result = dao.memberOut(id);
			
			request.getSession().invalidate();
			response.sendRedirect("/index.jsp");
			
		// 마이페이지 이동
		} else if(uri.equals("/myPage.member")) {
			String id = (String)request.getSession().getAttribute("loginId");
			MemberDTO dto = dao.myPage(id);
			
			request.setAttribute("dto", dto);
			request.getRequestDispatcher("/member/myPage.jsp").forward(request, response);
			
		// 개인정보 수정	
		} else if(uri.equals("/update.member")) {
			String id = (String)request.getSession().getAttribute("loginId");
			
			String phone = request.getParameter("phone");
			String email = request.getParameter("email");
			String zipcode = request.getParameter("zipcode");
			String roadAddress = request.getParameter("roadAddress");
			String jibunAddress = request.getParameter("jibunAddress");
			String specAddress = request.getParameter("specAddress");
			String personalAnswer = request.getParameter("personalAnswer");
			
			int result = dao.update(new MemberDTO(id, null, null, phone, email, zipcode, roadAddress, jibunAddress, specAddress, null, personalAnswer, 0));
			response.sendRedirect("/myPage.member");
			
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
