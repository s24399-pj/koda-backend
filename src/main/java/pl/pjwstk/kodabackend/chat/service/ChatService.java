package pl.pjwstk.kodabackend.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.mapper.ChatMessageMapper;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;
import pl.pjwstk.kodabackend.chat.persistence.repository.ChatMessageRepository;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AppUserRepository userRepository;
    private final ChatMessageMapper chatMessageMapper;

    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto messageDto) {
        AppUser sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Nadawca nie istnieje"));

        AppUser recipient = userRepository.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Odbiorca nie istnieje"));

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
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMessageAsDelivered(UUID messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException(ChatMessage.class.getName(), "Wiadomość nie istnieje"));

        message.setStatus(MessageStatus.DELIVERED);
        chatMessageRepository.save(message);
    }

    @Transactional
    public void markMessageAsRead(UUID messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException(ChatMessage.class.getName(), "Wiadomość nie istnieje"));

        message.setStatus(MessageStatus.READ);
        chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getUnreadMessages(UUID userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findByRecipientIdAndStatus(userId, MessageStatus.SENT);

        return unreadMessages.stream()
                .map(chatMessageMapper::toDto)
                .collect(Collectors.toList());
    }
}