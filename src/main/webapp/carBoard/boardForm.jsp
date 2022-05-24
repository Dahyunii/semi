<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
</head>

<style>
	*{
      font-family: "맑은 고딕", serif;
   }

</style>
<body>
   <form action="/writeProc.carBoard" method="post" id="myform" enctype="multipart/form-data">
      <table border="1" align=center>

         <tr>
            <th colspan="2">자유게시판 글 작성하기
         </tr>
         <tr>
            <td><input type="text" placeholder="글 제목을 입력하세요" size="97"
               name=title></td>
         </tr>

         <tr>
            <td id="mytd">
            <button type=button id=plusbtn>+</button>
         </tr>

         <tr>
        	 <td colspan="3">
        	 	<textarea id=summernote class=summernote name=contents></textarea>
        	 </td>
     	 </tr>
         
         
         <tr>
            <td colspan="2" align="right"><a href="javascript:history.back()"><button type="button">목록으로</button></a>
               <button type="submit">작성완료</button>
         </tr>


      </table>
   </form>
   <script>
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
      
      $(document).ready(function() {
         $('#summernote').summernote();
         
      });
      
      $("#filebtn").on("click",function(){
  		$.ajax({
  			url:"/upload.carFile",
  		}).done(function(resp){
  			
  		
  		})
  	  })
  	  
  	 let i=1;
		
		$("#plusbtn").on("click",function(){
			let inputplus=$("<input>");	
			let filename="file"+i;
			inputplus.attr("type","file");
			inputplus.attr("name",filename);
			i++;
			
			$("#mytd").append(inputplus);
		})
   </script>

</body>
</html>