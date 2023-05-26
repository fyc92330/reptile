package org.chun.reptile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.chun.reptile.service.BrowserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class ReptileApplication implements CommandLineRunner {

  private final BrowserService browserService;

  public static void main(String[] args) {
    SpringApplication.run(ReptileApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    CloseableHttpClient httpClient = browserService.defaultBrowser();
    this.reptile(httpClient);
  }

  private void reptile(CloseableHttpClient httpClient){
    //todo keyword
    //todo action execute
    //todo jsoup
    //todo collect list
    //todo convert .csv
  }
}
