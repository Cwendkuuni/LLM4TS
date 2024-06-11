package org.znerd.xmlenc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     String encoding = "UTF-8";
     encoder = XMLEncoder.getEncoder(encoding);
     assertEquals(encoding, encoder.getEncoding());
 }

 @Test
 public void testInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
     String encoding = null;
     try {
         encoder = XMLEncoder.getEncoder(encoding);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testInvalidEncoding2() throws IllegalArgumentException, UnsupportedEncodingException {
     String encoding = "INVALID_ENCODING";
     try {
         encoder = XMLEncoder.getEncoder(encoding);
         fail("UnsupportedEncodingException expected");
     } catch (UnsupportedEncodingException e) {
         // expected
     }
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
     Writer out = new StringWriter();
     encoder.declaration(out);
     String expected = "<?xml version=\"1.0\" encoding=\"" + encoder.getEncoding() + "\"?>";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testText() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] text = "This is a test < & >".toCharArray();
     encoder.text(out, text, 0, text.length, true);
     String expected = "This is a test &lt; &amp; &gt;";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testTextWithEscapedAmpersands() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] text = "This is a test &amp; &lt; &gt;".toCharArray();
     encoder.text(out, text, 0, text.length, false);
     String expected = "This is a test &amp; &lt; &gt;";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testTextWithNonAscii() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] text = "The character é should be encoded as &#233;".toCharArray();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(out, text, 0, text.length, true);
     String expected = "The character &#233; should be encoded as &#233;";
     assertEquals(expected, out.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] text = "The character ï is not valid.".toCharArray();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(out, text, 0, text.length, true);
 }

 @Test
 public void testTextWithInvalidCharacterEscaped() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] text = "The character &#x00ff; is not valid.".toCharArray();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(out, text, 0, text.length, true);
     String expected = "The character &#x00ff; is not valid.";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     char[] whitespace = " \t\r\n".toCharArray();
     encoder.whitespace(out, whitespace, 0, whitespace.length);
     String expected = " \t\r\n";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &amp; < \" >";
     encoder.attribute(out, name, value, '"', true);
     String expected = " name=\"value &amp; &lt; &amp; &gt; \"";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributeWithEscapedQuotationMarks() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &quot; &amp; &lt; &gt; &apos;";
     encoder.attribute(out, name, value, '\'', true);
     String expected = " name=\"value &quot; &amp; &lt; &gt; &apos;\"";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributeWithQuotationMarkInValue() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &quot;value&quot;";
     encoder.attribute(out, name, value, '"', true);
     String expected = " name=\"value &quot;value&quot;\"";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributeWithQuotationMarkInValueEscaped() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &amp;quot;value&amp;quot;";
     encoder.attribute(out, name, value, '"', false);
     String expected = " name=\"value &quot;value&quot;\"";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributeWithoutQuotationMarks() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &amp; < &gt;";
     encoder.attribute(out, name, value, (char) 0, true);
     String expected = " name=value &amp; < &gt;";
     assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributeWithInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value ï";
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(out, name, value, '"', true);
     fail("InvalidXMLException expected");
 }

 @Test
 public void testAttributeWithInvalidCharacterEscaped() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &#x00ff;";
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(out, name, value, '"', true);
     fail("InvalidXMLException expected");
 }

 @Test
 public void testAttributeWithInvalidCharacterEscapedWithQuotationMarks() throws IOException, InvalidXMLException, NullPointerException {
     Writer out = new StringWriter();
     String name = "name";
     String value = "value &#x00ff;\"";
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(out, name, value, '"', true);
     fail("InvalidXMLException expected");
 }
}