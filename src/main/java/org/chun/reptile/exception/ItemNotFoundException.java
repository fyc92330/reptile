package org.chun.reptile.exception;

public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException(){
    super("沒有查詢結果");
  }
}
