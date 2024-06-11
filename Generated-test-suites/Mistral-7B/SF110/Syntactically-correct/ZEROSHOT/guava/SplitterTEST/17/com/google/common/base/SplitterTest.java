package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split("test-1,test-2,test-3");
assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on("-");
 List<String> result = splitter.split("test-1,test-2,test-3");
assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\s+");
 List<String> result = splitter.split("test 1 test 2 test 3");
 assertEquals(Arrays.asList("test", "1", "test", "2", "test", "3"), result);
 }

 @Test
 public void testOnFixedLengthSeparator() {
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.split("1234567890");
 assertEquals(Arrays.asList("123", "456", "789"), result);
 }

 @Test
 public void testOmitEmptyStrings() {
 Splitter splitter = Splitter.on("-").omitEmptyStrings();
 List<String> result = splitter.split("test- -test-test--");
 assertEquals(Arrays.asList("test", "test", "test"), result);
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on("-").limit(2);
 List<String> result = splitter.split("test-1-test-2-test-3-test-4");
 assertEquals(Arrays.asList("test-1", "test-2"), result);
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on("-").trimResults();
 List<String> result = splitter.split(" test-1 test-2 test-3 ");
 assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 Splitter splitter = Splitter.on("-").trimResults(Pattern.compile("^\\s+|\\s+$"));
 List<String> result = splitter.split(" test-1 test-2 test-3 ");
 assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSequence() {
 Splitter splitter = Splitter.on("-");
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySequence() {
 Splitter splitter = Splitter.on("-");
 List<String> result = splitter.split("");
 assertTrue(result.isEmpty());
 }
}