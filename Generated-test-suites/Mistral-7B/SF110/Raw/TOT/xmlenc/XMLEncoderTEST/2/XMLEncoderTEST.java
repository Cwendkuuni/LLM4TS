package org.znerd.xmlenc;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";

 @Test
 public void testGetEncoderWithValidEncoding() throws Exception {
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 assertEquals(UTF8_ENCODING, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderWithNullEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(null);
 }
 catch (IllegalArgumentException e) {
 assertEquals("encoding == null", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderWithUnsupportedEncoding() throws Exception {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder("XYZ-123");
 }
 catch (IllegalArgumentException e) {
 // ignore
 }
 catch (UnsupportedEncodingException e) {
 assertEquals("XYZ-123", e.getMessage());
 }
 }

 @Test
 public void testDeclarationWithUTF8Encoding() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.declaration(out);
 String declaration = out.toString();
 assertTrue(declaration.startsWith(XMLEncoder.DECLARATION_START));
 assertTrue(declaration.endsWith(XMLEncoder.DECLARATION_END));
 assertTrue(declaration.contains(UTF8_ENCODING));
 }

 @Test
 public void testTextWithUSASCIIEncodingAndNoEscaping() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
 encoder.text(out, "Hello, World!");
 String text = out.toString();
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testTextWithISO8859_1EncodingAndEscaping() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ISO_8859_1_ENCODING);
 encoder.text(out, "Hello, <World>!", true);
 String text = out.toString();
 assertEquals("Hello, &lt;World&gt;!", text);
 }

 @Test
 public void testTextWithUTF8EncodingAndEscaping() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.text(out, "Hello, <World>!", true);
 String text = out.toString();
 assertEquals("Hello, <World>!", text);
 }

 @Test
 public void testTextWithUTF8EncodingAndSpecialChars() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 char[] chars = new char[128];
 new Random().nextBytes(chars);
 encoder.text(out, chars, 0, chars.length, true);
 String text = out.toString();
 assertTrue(text.length() > 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithNegativeLength() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.text(out, "Hello".toCharArray(), -1, 3, true);
 }

 @Test(expected = NullPointerException.class)
 public void testTextWithNullWriter() throws Exception {
 Writer out = null;
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.text(out, "Hello".toCharArray(), 0, 3, true);
 }

 @Test(expected = IOException.class)
 public void testTextWithIOException() throws Exception {
 Writer out = new StringWriter() {
 @Override
 public void write(int c, IOException e) throws IOException {
 throw e;
 }
 };
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.text(out, "Hello".toCharArray(), 0, 3, true);
 }

 @Test
 public void testTextWithUTF8EncodingAndNoSpecialChars() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.text(out, "Hello, World!", false);
 String text = out.toString();
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testWhitespaceWithUTF8Encoding() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 String whitespace = "   Hello, World!";
 encoder.whitespace(out, whitespace);
 String text = out.toString();
 assertEquals(whitespace, text);
 }

 @Test
 public void testAttributeWithUTF8Encoding() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 String name = "myAttribute";
 String value = "Hello, World!";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, true);
 String attribute = out.toString();
 assertEquals(" myAttribute=\"Hello, World!\"", attribute);
 }

 @Test
 public void testAttributeWithUTF8EncodingNoQuotationMark() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 String name = "myAttribute";
 String value = "Hello, World!";
 encoder.attribute(out, name, value, (char) 0, true);
 String attribute = out.toString();
 assertEquals(" myAttribute=" + value, attribute);
 }

 @Test
 public void testAttributeWithISO8859_1Encoding() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ISO_8859_1_ENCODING);
 String name = "myAttribute";
 String value = "Hello, World!";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, true);
 String attribute = out.toString();
 assertEquals(" myAttribute=\"Hello, World!\"", attribute);
 }

 @Test
 public void testAttributeWithISO8859_1EncodingNoQuotationMark() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(ISO_8859_1_ENCODING);
 String name = "myAttribute";
 String value = "Hello, World!";
 encoder.attribute(out, name, value, (char) 0, true);
 String attribute = out.toString();
 assertEquals(" myAttribute=" + value, attribute);
 }

 @Test
 public void testAttributeWithSameNameDifferentValues() throws Exception {
 Writer out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 String name = "myAttribute";
 String value1 = "Hello, World!";
 String value2 = "Hello, Again!";
 char quotationMark = '\"';
 encoder.attribute(out, name, value1, quotationMark, true);
 encoder.attribute(out, name, value2, quotationMark, true);
 String attribute = out.toString();
 assertEquals(" myAttribute=\"Hello, World!\"myAttribute=\"Hello, Again!\"" + quotationMark, attribute);
 }
}