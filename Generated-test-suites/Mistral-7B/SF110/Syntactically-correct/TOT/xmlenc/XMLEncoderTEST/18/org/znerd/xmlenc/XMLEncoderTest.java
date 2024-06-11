package org.znerd.xmlenc;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

public class XMLEncoderTest {

 private StringWriter out;
 private XMLEncoder encoder;

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
 encoding = XMLEncoder.getEncoder("UTF-8");
 assertEquals("UTF-8", encoding.getEncoding());
 }

 @Test
 public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
 assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("InvalidEncoding"));
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(out);
 String xmlDeclaration = out.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testText_ValidCharacter() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(out, "Hello, World!");
 String text = out.toString();
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testText_InvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 final char invalidCharacter = '\u00FF';
 encoder.text(out, String.valueOf(invalidCharacter).toCharArray(), 0, 1, false);
 String text = out.toString();
 // Uncomment the following line to see the actual output
 // System.out.println(text);
 assertThrows(InvalidXMLException.class, () -> encoder.text(out, String.valueOf(invalidCharacter).toCharArray(), 0, 1, false));
 }

 @Test
 public void testText_EscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(out, "& < > ' \"", false);
 String text = out.toString();
 // Uncomment the following line to see the actual output
 // System.out.println(text);
 assertEquals("&amp; &lt; &gt; '&apos; \"", text);
 }

 @Test
 public void testText_NoEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(out, "& < > ' \"", true);
 String text = out.toString();
 // Uncomment the following line to see the actual output
 // System.out.println(text);
 assertEquals("& & < & > ' & ' \"", text);
 }

 @Test
 public void testText_SevenBitEncoding() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("US-ASCII");
 encoder.text(out, "Hello, World!", false);
 String text = out.toString();
 assertEquals("Hello, World!", text);
 }

 @Test
 public void testText_SevenBitEncoding_InvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("US-ASCII");
 final char invalidCharacter = '\u00FF';
 encoder.text(out, String.valueOf(invalidCharacter).toCharArray(), 0, 1, false);
 String text = out.toString();
 // Uncomment the following line to see the actual output
 // System.out.println(text);
 assertThrows(InvalidXMLException.class, () -> encoder.text(out, String.valueOf(invalidCharacter).toCharArray(), 0, 1, false));
 }

 @Test
 public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.whitespace(out, " test ");
 String whitespace = out.toString();
 assertEquals(" test ", whitespace);
 }

 @Test
 public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
 out = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(out, "test", "test \"test\"", '\'', false);
 String attribute = out.toString();
 // Uncomment the following line to see the actual output
 // System.out.println(attribute);
 assertEquals("<test test=\"test &quot;test&quot;\"></test>", attribute);
 }

 @Test
 public void testAttribute_InvalidQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
 assertThrows(IllegalArgumentException.class, () -> encoder.attribute(out, "test", "test \"test\"", '|', false)); // Invalid quotation mark
 }
}