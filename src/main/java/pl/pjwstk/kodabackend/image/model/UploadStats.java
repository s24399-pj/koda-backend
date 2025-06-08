package pl.pjwstk.kodabackend.image.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statistics about uploaded files and images in the system")
public record UploadStats(
        @Schema(description = "Total number of files stored on disk", example = "250")
        int totalFiles,

        @Schema(description = "Total size of all files in bytes", example = "52428800")
        long totalSizeBytes,

        @Schema(description = "Total number of image records in database", example = "180")
        long totalImagesInDatabase,

        @Schema(description = "Path to the upload directory", example = "/app/uploads")
        String uploadDirectory
) {
    public static UploadStatsBuilder builder() {
        return new UploadStatsBuilder();
    }

    @Schema(description = "Human-readable formatted file size")
    public String getTotalSizeFormatted() {
        if (totalSizeBytes < 1024) return totalSizeBytes + " B";
        if (totalSizeBytes < 1024 * 1024) return String.format("%.1f KB", totalSizeBytes / 1024.0);
        if (totalSizeBytes < 1024 * 1024 * 1024)
            return String.format("%.1f MB", totalSizeBytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", totalSizeBytes / (1024.0 * 1024.0 * 1024.0));
    }

    public static class UploadStatsBuilder {
        private int totalFiles;
        private long totalSizeBytes;
        private long totalImagesInDatabase;
        private String uploadDirectory;

        public UploadStatsBuilder totalFiles(int totalFiles) {
            this.totalFiles = totalFiles;
            return this;
        }

        public UploadStatsBuilder totalSizeBytes(long totalSizeBytes) {
            this.totalSizeBytes = totalSizeBytes;
            return this;
        }

        public UploadStatsBuilder totalImagesInDatabase(long totalImagesInDatabase) {
            this.totalImagesInDatabase = totalImagesInDatabase;
            return this;
        }

        public UploadStatsBuilder uploadDirectory(String uploadDirectory) {
            this.uploadDirectory = uploadDirectory;
            return this;
        }

        public UploadStats build() {
            return new UploadStats(totalFiles, totalSizeBytes, totalImagesInDatabase, uploadDirectory);
        }
    }
}