<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시판</title>
<style>
	table a{
		text-decoration: none;
		color:black;
	}
	table a:hover{
		color:gray;
	}
</style>
</head>

<body>
	<div id="map" style="width:1000px;height:550px;margin-left:18%;"></div>

    <script type="text/javascript"
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=5dcd735409090fa849ba067f9017acc5"></script>
    <script type="module">
        import { item } from "/carBoard/electinfo.js";
        var mapContainer = document.getElementById('map'), // 지도의 중심좌표
            mapOption = {
                center: new kakao.maps.LatLng(37.567247, 126.982793), // 지도의 중심좌표
                level: 5 // 지도의 확대 레벨
            };

        var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        // 마커를 표시할 위치와 내용을 가지고 있는 객체 배열입니다 


      



         for(var i = 0; i < 1000; i++) {
            // 마커를 생성합니다
            var marker = new kakao.maps.Marker({
                map: map, // 마커를 표시할 지도
                position: new kakao.maps.LatLng(item[i].lat,item[i].lng) // 마커의 위치
            });

            // 마커에 표시할 인포윈도우를 생성합니다 
            var infowindow = new kakao.maps.InfoWindow({
                content: '<div style=width:350px; height:130px; >'+'<h3 style=padding:0%;margin:0%;background-color:#E6E6E6;>'+'<span style=margin:5px;>'+item[i].statnm+'</span>'+'</h3>'+'<span style=font-size:14px;margin:5px>'+item[i].addr+'</span>'+'<br>'+'<span style=font-size:14px;margin:5px>'+item[i].usetime +'</span>'+'<br>'+'<span style=font-size:14px;margin:5px>'+'주차료무료(Y/N) : '+item[i].parkingfree+'</span>'+'<br>'+'<span style=font-size:14px;margin:5px>'+'연락처 : '+item[i].busicall+'</span>'+'</div>' // 인포윈도우에 표시할 내용
            });

            // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
            // 이벤트 리스너로는 클로저를 만들어 등록합니다 
            // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
            kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
            kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
        }

        // 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
        function makeOverListener(map, marker, infowindow) {
            return function () {
                infowindow.open(map, marker);
            };
        }

        // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
        function makeOutListener(infowindow) {
            return function () {
                infowindow.close();
            };
        }
	

	 </script>


	<table border=1 width=800px align=center>
		<tr>
			<th colspan=5>전기차 게시판</th>
		</tr>
		<tr align=center>
			<td width=5%></td>
			<td width=50%>제목</td>
			<td width=10%>작성자</td>
			<td width=10%>날짜</td>
			<td width=5%>조회수</td>
		</tr>

		<c:forEach var="i" items="${list }">
			<tr>
				<td>${i.seq}
				<td><a href="detail.carBoard?seq=${i.seq }">${i.title }</a>
				<td>${i.writer }
				<td>${i.writeDate }
				<td>${i.viewCount }
			</tr>
		</c:forEach>

		<tr>
			<td colspan=5 align=center>
				${navi }
			</td>
		</tr>
		<tr>
			<td colspan=5 align=right><a href="/index.jsp"><button
						id="toMain">Main으로</button></a> <a href="/write.carBoard"><button>작성하기</button></a>
			</td>
		</tr>
	</table>
</body>

</html>