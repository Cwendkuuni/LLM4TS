package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {

 private XMLEncoder getEncoder(String encoding) throws UnsupportedEncodingException {
 return XMLEncoder.getEncoder(encoding);
 }

 @Test
 public void testGetEncoder() {
 XMLEncoder encoder = getEncoder("UTF-8");
 Assert.assertNotNull(encoder);
 Assert.assertEquals("UTF-8", encoder.getEncoding());

 encoder = null;
 try {
 getEncoder(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("encoding == null", e.getMessage());
 }

 encoder = getEncoder("ISO-8859-1");
 Assert.assertFalse(encoder._sevenBitEncoding);
 Assert.assertEquals("ISO-8859-1", encoder.getEncoding());

 encoder = getEncoder("ASCII");
 Assert.assertTrue(encoder._sevenBitEncoding);
 Assert.assertEquals("ASCII", encoder.getEncoding());

 encoder = null;
 try {
 getEncoder("invalidEncoding");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("invalidEncoding", e.getMessage());
 } catch (UnsupportedEncodingException e) {
 Assert.fail("Unexpected UnsupportedEncodingException");
 }
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.declaration(out);

 String declaration = out.toString();
 Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", declaration);
 }

 @Test(expected = NullPointerException.class)
 public void testDeclarationNullOutputStream() throws IOException {
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.declaration(null);
 }

 @Test
 public void testText() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(out, "Hello, World!", true);

 String text = out.toString();
 Assert.assertEquals("Hello, World!\n", text);
 }

 @Test
 public void testTextInvalidXMLChars() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 char[] invalidChars = {'<', '>', '&', '\'', '"', 128, 1};
 encoder.text(out, invalidChars, 0, invalidChars.length, true);

 String text = out.toString();
 Assert.assertEquals("<&amp;&apos;&quot;&gt;&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", text);
 }

 @Test
 public void testTextInvalidXMLEncoding() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("ISO-8859-1");
 encoder.text(out, "Hello, World!", true);

 String text = out.toString();
 Assert.assertEquals("<Hello, World!>\n", text);
 }

 @Test
 public void testTextSevenBitEncoding() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("ASCII");
 encoder.text(out, "Hello, World!", true);

 String text = out.toString();
 Assert.assertEquals("<Hello, World!>\n", text);
 }

 @Test
 public void testTextEscapedCharacters() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(out, "& < > & ' \" & & &", true);

 String text = out.toString();
 Assert.assertEquals("&amp; &lt; &gt; &amp; &apos; &quot; &amp; &amp;\n", text);
 }

 @Test
 public void testTextNoEscape() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.text(out, "& < > & ' \" &", false);

 String text = out.toString();
 Assert.assertEquals("& &lt; &gt; & &apos; &quot; &\n", text);
 }

 @Test
 public void testWhitespace() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.whitespace(out, "");
 encoder.whitespace(out, " ");
 encoder.whitespace(out, " \t\r\n\t\t");

 String text = out.toString();
 Assert.assertEquals(" ", text);
 }

 @Test
 public void testAttribute() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.attribute(out, "name", "value", '\"', true);

 String attribute = out.toString();
 Assert.assertEquals(" name=\"value\"\n", attribute);
 }

 @Test
 public void testAttributeQuotationMark() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.attribute(out, "name", "value\"value", '\"', true);

 String attribute = out.toString();
 Assert.assertEquals(" name=\"value&quot;value\"\n", attribute);
 }

 @Test
 public void testAttributeNoQuotationMark() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.attribute(out, "name", "value'value", '\'', true);

 String attribute = out.toString();
 Assert.assertEquals(" name=\"value'value\"\n", attribute);
 }

 @Test
 public void testAttributeNoEscape() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = getEncoder("UTF-8");
 encoder.attribute(out, "name", "value&amp;value", false);

 String attribute = out.toString();
 Assert.assertEquals(" name=\"value&amp;value\"\n", attribute);
 }
}