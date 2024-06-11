import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer outputStream;

 @Before
 public void setUp() throws UnsupportedEncodingException {
 encoder = XMLEncoder.getEncoder("UTF-8");
 outputStream = Mockito.mock(Writer.class);
 }

 @After
 public void tearDown() throws IOException {
 if (outputStream != null) {
 outputStream.close();
 }
 }

 @Test
 public void testGetEncoder() throws UnsupportedEncodingException {
 String encoding = "UTF-8";
 XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
 assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
 XMLEncoder encoder = XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
 String encoding = "ISO-8859-12";
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(encoding);
 } catch (IllegalArgumentException e) {
 // expected
 }
 XMLEncoder.getEncoder(encoding);
 }

 @Test
 public void testDeclaration() throws IOException {
 encoder.declaration(outputStream);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 String actualDeclaration = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 byte[] input = "This is a test string with <special> characters: <, >, &, ' and ".getBytes();
 encoder.text(outputStream, input, 0, input.length, false);
 String expectedOutput = "This is a test string with &lt;special&gt;characters: &amp;lt; &amp;gt; &amp;amp; &apos; &quot;";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
 byte[] input = "This is a test string with <special> characters: <, >, &, ' and &amp;".getBytes();
 encoder.text(outputStream, input, 0, input.length, true);
 String expectedOutput = "This is a test string with &lt;special&gt;characters: &amp;lt; &amp;gt; &amp;amp; &apos; &quot; &amp;amp;amp;";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTextSingleCharacter() throws IOException, InvalidXMLException {
 char c = '<';
 encoder.text(outputStream, c);
 String expectedOutput = "&lt;";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTextSingleCharacterWithEscapeAmpersands() throws IOException, InvalidXMLException {
 char c = '&';
 encoder.text(outputStream, c, true);
 String expectedOutput = "&amp;amp;";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
 String input = "  Test string   ";
 encoder.whitespace(outputStream, input);
 String expectedOutput = "  Test string   ";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAttribute() throws IOException, InvalidXMLException {
 String name = "test";
 String value = "&amp;<>&'\"";
 char quotationMark = '\'';
 encoder.attribute(outputStream, name, value, quotationMark, false);
 String expectedOutput = " test=\"&amp;<>&apos;&quot;\">";
 String actualOutput = new String(getByteArrayFromOutputStream(outputStream), Locale.ENGLISH);
 assertEquals(expectedOutput, actualOutput);
 }

 private byte[] getByteArrayFromOutputStream(Writer writer) throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 Writer out = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
 writer.write(out);
 out.flush();
 byte[] byteArray = byteArrayOutputStream.toByteArray();
 return byteArray;
 }
}