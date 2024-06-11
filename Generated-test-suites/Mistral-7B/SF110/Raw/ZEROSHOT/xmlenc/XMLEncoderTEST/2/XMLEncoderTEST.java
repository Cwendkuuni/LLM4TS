package org.znerd.xmlenc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer out;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 out = mock(Writer.class);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() {
 try {
 out.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetEncoder() {
 assertNotNull(encoder);
 assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(out);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoder.getEncoding() + "\"?>";
 verify(out, times(1)).write(expectedDeclaration.getBytes(), 0, expectedDeclaration.getBytes().length);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 final String text = "This is a <test> string with special characters: < & >, ' \" and ñ.";
 encoder.text(out, text, false);
 String expectedOutput = "This is a &lt;test&gt; string with special characters: &lt; &amp; &gt;, &apos; &quot; and ñ.";
 byte[] outputBytes = outputStream.toByteArray();
 assertEquals(expectedOutput, new String(outputBytes));
 }

 @Test
 public void testTextWithEscapedAmpersands() throws IOException, InvalidXMLException {
 final String text = "This is a <test> string with &amp;special characters: < & >, ' \" and ñ.";
 encoder.text(out, text, true);
 String expectedOutput = "This is a &lt;test&gt; string with &amp;amp;special characters: &lt; &amp; &gt;, &apos; &quot; and ñ.";
 byte[] outputBytes = outputStream.toByteArray();
 assertEquals(expectedOutput, new String(outputBytes));
 }

 @Test(expected = InvalidXMLException.class)
 public void testInvalidCharacter() throws IOException {
 final char invalidCharacter = (char) 0xC2;
 encoder.text(out, invalidCharacter);
 }

 @Test
 public void testWhitespace() throws IOException {
 final String whitespace = "\t Hello World!";
 encoder.whitespace(out, whitespace);
 byte[] outputBytes = outputStream.toByteArray();
 String expectedOutput = "\t Hello World!";
 assertEquals(expectedOutput, new String(outputBytes));
 }

 @Test
 public void testAttribute() throws IOException {
 final String name = "color";
 final String value = "red";
 encoder.attribute(out, name, value, '"', false);
 String expectedOutput = " color=\"red\"";
 byte[] outputBytes = outputStream.toByteArray();
 assertEquals(expectedOutput, new String(outputBytes));
 }

 @Test
 public void testAttributeWithQuote() throws IOException {
 final String name = "title";
 final String value = "The quick brown fox";
 encoder.attribute(out, name, value, '\'', true);
 String expectedOutput = " title='The quick brown fox'";
 byte[] outputBytes = outputStream.toByteArray();
 assertEquals(expectedOutput, new String(outputBytes));
 }
}