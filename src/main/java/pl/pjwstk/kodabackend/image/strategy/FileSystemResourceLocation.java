package pl.pjwstk.kodabackend.image.strategy;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemResourceLocation implements ResourceLocation {
    private final Path filePath;

    public FileSystemResourceLocation(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean exists() {
        return Files.exists(filePath);
    }

    @Override
    public Resource resource() {
        return new FileSystemResource(filePath);
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(filePath);
    }
}