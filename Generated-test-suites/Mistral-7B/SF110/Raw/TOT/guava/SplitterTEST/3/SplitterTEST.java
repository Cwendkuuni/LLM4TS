package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 CharSequence testString = "hello-world-hello";
 Splitter splitter = Splitter.on('-');
 List<String> expectedList = Arrays.asList("hello", "world", "hello");
 List<String> actualList = splitter.split(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testOnStringSeparator() {
 CharSequence testString = "hello-world-hello";
 Splitter splitter = Splitter.on("--");
 List<String> expectedList = Arrays.asList("hello", "", "world", "", "hello");
 List<String> actualList = splitter.split(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testOnPatternSeparator() {
 CharSequence testString = "hello-world-hello";
 Pattern separatorPattern = Pattern.compile("--");
 Splitter splitter = Splitter.on(separatorPattern);
 List<String> expectedList = Arrays.asList("hello", "", "world", "", "hello");
 List<String> actualList = splitter.split(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testOnEmptyStringSeparator() {
 CharSequence testString = "hello-world-hello";
 Splitter splitter = Splitter.on(" ");
 List<String> expectedList = Arrays.asList("hello", "world", "hello");
 List<String> actualList = splitter.split(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testLimit() {
 CharSequence testString = "123456789012345678901234567890";
 Splitter splitter = Splitter.on(" ").limit(5);
 List<String> expectedList = Arrays.asList("12345", "56789", "01234", "56789", "01234");
 List<String> actualList = splitter.split(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testTrimResults() {
 CharSequence testString = "123 456 789";
 Splitter splitter = Splitter.on(" ").trimResults();
 List<String> expectedList = Arrays.asList("123", "456", "789");
 List<String> actualList = splitter.splitToList(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test
 public void testOmitEmptyStrings() {
 CharSequence testString = "123 456  ";
 Splitter splitter = Splitter.on(" ").omitEmptyStrings();
 List<String> expectedList = Arrays.asList("123", "456");
 List<String> actualList = splitter.splitToList(testString);
 assertArrayEquals(expectedList.toArray(), actualList.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 Pattern invalidPattern = Pattern.compile("");
 Splitter splitter = Splitter.on(invalidPattern);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCharSequence() {
 Splitter splitter = Splitter.on('-');
 splitter.split(null);
 }
}