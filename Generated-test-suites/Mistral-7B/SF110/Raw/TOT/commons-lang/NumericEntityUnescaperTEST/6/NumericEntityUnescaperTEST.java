package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_ValidNumericEntityWithSemicolon() {
 String input = "(&#x61;&#x62;);";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("abc", out.toString());
 assertEquals(6, length);
 }

 @Test
 public void testTranslate_ValidNumericEntityWithoutSemicolon() {
 String input = "(&#x61;&#x62)";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("abc", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_InvalidNumericEntityWithSemicolon() {
 String input = "(&#x61;&#x62;&#x63);#";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InvalidNumericEntityWithoutSemicolon() {
 String input = "(&#x61;&#x62;&#x63)";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_HexNumericEntity() {
 String input = "(&#x41;&#x42;&#x43);";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 assertEquals("ABC", out.toString());
 assertEquals(3 + 3 + 1, length);
 }

 @Test
 public void testTranslate_MaxValueNumericEntity() {
 String input = "(&#xFFFF);";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 String expected = Character.toString((char) 0xFFFF);
 assertEquals(expected + expected.charAt(0), out.toString());
 assertEquals(3 + 1 + 1, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidNumericEntity_ErrorIfNoSemicolon() {
 String input = "(&#x61;&#x62)";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringQuasar(input), 0, out);
 }

 @Test
 public void testIsSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 }

}