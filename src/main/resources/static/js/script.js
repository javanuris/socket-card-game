'use strict'

let stompClient
let username
let gameType
let tokens

var sessionId = "";

const connect = (event) => {
    username = document.querySelector('#username').value.trim()
    gameType = document.querySelector('#gameType').value.trim()
    tokens = document.querySelector('#tokens').value


    if (username) {
        const login = document.querySelector('#login')
        login.classList.add('hide')

        const chatPage = document.querySelector('#chat-page')
        chatPage.classList.remove('hide')

        var socket = new SockJS('/secured/room');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            var url = stompClient.ws._transport.url;

            console.log("url: " + url);

            url = url.replace(
                "ws://localhost:8080/secured/room/", "");
            url = url.replace("/websocket", "");
            url = url.replace(/^[0-9]+\//, "");
            console.log("Your current session is: " + url);
            sessionId = url;
            onConnected();
        }, onError);
    }
    event.preventDefault()
}


const onConnected = options => {
    console.log("Your session is: " + sessionId);

    stompClient.subscribe('/secured/user/queue/specific-user' + '-user' + sessionId, onMessageReceived)

    stompClient.send("/chat.registerUser",
        {},
        JSON.stringify({sender: username, type: 'CONNECT', tokens: tokens, gameType: gameType})
    );

    const status = document.querySelector('#status')
    status.className = 'hide'
}

const onError = (error) => {
    const status = document.querySelector('#status')
    status.innerHTML = 'Could not find the connection you were looking for. Move along. Or, Refresh the page!'
    status.style.color = 'red'
}

const sendMessage = (event) => {
    const messageInput = document.querySelector('#message')
    const messageContent = messageInput.value.trim()

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            decision: messageInput.value,
            type: 'CHAT',
            time: moment().calendar()
        }
        stompClient.send('/chat.decision', {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
    event.preventDefault();
}


const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    console.log("onMessageReceived " + message);

    const chatCard = document.createElement('div')
    chatCard.className = 'card-body'

    const flexBox = document.createElement('div')
    flexBox.className = 'd-flex justify-content-end mb-4'
    chatCard.appendChild(flexBox)

    const messageElement = document.createElement('div')
    messageElement.className = 'msg_container_send'

    flexBox.appendChild(messageElement)

    if (message.type === 'CONNECT') {
        messageElement.classList.add('event-message')
        message.content = message.sender + ' in the room!'
    } else if (message.type === 'DISCONNECT') {
        messageElement.classList.add('event-message')
        message.content = message.sender + ' left the room!'
    } else if (message.type === 'INFO') {
        messageElement.classList.add('event-message')
        message.content = 'Balance:' + message.content + ' tokens'
    } else {
        messageElement.classList.add('chat-message')

        const avatarContainer = document.createElement('div')
        avatarContainer.className = 'img_cont_msg'
        const avatarElement = document.createElement('div')
        avatarElement.className = 'circle user_img_msg'
        const avatarText = document.createTextNode(message.sender[0])
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender)
        avatarContainer.appendChild(avatarElement)

        messageElement.style['background-color'] = getAvatarColor(message.sender)

        flexBox.appendChild(avatarContainer)

        const time = document.createElement('span')
        time.className = 'msg_time_send'
        time.innerHTML = message.time
        messageElement.appendChild(time)

    }

    if (message.type === 'RESULT') {
        messageElement.style['background-color'] = '#000ec6'
        messageElement.innerHTML = "Game result: " + message.content
    } else if (message.type === 'FINISH') {
        messageElement.style['background-color'] = '#aac601'
        messageElement.innerHTML = "Finish... Balance is: " + message.content
    } else if (message.type === 'ERROR') {
        messageElement.style['background-color'] = '#c60800'
        messageElement.innerHTML = "ERROR!!! " + message.content
    } else {
        messageElement.innerHTML = message.content
    }

    const chat = document.querySelector('#chat')
    chat.appendChild(flexBox)
    chat.scrollTop = chat.scrollHeight
}

const hashCode = (str) => {
    let hash = 0
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash)
    }
    return hash
}


const getAvatarColor = (messageSender) => {
    const colours = ['#2196F3', '#32c787', '#1BC6B4', '#A1B4C4']
    const index = Math.abs(hashCode(messageSender) % colours.length)
    return colours[index]
}

const loginForm = document.querySelector('#login-form')
loginForm.addEventListener('submit', connect, true)
const messageControls = document.querySelector('#message-controls')
messageControls.addEventListener('submit', sendMessage, true)