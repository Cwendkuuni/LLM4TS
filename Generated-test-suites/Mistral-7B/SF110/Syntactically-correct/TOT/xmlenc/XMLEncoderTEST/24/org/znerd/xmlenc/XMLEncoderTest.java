package org.znerd.xmlenc;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Test
 public void testEncoderConstructionWithValidEncoding() throws UnsupportedEncodingException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncoderConstructionWithNullEncoding() {
     try {
         encoder = new XMLEncoder(null);
         fail("IllegalArgumentException should have been thrown.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncoderConstructionWithUnsupportedEncoding() {
     try {
         encoder = new XMLEncoder("XYZ");
         fail("IllegalArgumentException should have been thrown.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
     writer = new StringWriter();
     encoder.declaration(writer);
     String declaration = writer.toString();
     String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     assertEquals(expected, declaration);
 }

 @Test
 public void testTextWithValidCharacter() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "Hello World", false);
     String output = writer.toString();
     String expected = "Hello World";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\u0001", false);
     String output = writer.toString();
     String expected = "&x0001;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithInvalidCharacterAndSevenBitEncoding() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("ASCII");
     encoder.text(writer, "\u0001", false);
     String output = writer.toString();
     String expected = "&amp;x0001;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithInvalidCharacterAndEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "Hello & World", true);
     String output = writer.toString();
     String expected = "Hello &amp; World";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithLessThanSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "<", false);
     String output = writer.toString();
     String expected = "&lt;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithGreaterThanSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, ">", false);
     String output = writer.toString();
     String expected = "&gt;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithAmpersandSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "&", false);
     String output = writer.toString();
     String expected = "&amp;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\"", false);
     String output = writer.toString();
     String expected = "&quot;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithApostrophe() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.text(writer, "\'", false);
     String output = writer.toString();
     String expected = "&apos;";
     assertEquals(expected, output);
 }

 @Test
 public void testTextWithWhitespace() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.whitespace(writer, "  \t Hello World ");
     String output = writer.toString();
     String expected = "  \t Hello World";
     assertEquals(expected, output);
 }

 @Test
 public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "value", '"', false);
     String output = writer.toString();
     String expected = " name=\"value\"";
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "\"value\"", '"', false);
     String output = writer.toString();
     String expected = " name=\"value\"";
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithApostrophe() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "value's", "'", false);
     String output = writer.toString();
     String expected = " name=\"value's\"";
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithEscapeAmpersand() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "value&number", "'", true);
     String output = writer.toString();
     String expected = " name=\"value&amp;number\"";
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithLessThanSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "<value", "'", false);
     String output = writer.toString();
     String expected = " name=\"<value\"";
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithGreaterThanSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "value>", "'", false);
     String output = writer.toString();
     String expected = " name=\"value>\"".replaceAll("&gt;", ">");
     assertEquals(expected, output);
 }

 @Test
 public void testAttributeWithAmpersandSign() throws IOException, NullPointerException, InvalidXMLException {
     writer = new StringWriter();
     encoder = new XMLEncoder("UTF-8");
     encoder.attribute(writer, "name", "name&name", "'", false);
     String output = writer.toString();
     String expected = " name=\"name&amp;name\"";
     assertEquals(expected, output);
 }
}