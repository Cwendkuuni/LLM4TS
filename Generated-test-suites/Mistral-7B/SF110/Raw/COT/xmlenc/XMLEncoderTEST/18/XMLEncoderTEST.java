package org.znerd.xmlenc.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class XMLEncoderTest {

 private StringWriter out;
 private XMLEncoder encoder;

 @Before
 public void setUp() {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 }

 @Test
 public void testGetEncoder() {
 String encoding = "UTF-8";
 XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ISO-8859-10"));
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(out);
 String xmlDeclaration = out.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test(expected = NullPointerException.class)
 public void testDeclarationNullWriter() {
 encoder.declaration(null);
 assertNull(out.toString());
 }

 @Test(expected = IOException.class)
 public void testDeclarationIOException() throws IOException {
 out = new StringWriter();
 encoder = new XMLEncoder("UTF-8"); // Not initialized properly
 encoder.declaration(out);
 assertThrows(IOException.class, () -> out.toString());
 }

 @Test
 public void testTextNullText() throws IOException, InvalidXMLException {
 encoder.text(out, null, 0, 0, false);
 assertEquals("", out.toString());
 }

 @Test
 public void testTextEmptyText() throws IOException, InvalidXMLException {
 encoder.text(out, "", false);
 assertEquals("", out.toString());
 }

 @Test
 public void testTextSimpleText() throws IOException, InvalidXMLException {
 encoder.text(out, "abc", false);
 assertEquals("abc", out.toString());
 }

 @Test
 public void testTextEscapedText() throws IOException, InvalidXMLException {
 encoder.text(out, "&<'>", false);
 assertEquals("&amp;&lt;&gt;", out.toString());
 }

 @Test
 public void testTextEscapedTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
 encoder.text(out, "&<'>", true);
 assertEquals("&amp;&amp;lt;&amp;gt;", out.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextIndexOutOfBounds() throws IOException, InvalidXMLException {
 char[] text = "abcde".toCharArray();
 encoder.text(out, text, 5, text.length - 1, false);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidXMLChar() throws IOException, InvalidXMLException {
 encoder = new XMLEncoder("US-ASCII");
 encoder.text(out, 'ñ', false);
 }

 @Test
 public void testTextInvalidXMLCharEscaped() throws IOException, InvalidXMLException {
 encoder = new XMLEncoder("US-ASCII");
 encoder.text(out, 'ñ', true);
 assertEquals("&amp;#241;", out.toString());
 }

 @Test
 public void testTextLF() throws IOException, InvalidXMLException {
 char c = '\n';
 encoder.text(out, c);
 assertEquals("\n", out.toString());
 }

 @Test
 public void testTextCR() throws IOException, InvalidXMLException {
 char c = '\r';
 encoder.text(out, c);
 assertEquals("\r", out.toString());
 }

 @Test
 public void testTextWhitespace() throws IOException, InvalidXMLException {
encoder.whitespace(out, " abc");
 assertEquals(" abc", out.toString());
 }

 @Test
 public void testTextAttribute() throws IOException {
 encoder.attribute(out, "name", "value", '"', false);
 assertEquals("name=\"value\"", out.toString());
 }

 @Test
 public void testTextAttributeQuotationMark() throws IOException {
 encoder.attribute(out, "name", "value\"value", '"', false);
 assertEquals("name=\"value\"value\"", out.toString());
 }

 @Test
 public void testTextAttributeEscapeAmpersands() throws IOException {
 encoder.attribute(out, "name", "value&value", '"', false);
 assertEquals("name=\"value&amp;value\"", out.toString());
 }

 @Test
 public void testTextAttributeEscapeQuotationMark() throws IOException {
 encoder.attribute(out, "name", "value\"&value", '"', false);
 assertEquals("name=\"value&quot;amp;value\"", out.toString());
 }

 @Test
 public void testTextAttributeDoubleQuotationMark() throws IOException {
 encoder.attribute(out, "name", "value\"value", '"', true);
 assertEquals("name=\"value\"value\"", out.toString());
 }

 @After
 public void tearDown() throws IOException {
 out = null;
 }

}