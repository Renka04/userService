package org.example.user.Service;

import org.example.user.Entity.ChatMessage;
import org.example.user.Repo.ChatMessageRepository;
import org.example.user.Signal.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final MessageService messageService;

    public ChatService(ChatMessageRepository chatMessageRepository, MessageService messageService) {
        this.chatMessageRepository = chatMessageRepository;
        this.messageService = messageService;
    }

    public void sendMessage(Long senderId, Long receiverId, String plainText) throws Exception {
        String encrypted = messageService.encryptMessage(senderId, receiverId, plainText);

        ChatMessage message = new ChatMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setEncryptedMessage(encrypted);
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);
    }

    public List<String> getConversation(Long user1, Long user2) {
        List<ChatMessage> messages = chatMessageRepository.findConversation(user1, user2);

        return messages.stream()
                .map(msg -> {
                    try {
                        return messageService.decryptMessage(
                                msg.getReceiverId(), msg.getSenderId(), msg.getEncryptedMessage()
                        );
                    } catch (Exception e) {
                        return "[Decryption failed]";
                    }
                })
                .toList();
    }
}
