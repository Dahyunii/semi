<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>

<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<style>

	*{box-sizing:border-box;}

	textarea{
		resize:none;
		border:0px;
	}
	
	.inputImage{
		margin:10px;
	}
	
	*{
		font-family: "맑은 고딕", serif;
	}

	/* 파일첨부영역 colspan 없이 좌우로 붙이기 */
    #tr td{
        float:left;
    }
	
</style>
</head>
<body>

	<form action="/writeConfirm.productBoard" method="post" enctype="multipart/form-data" id=form>
	
	<table border="1" align=center width="900">
        <tr>
            <th style="text-align:center; height:50px;">자유게시판 글 작성하기
        </tr>
        <tr>
            <td>
                <input type="text" id=title name=title placeholder="글 제목을 입력하세요." style="width:100%;">
            </td>
        </tr>
        <tr>
        	<td colspan="3" height=40>
        		&nbsp;
        		<select id=category name=category>
        			<option value=의류>의류
        			<option value=잡화>잡화
        			<option value=가전>가전
        			<option value=취미>취미
        			<option value=아동>아동
        			<option value=기타>기타
        		</select>
        		&nbsp; * 카테고리를 선택하세요.
        	</td>
        </tr>
        <tr>
        	<td colspan="3" height=40>
        		&nbsp;
        		<select id=sellingOption name=sellingOption>
        			<option value="물물교환 & 금전거래">물물교환 & 금전거래
        			<option value="물물교환만">물물교환만
        			<option value="금전거래만">금전거래만
        		</select>
        		&nbsp; * 거래방법을 선택하세요.
        	</td>
        </tr>
        <tr>
        	<td colspan=3 height=40>
        		&nbsp;
        		<input type=text id=pname name=pname placeholder="상품명을 입력해주세요.">
        	</td>
        </tr>
        <tr id=priceBox>
        	<td colspan="3" height=40>
        		&nbsp;
        		<input type=text id=price name=price placeholder="가격을 입력해주세요."> 원
        	</td>
        </tr>
        <tr>
        	<td colspan="3">
        		<button type=button id=fileAddBtn style="width:120px; background-color:lemonchiffon; margin:10px;">파일첨부하기</button>
        	</td>	
        </tr>
        
        <tr id="tr">
        	
        </tr>

		<tr>
			<td colspan="3"><textarea id=summernote class=summernote name=contents></textarea>
		</tr>
		
        <tr>
            <td colspan="3" align=center style="height:50px;">
                <a href="/list.productBoard?currPage=1"><input type="button" value="목록으로"></a>&nbsp;
                <button>작성완료</button>
            </td>
        </tr>
    </table>
	
	</form>
	
	
	
	<script>
	
		$("#title").focus()
		
        // 파일 첨부하기
        let i = 0
        
    	$("#fileAddBtn").on("click", function(){
    		
            i += 1
            
            if($(".td").length >= 3) {
            	alert("이미지는 최대 3장까지만 등록 가능합니다.")
            	return false
            }
            
            let td = $("<td class=td style='width:200px; margin:20px; border:0px;'>")
            let inputImage = "<input type=file style='width:200px;' class=inputImage id=inputImage" + i + " accept='image/jpeg, image/png' name=file" + i + ">"
            let previewImage = "<img style='width:200px; height:200px; object-fit:cover;' class=previewImage id=previewImage" + i + " src='https://dummyimage.com/500x500/ffffff/000000.png&text=preview+image'>"
			let deleteImgBtn = "<button type=button class=deleteImageBtn>삭제하기</button>"
            
    		td.append(inputImage)
            td.append(previewImage)
            td.append(deleteImgBtn)
            
            $("#tr").append(td)

            $(".inputImage").on("change", function(e){
            	
                let input = e.target

                if(input.files && input.files[0]) {
                	// 첨부파일 사이즈 체크
                	var maxSize = 5 * 1024 * 1024;
            		var fileSize = input.files[0].size;

            		if(fileSize > maxSize){
            			alert("이미지 사이즈는 5MB 이내로 등록 가능합니다.");
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
//             			$(this).closest(".td").remove()

            			return false;
            		}
                	
                }
            	
            })
            
            $(".deleteImageBtn").on("click", function(){
            	$(this).closest(".td").remove()
            })

		})
		
	
		// 판매방식 선택에 따른 가격입력칸 표시
		$("#sellingOption").on("change", function(){
			if($(this).val() == "물물교환 & 금전거래") {
				$("#priceBox").css("display", "")
			} else if($(this).val() == "물물교환만") {
				// 물물교환만 일 경우 가격 숨기고 "없음" 으로 price 전달
				$("#priceBox").css("display", "none")
				$("#price").val("없음")
			} else if($(this).val() == "금전거래만") {
				$("#priceBox").css("display", "")
			}
		})
		
		
		// 썸머노트
		$('.summernote').summernote({
			disableResizeEditor : false,
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
// 			    ['insert',['picture','link','video']],
			    ['view', ['fullscreen', 'help']]
			  ],
			fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
			fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
	  	});
		
		
		// 제출시
		$("#form").on("submit", function(){
			
			// 가격 정규표현식
			let priceRegex = /^[\d]{1,10}$/g;
			
			if($("#title").val() == "") {
				alert("제목을 입력해주세요.")
				$("#title").focus()
				return false
			} else if($("#pname").val() == "") {
				alert("상품명을 입력해주세요.")
				$("#pname").focus()
				return false
			} else if($("#sellingOption").val() != "물물교환만") {
				if($("#price").val() == "") {
					alert("가격을 입력해주세요.")
					$("#price").focus()
					return false
				} else if(priceRegex.test($("#price").val()) == false) {
					alert("가격을 숫자로만 입력해주세요.")
					$("#price").focus()
					return false
				}
			} 
			
			// else if 로 작성하면 왜인지 작동하지 않는다.
			if($("#summernote").summernote('isEmpty')) {
				alert("내용을 입력해주세요.")
				$("#summernote").summernote('focus')
				return false
			}
			
			// 이미지는 최소 1장 이상 첨부하도록 한다
			if($(".td").length == 0 || $(".inputImage").val() == "") {
				alert("상품이미지를 한 장 이상 등록해주세요.")
				return false
			}
			
			if(confirm("상품게시글을 등록하시겠습니까?")) {
				console.log("컨펌")
				return true
			} else {
				return false
			}
			
		})
		
	</script>
	
	
</body>
</html>