package org.znerd.xmlenc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setup() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new StringWriter();
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(writer);
 String declaration = writer.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration);
 }

 @Test
 public void testTextWithEscapes() throws IOException, InvalidXMLException {
 String text = "&<'>\"";
 encoder.text(writer, text, true);
 String encodedText = writer.toString();
 assertEquals("&amp;&lt;&gt;&gt;&quot;", encodedText);
 }

 @Test
 public void testTextWithoutEscapes() throws IOException, InvalidXMLException {
 String text = "abcdefghijklmnopqrstuvwxyz";
 encoder.text(writer, text, false);
 String encodedText = writer.toString();
 assertEquals("abcdefghijklmnopqrstuvwxyz", encodedText);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
 String text = "abc\u0080def";
 encoder.text(writer, text.toCharArray(), 0, text.length(), false);
 fail("Expected InvalidXMLException");
 }

 @Test
 public void testAttribute() throws IOException {
 String name = "testName";
 String value = "&<'>\"";
 char quotationMark = '\"';
 encoder.attribute(writer, name, value, quotationMark, true);
 String encodedAttribute = writer.toString();
 assertEquals("<testName=\"&amp;&lt;&gt;&gt;&quot;\"></testName>", encodedAttribute);
 }

 @Test
 public void testWhitespace() throws IOException {
 String whitespace = " Hello World ";
 encoder.whitespace(writer, whitespace);
 String encodedWhitespace = writer.toString();
 assertEquals(" Hello World ", encodedWhitespace);
 }

}