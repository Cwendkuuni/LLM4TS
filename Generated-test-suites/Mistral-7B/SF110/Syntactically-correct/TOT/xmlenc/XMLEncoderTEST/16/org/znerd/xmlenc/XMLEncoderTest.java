import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker; // Assuming XMLChecker is a separate utility class

public class XMLEncoderTest {

 private static final String UTF8_ENCODING = "UTF-8";
 private static final String US_ASCII_ENCODING = "US-ASCII";

 @Test
 public void testGetter() {
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 Assert.assertEquals(UTF8_ENCODING, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("encoding == null", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncodingUTF16() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder("UTF-16");
 } catch (IllegalArgumentException e) {
 // Ignored
 } catch (UnsupportedEncodingException e) {
 Assert.assertEquals("UTF-16", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncodingISO8859() {
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder("ISO-8859-1");
 } catch (IllegalArgumentException e) {
 // Ignored
 } catch (UnsupportedEncodingException e) {
 Assert.assertEquals("ISO-8859-1", e.getMessage());
 }
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.declaration(out);
 String expected = "<?xml version=\"1.0\" encoding=\"" + UTF8_ENCODING + "\"?>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 // Valid text
 encoder.text(out, "Valid text");
 Assert.assertEquals("Valid text", out.toString());

 // Text with escape characters
 encoder.text(out, "&lt; &gt; &amp; &apos; &quot; &quot; &#123; &amp;#65; &quot;", false);
 Assert.assertEquals("&lt; &gt; &amp; &apos; &quot; &quot; &#123; &amp;#65; &quot;", out.toString());
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacter() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 // Invalid character although UTF-8 supports it
 encoder.text(out, '\u0080');
 }

 @Test
 public void testTextWithUSASCIIEncoding() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(US_ASCII_ENCODING);
 // Valid text
 encoder.text(out, "Valid text");
 Assert.assertEquals("Valid text", out.toString());

 // Text with escape characters
 encoder.text(out, "&lt; &gt; &amp; &apos; &quot; &quot; &amp;#65; &quot;", false);
 Assert.assertEquals("&amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;quot; &amp;#65; &amp;quot;", out.toString());
 }

 @Test
 public void testWhitespace() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.whitespace(out, " ");
 Assert.assertEquals(" ", out.toString());
 }

 @Test
 public void testAttribute() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
 encoder.attribute(out, "name", "value", '"', false);
 Assert.assertEquals(" name=\"value\"", out.toString());

 encoder.attribute(out, "name2", "value2", '\'', true);
 Assert.assertEquals(" name2=\"value2\"", out.toString());
 }
}