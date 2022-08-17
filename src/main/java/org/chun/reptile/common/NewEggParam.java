package org.chun.reptile.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewEggParam {

  /** 關鍵字 多個關鍵字用+連接 */
  private String d;
  /** 一頁顯示多少筆資料 */
  private Integer PageSize = 96;
  /** 頁數 */
  private Integer page;

  public NewEggParam(String d, int page){
    this.d = d;
    this.page = page;
  }
}
