package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private static final String ENTITY_WITH_SEMI_COLON = "&num;";
 private static final String ENTITY_WITHOUT_SEMI_COLON = "&num";
 private static final String HEX_ENTITY = "&#xabcd;";
 private static final String OCTAL_ENTITY = "&#0123;";
 private static final String NUMBER_OUT_OF_RANGE = "&ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+";

 @Test
 public void testConstructorWithOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testConstructorWithoutOptions() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

 unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

 unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testTranslate_EntityWithSemiColon() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &num; test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("This is a \u0026num;\u0026 test.", input.toString());
 assertEquals("This is a \u0026num;\u0026 test.", out.toString());
 assertEquals(input.length(), length);
 }

 @Test
 public void testTranslate_EntityWithoutSemiColon() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &num test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("This is a \u0026num test.", input.toString());
 assertEquals("This is a \u0026num test.", out.toString());
 assertEquals(input.length(), length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_EntityWithoutSemiColonWithErrorOption() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &num test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 int length = unescaper.translate(input, 0, out);
 fail("An IllegalArgumentException should be thrown.");
 }

 @Test
 public void testTranslate_HexEntity() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &#xABCD; test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("This is a \u0026ABCD;\u0026 test.", input.toString());
 assertEquals("\u0041\u0042\u0043\u0044", out.toString());
 assertEquals(input.length(), length);
 }

 @Test
 public void testTranslate_OctalEntity() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &#0123; test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("This is a \u00260123;\u0026 test.", input.toString());
 assertEquals("\u0033", out.toString());
 assertEquals(input.length(), length);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidEntity() throws Exception {
 StringWriter out = new StringWriter();
 CharSequence input = new String("This is a &ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+ test.");
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 fail("A NumberFormatException should be thrown.");
 }
}