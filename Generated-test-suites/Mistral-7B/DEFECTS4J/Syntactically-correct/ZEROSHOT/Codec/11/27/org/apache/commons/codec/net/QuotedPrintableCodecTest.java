import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.BytesUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeDecodeByte() throws Exception {
 byte[] input = BytesUtil.toByteArray("HelloWorld!".getBytes());
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertEquals(input.length, decoded.length);
 for (int i = 0; i < input.length; i++) {
 assertEquals(input[i], decoded[i]);
 }
 }

 @Test
 public void testEncodeDecodeString() throws Exception {
 String input = "HelloWorld!";
 String encoded = codec.encode(input);
 String decoded = codec.decode(encoded, codec.getDefaultCharset());
 assertEquals(input, decoded);
 }

 @Test
 public void testEncodeDecodeStringWithCustomCharset() throws Exception {
 String input = "HelloWorld!";
 String encoded = codec.encode(input, "UTF-8");
 String decoded = codec.decode(encoded, "UTF-8");
 assertEquals(input, decoded);
 }

 @Test
 public void testEncodeBinaryData() throws Exception {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeWhitespace() throws Exception {
 byte[] input = BytesUtil.toByteArray(" \tHelloWorld! ");
 byte[] encoded = codec.encode(input);
 assertEquals(encoded.length, input.length + 2 * 3); // 2 spaces and 1 tab encoded
 byte[] decoded = codec.decode(encoded);
 assertEquals(input.length, decoded.length);
 for (int i = 0; i < input.length; i++) {
 assertEquals(input[i], decoded[i]);
 }
 }

 @Test
 public void testEncodeCRLF() throws Exception {
 byte[] input = BytesUtil.toByteArray("\r\nHelloWorld!\r\n");
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertEquals(input.length, decoded.length);
 for (int i = 0; i < input.length; i++) {
 assertEquals(input[i], decoded[i]);
 }
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidData() throws Exception {
 byte[] input = BytesUtil.toByteArray("HelloWorld!=\r\n");
 byte[] decoded = codec.decode(BytesUtil.concatenate(input, new byte[]{0x01}));
 fail("DecoderException expected");
 }
}