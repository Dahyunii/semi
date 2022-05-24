package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.MultichatDAO;
import dto.MultichatDTO;

@WebServlet("*.multichatAjax")
public class MultichatAjaxController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		MultichatDAO dao = MultichatDAO.getInstance();
		Gson g = new Gson();
		
		try {
		
		if(uri.equals("/list.multichatAjax")) {
			List<MultichatDTO> list = dao.getMultichatList();
			System.out.println(list.get(0).getWriter());
			
			response.getWriter().append(g.toJson(list));
		}
			
			
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
