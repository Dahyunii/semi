<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${dto.title }</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<link
   href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
   rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script
   src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link
   href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css"
   rel="stylesheet">
<script
   src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<style>
* {
	box-sizing: border-box;
}

table {
	width: 700px;
}

textarea {
	width: 100%;
	height: 100%;
}

#reply {
	background-color: #FBEFEF;
}

#write {
	background-color: #EFF8FB;
}

#filecolorlist{
	background-color:#E0F8E0;
}

*{
      font-family: "맑은 고딕", serif;
   }

</style>
</head>
<body>
	<form action="update.carBoard" method="post" id="modifyFrm">
		<input type="hidden" value="${dto.seq }" name="seq">
		<table border=1 align=center id="write">
			<tr>
				<td align=center colspan=2 id="titleTH">${dto.title }</td>
				<input type="hidden" id="titleInput" name=title value="${dto.title }">
			</tr>
			<tr>
				<td>${dto.writer }
				<td>${dto.writeDate }
			</tr>
			<tr id=oricont>
				<td colspan=2 id="contentsTD">${dto.contents }</td>
				<input type="hidden" id="contentsInput" name="contents" value="${dto.contents }">
			</tr>
			<tr id=summerTR style="display:none;">
        		 <td colspan="2">
        		 	<textarea id=summernote class=summernote name=summercontents></textarea>	
        		 </td>
     		</tr>
			<tr>
				<td colspan=2 align=right id="btns">
				<c:if test="${loginID == dto.writer }">
						<button id="modify" type=button>수정하기</button>
						<button id="delete" type=button>삭제하기</button>
						<script>
							$("#delete").on("click", function() {
								location.href = "delete.carBoard?seq=${dto.seq}";
							})

							$("#modify").on("click",function() {
										$("#titleTH").removeAttr("align");
										$("#titleTH").attr("contenteditable",
												"true");
										$("#titleTH").focus();

										
										$("#oricont").css("display","none");
										$("#summerTR").css("display","block");
										$(".note-editable").append("${dto.contents}");
									
										$("#contentsInput").attr("name","oricontents")
										$("#summernote").attr("name","contents");
										
										$("#modify").css("display", "none");
										$("#delete").css("display", "none");
										$("#back").css("display", "none");

										let okBtn = $("<button>");            // okBtn은 submit 로 동작한다.
										okBtn.text("수정완료");
										okBtn.css("margin-right", "5px");
										
										let cancelBtn = $("<button>");
										cancelBtn.attr("type","button");
										cancelBtn.text("취소");

										cancelBtn.on("click", function() {
											location.reload();
										})
			
										$("#btns").prepend(cancelBtn);
										$("#btns").prepend(okBtn);

									})
						</script>
					</c:if>
					<button id="back" type=button>목록으로</button>
			</tr>
		</table>
	</form>

	<form action="enroll.carReply">
		<input type="hidden" value="${dto.seq }" name="parent_seq">
		<table border=1 align=center>
			<tr>
				<td colspan="2">댓글 작성
			</tr>

			<tr>
				<td><textarea id="textArea" name="commenttext"></textarea></td>
				<td align=center>
					<button>댓글 등록</button>
				</td>
			</tr>
		</table>
	</form>

	<table border=1 align=center id="filecolorlist">
		<tr>
			<td>
				<button id="list">파일 목록 가져오기</button>
	
				<fieldset id="fileList">
    				<!-- 이 위치가 append 위치이다  -->
				</fieldset>
			</td>
		</tr>
	</table>

	
<!-- <form action="modify.reply"> -->
	<table border=1 align=center id="reply">
		<tr>
			<th colspan=6>댓글목록</th>
		</tr>
		<c:forEach var="i" items="${replylist }">
			<tr>
				<td class="replyseq">${i.seq}
				<td>${i.writer }
				<td class="replycontents">${i.contents }
				<td>${i.writeDate }
					<c:choose>
						<c:when test="${i.writer ==loginID }">
							<td id="btnn">
								<input type="button" value="삭제" class="replydelete" id="replydelete" replyseq="${i.seq}" >
								<input type="button" value="수정" class="replymodify" id="replymodify" modifyseq="${i.seq}">
							</td>
						</c:when>
					</c:choose>
			</tr>
		</c:forEach>
	</table>
<!-- </form>	 -->




	<script>
		$(".replydelete").on("click", function() { 
			
			$.ajax({
				url:"/delete.carReply",
				data : {replyseq : $(this).attr("replyseq")}
		
			  }).done(function(resp){
			 	if(resp=="true"){
			 		location.reload();
			 	}
			});
		})
		
		$(".replymodify").on("click",function(){
			let modifyokbtn = $("<button>");       
			modifyokbtn.attr("type","submit");
			modifyokbtn.text("수정완료");
			modifyokbtn.attr("class","modiok");
			modifyokbtn.css("margin-right", "5px");
			
			let modifycancel = $("<button>");
			modifycancel.attr("type","button");
			modifycancel.css("margin-right", "5px");
			modifycancel.text("취소");
			
			$(this).closest('tr').find("#replydelete").css("display", "none");
			$(this).closest('tr').find("#replymodify").css("display", "none");

			$(this).closest('td').append(modifyokbtn);
			$(this).closest('td').append(modifycancel);
			
			$(this).closest('tr').find('.replyseq').attr("name","modifyseq");
			$(this).closest('tr').find('.replycontents').attr("name","conte");
			
			$(this).closest('tr').find('.replycontents').attr("contenteditable","true");
			$(this).closest('tr').find('.replycontents').focus();
			

			
			let conte
			let modifyseq
			$(".modiok").on("click",function(){
				conte=$(this).closest('tr').find('.replycontents').text();
				modifyseq=$(this).closest('tr').find('.replyseq').text()
				
				$.ajax({
					url:"/modify.carReply",
					data : {"modifyseq" : modifyseq,
						conte:conte	}
				}).done(function(resp){
					location.reload();
				});	
			})
			
			

			

			
			modifycancel.on("click", function() {
				location.reload();
			})
			
			
			
		})
		 $('.summernote').summernote({
         disableResizeEditor : false,
         height : 500,
         leng : 'ko-KR',
         disableDragAndDrop : true,
           toolbar: [
             ['fontname', ['fontname']],
             ['fontsize', ['fontsize']],
             ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
             ['color', ['forecolor','color']],
             ['table', ['table']],
             ['para', ['ul', 'ol', 'paragraph']],
             ['height', ['height']],
             ['view', ['fullscreen', 'help']]
           ],
         fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
         fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
        });
		
		
		
		
		$("#list").on("click",function(){
			$.ajax({
				dataType: 'json',
				url:"/list.carFile",
				data : {dtoseq : ${dto.seq }}
				
			}).done(function(resp){
			
				
				for(let i=0;i<resp.length;i++){
					let filediv=$("<div>");
					
					let anker=$("<a>");
					anker.attr("href","/download.carFile?oriName="+resp[i].oriName+"&sysName="+resp[i].sysName);
					anker.text(resp[i].oriName)
					
					filediv.append(anker)
					
					$("#fileList").append(filediv);
				}
				$("#list").off('click');
			})
		})
		
		
	
	
		$("#back").on("click", function() {
			location.href = "/list.carBoard?cpage="+${cpage};
		})
		
		$("#modifyFrm").on("submit",function(){
			$("#titleInput").val($("#titleTH").text());
// 			$("#contentsInput").val($(".note-editable").text());
			
		})
		
		
	</script>
</body>
</html>