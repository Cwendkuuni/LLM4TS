import org.apache.commons.codec.Decoders;
import org.apache.commons.codec.Encoders;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

 @Test
 public void testEncodeBytes() {
 byte[] input = {0x61, 0x62, 0x63};
 byte[] expectedOutput = {0x61, 0x62, 0x63}; // No need to encode ASCII characters
 byte[] output = quotedPrintableCodec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeBytesWithNonPrintableCharacters() {
 byte[] input = {0x00, 0xFF};
 byte[] expectedOutput = new byte[]{'=', '0', '=', 'FF'};
 byte[] output = quotedPrintableCodec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "abc";
 String expectedOutput = "abc";
 String output = quotedPrintableCodec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStringWithNonPrintableCharacters() throws UnsupportedEncodingException {
 String input = "ab\0cd";
 String expectedOutput = "ab=0Cd";
 String output = quotedPrintableCodec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeBytes() throws DecoderException {
 byte[] input = new byte[]{'=', '61', '=', '62', '=', '63'};
 byte[] expectedOutput = {0x61, 0x62, 0x63};
 byte[] output = quotedPrintableCodec.decode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeBytesWithMultipleEscapes() throws DecoderException {
 byte[] input = new byte[]{'=', '31', '=', '32', '=', '33', '=', '31', '=', '32'};
 byte[] expectedOutput = {0x1, 0x2, 0x3, 0x1, 0x2};
 byte[] output = quotedPrintableCodec.decode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeBytesWithInvalidHex() throws DecoderException {
 byte[] input = new byte[]{'=', 'FF', '=', '0X'};
 // The 'X' in the third byte is not a valid hexadecimal digit
 quotedPrintableCodec.decode(input);
 }

 @Test
 public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
 String input = "ab=0Cd";
 String expectedOutput = "ab\0cd";
 String output = new String(quotedPrintableCodec.decode(input.getBytes(StandardCharsets.US_ASCII)));
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringWithTrailingWhitespace() throws UnsupportedEncodingException, DecoderException {
 String input = "ab=0Cd e";
 String expectedOutput = "ab\0cd";
 String output = new String(quotedPrintableCodec.decode(input.getBytes(StandardCharsets.US_ASCII)));
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeStringWithMissingHexChar() throws UnsupportedEncodingException, DecoderException {
 String input = "ab=";
 quotedPrintableCodec.decode(input.getBytes(StandardCharsets.US_ASCII));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeStringWithExtraHexChar() throws UnsupportedEncodingException, DecoderException {
 String input = "ab=0Cde";
 quotedPrintableCodec.decode(input.getBytes(StandardCharsets.US_ASCII));
 }
}