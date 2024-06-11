import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeDecodeBytes() throws Exception {
 byte[] bytes = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x69, 0x73, 0x73, 0x6F, 0x72};
 byte[] encodedBytes = codec.encode(bytes);
 byte[] decodedBytes = codec.decode(encodedBytes);
 assertEquals(bytes, decodedBytes);
 }

 @Test
 public void testEncodeDecodeString() throws Exception {
 String original = "hello world";
 String encoded = codec.encode(original, CharEncoding.UTF_8);
 String decoded = codec.decode(encoded, CharEncoding.UTF_8);
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeDecodeObject() throws Exception {
 String original = "hello world";
 Object encodedObject = codec.encode(original);
 Object decodedObject = codec.decode(encodedObject);
 assertEquals(original, decodedObject);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidObject() throws Exception {
 QuotedPrintableCodec codec = new QuotedPrintableCodec();
 Object invalidObject = new Object() {
 };
 codec.encode(invalidObject);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidBytes() throws Exception {
 byte[] invalidBytes = {'a', 'b', 'c'};
 codec.decode(invalidBytes);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidString() throws Exception {
 String invalidString = "abcd";
 codec.decode(invalidString.getBytes(), CharEncoding.UTF_8);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDecodeInvalidCharset() throws Exception {
 String invalidString = "abcd";
 codec.decode(invalidString.getBytes(), "ISO-8859-1");
 }

 @Test(expected = DecoderException.class)
 public void testDecodeIncompleteBytes() throws Exception {
 byte[] incompleteBytes = {0x68, 0x65, 0x61};
 codec.decodeQuotedPrintable(incompleteBytes);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeIncompleteString() throws Exception {
 String incompleteString = "hello world=";
 codec.decodeQuotedPrintable(incompleteString.getBytes(), CharEncoding.UTF_8);
 }
}