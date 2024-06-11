import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

 @Test
 public void testEncoderCreation() {
 String encoding = "UTF-8";
 XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
 Assert.assertNotNull(encoder);
 Assert.assertEquals(encoding, encoder.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoding() {
 XMLEncoder encoder = XMLEncoder.getEncoder(null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedEncoding() {
 String encoding = "ABCDEF";
 XMLEncoder encoder = null;
 try {
 encoder = XMLEncoder.getEncoder(encoding);
 } catch (IllegalArgumentException e) {
 // expected
 }
 Assert.assertNull(encoder);
 }

 @Test
 public void testDeclaration() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(out);
 String declaration = out.toString(StandardCharsets.UTF_8);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Assert.assertEquals(expectedDeclaration, declaration);
 }

 @Test
 public void testTextWithEscapes() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String text = "&<>'\"";
 encoder.text(out, text, false);
 String encodedText = out.toString(StandardCharsets.UTF_8);
 String expectedEncodedText = "&amp;&lt;&gt;&apos;&quot;";
 Assert.assertEquals(expectedEncodedText, encodedText);
 }

 @Test
 public void testTextWithoutEscapes() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String text = "abc&def";
 encoder.text(out, text, true);
 String encodedText = out.toString(StandardCharsets.UTF_8);
 String expectedEncodedText = "abc&def";
 Assert.assertEquals(expectedEncodedText, encodedText);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWriter() {
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.declaration(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTextWithInvalidCharacterIndex() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 char[] text = {'a', 'b', 'c'};
 encoder.text(out, text, 2, -1);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacter() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 char invalidCharacter = '\u00FF';
 encoder.text(out, invalidCharacter);
 }

 @Test(expected = NullPointerException.class)
 public void testNullText() {
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 encoder.text(null, null, 0, 0, true);
 }

 @Test
 public void testWhitespace() throws IOException, InvalidXMLException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String whitespace = "   \t\r\n";
 encoder.whitespace(out, whitespace);
 String encodedWhitespace = out.toString(StandardCharsets.UTF_8);
 String expectedEncodedWhitespace = "   \t\r\n";
 Assert.assertEquals(expectedEncodedWhitespace, encodedWhitespace);
 }

 @Test
 public void testAttribute() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "xml:version";
 String value = "1.0";
 char quotationMark = '\'';
 encoder.attribute(out, name, value, quotationMark, false);
 String encodedAttribute = out.toString(StandardCharsets.UTF_8);
 String expectedEncodedAttribute = "xml:version=\"1.0\"";
 Assert.assertEquals(expectedEncodedAttribute, encodedAttribute);
 }

 @Test
 public void testAttributeWithQuotationMark() throws IOException {
 StringWriter out = new StringWriter();
 XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
 String name = "xml:name";
 String value = "John Doe &amp;&quot;Smith&quot;";
 char quotationMark = '\"';
 encoder.attribute(out, name, value, quotationMark, false);
 String encodedAttribute = out.toString(StandardCharsets.UTF_8);
 String expectedEncodedAttribute = "xml:name=\"John Doe &amp;
 ><ampersand>&quot;Smith&quot;\"";
 Assert.assertEquals(expectedEncodedAttribute, encodedAttribute);
 }
}