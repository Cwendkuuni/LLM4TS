package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setUp() throws Exception {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new StringWriter();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
 XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncoding() {
 try {
 XMLEncoder.getEncoder("ASCII123");
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 XMLEncoder.getEncoder("ASCII123");
 }

 @Test
 public void testGetEncoding() {
 Assert.assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(writer);
 String declaration = writer.toString();
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Assert.assertEquals(expectedDeclaration, declaration);
 }

 @Test
 public void testTextSimple() throws IOException {
 encoder.text(writer, "Hello, World!");
 String text = writer.toString();
 String expectedText = "Hello, World";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testTextWithEscapes() throws IOException {
 char[] ch = new char[10];
 new Random().nextBytes(ch);
 int length = ch.length;
 encoder.text(writer, ch, 0, length, true);
 String text = writer.toString();
 for (int i = 0; i < length; ++i) {
 char c = ch[i];
 if ((c < 63 || c > 127) && (c < 39 || c > 59) && (c < 32 || c > 37) && (c != 38 || encoder._sevenBitEncoding) && (c <= 127 || encoder._sevenBitEncoding) && c != 10 && c != 13 && c != 61) {
 String escaped = Character.isDigit(c) ? "x" + Integer.toHexString((int) c).substring(1) : Character.isWhitespace(c) ? "s" : c == '&' ? "amp;" : c == '<' ? "lt;" : c == '>' ? "gt;" : "'" == c ? "apos;" : "quo;";
 String expectedEscape = Character.isWhitespace(c) ? "" : "&" + escaped;
 String actualEscape = text.substring(writer.getPosition() - encoder.text(writer, new char[]{c}, 0, 1, true).length(), writer.getPosition());
 Assert.assertEquals(expectedEscape, actualEscape);
 }
 text = text.substring(writer.getPosition());
 }
 writer.getPosition(); // Reset the writer position
 Assert.assertEquals(new String(ch), text);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextNegativeLength() throws IOException {
 encoder.text(writer, new char[]{'x'}, -1, 1, true);
 }

 @Test(expected = NullPointerException.class)
 public void testTextNullCharacterArray() throws IOException {
 encoder.text(writer, null, 0, 0, true);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException {
 encoder.text(writer, new char[]{'&'}, 0, 1, true);
 }

 @Test
 public void testTextSingleCharacter() throws IOException {
 char c = 'A';
 encoder.text(writer, c);
 String text = writer.toString();
 String expectedText = Character.isWhitespace(c) ? "" : "&" + Character.isDigit(c) ? "x" + Integer.toHexString((int) c).substring(1) : "amp;" + Character.toString(c).toLowerCase();
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testTextSingleCharacterWithEscapeAmpersands() throws IOException {
 char c = '&';
 encoder.text(writer, c, true);
 String text = writer.toString();
 String expectedText = "&amp;";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testWhitespace() throws IOException {
 String s = " ";
 encoder.whitespace(writer, s);
 String whiteSpace = writer.toString();
 Assert.assertEquals(s, whiteSpace);
 }

 @Test
 public void testWhitespaceNullString() throws IOException {
 encoder.whitespace(writer, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testWhitespaceNegativeLength() throws IOException {
 encoder.whitespace(writer, new char[]{'s'}, -1);
 }

 @Test(expected = NullPointerException.class)
 public void testWhitespaceNullCharacterArray() throws IOException {
 encoder.whitespace(writer, null);
 }

 @Test
 public void testAttributeName() throws IOException {
 encoder.attribute(writer, "name", "value", '\"', true);
 String attribute = writer.toString();
 String expectedAttribute = " name=\"value\"";
 Assert.assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeNameWithQuotationMark() throws IOException {
 encoder.attribute(writer, "name", "value\"value", '\"', true);
 String attribute = writer.toString();
 String expectedAttribute = " name=\"value\"value\"";
 Assert.assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeValue() throws IOException {
 encoder.attribute(writer, "name", "value", '\"', true);
 char[] ch = new char[10];
 new Random().nextBytes(ch);
 int length = ch.length;
 encoder.attribute(writer, "name", new String(ch), '\"', true);
 String attribute = writer.toString();
 for (int i = 0; i < length; ++i) {
 char c = ch[i];
 if ((c < 63 || c > 127) && (c < 40 || c > 59) && (c < 32 || c > 37 || c == 34) && (c != 38 || encoder._sevenBitEncoding) && (c <= 127 || encoder._sevenBitEncoding) && (c != 10 && c != 13 && c != 61)) {
 String escaped = Character.isDigit(c) ? "x" + Integer.toHexString((int) c).substring(1) : Character.isWhitespace(c) ? "s" : c == '&' ? "amp;" : c == '<' ? "lt;" : c == '>' ? "gt;" : "'" == c ? "apos;" : "quo;";
 String expectedEscape = Character.isWhitespace(c) ? "" : "&" + escaped;
 String actualEscape = attribute.substring(writer.getPosition() - encoder.attribute(writer, new String(new char[]{c}), "name", null, '\"', true).length(), writer.getPosition());
 Assert.assertEquals(expectedEscape, actualEscape);
 }
 attribute = attribute.substring(writer.getPosition());
 }
 writer.getPosition(); // Reset the writer position
 Assert.assertEquals(new String(ch), attribute.substring(0, attribute.indexOf("name=\"") + "name".length() + 1));
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeNullName() throws IOException {
 encoder.attribute(writer, null, "value", '\"', true);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeNullValue() throws IOException {
 encoder.attribute(writer, "name", null, '\"', true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeNullQuotationMark() throws IOException {
 encoder.attribute(writer, "name", "value", (char) 0, true);
 }

 @Test
 public void testAttributeQuotationMarkDifferent() throws IOException {
 encoder.attribute(writer, "name", "value", '\'', true);
 String attribute = writer.toString();
 String expectedAttribute = " name='value'";
 Assert.assertEquals(expectedAttribute, attribute);
 }
}