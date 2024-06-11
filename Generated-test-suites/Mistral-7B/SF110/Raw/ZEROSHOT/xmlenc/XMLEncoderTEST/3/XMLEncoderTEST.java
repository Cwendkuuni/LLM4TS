package org.znerd.xmlenc;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testGetEncoder() throws Exception {
     String encoding = "UTF-8";
     XMLEncoder expectedEncoder = new XMLEncoder(encoding);
     XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
     assertSame(expectedEncoder, actualEncoder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     XMLEncoder.getEncoder("ABC123");
 }

 @Test
 public void testGetEncoding() {
     encoder = new XMLEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.declaration(writer);
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     String actualDeclaration = writer.toString();
     assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testTextWithAscii() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "Hello World", false);
     String expectedText = "Hello World";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithSpecialChars() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "&<>'\"", false);
     String expectedText = "&amp;&lt;>&apos;&gt;&quot;";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithSpecialCharsEscapeAmpersands() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "&<>'\"", true);
     String expectedText = "&amp;amp;&lt;&gt;&apos;&amp;apos;&quot;";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithNonAscii() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\u00E9\u00E0", false);
     String expectedText = "\u00E9\u00E0";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithNonAsciiEscape() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\u00E9\u00E0", true);
     String expectedText = "&amp;\u00E9;&amp;\u00E0";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithNonAsciiSevenBit() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.text(writer, "\u00E9\u00E0", false);
     String expectedText = "\u00E9\u00E0";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithNonAsciiSevenBitEscape() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("ISO-8859-1");
     encoder.text(writer, "\u00E9\u00E0", true);
     String expectedText = "&amp;#231;&amp;#224;";
     String actualText = writer.toString();
     assertEquals(expectedText, actualText);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws Exception {
     writer = new StringWriter();    
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\u007F", false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextInvalidIndex() throws Exception {
     writer = new StringWriter();    
     String text = "test";
     char[] ch = new char[1];
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, ch, 2, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testTextNullWriter() throws Exception {
     encoder = new XMLEncoder("UTF-8");
     encoder.text(null, "test".toCharArray(), 0, "test".length(), false);
 }

 @Test(expected = IOException.class)
 public void testTextIOException() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     writer.close();
     encoder.text(writer, "test".toCharArray(), 0, "test".length(), false);
 }

 @Test
 public void testWhitespace() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.whitespace(writer, "test");
     String expectedWhitespace = "test";
     String actualWhitespace = writer.toString();
     assertEquals(expectedWhitespace, actualWhitespace);
 }

 @Test
 public void testAttribute() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "test \"test\"", '"', false);
     String expectedAttribute = " test=\"test \\\"test\\\"\"";
     String actualAttribute = writer.toString();
     assertEquals(expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithAscii() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "testAscii", ' ', false);
     String expectedAttribute = " test=\"testAscii\"";
     String actualAttribute = writer.toString();
     assertEquals(expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithSpecialChars() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "test&<>'\"", '"', false);
     String expectedAttribute = " test=\"test&amp;&lt;&gt;&apos;&quot;\"";
     String actualAttribute = writer.toString();
     assertEquals(expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithSpecialCharsEscapeAmpersands() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "test&<>'\"", '"', true);
     String expectedAttribute = " test=\"test&amp;amp;&lt;&gt;&apos;&amp;apos;&quot;\"";
     String actualAttribute = writer.toString();
     assertEquals(expectedAttribute, actualAttribute);
 }

 @Test(expected = InvalidXMLException.class)
 public void testAttributeInvalidCharacter() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "\u007Ftest", '"', false);
 }

 @Test
 public void testAttributeNullName() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, null, "test", '"', false);
 }

 @Test
 public void testAttributeNullValue() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", null, '"', false);
 }

 @Test
 public void testAttributeNullQuotationMark() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "test", "test", (char) 0, false);
 }

 @Test
 public void testAttributeNullWriter() throws Exception {
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(null, "test", "test".toCharArray(), ' ', false);
 }

 @Test
 public void testAttributeIOException() throws Exception {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     writer.close();
     encoder.attribute(writer, "test", "test".toCharArray(), ' ', false);
 }
}