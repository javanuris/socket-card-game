package kz.nuris.cardgame.controller;

import kz.nuris.cardgame.controller.model.ChatMessage;
import kz.nuris.cardgame.controller.model.MessageType;
import kz.nuris.cardgame.service.player.model.Player;
import kz.nuris.cardgame.service.session.CardGameSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ObjectProvider<CardGameSession> gameSessionFactory;

    private CardGameSession cardGameSession;

    private Map<String, Integer> sessionTypeDetector = new HashMap<>();
    private Set<String> playerCount = new HashSet<>();

    // @MessageMapping("/chat.newUser")
    public ChatMessage newUser(@Payload final ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("new user");
        var username = chatMessage.getSender();
        var gameType = chatMessage.getContent();

        headerAccessor.getSessionAttributes().put("username", username);

        playerCount.add(username);
        sessionTypeDetector.put(gameType, sessionTypeDetector.getOrDefault(gameType, 0) + 1);

        if (sessionTypeDetector.get(gameType) > 1) {

            var players = playerCount.stream().map(x -> {
                var player = new Player();
                player.setId(ThreadLocalRandom.current().nextLong());
                player.setTokens(BigDecimal.valueOf(1000));
                player.setName(x);
                return player;
            }).collect(Collectors.toList());


            cardGameSession =
                    gameSessionFactory.getObject("single", players);

            cardGameSession.dealCards().forEach(x -> {


                var message = ChatMessage.builder()
                        .type(MessageType.CHAT)
                        .sender(username)
                        .content(x.getHand().toString())
                        .build();

                simpMessageSendingOperations.convertAndSend(
                        "/topic/public",
                        message);

            });

        }

        return chatMessage;
    }

    @EventListener
    public void handleWebSocketEventConnectListener(final SessionConnectedEvent event) {
        log.info("@connected@");
    }

    @EventListener
    public void handleWebSocketEventDisconnectListener(final SessionDisconnectEvent event) {
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        final String username = (String) headerAccessor.getSessionAttributes().get("username");

        playerCount.remove(username);

        final ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.DISCONNECT)
                .sender(username).build();

        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }


    @MessageMapping("/chat.newUser")
    public void newUser(
            @Payload final ChatMessage chatMessage,
            @Header("simpSessionId") String sessionId) throws Exception {

        var username = chatMessage.getSender();
        var gameType = chatMessage.getContent();


        playerCount.add(username);
        sessionTypeDetector.put(gameType, sessionTypeDetector.getOrDefault(gameType, 0) + 1);

        if (sessionTypeDetector.get(gameType) > 1) {

            var players = playerCount.stream().map(x -> {
                var player = new Player();
                player.setId(ThreadLocalRandom.current().nextLong());
                player.setTokens(BigDecimal.valueOf(1000));
                player.setName(x);
                return player;
            }).collect(Collectors.toList());


            cardGameSession =
                    gameSessionFactory.getObject("single", players);

            cardGameSession.dealCards().forEach(x -> {


                var message = ChatMessage.builder()
                        .type(MessageType.CHAT)
                        .sender(username)
                        .content(x.getHand().toString())
                        .build();


                simpMessageSendingOperations.convertAndSend(
                        "/secured/user/queue/specific-user" + "-user" + x.getPlayer().getName(), message);

            });


        }

        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user" + "-user" + sessionId, chatMessage);
    }


    @MessageMapping("/chat.send")
    public void sendSpecific(
            @Payload final ChatMessage chatMessage,
            @Header("simpSessionId") String sessionId) throws Exception {
        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user" + "-user" + sessionId, chatMessage);
    }
}
