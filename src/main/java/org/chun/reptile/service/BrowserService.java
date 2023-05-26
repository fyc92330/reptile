package org.chun.reptile.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class BrowserService {

  public CloseableHttpClient defaultBrowser() {
    return HttpClients.createDefault();
  }

  public String execute(String keyword, CloseableHttpClient httpClient){
    HttpGet request = new HttpGet(this.requestUrl(keyword, 0));
    // 設定request header偽裝成瀏覽器
    request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

    try(CloseableHttpResponse response = httpClient.execute(request)){
      //todo code!=200
      return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }catch (Exception e){
      e.printStackTrace();
    }

    return Strings.EMPTY;
  }


  private String requestUrl(String keyword, int page){
    String url;
    url = "";//todo url
    return url;
  }
}
