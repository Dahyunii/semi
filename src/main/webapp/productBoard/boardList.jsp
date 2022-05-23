<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<style>

	a{
		text-decoration:none;
		color:black;
	}

	a:hover{
		cursor:pointer;
		text-decoration:underline;
		color:dodgerblue;
	}
	
</style>
<body>

	<table border="1" align=center width="800">
        <tr>
            <th colspan="5">자유게시판
        </tr>
        <tr align="center">
            <td width=30>#</td>
            <td width=500>제목</td>
            <td>작성자</td>
            <td>날짜</td>
            <td>조회</td>
        </tr>
        
        	<c:forEach var="i" items="${boardList }">
        
        	<tr>
            <td align="center">${i.seq }
            <td align="center"><a href="/detail.productBoard?seq=${i.seq }">${i.title }</a>
            <td align="center">${i.writer }
            <td align="center">${i.formedDate }
            <td align="center">${i.viewCount }
        	</tr>
        
        	</c:forEach>
        	
        <tr>
            <td colspan="5" align="center">
            	<a href="/list.productBoard?currPage=1" id=toStart>[처음]&nbsp;</a>
        		<a href="/list.productBoard?currPage=${currPage-11 - (currPage-11)%10 + 1 }" id=prev>[이전]&nbsp;</a>
        		<a href="/list.productBoard?currPage=${currPage+9 - (currPage+9)%10 + 1 }" id=next>[다음]&nbsp;</a>
        		<a href="/list.productBoard?currPage=${totalPage }" id=toEnd>[마지막]</a>
            </td>
        </tr>
        <tr>
            <td colspan="5" align="right">
            	<a href="/index.jsp"><button>메인으로</button></a>
            	<a href="/write.productBoard"><button>작성하기</button></a>
            	<a href="/insert50.productBoard"><button>글 50개 작성하기</button></a>
        </tr>
    </table>
    
    
    
    <script>
    
    	let startNavi = ${currPage-1 }-(${currPage-1 }%10)+1
    
    	// totalPage 10 이하 - 처음 이전 다음 마지막 모두 없음
    	if(${totalPage } <= 10) {
    		$("#prev").css("display", "none")
    		$("#next").css("display", "none")
    		$("#toStart").css("display", "none")
    		$("#toEnd").css("display", "none")
    		
    		for(let i=1; i<=${totalPage }; i++) {
    			let seq = $("<a href=/list.productBoard?currPage=" + i + " id=a" + i + ">" + i + "</a>")
    			$("#next").before(seq)
    			$("#next").before("\u00a0\u00a0")
    		}
    		
    	// totalPage 10 초과, 현재페이지 10페이지 이하 - 처음 이전 없음
    	} else if(${currPage } <= 10) {
    		$("#prev").css("display", "none")
    		$("#toStart").css("display", "none")
    		
    		for(let i=startNavi; i<=startNavi+9; i++) {
    			let seq = $("<a href=/list.productBoard?currPage=" + i + " id=a" + i + ">" + i + "</a>")
    			$("#next").before(seq)
    			$("#next").before("\u00a0\u00a0")
    		}
    		
    	// 현재 페이지가 10페이지씩 끊었을때 마지막일때 ex) 31~35 페이지 (totalPage 35) - 다음 마지막 없음
    	} else if(${currPage - (currPage-1)%10 } == ${totalPage - (totalPage-1)%10 }) {
    		$("#next").css("display", "none")
    		$("#toEnd").css("display", "none")
    		
    		for(let i=startNavi; i<=${totalPage }; i++) {
    			let seq = $("<a href=/list.productBoard?currPage=" + i + " id=a" + i + ">" + i + "</a>")
    			$("#next").before(seq)
    			$("#next").before("\u00a0\u00a0")
    		}
    		
    	// 그 외 일반적인 경우 - 처음 이전 다음 마지막 모두 있음
    	} else {
    		for(let i=startNavi; i<=startNavi+9; i++) {
    			let seq = $("<a href=/list.productBoard?currPage=" + i + " id=a" + i + ">" + i + "</a>")
    			$("#next").before(seq)
    			$("#next").before("\u00a0\u00a0")
    		}
    	}
    	
    	// 현재 페이지에 색깔과 밑줄
    	$("#a" + ${currPage }).css({"color":"dodgerblue", "text-decoration":"underline"})
    	
    </script>

</body>
</html>