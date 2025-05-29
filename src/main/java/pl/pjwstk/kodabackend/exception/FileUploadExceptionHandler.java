package pl.pjwstk.kodabackend.exception;

public class FileUploadExceptionHandler extends RuntimeException {
  public FileUploadExceptionHandler(String message) {
    super(message);
  }
}
