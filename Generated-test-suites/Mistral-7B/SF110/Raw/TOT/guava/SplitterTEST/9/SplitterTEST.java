package com.google.common.base;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SplitterTest {

 // Test for the static factory method 'on(char)'
 @Test
 public void testOnChar() {
 Splitter splitter = Splitter.on('-');
 String text = "key1-value1-key2-value2";
 List<String> result = splitter.splitToList(text);
 List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
 assertEquals(expected, result);
 }

 // Test for the static factory method 'on(CharMatcher)' with a simple character class
 @Test
 public void testOnCharMatcherSimple() {
 CharMatcher separatorMatcher = CharMatcher.is(':');
 Splitter splitter = Splitter.on(separatorMatcher);
 String text = "key1:value1:key2:value2";
 List<String> result = splitter.splitToList(text);
 List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
 assertEquals(expected, result);
 }

 // Test for the static factory method 'on(CharMatcher)' with a complex character class
 @Test
 public void testOnCharMatcherComplex() {
 CharMatcher separatorMatcher = CharMatcher.or(CharMatcher.is(':'), CharMatcher.is('-'));
 Splitter splitter = Splitter.on(separatorMatcher);
 String text = "key1:value1-key2:value2";
 List<String> result = splitter.splitToList(text);
 List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
 assertEquals(expected, result);
 }

 // Test for the static factory method 'onPattern(String)'
 @Test
 public void testOnPattern() {
 Pattern separatorPattern = Pattern.compile(":|-");
 Splitter splitter = Splitter.onPattern(separatorPattern.pattern());
 String text = "key1:value1-key2:value2";
 List<String> result = splitter.splitToList(text);
 List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
 assertEquals(expected, result);
 }

 // Test for the static factory method 'on(String)'
 @Test
 public void testOnString() {
 Splitter splitter = Splitter.on(",");
 String text = "item1,item2,item3";
 List<String> result = splitter.splitToList(text);
 List<String> expected = Arrays.asList("item1", "item2", "item3");
 assertEquals(expected, result);
 }

 // Test for the method 'omitEmptyStrings()'
 @Test
 public void testOmitEmptyStrings() {
 Splitter splitter = Splitter.on(",");
 List<String> result = splitter.splitToList("item1, ,item3");
 List<String> expected = Arrays.asList("item1", "item3");
 assertEquals(expected, result);
 }

 // Test for the method 'limit(int)'
 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on(",");
 List<String> result = splitter.splitToList("item1,item2,item3,item4,item5", 3);
 List<String> expected = Arrays.asList("item1", "item2", "item3");
 assertEquals(expected, result);
 }

 // Test for the method 'trimResults()'
 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on(",").trimResults();
 List<String> result = splitter.splitToList(" item1, item2,  item3 ");
 List<String> expected = Arrays.asList("item1", "item2", "item3");
 assertEquals(expected, result);
 }

 // Test for the method 'trimResults(CharMatcher)'
 @Test
 public void testTrimResultsCharMatcher() {
 Splitter splitter = Splitter.on(",").trimResults(CharMatcher.WHITESPACE);
 List<String> result = splitter.splitToList(" item1, item2,  item3 ");
 List<String> expected = Arrays.asList("item1", "item2", "item3");
 assertEquals(expected, result);
 }

 // Test for the method 'withKeyValueSeparator(String)'
 @Test
 public void testWithKeyValueSeparator() {
 Splitter splitter = Splitter.on("=").trimResults().withKeyValueSeparator(":");
 Map<String, String> result = splitter.new MapSplitter().split(
 "key1=value1:key2=value2:key3=value3"
 );
 Map<String, String> expected = new LinkedHashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 expected.put("key3", "value3");
 assertEquals(expected, result);
 }

 // Test for the method 'withKeyValueSeparator(char)'
 @Test
 public void testWithKeyValueSeparatorChar() {
 Splitter splitter = Splitter.on("=").trimResults().withKeyValueSeparator(':');
 Map<String, String> result = splitter.new MapSplitter().split(
 "key1=value1:key2=value2:key3=value3"
 );
 Map<String, String> expected = new LinkedHashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 expected.put("key3", "value3");
 assertEquals(expected, result);
 }

 // Test for the method 'withKeyValueSeparator(Splitter)'
 @Test
 public void testWithKeyValueSeparatorSplitter() {
 Splitter keyValueSplitter = Splitter.on("=");
 Map<String, String> result = new Splitter(
 Splitter.on(","), keyValueSplitter
 ).new MapSplitter().split(
 "item1=value1,item2=value2,item3=value3"
 );
 Map<String, String> expected = new LinkedHashMap<>();
 expected.put("item1", "value1");
 expected.put("item2", "value2");
 expected.put("item3", "value3");
 assertEquals(expected, result);
 }
}