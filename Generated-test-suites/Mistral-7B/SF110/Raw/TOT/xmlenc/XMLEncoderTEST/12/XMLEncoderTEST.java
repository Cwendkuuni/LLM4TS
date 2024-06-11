package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testGetEncoder() {
     String encoding = "UTF-8";
     encoder = XMLEncoder.getEncoder(encoding);
     assertEquals(encoding, encoder.getEncoding());
 }

 @Test
 public void testInvalidEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("INVALID_ENCODING"));
 }

 @Test
 public void testDeclaration() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     encoder.declaration(writer);
     String xmlDeclaration = writer.toString();
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     assertEquals(expectedDeclaration, xmlDeclaration);
 }

 @Test
 public void testTextWithValidCharacters() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String text = "This is a test.";
     encoder.text(writer, text, false);
     String encodedText = writer.toString();
     String expectedEncodedText = "This is a test.";
     assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testTextWithInvalidCharacters() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String text = "This is a <test> with & invalid characters!";
     encoder.text(writer, text.toCharArray(), 0, text.length(), false);
     String encodedText = writer.toString();
     String expectedEncodedText = "This is a &lt;test&gt; with &amp; invalid characters!";
     assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testTextWithSevenBitEncoding() {
     encoder = new XMLEncoder("ISO-8859-1");
     writer = new StringWriter();
     String text = "This is a Øø éé æå ÆÅ ßß ßß";
     encoder.text(writer, text.toCharArray(), 0, text.length(), false);
     String encodedText = writer.toString();
     String expectedEncodedText = "This is a &#211;&#223; &#233;#233; &#226;#230; &#226;#231; #226;#195; #226;#195; #226;#195; #226;#195;";
     assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testTextWithEscapeAmpersands() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     String text = "This is a & test & with &amp; escape &amp;amp; ampersands.";
     encoder.text(writer, text.toCharArray(), 0, text.length(), true);
     String encodedText = writer.toString();
     String expectedEncodedText = "This is a &amp;&amp; test &amp;&amp;amp; ampersands.";
     assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testTextWithInvalidXMLCharacters() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     char invalidChar = 1; // Unicode character that is not valid in XML
     encoder.text(writer, new char[] { invalidChar }, 0, 1, false);
     assertThrows(InvalidXMLException.class, () -> writer.toString());
 }

 @Test
 public void testTextWithNullText() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     encoder.text(writer, null, 0, 0, false);
     assertThrows(NullPointerException.class, () -> writer.toString());
 }

 @Test
 public void testTextWithEmptyText() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     encoder.text(writer, new char[0], 0, 0, false);
     String encodedText = writer.toString();
     assertEquals("", encodedText);
 }

 @Test
 public void testTextWithSingleCharacter() {
     encoder = new XMLEncoder("UTF-8");
     writer = new StringWriter();
     char character = 'A';
     encoder.text(writer, character);
     String encodedText = writer.toString();
     String expectedEncodedText = "#65";
     assertEquals(expectedEncodedText, encodedText);
 }
}