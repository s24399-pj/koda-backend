package pl.pjwstk.kodabackend.image.validator;

import org.springframework.stereotype.Component;

/**
 * Validator for file names to prevent directory traversal attacks.
 * <p>
 * Checks for dangerous characters like "..", "/", "\" that could be used
 * to access files outside the intended directory.
 * </p>
 */
@Component
public class FilenameValidator {

    /**
     * Validates if filename is safe to use.
     * <p>
     * Rejects filenames that contain:
     * <ul>
     *   <li>"…" (parent directory references)</li>
     *   <li>"/" or "\" (path separators)</li>
     *   <li>null values</li>
     * </ul>
     *
     * @param filename the filename to validate
     * @return {@code true} if the filename is safe, {@code false} otherwise
     */
    public boolean isValid(String filename) {
        return filename != null &&
                !filename.contains("..") &&
                !filename.contains("/") &&
                !filename.contains("\\");
    }
}