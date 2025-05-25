package pl.pjwstk.kodabackend.chat.mapper;

import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;

@Component
public class ChatMessageMapper {

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
}