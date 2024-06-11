import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

public class XMLEncoderTest {

 private static final String UTF_8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
 private static final String EMPTY_STRING = "";
 private static final String ASCII_TEXT = "This is an ASCII text.";
 private static final String UTF_8_TEXT = "This is a UTF-8 text with special characters: Price €123,456.78.";
 private static final String ISO_8859_1_TEXT = "This is an ISO-8859-1 text with special characters: Price €123,456.78.";
 private static final String INVALID_XML_TEXT = "<invalid>";
 private static final char QUOTE = '\'';
 private static final char APOSTROPHE = '\'';
 private static final char LESS_THAN = '<';
 private static final char GREATER_THAN = '>';
 private static final char AMPERSAND = '&';
 private static final char ESCAPED_AMPERSAND = '&amp;';
 private static final char ESCAPED_QUOTE = '&quot;';
 private static final char ESCAPED_APOSTROPHE = '&apos;';
 private static final char ESCAPED_LESS_THAN = '&lt;';
 private static final char ESCAPED_GREATER_THAN = '&gt;';

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     Assert.assertEquals(UTF_8_ENCODING, XMLEncoder.getEncoder(UTF_8_ENCODING).getEncoding());
     Assert.assertEquals(US_ASCII_ENCODING, XMLEncoder.getEncoder(US_ASCII_ENCODING).getEncoding());
     Assert.assertEquals(ISO_8859_1_ENCODING, XMLEncoder.getEncoder(ISO_8859_1_ENCODING).getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     XMLEncoder.getEncoder("UnsupportedEncoding");
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.declaration(out);
     String declaration = out.toString();
     String expected = "<?xml version=\"1.0\" encoding=\"" + UTF_8_ENCODING + "\"?>";
     Assert.assertEquals(expected, declaration);
 }

 @Test(expected = NullPointerException.class)
 public void testDeclarationNullWriter() {
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.declaration(null);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.text(out, ASCII_TEXT);
     String result = out.toString();
     String expected = ASCII_TEXT;
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testTextSpecialChars() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.text(out, UTF_8_TEXT);
     String result = out.toString();
     String expected = "This is a UTF-8 text with special characters: Price &#x20AC;123,456.78.";
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testTextInvalidChar() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     byte[] invalidChar = { (byte) 0xFF };
     encoder.text(out, invalidChar, 0, 1);
     Assert.assertThrows(InvalidXMLException.class, () -> out.toString());
 }

 @Test
 public void testTextInvalidXML() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.text(out, INVALID_XML_TEXT);
     Assert.assertThrows(InvalidXMLException.class, () -> out.toString());
 }

 @Test
 public void testTextWithQuotationMark() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String text = "Price \"Product\"";
     encoder.text(out, text, true, QUOTE);
     String result = out.toString();
     String expected = "Price &#34;Product&#34;";
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testTextWithApostrophe() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String text = "Price 'Product'";
     encoder.text(out, text, false, APOSTROPHE);
     String result = out.toString();
     String expected = "Price &#39;Product&#39;";
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testTextWithEscapeAmpersand() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String text = "Price &Product";
     encoder.text(out, text, true, AMPERSAND);
     String result = out.toString();
     String expected = "Price &amp;Product";
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testTextWithNoEscapeAmpersand() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String text = "Price &Product";
     encoder.text(out, text, false, AMPERSAND);
     String result = out.toString();
     Assert.assertEquals("Price &Product", result);
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String whitespace = "   ";
     encoder.whitespace(out, whitespace);
     String result = out.toString();
     String expected = whitespace;
     Assert.assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testWhitespaceNullWriter() {
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     encoder.whitespace(null, EMPTY_STRING.toCharArray(), 0, EMPTY_STRING.length());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testWhitespaceInvalidIndex() {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String whitespace = "   ";
     encoder.whitespace(out, whitespace.toCharArray(), 3, whitespace.length());
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String name = "class";
     String value = "MyClass";
     encoder.attribute(out, name, value, QUOTE, true);
     String result = out.toString();
     String expected = "class=\"MyClass\"";
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testAttributeSpecialChars() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String name = "class";
     String value = "MyClass & Product";
     encoder.attribute(out, name, value, APOSTROPHE, true);
     String result = out.toString();
     String expected = "class=\"MyClass &amp; Product\"";
     Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeInvalidQuotationMark() {
     String name = "class";
     String value = "MyClass";
     XMLEncoder.attribute(null, name, value, '\u0000', true);
 }

 @Test(expected = IOException.class)
 public void testAttributeNullWriter() {
     String name = "class";
     String value = "MyClass";
     XMLEncoder.attribute(null, name, value, QUOTE, true);
 }

 @Test
 public void testAttributeEmptyString() throws IOException, InvalidXMLException, NullPointerException {
     final Writer out = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     String name = "class";
     String value = EMPTY_STRING;
     encoder.attribute(out, name, value, QUOTE, true);
     String result = out.toString();
     String expected = "class=\"\"";
     Assert.assertEquals(expected, result);
 }
}