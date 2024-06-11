import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class XMLEncoderTest {

 private static final String UTF_8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";
 private static final String ISO_8859_1_ENCODING = "ISO-8859-1";

 @Test
 public void testXMLEncoderInstance() throws Exception {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     Assert.assertEquals(UTF_8_ENCODING, utf8Encoder.getEncoding());
     XMLEncoder usAsciiEncoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
     Assert.assertEquals(US_ASCII_ENCODING, usAsciiEncoder.getEncoding());
     XMLEncoder iso88591Encoder = XMLEncoder.getEncoder(ISO_8859_1_ENCODING);
     Assert.assertEquals(ISO_8859_1_ENCODING, iso88591Encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
     try {
         XMLEncoder encoder = XMLEncoder.getEncoder(null);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("encoding == null", e.getMessage());
     }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncodingUTF16() {
     try {
         XMLEncoder encoder = XMLEncoder.getEncoder("UTF-16");
     } catch (Exception e) {
         Assert.assertThat(e.getClass(), is(UnsupportedEncodingException.class));
     }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncodingISO8859X() {
     try {
         XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-X");
     } catch (Exception e) {
         Assert.assertThat(e.getClass(), is(UnsupportedEncodingException.class));
     }
 }

 @Test
 public void testDeclaration() throws IOException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     utf8Encoder.declaration(writer);
     String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + UTF_8_ENCODING + "\"?>";
     Assert.assertEquals(expectedDeclaration, writer.toString());
 }

 @Test
 public void testTextWithValidCharacters() throws IOException, InvalidXMLException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String text = "The quick brown fox jumps over the lazy dog";
     utf8Encoder.text(writer, text, false);
     String expectedText = "The quick brown fox jumps over the lazy dog";
     Assert.assertEquals(expectedText, writer.toString());
 }

 @Test
 public void testTextWithEscapedCharacters() throws IOException, InvalidXMLException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String text = "The <quick&brown> fox jumps over the <lazy> dog";
     utf8Encoder.text(writer, text, true);
     String expectedText = "The &lt;quick&amp;brown&gt; fox jumps over the &lt;lazy&gt; dog";
     Assert.assertEquals(expectedText, writer.toString());
 }

 @Test
 public void testTextWithInvalidCharacters() throws IOException, InvalidXMLException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String text = "The &#x60;xml&gt; version=\"1.0\" encoding=\"" + UTF_8_ENCODING + "\"?>\n<root><element>&#x0001;</element></root>";
     utf8Encoder.text(writer, text.getBytes(StandardCharsets.UTF_8));
     String expectedText = "The &amp;#x60;xml&gt; version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>&#x0001;</element></root>";
     Assert.assertEquals(expectedText, writer.toString());
 }

 @Test
 public void testWhitespace() throws IOException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String whitespace = "   ";
     utf8Encoder.whitespace(writer, whitespace);
     Assert.assertEquals("   ", writer.toString());
 }

 @Test
 public void testAttribute() throws IOException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String name = "name";
     String value = "value";
     char quotationMark = '"';
     utf8Encoder.attribute(writer, name, value, quotationMark, false);
     String expectedAttribute = " name=\"value\"";
     Assert.assertEquals(expectedAttribute, writer.toString());
 }

 @Test
 public void testAttributeWithEscapedCharacters() throws IOException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String name = "name";
     String value = "&lt;value&gt;";
     char quotationMark = '\'';
     utf8Encoder.attribute(writer, name, value, quotationMark, true);
     String expectedAttribute = " name='&lt;value&gt;'";
     Assert.assertEquals(expectedAttribute, writer.toString());
 }

 @Test
 public void testAttributeWithQuotationMarkEqualToAttributeMark() throws IOException {
     XMLEncoder utf8Encoder = XMLEncoder.getEncoder(UTF_8_ENCODING);
     StringWriter writer = new StringWriter();
     String name = "name";
     String value = "value='&quot;'";
     char quotationMark = '&';
     utf8Encoder.attribute(writer, name, value, quotationMark, true);
     String expectedAttribute = " name='&amp;quot;'";
     Assert.assertEquals(expectedAttribute, writer.toString());
 }

}