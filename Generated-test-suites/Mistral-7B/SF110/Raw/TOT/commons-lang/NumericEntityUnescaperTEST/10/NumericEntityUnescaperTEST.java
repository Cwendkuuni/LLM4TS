package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_ValidNumericEntity() {
 String input = "&amp;#123;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("123", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_ValidNumericEntityWithHex() {
 String input = "&amp;#x41;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("A", out.toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslate_ValidNumericEntityWithLongValue() {
 String input = "&amp;#65533;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 char[] expectedChars = Character.toChars(0x10FFFF);
 out.getBuffer().getBytes(0, (int) expectedChars.length, new byte[expectedChars.length], 0);
 assertEquals(expectedChars, out.toString().toCharArray());
 assertEquals(6, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_MissingSemiColon() {
 String input = "&amp;#123";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testTranslate_InvalidNumericEntity() {
 String input = "&amp;#abc;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InvalidNumericEntityWithHex() {
 String input = "&amp;#xz;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_EmptyInput() {
 String input = "";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InvalidInput() {
 String input = "&amp;#";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InvalidInputWithSemiColon() {
 String input = "&amp;#123;x";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_ValidInputWithOptions() {
 String input = "&amp;#123;";
 NumericEntityUnescaper unescaper1 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter out1 = new StringWriter();
 StringWriter out2 = new StringWriter();
 int length1 = unescaper1.translate(new StringQuasar(input), 0, out1);
 int length2 = unescaper2.translate(new StringQuasar(input), 0, out2);
 assertTrue(length1 == 0 || length2 == 0);
 if (length1 > 0) {
 assertEquals("", out1.toString());
 }
 if (length2 > 0) {
 assertEquals("", out2.toString());
 }
 }

 @Test
 public void testTranslate_ValidInputWithEmptyOptions() {
 String input = "&amp;#123;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("123", out.toString());
 assertEquals(3, length);
 }
}