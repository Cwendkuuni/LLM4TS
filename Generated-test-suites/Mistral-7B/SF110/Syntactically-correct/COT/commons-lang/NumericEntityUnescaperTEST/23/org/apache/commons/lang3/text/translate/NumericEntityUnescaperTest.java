package org.junit.tests;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private static final String ENTITY_TEST_CASE = "&%#A; &%#x1F; &%#x10F0; &%#x10FF; &%#ABC; &%#abc; &%#1234; &%#12345; &%#65535; &%#65536; &%#SemiColon; &%#semiColon;";

 @Test
 public void testNumericEntityUnescaperConstructorWithNoOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithInvalidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "&%ab;";
 CharSequence cs = new StringCharSequence(input);
 StringWriter sw = new StringWriter();
 unescaper.translate(cs, 0, sw);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testTranslateWithValidNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = ENTITY_TEST_CASE;
 StringWriter sw = new StringWriter();
 unescaper.translate(new StringCharSequence(input), 0, sw);
 String output = sw.toString();
 assertEquals("&#A; &#x1F; &#x10F0; &#x10FF; &#ABC; &#abc; &#1234; &#12345; &#65535; &#65536; SemiColon; semiColon;", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithMissingSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 NumericEntityUnescaper unescaperWithError = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 String inputWithNoSemiColon = "&%#1234";
 CharSequence cs = new StringCharSequence(inputWithNoSemiColon);
 StringWriter sw = new StringWriter();
 unescaper.translate(cs, 0, sw);
 String output = sw.toString();
 unescaperWithError.translate(cs, 0, sw);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 assertEquals("", output);
 }

 @Test
 public void testTranslateWithSemiColonOptional() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 String inputWithSemiColon = "&%#1234;";
 StringWriter sw = new StringWriter();
 unescaper.translate(new StringCharSequence(inputWithSemiColon), 0, sw);
 String output = sw.toString();
 assertEquals("&#1234;", output);
 }

}