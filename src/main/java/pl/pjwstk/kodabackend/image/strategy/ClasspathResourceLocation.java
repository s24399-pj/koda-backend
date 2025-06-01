package pl.pjwstk.kodabackend.image.strategy;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public record ClasspathResourceLocation(Resource resource) implements ResourceLocation {

    @Override
    public boolean exists() {
        return resource.exists();
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(Paths.get(Objects.requireNonNull(resource.getFilename())));
    }
}