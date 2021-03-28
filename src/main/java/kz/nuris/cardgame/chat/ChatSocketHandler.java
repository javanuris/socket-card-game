package kz.nuris.cardgame.chat;

import kz.nuris.cardgame.chat.model.*;
import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.model.Decision;
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
public class ChatSocketHandler {

    private final ChatSendingOperations sendingOperations;

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

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String username =
                (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        log.info("disconnect username: " + username);

        playersInRoom.remove(new Player(username));

        sendingOperations.sendDisconnect(username, playersInRoom);
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
        try {
            var player = new Player(ThreadLocalRandom.current().nextLong(), username, tokens);
            playerService.saveOrBlow(player);
            playersInRoom.put(player, sessionId);
            gameTypeCounter.put(gameType, gameTypeCounter.getOrDefault(gameType, 0) + 1);

            sendingOperations.sendConnect(username, playersInRoom);
            sendingOperations.sendBalanceInfo(player.getId(), playersInRoom);


            if (playersInRoom.size() >= CardGameSession.MIN_PLAYERS_COUNT
                    && gameTypeCounter.get(gameType) == playersInRoom.size()) {

                if (cardGameSession == null) {
                    cardGameSession =
                            gameSessionFactory.getObject(gameType,
                                    new ArrayList<>(playersInRoom.keySet()));
                }
                sendingOperations.sendDealCard(cardGameSession, playersInRoom);
            }
        } catch (CardGameException e) {
            sendingOperations.sendError(playersInRoom, e.getCode());
            log.error(e);
        }
    }

    @MessageMapping("/chat.decision")
    public void sendDecision(
            @Payload final PlayerDecisionMessage message,
            @Header("simpSessionId") String sessionId) {

        try {
            var player = playerService.getByNameOrBlow(message.getSender());

            if (playersInRoom.get(player) != null && playersInRoom.get(player).equals(sessionId)) {

                //ignore if player's decision have already been made
                if (playersDecision.get(player) == null) {

                    playersDecision.put(player, message.getDecision());
                    sendingOperations.sendDecision(player, message.getDecision(), playersInRoom);


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
                            sendingOperations.sendGameResult(x.getPlayer(), x.getGameResult(), playersInRoom);
                            sendingOperations.sendBalanceInfo(x.getPlayer().getId(), playersInRoom);
                        });

                        // clean for getting new decision from players
                        playersDecision.clear();

                        if (cardGameSession.isDeckEmpty()) {
                            result.forEach(x ->
                                    sendingOperations.sendFinish(x.getPlayer().getId(), playersInRoom));
                        } else {
                            sendingOperations.sendDealCard(cardGameSession, playersInRoom);
                        }
                    }
                }
            }
        } catch (CardGameException e) {
            sendingOperations.sendError(playersInRoom, e.getCode());
            log.error(e);
        }
    }


}
