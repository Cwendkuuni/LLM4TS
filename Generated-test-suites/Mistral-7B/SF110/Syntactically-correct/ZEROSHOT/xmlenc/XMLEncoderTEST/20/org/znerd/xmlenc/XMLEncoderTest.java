package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_LATIN1_ENCODING = "ISO-8859-1";

 @Test
 public void testGetEncoder() {
     assertNotNull(XMLEncoder.getEncoder(UTF8_ENCODING));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncodingUTF16() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("UTF-16"));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncodingOther() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("other"));
 }

 @Test
 public void testXMLEncoderInstance() {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     assertEquals(UTF8_ENCODING, encoder.getEncoding());
     assertFalse(encoder._sevenBitEncoding);
 }

 @Test
 public void testXMLEncoderInstanceUSASCII() {
     final XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     assertEquals(US_ASCII_ENCODING, encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);
 }

 @Test
 public void testXMLEncoderInstanceISOLatin1() {
     final XMLEncoder encoder = XMLEncoder.getEncoder(ISO_LATIN1_ENCODING);
     assertEquals(ISO_LATIN1_ENCODING, encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);
 }

 @Test
 public void testXMLEncoderDeclaration() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     encoder.declaration(outputStream);
     final byte[] expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes();
     assertEquals(expectedDeclaration.length, outputStream.size());
     assertArrayEquals(expectedDeclaration, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderTextWithEscaping() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.text(writer, "& < > ' \" >", false);
     final byte[] expected = "&amp; &lt; &gt; &apos; &quot; &gt;".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderTextWithoutEscaping() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.text(writer, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", false);
     final byte[] expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderTextWithSevenBitEncoding() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(ISO_LATIN1_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.text(writer, "öäüÖÄÜ", false);
     final byte[] expected = "&ouml; &auml; &Uuml; &OUml; &szlig; &szlig; &OElig; &OElig;".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test(expected = InvalidXMLException.class)
 public void testXMLEncoderTextInvalidCharacter() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.text(writer, 0x10000);
 }

 @Test
 public void testXMLEncoderWhitespace() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.whitespace(writer, " abc\ndef ");
     final byte[] expected = " abc\ndef ".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderAttribute() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.attribute(writer, "name", "value & < > ' \" >", '\"', false);
     final byte[] expected = "name=\"value &amp; &lt; &gt; &apos; &quot; &gt;\"".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderAttributeSingleQuote() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.attribute(writer, "name", "value ' & < > ' \" >", '\'', false);
     final byte[] expected = "name=\"value ' &amp; &lt; &gt; &apos; &quot; &gt;'\"".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }

 @Test
 public void testXMLEncoderAttributeWithoutEscaping() throws IOException {
     final XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     final Writer writer = new OutputStreamWriter(outputStream);

     encoder.attribute(writer, "name", "value ' & < >", '\'', true);
     final byte[] expected = "name=\"value ' & &lt; &gt;\"".getBytes();
     assertEquals(expected.length, outputStream.size());
     assertArrayEquals(expected, outputStream.toByteArray());
 }
}