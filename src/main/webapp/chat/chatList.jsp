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
<body>


	<div style="border: 1px solid black">
	
	<c:if test = "${list[0].user1 ne null }">
		<c:forEach var="i" items="${list}">

			<c:if test="${(i.user1 eq loginId)}">

				<div class="invisible" style="border: 1px solid black; width: 500px">

					<div>
						<a
							href="/chatWith.chat?with=${i.user2}">${i.user2 }님과의
							대화</a>
					</div>

					<c:forEach var="j" items="${list2}">
						<c:if
							test="${(j.sender eq i.user1 && j.receiver eq i.user2) or (j.receiver eq i.user1 && j.sender eq i.user2) }">
							<span>${j.contents }</span>
							<c:if test="${j.receiver eq loginId && j.read == 0 }">
								<span style="color: red;">New</span>
							</c:if>
						</c:if>
					</c:forEach>
					<button class="roomOut" opponentId=${i.user2 } invisibleTo=${i.invisibleTo }>채팅방 나가기</button>
				</div>

			</c:if>

			<c:if test="${(i.user2 eq loginId)}">

				<div class="invisible" style="border: 1px solid black; width: 500px">
					<div>
						<a
							href="/chatWith.chat?with=${i.user1}">${i.user1 }님과의
							대화</a>
					</div>

					<c:forEach var="j" items="${list2}">
						<c:if
							test="${(j.sender eq i.user1 && j.receiver eq i.user2) or (j.receiver eq i.user1 && j.sender eq i.user2) }">
							<span>${j.contents }</span>
							<c:if test="${j.receiver eq loginId && j.read == 0 }">
								<span style="color: red;">New</span>
							</c:if>
						</c:if>
					</c:forEach>
					<button class="roomOut" opponentId=${i.user1 } invisibleTo=${i.invisibleTo }>채팅방 나가기</button>
				</div>

			</c:if>

		</c:forEach>
		</c:if>
		
		<c:if test="${list[0].user1 eq null }">
			목록이 없습니다.
		</c:if>
	</div>

	<script>
		$(".roomOut").on("click", function() {
			$.ajax({
				url : "/invisibleTo.chat",
				data : {
					loginId : "${loginId}",
					opponentId : $(this).attr("opponentId"),
					invisibleTo : $(this).attr("invisibleTo")
				},
				dataType:"json"	
			}).done(function(resp) {
				location.reload();
			})
			//console.log($(this).attr("test"));
			//location.href = "/invisibleTo.chat?logintId="${loginId}&opponent;
		})
	</script>
</body>
</html>