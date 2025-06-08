package pl.pjwstk.kodabackend.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.dto.ConversationDto;
import pl.pjwstk.kodabackend.chat.mapper.ChatMessageMapper;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;
import pl.pjwstk.kodabackend.chat.persistence.repository.ChatMessageRepository;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AppUserRepository userRepository;
    private final ChatMessageMapper chatMessageMapper;

    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto messageDto) {
        AppUser sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender does not exist"));

        AppUser recipient = userRepository.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient does not exist"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(messageDto.getContent())
                .createdAt(LocalDateTime.now())
                .status(MessageStatus.SENT)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return chatMessageMapper.toDto(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessages(UUID senderId, UUID recipientId) {
        List<ChatMessage> messages = chatMessageRepository.findChatMessages(senderId, recipientId);
        return messages.stream()
                .map(chatMessageMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConversationDto> getUserConversations(UUID userId) {
        List<UUID> conversationPartnerIds = chatMessageRepository.findConversationPartnerIds(userId);

        return conversationPartnerIds.stream().map(partnerId -> {
            AppUser partner = userRepository.findById(partnerId)
                    .orElseThrow(() -> new EntityNotFoundException(AppUser.class.getName(),
                            "User does not exist"));

            List<ChatMessage> recentMessages = chatMessageRepository.findMessagesByConversation(
                    userId, partnerId, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt")));

            ChatMessage lastMessage = recentMessages.isEmpty() ? null : recentMessages.getFirst();

            int unreadCount = chatMessageRepository.countUnreadMessagesInConversation(userId, partnerId);

            String profilePictureBase64 = convertProfilePictureToBase64(partner.getProfilePicture());

            return ConversationDto.builder()
                    .userId(partnerId)
                    .userName(partner.getFirstName() + " " + partner.getLastName())
                    .profilePicture(profilePictureBase64)
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                    .lastMessageDate(lastMessage != null ? lastMessage.getCreatedAt() : null)
                    .unreadCount(unreadCount)
                    .build();
        }).toList();
    }

    private String convertProfilePictureToBase64(byte[] profilePicture) {
        if (profilePicture == null || profilePicture.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(profilePicture);
    }
}