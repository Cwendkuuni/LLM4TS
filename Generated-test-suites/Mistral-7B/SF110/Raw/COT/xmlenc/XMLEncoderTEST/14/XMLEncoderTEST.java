package org.znerd.xmlenc;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private static XMLEncoder getEncoder(final String encoding) throws IllegalArgumentException, UnsupportedEncodingException {
 return XMLEncoder.getEncoder(encoding);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
 assertThrows(IllegalArgumentException.class, () -> getEncoder(null));
 }

 @Test
 public void testUtf8Encoding() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder = getEncoder("UTF-8");
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
 assertThrows(UnsupportedEncodingException.class, () -> getEncoder("UnsupportedEncoding"));
 }

 @Test
 public void testUsaciiEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder = getEncoder("US-ASCII");
 assertEquals("US-ASCII", encoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.declaration(writer);
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expected, writer.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullWriterInDeclaration() {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.declaration(null);
 }

 @Test
 public void testTextWithEscapableCharacter() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(writer, "This < example > contains & an &, and a ñ character & #241;.");
 String expected = "This &lt; example &gt; contains &amp; an &amp;, and a &aacute; character & #x00E1;.";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testTextWithNonEscapableCharacter() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(writer, "This < example > contains < > and = signs.");
 String expected = "This <example> contains < and = signs.";
 assertEquals(expected, writer.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithInvalidIndex() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 char[] ch = "example".toCharArray();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(writer, ch, 2, ch.length - 2);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacterUTF8() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(writer, 0xFFFD);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacterUSASCII() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("US-ASCII");
 encoder.text(writer, 0xFFFD);
 }

 @Test(expected = IOException.class)
 public void testTextWithNullCharacterArray() throws IOException, InvalidXMLException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(null, null, 0, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testTextWithNullWriter() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(null, "example".toCharArray(), 0, "example".length());
 }

 @Test(expected = NullPointerException.class)
 public void testTextWithNullCharacterArrayStart() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(new StringWriter(), null, 0, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithNegativeLength() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(writer, "example".toCharArray(), -1, "example".length());
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacterISO8859_1() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("ISO-8859-1");
 encoder.text(writer, 0x00A0);
 }

 @Test
 public void testTextWithValidCharacterISO8859_1() throws IOException, InvalidXMLException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("ISO-8859-1");
 encoder.text(writer, "European currency symbol: &euro;");
 String expected = "European currency symbol: &euro;";
 assertEquals(expected, writer.toString());
 }

 @Test(expected = IOException.class)
 public void testWhitespaceWithNullString() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.whitespace(null, null, 0, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testWhitespaceWithNullCharacterArray() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.whitespace(new StringWriter(), null, 0, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testWhitespaceWithInvalidIndexStart() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 char[] ch = "example".toCharArray();
 encoder.whitespace(writer, ch, -1, ch.length);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testWhitespaceWithInvalidIndexEnd() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 char[] ch = "example".toCharArray();
 encoder.whitespace(writer, ch, 0, -1);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeWithNullName() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 String name = null;
 String value = "example";
 char quotationMark = '\"';
 boolean escapeAmpersands = true;
 encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeWithNullValue() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 String name = "example";
 char quotationMark = '\"';
 boolean escapeAmpersands = true;
 String value = null;
 encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeWithNullQuotationMark() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 String name = "example";
 String value = "example";
 boolean escapeAmpersands = true;
 char quotationMark = '\0';
 encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeWithInvalidQuotationMark() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 StringWriter writer = new StringWriter();
 String name = "example";
 String value = "example";
 boolean escapeAmpersands = true;
 char quotationMark = '\u0001';
 encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
 }

 @Test
 public void testAttribute() throws IOException {
 StringWriter writer = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 String name = "exampleName";
 String value = "exampleValue";
 char quotationMark = '\"';
 boolean escapeAmpersands = true;
 encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
 String expected = "exampleName=\"exampleValue\"";
 assertEquals(expected, writer.toString());
 }

}