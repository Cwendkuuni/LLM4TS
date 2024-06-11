package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 private final String testInput = "This text contains &num;1234, &HEXaDECimALx1F &semiColonRequired; &semiColonOptional";
 private final String expectedOutput = "This text contains \u0031\u0032\u0033\u0034, \u0026\u0031\u003F, ; \u0026\u0031\u0030";

 @Test
 public void test_NumericEntityUnescaper_DefaultConstructor() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 CharSequence input = new String(testInput);
 int translatedLength = translator.translate(input, 0, out);
 String actualOutput = out.getBuffer().toString().substring(0, translatedLength);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void test_NumericEntityUnescaper_ConstructorWithNoOptions() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
 StringWriter out = new StringWriter();
 CharSequence input = new String(testInput);
 int translatedLength = translator.translate(input, 0, out);
 String actualOutput = out.getBuffer().toString().substring(0, translatedLength);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_NumericEntityUnescaper_InvalidOption() {
 try {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(OPTION.valueOf("INVALID_OPTION"));
 }
 catch (IllegalArgumentException iae) {
 // Expected
 }
 }

 @Test
 public void test_NumericEntityUnescaper_IsSet_SemiColonRequired() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(SEMI_COLON_REQUIRED, SEMI_COLON_OPTIONAL);
 assertTrue(translator.isSet(SEMI_COLON_REQUIRED));
 assertFalse(translator.isSet(SEMI_COLON_OPTIONAL));
 }

 @Test
 public void test_NumericEntityUnescaper_IsSet_SemiColonOptional() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(SEMI_COLON_REQUIRED, SEMI_COLON_OPTIONAL, ERROR_IF_NO_SEMI_COLON);
 assertFalse(translator.isSet(SEMI_COLON_REQUIRED));
 assertTrue(translator.isSet(SEMI_COLON_OPTIONAL));
 assertFalse(translator.isSet(ERROR_IF_NO_SEMI_COLON));
 }

 @Test
 public void test_NumericEntityUnescaper_IsSet_ErrorIfNoSemiColon() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(SEMI_COLON_REQUIRED, ERROR_IF_NO_SEMI_COLON);
 assertFalse(translator.isSet(SEMI_COLON_REQUIRED));
 assertFalse(translator.isSet(SEMI_COLON_OPTIONAL));
 assertTrue(translator.isSet(ERROR_IF_NO_SEMI_COLON));
 }

 @Test
 public void test_NumericEntityUnescaper_Translate_SimpleNumber() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 CharSequence input = new String("&num;1234");
 int translatedLength = translator.translate(input, 0, out);
 String actualOutput = out.getBuffer().toString().substring(0, translatedLength);
 assertEquals("\u0031\u0032\u0033\u0034", actualOutput);
 }

 @Test
 public void test_NumericEntityUnescaper_Translate_HexNumber() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 CharSequence input = new String("&HEXaDECimALx1F");
 int translatedLength = translator.translate(input, 0, out);
 String actualOutput = out.getBuffer().toString().substring(0, translatedLength);
 assertEquals("\u0026\u0031\u003F", actualOutput);
 }

 @Test
 public void test_NumericEntityUnescaper_Translate_MissingSemiColon() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
 StringWriter out = new StringWriter();
 CharSequence input = new String("&num;1234");
 int translatedLength = translator.translate(input, 0, out);
 String actualOutput = out.getBuffer().toString().substring(0, translatedLength);
 assertEquals("\u0031\u0032\u0033\u0034", actualOutput);
 }

 @Test
 public void test_NumericEntityUnescaper_Translate_MissingSemiColon_ErrorIfNoSemiColon() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
 StringWriter out = new StringWriter();
 CharSequence input = new String("&num;1234");
 try {
 translator.translate(input, 0, out);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException expected) {
 // Expected
 }
 }

 @Test
 public void test_NumericEntityUnescaper_Translate_InvalidNumber() {
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 CharSequence input = new String("&num;invalid");
 int translatedLength = translator.translate(input, 0, out);
 assertEquals(0, translatedLength);
 }
}