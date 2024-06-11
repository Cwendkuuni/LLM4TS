package org.junit.tests;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 private final NumericEntityUnescaper semiColonRequiredUnescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

 @Test
 public void testIsSet() {
 OPTION option = OPTION.semiColonOptional;
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(option);
 assertTrue(unescaper.isSet(option));
 }

 @Test
 public void testIsSetWhenNoOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testTranslateBasicScenario() throws IOException {
 String input = "&amp;";
 char expected = (char) 38; // '&' ASCII value
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(1, length);
 assertEquals(expected, out.toString().charAt(0));
 }

 @Test
 public void testTranslateWithSemiColon() throws IOException {
 String input = "&amp;";
 Writer out = new StringWriter();
 int length = semiColonRequiredUnescaper.translate(new StringReader(input), 0, out);
 assertEquals(2, length);
 assertEquals(expected, out.toString().charAt(0));
 assertEquals(';', out.toString().charAt(1));
 }

 @Test
 public void testTranslateWithSemiColonMissing() throws IOException {
 String input = "&amp;";
 Writer out = new StringWriter();
 assertThrows(IllegalArgumentException.class, () -> semiColonRequiredUnescaper.translate(new StringReader(input), 0, out));
 }

 @Test
 public void testTranslateWithInvalidCharacter() throws IOException {
 String input = "&invalidEntity;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithHexValue() throws IOException {
 String input = "&#xff00;";
 char expected = (char) 0xff00;
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(3, length);
 assertEquals(expected, out.toString().charAt(0));
 assertEquals(expected >> 8, out.toString().charAt(1));
 assertEquals(';', out.toString().charAt(2));
 }

 @Test
 public void testTranslateWithInvalidHexValue() throws IOException {
 String input = "&#xabcd;";
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithLongValue() throws IOException {
 String input = "&#65536;";
 char expected = (char) 0x10000;
 Writer out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals(3, length);
 assertEquals(expected, out.toString().charAt(0));
 assertEquals(expected >> 16, out.toString().charAt(1));
 assertEquals(expected >> 8, out.toString().charAt(2));
 }

 @Test
 public void testTranslateWithLongValueAndSemiColonRequired() throws IOException {
 String input = "&#65536;";
 Writer out = new StringWriter();
 int length = semiColonRequiredUnescaper.translate(new StringReader(input), 0, out);
 assertEquals(4, length);
 assertEquals(expected, out.toString().charAt(0));
 assertEquals(expected >> 16, out.toString().charAt(1));
 assertEquals(expected >> 8, out.toString().charAt(2));
 assertEquals(';', out.toString().charAt(3));
 }

 @Test
 public void testTranslateWithLongValueAndSemiColonOptional() throws IOException {
 String input = "&#65536;";
 Writer out = new StringWriter();
 int length = new NumericEntityUnescaper(OPTION.semiColonOptional).translate(new StringReader(input), 0, out);
 assertEquals(3, length);
 assertEquals(expected, out.toString().charAt(0));
 assertEquals(expected >> 16, out.toString().charAt(1));
 assertEquals(expected >> 8, out.toString().charAt(2));
 }

 @Test
 public void testTranslateWithLongValueAndErrorIfNoSemiColon() {
 String input = "&#65536";
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, new StringWriter()));
 }
}