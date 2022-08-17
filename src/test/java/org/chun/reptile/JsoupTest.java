package org.chun.reptile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsoupTest {

  @Test
  @DisplayName("模擬爬蟲瀏覽器")
  void fakeBrowser() {
    final String MODEL_TITLE = "Desktop Memory Model";
    String host = "https://www.newegg.com/p/pl?";
    String requestUrl = this.getParameterMap().entrySet().stream()
        .map(entry -> entry.getKey().concat("=").concat(entry.getValue()).concat("&"))
        .map(host::concat)
        .findAny().get();

    // 1. 模擬開啟瀏覽器
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response;
    // 2. 模擬瀏覽器輸入網址
    HttpGet request = new HttpGet(requestUrl);
    // 3. 設定request header偽裝成瀏覽器
    request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
    this.setProxyHost(request, false);

    try {
      // 4. 執行 -> Enter
      response = httpClient.execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (HttpStatus.SC_OK != statusCode) {
        throw new RuntimeException(String.format("request error, http status:%d", statusCode));
      }

      // 5. 執行成功進行處理
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
//      System.out.println(html);
      System.out.println("===========================================================================");

      // 6. jsoup解析
      Document document = Jsoup.parse(html);
      Element target = document.getElementById("app")
//          .getElementsByClass("page-content").first()
//          .getElementsByClass("page-section").first()
//          .getElementsByClass("page-section-inner").first()
//          .getElementsByClass("has-side-left").first()
//          .getElementsByClass("row-body").first()
          .getElementsByClass("row-body-inner").first()
//          .getElementsByClass("row-body-border").first()
          .getElementsByClass("list-wrap").first();

      Element toolbar = target.getElementsByClass("list-tools-bar").first();
      if (toolbar == null) {
        System.out.println("分頁工具不存在");
      } else {
        // 頁數
        Element pageElement = toolbar.getElementsByClass("list-tool-pagination").first();
        String thisPage = pageElement.select("span").first()
            .select("strong").first()
            .text()
            .split("/")[1];

        System.out.println("現在頁數:" + thisPage);
      }


      Elements targetProdList =
//          target.getElementsByClass("item-cells-wrap border-cells four-cells items-grid-view").first()
          target.getElementsByClass("item-cells-wrap").first()
              .getElementsByClass("item-cell");

      System.out.printf("網頁標題:%s%n", document.getElementsByTag("title").first());

      for (Element prod : targetProdList) {
        System.out.println("---------------------------------------------------------------------------");
        Element item = prod.getElementsByClass("item-container").first();
        // 商品圖片
        Element itemImg = item.getElementsByClass("item-img").first();
        String imgUrl = itemImg.select("a").attr("href");
        // 商品內容
        Element itemInfo = item.getElementsByClass("item-info").first();
        String itemTitle = itemInfo.select("a").text();
        // Model(optional)
        String model = Strings.EMPTY;
        if (itemTitle.contains(MODEL_TITLE)) {
          model = itemTitle.split(MODEL_TITLE)[1];
        }
        // 商品價格
        Element itemAction = item.getElementsByClass("item-action").first();
        Element itemPrice = itemAction.getElementsByClass("price").first()
            .getElementsByClass("price-current").first();
        String price = itemPrice.select("strong").text().concat(itemPrice.select("sup").text());

//        System.out.printf("圖片路徑:%s%n", imgUrl);
        System.out.printf("商品標題:%s%n", itemTitle);
        System.out.printf("Model:%s%n", model);
        System.out.printf("商品價格:%s%n", price);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> getParameterMap() {
    Map<String, String> rtnMap = new HashMap<>();
    rtnMap.put("d", "corsair");
    rtnMap.put("PageSize", "96");

    return rtnMap;
  }

  private void setProxyHost(HttpGet request, boolean need) {
    if (!need) {
      return;
    }

    HttpHost proxy = new HttpHost("60.13.42.232", 9999);
    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
    request.setConfig(config);
  }
}
