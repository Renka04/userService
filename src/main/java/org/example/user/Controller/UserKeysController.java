package org.example.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.user.Service.UserKeysService;
import org.example.user.Signal.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
@Tag(name = "User Keys API", description = "Operations for Signal keys generation, encryption and decryption")
public class UserKeysController {

    private final UserKeysService userKeysService;
    private final Message messageService;

    public UserKeysController(UserKeysService userKeysService, Message messageService) {
        this.userKeysService = userKeysService;
        this.messageService = messageService;
    }

    @Operation(summary = "Generate and save Signal keys for a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Keys generated and saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Failed to generate keys", content = @Content)
            })
    @PostMapping("/{userId}/generate")
    public ResponseEntity<String> generateKeys(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        try {
            userKeysService.generateAndSaveKeys(userId);
            return ResponseEntity.ok("Keys generated and saved for user " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate keys: " + e.getMessage());
        }
    }

    @Operation(summary = "Encrypt a message from sender to receiver",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Plain text message to encrypt",
                    required = true,
                    content = @Content(schema = @Schema(type = "string"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encrypted message returned"),
                    @ApiResponse(responseCode = "400", description = "Encryption failed", content = @Content)
            })
    @PostMapping("/encrypt")
    public ResponseEntity<String> encryptMessage(
            @Parameter(description = "Sender user ID", required = true)
            @RequestParam Long senderId,
            @Parameter(description = "Receiver user ID", required = true)
            @RequestParam Long receiverId,
            @RequestBody String plainText) {
        try {
            String encrypted = messageService.encryptMessage(senderId, receiverId, plainText);
            return ResponseEntity.ok(encrypted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Encryption failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Decrypt an encrypted message for receiver from sender",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Encrypted message in Base64 format",
                    required = true,
                    content = @Content(schema = @Schema(type = "string"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Decrypted plain text message returned"),
                    @ApiResponse(responseCode = "400", description = "Decryption failed", content = @Content)
            })
    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptMessage(@RequestParam Long receiverId,
                                                 @RequestParam Long senderId,
                                                 @RequestBody String encryptedBase64) {
        try {
            if (encryptedBase64.startsWith("\"") && encryptedBase64.endsWith("\"")) {
                encryptedBase64 = encryptedBase64.substring(1, encryptedBase64.length() - 1);
            }

            String decrypted = messageService.decryptMessage(receiverId, senderId, encryptedBase64);
            return ResponseEntity.ok(decrypted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage());
        }
    }

}
