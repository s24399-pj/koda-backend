package pl.pjwstk.kodabackend.image.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceLocationStrategy {

    private final ResourceLoader resourceLoader;

    @Value("${koda.upload.dir:classpath:offer-images/}")
    private String uploadDir;

    public ResourceLocation resolveLocation(String filename) {
        return uploadDir.startsWith("classpath:")
                ? resolveClasspathLocation(filename)
                : resolveFileSystemLocation(filename);
    }

    private ResourceLocation resolveClasspathLocation(String filename) {
        String resourcePath = uploadDir + filename;
        Resource resource = resourceLoader.getResource(resourcePath);

        log.debug("Resolving classpath location: {}", resourcePath);
        return new ClasspathResourceLocation(resource);
    }

    private ResourceLocation resolveFileSystemLocation(String filename) {
        Path filePath = Paths.get(uploadDir, filename);

        log.debug("Resolving filesystem location: {}", filePath);
        return new FileSystemResourceLocation(filePath);
    }
}