package org.chun.reptile.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
  public static <K, V> Map<K, V> newHashMap(K key1, V value1 ) {
    Map map = new HashMap();
    map.put( key1, value1 );
    return map;
  }

  public static <K, V> Map<K, V> newHashMap( K key1, V value1, K key2, V value2 ) {
    Map map = new HashMap();
    map.put( key1, value1 );
    map.put( key2, value2 );
    return map;
  }

  public static <K, V> Map<K, V> newHashMap( K key1, V value1, K key2, V value2, K key3, V value3 ) {
    Map map = new HashMap();
    map.put( key1, value1 );
    map.put( key2, value2 );
    map.put( key3, value3 );
    return map;
  }

  public static <K, V> Map<K, V> newHashMap( K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4 ) {
    Map map = new HashMap();
    map.put( key1, value1 );
    map.put( key2, value2 );
    map.put( key3, value3 );
    map.put( key4, value4 );
    return map;
  }

  public static <K, V> Map<K, V> newHashMap( K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5 ) {
    Map map = new HashMap();
    map.put( key1, value1 );
    map.put( key2, value2 );
    map.put( key3, value3 );
    map.put( key4, value4 );
    map.put( key5, value5 );
    return map;
  }
}
