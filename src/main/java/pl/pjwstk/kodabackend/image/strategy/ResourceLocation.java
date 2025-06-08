package pl.pjwstk.kodabackend.image.strategy;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Strategy interface for different resource location implementations.
 * <p>
 * This interface provides a common abstraction for accessing resources
 * from different locations such as file system, classpath, or remote sources.
 * It follows the Strategy design pattern to allow flexible resource handling.
 * </p>
 */
public interface ResourceLocation {

    /**
     * Checks whether the resource exists at the specified location.
     * <p>
     * This method should perform a lightweight check to determine if the
     * resource is available without actually loading it.
     * </p>
     *
     * @return {@code true} if the resource exists, {@code false} otherwise
     */
    boolean exists();

    /**
     * Retrieves the Spring Resource object for accessing the actual resource.
     * <p>
     * This method returns a Spring Resource that can be used to read the
     * resource content, get input streams, or perform other resource operations.
     * </p>
     *
     * @return the Spring Resource object representing this resource location
     * @throws IllegalStateException if the resource cannot be accessed
     */
    Resource resource();

    /**
     * Determines the MIME content type of the resource.
     * <p>
     * This method attempts to detect the content type based on the resource's
     * file extension or content. Common return values include:
     * </p>
     * <ul>
     *   <li>{@code "image/jpeg"} for JPEG images</li>
     *   <li>{@code "image/png"} for PNG images</li>
     *   <li>{@code "application/octet-stream"} for unknown types</li>
     * </ul>
     *
     * @return the MIME content type as a string, or {@code null} if it cannot be determined
     * @throws IOException if an I/O error occurs while determining the content type
     */
    String getContentType() throws IOException;
}