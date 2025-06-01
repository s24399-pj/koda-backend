package pl.pjwstk.kodabackend.image.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "koda.upload")
public class ImageUploadProperties {
    private String dir = "classpath:offer-images/";
    private long maxFileSize = 5242880L; // 5MB
    private int maxFiles = 10;
    private Set<String> allowedTypes = Set.of("image/jpeg", "image/png", "image/webp");
    private Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "webp");
}