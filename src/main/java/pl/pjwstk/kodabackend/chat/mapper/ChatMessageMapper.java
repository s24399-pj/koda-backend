package pl.pjwstk.kodabackend.chat.mapper;

import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

    /**
     * Konwertuje encję ChatMessage na obiekt DTO
     */
    public ChatMessageDto toDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }

        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .senderId(chatMessage.getSender().getId())
                .senderName(chatMessage.getSender().getFirstName() + " " + chatMessage.getSender().getLastName())
                .recipientId(chatMessage.getRecipient().getId())
                .recipientName(chatMessage.getRecipient().getFirstName() + " " + chatMessage.getRecipient().getLastName())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .status(chatMessage.getStatus())
                .build();
    }

    /**
     * Konwertuje listę encji ChatMessage na listę obiektów DTO
     */
    public List<ChatMessageDto> toDtoList(List<ChatMessage> chatMessages) {
        if (chatMessages == null) {
            return Collections.emptyList();
        }

        return chatMessages.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Aktualizuje encję ChatMessage na podstawie danych z DTO
     * Używane przy aktualizacji istniejących wiadomości
     */
    public void updateEntityFromDto(ChatMessageDto dto, ChatMessage entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Aktualizujemy tylko te pola, które mogą się zmienić (np. status)
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        // Treść wiadomości nie powinna być modyfikowana po utworzeniu
        // Podobnie jak nadawca, odbiorca i data utworzenia
    }
}