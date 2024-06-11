import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringWriter;
import java.io.Writer;

@RunWith(JUnit4.class)
public class XMLEncoderTest {

 private final String ENCODING_UTF_8 = "UTF-8";
 private final String ENCODING_US_ASCII = "US-ASCII";
 private final String ENCODING_ISO_8859_1 = "ISO-8859-1";
 private final String ENCODING_ISO_8859_15 = "ISO-8859-15";
 private final XMLEncoder encoderUTF8;
 private final XMLEncoder encoderUSASCII;
 private final XMLEncoder encoderISO88591;
 private final XMLEncoder encoderISO885915;

 @Before
 public void setUp() throws Exception {
 encoderUTF8 = XMLEncoder.getEncoder(ENCODING_UTF_8);
 encoderUSASCII = XMLEncoder.getEncoder(ENCODING_US_ASCII);
 encoderISO88591 = XMLEncoder.getEncoder(ENCODING_ISO_8859_1);
 encoderISO885915 = XMLEncoder.getEncoder(ENCODING_ISO_8859_15);
 }

 @Test
 public void testGetEncoder() throws Exception {
 Assert.assertNotNull(encoderUTF8);
 Assert.assertNotNull(encoderUSASCII);
 Assert.assertNotNull(encoderISO88591);
 Assert.assertNotNull(encoderISO885915);
 Assert.assertEquals(ENCODING_UTF_8, encoderUTF8.getEncoding());
 Assert.assertEquals(ENCODING_US_ASCII, encoderUSASCII.getEncoding());
 Assert.assertEquals(ENCODING_ISO_8859_1, encoderISO88591.getEncoding());
 Assert.assertEquals(ENCODING_ISO_8859_15, encoderISO885915.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEncoderNullEncoding() throws Exception {
 Assert.assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetEncoderUnsupportedEncoding() throws Exception {
 Assert.assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ABCDEF"));
 }

 @Test
 public void testDeclarationUTF8() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.declaration(out);
 final String expected = "<?xml version=\"1.0\" encoding=\"" + ENCODING_UTF_8 + "\"?>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDeclarationUSASCII() throws Exception {
 final Writer out = new StringWriter();
 encoderUSASCII.declaration(out);
 final String expected = "<?xml version=\"1.0\" encoding=\"" + ENCODING_US_ASCII + "\"?>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDeclarationISO88591() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.declaration(out);
 final String expected = "<?xml version=\"1.0\" encoding=\"" + ENCODING_ISO_8859_1 + "\"?>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDeclarationISO885915() throws Exception {
 final Writer out = new StringWriter();
 encoderISO885915.declaration(out);
 final String expected = "<?xml version=\"1.0\" encoding=\"" + ENCODING_ISO_8859_15 + "\"?>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testTextUTF8WithEscaping() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.text(out, "abc<def>123&456=789'10\"11<12&13>14\'15\"16", true);
 final String expected = "abc&lt;def&gt;123&amp;456=789&apos;10&quot;11&lt;12&amp;13&gt;14&apos;15&quot;16";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testTextUTF8WithoutEscaping() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.text(out, "abc<def>123&456=789'10\"11<12&13>", false);
 final String expected = "abc<def>123&456=789'10\"11<12&13>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testTextUSASCII() throws Exception {
 final Writer out = new StringWriter();
 encoderUSASCII.text(out, "abc<def>123&456=789'10\"11<12&13>", false);
 final String expected = "abc<def>123&456=789'10\"11<12&13>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextUTF8InvalidCharacter() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.text(out, "\u0080", false);
 Assert.assertThrows(InvalidXMLException.class, () -> out.toString());
 }

 @Test
 public void testTextISO88591() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.text(out, "abc<def>123&456=789'10\"11<12&13>", false);
 final String expected = "abc<def>123&456=789'10\"11<12&13>";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = InvalidXMLException.class)
 public void testTextISO88591InvalidCharacter() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.text(out, "\u0080", false);
 Assert.assertThrows(InvalidXMLException.class, () -> out.toString());
 }

 @Test
 public void testWhitespaceUTF8() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.whitespace(out, " abc   \t123");
 final String expected = " abc   \t123";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testWhitespaceUTF8WithQuotationMark() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.whitespace(out, "\"abc\"   \t123");
 final String expected = "\"abc\"   \t123";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeUTF8() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.attribute(out, "name", "value", '"', true);
 final String expected = " name=\"value\"";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeUTF8WithQuotationMarkInValue() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.attribute(out, "name", "value=\"value\"", '"', true);
 final String expected = " name=\"value=\"value\"\"";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeUTF8WithQuotationMarkInName() throws Exception {
 final Writer out = new StringWriter();
 encoderUTF8.attribute(out, "name=\"name\"", "value", '"', true);
 final String expected = " name=\"name\"={\"value\"}";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeUSASCII() throws Exception {
 final Writer out = new StringWriter();
 encoderUSASCII.attribute(out, "name", "value", '\'', false);
 final String expected = " name='value'";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeISO88591() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.attribute(out, "name", "value", '"', true);
 final String expected = " name=\"value\"";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeISO88591WithQuotationMarkInValue() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.attribute(out, "name", "value=\"value\"", '"', true);
 final String expected = " name=\"value=\"value\"\"";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAttributeISO88591WithQuotationMarkInName() throws Exception {
 final Writer out = new StringWriter();
 encoderISO88591.attribute(out, "name=\"name\"", "value", '"', true);
 final String expected = " name=\"name\"={\"value\"}";
 final String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

}