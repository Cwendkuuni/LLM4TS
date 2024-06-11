package com.google.common.base;

import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 String input = "item1-item2-item3-item4";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3", "item4");
 assertEquals(expected, result);
 }

 @Test
 public void testOnRegexSeparator() {
 Splitter splitter = Splitter.onPattern("[.-]");
 String input = "item1-item2.item3-item4";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3", "item4");
 assertEquals(expected, result);
 }

 @Test
 public void testOnEmptySeparator() {
 Splitter splitter = Splitter.on("");
 String input = "item1item2item3item4";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3", "item4");
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullSeparator() {
 Splitter splitter = null;
 String input = "item1-item2-item3-item4";
 Iterable<String> result = splitter.split(input);
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testOnStringSeparatorWithLimit() {
 Splitter splitter = Splitter.on("-").limit(3);
 String input = "item1-item2-item3-item4-item5-item6-item7";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3");
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparatorWithTrimming() {
 Splitter splitter = Splitter.on("-").trimResults();
 String input = "item1- item2 -item3- item4 ";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3", "item4");
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparatorWithEmptyStrings() {
 Splitter splitter = Splitter.on(" ").omitEmptyStrings();
 String input = "item1 item2 item3 item4 item5";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("item1", "item2", "item3", "item4", "item5");
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparatorWithCustomStrategy() {
 Splitter splitter = new Splitter(new Splitter.Strategy() {
 @Override
 public Iterator<String> iterator(Splitter p0, CharSequence p1) {
 return new Iterator<String>() {
 @Override
 public boolean hasNext() {
 return p1.length() > 0;
 }

 @Override
 public String next() {
 return String.valueOf(p1.charAt(0));
 }

 @Override
 public void remove() {
 throw new UnsupportedOperationException();
 }
 };
 }
 });
 String input = "abcdef";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e", "f");
 assertEquals(expected, result);
 }
}