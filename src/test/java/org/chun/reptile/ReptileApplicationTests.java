package org.chun.reptile;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

class ReptileApplicationTests {

  private static final String PROPERTY_OS_NAME = "os.name";
  private static final String MAC_SYSTEM_PREFIX = "Mac OS";
  private static final String WINDOWS_SYSTEM_PREFIX = "Windows";

  @Test
  @DisplayName("辨識系統路徑")
  @SneakyThrows
  void systemIdentify() {
    String osName = System.getProperty(PROPERTY_OS_NAME);
    System.out.println(osName);
    if(osName.startsWith(MAC_SYSTEM_PREFIX)){
      String menuPath = String.format("%s/Downloads", System.getProperty("user.home"));
      File menu = new File(menuPath);
      if(menu.exists()){
        System.out.println("下載目錄存在");
        String filePath = String.format("%s/SystemOsTest.csv", menuPath);
        File file = new File(filePath);
        if(file.exists()){
          System.out.println("文件已存在");
        }else if(file.createNewFile()){
          System.out.println("文件建立成功");
        }else {
          System.out.println("建立失敗");
        }
      }else{
        System.out.println("目錄不存在");
      }
    }
  }

  @Test
  @DisplayName("系統屬性")
  void systemProperty(){
    Properties properties = System.getProperties();
    System.out.println("============== 系統屬性名稱 ==============");
    properties.stringPropertyNames().stream().sorted().forEach(System.out::println);
    System.out.println("============== 系統屬性內容 ==============");
    System.out.println("toolkit (awt.toolkit): " + properties.getProperty("awt.toolkit"));
    System.out.println("encode (file.encoding): " + properties.getProperty("file.encoding"));
    System.out.println("separator (file.separator): " + properties.getProperty("file.separator"));
    System.out.println("classPath (java.class.path): " + properties.getProperty("java.class.path"));
    System.out.println("classVersion (java.class.version): " + properties.getProperty("java.class.version"));
    System.out.println("extDirs (java.ext.dirs): " + properties.getProperty("java.ext.dirs"));
    System.out.println("osName (os.name): " + properties.getProperty("os.name"));
    System.out.println("osVersion (os.version): " + properties.getProperty("os.version"));
    System.out.println("userCountry (user.country): " + properties.getProperty("user.country"));
    System.out.println("userDir (user.dir): " + properties.getProperty("user.dir"));
    System.out.println("userHome (user.home): " + properties.getProperty("user.home"));
    System.out.println("userName (user.name): " + properties.getProperty("user.name"));
  }

}
