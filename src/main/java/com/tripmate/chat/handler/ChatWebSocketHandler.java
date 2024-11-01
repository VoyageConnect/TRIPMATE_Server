package com.tripmate.chat.handler;

import com.tripmate.chat.dto.MessageDTO;
import com.tripmate.chat.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageService messageService;  // 메시지 서비스 추가
    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 받은 메시지 처리
        String payload = message.getPayload();
        log.info("Received message: {}", payload);

        // 메시지를 DTO로 변환
        MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class);

        // 메시지를 DB에 저장 (추가된 부분)
        messageService.sendMessage(messageDTO);

        // 채팅방 내 모든 사용자에게 메시지 전송
        for (WebSocketSession webSocketSession : sessionMap.values()) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDTO)));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // WebSocket 연결이 이루어졌을 때 세션 저장
        sessionMap.put(session.getId(), session);
        log.info("WebSocket session established: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // WebSocket 연결이 종료되었을 때 세션 제거
        sessionMap.remove(session.getId());
        log.info("WebSocket session closed: {}", session.getId());
    }
}
