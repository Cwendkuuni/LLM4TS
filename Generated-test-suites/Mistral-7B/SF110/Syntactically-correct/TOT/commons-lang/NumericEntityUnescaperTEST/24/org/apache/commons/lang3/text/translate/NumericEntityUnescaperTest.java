package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateSimpleNumericEntity() {
 String input = "&amp;#123;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 String expectedOutput = "&#123;";
 assertEquals(expectedOutput.length(), translatedLength);
 assertEquals(expectedOutput, output.toString());
 }

 @Test
 public void testTranslateLowercaseNumericEntity() {
 String input = "&amp;#x61;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 char expectedOutput = 'a';
 assertEquals(Character.toString(expectedOutput).length(), translatedLength);
 assertEquals(Character.toString(expectedOutput), output.toString());
 }

 @Test
 public void testTranslateUppercaseNumericEntity() {
 String input = "&amp;#X41;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 char expectedOutput = 'A';
 assertEquals(Character.toString(expectedOutput).length(), translatedLength);
 assertEquals(Character.toString(expectedOutput), output.toString());
 }

 @Test
 public void testTranslateLargeNumericEntity() {
 String input = "&amp;#65535;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 char[] expectedOutput = Character.toChars(65535);
 assertEquals(expectedOutput.length, translatedLength);
 assertEquals(expectedOutput, output.toString().toCharArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNoSemiColon() {
 String input = "&amp;#123";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 translator.translate(new StringQuasar(input), 0, output);
 fail("Should have thrown IllegalArgumentException for missing semicolon");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNoSemiColonRequired() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&amp;#123";
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar("&amp;#123"), 0, output);
 fail("Should have thrown IllegalArgumentException for missing semicolon");
 }

 @Test
 public void testTranslateEmptyInput() {
 String input = "";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 assertEquals(0, translatedLength);
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateInvalidEntity() {
 String input = "&amp;#invalid;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 assertEquals(0, translatedLength);
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateInvalidHexEntity() {
 String input = "&amp;#xyz;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 assertEquals(0, translatedLength);
 assertEquals("", output.toString());
 }

 @Test
 public void testTranslateWithSemiColonRequired() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#123;";
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 char[] expectedOutput = Character.toChars(123);
 assertEquals(expectedOutput.length, translatedLength);
 assertEquals(expectedOutput, output.toString().toCharArray());
 }

 @Test
 public void testTranslateWithSemiColonOptional() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&amp;#123";
 StringWriter output = new StringWriter();
 int translatedLength = translator.translate(new StringQuasar(input), 0, output);
 char[] expectedOutput = Character.toChars(123);
 assertEquals(expectedOutput.length, translatedLength);
 assertEquals(expectedOutput, output.toString().toCharArray());
 }
}