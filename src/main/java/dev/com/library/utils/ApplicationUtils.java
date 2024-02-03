package dev.com.library.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationUtils {
  private Integer serverPort;

  public static int numberOfAvailableCores() {
//    return Runtime.getRuntime().availableProcessors() / 2;
    return 1;
  }

}
