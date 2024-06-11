import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeAndDecodeByteArray() throws Exception {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeAndDecodeString() throws Exception {
 String input = "This is a test string.";
 String encoded = codec.encode(input);
 String decoded = codec.decode(encoded.getBytes()).toString();

 Assert.assertEquals(input, decoded);
 }

 @Test
 public void testEncodeAndDecodeStringWithCharset() throws Exception {
 String input = "This is a test string.";
 String encoded = codec.encode(input.getBytes("UTF-8"));
 String decoded = new String(codec.decode(encoded.getBytes()), "UTF-8");

 Assert.assertEquals(input, decoded);
 }

 @Test
 public void testEncodeSingleByte() throws Exception {
 byte[] input = {0x20}; // space
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeCRAndLF() throws Exception {
 byte[] input = {0x0D, 0x0A}; // CR and LF
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);

 Assert.assertArrayEquals(input, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalid() throws Exception {
 byte[] input = {0x0D, 0x61, 0x6D, 0x65, 0x74, 0x68, 0x69, 0x6E, 0x67, 0x6C, 0x65}; // "answer" instead of CR and LF
 byte[] decoded = codec.decode(input);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalid() throws Exception {
 byte[] input = {0xFF}; // invalid byte
 byte[] encoded = codec.encode(input);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDecodeWithUnsupportedCharset() throws Exception {
 String input = "This is a test string.";
 byte[] decoded = codec.decode(input.getBytes("ISO-8859-15"));
 }
}