<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<style>
	
	*{box-sizing:border-box;}
	
	tr{
		border:1px solid black;
	}

	table input[type=text]{
		border:1px solid black;
		background-color:transparent;
	}
	
	#replyBox{
		overflow:hidden;
	}
	
	#replyBox div{
		float:left;
	}
	
	.replyTop td{
		height:40px;
		background-color:#fbe5e7;
	}
	
	.replyBottom td{
		height:60px;
		background-color:ivory;
	}
	
	*{
		font-family: "맑은 고딕", serif;
	}
	
	.carousel-indicators button{
 		background-color:red;
	}
	
	
</style>
</head>
<body>

	<div id=container>
	
	<!-- 상품이미지 캐러셀 -->
	<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel" style="width:800px; height:800px; margin:0 auto;">
  		<div class="carousel-indicators" id=test>
  		
    		<button type="button" style="border-radius:45%; width:20px; height:18px; margin:7px;" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
    		
    		<c:if test="${imageList[1].sysName ne null }">
    		<button type="button" style="border-radius:45%; width:20px; height:18px; margin:7px;" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
    		</c:if>
    		
    		<c:if test="${imageList[2].sysName ne null }">
    		<button type="button" style="border-radius:45%; width:20px; height:18px; margin:7px;" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
    		</c:if>
    		
  		</div>
  		<div class="carousel-inner" id=carouselBox>
  		
    		<div class="carousel-item active">
     			 <img src="/file/${imageList[0].sysName }" style="width:800px; height:800px; object-fit:cover;">
    		</div>
    		
    		<c:if test="${imageList[1].sysName ne null }">  			
  			<div class="carousel-item">
     			 <img src="/file/${imageList[1].sysName }" style="width:800px; height:800px; object-fit:cover;">
    		</div>
    		</c:if>
    		
    		<c:if test="${imageList[2].sysName ne null }">  			
  			<div class="carousel-item">
     			 <img src="/file/${imageList[2].sysName }" style="width:800px; height:800px; object-fit:cover;">
    		</div>
    		</c:if>
    		
  		</div>
  		<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
    		<span class="carousel-control-prev-icon" aria-hidden="true"></span>
    		<span class="visually-hidden">Previous</span>
  		</button>
  		<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
   			<span class="carousel-control-next-icon" aria-hidden="true"></span>
    		<span class="visually-hidden">Next</span>
 		</button>
	</div>



    <form action="/update.productBoard" method=post enctype="multipart/form-data" id=form>

	<!-- input type hidden 모음 -->
	<input type=hidden name=seq value=${BoardDTO.seq }>
	<input type=hidden id=updateTitle name=title>
<!-- 	<input type=hidden id=updateContents name=contents> -->
	
		
	<table border="1" align=center width="800">
			
<!--         <tr><th colspan=4>게시글 -->
        
        <tr align=center height=40>
        	<td id=title>${BoardDTO.title }</td>
        	<td width=180>작성자 : ${BoardDTO.writer } 
        	<td width=200>${BoardDTO.formedDate }
        
<!--         <tr align=right> -->
<!--         	<td colspan=3 style=padding-right:10px;>첨부파일 :  -->
        	
<%--         	<c:forEach var="i" items="${filesList }"> --%>
<%--         		<a href="/download.file?sys_name=${i.sys_name }&ori_name=${i.ori_name }">${i.ori_name }</a> --%>
<%--         	</c:forEach> --%>

        <tr>
        	<td colspan=3 height=40 id=category>&nbsp;카테고리 : ${BoardDTO.category }
        	<td colspan=3 height=40 id=modifyCategory style="display:none;">
        		&nbsp;
        		<select name=category>
        			<option value=의류 id=clothing>의류
        			<option value=잡화 id=stuff>잡화
        			<option value=가전 id=appliances>가전
        			<option value=취미 id=leisure>취미
        			<option value=아동 id=children>아동
        			<option value=기타 id=else>기타
        		</select>
        		&nbsp;* 카테고리
        	</td>
        </tr>
        
        <tr>
        	<td colspan=3 height=40 id=sellingOption>&nbsp;거래방법 : ${BoardDTO.sellingOption }
        	<td colspan=3 height=40 id=modifySellingOption style="display:none;">
        		&nbsp;
        		<select id=sellingOptionSelect name=sellingOption>
        			<option value="물물교환 & 금전거래" id=all>물물교환 & 금전거래
        			<option value="물물교환만" id=exchange>물물교환만
        			<option value="금전거래만" id=money>금전거래만
        		</select>
        		&nbsp;* 거래방법
        	</td>
        </tr>
        
        <tr>
        	<td colspan=3 height=40 id=pname>&nbsp;상품명 : ${BoardDTO.pname }
        	<td colspan=3 height=40 id=modifyPname style="display:none;">
        		&nbsp;<input type=text name=pname value=${BoardDTO.pname } style="height:80%;">
        		&nbsp;* 상품명
        	</td>
        	
        <tr id=priceBox>
        	<td colspan=3 height=40 id=price>&nbsp;가격 : ${BoardDTO.price } 원
        	<td colspan=3 height=40 id=modifyPrice style="display:none;">
        		&nbsp;<input type=text id=submitPrice name=price value=${BoardDTO.price } style="height:80%;">
        		&nbsp;* 가격
        	</td>
        </tr>
        
        <tr id=fileAddBtnBox style="display:none;">
        	<td><button type=button id=fileAddBtn style="margin:10px; background-color:lemonchiffon;">파일첨부하기</button>
        </tr>
        
        <tr id=imageBox style="display:none;">
        
        	<c:forEach var="i" items="${imageList }">
        		<c:set var="count" value="${count+1 }"/>
        	
        	<td class=td style="width:200px; text-align:center;">
        		<input type=hidden name="imageSeq${count }" value=${i.seq }>
				<br>
        		<img src="/file/${i.sysName }" class=previewImage id="previewImage" style="width:200px; height:200px; object-fit:cover;"><br>
        		<button type=button class=deleteSavedImageBtn id=deleteSavedImageBtn imageSeq=${i.seq }>삭제하기</button>
        	</td>
        	
        	</c:forEach>
        	
        </tr>	
        
        <tr id=contentsTd>
        	<td colspan=3 id=contents height=300>${BoardDTO.contents }</td>
        <tr id=summernoteTd style="display:none;">	
        	<td colspan="3"><textarea id=summernote class=summernote name=contents></textarea>
        	
        <tr>
        	<td colspan=3 align=center id=btnBox><button type=button id=toList>목록으로</button>
        
		<!-- 작성자 본인일 경우 수정, 삭제 표시 -->
        <c:if test="${BoardDTO.writer eq loginId }">
        	<button type=button id=updateBtn>수정하기</button> 
        	<a href="/delete.productBoard?seq=${BoardDTO.seq }"><button type=button id=deleteBtn>삭제하기</button></a>
        	
        </c:if>
        
        <!-- 채팅하기 -->
        <c:if test="${BoardDTO.writer ne loginId }">
        	 
        	<a href="/chatWith.chat?with=${BoardDTO.writer}"><button type=button id=chatBtn>채팅하기</button></a>
        </c:if>
        
        
    </table>
    
    </form>
    
    <br><br>
    
	<!-- 댓글작성 -->
	
	<form action="/insert.productReply?parentSeq=${BoardDTO.seq }&writer=${loginId}" method=post id=replyForm>
	
        <div id="top" style="border: 1px solid black; width:800px; height:100px; margin:0 auto;">

            <div style="background-color: #fbe5e7; height:40%; line-height:40px; border:0px solid black;">
				&nbsp;${loginId }
            </div>
            
            <div class="replyBox" id="replyBox" style="height:60%;">
                <div style="width:85%; height:100%; border-top:1px solid black;">
                <input type="text" id="inputContents" name="contents" style="width:100%; height:100%; border:0px; background-color:ivory;" placeholder="내용을 입력해주세요.">
                </div>

				<div style="width:15%; height:100%;">             
                <button id="replyBtn" style="width:100%; height:100%;">댓글달기</button>
                </div>
            </div>

        </div>
        
        <br><hr width=800 style="margin:auto;"><br>
    
    </form>
    
    </div>
    
    
    
    <script>

    	// 물물교환만 일 경우 가격 표시 안함
    	if("${BoardDTO.sellingOption }" == "물물교환만") {
    		$("#priceBox").css("display", "none")
    	}
    	
    	// 게시판 목록으로
    	$("#toList").on("click", function(){
    		location.href="/list.productBoard?currPage=${prevPage }"
    	})
    	
    	// 게시글 삭제
    	$("#deleteBtn").on("click", function(){
    		if(confirm("정말 삭제하시겠습니까?")) {
    			return true
    		} else {
    			return false
    		}
    	})
    	
    	// 게시글 수정
    	$("#updateBtn").on("click", function(){
    		
    		// 첨부 이미지 수정
    		$("#fileAddBtnBox").css("display", "")
    		$("#imageBox").css("display", "")
    		
    		let i = 4
    		
    		$("#fileAddBtn").on("click", function(){
    			
	            i += 1
	            
	            if($(".td").length >= 3) {
	            	alert("이미지는 최대 3장까지만 등록 가능합니다.")
	            	return false
	            }
				
				let td = $("<td class=td style='width:200px; text-align:center;'>")
	            let inputImage = "<input type=file style='width:200px;' class=inputImage id=inputImage" + i + " class=inputTypeFile accept='image/*' name=file" + i + ">"
	            let previewImage = "<img style='width:200px; height:200px; object-fit:cover;' class=previewImage id=previewImage" + i + " src='https://dummyimage.com/500x500/ffffff/000000.png&text=preview+image'>"
				let deleteImgBtn = "<button type=button class=deleteImageBtn>삭제하기</button>"
	            
	    		td.append(inputImage)
	            td.append(previewImage)
	            td.append(deleteImgBtn)

				$("#imageBox").append(td)
				
				
				// 파일첨부버튼을 안누르고 기존 사진을 변경할때와 기존사진을 삭제하고 파일첨부버튼을 눌러 새로 추가한 후 사진을 지정하는
				// 두가지 케이스가 있으므로 아래 함수가 두번 들어가야 한다.
				$(".inputImage").on("change", function(e){
					
	                let input = e.target
	
	                if(input.files && input.files[0]) {
	                	// 첨부파일 사이즈 체크
	                	var maxSize = 5 * 1024 * 1024;
	            		var fileSize = input.files[0].size;

	            		if(fileSize > maxSize){
	            			alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
	            			$(this).val('');
	            			return false;
	            		}
	                	
	            		// 첨부파일 확장자 체크
	                	pathpoint = input.value.lastIndexOf('.');
	            		filepoint = input.value.substring(pathpoint+1,input.length);
	            		filetype = filepoint.toLowerCase();
	            		
	            		// 정상적인 이미지 확장자 파일인 경우
	            		if(filetype=='jpg' || filetype=='gif' || filetype=='png' || filetype=='jpeg' || filetype=='bmp') {
	            			let reader = new FileReader()
	                    	reader.onload = e => {
	                       		$(this).siblings().attr("src", e.target.result)
	                    	}

	                    	reader.readAsDataURL(input.files[0])

	            		} else {
	            			alert('이미지 파일만 업로드 하실 수 있습니다.');
							$(this).val("")
//	             			$(this).closest(".td").remove()

	            			return false;
	            		}
	                	
	                }
	                
	            })
	            
	            $(".deleteSavedImageBtn").on("click", function(){
	            	
	            	$.ajax({
	            		url : "/deleteSavedImage.productBoard",
	            		data : {imageSeq : $(this).attr("imageSeq")}
	            	})
            		
	            	$(this).closest(".td").remove()
	            	
            	})
            	
            	$(".deleteImageBtn").on("click", function(){
            		$(this).closest(".td").remove()
            	})
	            
    		})
	            
	            // 여기가 위 함수 두번째
// 	            $(".inputImage").on("change", function(e){
// 	                let input = e.target
	
// 	                if(input.files && input.files[0]) {
// 	                    let reader = new FileReader()
// 	                    reader.onload = e => {
// 	//                         $(this).next($("#previewImage"+i)).attr("src", e.target.result)
// 	                        $(this).siblings(".previewImage").attr("src", e.target.result)
// 	                    }
	
// 	                    reader.readAsDataURL(input.files[0])
// 	                        console.log($(this).val())
// 	                }
// 	            })
            
            	
            	$(".deleteSavedImageBtn").on("click", function(){
	            	
	            	$.ajax({
	            		url : "/deleteSavedImage.productBoard",
	            		data : {imageSeq : $(this).attr("imageSeq")}
	            	})
            		
	            	$(this).closest(".td").remove()
	            	
            	})
            	
//             	$(".deleteImageBtn").on("click", function(){
//             		$(this).closest(".td").remove()
//             	})

    		
    		// 수정버튼 눌렀을때 물물교환만 일 경우 가격 숨김
    		if("${BoardDTO.sellingOption }" == "물물교환만") {
				$("#priceBox").css("display", "none")
				$("#price").val("없음")
			} else {
				$("#priceBox").css("display", "")
			}
    		
    		// 판매방식 선택에 따른 가격입력칸 표시
        	$("#sellingOptionSelect").on("change", function(){
    			if($(this).val() == "물물교환 & 금전거래") {
    				$("#priceBox").css("display", "")
    			} else if($(this).val() == "물물교환만") {
    				$("#priceBox").css("display", "none")
    				$("#price").val("없음")
    			} else if($(this).val() == "금전거래만") {
    				$("#priceBox").css("display", "")
    			}
    		})
    		
    		// 수정버튼 눌렀을때 카테고리에 게시글 카테고리 selected 부여
    		if("${BoardDTO.category }" == "의류") {
    			$("#clothing").attr("selected", "")
    		} else if("${BoardDTO.category }" == "잡화") {
    			$("#stuff").attr("selected", "")
    		} else if("${BoardDTO.category }" == "가전") {
    			$("#appliances").attr("selected", "")
    		} else if("${BoardDTO.category }" == "취미") {
    			$("#leisure").attr("selected", "")
    		} else if("${BoardDTO.category }" == "아동") {
    			$("#children").attr("selected", "")
    		} else {
    			$("#else").attr("selected", "")
    		}
    		
    		// 수정버튼 눌렀을때 거래방법에 게시글 거래방법 selected 부여
    		if("${BoardDTO.sellingOption }" == "물물교환 & 금전거래") {
    			$("#all").attr("selected", "")
    		} else if("${BoardDTO.sellingOption }" == "물물교환만") {
    			$("#exchange").attr("selected", "")
    		} else if("${BoardDTO.sellingOption }" == "금전거래만") {
    			$("#money").attr("selected", "")
    		}
    		
    		
    		$("#title").attr("contenteditable", "true").css("background-color", "lemonchiffon")
    		$("#category").css("display", "none")
    		$("#modifyCategory").css("display", "")
    		$("#sellingOption").css("display", "none")
    		$("#modifySellingOption").css("display", "")
    		$("#pname").css("display", "none")
    		$("#modifyPname").css("display", "")
    		$("#price").css("display", "none")
    		$("#modifyPrice").css("display", "")
    		$("#contents").attr("contenteditable", "true").css("background-color", "lemonchiffon")
    		$("#contents").attr("contenteditable", "true")
    		$("#toList").css("display", "none")
    		$("#updateBtn").css("display", "none")
    		$("#deleteBtn").css("display", "none")
    		let confirm = $("<button>수정완료</button>")
    		let cancel = $("<button type=button id=cancelBtn>취소</button>")
    		$("#btnBox").append(confirm)
    		$("#btnBox").append("\u00a0")
    		$("#btnBox").append(cancel)
    		
    		$("#cancelBtn").on("click", function(){
    			location.reload()
    		})
    		
    		
    		// 썸머노트
    		$('.summernote').summernote({
    			height : 500,
    			leng : 'ko-KR',
    			disableDragAndDrop : true,
    		  	toolbar: [
    			    // [groupName, [list of button]]
    			    ['fontname', ['fontname']],
    			    ['fontsize', ['fontsize']],
    			    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
    			    ['color', ['forecolor','color']],
    			    ['table', ['table']],
    			    ['para', ['ul', 'ol', 'paragraph']],
    			    ['height', ['height']],
//     			    ['insert',['picture','link','video']],
    			    ['view', ['fullscreen', 'help']]
    			  ],
    			fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
    			fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
    	  	});
    		
    		$("#contentsTd").css("display", "none")
    		$("#summernoteTd").css("display", "")
    		$("#summernote").summernote('code', '${BoardDTO.contents}')
    		
    	})
    	
		// 수정 완료    	
    	$("#form").on("submit", function(){
    		console.log($(".td").length)
    		$("#updateTitle").val($("#title").text())
//     		$("#updateContents").val($("#contents").text())
    		$("#imageFirstSeq").val(${imageList[0].seq } + $(".td").length - 1)
    		$("#savedImageCount").val($(".savedTd").length)
    		
    		// 이미지는 최소 1장 이상 첨부하도록 한다
    			if($(".td").length == 0 || $(".inputImage").val() == "") {
    				alert("상품이미지를 한 장 이상 등록해주세요.")
    				return false
    			}
    		
    		if(confirm("수정하시겠습니까?")) {
    			if($("#sellingOptionSelect").val() == "물물교환만") {
    				$("#submitPrice").val("없음")
    			}
    			return true
    		} else {
    			return false
    		}
    	})
    	
    	
    	
    	// 댓글 출력
    	$(function(){
    	
    		$.ajax({
    			url : "/list.productReply",
    			data : {parentSeq : ${BoardDTO.seq }},
    			dataType : "json"
    		}).done(function(list){
    			
    			for(let i=0; i<list.length; i++) {
    				
    				let container = $("<div width=800>")
    				let table = $("<table border=1 align=center width=800 id=replyTable>")
    				
    				let replyTop = $("<tr class=replyTop>")
    				replyTop.append("<td>&nbsp;아이디 : " + list[i].writer)
    				replyTop.append("<td align=center width=200>" + list[i].formedDate)
    				
    				let replyBottom = $("<tr class=replyBottom>")
    				let replyBottomTd = $("<td colspan=2 class=updateReplyBox>&nbsp;" + list[i].contents + "</td>")
    				replyBottomTd.append("<input type=hidden class=updateReplySubmitBox name=contents>")
    				replyBottom.append(replyBottomTd)
    				
    				table.append(replyTop)
    				table.append(replyBottom)
    				
    				if(list[i].writer == "${loginId }") {
    					
    					let row = $("<tr>")
    					let td = $("<td colspan=2 align=right id=appendBox style=background-color:#f0fff0>")
    					td.append("<button class=updateReplyBtn>수정하기")
    					td.append("<button class=deleteReplyBtn replySeq=" + list[i].seq + ">삭제하기")
    					td.append("<input type=hidden class=replySeq value=" + list[i].seq + ">")
    					
    					row.append(td)
    					table.append(row)
    				}
    				
    				$("#container").append(table)
    				$("#container").append("<br>")
    			}
    			
    			
    			// 댓글 수정, 삭제 : ajax 사용
    			$(".updateReplyBtn").on("click", function(){
    	    		
    	    		let submitBox = $(this).closest("#replyTable").find(".updateReplySubmitBox")
    	    		let inputBox = submitBox.parent()
    	    		
    	    		inputBox.attr("contenteditable", "true")
    	    		inputBox.css("background-color", "white")
    	    		
    	    		$(this).css("display", "none")
    	    		$(this).siblings(".deleteReplyBtn").css("display", "none")
    	    		let parent = $(this).parent()
    	    		let confirmBtn = $("<button>수정완료</button>")
    	    		parent.append(confirmBtn)
    	    		parent.append("\u00a0")
    	    		parent.append("<a href='javascript:location.reload()'><button type=button>취소")
    	    		
    	    		
    				confirmBtn.on("click", function(){
    					if(confirm("수정하시겠습니까?")) {
    					
    					submitBox.val(inputBox.text())
    					
    					$.ajax({
    						url : "/update.productReply",
    						data : {seq : $(this).siblings(".deleteReplyBtn").attr("replySeq"), contents : submitBox.val()}
    					}).done(function(){
    						location.reload()
    					})
    					
    					} else {
    						return false
    					}
    	    		
    				})
    	    		
    	    	})
    	    	
    	    	$(".deleteReplyBtn").on("click", function(){
    	    		
    	    		if(confirm("삭제하시겠습니까?")) {
    	    			$.ajax({
    	    				url : "/delete.productReply",
    	    				data : {seq : $(this).attr("replySeq")}
    	    			}).done(function(){
    	    				location.reload()
    	    			})
    	    			
    	    		} else {
    	    			return false
    	    		}
    	    		
    	    	})
    			
    		})		
    		
    		
    	})
    	
    	
    	
    
    </script>

</body>
</html>