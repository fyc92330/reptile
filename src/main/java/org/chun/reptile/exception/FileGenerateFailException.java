package org.chun.reptile.exception;

public class FileGenerateFailException extends RuntimeException {

  public FileGenerateFailException() {
    super("檔案轉換失敗");
  }
}
