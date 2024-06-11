package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;
import java.io.StringWriter;
import java.util.Random;

public class XMLEncoderTest {

 private static final String ENCODING_UTF_8 = "UTF-8";
 private static final String ENCODING_US_ASCII = "US-ASCII";
 private static final String ENCODING_ISO_8859_1 = "ISO-8859-1";
 private static final String EMPTY_STRING = "";
 private static final String WHITESPACE_STRING = "   \t\n\r\f ";
 private static final String NON_ASCII_CHARACTER = "€";
 private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + ENCODING_UTF_8 + "\"?>";
 private static final String XML_TEXT = "<root><text>Hello, World!</text></root>";
 private static final String XML_ATTRIBUTE_STRING = "<root attr=\"value&quot;/>";
 private static final String XML_WHITESPACE = "<root><text>   Hello, World!   </text></root>";

 @Test
 public void testEncoderCreationWithValidEncodings() {
 XMLEncoder encoder1 = XMLEncoder.getEncoder(ENCODING_UTF_8);
 XMLEncoder encoder2 = XMLEncoder.getEncoder(ENCODING_US_ASCII);
 XMLEncoder encoder3 = XMLEncoder.getEncoder(ENCODING_ISO_8859_1);
 Assert.assertNotNull(encoder1);
 Assert.assertNotNull(encoder2);
 Assert.assertNotNull(encoder3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncoderCreationWithNullEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 Assert.assertNull(encoder);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testEncoderCreationWithUnsupportedEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder("ISO-8859-3");
 } catch (IllegalArgumentException | UnsupportedEncodingException e) {
 // Expected
 }
 Assert.assertNull(encoder);
 }

 @Test
 public void testGetEncoding() {
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 Assert.assertEquals(ENCODING_UTF_8, encoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 encoder.declaration(writer);
 String encodedDeclaration = writer.toString();
 Assert.assertEquals(XML_DECLARATION, encodedDeclaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 encoder.text(writer, XML_TEXT);
 String encodedXML = writer.toString();
 Assert.assertEquals(XML_TEXT, encodedXML);
 }

 @Test
 public void testTextWithEscaping() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 char[] textChars = XML_TEXT.toCharArray();
 encoder.text(writer, textChars, 0, textChars.length, true);
 String encodedXML = writer.toString();
 Assert.assertEquals(XML_TEXT, encodedXML);
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 char[] textChars = NON_ASCII_CHARACTER.toCharArray();
 try {
 encoder.text(writer, textChars, 0, textChars.length, false);
 fail("Expected InvalidXMLException");
 } catch (InvalidXMLException e) {
 // Expected
 }
 }

 @Test
 public void testTextWithInvalidCharacterAndEscaping() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 char[] textChars = NON_ASCII_CHARACTER.toCharArray();
 try {
 encoder.text(writer, textChars, 0, textChars.length, true);
 fail("Expected InvalidXMLException");
 } catch (InvalidXMLException e) {
 // Expected
 }
 String encodedXML = writer.toString();
 Assert.assertEquals(XML_DECLARATION + XML_TEXT.replaceAll(NON_ASCII_CHARACTER, "&#" + Integer.toHexString(NON_ASCII_CHARACTER.codePointAt(0)) + ";"), encodedXML);
 }

 @Test
 public void testWhitespace() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 encoder.whitespace(writer, WHITESPACE_STRING);
 String encodedWhitespace = writer.toString();
 Assert.assertEquals(WHITESPACE_STRING, encodedWhitespace);
 }

 @Test
 public void testAttribute() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 String attributeValue = "value&quot;";
 char quotationMark = '\"';
 encoder.attribute(writer, "attr", attributeValue, quotationMark, false);
 String encodedAttribute = writer.toString();
 String expectedAttribute = XML_ATTRIBUTE_STRING;
 Assert.assertEquals(expectedAttribute, encodedAttribute);
 }

 @Test
 public void testRandomText() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(ENCODING_UTF_8);
 Random random = new Random();
 StringBuilder textBuilder = new StringBuilder(100);
 for (int i = 0; i < 100; i++) {
 char c = (char) (random.nextInt(128) + 1);
 textBuilder.append(c);
 }
 String text = textBuilder.toString();
 encoder.text(writer, text.toCharArray(), 0, text.length(), false);
 String encodedText = writer.toString();
 Assert.assertEquals(XML_DECLARATION + XML_TEXT.replaceAll("[^\\x00-\\x7F]", ""), encodedText);
 }

}