package pl.pjwstk.kodabackend.image.strategy;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ResourceLocation {
    boolean exists();

    Resource resource();

    String getContentType() throws IOException;
}