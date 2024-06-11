package org.znerd.xmlenc;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testDeclarationWithUTF8Encoding() throws IOException, NullPointerException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.declaration(writer);
     String declaration = writer.toString();
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     assertEquals(expectedDeclaration, declaration);
 }

 @Test
 public void testTextWithUTF8EncodingAndEscapeAmpersandsFalse() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "This is & a test string.", false);
     String output = writer.toString();
     String expectedOutput = "This is &amp; a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test
 public void testTextWithUTF8EncodingAndEscapeAmpersandsTrue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "This is & a test string.", true);
     String output = writer.toString();
     String expectedOutput = "This is &amp;amp; a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test
 public void testTextWithISO88591EncodingAndEscapeAmpersandsFalse() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.text(writer, "This is & a test string.", false);
     String output = writer.toString();
     String expectedOutput = "This is &amp; a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test
 public void testTextWithISO88591EncodingAndEscapeAmpersandsTrue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.text(writer, "This is & a test string.", true);
     String output = writer.toString();
     String expectedOutput = "This is &amp;amp; a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
     encoder = new XMLEncoder("ABC-123");
 }

 @Test
 public void testTextWithAsciiEncodingAndEscapeAmpersandsFalse() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ASCII");
     encoder.text(writer, "This is & a test string.", false);
     String output = writer.toString();
     String expectedOutput = "This is & a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test
 public void testTextWithAsciiEncodingAndEscapeAmpersandsTrue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ASCII");
     encoder.text(writer, "This is & a test string.", true);
     String output = writer.toString();
     String expectedOutput = "This is &amp; a test string.";
     assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderWithNullEncoding() throws IllegalArgumentException {
     encoder = new XMLEncoder(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithInvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "This is \u0001 a test string.", false);
 }

 @Test
 public void testWhitespaceWithUTF8Encoding() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.whitespace(writer, "   ");
     String output = writer.toString();
     String expectedOutput = "   ";
     assertEquals(expectedOutput, output);
 }

 @Test
 public void testAttributeWithUTF8EncodingQuotationMarkQuote() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "\"This is a test\"", '\"', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">This is a test</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithUTF8EncodingQuotationMarkApostrophe() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "'This is a test'", '\'', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">'This is a test</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithUTF8EncodingEscapeAmpersandsFalse() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "name&amp;value", '\"', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">name&amp;value</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithUTF8EncodingEscapeAmpersandsTrue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "name&amp;amp;value", '\"', true);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">name&amp;amp;amp;value</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithISO88591EncodingQuotationMarkQuote() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.attribute(writer, "test", "\"This is a test\"", '\"', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">This is a test</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithISO88591EncodingQuotationMarkApostrophe() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.attribute(writer, "test", "'This is a test'", '\'', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">'This is a test</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithISO88591EncodingEscapeAmpersandsFalse() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.attribute(writer, "test", "name&amp;value", '\"', false);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">name&amp;value</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testAttributeWithISO88591EncodingEscapeAmpersandsTrue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.attribute(writer, "test", "name&amp;amp;value", '\"', true);
     String output = writer.toString();
     String expectedOutput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.w3.org/2001/XMLSchema.xsd\">name&amp;amp;amp;value</test>";
     assertTrue(output.contains(expectedOutput));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeWithNullName() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, null, "value", '\"', false);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeWithNullValue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", null, '\"', false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeWithEmptyStringAsValue() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "", '\"', false);
 }

 @Test
 public void testAttributeWithInvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "This is \u0001 a test", '\"', false);
 }
}