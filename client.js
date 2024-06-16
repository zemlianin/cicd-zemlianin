const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

// Подключение к вашему WebSocket серверу
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // Подписка на топик
    stompClient.subscribe('/topic/accounts', function(message) {
        console.log('Received: ' + message.body);
    });
}, function(error) {
    console.error('Connection error: ' + error);
});
