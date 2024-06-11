package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSingleCharacterInput() {
 char[] input = {'A'};
 char[] expected = {'A'};
 String message = new String(input);
 String result = HTMLFilter.filter(message);
 char[] resultCharArray = result.toCharArray();
 assertEquals(expected.length, resultCharArray.length);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals(expected[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] input = {'<', '>', '&', '"'};
 char[] expected = {'&lt;', '&gt;', '&amp;', '&quot;'};
 String message = new String(input);
 String result = HTMLFilter.filter(message);
 char[] resultCharArray = result.toCharArray();
 assertEquals(expected.length, resultCharArray.length);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals(expected[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 char[] input = {'<', '>', '<', '&', '>', '&', '>', '\"', '>'};
 char[] expected = {'&lt;', '&gt;', '&lt;', '&amp;', '&gt;', '&amp;', '&gt;', '&quot;', '&gt;'};
 String message = new String(input);
 String result = HTMLFilter.filter(message);
 char[] resultCharArray = result.toCharArray();
 assertEquals(expected.length, resultCharArray.length);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals(expected[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithLongSequenceOfSpecialCharacters() {
 char[] input = {'<', '>', '<', '&', '>', '&', '>', '&', '>', '&', '>', '&', '>', '&', '>', '&', '>', '&', '>', '&'};
 char[] expected = {'&lt;', '&gt;', '&lt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;', '&amp;', '&gt;'};
 String message = new String(input);
 String result = HTMLFilter.filter(message);
 char[] resultCharArray = result.toCharArray();
 assertEquals(expected.length, resultCharArray.length);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals(expected[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithMixedSpecialAndNormalCharacters() {
 char[] input = {'A', '<', 'B', '>', 'C', '&', 'D', '>', 'E', '&', 'F', '>'};
 char[] expected = {'A', '&lt;', 'B', '&gt;', 'C', '&amp;', 'D', '&gt;', 'E', '&amp;', 'F', '&gt;'};
 String message = new String(input);
 String result = HTMLFilter.filter(message);
 char[] resultCharArray = result.toCharArray();
 assertEquals(expected.length, resultCharArray.length);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals(expected[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithLongMessage() {
 String longMessage = "This is a long message that contains special characters: < & > \"";
 String result = HTMLFilter.filter(longMessage);
 assertEquals(longMessage.length(), result.length());
 }
}