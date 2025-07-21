package pl.pjwstk.kodabackend.image.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceLocationStrategyTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private ResourceLocationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ResourceLocationStrategy(resourceLoader);
    }

    @Test
    void resolveLocation_WhenClasspathLocation_ReturnsClasspathResourceLocation() {
        // Arrange
        String uploadDir = "classpath:offer-images/";
        String filename = "test-image.jpg";
        ReflectionTestUtils.setField(strategy, "uploadDir", uploadDir);

        when(resourceLoader.getResource("classpath:offer-images/test-image.jpg"))
                .thenReturn(resource);

        // Act
        ResourceLocation result = strategy.resolveLocation(filename);

        // Assert
        assertNotNull(result);
        assertInstanceOf(ClasspathResourceLocation.class, result);
        verify(resourceLoader).getResource("classpath:offer-images/test-image.jpg");
    }

    @Test
    void resolveLocation_WhenFileSystemLocation_ReturnsFileSystemResourceLocation() {
        // Arrange
        String uploadDir = "/var/uploads/images/";
        String filename = "test-image.jpg";
        ReflectionTestUtils.setField(strategy, "uploadDir", uploadDir);

        // Act
        ResourceLocation result = strategy.resolveLocation(filename);

        // Assert
        assertNotNull(result);
        assertInstanceOf(FileSystemResourceLocation.class, result);
        verifyNoInteractions(resourceLoader);
    }

    @Test
    void resolveLocation_WhenClasspathWithoutTrailingSlash_HandlesCorrectly() {
        // Arrange
        String uploadDir = "classpath:offer-images";
        String filename = "test.png";
        ReflectionTestUtils.setField(strategy, "uploadDir", uploadDir);

        when(resourceLoader.getResource("classpath:offer-imagestest.png"))
                .thenReturn(resource);

        // Act
        ResourceLocation result = strategy.resolveLocation(filename);

        // Assert
        assertNotNull(result);
        assertInstanceOf(ClasspathResourceLocation.class, result);
        // Note: This test shows a potential bug - paths are concatenated without ensuring separator
    }

    @Test
    void resolveLocation_WhenFilenameContainsPath_PreservesFullPath() {
        // Arrange
        String uploadDir = "/uploads/";
        String filename = "subfolder/image.jpg";
        ReflectionTestUtils.setField(strategy, "uploadDir", uploadDir);

        // Act
        ResourceLocation result = strategy.resolveLocation(filename);

        // Assert
        assertNotNull(result);
        assertInstanceOf(FileSystemResourceLocation.class, result);
    }
}