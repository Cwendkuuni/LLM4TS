package org.znerd.xmlenc;

import org.junit.Test;
import org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

 @Test
 public void testGetEncoder() {
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 try {
 XMLEncoder encoder = XMLEncoder.getEncoder(null);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 try {
 XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-11");
 fail("UnsupportedEncodingException expected but not thrown");
 } catch (UnsupportedEncodingException e) {
 // pass
 }
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(out);
 String xmlDeclaration = out.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String text = "This is a test string with special characters: <>&\"'";
 encoder.text(out, text, false);
 String encodedText = out.toString();
 assertEquals("<This is a test string with special characters: &lt;>&amp;&quot;'&gt;", encodedText);
 }

 @Test
 public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String text = "This is a test string with special characters: <>&\"'";
 encoder.text(out, text, true);
 String encodedText = out.toString();
 assertEquals("<This is a test string with special characters: &lt; &amp; &quot; &apos;>", encodedText);
 }

 @Test
 public void testTextSingleCharacter() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 char c = '&';
 encoder.text(out, c);
 String encodedText = out.toString();
 assertEquals("&amp;", encodedText);
 }

 @Test
 public void testTextSingleCharacterWithEscapeAmpersands() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 char c = '&';
 encoder.text(out, c, true);
 String encodedText = out.toString();
 assertEquals("&amp;amp;", encodedText);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 char invalidChar = 0x00F0;
 encoder.text(out, invalidChar);
 fail("InvalidXMLException expected but not thrown");
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String whitespace = " \t\r\n\f";
 encoder.whitespace(out, whitespace);
 String encodedWhitespace = out.toString();
 assertEquals(" \t\r\n\f", encodedWhitespace);
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "name";
 String value = "value & ' > <";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, false);
 String encodedAttribute = out.toString();
 assertEquals("<name=\"value &amp; ' &gt; &lt;\">", encodedAttribute);
 }

 @Test
 public void testAttributeWithQuotationMarkInValue() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "name";
 String value = "value \" > <";
 char quotationMark = '\'';
 encoder.attribute(out, name, value,quotationMark, false);
 String encodedAttribute = out.toString();
 assertEquals("<name=\"value \\\" &gt; &lt;\">", encodedAttribute);
 }

 @Test
 public void testAttributeWithEscapeAmpersands() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "name";
 String value = "value & ' > <";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, true);
 String encodedAttribute = out.toString();
 assertEquals("<name=\"value &amp; ' &gt; &amp;lt;\">", encodedAttribute);
 }

 @Test
 public void testAttributeWithQuotationMarkInValueAndEscapeAmpersands() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "name";
 String value = "value \\\" & ' > <";
 char quotationMark = '\'';
 encoder.attribute(out, name, value, quotationMark, true);
 String encodedAttribute = out.toString();
 assertEquals("<name=\"value \\\" &amp; &amp;quot; &amp;amp; ' &gt; &amp;gt; &amp;lt; &amp;lt; &amp;gt; &amp;gt; &amp;apos; &amp;apos;\">", encodedAttribute);
 }
}