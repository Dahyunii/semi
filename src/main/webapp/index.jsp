<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>

	*{box-sizing:border-box;}
	table button{width:100%;}
	body{margin:0; padding:0;}
	
	
        #board{
            position:relative;
        }

        .econ{
            width:75px;
            margin:15px;
        }
        .econ:hover{
            cursor:pointer;
        }

        #result>div>div{
            border-radius: 7px;
            background-color:white;
            color: black;
            margin:12px;
            padding:10px;
            display:inline-block;
            max-width:50%;

            word-break: break-all;
        }

        #input{
            border:1px solid black;
            height:100%;
            overflow: auto;
        }

        #inputBox{
            overflow: hidden;
        }
        
        #inputBox>div{
            float:left;
        }
        
        #result{
            overflow-y: scroll;
        }
        #result::-webkit-scrollbar {
          width: 9px;
        }
        #result::-webkit-scrollbar-track {
          background-color: transparent;
        }
        #result::-webkit-scrollbar-thumb {
          border-radius: 10px;
          background-color: pink;
        }

</style>
</head>
<body>

	<c:choose>
	
	
	<c:when test="${isLoginOk == 1}">
	
	<!-- 로그인시 -->
    <table border=1 align=center>
		<tr><th colspan=4> ${loginId } 님 안녕하세요.
		<tr>
		<td align=center><button id="toBoard">Board</button>
		<tr>
		<td align=center><button id=toMyPage>MyPage</button>
		<tr>
		<td align=center><button id=logout>Logout</button> 
		<tr>
		<td align=center><button id="memberOut">MemberOut</button>
		<tr>
		<td align=center><button id="chatRoom">채팅함</button>
	</table>
	
	<br><br>
	
	<!-- 로그인시 다중채팅창 -->
	<div id="board" style="width:400px; height:450px; margin:auto;">
        
        <div id="result" style="height:80%; background-color: darkcyan;">
            
            
        </div>
        
        <div id="inputBox" style="height:20%;">
            <div id="input" contenteditable="true" style="width:75%; padding-left:7px; padding-top:7px; outline:0px;"></div>
            <div style="width:25%; height:100%;">
                <button id="send" style="width:100%; height:100%;">
                    Send
                </button>
            </div>
        </div>
    </div>
	
    
    </c:when>
    
    
    <c:otherwise>
    
    <!-- 로그인 실패시 -->
    <c:if test="${isLoginOk == 0 }">
    	<script>
			Swal.fire({
				title: "로그인 실패",
				text: "아이디 또는 비밀번호를 다시 확인해주세요.",
				icon: "info"
			}).then(function(){
				location.href="/logout.member"
			})
    	</script>
    </c:if>
    
	
	<!-- 기본 화면 -->
<!-- 	<div style="width:100vw; height:100vh; display:flex; justify-content:center; align-items:center;"> -->
	<div>
	
	<form action="/login.member" method=post id=loginForm>

	<table border="1" align='center'>
        <tr>
            <th colspan="2">Login Box</th>
        </tr>
        <tr>
            <td align=center>아이디</td>
            <td><input type="text" id="id" name="id" placeholder="Input your ID"></td>
        </tr>
        <tr>
            <td align=center>패스워드</td>
            <td><input type="password" id="pw" name="pw" placeholder="Input your PW"></td>   
        </tr>
        <tr>
            <th colspan="2" align="center" height=40>
            <button id=login style=width:80px;>로그인</button>&nbsp;
            <input type="button" id=toJoinForm value="회원가입" style=width:80px;>
        </tr>     
    </table>
    
    </form>
    </div>
	
	</c:otherwise>
	
	
	</c:choose>
	
	
	<br><br>
	
	
	
	
	
	<table id=productTable align=center>
		
	
	</table>
	
	
	
	<script>
	
		$("#id").focus()
	
		$("#login").on("click", function(){
			location.href="/login.member"
		})
		
		$("#pw").on("keyup", function(e){
			if(e.which == 13) {
				$("#loginForm").submit()
			}
		})
	
		$("#toJoinForm").on("click", function(){
			location.href="/member/joinForm.jsp"
		})
		
		$("#toBoard").on("click", function(){
			location.href="/list.productBoard?currPage=1"
		})
		
		$("#toMyPage").on("click", function(){
			location.href="/myPage.member"
		})
		
		$("#logout").on("click", function(){
			location.href="/logout.member"
		})
		
		$("#memberOut").on("click", function(){
			Swal.fire({
				  title: "정말 탈퇴하시겠습니까?",
				  text: "탈퇴 후에는 계정 복구가 불가능합니다.",
				  icon: 'warning',
				  showCancelButton: true,
				  confirmButtonColor: '#3085d6',
				  cancelButtonColor: '#d33',
				  confirmButtonText: "네, 탈퇴할래요",
				  cancelButtonText: "잘못눌렀어요"
				}).then((result) => {
				  if (result.isConfirmed) {
				    Swal.fire(
				      '탈퇴되었습니다',
				      '다음에 다시 만나요',
				      'success'
				    ).then(function(){
				    	location.href="/memberOut.member"
				    })
				  }
				})
		})
		
		
		// 로그인 성공시 다중채팅창 띄우기 (웹소켓 접속)
		if("${isLoginOk}" == 1) {
			let webSocket = new WebSocket("ws://localhost/multi.chat")

	        let messageTextArea = document.getElementById("result")

	        webSocket.onopen = function(message) {
				
				// 다중채팅창의 기존 메시지 (최대 100개) 를 우선 출력한다.
				$.ajax({
					url : "/list.multichatAjax",
					type : "post",
					dataType : "json"
				}).done(function(list){
					for(let i=0; i<list.length; i++) {
						
						if(list[i].writer == "${loginId}") {
							$("#result").append("<div style='text-align:right;'><div style='text-align:left; background-color:lemonchiffon;'>" + list[i].contents);
						
						} else {
							$("#result").append("<div style='color:white; margin-bottom:-6px;'>&nbsp;&nbsp;&nbsp;" + list[i].writer + "</div><div><div>" + list[i].contents);
							
						}
						
						$("#result").scrollTop($("#result").prop("scrollHeight"));
						
					}
				})
				
                $("#input").html("");
                $("#input").focus();
	        }

	        webSocket.onclose = function(message) {
	            messageTextArea.innerHTML += message.code + ""
	            messageTextArea.innerHTML += "Server is now Disconnected...\n"
	        }

	        webSocket.onerror = function(message) {
	            messageTextArea.innerHTML += "error...\n"
	        }

	        webSocket.onmessage = function(message) {
	        	let result = message.data.split("!%!")
	        	$("#result").append("<div style='color:white; margin-bottom:-6px;'>&nbsp;&nbsp;&nbsp;" + result[0] + "</div><div><div>" + result[1]);
                $("#result").scrollTop($("#result").prop("scrollHeight"));
	        }

			document.getElementById("send").addEventListener("click", function(){
				let user = "${loginId }"
		        let message = $("#input").text()
				
		        if(message != "") {
	    			$("#result").append("<div style='text-align:right;'><div style='text-align:left; background-color:lemonchiffon;'>" + message);
	                $("#input").text("");
	                $("#input").focus();
	                $("#result").scrollTop($("#result").prop("scrollHeight"));
	                    
	    	        webSocket.send("{{" + user + "}}" + message)
	    	        message = ""
	    		} else {
	    			return false
	    		}
	        })

	        document.getElementById("input").addEventListener("keyup", function(e){
	        	if(e.which == 13) {
	        		let user = "${loginId }"
	    		    let message = $("#input").text()
	    		    
	    		    if(message != "") {
	    				$("#result").append("<div style='text-align:right;'><div style='text-align:left; background-color:lemonchiffon;'>" + message);
	                	$("#input").text("");
	                	$("#input").focus();
	                	$("#result").scrollTop($("#result").prop("scrollHeight"));
	                    
	    	        	webSocket.send("{{" + user + "}}" + message)
	    	        	message = ""
	    		    } else {
	    		    	return false
	    		    }
	        	}
	        })
	        
// 	        document.getElementById("disconnect").addEventListener("click", function(){
// 	            webSocket.close()
// 	        })
			
		}
		
		
		
		
		
		
		$(function(){
			
			// 무한 스크롤
			
			// 메인 로드시 기본 5개
			$.ajax({
				url : "/scrollImage.productBoard",
				type : "post",
				dataType : "json"
			// 이미지
			}).done(function(list){
				
				for(let i=0; i<5; i++) {
					let table = $("#productTable")
					let tr = $("<tr id=tr"+i+">")
					let td = $("<td>")
					let image = $("<img src=/file/"+list[i]+" style='width:200px; height:200px; object-fit:cover;'>")
					td.append(image)
					tr.append(td)
					table.append(tr)
					table.append("<br>")
				}
			// 게시글
			}).done(function(){
				
				$.ajax({
					url : "/scrollBoard.productBoard",
					type : "post",
					dataType : "json"
				}).done(function(list2){
					
					for(let i=0; i<5; i++) {
						let tr = $("#tr"+i)
						let td = $("<td style='width:400px; border:1px solid black; text-align:center;'>"+list2[i].title+"</br>"+list2[i].price+"</br>"+list2[i].sellingOption+"</td>")
						tr.append(td)
					}				
					
				})
			})
			
			
			// 스크롤시 5개씩 추가
			let scrollCount = 5
		
			$(document).scroll(function() {
    			var maxHeight = $(document).height();
    			var currentScroll = $(window).scrollTop() + $(window).height();

    			if (maxHeight <= currentScroll + 50) {
    				$.ajax({
    					url : "/scrollImage.productBoard",
    					type : "post",
    					dataType : "json"
    				// 이미지
    				}).done(function(list){
    				
			    		for(let i=scrollCount; i<scrollCount+5; i++) {
			    			
			    			if(i < list.length) {
			    			
			    				let table = $("#productTable")
			    				let tr = $("<tr id=tr"+i+">")
			    				let td = $("<td>")
			    				let image = $("<img src=/file/"+list[i]+" style='width:200px; height:200px; object-fit:cover;'>")
			    				td.append(image)
			    				tr.append(td)
			    				table.append(tr)
			    				table.append("<br>")
			    			
			    			} else {
			    				break;
			    			}
			    		
			    		}
    				// 게시글
    				}).done(function(list2){
    					
    					$.ajax({
    						url : "/scrollBoard.productBoard",
    						type : "post",
    						dataType : "json"
    					}).done(function(list2){
    							
    						for(let i=scrollCount; i<scrollCount+5; i++) {
    							
    							if(i < list2.length) {	
    							
    								let tr = $("#tr"+i)
    								let td = $("<td style='width:400px; border:1px solid black; text-align:center;'>"+list2[i].title+"</br>"+list2[i].price+"</br>"+list2[i].sellingOption+"</td>")
    								tr.append(td)
    							
    							} else {
    								break;
    							}
    						}
    						
    						scrollCount += 5
    						
    					})
    				})
    			}
  			})
  			
  			
  			
  			// 채팅
			$("#chatRoom").on("click",function(){
				location.href = "/chatRoom.chat?loginId=${loginId}";
			})
		
			$.ajax({
    			url:"/notice.chat",
    			data:{loginId:"${loginId}"},
    			dataType:"json"	
    		}).done(function(resp){
    			if(resp > 0){
    				$("#chatRoom").append("New").css("color","red")
 
    			}
    		})
		
		
		})
		
		
		
	
	</script>
	

</body>
</html>