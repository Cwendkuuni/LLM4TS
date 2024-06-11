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
 public void testFilterSingleCharInput() {
 char[] message = {'a'};
 char[] expectedResult = {'a'};
 String result = HTMLFilter.filter(new String(message));
 char[] resultCharArray = result.toCharArray();
 assertEquals(expectedResult.length, resultCharArray.length);
 for (int i = 0; i < expectedResult.length; i++) {
 assertEquals(expectedResult[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] message = {'<', '>', '&', '\"'};
 char[] expectedResult = {'&lt;', '&gt;', '&amp;', '&quot;'};
 String result = HTMLFilter.filter(new String(message));
 char[] resultCharArray = result.toCharArray();
 assertEquals(expectedResult.length, resultCharArray.length);
 for (int i = 0; i < expectedResult.length; i++) {
 assertEquals(expectedResult[i], resultCharArray[i]);
 }
 }

 @Test
 public void testFilterInputWithMixedSpecialAndNormalChars() {
 char[] message = {'a', '<', 'b', '&', 'c', '>', 'd', 'e', 'f', '\"', 'g'};
 char[] expectedResult = {'a', "&lt;", 'b', "&amp;", 'c', "&gt;", 'd', 'e', 'f', "&quot;", 'g'};
 String result = HTMLFilter.filter(new String(message));
 char[] resultCharArray = result.toCharArray();
 assertEquals(expectedResult.length, resultCharArray.length);
 for (int i = 0; i < expectedResult.length; i++) {
 assertEquals(expectedResult[i], resultCharArray[i]);
 }
 }
}