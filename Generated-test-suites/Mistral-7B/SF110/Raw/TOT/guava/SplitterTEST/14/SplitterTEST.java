package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

  @Test
  public void testSimpleCharSeparator() {
    Splitter splitter = Splitter.on('-');
    List<String> result = splitter.split("test-one-two-three");
    assertEquals(Arrays.asList("test", "one", "two", "three"), result);
  }

  @Test
  public void testEmptyStringSeparator() {
    Splitter splitter = Splitter.on(" ");
    List<String> result = splitter.split("test one two three");
    assertEquals(Arrays.asList("test", "", "one", "", "two", "", "three"), result);
  }

  @Test
  public void testEmptyInput() {
    Splitter splitter = Splitter.on('-');
    List<String> result = splitter.split("");
    assertEquals(Collections.emptyList(), result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    Splitter splitter = Splitter.on('-');
    splitter.split(null);
  }

  @Test
  public void testRegexSeparator() {
    Pattern pattern = Pattern.compile("\\s+");
    Splitter splitter = Splitter.onPattern(pattern);
    List<String> result = splitter.split("test one two three");
    assertEquals(Arrays.asList("test", "", "one", "", "two", "", "three"), result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullRegexSeparator() {
    Splitter.onPattern(null);
  }

  @Test
  public void testFixedLengthSeparator() {
    Splitter splitter = Splitter.fixedLength(3);
    List<String> result = splitter.split("test123test456test789");
    assertEquals(Arrays.asList("test12", "test45", "test78"), result);
  }

  @Test
  public void testEmptyStringsOmitted() {
    Splitter splitter = Splitter.on('-').omitEmptyStrings();
    List<String> result = splitter.split("test-one--two-three");
    assertEquals(Arrays.asList("one", "two", "three"), result);
  }

  @Test
  public void testLimit() {
    Splitter splitter = Splitter.on('-').limit(2);
    List<String> result = splitter.split("test-one--two-three-four-five");
    assertEquals(Arrays.asList("test-one--two", "three-four-five"), result);
  }

  @Test
  public void testMapSplitter() {
    Splitter keySplitter = Splitter.on(':');
    Splitter valueSplitter = Splitter.on(' ');
    MapSplitter mapSplitter = Splitter.withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
    List<Map.Entry<String, String>> result = mapSplitter.split("test:one two:three four:five six:seven").entrySet();
    assertEquals(Arrays.asList(Map.entry("test", "one"), Map.entry("two", "three"), Map.entry("four", "five"), Map.entry("six", "seven")), result);
  }

  @Test
  public void testEmptyMapEntry() {
    Splitter keySplitter = Splitter.on(':');
    Splitter valueSplitter = Splitter.on(' ');
    MapSplitter mapSplitter = Splitter.withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
    List<Map.Entry<String, String>> result = mapSplitter.split("test:one :two:three :four:five six:seven :");
    assertEquals(Arrays.asList(Map.entry("test", "one"), Map.entry("", "two"), Map.entry("three", ""), Map.entry("four", "five"), Map.entry("six", "seven")), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidKeyValueSeparator() {
    Splitter keySplitter = Splitter.on(':');
    Splitter valueSplitter = Splitter.on(':');
    MapSplitter mapSplitter = Splitter.withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
    mapSplitter.split("test:one:two:three:four:five six:seven :");
  }
}