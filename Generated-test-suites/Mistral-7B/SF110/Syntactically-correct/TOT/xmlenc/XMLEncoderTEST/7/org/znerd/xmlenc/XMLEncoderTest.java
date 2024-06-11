import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private StringWriter writer;

 @Before
 public void setUp() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 writer = new StringWriter();
 }

 @After
 public void tearDown() {
 try {
 writer.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeclaration() throws IOException, NullPointerException {
 encoder.declaration(writer);
 String xmlDeclaration = writer.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testTextWithValidChars() throws IOException, NullPointerException {
 encoder.text(writer, "Hello World!", true);
 String result = writer.toString();
 assertEquals("Hello World!", result);
 }

 @Test
 public void testTextWithInvalidXMLChars() throws IOException, InvalidXMLException, NullPointerException {
 encoder.text(writer, "Hello World!<>", false);
 String result = writer.toString();
 assertEquals("Hello World!&lt;&gt;", result);
 }

 @Test
 public void testTextWithSevenBitEncoding() throws IOException, NullPointerException {
 encoder = XMLEncoder.getEncoder("ASCII");
 writer.getBufferedWriter().write("Hello World!");
 writer.flush();
 String encoded = new String(writer.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
 assertEquals("Hello World!", encoded);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEncoding() throws IllegalArgumentException {
 XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
 XMLEncoder.getEncoder("ISOO-8859-X");
 }

 @Test(expected = NullPointerException.class)
 public void testNullWriter() throws IOException {
 encoder.declaration(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidCharacterIndex() throws IOException {
 String testString = " testing index out of bounds exception ";
 char[] ch = testString.toCharArray();
 encoder.text(writer, ch, -1, 1);
 }

 @Test(expected = InvalidXMLException.class)
 public void testInvalidXMLChars() throws IOException, InvalidXMLException {
 encoder.text(writer, "Hello World!\u00E9", true);
 String result = writer.toString();
 assertEquals("Hello World!&Atilde;", result);
 }
}