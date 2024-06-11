package org.znerd.xmlenc.test;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLEncoder.InvalidXMLException;
import org.znerd.xmlenc.XMLChecker;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer out;
 private ByteArrayOutputStream outputStream;
 private XMLChecker xmlChecker;

 @Before
 public void setup() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 out = mock(Writer.class);
 xmlChecker = mock(XMLChecker.class);
 }

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
 String encoding = "UTF-8";
 XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder encoder = XMLEncoder.getEncoder(null);
 fail("IllegalArgumentException should be thrown when encoding is null.");
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 String encoding = "ABC-123";
 XMLEncoder encoder = assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder(encoding));
 assertEquals("ABC-123", encoder.getMessage());
 }

 @Test
 public void testTextNullText() throws IOException, InvalidXMLException {
 encoder.text(out, null, 0, 0, false);
 verify(out, never()).write(anyChar());
 }

 @Test
 public void testTextEmptyText() throws IOException, InvalidXMLException {
 String emptyText = "";
 encoder.text(out, emptyText, 0, emptyText.length(), false);
 verify(out, times(3)).write(32);
 verify(out).write("", 0, emptyText.length());
 }

 @Test
 public void testTextChar() throws IOException, InvalidXMLException {
 char c = 'A';
 encoder.text(out, c);
 verify(out).write(c);
 }

 @Test
 public void testTextCharSevenBitEncodingTrue() throws IOException, InvalidXMLException {
 char c = '€';
 encoder = new XMLEncoder("ASCII");
 encoder.text(out, c);
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;#128;");
 }

 @Test
 public void testTextCharSevenBitEncodingFalse() throws IOException, InvalidXMLException {
 char c = '€';
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, c);
 verify(out).write(c);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
 char c = 0xDB;
 encoder.text(out, c);
 fail("InvalidXMLException should be thrown when encoding invalid character.");
 }

 @Test
 public void testTextInvalidCharacterEscaped() throws IOException, InvalidXMLException {
 char c = 0xDB;
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, c);
 verify(out).write(XMLEncoder.AMPERSAND_HASH, 0, 2);
 verify(out).write("x");
 verify(out).write("DB");
 verify(out).write(59);
 }

 @Test
 public void testTextAmpersand() throws IOException, InvalidXMLException {
 char c = '&';
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, c, true);
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;");
 }

 @Test
 public void testTextAmpersandEscaped() throws IOException, InvalidXMLException {
 char c = '&';
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, c, false);
 verify(out).write(c);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextInvalidIndex() throws IOException, InvalidXMLException {
 char[] ch = "abc".toCharArray();
 encoder.text(out, ch, 3, ch.length, false);
 fail("IndexOutOfBoundsException should be thrown when invalid index.");
 }

 @Test
 public void testTextString() throws IOException, InvalidXMLException {
 String text = "abcdef";
 encoder.text(out, text, false);
 verify(out).write("abcdef");
 }

 @Test
 public void testTextStringEscaped() throws IOException, InvalidXMLException {
 String text = "abc&def";
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, text, false);
 verify(out).write("abc");
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;");
 verify(out).write("def");
 }

 @Test
 public void testTextStringWithQuotationMark() throws IOException, InvalidXMLException {
 String text = "abc\"def";
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, text, true);
 verify(out).write("abc");
 verify(out).write(XMLEncoder.ESC_QUOTE, 0, 6);
 verify(out).write("quot;def");
 }

 @Test
 public void testTextStringWithQuotationMarkEscaped() throws IOException, InvalidXMLException {
 String text = "abc\"def";
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, text, false);
 verify(out).write("abc");
 verify(out).write(XMLEncoder.ESC_QUOTE, 0, 6);
 verify(out).write("quot;quot;d");
 verify(out).write("e");
 verify(out).write("f");
 }

 @Test
 public void testTextStringWithAmpersand() throws IOException, InvalidXMLException {
 String text = "abc&def";
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, text, false);
 verify(out).write("abc");
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;");
 verify(out).write("def");
 }

 @Test
 public void testTextStringWithAmpersandEscaped() throws IOException, InvalidXMLException {
 String text = "abc&def";
 encoder = new XMLEncoder("UTF-8");
 encoder.text(out, text, true);
 verify(out).write("abc");
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;");
 verify(out).write("def");
 }

 @Test
 public void testDeclarationNullOut() throws IOException {
 encoder.declaration(null);
 fail("NullPointerException should be thrown when out is null.");
 }

 @Test
 public void testDeclarationEmptyEncoding() throws IOException {
 encoder.declaration(out);
 verify(out).write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
 verify(out).write(0);
 verify(out).write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
 }

 @Test
 public void testDeclarationUTF8() throws IOException {
 encoder.declaration(out);
 verify(out).write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
 verify(out).write("UTF-8".getBytes(StandardCharsets.UTF_8), 0, "UTF-8".getBytes(StandardCharsets.UTF_8).length);
 verify(out).write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
 }

 @Test
 public void testDeclarationASCII() throws IOException {
 encoder = new XMLEncoder("ASCII");
 encoder.declaration(out);
 verify(out).write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
 verify(out).write("ASCII".getBytes(StandardCharsets.UTF_8), 0, "ASCII".getBytes(StandardCharsets.UTF_8).length);
 verify(out).write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
 }

 @Test
 public void testWhitespaceNullOut() throws IOException {
 encoder.whitespace(out, null, 0, 0);
 verify(out, never()).write(anyChar());
 }

 @Test
 public void testWhitespaceEmptyString() throws IOException {
 encoder.whitespace(out, "", 0, 0);
 verify(out, times(1)).write(32);
 }

 @Test
 public void testWhitespaceString() throws IOException {
 String s = " ";
 encoder.whitespace(out, s, 0, s.length());
 verify(out).write(s.toCharArray(), 0, s.length());
 }

 @Test
 public void testWhitespaceStringWithLength() throws IOException {
 String s = "abc ";
 encoder.whitespace(out, s, 1, 3);
 verify(out).write(s.substring(1, 3).toCharArray(), 0, s.substring(1, 3).length());
 }

 @Test
 public void testAttributeNullName() throws IOException, InvalidXMLException {
 encoder.attribute(out, null, "", '"', false);
 fail("NullPointerException should be thrown when name is null.");
 }

 @Test
 public void testAttributeEmptyName() throws IOException, InvalidXMLException {
 String name = "";
 encoder.attribute(out, name, "value", '"', false);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write("value");
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 }

 @Test
 public void testAttributeQuotationMarkDifferent() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "'value'";
 encoder.attribute(out, name, value, '"', false);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_APOSTROPHE, 0, 2);
 verify(out).write("value'");
 }

 @Test
 public void testAttributeQuotationMarkSame() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "\"value\"";
 encoder.attribute(out, name, value, '"', false);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write("value\"");
 }

 @Test
 public void testAttributeEscapeAmpersandTrue() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "value&";
 encoder.attribute(out, name, value, '"', true);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
 verify(out).write("amp;");
 verify(out).write("value");
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 }

 @Test
 public void testAttributeEscapeAmpersandFalse() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "value&";
 encoder.attribute(out, name, value, '"', false);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write("value");
 verify(out).write('&');
 }

 @Test
 public void testAttributeEscapeCharacter() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "value<";
 encoder.attribute(out, name, value, '"', false);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write(XMLEncoder.ESC_LESS_THAN, 0, 4);
 verify(out).write("value");
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 }

 @Test
 public void testAttributeEscapeCharacterEscaped() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "value<";
 encoder.attribute(out, name, value, '"', true);
 verify(out).write(32);
 verify(out).write(name);
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 verify(out).write(XMLEncoder.ESC_LESS_THAN, 0, 4);
 verify(out).write(XMLEncoder.ESC_LESS_THAN, 0, 4);
 verify(out).write("value");
 verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
 }

 @Test
 public void testAttributeInvalidCharacter() throws IOException, InvalidXMLException {
 String name = "name";
 String value = "value\u00DB";
 encoder = new XMLEncoder("UTF-8");
 encoder.attribute(out, name, value, '"', false);
 verify(xmlChecker).checkS(value.toCharArray(), 0, value.length());
 verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 2);
 verify(out).write("amp;");
 verify(out).write("entity");
 verify(out).write("name");
 verify(out).write(";");
 }

}