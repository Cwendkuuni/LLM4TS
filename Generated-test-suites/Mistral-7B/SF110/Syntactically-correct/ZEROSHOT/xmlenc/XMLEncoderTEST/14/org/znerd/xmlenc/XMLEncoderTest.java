package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     String encoding = "UTF-8";
     XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
     assertEquals(UTF8_ENCODING, encoder.getEncoding());
     assertFalse(encoder._sevenBitEncoding);

     encoding = "US-ASCII";
     encoder = XMLEncoder.getEncoder(encoding);
     assertEquals(US_ASCII_ENCODING, encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     XMLEncoder.getEncoder("ISO-8859-11");
 }

 @Test
 public void testDeclaration() throws IOException {
     StringWriter stringWriter = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     encoder.declaration(stringWriter);
     String declaration = stringWriter.toString();
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     assertEquals(expectedDeclaration, declaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String text = "The quick brown fox jumps over the lazy dog.";
     encoder.text(byteArrayOutputStream, text, false);

     byte[] expectedBytes = "The quick brown fox jumps over the lazy dog.".getBytes(StandardCharsets.UTF_8);
     byte[] actualBytes = byteArrayOutputStream.toByteArray();
     assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testTextWithEscapes() throws IOException, InvalidXMLException {
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String text = "<abcd&efgh>";
     encoder.text(byteArrayOutputStream, text, true);

     byte[] expectedBytes = "<abcd&amp;efgh>".getBytes(StandardCharsets.UTF_8);
     byte[] actualBytes = byteArrayOutputStream.toByteArray();
     assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String text = "The character 0x1F is not valid.";
     assertThrows(InvalidXMLException.class, () -> encoder.text(byteArrayOutputStream, text, false));
 }

 @Test
 public void testTextWithInvalidCharacterEscaped() throws IOException, InvalidXMLException {
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String text = "The character 0x1F is not valid.";
     encoder.text(byteArrayOutputStream, text, true);

     byte[] expectedBytes = "The character 0x1F is not valid.";
     byte[] actualBytes = byteArrayOutputStream.toByteArray();
     assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testWhitespace() throws IOException {
     StringWriter stringWriter = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String whitespace = "   ";
     encoder.whitespace(stringWriter, whitespace);

     String expectedWhitespace = "   ";
     assertEquals(expectedWhitespace, stringWriter.toString());
 }

 @Test
 public void testAttribute() throws IOException {
     StringWriter stringWriter = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String name = "name";
     String value = "value";
     char quotationMark = '\"';
     encoder.attribute(stringWriter, name, value, quotationMark, false);

     String expectedAttribute = " name=\"value\"";
     assertEquals(expectedAttribute, stringWriter.toString());
 }

 @Test
 public void testAttributeWithEscapes() throws IOException {
     StringWriter stringWriter = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String name = "name";
     String value = "&amp;<>&quot;";
     char quotationMark = '\"';
     encoder.attribute(stringWriter, name, value, quotationMark, true);

     String expectedAttribute = " name=\"&amp;<>&amp;quot;\">";
     assertEquals(expectedAttribute, stringWriter.toString());
 }

 @Test
 public void testAttributeQuotationMarkDifferentThanDefault() throws IOException {
     StringWriter stringWriter = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

     String name = "name";
     String value = "\"value\"";
     char quotationMark = '\'';
     encoder.attribute(stringWriter, name, value, quotationMark, false);

     String expectedAttribute = " name='value'";
     assertEquals(expectedAttribute, stringWriter.toString());
 }
}