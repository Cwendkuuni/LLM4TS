package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;
 private ByteArrayOutputStream outputStream;

 @Test
 public void testNumericEntityUnescaperConstructorWithOptions() {
     unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
     assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
     assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testNumericEntityUnescaperConstructorWithoutOptions() {
     unescaper = new NumericEntityUnescaper();
     assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
     assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testIsSet_semiColonRequired() throws IOException {
     unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#x1F60A;&#x1F611;"; // Emoji Sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 8, length);
     assertEquals("Expected output to contain semicolon, but it was missing", ";&#x1F60A;&#x1F611;", outputStream.toString());
 }

 @Test
 public void testIsSet_semiColonOptional() throws IOException {
     unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#x1F60A;&#x1F611"; // Emoji Sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 6, length);
     assertEquals("Expected output to not contain semicolon", "&#x1F60A;&#x1F611", outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testErrorIfNoSemiColon_missingSemiColon() throws IOException {
     unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#x1F60A;&#x1F611"; // Emoji Sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test
 public void testIsSet_none() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#x1F60A;&#x1F611;"; // Emoji Sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 6, length);
     assertEquals("Expected output to not contain semicolon", "#x1F60A;&#x1F611", outputStream.toString());
 }

 @Test
 public void testHexValueWithinBound() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#x0041;&#x0042;"; // ASCII characters
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 3, length);
     assertEquals("Expected output to be ASCII characters", "AB", outputStream.toString());
 }

 @Test
 public void testHexValueOutOfBound() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#xFFFF;&#x10000;"; // Unicode characters out of bounds
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 6, length);
     assertEquals("Expected output to be two surrogate pairs", "\uD800\uDC00\uD800\uDC00", outputStream.toString());
 }

 @Test
 public void testDecimalValueWithinBound() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#123;&#123;"; // Valid decimal values
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 3, length);
     assertEquals("Expected output to be decimal characters", "{\\n}", outputStream.toString());
 }

 @Test
 public void testDecodeInvalidCharacterSequence() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;#xyz;&#x1F60A;"; // Invalid character sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 0, length);
 }

 @Test
 public void testDecodeEmptyCharacterSequence() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = "&amp;"; // Empty character sequence
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 0, length);
 }

 @Test
 public void testDecodeNullCharacterSequence() throws IOException {
     unescaper = new NumericEntityUnescaper();
     outputStream = new ByteArrayOutputStream();
     final String input = null;
     final Writer writer = new OutputStreamWriter(outputStream);
     int length = unescaper.translate(new StringBuffer(input), 0, writer);
     assertEquals("Invalid number of characters written to the output stream", 0, length);
 }

}