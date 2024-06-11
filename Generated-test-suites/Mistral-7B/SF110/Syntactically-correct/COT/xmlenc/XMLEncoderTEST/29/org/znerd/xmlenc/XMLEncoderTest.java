package org.znerd.xmlenc;

import static org.junit.Assert.*;

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
 public void setup() {
 writer = new StringWriter();
 encoder = XMLEncoder.getEncoder("UTF-8");
 }

 @Test
 public void testGetEncoder() {
 String encoding = "UTF-8";
 XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, actualEncoder.getEncoding());
 }

 @Test
 public void testDeclaration() throws Exception {
 encoder.declaration(writer);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoder.getEncoding() + "\"?>";
 assertEquals(expectedDeclaration, writer.toString());
 }

 @Test
 public void testTextNullText() throws Exception {
 encoder.text(writer, null, 0, 0, false);
 assertEquals( "", writer.toString() );
 }

 @Test
 public void testTextEmptyString() throws Exception {
 encoder.text(writer, "", 0, 0, false);
 assertEquals( "", writer.toString() );
 }

 @Test
 public void testTextSingleCharacter() throws Exception {
 char c = 'A';
 encoder.text(writer, c);
 assertEquals(c + "", writer.toString());
 }

 @Test
 public void testTextMultipleCharacters() throws Exception {
 String text = "Hello World!";
 encoder.text(writer, text.toCharArray(), 0, text.length(), false);
 assertEquals(text, writer.toString());
 }

 @Test
 public void testTextMultipleCharactersWithEscaping() throws Exception {
 encoder.text(writer, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><element & attr='value'></element></root>".toCharArray(), 0, 46, true);
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><element &amp; attr='value'></element></root>", writer.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testTextNullWriter() throws Exception {
 encoder.text(null, "Hello World!".toCharArray(), 0, "Hello World!".length(), false);
 }

 @Test(expected = IOException.class)
 public void testTextIOException() throws Exception {
 StringWriter writer = new StringWriter();
 writer.close();
 encoder.text(writer, "Hello World!".toCharArray(), 0, "Hello World!".length(), false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextNegativeLength() throws Exception {
 char[] ch = "Hello World!".toCharArray();
 encoder.text(writer, ch, -1, 1, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 try {
 XMLEncoder.getEncoder(null);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 try {
 XMLEncoder.getEncoder("ASCIIIII");
 fail();
 } catch (UnsupportedEncodingException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXMLEncoderNullEncoding() {
 XMLEncoder encoder = new XMLEncoder(null);
 fail();
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testXMLEncoderUnsupportedEncoding() {
 XMLEncoder encoder = new XMLEncoder("ASCIIIII");
 fail();
 }

 @Test
 public void testTextWithSevenBitEncoding() throws Exception {
 encoder = new XMLEncoder("ASCII");
 String text = "Hello World! ≠ ≤ ≥ ≉ ≈ ∑ ";
 encoder.text(writer, text.toCharArray(), 0, text.length(), false);
 assertEquals(text, writer.toString());
 }

 @Test
 public void testTextWithSevenBitEncodingAndEscaping() throws Exception {
 encoder = new XMLEncoder("ASCII");
 String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><element ≠ ≤ ≥ ≉ ≈ ∑ &amp; attr='value'></element></root>";
 encoder.text(writer, text.toCharArray(), 0, text.length(), true);
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<?xml version=\"1.0\" encoding=\"ASCII\"?>\r\n<root><element &lt;=&gt;!=&amp;le;&amp;ge;&amp;ne;&amp;approx;&amp;sum;&amp;amp; attr='value'></element></root>", writer.toString());
 }

 @Test
 public void testAttributeNullName() throws Exception {
 encoder.attribute(writer, null, "value", '\'', false);
 fail();
 }

 @Test
 public void testAttributeEmptyName() throws Exception {
 encoder.attribute(writer, "", "value", '\'', false);
 fail();
 }

 @Test
 public void testAttributeNullValue() throws Exception {
 encoder.attribute(writer, "name", null, '\'', false);
 fail();
 }

 @Test
 public void testAttributeEmptyValue() throws Exception {
 encoder.attribute(writer, "name", "", '\'', false);
 assertEquals( "name=\"\"", writer.toString() );
 }

 @Test
 public void testAttributeQuotationMarkMatch() throws Exception {
 encoder.attribute(writer, "name", "\"value\"", '\"', false);
 assertEquals( "name=\"value\"", writer.toString() );
 }

 @Test
 public void testAttributeQuotationMarkMismatch() throws Exception {
 encoder.attribute(writer, "name", "value'", '\"', false);
 assertEquals( "name=\"value'\">", writer.toString() );
 }

 @Test
 public void testAttributeEscapedQuotationMark() throws Exception {
 encoder.attribute(writer, "name", "value&quot;", '\"', false);
 assertEquals( "name=\"value&quot;\"", writer.toString() );
 }

 @Test
 public void testAttributeEscapeAmpersands() throws Exception {
 encoder.attribute(writer, "name", "value&amp;", '\"', false);
 assertEquals( "name=\"value&amp;\"", writer.toString() );
 }

 @Test
 public void testAttributeWithEscaping() throws Exception {
 encoder.attribute(writer, "name", "value &amp; attr='value&quot;'", '\"', true);
 assertEquals( "<name value=\"value &amp; attr='value&quot;'/>", writer.toString() );
 }

 @Test
 public void testAttributeWrapWhitespace() throws Exception {
 encoder.whitespace(writer, " ");
 encoder.attribute(writer, "name", "value", '\'', false);
 encoder.whitespace(writer, " ");
 assertEquals( " name=\"value\"", writer.toString() );
 }

 @Test
 public void testAttributeWrapWhitespaceWithEscaping() throws Exception {
 encoder.whitespace(writer, " ");
 encoder.attribute(writer, "name", "value &amp; attr='value&quot;'", '\'', true);
 encoder.whitespace(writer, " ");
 assertEquals( "<name value=\"value &amp; attr='value&quot;'\" />", writer.toString() );
 }

 @Test
 public void testWhitespaceNullString() throws Exception {
 encoder.whitespace(writer, null);
 assertEquals( "", writer.toString() );
 }

 @Test
 public void testWhitespaceEmptyString() throws Exception {
 encoder.whitespace(writer, "");
 assertEquals( "", writer.toString() );
 }

 @Test
 public void testWhitespaceSingleCharacter() throws Exception {
 encoder.whitespace(writer, " ");
 assertEquals( " ", writer.toString() );
 }

 @Test
 public void testWhitespaceMultipleCharacters() throws Exception {
 encoder.whitespace(writer, "  \t\r\n");
 assertEquals( "\t\r\n", writer.toString() );
 }

 @Test
 public void testWhitespaceMultipleCharactersWithEscaping() throws Exception {
 encoder.whitespace(writer, " &nbsp; &nbsp; &nbsp; &nbsp;");
 assertEquals(" &nbsp; &nbsp; &nbsp; &nbsp;", writer.toString());
 }
}