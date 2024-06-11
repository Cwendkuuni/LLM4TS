package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;
 private StringWriter outContent;

 @Before
 public void setUp() {
 unescaper = new NumericEntityUnescaper();
 outContent = new StringWriter();
 }

 @Test
 public void testBasicTranslate() {
 char[] input = {'&', '#', '1', '2', '#', ';'};
 int expectedLength = 3;
 int actualLength = unescaper.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals("\u0001\u0002", outContent.toString());
 }

 @Test
 public void testTranslateHex() {
 char[] input = {'&', '#', 'x', 'a', 'b', '#', ';'};
 int expectedLength = 3;
 int actualLength = unescaper.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals("\u0041\u0042", outContent.toString());
 }

 @Test
 public void testTranslateWithoutSemicolon() {
 char[] input = {'&', '#', '1', '2'};
 int expectedLength = 2;
 int actualLength = unescaper.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals(Character.toString((char) 1), outContent.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithErrorIfNoSemicolon() {
 char[] input = {'&', '#', '1', '2'};
 NumericEntityUnescaper unescaperWithError = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 int actualLength = unescaperWithError.translate(new String(input), 0, outContent);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testTranslateWithSemiColonRequired() {
 char[] input = {'&', '#', '1', '2'};
 NumericEntityUnescaper unescaperWithSemicolonRequired = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int expectedLength = 4;
 int actualLength = unescaperWithSemicolonRequired.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals("\u0001\u0002;", outContent.toString());
 }

 @Test
 public void testTranslateWithSemiColonOptional() {
 char[] input = {'&', '#', '1', '2'};
 NumericEntityUnescaper unescaperWithSemicolonOptional = new NumericEntityUnescaper(OPTION.semiColonOptional);
 int expectedLength = 3;
 int actualLength = unescaperWithSemicolonOptional.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals("\u0001\u0002", outContent.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumber() {
 char[] input = {'&', '#', 'x', '#', 'a', '#'};
 int actualLength = unescaper.translate(new String(input), 0, outContent);
 fail("Should throw NumberFormatException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateIndexOutOfBounds() {
 char[] input = {'&', '#', '1', '2', '#'};
 int actualLength = unescaper.translate(new String(input), 3, outContent);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testTranslateLargeNumber() {
 char[] input = {'&', '#', 'x', 'F', 'F', '#', ';'};
 int expectedLength = 4;
 int actualLength = unescaper.translate(new String(input), 0, outContent);
 assertEquals(expectedLength, actualLength);
 assertEquals("\uFFEFF", outContent.toString());
 }
}