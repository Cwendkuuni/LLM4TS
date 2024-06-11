package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.UnsupportedEncodingException;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter stringWriter;
 private ByteArrayOutputStream byteArrayOutputStream;

 @Test
 public void testGetEncoder() {
     encoder = XMLEncoder.getEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ABC"));
 }

 @Test
 public void testDeclaration() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.declaration(stringWriter);
     assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", stringWriter.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testDeclarationNullWriter() {
     encoder = XMLEncoder.getEncoder("UTF-8");
     assertThrows(NullPointerException.class, () -> encoder.declaration(null));
 }

 @Test
 public void testText() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, "Hello World!");
     assertEquals("Hello World!\n", stringWriter.toString());
 }

 @Test
 public void testTextEscaped() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, "&<>'\"", false);
     assertEquals("&amp;&lt;&gt;&apos;&quot;\n", stringWriter.toString());
 }

 @Test
 public void testTextEscapedSevenBit() throws IOException {
     encoder = XMLEncoder.getEncoder("ASCII");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, "&<>'\"", false);
     assertEquals("&amp;amp;&lt;lt;&gt;gt;&apos;apos;&quot;quot;\n", stringWriter.toString());
 }

 @Test
 public void testTextEscapedAmpersands() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, "abc&def", true);
     assertEquals("abc&amp;def\n", stringWriter.toString());
 }

 @Test
 public void testTextSingleCharacter() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, '&');
     encoder.text(stringWriter, '\u00A0');
     encoder.text(stringWriter, '\u00E5');
     assertEquals("&amp;&#x00A0;&#x00E5;\n", stringWriter.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextNegativeIndex() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     byteArrayOutputStream = new ByteArrayOutputStream();
     encoder.text(stringWriter, new char[]{'a'}, -1, 1);
     assertThrows(IndexOutOfBoundsException.class, () -> encoder.text(stringWriter, new char[]{'a'}, -1, 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextNegativeLength() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     byteArrayOutputStream = new ByteArrayOutputStream();
     encoder.text(stringWriter, new char[]{'a'}, 1, -1);
     assertThrows(IndexOutOfBoundsException.class, () -> encoder.text(stringWriter, new char[]{'a'}, 1, -1));
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, '\u0080');
     assertThrows(InvalidXMLException.class, () -> encoder.text(stringWriter, '\u0080'));
 }

 @Test
 public void testTextInvalidCharacterSevenBit() throws IOException {
     encoder = XMLEncoder.getEncoder("ASCII");
     stringWriter = new StringWriter();
     encoder.text(stringWriter, '\u0080');
     assertEquals("&amp;#x58;\n", stringWriter.toString());
 }

 @Test
 public void testWhitespace() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.whitespace(stringWriter, " Hello World ");
     assertEquals(" Hello World \n", stringWriter.toString());
 }

 @Test
 public void testAttribute() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", "value & < > '\"", '\"');
     assertEquals(" name=\"value &amp; &lt; &gt; &apos;&quot;\n", stringWriter.toString());
 }

 @Test
 public void testAttributeQuoteSingle() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", "value'", '\'');
     assertEquals(" name=\"value'\n", stringWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeQuoteInvalid() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     assertThrows(IllegalArgumentException.class, () -> encoder.attribute(stringWriter, "name", "value", 'Z'));
 }

 @Test
 public void testAttributeEscaped() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", "value & < > '\"", '\"');
     assertEquals(" name=\"value &amp; &lt; &gt; &apos;&quot;\n", stringWriter.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testAttributeInvalidCharacter() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", "value\u0080", '\"');
     assertThrows(InvalidXMLException.class, () -> encoder.attribute(stringWriter, "name", "value\u0080", '\"'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAttributeNegativeLength() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", "", 1);
     assertThrows(IndexOutOfBoundsException.class, () -> encoder.attribute(stringWriter, "name", "", 1));
 }

 @Test
 public void testAttributeNullValue() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     stringWriter = new StringWriter();
     encoder.attribute(stringWriter, "name", null, '\"');
     assertEquals(" name=\"\"\n", stringWriter.toString());
 }

}