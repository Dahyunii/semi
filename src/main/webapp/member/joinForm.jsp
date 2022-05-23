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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<style>
        * {
            box-sizing: border-box;
        }

        #table {
            width: 600px;
            margin: 0 auto;
        }

        #table tr:not(#a, #b) td:first-child {
            text-align: right;
        }

</style>
<body>

	<form action="/insert.member" method="post" id="form">

    <table id="table" border="1">

        <tr id="a">
            <th colspan="2">회원 가입 정보 입력</th>
        </tr>
        <tr>
            <td style="width:20%;">아이디</td>
            <td>
                <input type="text" id="id" name="id" class="input">
                <button type=button id="duplCheck">중복확인</button>
            </td>
        </tr>
        <tr>
            <td>패스워드</td>
            <td>
                <input type="password" id="pw" name="pw" class="input">
            </td>
        </tr>
        <tr>
            <td>패스워드 확인</td>
            <td>
                <input type="password" id="pwCheck" class="input">
                <input type="text" id="result" style="border:0; width:200px;">
            </td>
        </tr>
        <tr>
            <td>이름</td>
            <td>
                <input type="text" id="name" name="name" class="input">
            </td>
        </tr>
        <tr>
            <td>전화번호</td>
            <td>
                <input type="text" id="phone" name="phone" class="input">
            </td>
        </tr>
        <tr>
            <td>이메일</td>
            <td>
                <input type="text" id="email" name="email" class="input">
            </td>
        </tr>
        <tr>
            <td>우편번호</td>
            <td>
                <input type="text" id="zipcode" name="zipcode" placeholder="우편번호">
                <input type="text" id="roadAddress" name="roadAddress" placeholder="도로명주소">
                <!-- form 태그 내의 button 은 기본 submit 으로 작동한다. -->
                <button type="button" id="search">주소찾기</button>
                <input type="text" id="jibunAddress" name="jibunAddress" placeholder="지번주소">
            </td>
        </tr>
        <tr>
            <td>상세주소</td>
            <td>
                <input type="text" id="specAddress" name="specAddress">
            </td>
        </tr>
        <tr>
        	<td>개인질문답변
        	<td>당신의 보물 1호는?<br>
        	<input type="text" id="personalAnswer" name="personalAnswer">
        </tr>
        <tr id="b" align="center">
            <td colspan="2">
                    <input type="submit" value="회원가입" id="btnSubmit">
                    <input type="reset" value="다시 입력" id="btnReset">
                    <button type="button" id="back">뒤로가기</button>
            </td>
        </tr>

    </table>

    </form>
    
    
    
    <script>
    
    	// 카카오 지도 api
        document.getElementById("search").onclick = execDaumPostcode;

        function execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function (data) {

                    document.getElementById('zipcode').value = data.zonecode;
                    document.getElementById("roadAddress").value = data.roadAddress;
                    document.getElementById("jibunAddress").value = data.jibunAddress;

        	        }
    	        }).open();
	        }
    	
    	
        // 아이디 중복검사
		$("#duplCheck").on("click", function(){
    		let idRegex = /^[a-z][a-z\d_]{5,13}$/g;
    		
			if($("#id").val() == "") {
				alert("아이디를 입력해주세요.")
				$("#id").focus()
				return false
			} else if(idRegex.test($("#id").val()) == false) {
				alert("아이디는 6~14자의 소문자, 숫자, '_' 만 사용 가능하며 숫자와 '_' 는 첫 글자로 올 수 없습니다.")
				$("#id").val("")
				$("#id").focus()
				return false
			}
			
			$.ajax({
				url : "/duplCheck.member",
				data : {id : $("#id").val()},
				dataType : "json"
			}).done(function(result){
				if(result) {
					alert("이미 존재하는 아이디입니다.")
					$("#id").val("")
					$("#id").focus()
				} else {
					if(confirm("사용가능한 아이디입니다. 사용하시려면 확인을 눌러주세요.")) {
						$("#pw").focus()
					} else {
						$("#id").val("")
						$("#id").focus()
					}
				}
			})
			
		})
        
        
		// 패스워드 일치 확인
		$("#pwCheck").on("input", function(){
			if($("#pw").val() == $(this).val()) {
				$("#result").val("패스워드가 일치합니다.").css("color", "dodgerblue")
			} else {
				$("#result").val("패스워드가 일치하지 않습니다.").css("color", "crimson")
			}
		})
		
		
		// 양식 제출
		$("#form").on("submit", function(e){
			let idRegex = /^[a-z][a-z\d_]{5,13}$/g;
			let pwRegex = /.{8,16}/g;
			let nameRegex = /^[가-힣]{2,5}$/g;
			let phoneRegex = /^01\d-?\d{3,4}-?\d{4}$/g;
			let emailRegex = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/g;
			
			// 아이디
            if ($("#id").val() == "") {
                alert("아이디를 입력해주세요.")
                $("#id").focus()
                return false
            } else if (idRegex.test($("#id").val()) == false) {
                alert("아이디는 6~14자의 소문자, 숫자, '_' 만 사용 가능하며 숫자와 '_' 는 첫 글자로 올 수 없습니다.")
                $("#id").val("")
                $("#id").focus()
                return false
            // 패스워드 확인
            } else if ($("#pw").val() == "") {
                alert("패스워드를 입력해주세요.")
                $("#pw").focus()
                return false
            } else if (pwRegex.test($("#pw").val()) == false) {
                alert("패스워드는 8~16자로 입력해주세요.")
                $("#pw").val("")
                $("#pw").focus()
                return false
            // 이름
            } else if ($("#name").val() == "") {
                alert("이름을 입력해주세요.")
                $("#name").focus()
                return false
            } else if (nameRegex.test($("#name").val()) == false) {
                alert("이름은 한글 2~5자로 입력해주세요.")
                $("#name").val("")
                $("#name").focus()
                return false
            // 전화번호
            } else if ($("#phone").val() == "") {
                alert("전화번호를 입력해주세요.")
                $("#phone").focus()
                return false
            } else if (phoneRegex.test($("#phone").val()) == false) {
                alert("전화번호를 형식에 맞게 입력해주세요.")
                $("#phone").val("")
                $("#phone").focus()
                return false
            // 이메일
            } else if ($("#email").val() == "") {
                alert("이메일을 입력해주세요.")
                $("#email").focus()
                return false
            } else if (emailRegex.test($("#email").val()) == false) {
                alert("이메일을 형식에 맞게 입력해주세요.")
                $("#email").val("")
                $("#email").focus()
                return false
            // 우편번호
            } else if ($("#zipcode").val() == "") {
            	alert("우편번호를 입력해주세요.")
            	$("#zipcode").focus()
            	return false
       		// 도로명주소, 지번주소
            } else if ($("#roadAddress").val() == "" && $("#jibunAddress").val() == "") {
            	alert("도로명주소 또는 지번주소를 입력해주세요.")
            	$("#roadAddress").focus()
            	return false
            } else if ($("#specAddress").val() == "") {
            	alert("상세주소를 입력해주세요.")
            	$("#specAddress").focus()
            	return false
            // 개인질문답변
            } else if ($("#personalAnswer").val() == "") {
            	alert("개인질문답변을 입력해주세요.")
            	$("#personalAnswer").focus()
            	return false
            // 가입완료
            } else {
				e.preventDefault()
    			
    			Swal.fire({
    				title : "Welcome!",
    				text : "회원가입이 완료되었습니다!",
    				icon : "success"
    			}).then(function(){
    				document.getElementById("form").submit();
    			})
            }

		})
		
		// 뒤로가기
		$("#back").on("click", function(){
        	location.href="/index.jsp"
        })
		
		
    </script>

</body>
</html>