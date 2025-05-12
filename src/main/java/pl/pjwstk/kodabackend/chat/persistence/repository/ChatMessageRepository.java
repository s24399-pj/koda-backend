package pl.pjwstk.kodabackend.chat.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findByRecipientIdAndStatusNot(UUID recipientId, MessageStatus status);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.sender.id = :senderId AND m.recipient.id = :recipientId) " +
            "OR (m.sender.id = :recipientId AND m.recipient.id = :senderId) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatMessages(
            @Param("senderId") UUID senderId,
            @Param("recipientId") UUID recipientId);

    List<ChatMessage> findByRecipientIdAndStatus(UUID recipientId, MessageStatus status);
}