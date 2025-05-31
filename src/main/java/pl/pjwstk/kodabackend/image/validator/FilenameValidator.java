package pl.pjwstk.kodabackend.image.validator;

import org.springframework.stereotype.Component;

@Component
public class FilenameValidator {

    public boolean isValid(String filename) {
        return filename != null &&
                !filename.contains("..") &&
                !filename.contains("/") &&
                !filename.contains("\\");
    }
}