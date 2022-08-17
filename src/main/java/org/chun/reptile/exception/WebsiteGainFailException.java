package org.chun.reptile.exception;

public class WebsiteGainFailException extends RuntimeException {

  public WebsiteGainFailException() {
    super("網頁資料獲取失敗.");
  }

  public WebsiteGainFailException(int statusCode) {
    super(String.format("網頁資料獲取失敗. 狀態碼:%d", statusCode));
  }
}
