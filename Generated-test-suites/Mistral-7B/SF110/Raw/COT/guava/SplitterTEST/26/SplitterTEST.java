package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.StrictMath;

public class SplitterTest {

 private Splitter stringSplitter;
 private Splitter charSplitter;
 private Splitter patternSplitter;
 private Splitter fixedLengthSplitter;

 @BeforeClass
 public static void setupClass() {
 stringSplitter = Splitter.on(" ");
 charSplitter = Splitter.on('-');
 patternSplitter = Splitter.onPattern("\\s+");
 fixedLengthSplitter = Splitter.fixedLength(2);
 }

 @Test
 public void testOnString() {
 String input = "One Two Three Four Five";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString())));
 List<String> expected = new ArrayList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
 assertEquals(expected, output);
 }

 @Test
 public void testOnChar() {
 String input = "A-B-C-D-E";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(charSplitter.toString())));
 List<String> expected = new ArrayList<String>(Arrays.asList("A", "-", "B", "-", "C", "-", "D", "-", "E"));
 assertEquals(expected, output);
 }

 @Test
 public void testOnPattern() {
 String input = "foo bar baz qux quux";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(patternSplitter.toString())));
 List<String> expected = new ArrayList<String>(Arrays.asList("foo", "bar", "baz", "qux", "quux"));
 assertEquals(expected, output);
 }

 @Test
 public void testFixedLength() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(fixedLengthSplitter.toString())));
 List<String> expected = new ArrayList<String>(Arrays.asList("ab", "cd", "ef", "gh", "ij", "jk", "lm", "no", "pq", "rs", "tu", "wv", "xy", "z"));
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptyStringPattern() {
 String input = "";
 Splitter splitter = Splitter.onPattern("");
 try {
 input.split(splitter.toString());
 } catch (IllegalArgumentException e) {
 assertEquals("The pattern may not match the empty string: ", e.getMessage().substring(0, 36));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullSequence() {
 Splitter splitter = stringSplitter;
 try {
 splitter.split(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testOnEmptySequence() {
 String input = "";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString())));
 assertTrue(output.isEmpty());
 // This will throw an IndexOutOfBoundsException because the expected list has one element
 List<String> expected = new ArrayList<String>(Arrays.asList(""));
 assertEquals(expected, output);
 }

 @Test
 public void testLimit() {
 String input = "One Two Three Four Five Six Seven Eight Nine Ten Eleven Twelve Thirteen Fourteen Fifteen";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString(), 3)));
 List<String> expected = new ArrayList<String>(Arrays.asList("One", "Two", "Three"));
 assertEquals(expected, output);
 }

 @Test
 public void testOmitEmptyStrings() {
 String input = "One Two Three Four Five Six Seven Eight Nine Ten Eleven Twelve Thirteen Fourteen Fifteen";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString(), 3), false));
 List<String> expected = new ArrayList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen"));
 assertTrue(output.size() == expected.size());
 for (int i = 0; i < output.size(); i++) {
 assertTrue(output.get(i).length() > 0);
 }
 }

 @Test
 public void testTrimResults() {
 String input = " One Two Three Four Five ";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString()), CharMatcher.WHITESPACE));
 List<String> expected = new ArrayList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
 assertEquals(expected, output);
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 String input = " One Two Three Four Five ";
 List<String> output = new ArrayList<String>(Arrays.asList(input.split(stringSplitter.toString()), CharMatcher.JAVA_ISO_DECIimal_DIGITS));
 List<String> expected = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));
 assertEquals(expected, output);
 }

 @Test
 public void testSplitToList() {
 String input = "One Two Three Four Five Six Seven Eight Nine Ten Eleven Twelve Thirteen Fourteen Fifteen";
 List<String> output = stringSplitter.splitToList(input);
 List<String> expected = new ArrayList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen"));
 assertEquals(expected, output);
 }

 @Test
 public void testWithKeyValueSeparator() {
 String input = "name=John doe age=30";
 Map<String, String> output = new LinkedHashMap<String, String>(Splitter.on("=").withKeyValueSeparator(Splitter.on("=")).split(input));
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("name", "John");
 put("doe", "John doe");
 put("age", "30");
 }};
 assertEquals(expected, output);
 }
}