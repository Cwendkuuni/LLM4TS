package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class XMLEncoderTest {

 XMLEncoder encoder;
 Writer writer;

 @Test
 public void testGetEncoder() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testGetEncoderWithInvalidEncoding() {
 assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("invalidEncoding"));
 }

 @Test
 public void testDeclaration() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(writer);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", writer.toString());
 }

 @Test
 public void testText() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(writer, "Hello World");
 assertEquals("Hello World\n", writer.toString());
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(writer, "Hello <world>");
 assertEquals("Hello &lt;world&gt;\n", writer.toString());
 }

 @Test
 public void testTextWithEscapeAmpersands() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(writer, "Hello & world");
 assertEquals("Hello &amp; world\n", writer.toString());
 }

 @Test
 public void testTextWithSevenBitEncoding() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("ASCII");
 encoder.text(writer, "Hello World\u00A1");
 assertEquals("Hello World\u00A1\n", writer.toString());
 }

 @Test
 public void testTextWithInvalidCharacterAndSevenBitEncoding() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("ASCII");
 encoder.text(writer, "Hello <world>");
 assertEquals("Hello &lt;world&gt;\n", writer.toString());
 }

 @Test
 public void testTextWithEscapeAmpersandsAndSevenBitEncoding() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("ASCII");
 encoder.text(writer, "Hello & world");
 assertEquals("Hello &amp; world\n", writer.toString());
 }

 @Test
 public void testTextWithEmptyString() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(writer, "");
 assertEquals("", writer.toString());
 }

 @Test
 public void testTextWithNull() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 assertThrows(NullPointerException.class, () -> encoder.text(writer, null));
 }

 @Test
 public void testTextCharacterByCharacter() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\u00A1'};
 encoder.text(writer, text, 0, text.length);
 assertEquals(new String(text), writer.toString());
 }

 @Test
 public void testTextCharacterByCharacterWithInvalidCharacter() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '<'};
 encoder.text(writer, text, 0, text.length);
 assertEquals("Hello &lt;world&gt;\u00A1\n", writer.toString());
 }

 @Test
 public void testTextCharacterByCharacterWithEscapeAmpersands() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '&'};
 encoder.text(writer, text, 0, text.length);
 assertEquals("Hello &amp;world\n", writer.toString());
 }

 @Test
 public void testTextCharacterByCharacterWithEmptyString() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = {'\u0000'};
 encoder.text(writer, text, 0, text.length);
 assertEquals("", writer.toString());
 }

 @Test
 public void testTextCharacterByCharacterWithNull() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = null;
 assertThrows(NullPointerException.class, () -> encoder.text(writer, text, 0, text.length));
 }

 @Test
 public void testWhitespace() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.whitespace(writer, " ");
 assertEquals(" \n", writer.toString());
 }

 @Test
 public void testWhitespaceWithNull() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 assertThrows(NullPointerException.class, () -> encoder.whitespace(writer, null));
 }

 @Test
 public void testWhitespaceCharacterByCharacter() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] whitespace = {' ', '\t', '\n', '\r'};
 encoder.whitespace(writer, whitespace, 0, whitespace.length);
 assertEquals(new String(whitespace), writer.toString());
 }

 @Test
 public void testAttribute() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(writer, "name", "value", '\"');
 assertEquals(" name=\"value\"\n", writer.toString());
 }

 @Test
 public void testAttributeWithNullName() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 assertThrows(NullPointerException.class, () -> encoder.attribute(writer, null, "value", '\"'));
 }

 @Test
 public void testAttributeWithNullValue() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(writer, "name", null, '\"');
 assertEquals(" name=\"\"\n", writer.toString());
 }

 @Test
 public void testAttributeWithInvalidQuotationMark() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 assertThrows(IllegalArgumentException.class, () -> encoder.attribute(writer, "name", "value", 'A'));
 }

 @Test
 public void testAttributeWithEscapeAmpersands() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(writer, "name", "value &amp; more", '\"');
 assertEquals(" name=\"value &amp; more\"\n", writer.toString());
 }

 @Test
 public void testAttributeCharacterByCharacter() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(writer, "name", "value", '\"');
 char[] attribute = {'n', 'a', 'm', 'e', '=', '\"', 'v', 'a', 'l', 'u', 'e', ' ', '&', 'a', 'm', 'p', ';', '\"'};
 encoder.attribute(writer, attribute, 0, attribute.length);
 assertEquals(new String(attribute), writer.toString());
 }

 @Test
 public void testAttributeCharacterByCharacterWithInvalidQuotationMark() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] attribute = {'n', 'a', 'm', 'e', '=', 'A', 'v', 'a', 'l', 'u', 'e', ' ', '&', 'a', 'm', 'p', ';'};
 assertThrows(IllegalArgumentException.class, () -> encoder.attribute(writer, attribute, 0, attribute.length));
 }

 @Test
 public void testAttributeCharacterByCharacterWithEmptyString() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(writer, "name", "", '\"');
 assertEquals(" name=\"\"\n", writer.toString());
 }

 @Test
 public void testAttributeCharacterByCharacterWithNull() throws IOException {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 char[] attribute = null;
 assertThrows(NullPointerException.class, () -> encoder.attribute(writer, attribute, 0, attribute.length));
 }
}