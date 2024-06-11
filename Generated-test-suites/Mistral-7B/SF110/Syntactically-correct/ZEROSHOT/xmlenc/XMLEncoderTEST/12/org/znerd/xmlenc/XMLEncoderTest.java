import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class XMLEncoderTest {

 private static final String ENCODING_UTF_8 = "UTF-8";
 private static final String ENCODING_US_ASCII = "US-ASCII";

 @Test
 public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
     XMLEncoder encoderUtf8 = XMLEncoder.getEncoder(ENCODING_UTF_8);
     Assert.assertEquals(ENCODING_UTF_8, encoderUtf8.getEncoding());

     XMLEncoder encoderUSASCII = XMLEncoder.getEncoder(ENCODING_US_ASCII);
     Assert.assertEquals(ENCODING_US_ASCII, encoderUSASCII.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() {
     XMLEncoder encoder = null;
     try {
         encoder = XMLEncoder.getEncoder(null);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("encoding == null", e.getMessage());
     }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() {
     XMLEncoder encoder = null;
     try {
         encoder = XMLEncoder.getEncoder("ISO-1234-5");
     } catch (IllegalArgumentException e) {
         // Should not reach here
     } catch (UnsupportedEncodingException e) {
         Assert.assertEquals("ISO-1234-5", e.getMessage());
     }
 }

 @Test
 public void testDeclaration() throws IOException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoderUtf8 = XMLEncoder.getEncoder(ENCODING_UTF_8);
     encoderUtf8.declaration(writer);
     String declarationUtf8 = writer.toString();
     Assert.assertEquals("<?xml version=\"1.0\" encoding=\"" + ENCODING_UTF_8 + "\"?>\n", declarationUtf8);

     StringWriter writerUsaascii = new StringWriter();
     XMLEncoder encoderUSASCII = XMLEncoder.getEncoder(ENCODING_US_ASCII);
     encoderUSASCII.declaration(writerUsaascii);
     String declarationUSASCII = writerUsaascii.toString();
     Assert.assertEquals("<?xml version=\"1.0\" encoding=\"" + ENCODING_US_ASCII + "\"?>\n", declarationUSASCII);
 }

 @Test
 public void testText() throws IOException, InvalidXMLException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoderUtf8 = XMLEncoder.getEncoder(ENCODING_UTF_8);

     encoderUtf8.text(writer, "Hello, World!", false);
     String textHelloWorld = writer.toString();
     Assert.assertEquals("Hello, World!\n", textHelloWorld);

     encoderUtf8.text(writer, "Hello, <World>!", false);
     String textHelloWorldWithEntity = writer.toString();
     Assert.assertEquals("Hello, &lt;World&gt;!\n", textHelloWorldWithEntity);

     encoderUtf8.text(writer, "<", false);
     String textLessThan = writer.toString();
     Assert.assertEquals("&lt;\n", textLessThan);

     encoderUtf8.text(writer, ">", false);
     String textGreaterThan = writer.toString();
     Assert.assertEquals("&gt;\n", textGreaterThan);

     encoderUtf8.text(writer, "&", false);
     String textAmpersand = writer.toString();
     Assert.assertEquals("&amp;\n", textAmpersand);

     encoderUtf8.text(writer, "\"", false);
     String textQuote = writer.toString();
     Assert.assertEquals("&quot;\n", textQuote);

     encoderUtf8.text(writer, "&amp;", false);
     String textAmpersandEntity = writer.toString();
     Assert.assertEquals("&amp;amp;\n", textAmpersandEntity);
 }

 @Test
 public void testTextEscapeAmpersands() throws IOException, InvalidXMLException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoderUtf8 = XMLEncoder.getEncoder(ENCODING_UTF_8);

     encoderUtf8.text(writer, "Hello, &World!", true);
     String textHelloWorldWithEscapedAmpersand = writer.toString();
     Assert.assertEquals("Hello, &amp;&World;\n", textHelloWorldWithEscapedAmpersand);
 }

 @Test
 public void testTextSevenBitEncoding() throws IOException, InvalidXMLException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoderUSASCII = XMLEncoder.getEncoder(ENCODING_US_ASCII);

     encoderUSASCII.text(writer, "Hello, World!", false);
     String textHelloWorldSevenBit = writer.toString();
     Assert.assertEquals("Hello, World!\n", textHelloWorldSevenBit);

     encoderUSASCII.text(writer, "Hello, <World>!", false);
     String textHelloWorldWithEntitySevenBit = writer.toString();
     Assert.assertEquals("Hello, &lt;World&gt;\n", textHelloWorldWithEntitySevenBit);

     encoderUSASCII.text(writer, "&", false);
     String textAmpersandSevenBit = writer.toString();
     Assert.assertEquals("&amp;\n", textAmpersandSevenBit);

     encoderUSASCII.text(writer, "\"", false);
     String textQuoteSevenBit = writer.toString();
     Assert.assertEquals("&quot;\n", textQuoteSevenBit);

     encoderUSASCII.text(writer, "&amp;", false);
     String textAmpersandEntitySevenBit = writer.toString();
     Assert.assertEquals("&amp;amp;\n", textAmpersandEntitySevenBit);
 }

 @Test
 public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
     StringWriter writer = new StringWriter();
     XMLEncoder encoderUtf8 = XMLEncoder.getEncoder(ENCODING_UTF_8);

     byte[] invalidCharacter = new byte[] { (byte) 0x1234 };
     char[] ch = new char[1];
     ch[0] = (char) StandardCharsets.UTF_8.decode(new ByteArrayInputStream(invalidCharacter)).read();

     encoderUtf8.text(writer, ch, 0, 1, false);
     String textInvalidCharacter = writer.toString();
     Assert.assertEquals("", textInvalidCharacter);
     Assert.assertThrows(InvalidXMLException.class, () -> encoderUtf8.text(writer, ch, 0, 1, false));
 }
}