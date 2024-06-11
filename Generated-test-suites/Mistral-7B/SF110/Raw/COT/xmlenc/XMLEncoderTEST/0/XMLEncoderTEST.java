package org.znerd.xmlenc;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;

public class XMLEncoderTest {

 private static final String UTF_8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
 private static final char[] XML_DECLARATION = {"<?xml version=\"1.0\" encoding=\"".toCharArray()};
 private static final char[] XML_DECLARATION_END = {"\"?>".toCharArray()};
 private static final char[] ESC_GREATER_THAN = {'&', 'g', 't', ';'};
 private static final char[] ESC_LESS_THAN = {'&', 'l', 't', ';'};
 private static final char[] ESC_AMPERSAND = {'&', 'a', 'm', 'p', ';'};
 private static final char[] ESC_APOSTROPHE = {'&', 'a', 'p', 'o', 's', ';'};
 private static final char[] ESC_QUOTE = {'&', 'q', 'u', 'o', 't', ';'};
 private static final char[] AMPERSAND_HASH = {'&', '#'};
 private static final char[] EQUALS_APOSTROPHE = {'=', '\''};
 private static final char[] EQUALS_QUOTE = {'=', '\"'};

 @Test
 public void testGetEncoder_NullEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test
 public void testGetEncoder_UTF8Encoding() throws UnsupportedEncodingException {
     XMLEncoder encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     assertEquals(UTF_8_ENCODING, encoder.getEncoding());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoder_UnsupportedEncoding() throws UnsupportedEncodingException {
     XMLEncoder.getEncoder("UnsupportedEncoding");
 }

 @Test
 public void testText_Writer_String_EscapeAmpersandsFalse() throws IOException {
     String text = "abc<def>123>";
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     Writer writer = new Writer() {
         @Override
         public void write(char[] ch, int start, int length) throws IOException {
             outputStream.write(ch, start, length);
         }
     };
     encoder.text(writer, text, false);
     String expected = new String(outputStream.toByteArray(), StandardCharsets.UTF_8)
             .replaceAll("&lt;", "<")
             .replaceAll("&gt;", ">");
     assertEquals(expected, text);
 }

 @Test
 public void testText_Writer_CharArray_Start_Length_EscapeAmpersandsFalse() throws IOException {
     char[] ch = {'a', 'b', 'c', '<', 'd', 'e', 'f', '>', '1', '2', '3'};
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     Writer writer = new Writer() {
         @Override
         public void write(char[] ch, int start, int length) throws IOException {
             outputStream.write(ch, start, length);
         }
     };
     encoder.text(writer, ch, 0, ch.length, false);
     String expected = new String(outputStream.toByteArray(), StandardCharsets.UTF_8)
             .replaceAll("&lt;", "<")
             .replaceAll("&gt;", ">");
     assertEquals(expected, new String(ch, 0, ch.length));
 }

 @Test
 public void testText_Writer_CharArray_Start_Length_EscapeAmpersandsTrue() throws IOException {
     char[] ch = {'a', 'b', 'c', '<', 'd', 'e', 'f', '>', '1', '2', '3'};
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     Writer writer = new Writer() {
         @Override
     public void write(char[] ch, int start, int length) throws IOException {
             outputStream.write(ch, start, length);
         }
     };
     encoder.text(writer, ch, 0, ch.length, true);
     String expected = new String(outputStream.toByteArray(), StandardCharsets.UTF_8)
             .replaceAll("&lt;", "&lt;")
             .replaceAll("&gt;", "&gt;")
             .replaceAll("&amp;", "&amp;")
             .replaceAll("&apos;", "&apos;")
             .replaceAll("&quot;", "&quot;");
     for (int i = 0; i < ch.length; ++i) {
         if (ch[i] == '<') {
             expected = expected.replaceAll("&lt;", "&lt;").replaceAll("&gt;", "&gt;")
                     + "&lt;" + (i < ch.length - 1 ? ">" : "");
         }
         else if (ch[i] == '>') {
             expected = expected.replaceAll("&lt;", "&lt;")
                     .replaceAll("&gt;", "&gt;") + "&gt;" + (i < ch.length - 1 ? ">" : "");
         }
         else if (ch[i] == '&') {
             expected = expected.replaceAll("&lt;", "&lt;")
                     .replaceAll("&gt;", "&gt;") + "&amp;" + (i < ch.length - 1 ? ">" : "");
         }
         else if ((ch[i] < 63 || ch[i] > 127) && ch[i] != 39 && ch[i] != 34 && ch[i] != 32
                 && ch[i] != 37 && ch[i] != 38 && ch[i] != 10 && ch[i] != 13 && ch[i] != 61) {
             Random random = new Random();
             int randomNumber = random.nextInt(65535);
             expected = expected.replaceAll("\\" + Integer.toHexString(ch[i]) + "",
                     "&#" + Integer.toHexString(randomNumber) + ";");
         }
     }
     assertEquals(expected, new String(ch, 0, ch.length));
 }

 @Test(expected = InvalidXMLException.class)
 public void testText_Writer_CharArray_Start_Length_InvalidCharacter() throws IOException {
     char[] ch = {'\u0001'};
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     Writer writer = new Writer() {
         @Override
     public void write(char[] ch, int start, int length) throws IOException {
             outputStream.write(ch, start, length);
         }
     };
     encoder.text(writer, ch, 0, ch.length, false);
 }

 @Test
 public void testDeclaration_Writer() throws IOException, NullPointerException {
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     Writer writer = new Writer() {
         @Override
         public void write(char[] ch, int start, int length) throws IOException {
             outputStream.write(ch, start, length);
         }
     };
     encoder.declaration(writer);
     String expected = new String(outputStream.toByteArray(), StandardCharsets.UTF_8)
             .replaceAll("&lt;", "<")
             .replaceAll("&gt;", ">");
     assertEquals(new String(XML_DECLARATION, 0, XML_DECLARATION.length)
             + new String(encoder._encodingCharArray, 0, encoder._encodingCharArray.length)
             + new String(XML_DECLARATION_END, 0, XML_DECLARATION_END.length), expected);
 }

 @Test(expected = NullPointerException.class)
 public void testDeclaration_NullWriter() throws IOException {
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     encoder.declaration(null);
 }

 @Test(expected = IOException.class)
 public void testText_Writer_CharArray_Start_Length_IOException() throws IOException, NullPointerException {
     Writer writer = new Writer() {
         @Override
         public void write(char[] ch, int start, int length) throws IOException {
             throw new IOException("Test IOException");
         }
     };
     XMLEncoder encoder = new XMLEncoder(UTF_8_ENCODING);
     encoder.text(writer, "abc".toCharArray(), 0, "abc".length(), false);
 }

}