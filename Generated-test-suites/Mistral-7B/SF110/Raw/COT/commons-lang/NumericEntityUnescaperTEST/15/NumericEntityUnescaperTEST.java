package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;
 private StringWriter writer;

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
     OPTION[] options = new OPTION[] {OPTION.semiColonRequired};
     unescaper = new NumericEntityUnescaper(options);
     assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithoutOptions() {
     unescaper = new NumericEntityUnescaper();
     assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSet_semiColonRequiredOption() {
     OPTION[] options = new OPTION[] {OPTION.semiColonRequired};
     unescaper = new NumericEntityUnescaper(options);
     assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSet_semiColonOptionalOption() {
     OPTION[] options = new OPTION[] {OPTION.semiColonOptional};
     unescaper = new NumericEntityUnescaper(options);
     assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSet_errorIfNoSemiColonOption() {
     OPTION[] options = new OPTION[] {OPTION.errorIfNoSemiColon};
     unescaper = new NumericEntityUnescaper(options);
     assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSet_noOptions() {
     unescaper = new NumericEntityUnescaper();
     assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testTranslate_noNumericEntity() throws Exception {
     writer = new StringWriter();
     CharSequence input = "this is a test string";
     int index = 2;
     int actual = unescaper.translate(input, index, writer);
     assertEquals(0, actual);
 }

 @Test
 public void testTranslate_validNumericEntity() throws Exception {
     writer = new StringWriter();
     CharSequence input = "this is a test string &amp;#123;";
     int index = 10;
     int actual = unescaper.translate(input, index, writer);
     assertEquals(3, actual);
     assertEquals("\u007B", writer.toString());
 }

 @Test
 public void testTranslate_validNumericEntityWithHex() throws Exception {
     writer = new StringWriter();
     CharSequence input = "this is a test string &amp;#x41";
     int index = 10;
     int actual = unescaper.translate(input, index, writer);
     assertEquals(3, actual);
     assertEquals("A", writer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_invalidNumericEntityFormat() throws Exception {
     writer = new StringWriter();
     CharSequence input = "this is a test string &amp;#123;x";
     int index = 10;
     unescaper.translate(input, index, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_numericEntityWithoutSemiColon() throws Exception {
     writer = new StringWriter();
     OPTION[] options = new OPTION[] {OPTION.semiColonRequired};
     unescaper = new NumericEntityUnescaper(options);
     CharSequence input = "this is a test string &amp;#123;";
     int index = 10;
     unescaper.translate(input, index, writer);
 }

 @Test
 public void testTranslate_numericEntityWithSemiColon() throws Exception {
     writer = new StringWriter();
     CharSequence input = "this is a test string &amp;#123;";
     int index = 10;
     int actual = unescaper.translate(input, index, writer);
     assertEquals(4, actual);
     assertEquals("\u007B;", writer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_numericEntityWithoutSemiColon_errorOption() throws Exception {
     writer = new StringWriter();
     OPTION[] options = new OPTION[] {OPTION.errorIfNoSemiColon};
     unescaper = new NumericEntityUnescaper(options);
     CharSequence input = "this is a test string &amp;#123;";
     int index = 10;
     unescaper.translate(input, index, writer);
 }
}