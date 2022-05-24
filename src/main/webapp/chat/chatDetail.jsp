<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<style>
* {
	box-sizing: border-box;
}

body {
	margin: 0;
	padding: 0;
}


#result>div>div {
	border-radius: 7px;
	background-color: white;
	color: black;
	margin: 12px;
	padding: 10px;
	display: inline-block;
	max-width: 50%;
	word-break: break-all;
}

#input {
	border: 1px solid black;
	height: 100%;
	overflow: auto;
}

#inputBox {
	overflow: hidden;
}

#inputBox>div {
	float: left;
}

#result {
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

	<div id="board" style="width: 400px; height: 450px; margin: auto; margin-top:200px;">

		<div id="result" style="height: 80%; background-color: darkcyan;">
		
			<c:forEach var="i" items="${list }">
				<c:if test="${i.sender eq loginId }">
					<div style='text-align:right;'>
					<div style='text-align:left; background-color:lemonchiffon;'>${i.contents }</div></div>
				</c:if>
				<c:if test="${i.sender ne loginId }">
					<div style='color:white; margin-bottom:-6px;'>&nbsp;&nbsp;&nbsp;${i.sender }</div>
					<div><div>${i.contents }</div></div>
					
				</c:if>
			</c:forEach>

		</div>

		<div id="inputBox" style="height: 20%;">
			<div id="input" contenteditable="true"
				style="width: 75%; padding-left: 7px; padding-top: 7px; outline: 0px;"></div>
			<div style="width: 25%; height: 100%;">
				<button id="send" style="width: 100%; height: 100%;">Send</button>
			</div>
		</div>
	</div>
	<br>
	<div style="text-align:center;">
		<a href="/chatRoom.chat?loginId=${loginId}"><button>목록으로</button></a>
	</div>


<script>

    let webSocket = new WebSocket("ws://localhost/broadsocket?loginId=${loginId}")

      let messageTextArea = document.getElementById("result")

      webSocket.onopen = function(message) {
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
         
         let blank_pattern = /^\s+|\s+$/g;
       	 if(message.replace(blank_pattern,'') != ""){
       
           $("#result").append("<div style='text-align:right;'><div style='text-align:left; background-color:lemonchiffon;'>" + message);
              $("#input").text("");
              $("#input").focus();
              $("#result").scrollTop($("#result").prop("scrollHeight"));
                  
             webSocket.send("${loginId}{=}${with }{=}" + message)
             message = ""
        }else{
        	return false;
        }
      })
      
    

      document.getElementById("input").addEventListener("keyup", function(e){
         if(e.which == 13) {
            let user = "${loginId }"
            let message = $("#input").text()
            
           let blank_pattern = /^\s+|\s+$/g;
       	   if(message.replace(blank_pattern,'') != ""){
              $("#result").append("<div style='text-align:right;'><div style='text-align:left; background-color:lemonchiffon;'>" + message);
                 $("#input").text("");
                 $("#input").focus();
                 $("#result").scrollTop($("#result").prop("scrollHeight"));
                  
                 webSocket.send("${loginId}{=}${with }{=}" + message)
                message = ""
            } else {
               return false
            }
         }
      })
      
//       document.getElementById("disconnect").addEventListener("click", function(){
//           webSocket.close()
//       })
    
 
</script>

</body>
</html>