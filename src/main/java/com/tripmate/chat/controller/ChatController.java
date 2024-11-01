package com.tripmate.chat.controller;

import com.tripmate.chat.domain.ChatRoom;
import com.tripmate.chat.domain.Message;
import com.tripmate.chat.dto.MessageDTO;
import com.tripmate.chat.service.ChatRoomService;
import com.tripmate.chat.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    /**
     *
     * 매칭 url = api/match 라고 가정,
     * 매칭하고 나서 상대에게 채팅하기를 누르면 api/match/room 으로 POST 요청 -> 채팅 방 만들기
     */
    @Operation(summary = "채팅 방 생성", description = "원하는 시용자와 채팅을 누르면 그 사용자와 채팅 방 생성")
    @PostMapping("api/match/room")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String userId1, @RequestParam String userId2) {

        // 채팅방 생성
        ChatRoom chatRoom = chatRoomService.createChatRoom(userId1, userId2);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 채팅방이 생성되면 프론트는 chatRoom의 id를 이용하여 해당 채팅방으로 이동해야 함

     * 만약 채팅이 진행 중이라면, 로그인 시 매칭화면이 아닌 채팅화면으로 바로 이동하게 끔 해야함

     * 채팅 방 url = api/chat/room/{chatRoomId}
     * 메세지 보내면, /api/chat/room/{chatRoomId}/message 로 POST
     */


    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방의 모든 메시지를 시간순으로 조회")
    @GetMapping("/api/chat/room/{chatRoomId}/messages")
    public ResponseEntity<List<Message>> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        List<Message> messages = messageService.getMessagesByChatRoom(chatRoomId);
        return ResponseEntity.ok(messages);
    }
}

