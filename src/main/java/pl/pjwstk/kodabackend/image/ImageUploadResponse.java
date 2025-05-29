package pl.pjwstk.kodabackend.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private UUID id;
    private String url;
    private String filename;
    private Long size;
    private String contentType;
    private Integer sortOrder;
}