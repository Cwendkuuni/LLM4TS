import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte testByte = 0x61; // 'a'
 byte[] encoded = codec.encode(new byte[]{ testByte });
 assertEquals(2, encoded.length);
 assertEquals(Hex.encodeHexString(new byte[]{ ESCAPE_CHAR, '61' }), new String(encoded));
 }

 @Test
 public void testEncodeSeveralBytes() {
 byte[] testBytes = { 0x61, 0x62, 0x63 }; // 'abd'
 byte[] encoded = codec.encode(testBytes);
 assertEquals(6, encoded.length);
 assertEquals(Hex.encodeHexString(new byte[]{ ESCAPE_CHAR, '61', ESCAPE_CHAR, '62', ESCAPE_CHAR, '63' }), new String(encoded));
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String testString = "abc";
 byte[] encodedBytes = codec.encode(testString.getBytes(CharEncoding.UTF_8));
 String encodedString = new String(encodedBytes, CharEncoding.UTF_8);
 assertEquals("=61=62=63", encodedString);
 }

 @Test
 public void testEncodeEdgeCases() throws UnsupportedEncodingException {
 String testString = "\u0000\u000F\u001F\u007F\u0080\u00FF";
 byte[] encodedBytes = codec.encode(testString.getBytes(CharEncoding.UTF_8));
 String expected = "=\u0000=\u000F=\u001F=\u007F=\u0080=\u00FF";
 assertEquals(expected, new String(encodedBytes, CharEncoding.UTF_8));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testEncodeWithInvalidCharset() throws UnsupportedEncodingException {
 String testString = "abc";
 byte[] encodedBytes = codec.encode(testString.getBytes("invalidCharset"));
 }

 @Test
 public void testDecodeBytes() throws DecoderException {
 byte[] encoded = new byte[]{ ESCAPE_CHAR, '61', ESCAPE_CHAR, '62', ESCAPE_CHAR, '63' };
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(new byte[]{ 0x61, 0x62, 0x63 }, decoded);
 }

 @Test
 public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
 String encodedString = "=61=62=63";
 byte[] decodedBytes = codec.decode(encodedString.getBytes(CharEncoding.UTF_8));
 assertEquals("abc", new String(decodedBytes, CharEncoding.UTF_8));
 }

 @Test
 public void testDecodeEmptyString() throws DecoderException, UnsupportedEncodingException {
 String encodedString = "";
 byte[] decodedBytes = codec.decode(encodedString.getBytes(CharEncoding.UTF_8));
 assertEquals("", new String(decodedBytes, CharEncoding.UTF_8));
 }

 @Test
 public void testDecodeStringWithInvalidChars() throws DecoderException {
 String encodedString = "=61=62=64"; // 'd' instead of '3'
 byte[] decodedBytes = codec.decode(encodedString.getBytes(CharEncoding.UTF_8));
 assertEquals("", new String(decodedBytes, CharEncoding.UTF_8));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeWithInvalidData() throws DecoderException {
 String encodedString = "=61=62=65=\u0000"; // invalid character '\u0000'
 byte[] decodedBytes = codec.decode(encodedString.getBytes(CharEncoding.UTF_8));
 }

 @Test
 public void testDecodeStringWithEmptyData() throws DecoderException {
 String encodedString = "=";
 byte[] decodedBytes = codec.decode(encodedString.getBytes(CharEncoding.UTF_8));
 assertEquals("", new String(decodedBytes, CharEncoding.UTF_8));
 }
}