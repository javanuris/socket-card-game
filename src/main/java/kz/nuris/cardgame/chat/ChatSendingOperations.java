package kz.nuris.cardgame.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.nuris.cardgame.chat.model.Message;
import kz.nuris.cardgame.chat.model.MessageResponse;
import kz.nuris.cardgame.chat.model.MessageType;
import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.model.Decision;
import kz.nuris.cardgame.service.game.model.GameResult;
import kz.nuris.cardgame.service.player.PlayerService;
import kz.nuris.cardgame.service.player.model.Player;
import kz.nuris.cardgame.service.session.CardGameSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatSendingOperations {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final PlayerService playerService;

    private final static String SPECIFIC_USER_QUEUE = "/secured/user/queue/specific-user-user";

    void sendConnect(String username, Map<Player, String> playersInRoom) {
        var message = new MessageResponse();
        message.setType(MessageType.CONNECT);
        message.setSender(username);
        playersInRoom.forEach((x, sessionId) -> sendMessage(playersInRoom.get(x), message));
    }

    void sendDisconnect(String username, Map<Player, String> playersInRoom) {
        var message = new MessageResponse();
        message.setType(MessageType.DISCONNECT);
        message.setSender(username);
        playersInRoom.forEach((x, sessionId) -> sendMessage(playersInRoom.get(x), message));
    }

    void sendError(Map<Player, String> playersInRoom, CardGameException.CardGameExceptionCode code) {
        var message = new MessageResponse();
        message.setType(MessageType.ERROR);
        message.setContent(code.name());
        playersInRoom.forEach((x, sessionId) -> sendMessage(playersInRoom.get(x), message));
    }

    void sendBalanceInfo(Long playerId, Map<Player, String> playersInRoom) {
        var player = playerService.getByIdOrBlow(playerId);
        var message = new MessageResponse();
        message.setType(MessageType.INFO);
        message.setContent(playerService.getByIdOrBlow(player.getId()).getTokens().toString());
        message.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                SPECIFIC_USER_QUEUE + playersInRoom.get(player), message);
    }

    void sendFinish(Long playerId, Map<Player, String> playersInRoom) {
        var player = playerService.getByIdOrBlow(playerId);
        var message = new MessageResponse();
        message.setType(MessageType.FINISH);
        message.setContent(playerService.getByIdOrBlow(player.getId()).getTokens().toString());
        message.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                SPECIFIC_USER_QUEUE + playersInRoom.get(player), message);
    }

    void sendDecision(Player player, Decision decision, Map<Player, String> playersInRoom) {
        var temp = new MessageResponse();
        temp.setType(MessageType.CHAT);
        temp.setContent(decision.name());
        temp.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                SPECIFIC_USER_QUEUE + playersInRoom.get(player), temp);
    }

    void sendGameResult(Player player, GameResult result, Map<Player, String> playersInRoom) {
        var temp = new MessageResponse();
        temp.setType(MessageType.RESULT);
        temp.setContent(result.name());
        temp.setSender(player.getName());
        simpMessageSendingOperations.convertAndSend(
                SPECIFIC_USER_QUEUE + playersInRoom.get(player), temp);
    }

    void sendDealCard(CardGameSession cardGameSession, Map<Player, String> playersInRoom) {

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
                SPECIFIC_USER_QUEUE + sessionId, message);
    }

}
