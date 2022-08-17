package org.chun.reptile.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.chun.reptile.constant.Const;
import org.chun.reptile.enums.WebsiteEnum;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OutputResultRvo {

  private static final String DESKTOP_MEMORY_MODEL = "Desktop Memory Model";
  /** 產品標題 */
  private String title;
  /** 產品型號 */
  private String model;
  /** 產品價格 */
  private String price;
  /** 產品來源 */
  private String root;

  public OutputResultRvo(String title, String price) {
    this.title = title;
    this.price = price;
    this.root = WebsiteEnum.NEW_EGG.name();
    this.model = title.contains(DESKTOP_MEMORY_MODEL)
        ? title.split(DESKTOP_MEMORY_MODEL)[1].split(Const.BLANK_STR)[0] : null;
  }

}
