package org.chun.reptile.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chun.reptile.common.NewEggParam;

@Getter
@AllArgsConstructor
public enum WebsiteEnum {

  NEW_EGG("https://www.newegg.com/p/pl", NewEggParam.class);
//  AMAZON("", AmazonParam.class);

  private final String url;
  private final Class<?> clazz;

}
