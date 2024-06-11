import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

 private final String encodingUtf8 = "UTF-8";
 private final String encodingUscii = "US-ASCII";
 private final String encodingInvalid = "InvalidEncoding";
 private final String encodingNull = null;
 private final Writer writer = new StringWriter();

 @Test
 public void testGetEncoderUtf8() {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 Assert.assertNotNull(encoder);
 Assert.assertEquals(encodingUtf8, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(encodingNull);
 }
 catch (IllegalArgumentException ex) {
 Assert.assertEquals("encoding == null", ex.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderInvalidEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(encodingInvalid);
 }
 catch (UnsupportedEncodingException ex) {
 Assert.assertEquals(encodingInvalid, ex.getMessage());
 }
 }

 @Test
 public void testDeclarationUtf8() throws IOException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.declaration(writer);
 String declaration = writer.toString();
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + encodingUtf8 + "\"?>";
 Assert.assertEquals(expectedDeclaration, declaration);
 }

 @Test
 public void testTextUtf8() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.text(writer, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", -1, -1, false);
 String text = writer.toString();
 String expectedText = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testTextUtf8EscapeAmpersands() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.text(writer, "&", -1, -1, true);
 String text = writer.toString();
 String expectedText = "&amp;";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testTextUscii() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUscii);
 encoder.text(writer, "abc", -1, -1, false);
 String text = writer.toString();
 String expectedText = "abc";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testTextUsciiEscapeAmpersands() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUscii);
 encoder.text(writer, "&", -1, -1, true);
 String text = writer.toString();
 String expectedText = "&amp;";
 Assert.assertEquals(expectedText, text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTextNull() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.text(writer, null, -1, -1, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextNegativeLength() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 char[] ch = new char[10];
 encoder.text(writer, ch, -1, 10, false);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.text(writer, new char[] { 0xFF }, -1, 1, false);
 }

 @Test
 public void testAttributeQuotationMarkDoubleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);
 encoder.attribute(writer, "name", "value\"", '\"', false);
 String attribute = writer.toString();
 String expectedAttribute = " name=\"value\"";
 Assert.assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeQuotationMarkSingleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8);encoder.attribute(writer, "name", "value'", '\'', false); String attribute = writer.toString(); String expectedAttribute = " name='value'"; Assert.assertEquals(expectedAttribute, attribute); }

 @Test
 public void testAttributeEscapeAmpersandsDoubleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", "value&amp;", '\"', false); String attribute = writer.toString(); String expectedAttribute = " name=\"value&amp;\""; Assert.assertEquals(expectedAttribute, attribute); }

 @Test
 public void testAttributeEscapeAmpersandsSingleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", "value&amp;", '\'', false); String attribute = writer.toString(); String expectedAttribute = " name='value&amp;'"; Assert.assertEquals(expectedAttribute, attribute); }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeNullName() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, null, "value", '\"', false); }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeNullValue() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", null, '\"', false); }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeInvalidQuotationMark() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", "value", '\u0001', false); }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeEscapeQuotationMarkDoubleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", "value\\\"", '\"', false); }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeEscapeQuotationMarkSingleQuote() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); encoder.attribute(writer, "name", "value\\'", '\'', false); }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
 XMLEncoder encoder = XMLEncoder.getEncoder(encodingUtf8); char[] ch = new char[10]; String whitespace = new String(ch); encoder.whitespace(writer, ch, 0, ch.length); String expectedWhitespace = whitespace; String actualWhitespace = writer.toString(); Assert.assertEquals(expectedWhitespace, actualWhitespace); }

}