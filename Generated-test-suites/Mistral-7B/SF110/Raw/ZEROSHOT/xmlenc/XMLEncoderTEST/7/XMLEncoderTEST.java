package org.znerd.xmlenc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setUp() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new StringWriter();
 }

 @After
 public void tearDown() throws IOException {
 writer.close();
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(writer);
 String xmlDeclaration = writer.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 char[] text = {'H', 'e', 'l', 'l', 'o'};
 encoder.text(writer, text, 0, text.length, false);
 String xmlText = writer.toString();
 assertEquals("Hello", xmlText);
 }

 @Test
 public void testEscapeAmpersands() throws IOException, InvalidXMLException {
 char[] text = {'&', 'a', 'm', 'p', ';'};
 encoder.text(writer, text, 0, text.length, true);
 String xmlText = writer.toString();
 assertEquals("&amp;", xmlText.substring(writer.getPosition() - text.length - 4, writer.getPosition()));
 writer.getPosition(0); // reset writer position
 }

 @Test(expected = InvalidXMLException.class)
 public void testInvalidText() throws IOException {
 char[] text = {'<', '>', '&'};
 encoder.text(writer, text, 0, text.length, false);
 fail("Expected InvalidXMLException but none was thrown.");
 }

 @Test
 public void testTextWithEscape() throws IOException, InvalidXMLException {
 char[] text = {'<', '?', '>'};
 encoder.text(writer, text, 0, text.length, false);
 String xmlText = writer.toString();
 assertEquals("&lt;?&gt;", xmlText.substring(writer.getPosition() - text.length - 6, writer.getPosition()));
 writer.getPosition(0); // reset writer position
 }

 @Test
 public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
 char[] text = {'<', '&', '>', ' '};
 encoder.text(writer, text, 0, text.length, true);
 String xmlText = writer.toString();
 assertEquals("&lt; &amp; &gt;", xmlText.substring(writer.getPosition() - text.length - 7, writer.getPosition()));
 writer.getPosition(0); // reset writer position
 }

 @Test
 public void testWhitespace() throws IOException {
 String whitespace = "   ";
 encoder.whitespace(writer, whitespace);
 String xmlWhitespace = writer.toString();
 assertEquals("   ", xmlWhitespace.substring(writer.getPosition() - whitespace.length(), writer.getPosition()));
 }

 @Test
 public void testAttribute() throws IOException {
 String name = "test";
 String value = "value";
 char quotationMark = '\"';
 encoder.attribute(writer, name, value, quotationMark, false);
 String xmlAttribute = writer.toString();
 assertEquals(" xmlns:test=\"" + name + "\"xml:space=\"preserve\">" + value + "\"/>", xmlAttribute.substring(writer.getPosition() - name.length() - value.length() - 10, xmlAttribute.length()));
 writer.getPosition(0); // reset writer position
 }

 @Test
 public void testAttributeWithEscape() throws IOException {
 String name = "test";
 String value = "value &amp; ampersand &lt; less than &gt; greater than &apos; apostrophe &quot; quote";
 char quotationMark = '\'';
 encoder.attribute(writer, name, value, quotationMark, true);
 String xmlAttribute = writer.toString();
 assertEquals(" xmlns:test=\"" + name + "\">" +
 "&amp;amp; &amp;amp;amp;amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot; &quot;quote&quot;</ quot;>" +
 "value &amp; &amp;amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot; &quot;quote&quot;</ quot;>" +
 "</" + name + ">", xmlAttribute.substring(writer.getPosition() - name.length() - value.length() - 10, xmlAttribute.length()));
 writer.getPosition(0); // reset writer position
 }
}