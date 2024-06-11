package org.znerd.xmlenc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setUp() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new StringWriter();
 }

 @After
 public void tearDown() {
 writer.getBuffer().setLength(0);
 }

 @Test
 public void testGetEncoder() {
 String encoding = "UTF-8";
 XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder encoder = XMLEncoder.getEncoder(null);
 assertNull(encoder);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 String encoding = "ABC-123";
 try {
 XMLEncoder.getEncoder(encoding);
 } catch (IllegalArgumentException e) {
 // expected
 }
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(encoding);
 } catch (UnsupportedEncodingException e) {
 // expected
 }
 assertNull(encoder);
 }

 @Test
 public void testDeclareEncoding() throws IOException {
 encoder.declare(writer);
 String xmlDecl = writer.toString();
 String expectedXmlDecl = "<?xml version=\"1.0\" encoding=\"" + encoder.getEncoding() + "\"?>";
 assertEquals(expectedXmlDecl, xmlDecl);
 }

 @Test
 public void testEncodeTextSimple() throws IOException, InvalidXMLException {
 String text = "The quick brown fox jumps over the lazy dog";
 encoder.text(writer, text, false);
 String encodedText = writer.toString();
 String expectedEncodedText = "The quick brown fox jumps over the lazy dog";
 assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testEncodeTextWithAmpersand() throws IOException, InvalidXMLException {
 String text = "The text contains &amp;, &apos; and &quot;";
 encoder.text(writer, text, false);
 String encodedText = writer.toString();
 String expectedEncodedText = "The text contains &amp;, &apos; and &quot;";
 assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testEncodeTextWithSpecialChars() throws IOException, InvalidXMLException {
 String text = "<>\"'&";
 encoder.text(writer, text, false);
 String encodedText = writer.toString();
 String expectedEncodedText = "&lt;>&gt;&quot;&apos;&amp;";
 assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testEncodeTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
 String text = "The text contains ÇáéíöúÁÉÍÖÚ";
 encoder = XMLEncoder.getEncoder("ASCII");
 encoder.text(writer, text, false);
 String encodedText = writer.toString();
 String expectedEncodedText = "The text contains &C3;&A1;&E1;&O3;&U0;&A3;&C3;&A9;&O3;&U0;&A5;&O3;&U0;&A9;&O3;&U0;&A3;&C3;&A1;&C3;&A9;&O3;&U0;&A3;&C3;&AA;&O3;&U0;&A9;&O3;&U0;&AA";
 assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testEncodeTextWithEscapedAmpersands() throws IOException, InvalidXMLException {
 String text = "The text contains &amp;amp;, &apos;apos; and &quot;quot;";
 encoder.text(writer, text, true);
 String encodedText = writer.toString();
 String expectedEncodedText = "The text contains &amp;amp;&apos;apos;&quot;quot;";
 assertEquals(expectedEncodedText, encodedText);
 }

 @Test(expected = NullPointerException.class)
 public void testEncodeTextWithNullText() throws IOException, InvalidXMLException {
 encoder.text(writer, null, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testEncodeTextWithNegativeLength() throws IOException, InvalidXMLException {
 char[] ch = new char[10];
 encoder.text(writer, ch, -1, 5, false);
 }

 @Test(expected = InvalidXMLException.class)
 public void testEncodeTextWithInvalidChar() throws IOException {
 char[] ch = new char[1];
 ch[0] = 0x01;
 encoder.text(writer, ch, 0, 1, false);
 }

 @Test
 public void testEncodeWhitespace() throws IOException {
 String whitespace = "   \t\r\n";
 encoder.whitespace(writer, whitespace);
 String encodedWhitespace = writer.toString();
 String expectedEncodedWhitespace = "   \t\r\n";
 assertEquals(expectedEncodedWhitespace, encodedWhitespace);
 }

 @Test
 public void testEncodeAttribute() throws IOException {
 String name = "testName";
 String value = "The value contains &amp;, &apos; and &quot;";
 char quotationMark = '\"';
 encoder.attribute(writer, name, value, quotationMark, false);
 String expectedAttribute = "<" + name + ">" + value + "</" + name + ">";
 String encodedAttribute = writer.toString();
 assertEquals(expectedAttribute, encodedAttribute);
 }

 @Test
 public void testEncodeAttributeWithEscapedAmpersands() throws IOException {
 String name = "testName";
 String value = "The value contains &amp;amp;, &apos;apos; and &quot;quot;";
 char quotationMark = '\"';
 encoder.attribute(writer, name, value, quotationMark, true);
 String expectedAttribute = "<" + name + ">" + value.replace("&amp;", "&amp;amp;")
 + "</" + name + ">";
 String encodedAttribute = writer.toString();
 assertEquals(expectedAttribute, encodedAttribute);
 }

 @Test(expected = NullPointerException.class)
 public void testEncodeAttributeWithNullName() throws IOException {
 encoder.attribute(writer, null, "", '\"', false);
 }

 @Test(expected = NullPointerException.class)
 public void testEncodeAttributeWithNullValue() throws IOException {
 encoder.attribute(writer, "testName", null, '\"', false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeAttributeWithInvalidQuotationMark() throws IOException {
 encoder.attribute(writer, "testName", "", 'z', false);
 }
}