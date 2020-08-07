var userList = document.getElementById('userList');
var chatPage = document.querySelector('#chat-page');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var userColor = null;

window.onunload = function() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
};

window.onload = function() {
	getUserList();
	getConnect();
}

function randomUserColor() {
	userColor = "#" + Math.round(Math.random() * 0xFFFFFF).toString(16);
}

function getConnect() {

	var socket = new SockJS('/stomp-server');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function() {
		randomUserColor();

		stompClient.subscribe("/chat/join", function(data) {
			getUserList();
			receiveNotice(data);
		});

		stompClient.subscribe('/chat/public', receiveMsg);

		stompClient.subscribe('/user/chat/private', receiveMsg);

		stompClient.subscribe("/chat/leave", receiveNotice);
	});
}


function getUserList() {
	$('#userList').empty();
	$.getJSON("/getUserList", function(data) {
		$(data.userList).each(function() {
			var userElement = document.createElement('li');
			userElement.addEventListener('click', whisper);
			var userText = document.createTextNode(this);
			userElement.appendChild(userText);
			userList.appendChild(userElement);
		});
	});
}

function receiveNotice(notice) {
	var chatMsg = JSON.parse(notice.body);

	var messageElement = document.createElement('li');
	messageElement.classList.add('event-message');
	var textElement = document.createElement('p');
	var messageText = document.createTextNode(chatMsg.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}

function receiveMsg(message) {
	var chatMsg = JSON.parse(message.body);

	if (chatMsg.receiver) {
		chatMsg.content = chatMsg.sender + ">> " + chatMsg.content;
	}

	var messageElement = document.createElement('li');

	messageElement.classList.add('chat-message');

	var avatarElement = document.createElement('i');
	var avatarText = document.createTextNode(chatMsg.sender[0]);
	avatarElement.appendChild(avatarText);
	avatarElement.style['background-color'] = chatMsg.userColor;

	messageElement.appendChild(avatarElement);

	var usernameElement = document.createElement('span');
	var usernameText = document.createTextNode(chatMsg.sender);
	usernameElement.appendChild(usernameText);
	messageElement.appendChild(usernameElement);

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(chatMsg.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}

$('#sendChat').click(function() {
	var url = "/app/sendChat";
	var message = messageInput.value;
	if(message)
	var data = {
		content: message,
		userColor: userColor
	}
	stompClient.send(url, data);
	messageInput.value = "";

});

function whisper() {
	var user = $(this).html();
	messageInput.value = "/" + user + ": ";
}