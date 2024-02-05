package dev.com.library.utils;

public enum LogUtils {
  RUN_HTTP_SERVER_SUCCESS_MESSAGE("HTTP server running on port %s"),
  RUN_HTTP_SERVER_ERROR_MESSAGE("Cannot run HTTP server"),
  ERROR_NOT_FOUND_DOCUMENT("Document with _id %s not found"),
  RUN_APP_SUCCESSFULLY_MESSAGE("Library-api started successfully in %d ms");
  private final String message;

  LogUtils(final String message) {
    this.message = message;
  }

  public String buildMessage(Object... argument) {
    return String.format(message, argument);
  }

}
