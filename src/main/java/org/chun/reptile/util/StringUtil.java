package org.chun.reptile.util;

public class StringUtil {
  /**
   * @param str
   * @return
   */
  public static String concat(Object... str) {
    return concatWithDel("", str);
  }

  /**
   * @param delimeter
   * @param str
   * @return
   */
  public static String concatWithDel(String delimeter, Object... str) {
    StringBuilder sb = new StringBuilder(delimeter);
    for (Object s : str) {
      sb.append(s);
      sb.append(delimeter);
    }
    return sb.toString();
  }
}
