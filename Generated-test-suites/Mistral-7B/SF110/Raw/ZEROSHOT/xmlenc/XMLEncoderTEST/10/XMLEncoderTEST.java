package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer writer;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new OutputStreamWriter(new ByteArrayOutputStream());
 outputStream = (ByteArrayOutputStream) writer.getOutputStream();
 }

 @Test
 public void testGetEncoderThrowsUnsupportedEncodingException() {
 assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("invalidEncoding"));
 }

 @Test
 public void testGetEncoderReturnsEncoderInstance() {
 assertEquals(encoder, XMLEncoder.getEncoder("UTF-8"));
 }

 @Test
 public void testGetEncodingReturnsEncoding() {
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testDeclarationWritesCorrectly() throws IOException {
 encoder.declaration(writer);
 String xmlDeclaration = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testTextWritesCorrectly() throws IOException {
 encoder.text(writer, "Hello, World!", false);
 String text = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testTextEscapesCorrectly() throws IOException {
 char[] text = {'<', '>', '&', '\'', '\"', '=', 128, 65, 32, 127, ' ', '\t'};
 encoder.text(writer, text, 0, text.length, true);
 String xmlText = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = "&lt;&gt;&amp;&apos;&quot;=&#x80;C &#xA1; ";
 assertEquals(expectedResult, xmlText);
 }

 @Test
 public void testTextDoesNotEscapeValidCharacters() throws IOException {
 encoder.text(writer, "Hello, & World!");
 String text = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals("Hello, & World!", text);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextThrowsIndexOutOfBoundsException() throws IOException {
 encoder.text(writer, new char[]{}, 1, 1);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextThrowsInvalidXMLException() throws IOException {
 char[] text = {'<', '>', '&', 128, '=', '\t'};
 encoder.text(writer, text, 0, text.length, true);
 }

 @Test
 public void testTextWritesWithSevenBitEncoding() throws IOException {
 encoder = new XMLEncoder("ISO-8859-1");
 encoder.text(writer, "Hello, World!", false);
 String text = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testTextEscapesWithSevenBitEncoding() throws IOException {
 encoder = new XMLEncoder("ISO-8859-1");
 char[] text = {'<', '>', '&', '\'', '\"', '=', '\u0080', 'C', ' ', '\t'};
 encoder.text(writer, text, 0, text.length, true);
 String xmlText = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = "&lt;&gt;&amp;&apos;&quot;=&#x80;C &#xA0; ";
 assertEquals(expectedResult, xmlText);
 }

 @Test
 public void testWhitespaceWritesCorrectly() throws IOException {
 encoder.whitespace(writer, "ABCDE");
 String whitespace = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals("ABCDE", whitespace);
 }

 @Test
 public void testAttributeWritesCorrectly() throws IOException {
 encoder.attribute(writer, "name", "value", '\"', true);
 String xmlAttribute = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = " name=\"value\"";
 assertEquals(expectedResult, xmlAttribute);
 }

 @Test
 public void testAttributeEscapesCorrectly() throws IOException {
 char[] quotationMark = {'&', 'q', 'u', 'o', 't'};
 encoder.attribute(writer, "name", "value &quot;", quotationMark[0], true);
 String xmlAttribute = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = " name=\"value &amp;quot;\"]";
 assertEquals(expectedResult, xmlAttribute);
 }

 @Test
 public void testAttributeDoesNotEscapeQuotationMarkIfNotUsed() throws IOException {
 encoder.attribute(writer, "name", "value\"", '\"', false);
 String xmlAttribute = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = " name=\"value\"";
 assertEquals(expectedResult, xmlAttribute);
 }

 @Test
 public void testAttributeDoesNotEscapeAmpersandIfNotUsed() throws IOException {
 encoder.attribute(writer, "name", "value&", '&', false);
 String xmlAttribute = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedResult = " name=\"value&\"";
 assertEquals(expectedResult, xmlAttribute);
 }

 @Test
 public void testAttributeThrowsNullPointerException() throws IOException {
 encoder.attribute(writer, null, "value", '\"', true);
 assertNull(outputStream.toString());
 }
}