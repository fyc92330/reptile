package org.chun.reptile.util;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.chun.reptile.common.OutputResultRvo;
import org.chun.reptile.constant.Const;
import org.chun.reptile.exception.FileGenerateFailException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileGenerateUtil {

  private static final String WINDOWS_OS = "Windows";
  private static final String DOWNLOADS_FOLDER_NAME = "Downloads";
  private static final char SEPARATOR = ',';
  private static final String[] FILE_HEADER_ARRAY = new String[]{"標題", "型號", "價格"};

  public static String genFilePath(String fileName) {
    String folderSymbol = System.getProperty("os.name").contains(WINDOWS_OS) ? Const.BACK_SLASH_STR : Const.SLASH_STR;
    return StringUtil.concat(System.getProperty("user.home"), folderSymbol, DOWNLOADS_FOLDER_NAME, folderSymbol, fileName);
  }

  /**
   * 轉換csv檔輸出
   *
   * @param outputResultRvoList
   * @param filePath
   */
  public static boolean convertCsvFile(List<OutputResultRvo> outputResultRvoList, String filePath) {
    try {
      List<String[]> fileContentList = null;

      // 檔案存在需要紀錄內容
      if (new File(filePath).exists()) {
        fileContentList = readCsvFile(filePath);
      }

      writeCsvFile(fileContentList, outputResultRvoList, filePath);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 讀取存在的csv檔
   *
   * @param filePath
   * @return
   */
  private static List<String[]> readCsvFile(String filePath) {
    List<String[]> fileContentList = new ArrayList<>();
    try (CsvReader reader = CsvReader.builder().fieldSeparator(SEPARATOR).build(new FileReader(filePath))) {
      Iterator<CsvRow> csvRowIterator = reader.stream().iterator();
      while (csvRowIterator.hasNext()) {
        fileContentList.add(csvRowIterator.next().getFields().toArray(new String[3]));
      }
    } catch (Exception e) {
      throw new FileGenerateFailException();
    }
    return fileContentList;
  }


  /**
   * 新建立csv檔
   *
   * @param fileContentList
   * @param outputResultRvoList
   * @param filePath
   */
  private static void writeCsvFile(List<String[]> fileContentList, List<OutputResultRvo> outputResultRvoList, String filePath) {
    try (CsvWriter writer = CsvWriter.builder().fieldSeparator(SEPARATOR).build(new FileWriter(filePath))) {
      // 檔案存在覆寫, 不存在則新建
      if (CollectionUtils.isEmpty(fileContentList)) {
        writer.writeRow(FILE_HEADER_ARRAY);
      } else {
        fileContentList.forEach(writer::writeRow);
      }

      // 寫入data
      outputResultRvoList.forEach(rvo -> writer.writeRow(rvo.getTitle(), rvo.getModel(), rvo.getPrice()));
    } catch (Exception e) {
      throw new FileGenerateFailException();
    }
  }
}
