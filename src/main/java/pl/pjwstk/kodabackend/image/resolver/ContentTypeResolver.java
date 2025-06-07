package pl.pjwstk.kodabackend.image.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.image.strategy.ResourceLocation;

import java.io.IOException;

@Slf4j
@Component
public class ContentTypeResolver {

    public String resolveContentType(String filename, ResourceLocation location) {
        try {
            String contentType = location.getContentType();
            return contentType != null ? contentType : getContentTypeByExtension(filename);
        } catch (IOException e) {
            log.warn("Failed to probe content type for: {}, falling back to extension-based detection", filename);
            return getContentTypeByExtension(filename);
        }
    }

    private String getContentTypeByExtension(String filename) {
        if (filename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return switch (filename.toLowerCase()) {
            case String name when name.endsWith(".jpg") || name.endsWith(".jpeg") -> MediaType.IMAGE_JPEG_VALUE;
            case String name when name.endsWith(".png") -> MediaType.IMAGE_PNG_VALUE;
            case String name when name.endsWith(".webp") -> "image/webp";
            case String name when name.endsWith(".gif") -> MediaType.IMAGE_GIF_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }
}