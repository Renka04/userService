package org.example.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.user.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat API", description = "Send and retrieve encrypted chat messages")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "Send a message (encrypted and stored in MongoDB)")
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @Parameter(description = "ID of the sender") @RequestParam Long senderId,
            @Parameter(description = "ID of the receiver") @RequestParam Long receiverId,
            @Parameter(description = "The plain text message") @RequestParam String message) {
        try {
            chatService.sendMessage(senderId, receiverId, message);
            return ResponseEntity.ok("Message sent and encrypted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send message: " + e.getMessage());
        }
    }

    @Operation(summary = "Get decrypted conversation between two users")
    @GetMapping("/conversation")
    public ResponseEntity<List<String>> getConversation(
            @Parameter(description = "User 1 ID") @RequestParam Long user1,
            @Parameter(description = "User 2 ID") @RequestParam Long user2) {
        List<String> conversation = chatService.getConversation(user1, user2);
        return ResponseEntity.ok(conversation);
    }
}
