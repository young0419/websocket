<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
<title>WebSocket Chat</title>
<link rel="stylesheet" href="/css/chat.css" />
</head>
<body>
	<div id="chat-page">
		<div class="chat-container">
			<div>
				<h2>유저목록</h2>
				<ul id="userList">
				</ul>
			</div>
			<div class="chat-header">
				<h2>Spring WebSocket Chat Demo</h2>
			</div>
			<ul id="messageArea">
			</ul>
			<div class="form-group">
				<div class="input-group clearfix">
					<input type="text" id="message" placeholder="Type a message..."
						autocomplete="off" class="form-control" />
					<button type="button" id="sendChat" class="primary">보내기</button>
				</div>
			</div>
		</div>
	</div>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
<script	src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
<script	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/js/chat.js"></script>
</body>
</html>