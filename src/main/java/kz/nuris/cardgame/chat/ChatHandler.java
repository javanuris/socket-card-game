package kz.nuris.cardgame.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.nuris.cardgame.chat.model.*;
import kz.nuris.cardgame.service.game.model.Decision;
import kz.nuris.cardgame.service.game.model.GameResult;
import kz.nuris.cardgame.service.player.PlayerService;
import kz.nuris.cardgame.service.player.model.Player;
import kz.nuris.cardgame.service.session.CardGameSession;
import kz.nuris.cardgame.service.session.model.CardPlayerDecision;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatHandler {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ObjectProvider<CardGameSession> gameSessionFactory;

    private final PlayerService playerService;

    private CardGameSession cardGameSession;

    private Map<String, Integer> gameTypeCounter = new HashMap<>();
    private Map<Player, String> playersInRoom = new HashMap<>();
    private Map<Player, Decision> playersDecision = new HashMap<>();


    @EventListener
    public void handleWebSocketEventConnectListener(final SessionConnectedEvent event) {
        log.info("connected");
    }

    @EventListener
    public void handleWebSocketEventDisconnectListener(final SessionDisconnectEvent event) {
        log.info("disconnect");

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        playersInRoom.remove(new Player(username));

        var message = new MessageResponse();
        message.setType(MessageType.DISCONNECT);
        message.setSender(username);

        playersInRoom.forEach((x, sessionId) -> sendMessage(playersInRoom.get(x), message));
    }


    @MessageMapping("/chat.registerUser")
    public void registerUser(
            @Payload final RegisterPlayerMessage chatMessage,
            @Header("simpSessionId") String sessionId,
            SimpMessageHeaderAccessor headerAccessor) {

        var username = chatMessage.getSender();
        var gameType = chatMessage.getGameType();
        var tokens = chatMessage.getTokens();

        headerAccessor.getSessionAttributes().put("username", username);

        var player = new Player(ThreadLocalRandom.current().nextLong(), username, tokens);
        playerService.saveOrBlow(player);
        playersInRoom.put(player, sessionId);
        gameTypeCounter.put(gameType, gameTypeCounter.getOrDefault(gameType, 0) + 1);

        sendMessage(playersInRoom.get(player), chatMessage);
        sendBalanceInfo(player.getId());


        if (playersInRoom.size() > 1
                && gameTypeCounter.get(gameType) == playersInRoom.size()) {

            if (cardGameSession == null) {
                cardGameSession =
                        gameSessionFactory.getObject(gameType,
                                new ArrayList<>(playersInRoom.keySet()));
            }
            sendDealCard(cardGameSession);
        }
    }

    @MessageMapping("/chat.decision")
    public void sendDecision(
            @Payload final PlayerDecisionMessage message,
            @Header("simpSessionId") String sessionId) {

        var player = playerService.getByNameOrBlow(message.getSender());

        if (playersInRoom.get(player) != null && playersInRoom.get(player).equals(sessionId)) {
            if (playersDecision.get(player) == null) {

                playersDecision.put(player, message.getDecision());
                sendDecision(player, message.getDecision());

                if (playersDecision.size() == playersInRoom.size()) {

                    var decisions = playersDecision.entrySet().stream()
                            .map(x -> {
                                var temp = new CardPlayerDecision();
                                temp.setDecision(x.getValue());
                                temp.setPlayer(x.getKey());
                                return temp;
                            })
                            .collect(Collectors.toList());

                    var result = cardGameSession.play(decisions);

                    result.forEach(x -> {
                        sendGameResult(x.getPlayer(), x.getGameResult());
                        sendBalanceInfo(x.getPlayer().getId());
                    });

                    playersDecision.clear();

                    sendDealCard(cardGameSession);
                }
            }
        }
    }


    private void sendDealCard(CardGameSession cardGameSession) {

        cardGameSession.dealCards().forEach(x -> {
            String json = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                json = mapper.writeValueAsString(x.getHand());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            var message = new MessageResponse();
            message.setType(MessageType.CHAT);
            message.setSender(x.getPlayer().getName());
            message.setContent(json);

            sendMessage(playersInRoom.get(x.getPlayer()), message);
        });

    }

    private void sendMessage(String sessionId, Message message) {
        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user-user" + sessionId, message);
    }

    private void sendBalanceInfo(Long playerId) {
        var player = playerService.getByIdOrBlow(playerId);
        var message = new MessageResponse();
        message.setType(MessageType.INFO);
        message.setContent(playerService.getByIdOrBlow(player.getId()).getTokens().toString());
        message.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user-user" + playersInRoom.get(player), message);
    }

    private void sendDecision(Player player, Decision decision) {
        var temp = new MessageResponse();
        temp.setType(MessageType.CHAT);
        temp.setContent(decision.name());
        temp.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user-user" + playersInRoom.get(player), temp);
    }

    private void sendGameResult(Player player, GameResult result) {
        var temp = new MessageResponse();
        temp.setType(MessageType.RESULT);
        temp.setContent(result.name());
        temp.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                "/secured/user/queue/specific-user-user" + playersInRoom.get(player), temp);
    }
}
