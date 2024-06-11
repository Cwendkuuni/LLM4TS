package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testGetEncoder() {
     String encoding = "UTF-8";
     XMLEncoder expectedEncoder = new XMLEncoder(encoding);
     XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
     Assert.assertSame(expectedEncoder, actualEncoder);
 }

 @Test
 public void testGetEncoderInvalidEncoding() {
     String encoding = "INVALID_ENCODING";
     try {
         XMLEncoder.getEncoder(encoding);
         Assert.fail("Should have thrown UnsupportedEncodingException");
     } catch (UnsupportedEncodingException e) {
         // Expected
     }
 }

 @Test
 public void testGetEncoderNullEncoding() {
     String encoding = null;
     try {
         XMLEncoder.getEncoder(encoding);
         Assert.fail("Should have thrown IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testDeclaration() throws IOException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     encoder.declaration(writer);
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     String actualDeclaration = writer.toString();
     Assert.assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String text = "The quick brown fox jumps over the lazy dog.";
     encoder.text(writer, text, false);
     String expectedText = "The quick brown fox jumps over the lazy dog.";
     String actualText = writer.toString();
     Assert.assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithEscaping() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String text = "& < > ' \" ";
     encoder.text(writer, text.toCharArray(), 0, text.length(), true);
     String expectedText = "&amp; &lt; &gt; &apos; &quot;";
     String actualText = writer.toString();
     Assert.assertEquals(expectedText, actualText);
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     char invalidCharacter = '\u0001';
     try {
         encoder.text(writer, invalidCharacter);
         Assert.fail("Should have thrown InvalidXMLException");
     } catch (InvalidXMLException e) {
         // Expected
     }
 }

 @Test
 public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("US-ASCII");
     writer = new StringWriter();
     String text = "The quick brown fox jumps over the lazy dog.";
     encoder.text(writer, text.toCharArray(), 0, text.length());
     String expectedText = "The quick brown fox jumps over the lazy dog.";
     String actualText = writer.toString();
     Assert.assertEquals(expectedText, actualText);
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String whitespace = " \t\n\r\f";
     encoder.whitespace(writer, whitespace);
     String expectedWhitespace = " \t\n\r\f";
     String actualWhitespace = writer.toString();
     Assert.assertEquals(expectedWhitespace, actualWhitespace);
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String name = "name";
     String value = "The quick brown fox jumps over the lazy dog.";
     char quotationMark = '\"';
     encoder.attribute(writer, name, value, quotationMark, false);
     String expectedAttribute = " name=\"The quick brown fox jumps over the lazy dog.\"";
     String actualAttribute = writer.toString();
     Assert.assertEquals(expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithEscaping() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String name = "name";
     String value = "& < > ' &amp; \"";
     char quotationMark = '\'';
     encoder.attribute(writer, name, value, quotationMark, true);
     String expectedAttribute = " name=\"&amp; &lt; &gt; ' &amp; &quot;\"";
     String actualAttribute = writer.toString();
     Assert.assertEquals(expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithInvalidQuotationMark() throws IOException, InvalidXMLException {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String name = "name";
     String value = "The quick brown fox jumps over the lazy dog.";
     char quotationMark = '^';
     try {
         encoder.attribute(writer, name, value, quotationMark, false);
         Assert.fail("Should have thrown IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }
}