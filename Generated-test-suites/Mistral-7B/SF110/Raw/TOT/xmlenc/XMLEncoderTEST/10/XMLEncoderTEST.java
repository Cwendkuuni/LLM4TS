import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
 private static final String IS_NOT_XML_ENCODING = "IS_NOT_XML_ENCODING";

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder1 = XMLEncoder.getEncoder(UTF8_ENCODING);
 XMLEncoder encoder2 = XMLEncoder.getEncoder(US_ASCII_encoding);
 XMLEncoder encoder3 = XMLEncoder.getEncoder(ISO_8859_1_ENCODING);
 XMLEncoder encoder4 = XMLEncoder.getEncoder(IS_NOT_XML_ENCODING);

 Assert.assertSame(UTF8_ENCODING, encoder1.getEncoding());
 Assert.assertSame(US_ASCII_ENCODING, encoder2.getEncoding());
 Assert.assertSame(ISO_8859_1_ENCODING, encoder3.getEncoding());
 Assert.assertNull(encoder4);
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 encoder.declaration(out);
 String xmlDeclaration = out.toString();
 Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testTextBasic() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String text = "Hello, world!";
 encoder.text(out, text, false);
 String encodedText = out.toString();
 Assert.assertEquals("<Hello,world/>", encodedText);
 }

 @Test
 public void testTextWithEscapes() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String text = "<Hello&World!>";
 encoder.text(out, text.toCharArray(), 0, text.length(), true);
 String encodedText = out.toString();
 Assert.assertEquals("<Hello&amp;World!>", encodedText);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 char[] ch = {(char) 0x10FF};
 encoder.text(out, ch, 0, 1);
 }

 @Test
 public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String whitespace = "   \t \r \n";
 encoder.whitespace(out, whitespace);
 String encodedWhitespace = out.toString();
 Assert.assertEquals("   \t \r \n", encodedWhitespace);
 }

 @Test
 public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String name = "xml:lang";
 String value = "en";
 char quotationMark = '\'';
 encoder.attribute(out, name, value, quotationMark, true);
 String expectedAttribute = "<xml:lang>'en'/>";
 String attribute = out.toString();
 Assert.assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeDoubleQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String name = "xml:lang";
 String value = "fr";
 char quotationMark = '"';
 encoder.attribute(out, name, value, quotationMark, true);
 String expectedAttribute = "<xml:lang>" + XMLChecker.Q + "fr" + XMLChecker.Q + "</xml:lang>";
 String attribute = out.toString();
 Assert.assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
 Writer out = new StringWriter();
 XMLEncoder encoder = new XMLEncoder("UTF-8");
 String name = "xml:lang";
 String value = "으";
 char quotationMark = '\'';
 encoder.attribute(out, name, value, quotationMark, true);
 }
}