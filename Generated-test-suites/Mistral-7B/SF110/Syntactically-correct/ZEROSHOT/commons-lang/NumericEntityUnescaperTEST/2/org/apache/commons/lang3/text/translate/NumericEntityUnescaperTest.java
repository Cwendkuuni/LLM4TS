package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 private static final String TEST_CHAR_SEQUENCE = "&amp;#160;&#x20AC;&#x1F602;&#12345; &'&#174; &#xFFFD;&#x1F60E; &#0065&#006e; &#x1F601; &#12345;&#12345; &#xFFFE;";

 @Test
 public void testDefaultOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar(TEST_CHAR_SEQUENCE), 0, writer);
 assertEquals(TEST_CHAR_SEQUENCE.length(), length);
 String result = writer.toString();
 assertEquals(StringUtils.DELETE_WHITESpace, TEST_CHAR_SEQUENCE);
 assertEquals(StringUtils.DELETE_WHITESpace, result);
 }

 @Test
 public void testSemiColonRequired() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar(TEST_CHAR_SEQUENCE), 0, writer);
 String result = writer.toString();
 assertTrue(result.endsWith(";"));
 assertEquals(TEST_CHAR_SEQUENCE.length() + 1, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar(TEST_CHAR_SEQUENCE.substring(0, TEST_CHAR_SEQUENCE.length() - 1)), 0, writer);
 }

 @Test
 public void testHexValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#x1F602;"), 0, writer);
 assertEquals(2, length);
 String result = writer.toString();
 assertEquals("\uD835\uDE02", result);
 }

 @Test
 public void testOctalValue() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#0110;"), 0, writer);
 assertEquals(2, length);
 String result = writer.toString();
 assertEquals("\u006E", result);
 }

 @Test
 public void testSetSemiColonOptional() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar(TEST_CHAR_SEQUENCE), 0, writer);
 String result = writer.toString();
 assertFalse(result.endsWith(";"));
 assertEquals(TEST_CHAR_SEQUENCE.length(), length);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#xyz;"), 0, writer);
 }

 @Test
 public void testEntityBeyondRange() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#65536;"), 0, writer);
 String result = writer.toString();
 assertEquals("\uD800\uDC00", result);
 }

 @Test
 public void testEntitySubsequence() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#123;#123;"), 0, writer);
 String result = writer.toString();
 assertEquals("\uD835\uDE03\uD835\uDE03", result);
 }

 @Test
 public void testEntitySubsequenceWithSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#123;#123;"), 0, writer);
 String result = writer.toString();
 assertTrue(result.endsWith(";"));
 assertEquals("\uD835\uDE03\uD835\uDE03", result.substring(0, result.length() - 1));
 assertEquals(TEST_CHAR_SEQUENCE.length() + 1, length);
 }

 @Test
 public void testErrorIfNoSemiColonWithEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#123;"), 0, writer);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testErrorIfNoSemiColonWithEntitySubsequence() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter writer = new StringWriter();
 int length = unescaper.translate(new StringQuasar("&amp;#123;#123;"), 0, writer);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testOptionsEnumSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 EnumSet<NumericEntityUnescaper.OPTION> options = unescaper.options;
 assertTrue(options.contains(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(options.contains(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 assertEquals(2, options.size());
 }

}