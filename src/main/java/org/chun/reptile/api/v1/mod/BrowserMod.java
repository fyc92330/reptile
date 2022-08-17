package org.chun.reptile.api.v1.mod;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.reptile.common.NewEggParam;
import org.chun.reptile.enums.WebsiteEnum;
import org.chun.reptile.exception.WebsiteGainFailException;
import org.chun.reptile.util.JsonBean;
import org.chun.reptile.util.StringUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrowserMod {

  /**
   * 建立模擬瀏覽器
   *
   * @return
   */
  public CloseableHttpClient defaultBrowser() {
    return HttpClients.createDefault();
  }

  /**
   * 執行操作
   *
   * @param keyWord
   * @param websiteEnum
   * @param httpClient
   * @return
   */
  public String executeAction(String keyWord, WebsiteEnum websiteEnum, CloseableHttpClient httpClient, int searchPage) {
    HttpGet request = new HttpGet(this.genRequestUrl(keyWord, websiteEnum, searchPage));
    // 設定request header偽裝成瀏覽器
    request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

    try (CloseableHttpResponse response = httpClient.execute(request)) {
      final int statusCode = response.getStatusLine().getStatusCode();
      if (HttpStatus.SC_OK != statusCode) {
        throw new WebsiteGainFailException(statusCode);
      }
      return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

    } catch (WebsiteGainFailException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new WebsiteGainFailException();
    }
    return Strings.EMPTY;
  }

  /**
   * 建立請求url
   *
   * @param keyWord
   * @param websiteEnum
   * @return
   */
  private String genRequestUrl(String keyWord, WebsiteEnum websiteEnum, int searchPage) {
    String url;
    if (WebsiteEnum.NEW_EGG == websiteEnum) {
      NewEggParam neweggParam = new NewEggParam(keyWord, searchPage);
      Map<String, Object> map = JsonBean.Extra.objectMapper().convertValue(neweggParam, Map.class);
      String requestParam = map.entrySet().stream()
          .map(entry -> StringUtil.concat(entry.getKey(), "=", entry.getValue()))
          .collect(Collectors.joining("&"));
      url = StringUtil.concat(websiteEnum.getUrl(), "?", requestParam);
    } else {
      throw new RuntimeException("not invalid.");
    }
    return url;
  }
}
