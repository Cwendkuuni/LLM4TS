package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * NumericEntityUnescaperTest.java - Test cases for the NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Test
 public void testSimpleNumericEntity() {
 unescaper = new NumericEntityUnescaper();
 String input = "&amp;#160;";
 String expected = " ";
 try {
 char[] output = new char[input.length() + 1];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 assertEquals(expected, new String(output, 0, length));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testHexNumericEntity() {
 unescaper = new NumericEntityUnescaper();
 String input = "&amp;#x21E6;";
 String expected = "Ⓐ";
 try {
 char[] output = new char[input.length() + 1];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 assertEquals(expected, new String(output, 0, length));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLargeNumericEntity() {
 unescaper = new NumericEntityUnescaper();
 String input = "&amp;#1114117;";
 String expected = "\uD840\uDC35";
 try {
 char[] output = new char[input.length() + 1];
 int length = unescaper.translate(new String(input), 0, new StringWriter(output).getBuffer());
 assertEquals(expected, new String(output, 0, length));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testInvalidNumericEntity() {
 unescaper = new NumericEntityUnescaper();
 String input = "&amp;#asdf;";
 try {
 unescaper.translate(new String(input), 0, new StringWriter(new char[input.length() + 1]).getBuffer());
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}