package org.example.user.Controller;

import org.example.user.Service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam Long senderId,
                            @RequestParam Long receiverId,
                            @RequestParam String message) throws Exception {
        chatService.sendMessage(senderId, receiverId, message);
    }

    @GetMapping("/conversation")
    public List<String> getConversation(@RequestParam Long user1,
                                        @RequestParam Long user2) {
        return chatService.getConversation(user1, user2);
    }
}
