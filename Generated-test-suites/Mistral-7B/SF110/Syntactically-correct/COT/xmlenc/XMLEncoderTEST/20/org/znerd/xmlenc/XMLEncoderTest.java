package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setUp() {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 }

 @Test
 public void testGetEncoder() {
 String encoding = "UTF-8";
 XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
 assertEquals("Expected the same instance of XMLEncoder for the given encoding", encoder, actualEncoder);
 }

 @Test
 public void testGetEncoding() {
 String actualEncoding = encoder.getEncoding();
 assertEquals("Expected the encoding to be UTF-8", "UTF-8", actualEncoding);
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(writer);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 String actualDeclaration = writer.toString();
 assertEquals("Expected the declaration to be correct", expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testTextNullText() throws IOException {
 try {
 encoder.text(writer, null, 0, 0, false);
 fail("Expected an IllegalArgumentException for null text parameter");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTextEmptyText() throws IOException {
 encoder.text(writer, "", false);
 String expectedText = "";
 String actualText = writer.toString();
 assertEquals("Expected an empty string as the result", expectedText, actualText);
 }

 @Test
 public void testTextBasic() throws IOException {
 String text = "Hello World!";
 encoder.text(writer, text, false);
 String expectedText = "Hello World!";
 String actualText = writer.toString();
 assertEquals("Expected the text to be correctly encoded", expectedText, actualText);
 }

 @Test
 public void testTextWithEscapedChars() throws IOException {
 char[] ch = {'<', '>', '&', '\''};
 int start = 0;
 int length = ch.length;
 encoder.text(writer, ch, start, length, false);
 String expectedText = "&lt;&gt;&amp;&apos;";
 String actualText = writer.toString();
 assertEquals("Expected the text with escaped chars to be correctly encoded", expectedText, actualText);
 }

 @Test
 public void testTextWithEscapedAmpersands() throws IOException {
 char[] ch = {'&', '&'};
 int start = 0;
 int length = ch.length;
 encoder.text(writer, ch, start, length, true);
 String expectedText = "&amp;&amp;";
 String actualText = writer.toString();
 assertEquals("Expected the text with escaped ampersands to be correctly encoded", expectedText, actualText);
 }

 @Test
 public void testTextWithInvalidChars() throws IOException, InvalidXMLException {
 char[] ch = {'<', '>', '&'};
 int start = 0;
 int length = ch.length;
 try {
 encoder.text(writer, ch, start, length, false);
 fail("Expected an InvalidXMLException for invalid chars");
 } catch (InvalidXMLException e) {
 // expected
 }
 }

 @Test
 public void testTextWithControlChars() throws IOException, InvalidXMLException {
 char[] ch = {'\u0001', '\u007f'};
 int start = 0;
 int length = ch.length;
 try {
 encoder.text(writer, ch, start, length, false);
 fail("Expected an InvalidXMLException for control chars");
 } catch (InvalidXMLException e) {
 // expected
 }
 }

 @Test
 public void testTextWithNonASCIIChars() throws IOException, UnsupportedEncodingException {
 String text = "\u00E9\u00E0";
 byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
 char[] ch = new char[bytes.length];
 for (int i = 0; i < bytes.length; ++i) {
 ch[i] = (char) bytes[i];
 }
 encoder.text(writer, ch, 0, ch.length, false);
 String expectedText = "\u00E9\u00E0";
 String actualText = writer.toString();
 assertEquals("Expected the text with non-ASCII chars to be correctly encoded", expectedText, actualText);
 }

 @Test
 public void testWhitespace() throws IOException {
 String whitespace = " ";
 encoder.whitespace(writer, whitespace.toCharArray(), 0, whitespace.length());
 String expectedWhitespace = " ";
 String actualWhitespace = writer.toString();
 assertEquals("Expected the whitespace to be correctly encoded", expectedWhitespace, actualWhitespace);
 }

 @Test
 public void testWhitespaceNullString() throws IOException {
 try {
 encoder.whitespace(writer, null, 0, 0);
 fail("Expected an IllegalArgumentException for null string parameter");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testWhitespaceEmptyString() throws IOException {
 encoder.whitespace(writer, "".toCharArray(), 0, 0);
 String expectedWhitespace = "";
 String actualWhitespace = writer.toString();
 assertEquals("Expected an empty string as the result", expectedWhitespace, actualWhitespace);
 }

 @Test
 public void testAttributeNameNull() throws IOException {
 try {
 encoder.attribute(writer, null, "", (char) 34, false);
 fail("Expected an IllegalArgumentException for null name parameter");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAttributeQuotationMarkInvalid() throws IOException {
 try {
 encoder.attribute(writer, "name", "", (char) 0, false);
 fail("Expected an IllegalArgumentException for an invalid quotation mark");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAttributeBasic() throws IOException {
 String name = "name";
 String value = "value";
 encoder.attribute(writer, name, value, (char) 34, false);
 String expectedAttribute = " name=\"value\"";
 String actualAttribute = writer.toString();
 assertEquals("Expected the attribute to be correctly encoded", expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithEscapedChars() throws IOException {
 String name = "name";
 String value = "&lt;&gt;&amp;apos;&apos;&lt;&gt;&amp;&apos;";
 encoder.attribute(writer, name, value, (char) 34, false);
 String expectedAttribute = " name=\"&lt;&gt;&amp;apos;&apos;&lt;&gt;&amp;&apos;\""
 + "\"";
 String actualAttribute = writer.toString();
 assertEquals("Expected the attribute with escaped chars to be correctly encoded", expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithEscapedAmpersands() throws IOException {
 String name = "name";
 String value = "&amp;&amp;";
 encoder.attribute(writer, name, value, (char) 34, true);
 String expectedAttribute = " name=\"&amp;&amp;\""
 + "\"";
 String actualAttribute = writer.toString();
 assertEquals("Expected the attribute with escaped ampersands to be correctly encoded", expectedAttribute, actualAttribute);
 }

 @Test
 public void testAttributeWithInvalidChars() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "<>";
 try {
 encoder.attribute(writer, name, value, (char) 34, false);
 fail("Expected an InvalidXMLException for invalid chars in attribute value");
 } catch (InvalidXMLException e) {
 // expected
 }
 }

 @Test
 public void testAttributeWithControlChars() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "\u0001\u007f";
 try {
 encoder.attribute(writer, name, value, (char) 34, false);
 fail("Expected an InvalidXMLException for control chars in attribute value");
 } catch (InvalidXMLException e) {
 // expected
 }
 }

 @Test
 public void testAttributeWithNonASCIIChars() throws IOException, UnsupportedEncodingException {
 String name = "name";
 String value = "\u00E9\u00E0";
 byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
 char[] ch = new char[bytes.length];
 for (int i = 0; i < bytes.length; ++i) {
 ch[i] = (char) bytes[i];
 }
 encoder.attribute(writer, name, new String(ch), (char) 34, false);
 String expectedAttribute = " name=\"\u00E9\u00E0\"";
 String actualAttribute = writer.toString();
 assertEquals("Expected the attribute with non-ASCII chars to be correctly encoded", expectedAttribute, actualAttribute);
 }
}