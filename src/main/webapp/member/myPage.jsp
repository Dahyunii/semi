<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<style>

	*{box-sizing:border-box;}
  	th{padding:10px; background-color:#fbe5e7;}
  	td:first-child{width:150px; padding:10px; background-color:#f0fff0; text-align:right;}
  	td:last-child{width:500px; background-color:ivory;}
  	.fixed{padding:10px;}
	.editable{width:100%; height:50%; padding:10px; border:0px; font-size:medium;}
	
</style>
<body>

	<form action="/update.member" method=post>

	<table border=1 align=center>
	
		<tr><th colspan=2 align=center>마이페이지
		
		<tr>
			<td>아이디 
			<td class=fixed>${dto.id }
		<tr>
			<td>이름
			<td class=fixed>${dto.name }
		<tr>
			<td>연락처 
			<td><input type=text name=phone value="${dto.phone }" class="editable" disabled>
		<tr>
			<td>이메일 
			<td><input type=text name=email value="${dto.email }" class="editable" disabled>
		<tr>
			<td>우편번호
			<td id=box2><input type=text name=zipcode id=zipcode value="${dto.zipcode }" class="editable" disabled>
		<tr>
			<td>도로명주소
			<td><input type=text name=roadAddress id=roadAddress value="${dto.roadAddress }" class="editable" disabled>
		<tr>
			<td>지번주소
			<td><input type=text name=jibunAddress id=jibunAddress value="${dto.jibunAddress }" class="editable" disabled>
		<tr>
			<td>상세주소 
			<td><input type=text name=specAddress value="${dto.specAddress }" class="editable" disabled>
		<tr>
			<td>개인질문답변
			<td><input type=text name=personalAnswer value="${dto.personalAnswer }" class="editable" disabled>
		<tr>
			<td>회원신뢰도
			<td class=fixed>${dto.reliability }
		<tr>
			<td>가입날짜
			<td class=fixed>${dto.formedDate }
		<tr>
			<th colspan=2 id=box1><button type=button id=modify>수정하기</button> <button type=button id=back>뒤로가기</button>
			
	</table>
	
	</form>
	
	
	
	<script>
	
		$("#modify").on("click", function(){
			$("#modify").css("display", "none")
			$("#back").css("display", "none")
			$(".editable").removeAttr("disabled")
			$(".editable").css("border", "1px solid black")
			
			let ok = $("<button>")
			ok.text("수정 완료")
			$("#box1").append(ok)
			$("#box1").append("\u00a0")
			
			let cancel = $("<button>")
			cancel.text("취소")
			cancel.attr("type", "button")
			cancel.on("click", function(){
				location.reload();
			})
			$("#box1").append(cancel)
			
			
			let findBtn = $("<button style=margin-left:10px;>")
			findBtn.text("주소찾기")
			findBtn.attr("type", "button")
			$("#zipcode").css("width", "80%")
			$("#box2").append(findBtn)

			
			findBtn.on("click", function(){
				new daum.Postcode({
	                oncomplete: function (data) {

	                    document.getElementById('zipcode').value = data.zonecode;
	                    document.getElementById("roadAddress").value = data.roadAddress;
	                    document.getElementById("jibunAddress").value = data.jibunAddress;

	                }
	            }).open();
			})
		})
		
		
		$("#back").on("click", function(){
			location.href="/index.jsp"
		})
		
	</script>


</body>
</html>