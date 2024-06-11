import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeDecode() throws Exception {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeDecodeString() throws Exception {
 String input = "test";
 String charset = "UTF-8";
 String encoded = codec.encode(input, charset);
 String decoded = codec.decode(encoded, charset);

 Assert.assertEquals(input, decoded);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDecodeUnsupportedCharset() throws Exception {
 String input = "test";
 String charset = "InvalidCharset";
 String encoded = codec.encode(input, "UTF-8").toString();

 // decode should throw UnsupportedEncodingException
 codec.decode(encoded, charset);
 }

 @Test
 public void testEncodeWhitespace() throws Exception {
 byte[] input = {0x0A, 0x20, 0x0D}; // LF, SP, CR
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeControlCharacters() throws Exception {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, (byte) 0x7F, 0x06, 0x07, 0x08};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeEscapedCharacters() throws Exception {
 byte[] input = {0x61, 0x62, 0x63, 0x64, 0x65, 0x3D, 0x3D, 0x66, 0x67, 0x68}; // "abcd===efg"
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeLineBreak() throws Exception {
 byte[] input = {0x0D, 0x0A}; // CR, LF
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeLineBreakWithData() throws Exception {
 byte[] input = {0x0D, 0x0A, 0x61, 0x62, 0x63}; // CR, LF, a, b, c
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeLineBreakWithDataAndWhitespace() throws Exception {
 byte[] input = {0x0D, 0x0A, 0x20, 0x61, 0x62, 0x63}; // CR, LF, SP, a, b, c
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidSequence() throws Exception {
 byte[] encoded = {0x3D, 0x3D, 0x3D, 0x3D}; // "===="

 // decode should throw DecoderException
 codec.decode(encoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidLength() throws Exception {
 byte[] encoded = {0x3D, 0x3D}; // "=="

 // decode should throw DecoderException
 codec.decode(encoded, 2);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeUnderflow() throws Exception {
 byte[] encoded = {0x3D, 0x3D, 0x3D}; // "==="

 // decode should throw DecoderException
 codec.decode(encoded, 2, new ByteArrayOutputStream(1).toByteArray());
 }

 @Test(expected = DecoderException.class)
 public void testDecodeOverflow() throws Exception {
 byte[] encoded = {0x3D, 0x3D, 0x3D}; // "==="

 // decode should throw DecoderException
 codec.decode(encoded, 5, new ByteArrayOutputStream(1).toByteArray());
 }
}