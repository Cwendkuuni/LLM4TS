import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 Assert.assertEquals(UTF8_ENCODING, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("encoding == null", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() throws IllegalArgumentException {
 try {
 XMLEncoder encoder = XMLEncoder.getEncoder("ABCDEFG");
 fail("Should throw UnsupportedEncodingException");
 } catch (UnsupportedEncodingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 encoder.declaration(out);
 String xmlDeclaration = out.toString();
 String expectedXMLDeclaration = "<?xml version=\"1.0\" encoding=\"" + UTF8_ENCODING + "\"?>";
 XMLChecker.checkS(xmlDeclaration, 0, xmlDeclaration.length());
 Assert.assertEquals(expectedXMLDeclaration, xmlDeclaration);
 }

 @Test(expected = NullPointerException.class)
 public void testDeclarationNullWriter() {
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 try {
 encoder.declaration(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = IOException.class)
 public void testDeclarationIOException() throws NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 try {
 encoder.declaration(out);
 out.flush(); // Throws IOException after write
 fail("Should throw IOException");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test
 public void testText() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String text = "Hello, World!";
 encoder.text(out, text, false);
 String xmlText = out.toString();
 String expectedXMLText = "Hello, World!\r\n";
 XMLChecker.checkS(xmlText, 0, xmlText.length());
 Assert.assertEquals(expectedXMLText, xmlText);
 }

 @Test
 public void testTextEscapeAmpersands() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String text = "<test>&hello</test>";
 encoder.text(out, text, true);
 String xmlText = out.toString();
 String expectedXMLText = "&lt;test&gt;&amp;hello&lt;/test&gt;\r\n";
 XMLChecker.checkS(xmlText, 0, xmlText.length());
 Assert.assertEquals(expectedXMLText, xmlText);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextInvalidIndex() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 char[] ch = "Hello, World!".toCharArray();
 try {
 encoder.text(out, ch, 12, 3);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidChar() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 char[] ch = "Hello, World!".toCharArray();
 try {
 encoder.text(out, ch, 0, ch.length, false);
 ch[0] = '\u00FD'; // Invalid character
 out.flush(); // Throws IOException after write
 fail("Should throw InvalidXMLException");
 } catch (InvalidXMLException e) {
 // Expected exception
 }
 }

 @Test
 public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String whitespace = " ";
 encoder.whitespace(out, whitespace);
 String xmlWhitespace = out.toString();
 String expectedXMLWhitespace = " \r\n";
 XMLChecker.checkS(xmlWhitespace, 0, xmlWhitespace.length());
 Assert.assertEquals(expectedXMLWhitespace, xmlWhitespace);
 }

 @Test
 public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String name = "test";
 String value = "Hello, World!";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, false);
 String xmlAttribute = out.toString();
 String expectedXMLAttribute = "<" + name + ">" + value + "</" + name + ">\r\n";
 XMLChecker.checkS(xmlAttribute, 0, xmlAttribute.length());
 Assert.assertEquals(expectedXMLAttribute, xmlAttribute);
 }

 @Test
 public void testAttributeQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String name = "test";
 String value = "Hello, World!";
 char quotationMark = '\'';
 encoder.attribute(out, name, value, quotationMark, false);
 String xmlAttribute = out.toString();
 String expectedXMLAttribute = "<" + name + ">" + value + "</" + name + ">\r\n";
 XMLChecker.checkS(xmlAttribute, 0, xmlAttribute.length());
 Assert.assertEquals(expectedXMLAttribute, xmlAttribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeInvalidQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
 String name = "test";
 String value = "Hello, World!";
 char quotationMark = 'Z'; // Invalid character
 try {
 encoder.attribute(out, name, value, quotationMark, false);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}