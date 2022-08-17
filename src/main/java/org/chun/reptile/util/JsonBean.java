package org.chun.reptile.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonBean {
  private final static ObjectMapper objectMapper = new ObjectMapper();
  private final static ObjectMapper extraObjectMapper = new ObjectMapper()
      .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
      .enable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL); // 內容值為null時，則該屬性不進行輸出

  private JsonBean() {
  }

  public static ObjectMapper objectMapper() {
    return objectMapper.copy();
  }

  public static class Extra {
    public static ObjectMapper objectMapper() {
      return extraObjectMapper.copy();
    }
  }
}
