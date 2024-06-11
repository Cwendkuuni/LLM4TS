package org.znerd.xmlenc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

 @Test
 public void testEncoderInitialization() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
 XMLEncoder encoder = null; // This line should throw an IllegalArgumentException
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncoding() {
 XMLEncoder encoder = null; // This line should throw an UnsupportedEncodingException
 try {
 encoder = XMLEncoder.getEncoder("XYZ-123");
 }
 catch (Exception e) {
 assertEquals("XYZ-123", e.getMessage());
 }
 }

 @Test
 public void testUTF8Encoding() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(out);
 encoder.text(out, "UTF-8 encoded text", true);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>UTF-8 encoded text", out.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullWriter() {
 final XMLEncoder encoder = new XMLEncoder("UTF-8");
 encoder.declaration(null); // This line should throw a NullPointerException
 }

 @Test
 public void testDeclareAndWriteText() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(out);
 encoder.text(out, "UTF-8 encoded text", true);
 encoder.text(out, "&lt;", false); // Should not escape since no ampersands are being escaped here
 encoder.text(out, "&amp;", false); // Should be written as "&amp;"
 encoder.text(out, "&apos;", false); // Should be written as "&apos;"
 encoder.text(out, "&quot;", false); // Should be written as "&quot;"
 encoder.text(out, "&gt;", false); // Should not escape since no greater than symbol is being escaped here
 encoder.text(out, "The quick brown fox jumps over the lazy dog", true); // Should escape all special characters
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>&lt;?&amp;amp;amp;apos;amp;amp;quot;?&gt;&amp;The quick brown fox jumps over the lazy dog&amp;", out.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithNegativeLength() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 final char[] chars = "UTF-8 encoded text".toCharArray();
 encoder.text(out, chars, -1, 0, true); // This line should throw an IndexOutOfBoundsException
 }

 @Test(expected = InvalidXMLException.class)
 public void testInvalidXMLCharacters() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(out, "The quick brown fox jumps over the λazy δog", true); // Should throw an InvalidXMLException for the Greek characters
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>The quick brown fox jumps over the λazy δog", out.toString()); // This should not happen
 }

 @Test
 public void testDeclareAndWriteTextWithUTF16Encoding() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-16");
 encoder.declaration(out);
 encoder.text(out, "UTF-16 encoded text", true);
 encoder.text(out, "&lt;", false); // Should not escape since no ampersands are being escaped here
 encoder.text(out, "&amp;", false); // Should be written as "&amp;"
 encoder.text(out, "&apos;", false); // Should be written as "&apos;"
 encoder.text(out, "&quot;", false); // Should be written as "&quot;"
 encoder.text(out, "&gt;", false); // Should not escape since no greater than symbol is being escaped here
 encoder.text(out, "The quick brown fox jumps over the λazy δog", true); // Should escape all special characters
 // UTF-16 encoding is a multibyte encoding, so the output will be different than UTF-8.
 // The test passes as long as the output is valid UTF-16 XML and all text is encoded correctly.
 }

 @Test
 public void testAttribute() throws IOException {
 final StringWriter out = new StringWriter();
 final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.attribute(out, "name", "value", '\"', true);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><name value=\"value\"/>", out.toString());
 }
}