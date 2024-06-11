import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.StringWriter;
import java.io.Writer;

public class XMLEncoderTest {

 private XMLEncoder encoder;
 private Writer writer;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 encoder = XMLEncoder.getEncoder("UTF-8");
 stringWriter = new StringWriter();
 writer = new StringWriter(stringWriter);
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
 public void testDeclaration() throws IOException {
 encoder.declaration(writer);
 String xmlDeclaration = stringWriter.toString();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration);
 }

 @Test
 public void testTextWithEscapes() throws IOException, InvalidXMLException {
 encoder.text(writer, "&<>'\"");
 String output = stringWriter.toString();
 assertEquals("&amp;&lt;&gt;&apos;&quot;", output);
 }

 @Test
 public void testTextWithEscapesAndSevenBitEncoding() throws IOException, InvalidXMLException {
 encoder = new XMLEncoder("ISO-8859-1");
 encoder.text(writer, "&<>'\"");
 String output = stringWriter.toString();
 assertEquals("&amp;<>&gt;&apos;&quot;", output);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextWithInvalidCharacter() throws IOException {
 encoder.text(writer, "\u0001");
 }

 @Test
 public void testTextWithCharacter() throws IOException {
 encoder.text(writer, '&');
 String output = stringWriter.toString();
 assertEquals("&amp;", output);
 }

 @Test
 public void testTextWithCharacterEscape() throws IOException {
 encoder.text(writer, '&', false);
 String output = stringWriter.toString();
 assertEquals("&amp;", output);
 }

 @Test
 public void testTextWithCharacterEscapeTrue() throws IOException {
 encoder.text(writer, '&', true);
 String output = stringWriter.toString();
 assertEquals("&amp;amp;", output);
 }

 @Test
 public void testWhitespace() throws IOException {
 encoder.whitespace(writer, " ");
 String output = stringWriter.toString();
 assertEquals(" ", output);
 }

 @Test
 public void testAttribute() throws IOException {
 encoder.attribute(writer, "name", "value&<>'\"", '"');
 String output = stringWriter.toString();
 assertEquals("<name value=\"&amp;&lt;&gt;&apos;&quot;\"></name>", output);
 }

 @Test
 public void testAttributeQuoteSingle() throws IOException {
 encoder.attribute(writer, "name", "value&<>'\"", '\'');
 String output = stringWriter.toString();
 assertEquals("<name value=\"&amp;&lt;&gt;&apos;'&apos;\"</name>", output);
 }
}