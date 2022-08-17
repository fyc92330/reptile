package org.chun.reptile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.model.event.CallbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.chun.reptile.api.v1.CommandLineService;
import org.chun.reptile.util.JsonBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SpringBootApplication
public class ReptileApplication implements CommandLineRunner {

  private final CommandLineService commandLineService;

  public static void main(String[] args) {
    SpringApplication.run(ReptileApplication.class, args);
  }

  @Override
  public void run(String... args) {
    // version 1
    commandLineService.main();
  }

  /**
   * LINE訊息入口
   *
   * @param request
   * @param signature
   * @throws JsonProcessingException
   */
  @PostMapping("/callback")
  public String lineCallBack(@RequestBody CallbackRequest request, @RequestHeader(name = "x-line-signature") String signature) {
    this.printJsonFormat(signature, "signature");
    this.printJsonFormat(request, "request");
    return "index";
  }

  /**
   * 印出Json格式資料
   *
   * @param param
   * @param name
   */
  @SneakyThrows
  private void printJsonFormat(Object param, String name) {
    log.info("{}: {}", name, JsonBean.Extra.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(param));
  }
}
