package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";

 @Test
 public void testGetEncoder() {
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
     assertEquals(UTF8_ENCODING, encoder.getEncoding());
     assertFalse(encoder._sevenBitEncoding);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("INVALID_ENCODING"));
 }

 @Test
 public void testDeclarationEmpty() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.declaration(out);
     String xmlDeclaration = out.toString();
     assertEquals("<?xml version=\"1.0\" encoding=\"" + UTF8_ENCODING + "\"?>", xmlDeclaration);
 }

 @Test
 public void testDeclarationWithEncoding() throws IOException {
     XMLEncoder encoder = new XMLEncoder("UTF-16");
     StringWriter out = new StringWriter();
     encoder.declaration(out);
     String xmlDeclaration = out.toString();
     assertEquals("<?xml version=\"1.0\" encoding=\"" + "UTF-16" + "\"?>", xmlDeclaration);
 }

 @Test
 public void testTextEmpty() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.text(out, "");
     assertEquals("", out.toString());
 }

 @Test
 public void testTextSingleCharacter() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.text(out, 'A');
     assertEquals("A", out.toString());
 }

 @Test
 public void testTextMultipleCharacters() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.text(out, "ABC");
     assertEquals("ABC", out.toString());
 }

 @Test
 public void testTextSpecialCharacters() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.text(out, "&<>'\"");
     assertEquals("&amp;&lt;&gt;&apos;&quot;", out.toString());
 }

 @Test
 public void testTextSpecialCharactersWithEscape() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.text(out, "&<>&>'\"", true);
     assertEquals("&amp;&amp;&lt;&gt;&apos;&apos;", out.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextInvalidIndex() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     byte[] input = "ABC".getBytes(StandardCharsets.UTF_8);
     encoder.text(out, input, 2, 2);
 }

 @Test
 public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     byte[] input = "\uD800".getBytes(StandardCharsets.UTF_8);
     assertThrows(InvalidXMLException.class, () -> encoder.text(out, input, 0, input.length));
 }

 @Test
 public void testTextInvalidCharacterWithSevenBitEncoding() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder("ASCII");
     StringWriter out = new StringWriter();
     byte[] input = "\uD800".getBytes(StandardCharsets.UTF_8);
     assertThrows(InvalidXMLException.class, () -> encoder.text(out, input, 0, input.length));
 }

 @Test
 public void testWhitespaceEmpty() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.whitespace(out, "");
     assertEquals("", out.toString());
 }

 @Test
 public void testWhitespaceSingleCharacter() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.whitespace(out, " ");
     assertEquals(" ", out.toString());
 }

 @Test
 public void testWhitespaceMultipleCharacters() throws IOException, InvalidXMLException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.whitespace(out, "ABC ");
     assertEquals("ABC ", out.toString());
 }

 @Test
 public void testAttributeEmpty() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "", ' ', false);
     assertEquals(" name=\"\"", out.toString());
 }

 @Test
 public void testAttributeSingleCharacter() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "A", ' ', false);
     assertEquals(" name=\"A\"", out.toString());
 }

 @Test
 public void testAttributeMultipleCharacters() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "ABC", ' ', false);
     assertEquals(" name=\"ABC\"", out.toString());
 }

 @Test
 public void testAttributeQuotationMarkSingleQuote() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "\"A\"", '\'', false);
     assertEquals(" name=\"&quot;A&quot;\">", out.toString());
 }

 @Test
 public void testAttributeQuotationMarkDoubleQuote() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "'A'", '\"', false);
     assertEquals(" name=\"&apos;A&apos;\">", out.toString());
 }

 @Test
 public void testAttributeEscapeAmpersands() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "&amp;A", ' ', true);
     assertEquals(" name=\"&amp;amp;A\"", out.toString());
 }

 @Test
 public void testAttributeEscapeSpecialCharacters() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     encoder.attribute(out, "name", "&lt;&gt;&amp;&apos;&quot;", ' ', true);
     assertEquals(" name=\"&lt;&amp;lt;&amp;amp;&amp;apos;&amp;apos;\">", out.toString());
 }

 @Test
 public void testAttributeInvalidCharacter() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF8_ENCODING);
     StringWriter out = new StringWriter();
     assertThrows(InvalidXMLException.class, () -> encoder.attribute(out, "name", "&#xFFFF;", ' ', false));
 }

 @Test
 public void testAttributeInvalidCharacterWithSevenBitEncoding() throws IOException {
     XMLEncoder encoder = new XMLEncoder("ASCII");
     StringWriter out = new StringWriter();
     assertThrows(InvalidXMLException.class, () -> encoder.attribute(out, "name", "&#xFFFF;", ' ', false));
 }
}