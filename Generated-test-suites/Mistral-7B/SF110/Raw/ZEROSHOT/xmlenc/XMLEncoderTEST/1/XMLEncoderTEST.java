package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.StringWriter;

public class XMLEncoderTest {

 private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 private static final String XML_TEXT = "<root><text>Hello World</text></root>";
 private static final String XML_ESCAPED_TEXT = "<root><text>&lt;Hello World&gt;</text></root>";
 private static final String XML_ATTRIBUTE = "<root att='<val&quot;1&gt;'/>";
 private static final String INVALID_XML = "<root att='<val&quot;1&apos;1&gt;'/>";

 @Test
 public void testGetEncoder() throws Exception {
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     assertEquals("UTF-8", encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ISO-10001-1"));
 }

 @Test
 public void testDeclaration() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.declaration(writer);
     assertEquals(XML_DECLARATION, writer.toString());
 }

 @Test
 public void testText() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.text(writer, "Hello World");
     assertEquals(XML_TEXT, writer.toString());
 }

 @Test
 public void testTextEscaped() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.text(writer, "Hello World", true);
     assertEquals(XML_ESCAPED_TEXT, writer.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextInvalidCharacter() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.text(writer, "\u0000");
 }

 @Test(expected = NullPointerException.class)
 public void testTextNullWriter() {
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     assertThrows(NullPointerException.class, () -> encoder.text(null, "Text", false));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextInvalidIndex() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     char[] text = new char[1];
     assertThrows(IndexOutOfBoundsException.class, () -> encoder.text(writer, text, 1, 0));
 }

 @Test
 public void testAttribute() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.attribute(writer, "att", "\"val&apos;1\"", '\'', true);
     assertEquals(XML_ATTRIBUTE, writer.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testAttributeInvalidXML() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     encoder.attribute(writer, null, null, '\'');
     assertThrows(InvalidXMLException.class, () -> writer.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeNullWriter() {
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     assertThrows(NullPointerException.class, () -> encoder.attribute(null, "att", "value", '\''));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAttributeInvalidIndex() throws Exception {
     StringWriter writer = new StringWriter();
     XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
     char[] value = new char[1];
     assertThrows(IndexOutOfBoundsException.class, () -> encoder.attribute(writer, "att", value, '\''));
 }
}