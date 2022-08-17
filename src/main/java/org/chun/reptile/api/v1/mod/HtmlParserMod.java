package org.chun.reptile.api.v1.mod;

import org.chun.reptile.common.OutputResultRvo;
import org.chun.reptile.constant.NewEggClassNameConst;
import org.chun.reptile.enums.WebsiteEnum;
import org.chun.reptile.exception.HtmlParserFailException;
import org.chun.reptile.exception.ItemNotFoundException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HtmlParserMod {

  private static final String NO_ITEMS_FOUND = "We have found 0 items";

  /**
   * 取得商品列表區塊
   *
   * @param document
   * @param websiteEnum
   * @return
   */
  public Element getProdTarget(Document document, WebsiteEnum websiteEnum) {
    return WebsiteEnum.NEW_EGG == websiteEnum
        ? this.newEggProdTarget(document)
        : this.amazonProdTarget(document);
  }

  /**
   * 取得總頁數
   *
   * @param prodTarget
   * @return
   */
  public int getPage(Element prodTarget) {
    int page = 1;
    try {
      Element toolbar = prodTarget.getElementsByClass(NewEggClassNameConst.LIST_TOOLS_BAR).first();
      if (toolbar == null) {
        return page;
      }

      Element pageElement = toolbar.getElementsByClass(NewEggClassNameConst.LIST_TOOL_PAGINATION).first();
      String totalPage = pageElement.select("span").first()
          .select("strong").first()
          .text()
          .split("/")[1];
      page = Integer.parseInt(totalPage);
    } catch (Exception e) {
      System.out.println("沒有分頁工具");
    }
    return page;
  }

  /**
   * 解析網頁, 組裝回傳資料
   *
   * @param element
   * @return
   */
  public List<OutputResultRvo> getOutputList(Element element) {
    List<OutputResultRvo> outputResultRvoList = new ArrayList<>();
    try {

      Elements itemElements = Objects.requireNonNull(element.getElementsByClass(NewEggClassNameConst.ITEM_CELL_WRAP).first())
          .getElementsByClass(NewEggClassNameConst.ITEM_CELL);
      for (Element item : itemElements) {

        // 商品內容
        Element itemInfo = item.getElementsByClass(NewEggClassNameConst.ITEM_INFO).first();
        String itemTitle = itemInfo.select("a").text();

        // 商品價格
        Element itemPrice = item.getElementsByClass(NewEggClassNameConst.PRICE_CURRENT).first();
        String price = itemPrice.select("strong").text().concat(itemPrice.select("sup").text());

        // 組裝回傳物件
        outputResultRvoList.add(new OutputResultRvo(itemTitle, price));
      }

    } catch (NullPointerException npe) {
      System.out.println("沒有任何商品輸出");
    } catch (Exception e) {
      throw new HtmlParserFailException();
    }
    return outputResultRvoList;
  }

  /** =================================================== private ================================================== */

  /**
   * NewEgg商品列表區塊
   *
   * @param document
   * @return
   */
  private Element newEggProdTarget(Document document) {
    // 取得查詢結果區塊
    Element targetArea = Objects.requireNonNull(document.getElementById("app"))
        .getElementsByClass(NewEggClassNameConst.ROW_BODY_INNER).first();
    if (targetArea == null) {
      throw new RuntimeException();
    }

    try {
      return targetArea.getElementsByClass(NewEggClassNameConst.LIST_WRAP).first();
    } catch (Exception e) {
      String errorMsg = targetArea.getElementsByClass(NewEggClassNameConst.RESULT_MESSAGE_ERROR).first()
          .select("span")
          .text();
      throw errorMsg.contains(NO_ITEMS_FOUND) ? new ItemNotFoundException() : new RuntimeException();
    }
  }

  private Element amazonProdTarget(Document document) {
    return new Element("amazon");
  }
}
