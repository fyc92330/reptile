package org.chun.reptile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.chun.reptile.constant.Const;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("檔案生成測試")
public class FileWriterTest {

  private static final String TEST_FILE_NAME = "testOutput";
  private static final String TEST_FILE_PATH = String.format("./%s.csv", TEST_FILE_NAME);
  private static final String[] CSV_HEADER = new String[]{"num", "title", "price"};
  private static final String[] CSV_CONTAIN = new String[]{"1", "麥香紅茶", "10"};
  private static final String[] CSV_OVERRIDE = new String[]{"2", "大麥綠茶", "15"};
  private static final String[] CSV_OVERRIDE_2 = new String[]{"3", "阿波羅奶茶", "18"};

  @Test
  @DisplayName("fastCsv讀取並覆寫檔案")
  void fastCsvOverride() {
    try {
      CsvReader reader = CsvReader.builder()
          .fieldSeparator(',')
          .build(new FileReader(TEST_FILE_PATH));
      List<String[]> fileContainArray = new ArrayList<>();
      Iterator<CsvRow> csvRowIterator = reader.stream().iterator();
      while (csvRowIterator.hasNext()) {
        CsvRow csvRow = csvRowIterator.next();
        fileContainArray.add(csvRow.getFields().toArray(new String[3]));
      }

      CsvWriter writer = CsvWriter.builder()
          .fieldSeparator(',')
          .build(new FileWriter(TEST_FILE_PATH));
      fileContainArray.forEach(writer::writeRow);
      writer.writeRow(CSV_OVERRIDE);

      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("fastCsv生成檔案")
  void fastCsvTest() {
    try {
      CsvWriter writer = CsvWriter.builder()
          .fieldSeparator(',')
          .build(new FileWriter(TEST_FILE_PATH));
      writer.writeRow(CSV_HEADER);
      writer.writeRow(CSV_CONTAIN);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.isFileGenerateSuccess();
    }
  }

  @Test
  @DisplayName("OpenCsv讀取並覆寫檔案")
  void openCsvOverride() {
    try {
      File file = new File(TEST_FILE_PATH);
      CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file)));
      String[] nextLine;
      List<String[]> fileContain = new ArrayList<>();
      while ((nextLine = reader.readNext()) != null) {
        System.out.println(String.join(Const.COMMA_STR, nextLine));
        fileContain.add(nextLine);
      }

      CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file)));
      writer.writeAll(fileContain);
      Stream.of(CSV_OVERRIDE, CSV_OVERRIDE_2).forEach(writer::writeNext);
      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("OpenCsv生成檔案")
  void openCsvTest() {
    try {
      ICSVWriter writer = new CSVWriterBuilder(new FileWriter(TEST_FILE_PATH))
          .withSeparator(Const.COMMA_STR.toCharArray()[0])
          .build();
      writer.writeNext(CSV_HEADER);
      writer.writeNext(CSV_CONTAIN);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.isFileGenerateSuccess();
    }
  }

  @Test
  @DisplayName("測試檔案生成")
  void genFile() {
    File file = new File(TEST_FILE_PATH);
    try {
      if (file.createNewFile()) {
        System.out.println("測試檔案生成成功");
      } else {
        throw new RuntimeException("測試檔案生成失敗");
      }

      this.isFileGenerateSuccess();
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write("This is test file.");
      fileWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("測試檔案移除")
  void removeFile() {
    File file = new File(TEST_FILE_PATH);
    if (file.exists()) {
      System.out.println("測試檔案存在, 開始移除...");
      if (file.isFile()) {
        System.out.println(file.delete()
            ? "檔案移除成功."
            : "檔案移除失敗."
        );
      } else {
        System.out.println("檔案格式不正確.");
      }
    } else {
      System.out.println("檔案不存在.");
    }
  }

  @Test
  void filenameTest() {
    System.out.println(Instant.now().toEpochMilli());
  }

  /**
   * 檢核檔案存在
   */
  private void isFileGenerateSuccess() {
    File file = new File(TEST_FILE_PATH);
    System.out.println(file.exists()
        ? "檔案生成成功"
        : "檔案生成失敗"
    );
  }

}
