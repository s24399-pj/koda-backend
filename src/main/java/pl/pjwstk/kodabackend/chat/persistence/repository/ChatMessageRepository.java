package pl.pjwstk.kodabackend.chat.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.sender.id = :senderId AND m.recipient.id = :recipientId) " +
            "OR (m.sender.id = :recipientId AND m.recipient.id = :senderId) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatMessages(
            @Param("senderId") UUID senderId,
            @Param("recipientId") UUID recipientId);

    @Query("SELECT DISTINCT " +
            "CASE WHEN cm.sender.id = :userId THEN cm.recipient.id ELSE cm.sender.id END " +
            "FROM ChatMessage cm " +
            "WHERE cm.sender.id = :userId OR cm.recipient.id = :userId")
    List<UUID> findConversationPartnerIds(@Param("userId") UUID userId);

    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE ((cm.sender.id = :userId AND cm.recipient.id = :partnerId) " +
            "OR (cm.sender.id = :partnerId AND cm.recipient.id = :userId)) " +
            "ORDER BY cm.createdAt DESC")
    List<ChatMessage> findMessagesByConversation(
            @Param("userId") UUID userId,
            @Param("partnerId") UUID partnerId,
            Pageable pageable);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm " +
            "WHERE cm.recipient.id = :userId AND cm.sender.id = :partnerId " +
            "AND cm.status = 'SENT'")
    int countUnreadMessagesInConversation(
            @Param("userId") UUID userId,
            @Param("partnerId") UUID partnerId);
}