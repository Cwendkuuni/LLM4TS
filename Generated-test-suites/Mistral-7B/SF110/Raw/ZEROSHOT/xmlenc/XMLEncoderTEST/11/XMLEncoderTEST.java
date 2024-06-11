package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer writer;
 private ByteArrayOutputStream outputStream;

 @Test
 public void testGetEncoder() {
     String encoding = "UTF-8";
     encoder = XMLEncoder.getEncoder(encoding);
     assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     String encoding = null;
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(encoding));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     String encoding = "ABC";
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder(encoding));
 }

 @Test
 public void testDeclaration() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     encoder.declaration(writer);
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
     byte[] outputBytes = outputStream.toByteArray();
     assertEquals(expectedDeclaration.getBytes(StandardCharsets.UTF_8), outputBytes);
 }

 @Test
 public void testTextWithEscapeAmpersandsFalse() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String text = "The <product> has a &price=10.99& name and a &quantity=5& code.";
     encoder.text(writer, text, false);
     byte[] outputBytes = outputStream.toByteArray();
     String expectedOutput = "The The &amp;lt;product&amp;gt; has a &amp;amp;price=10.99&amp; amp;amp;name and a &amp;amp;quantity=5&amp; amp;amp;code.";
     assertEquals(expectedOutput, new String(outputBytes, StandardCharsets.UTF_8));
 }

 @Test
 public void testTextWithEscapeAmpersandsTrue() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String text = "The <product> has a &price=10.99& name and a &quantity=5& code.";
     encoder.text(writer, text, true);
     byte[] outputBytes = outputStream.toByteArray();
     String expectedOutput = "The The &lt;product&gt; has a &amp;&amp;lt;price=10.99&amp;&amp;amp;amp;amp;gt;=10.99&amp;&amp;amp;amp;amp;amp;gt; and a &amp;&amp;lt;quantity=5&amp;&amp;amp;amp;amp;gt;=5&amp;&amp;amp;amp;amp;amp;gt; code.";
     assertEquals(expectedOutput, new String(outputBytes, StandardCharsets.UTF_8));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextOutOfBounds() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String text = "The <product>";
     encoder.text(writer, text.toCharArray(), 5, text.length(), false);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws IOException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String text = "The <product> has a &price=10.99& name and a &quantity=5& code.\u0001";
     encoder.text(writer, text.toCharArray(), false);
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String whitespace = "   ";
     encoder.whitespace(writer, whitespace);
     byte[] outputBytes = outputStream.toByteArray();
     assertEquals("   ".getBytes(StandardCharsets.UTF_8), outputBytes);
 }

 @Test(expected = NullPointerException.class)
 public void testWhitespaceNull() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String whitespace = null;
     encoder.whitespace(writer, whitespace);
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String name = "test";
     String value = "Test value";
     char quotationMark = '\"';
     encoder.attribute(writer, name, value, quotationMark, false);
     byte[] outputBytes = outputStream.toByteArray();
     String expectedOutput = "<" + name + " " + name + "=\"" + value + "\" />";
     assertEquals(expectedOutput, new String(outputBytes, StandardCharsets.UTF_8));
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeNullName() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String name = null;
     String value = "Test value";
     char quotationMark = '\"';
     encoder.attribute(writer, name, value, quotationMark, false);
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeNullValue() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String name = "test";
     String value = null;
     char quotationMark = '\"';
     encoder.attribute(writer, name, value, quotationMark, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeQuotationMarkNotDoubleOrSingleQuote() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String name = "test";
     String value = "Test value";
     char quotationMark = '`';
     encoder.attribute(writer, name, value, quotationMark, false);
 }

 @Test(expected = IOException.class)
 public void testWriterNull() throws InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     String text = "The <product> has a &price=10.99& name and a &quantity=5& code.";
     try {
         encoder.text(null, text.toCharArray(), false);
     } catch (NullPointerException e) {
         throw new AssertionError("Expected IOException, but got " + e.getClass().getName());
     }
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter2() throws IOException, InvalidXMLException {
     encoder = XMLEncoder.getEncoder("UTF-8");
     outputStream = new ByteArrayOutputStream();
     writer = new Writer() {
         @Override
         public void write(int c, int length) throws IOException {
             outputStream.write(c);
         }

         @Override
         public void flush() throws IOException {
             // Do nothing
         }

         @Override
         public void close() throws IOException {
             // Do nothing
         }
     };
     String text = "The <product> has a &price=10.99& name and a &quantity=5& code.\u0002";
     encoder.text(writer, text.toCharArray(), false);
 }
}