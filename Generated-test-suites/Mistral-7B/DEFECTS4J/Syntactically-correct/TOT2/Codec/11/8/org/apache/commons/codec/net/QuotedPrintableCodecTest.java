import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByteArray() {
 byte[] input = {0x68, 0x65, 0x6C, 0x6C, 0x6F};
 byte[] expectedOutput = Hex.decodeHex("426F6F6D6C6C68");
 byte[] output = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeString() {
 String input = "hello";
 String expectedOutput = "=68=65=6C=6C=6F";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStringDefaultCharset() throws UnsupportedEncodingException {
 String input = "hello";
 String expectedOutput = "=68=65=6C=6C=6F";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeByteArray() throws DecoderException {
 byte[] input = {0x42, 0x6F, 0x6F, 0x6D, 0x6C, 0x6C, 0x68};
 byte[] expectedOutput = {0x68, 0x65, 0x6C, 0x6C, 0x6F};
 byte[] output = codec.decode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeString() throws DecoderException {
 String input = "=68=65=6C=6C=6F";
 String expectedOutput = "hello";
 String output = codec.decode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringDefaultCharset() throws DecoderException, UnsupportedEncodingException {
 String input = "=68=65=6C=6C=6F";
 String expectedOutput = "hello";
 String output = codec.decode(input, codec.getDefaultCharset());
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeByteArrayInvalidData() throws DecoderException {
 byte[] input = {0x42, 0x6F, 0x6F, 0x6D, 0x6C, 0x6C, 0x67};
 byte[] output = codec.decode(input);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeStringInvalidData() throws DecoderException, UnsupportedEncodingException {
 String input = "=68=65=6C=6C=6F=";
 String output = codec.decode(input, codec.getDefaultCharset());
 }

 @Test
 public void testEncodeStringEdgeCases() {
 String input = "\u0000\u007F\n\r\b\f";
 String expectedOutput = "=\u0000=\u007F=\n=\r=\b=\f";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringEdgeCases() throws DecoderException, UnsupportedEncodingException {
 String input = "=\u0000=\u007F=\n=\r=\b=\f";
 String expectedOutput = "\u0000\u007F\n\r\b\f";
 String output = codec.decode(input, codec.getDefaultCharset());
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStringCR() {
 String input = "\r";
 String expectedOutput = "=\r";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringCR() throws DecoderException, UnsupportedEncodingException {
 String input = "=\r";
 String expectedOutput = "\r";
 String output = codec.decode(input, codec.getDefaultCharset());
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStringLF() {
 String input = "\n";
 String expectedOutput = "=\n";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringLF() throws DecoderException, UnsupportedEncodingException {
 String input = "=\n";
 String expectedOutput = "\n";
 String output = codec.decode(input, codec.getDefaultCharset());
 Assert.assertEquals(expectedOutput, output);
 }
}