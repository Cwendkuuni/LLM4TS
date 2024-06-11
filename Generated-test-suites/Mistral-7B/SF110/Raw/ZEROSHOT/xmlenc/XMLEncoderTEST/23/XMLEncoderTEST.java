package org.znerd.xmlenc.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

public class XMLEncoderTest {

 private static final String ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
 private static final String EMPTY_STRING = "";
 private static final String WHITESPACE = "   \t\r\n";
 private static final String NAME = "testName";
 private static final String VALUE = "testValue";
 private static final char QUOTATION_MARK = '"';
 private static final boolean ESCAPE_AMPERSANDS = true;
 private static final char[] ESCAPED_AMPERSAND = new char[]{'&', 'a', 'm', 'p', ';'};

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 assertEquals(ENCODING, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 XMLEncoder.getEncoder("InvalidEncoding");
 }

 @Test
 public void testDeclare() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.declare(writer);
 String xmlDeclaration = writer.toString();
 assertTrue(xmlDeclaration.startsWith("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>"));
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 char[] ch = "test".toCharArray();
 encoder.text(writer, ch, 0, ch.length, ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals("test", text);
 }

 @Test
 public void testTextWithEscapedCharacter() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write("&");
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals(new String(ESCAPED_AMPERSAND), text);
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
 ByteArrayOutputStream bytes = new ByteArrayOutputStream();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.text(bytes, 0x01, 0, 0, ESCAPE_AMPERSANDS);
 String errorMessage = new String(bytes.toByteArray());
 assertTrue(errorMessage.contains("The character 0x01 is not valid."));
 }

 @Test
 public void testTextSevenBitEncoding() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
 StringWriter writer = new StringWriter();
 char[] ch = new char[]{'<', '>', '&', '\u00A1'};
 encoder.text(writer, ch, 0, ch.length, ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals("&lt;&gt;&amp;#128;", text);
 }

 @Test
 public void testTextWithoutEscaping() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write("&");
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), false);
 String text = writer.toString();
 assertEquals("&", text);
 }

 @Test
 public void testTextWithQuotationMark() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write(QUOTATION_MARK);
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals("&quot;", text);
 }

 @Test
 public void testTextWithEscapedQuotationMark() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write(ESCAPED_QUOTE);
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals(new String(ESCAPED_QUOTE), text);
 }

 @Test
 public void testTextWithAmpersand() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write("&");
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals(new String(ESCAPED_AMPERSAND), text);
 }

 @Test
 public void testTextWithEscapedAmpersand() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 writer.write(ESCAPED_AMPERSAND);
 encoder.text(writer, writer.toString().toCharArray(), 0, writer.length(), ESCAPE_AMPERSANDS);
 String text = writer.toString();
 assertEquals(new String(ESCAPE_AMPERSAND), text);
 }

 @Test
 public void testTextWithWhitespace() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 StringWriter writer = new StringWriter();
 encoder.whitespace(writer, WHITESPACE);
 String xmlWhitespace = writer.toString();
 assertTrue(xmlWhitespace.startsWith(" xml version=\"1.0\" encoding=\"" + ENCODING + "\"?"));
 String trimmedXmlWhitespace = xmlWhitespace.substring(xmlDeclaration.length()).trim();
 assertEquals(WHITESPACE, trimmedXmlWhitespace);
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.attribute(writer, NAME, VALUE, QUOTATION_MARK, ESCAPE_AMPERSANDS);
 String xmlAttribute = writer.toString();
 assertTrue(xmlAttribute.startsWith("<" + NAME + " "));
 assertTrue(xmlAttribute.endsWith("=" + QUOTATION_MARK + ">" + VALUE + QUOTATION_MARK));
 }

 @Test
 public void testAttributeWithEscapedCharacter() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.attribute(writer, NAME, VALUE + "&", QUOTATION_MARK, ESCAPE_AMPERSANDS);
 String xmlAttribute = writer.toString();
 assertTrue(xmlAttribute.startsWith("<" + NAME + " "));
 assertTrue(xmlAttribute.endsWith("=" + QUOTATION_MARK + ">" + ESCAPED_AMPERSAND + VALUE.substring(xmlAttribute.indexOf(ESCAPED_AMPERSAND) + 4) + QUOTATION_MARK));
 }

 @Test
 public void testAttributeWithEscapedQuotationMark() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.attribute(writer, NAME, VALUE + ESCAPED_QUOTE, QUOTATION_MARK, ESCAPE_AMPERSANDS);
 String xmlAttribute = writer.toString();
 assertTrue(xmlAttribute.startsWith("<" + NAME + " "));
 assertTrue(xmlAttribute.endsWith("=" + ESCAPED_QUOTE + VALUE.substring(xmlAttribute.indexOf(ESCAPED_QUOTE) + 2) + QUOTATION_MARK + ">"));
 }

 @Test
 public void testAttributeWithAmpersand() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.attribute(writer, NAME, VALUE + "&", QUOTATION_MARK, ESCAPE_AMPERSANDS);
 String xmlAttribute = writer.toString();
 assertTrue(xmlAttribute.startsWith("<" + NAME + " "));
 assertTrue(xmlAttribute.endsWith("=" + ESCAPED_AMPERSAND + VALUE.substring(xmlAttribute.indexOf(ESCAPED_AMPERSAND) + 4) + QUOTATION_MARK + ">"));
 }

 @Test
 public void testAttributeWithEscapedAmpersand() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ENCODING);
 encoder.attribute(writer, NAME, VALUE + ESCAPED_AMPERSAND, QUOTATION_MARK, ESCAPE_AMPERSANDS);
 String xmlAttribute = writer.toString();
 assertTrue(xmlAttribute.startsWith("<" + NAME + " "));
 assertTrue(xmlAttribute.endsWith("=" + ESCAPED_AMPERSAND + ESCAPED_AMPERSAND + VALUE.substring(xmlAttribute.indexOf(ESCAPED_AMPERSAND) + 4) + QUOTATION_MARK + ">"));
 }

}