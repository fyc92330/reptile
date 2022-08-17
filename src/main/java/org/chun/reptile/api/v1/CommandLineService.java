package org.chun.reptile.api.v1;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.util.Strings;
import org.chun.reptile.api.v1.mod.BrowserMod;
import org.chun.reptile.api.v1.mod.HtmlParserMod;
import org.chun.reptile.common.OutputResultRvo;
import org.chun.reptile.constant.Const;
import org.chun.reptile.enums.WebsiteEnum;
import org.chun.reptile.exception.FileGenerateFailException;
import org.chun.reptile.util.FileGenerateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
@Service
public class CommandLineService {

  private final BrowserMod browserMod;
  private final HtmlParserMod htmlParserMod;
  private final ConfigurableApplicationContext context;

  public void main() {
    // 辨識參數
    WebsiteEnum websiteEnum = WebsiteEnum.NEW_EGG;
    try {
      // 模擬瀏覽器
      CloseableHttpClient httpClient = browserMod.defaultBrowser();
      // 查詢邏輯
      this.reptile(websiteEnum, httpClient);
    } catch (Exception e) {
      System.out.printf("%n網站(%s)查詢失敗%n", websiteEnum.name());
    } finally {
      System.out.println("已結束程式.");
      SpringApplication.exit(context);
    }
  }

  /** =================================================== private ================================================== */

  /**
   * 爬蟲邏輯
   *
   * @param websiteEnum
   * @param httpClient
   */
  private void reptile(WebsiteEnum websiteEnum, CloseableHttpClient httpClient) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.print("請輸入關鍵字: ");
      String keyword = this.multiKeyword(scanner.nextLine());
      System.out.printf("查詢關鍵字為:%s%n", keyword);
      String fileName = String.format("%s_%d.csv", keyword, Instant.now().toEpochMilli());
      int page = 1;

      for (int time = 1; time <= page; time++) {
        // 爬蟲
        String html = browserMod.executeAction(keyword, websiteEnum, httpClient, time);

        // 網頁解析
        if (!Strings.EMPTY.equals(html)) {
          Document document = Jsoup.parse(html);
          Element prodTarget = htmlParserMod.getProdTarget(document, websiteEnum);

          // first loop取得頁數
          if (page == 1) {
            page = this.suitablePage(scanner, htmlParserMod.getPage(prodTarget));
          }

          // 取得商品列表
          System.out.printf("取得第%d頁資料...", time);
          List<OutputResultRvo> outputResultRvoList = htmlParserMod.getOutputList(prodTarget);

          // 轉檔輸出csv, 檔案路徑恆定
          if (CollectionUtils.isNotEmpty(outputResultRvoList)){
            final String filePath = FileGenerateUtil.genFilePath(fileName);
            if (!FileGenerateUtil.convertCsvFile(outputResultRvoList, filePath)) {
              throw new FileGenerateFailException();
            }
          }
        }
      }

      // 重新查詢
      System.out.print("檔案生成完畢, 是否重啟程式(Y/N)? ");
      String check = this.check(scanner);
      if (Const.Y_STR.equals(check)) {
        this.reptile(websiteEnum, httpClient);
      } else if (Const.N_STR.equals(check)) {
        return;
      } else {
        throw new RuntimeException("程式代碼有誤");
      }
    }
  }

  /**
   * 轉換多關鍵字
   *
   * @param inputKeyword
   * @return
   */
  private String multiKeyword(String inputKeyword) {
    return inputKeyword.replaceAll(Const.ONE_BLANK_STR, Const.PLUS_STR);
  }

  /**
   * 取得適合的頁數
   *
   * @param scanner
   * @param totalPage
   * @return
   */
  private int suitablePage(Scanner scanner, int totalPage) {
    if (totalPage == 1) {
      return totalPage;
    }

    System.out.printf("查詢結果共有%d頁, 需要完全取得資料嗎(Y/N): ", totalPage);
    String getData = this.check(scanner);
    if (Const.Y_STR.equals(getData)) {
      return totalPage;
    }

    int page;
    boolean flag = true;
    System.out.print("請輸入取得的頁數: ");
    do {
      page = scanner.nextInt();
      if (page > totalPage) {
        System.out.printf("取得頁數不得超過最大頁數(%d), 請重新輸入取得頁數: ", totalPage);
      } else {
        flag = false;
      }
    } while (flag);
    return page;
  }

  /**
   * 檢核輸入是否為Y/N
   *
   * @param scanner
   * @return
   */
  private String check(Scanner scanner) {
    String check = Optional.ofNullable(scanner.next())
        .map(String::toUpperCase)
        .filter(s -> Const.Y_STR.equals(s) || Const.N_STR.equals(s))
        .orElse(Strings.EMPTY);
    if (Strings.EMPTY.equals(check)) {
      System.out.print("輸入錯誤, 請重新輸入(Y/N): ");
      check = this.check(scanner);
    }
    return check;
  }
}
