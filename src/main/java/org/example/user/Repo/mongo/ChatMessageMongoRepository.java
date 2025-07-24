package org.example.user.Repo.mongo;

import org.example.user.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long sender1, Long receiver1, Long sender2, Long receiver2);
}
