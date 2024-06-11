package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
 private static final Random RANDOM = new Random();
 private static final char RANDOM_CHAR = (char) RANDOM.nextInt(0xFF) & 0xFF;

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     assertEquals(UTF8_ENCODING, XMLEncoder.getEncoder(UTF8_ENCODING).getEncoding());
     assertEquals(US_ASCII_ENCODING, XMLEncoder.getEncoder(US_ASCII_ENCODING).getEncoding());
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("unknown-encoding"));
 }

 @Test
 public void testDeclaration() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     String encoding = UTF8_ENCODING;
     XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
     encoder.declaration(writer);
     String declaration = writer.toString();
     assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration);
 }

 @Test
 public void testTextWithSevenBitEncoding() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     String text = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
     encoder.text(writer, text, false);
     assertEquals(text, writer.toString());
 }

 @Test
 public void testTextWithEightBitEncoding() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char character = (char) 0xA0;
     encoder.text(writer, character);
     assertEquals("&amp;#xA0;", writer.toString());
     writer.getBuffer().setLength(0);
     encoder.text(writer, character, true);
     assertEquals("&xA0;", writer.toString());
 }

 @Test
 public void testTextWithInvalidCharacter() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char invalidCharacter = 0x00;
     assertThrows(InvalidXMLException.class, () -> encoder.text(writer, invalidCharacter));
 }

 @Test
 public void testTextWithAmpersand() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char ampersand = '&';
     encoder.text(writer, ampersand, false);
     assertEquals("&amp;&", writer.toString());
     writer.getBuffer().setLength(0);
     encoder.text(writer, ampersand, true);
     assertEquals("&amp;", writer.toString());
 }

 @Test
 public void testTextWithQuotationMark() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char quotationMark = '\"';
     encoder.text(writer, quotationMark, false);
     assertEquals("&quot;&quot;", writer.toString());
     writer.getBuffer().setLength(0);
     encoder.text(writer, quotationMark, true);
     assertEquals("&quot;", writer.toString());
 }

 @Test
 public void testTextWithLessThanSymbol() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char lessThanSymbol = '<';
     encoder.text(writer, lessThanSymbol, false);
     assertEquals("&lt;&lt;", writer.toString());
     writer.getBuffer().setLength(0);
     encoder.text(writer, lessThanSymbol, true);
     assertEquals("&lt;", writer.toString());
 }

 @Test
 public void testTextWithGreaterThanSymbol() throws NullPointerException, InvalidXMLException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     char greaterThanSymbol = '>';
     encoder.text(writer, greaterThanSymbol, false);
     assertEquals("&gt;&gt;", writer.toString());
     writer.getBuffer().setLength(0);
     encoder.text(writer, greaterThanSymbol, true);
     assertEquals("&gt;", writer.toString());
 }

 @Test
 public void testTextWithAttribute() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     String name = "name";
     String value = "value";
     char quotationMark = '\"';
     encoder.text(writer, name);
     encoder.write(writer, "=");
     encoder.attribute(writer, name, value, quotationMark, false);
     String result = writer.toString();
     assertEquals("<name=" + value + "/>", result);
     writer.getBuffer().setLength(0);
     encoder.attribute(writer, name, value, quotationMark, true);
     result = writer.toString();
     assertEquals("<name>" + value + "</name>", result);
 }

 @Test
 public void testTextWithAttributeQuotationMarkEscape() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     String name = "name";
     String value = "value with \"quoted\" part";
     char quotationMark = '\'';
     encoder.text(writer, name);
     encoder.write(writer, "=");
     encoder.attribute(writer, name, value, quotationMark, false);
     String result = writer.toString();
     assertEquals("<name=" + value.replace("\"", "&quot;") + "/>", result);
     writer.getBuffer().setLength(0);
     encoder.attribute(writer, name, value, quotationMark, true);
     result = writer.toString();
     assertEquals("<name>" + value.replace("\"", "&quot;") + "</name>", result);
 }

 @Test
 public void testTextWithAttributeAmpersandEscape() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     String name = "name";
     String value = "value&amp;more";
     char quotationMark = '\"';
     encoder.text(writer, name);
     encoder.write(writer, "=");
     encoder.attribute(writer, name, value, quotationMark, false);
     String result = writer.toString();
     assertEquals("<name=" + value.replace("&", "&amp;") + "/>", result);
     writer.getBuffer().setLength(0);
     encoder.attribute(writer, name, value, quotationMark, true);
     result = writer.toString();
     assertEquals("<name>" + value.replace("&", "&amp;") + "</name>", result);
 }

 @Test
 public void testTextWithAttributeInvalidCharacter() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     String name = "name";
     char invalidCharacter = 0x00;
     String value = "value";
     char quotationMark = '\"';
     encoder.text(writer, name);
     encoder.write(writer, "=");
     assertThrows(InvalidXMLException.class, () -> encoder.attribute(writer, name, value, quotationMark, false));
     writer.getBuffer().setLength(0);
     encoder.attribute(writer, name, value, quotationMark, true);
 }

 @Test
 public void testTextWithAttributeQuotationMarkAndAmpersandEscape() throws NullPointerException, IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     String name = "name";
     String value = "value with \"quoted\" part and &amp;more";
     char quotationMark = '\'';
     encoder.text(writer, name);
     encoder.write(writer, "=");
     encoder.attribute(writer, name, value, quotationMark, false);
     String result = writer.toString();
     assertEquals("<name=" + value.replace("\"", "&quot;").replace("&", "&amp;") + "/>", result);
     writer.getBuffer().setLength(0);
     encoder.attribute(writer, name, value, quotationMark, true);
     result = writer.toString();
     assertEquals("<name>" + value.replace("\"", "&quot;").replace("&", "&amp;") + "</name>", result);
 }
}