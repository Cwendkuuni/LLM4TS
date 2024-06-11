import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer writer;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() throws IOException {
 writer = Mockito.mock(Writer.class);
 outputStream = new ByteArrayOutputStream();
 Mockito.when(writer.write(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt()))
 .then(invocation -> outputStream.write(invocation.getArguments()[0], 0, (int) invocation.getArguments()[1]));
 }

 @Test
 public void testGetEncoder() throws UnsupportedEncodingException, IllegalArgumentException {
 String encoding = "UTF-8";
 XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, actualEncoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 String encoding = null;
 XMLEncoder actualEncoder = null;
 try {
 actualEncoder = XMLEncoder.getEncoder(encoding);
 } catch (IllegalArgumentException e) {
 assertEquals("encoding == null", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 String encoding = "ABCDEFG";
 XMLEncoder actualEncoder = null;
 try {
 actualEncoder = XMLEncoder.getEncoder(encoding);
 } catch (Exception e) {
 assertEquals("ABCDEFG", e.getMessage());
 }
 }

 @Test
 public void testDeclaration() throws IOException {
 String encoding = "UTF-8";
 encoder = new XMLEncoder(encoding);
 encoder.declaration(writer);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
 assertEquals(expectedDeclaration, outputStream.toString());
 }

 @Test
 public void testTextWithEscaping() throws IOException {
 encoder = new XMLEncoder("UTF-8");
 String text = "&<'>\"";
 encoder.text(writer, text, true);
 String expectedText = "&amp;&lt;&gt;&gt;&apos;&quot;";
 assertEquals(expectedText, outputStream.toString());
 }

 @Test
 public void testTextWithoutEscaping() throws IOException {
 encoder = new XMLEncoder("UTF-8");
 String text = "abcdefg";
 encoder.text(writer, text, false);
 String expectedText = "abcdefg";
 assertEquals(expectedText, outputStream.toString());
 }

 @Test
 public void testTextWithInvalidCharacter() throws IOException {
 encoder = new XMLEncoder("UTF-8");
 char invalidCharacter = '\u00FF';
 encoder.text(writer, invalidCharacter);
 String expectedErrorMessage = "The character 0xFF is not valid.";
 try {
 encoder.text(writer, invalidCharacter, false);
 } catch (InvalidXMLException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testTextWithSevenBitEncoding() throws IOException {
 encoder = new XMLEncoder("ASCII");
 String text = "abcdefg";
 encoder.text(writer, text, false);
 String expectedText = "abcdefg";
 assertEquals(expectedText, outputStream.toString());
 }

 @Test
 public void testWhitespace() throws IOException {
 String whitespace = " \t\r\n";
 encoder = new XMLEncoder("UTF-8");
 encoder.whitespace(writer, whitespace);
 String expectedWhitespace = " \t\r\n";
 assertEquals(expectedWhitespace, outputStream.toString());
 }

 @Test
 public void testAttribute() throws IOException {
 encoder = new XMLEncoder("UTF-8");
 String name = "name";
 String value = "value &<'>\"";
 char quotationMark = '\"';
 encoder.attribute(writer, name, value, quotationMark, true);
 String expectedAttribute = " name=\"value &amp;&lt;&gt;&gt;&apos;&quot;\"" +
 "\"";
 assertEquals(expectedAttribute, outputStream.toString());
 }

}