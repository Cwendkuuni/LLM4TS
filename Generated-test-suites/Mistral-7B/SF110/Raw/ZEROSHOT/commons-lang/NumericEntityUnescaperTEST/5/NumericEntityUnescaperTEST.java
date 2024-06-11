package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 private static final String NUMERIC_ENTITY = "&num; &amp; &amp;#160; &amp;#x1F4A9; &amp;#12345; &amp;#xABCD; &amp;#65536; &amp;#xyz;";

 @Test
 public void testNumericEntityUnescaper() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter writer = new StringWriter();
 CharSequence input = new StringBuffer(NUMERIC_ENTITY);

 int length = 0;
 int index = 0;
 try {
 while (length < input.length()) {
 int translatedLength = unescaper.translate(input, index, writer);
 index += translatedLength;
 length += translatedLength;
 }
 }
 catch (IOException e) {
 fail("IOException should not be thrown");
 }

 String output = writer.toString();
 String expectedOutput = "num semiColon Optional amp newline 🥗 12345 ABCD 65536 xyz";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNumericEntityUnescaperWithSemiColonRequired() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 StringWriter writer = new StringWriter();
 CharSequence input = new StringBuffer(NUMERIC_ENTITY);

 int length = 0;
 int index = 0;
 try {
 while (length < input.length()) {
 int translatedLength = unescaper.translate(input, index, writer);
 index += translatedLength;
 length += translatedLength;
 }
 }
 catch (IOException e) {
 fail("IOException should not be thrown");
 }

 String output = writer.toString();
 String expectedOutput = "num; semiColon Required amp newline 🥗 12345 ABCD 65536 xyz;";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNumericEntityUnescaperWithSemiColonOptional() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter writer = new StringWriter();
 CharSequence input = new StringBuffer(NUMERIC_ENTITY);

 int length = 0;
 int index = 0;
 try {
 while (length < input.length()) {
 int translatedLength = unescaper.translate(input, index, writer);
 index += translatedLength;
 length += translatedLength;
 }
 }
 catch (IOException e) {
 fail("IOException should not be thrown");
 }

 String output = writer.toString();
 String expectedOutput = "num semiColon Optional amp newline 🥗 12345 ABCD 65536 xyz";
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaperWithErrorIfNoSemiColon() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter writer = new StringWriter();
 CharSequence input = new StringBuffer("&num; &amp; &amp;#160; &amp;#x1F4A9; &amp;#12345; &amp;#ABCD; &amp;#65536; &amp;#xyz");

 int length = 0;
 int index = 0;
 try {
 while (length < input.length()) {
 int translatedLength = unescaper.translate(input, index, writer);
 index += translatedLength;
 length += translatedLength;
 }
 }
 catch (IOException e) {
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testSetOption() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
 assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

 NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 assertTrue(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
 assertTrue(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 assertFalse(unescaperWithOptions.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
 }
}