package com.tripmate.chat.service;

import com.tripmate.chat.domain.ChatRoom;
import com.tripmate.chat.domain.Message;
import com.tripmate.chat.dto.MessageDTO;
import com.tripmate.user.domain.User;
import com.tripmate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ChatRoomServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        // 테스트용 사용자 생성
        User user1 = new User();
        user1.setNickname("User1");
        user1.setUserId("testUserId1");  // userId 필드 추가
        userRepository.save(user1);

        User user2 = new User();
        user2.setNickname("User2");
        user2.setUserId("testUserId2");  // userId 필드 추가
        userRepository.save(user2);
    }


    @Test
    public void 채팅방생성하고메세지() {
        // DB에 넣은 userId를 사용하여 테스트
        User user1 = userRepository.findByNickname("User1").orElseThrow();
        User user2 = userRepository.findByNickname("User2").orElseThrow();

        // 채팅방 생성
        ChatRoom chatRoom = chatRoomService.createChatRoom(user1.getUserId(), user2.getUserId());

        // 결과 검증
        assertNotNull(chatRoom);
        assertTrue(chatRoom.getParticipants().contains(user1));
        assertTrue(chatRoom.getParticipants().contains(user2));


        // user1이 메세지 보냄
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSenderId(user1.getId());
        messageDTO.setChatRoomId(chatRoom.getId());
        messageDTO.setContent("안녕하세요");

        Message message = messageService.sendMessage(messageDTO);
        assertNotNull(message);

        // 메시지 잘 보내졌나 sendMessage 하고 저장되면 message 반환
        assertThat(message.getContent()).isEqualTo("안녕하세요");
        assertThat(message.getSender()).isEqualTo(user1);
        assertThat(message.getChatRoom()).isEqualTo(chatRoom);

        // 메세지 sender 가져와서 보낸 user 와 맞는지
        User sender = message.getSender();
        assertThat(sender.getId()).isEqualTo(user1.getId());
        assertThat(sender.getNickname()).isEqualTo(user1.getNickname());



    }
}
