import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeSimple() {
 byte[] input = "Hello World".getBytes();
 byte[] output = codec.encode(input);
 String expected = "7468697320576F726C6421";
 Assert.assertEquals(Hex.encodeHexString(expected.getBytes()), Hex.encodeHexString(output));
 }

 @Test
 public void testEncodeWhitespace() {
 byte[] input = "Hello\tWorld".getBytes();
 byte[] output = codec.encode(input);
 String expected = "7468692020576F726C6421";
 Assert.assertEquals(Hex.encodeHexString(expected.getBytes()), Hex.encodeHexString(output));
 }

 @Test
 public void testEncodeCR() {
 byte[] input = "Hello\rWorld".getBytes();
 byte[] output = codec.encode(input);
 String expected = "7468692E5C20576F726C6421"; // 2E is the hex code for CR
 Assert.assertEquals(Hex.encodeHexString(expected.getBytes()), Hex.encodeHexString(output));
 }

 @Test
 public void testEncodeLF() {
 byte[] input = "Hello\nWorld".getBytes();
 byte[] output = codec.encode(input);
 String expected = "7468692E5C0A576F726C6421"; // 2E is the hex code for LF, 0A is the hex code for LF
 Assert.assertEquals(Hex.encodeHexString(expected.getBytes()), Hex.encodeHexString(output));
 }

 @Test
 public void testDecodeSimple() throws DecoderException {
 byte[] input = "7468697320576F726C6421".getBytes();
 byte[] output = codec.decode(input);
 String expected = "Hello World";
 Assert.assertEquals(expected, new String(output, codec.getDefaultCharset()));
 }

 @Test
 public void testDecodeWhitespace() throws DecoderException {
 byte[] input = "7468692020576F726C6421".getBytes();
 byte[] output = codec.decode(input);
 String expected = "Hello   World";
 Assert.assertEquals(expected, new String(output, codec.getDefaultCharset()));
 }

 @Test
 public void testDecodeTab() throws DecoderException {
 byte[] input = "7468692E5C20576F726C6421".getBytes(); // 2E is the hex code for CR, 5C is the hex code for TAB, 20 is the hex code for space
 byte[] output = codec.decode(input);
 String expected = "Hello\tWorld";
 Assert.assertEquals(expected, new String(output, codec.getDefaultCharset()));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalid() throws DecoderException {
 byte[] input = "7468692E5C756E21".getBytes(); // 756E should be "rf" which is not a valid hex code for a byte
 byte[] output = codec.decode(input);
 }
}