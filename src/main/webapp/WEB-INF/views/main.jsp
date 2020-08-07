<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
<title>WebSocket Chat</title>
<link rel="stylesheet" href="/css/chat.css" />
</head>
<body>
	<div id="username-page">
	    <div class="username-page-container">
	        <h1 class="title">username을 입력하세요</h1>
	        <div class="form-group">
	            <input type="text" id="name" placeholder="Username" autocomplete="off" class="form-control" />
	            <button type="button" id="enterChat" class="accent username-submit">채팅 시작하기</button>
	        </div>
	    </div>
	</div>
<script type="text/javascript">
var enterBtn = document.getElementById("enterChat");

enterBtn.addEventListener('click', function() {
	var username = document.getElementById('name').value.trim();
	if(username){
		location.href = "/joinchat?userName=" + username;		
	}
})

</script>
</body>
</html>