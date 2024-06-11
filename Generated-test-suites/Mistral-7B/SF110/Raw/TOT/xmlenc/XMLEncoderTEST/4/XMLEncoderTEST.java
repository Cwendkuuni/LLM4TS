package org.znerd.xmlenc;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     assertNotNull(XMLEncoder.getEncoder("UTF-8"));
     assertNotNull(XMLEncoder.getEncoder("UTF-16"));
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(""));
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder("invalidEncoding"));
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ISO-8859-99"));
 }

 @Test
 public void testConstructor() throws IllegalArgumentException, UnsupportedEncodingException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
     assertFalse(encoder._sevenBitEncoding);

     encoder = XMLEncoder.getEncoder("US-ASCII");
     assertEquals("US-ASCII", encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);

     encoder = XMLEncoder.getEncoder("ASCII");
     assertEquals("ASCII", encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);

     encoder = XMLEncoder.getEncoder("ISO-8859-1");
     assertEquals("ISO-8859-1", encoder.getEncoding());
     assertTrue(encoder._sevenBitEncoding);
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.declaration(writer);
     String declaration = writer.toString();
     assertTrue(declaration.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
 }

 @Test
 public void testText() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");

     encoder.text(writer, "Hello, World!", false);
     assertEquals("Hello, World!", writer.toString());

     encoder.text(writer, "This is a <test> string.", true);
     assertEquals("This is a &lt;test&gt; string.", writer.toString());

     encoder.text(writer, "This is a <test> string.", false);
     assertEquals("This is a <test> string.", writer.toString());

     encoder = XMLEncoder.getEncoder("ASCII");

     encoder.text(writer, "Hello, World!", false);
     assertEquals("Hello, World!", writer.toString());

     encoder.text(writer, "This is a <test> string.", true);
     assertEquals("This is a &lt;test&gt; string.", writer.toString());

     encoder.text(writer, "This is a <test> string.", false);
     assertEquals("This is a <test> string.", writer.toString());
 }

 @Test
 public void testTextWithInvalidCharacters() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");

     encoder.text(writer, "This string contains invalid characters: < > & and control characters.", true);

     assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                 "This string contains invalid characters: &amp;lt; &amp;gt; &amp;amp; & #1 & #2 & #3 & #4 & #5 & #6 & #7 & #8 & #9 & #10 & #11 & #12 & #13 & #14 & #15 & #16 & #17 & #18 & #19 & #20 & #21 & #22 & #23 & #24 & #25 & #26 & #27 & #28 & #29 & #30 & #31 & #127;",
             writer.toString());

     writer.getBuffer().reset();
     encoder.text(writer, "This string contains invalid characters: < > & and control characters.", false);
     assertEquals("This string contains invalid characters: < > & and control characters.", writer.toString());
 }

 @Test
 public void testTextWithEmptyString() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.text(writer, "", false);
     assertEquals("", writer.toString());
 }

 @Test
 public void testTextWithSingleCharacter() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");

     encoder.text(writer, 'A');
     assertEquals("A", writer.toString());

     encoder = XMLEncoder.getEncoder("ASCII");
     encoder.text(writer, 'A');
     assertEquals("A", writer.toString());
 }

 @Test
 public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.whitespace(writer, "This is a test.");
     assertEquals("This is a test.", writer.toString());
 }

 @Test
 public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");

     encoder.attribute(writer, "xmlns", "http://www.w3.org/2000/xmlns/", '"');
     assertEquals("<xmlns xmlns=\"http://www.w3.org/2000/xmlns/\"/>", writer.toString());

     encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.attribute(writer, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", '\'');
     assertEquals("<xmlns:xsi xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", writer.toString());
 }

 @Test
 public void testAttributeWithInvalidQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = XMLEncoder.getEncoder("UTF-8");

     try {
         encoder.attribute(writer, "xmlns", "http://www.w3.org/2000/xmlns/", '^');
         fail();
     } catch (IllegalArgumentException e) {
         assertEquals("Character 0x2E ^ (^) is not a valid quotation mark.", e.getMessage());
     }
 }
}