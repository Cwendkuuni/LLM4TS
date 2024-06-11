package com.google.common.base;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SplitterTest {

 @Test
 public void testSplitOnChar() {
 String input = "ThisIsATestStringToSplitOnChar";
 Splitter splitter = Splitter.on(' ');
 List<String> result = splitter.splitToList(input);
 assertEquals("This", result.get(0));
 assertEquals("Is", result.get(1));
 assertEquals("A", result.get(2));
 assertEquals("Test", result.get(3));
 assertEquals("String", result.get(4));
 assertEquals("To", result.get(5));
 assertEquals("Split", result.get(6));
 assertEquals("On", result.get(7));
 assertEquals("Char", result.get(8));
 }

 @Test
 public void testSplitOnEmptyString() {
 String input = "Test, , Test";
 Splitter splitter = Splitter.on(", ");
 List<String> result = splitter.splitToList(input);
 assertEquals("Test", result.get(0));
 assertEquals("", result.get(1));
 assertEquals("Test", result.get(2));
 }

 @Test
 public void testSplitOnRegex() {
 String input = "ThisIsATestStringToSplitOnRegex[\\[\\]]";
 Splitter splitter = Splitter.onPattern("\\[|\\]");
 List<String> result = splitter.splitToList(input);
 assertEquals("ThisIsATestStringToSplitOnRegex", result.get(0));
 assertEquals("", result.get(1));
 assertEquals("", result.get(1));
 assertEquals("", result.get(2));
 assertEquals("", result.get(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitOnNullInput() {
 Splitter splitter = Splitter.on(' ');
 try {
 splitter.splitToList(null);
 } catch (IllegalArgumentException e) {
 assertEquals("CharSequence must not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitOnNullPattern() {
 Splitter splitter = Splitter.onPattern(null);
 try {
 splitter.splitToList("Test");
 } catch (IllegalArgumentException e) {
 assertEquals("Pattern must not be null", e.getMessage());
 }
 }

 @Test
 public void testSplitOnCustomStrategy() {
 String input = "Test:One Two:Three Four:Five";
 Splitter splitter = new Splitter(new Splitter.Strategy() {
 @Override
 public Splitter.SplittingIterator iterator(final Splitter splitter, final CharSequence sequence) {
 return new Splitter.SplittingIterator(splitter, sequence) {
 @Override
 public int separatorStart(final int start) {
 return sequence.indexOf(":", start);
 }

 @Override
 public int separatorEnd(final int separatorPosition) {
 return separatorPosition + 1;
 }
 };
 }
 });
 List<String> result = splitter.splitToList(input);
 assertEquals("Test", result.get(0));
 assertEquals("One", result.get(1));
 assertEquals("Two", result.get(2));
 assertEquals("Three", result.get(3));
 assertEquals("Four", result.get(4));
 assertEquals("Five", result.get(5));
 }

 @Test
 public void testSplitOnCustomStrategyWithEmptyString() {
 String input = "Test:One :Two:Three Four:Five";
 Splitter splitter = new Splitter(new Splitter.Strategy() {
 @Override
 public Splitter.SplittingIterator iterator(final Splitter splitter, final CharSequence sequence) {
 return new Splitter.SplittingIterator(splitter, sequence) {
 @Override
 public int separatorStart(final int start) {
 return sequence.indexOf(":", start);
 }

 @Override
 public int separatorEnd(final int separatorPosition) {
 return separatorPosition + 1;
 }
 };
 }
 });
 List<String> result = splitter.splitToList(input);
 assertEquals("Test", result.get(0));
 assertEquals("One", result.get(1));
 assertEquals("", result.get(2));
 assertEquals("Two", result.get(3));
 assertEquals("Three", result.get(4));
 assertEquals("Four", result.get(5));
 assertEquals("Five", result.get(6));
 }

 @Test
 public void testMapSplitter() {
 String input = "Key1=Value1 Key2=Value2 Key3=Value3";
 Splitter splitter = Splitter.on("=");
 Map<String, String> map = splitter.withKeyValueSeparator(splitter).split(input);
 assertEquals("Key1", map.keySet().iterator().next());
 assertEquals("Value1", map.get("Key1"));
 assertEquals("Key2", map.keySet().iterator().next());
 assertEquals("Value2", map.get("Key2"));
 assertEquals("Key3", map.keySet().iterator().next());
 assertEquals("Value3", map.get("Key3"));
 }
}