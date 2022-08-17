package org.chun.reptile.exception;

public class HtmlParserFailException extends RuntimeException {

  public HtmlParserFailException() {
    super("網頁解析失敗");
  }

  public HtmlParserFailException(String className) {
    super(String.format("網頁解析失敗. className:%s", className));
  }
}
