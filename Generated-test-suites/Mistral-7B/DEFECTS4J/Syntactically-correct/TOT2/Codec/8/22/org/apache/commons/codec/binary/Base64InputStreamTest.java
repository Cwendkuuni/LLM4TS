import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = new byte[]{'A', 'B', 'C', 'D'};

 /**
 * Test constructor with default options.
 */
 @Test
 public void testDefaultConstructor() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 assertNotNull(inputStream);
 }

 /**
 * Test constructor with encoding option.
 */
 @Test
 public void testEncodeConstructor() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
 assertNotNull(inputStream);
 }

 /**
 * Test read() method with default decoding option.
 */
 @Test
 public void testReadDefault() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 int result = inputStream.read();
 assertEquals(TEST_DATA[0], result);
 }

 /**
 * Test read() method with encoding option.
 */
 @Test
 public void testReadEncode() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
 int result = inputStream.read();
 assertNotNull(result);
 }

 /**
 * Test read(byte[], int, int) method with default decoding option.
 */
 @Test
 public void testReadDefaultArray() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 byte[] buffer = new byte[TEST_DATA.length];
 int len = inputStream.read(buffer, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, buffer);
 }

 /**
 * Test read(byte[], int, int) method with encoding option.
 */
 @Test
 public void testReadEncodeArray() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
 byte[] buffer = new byte[TEST_DATA.length * 4];
 int len = inputStream.read(buffer, 0, TEST_DATA.length * 4);
 Base64 base64 = new Base64();
 base64.decode(buffer, 0, len);
 assertArrayEquals(TEST_DATA, base64.getDecodedBuffer());
 }

 /**
 * Test markSupported() method.
 */
 @Test
 public void testMarkSupported() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 assertFalse(inputStream.markSupported());
 }

 /**
 * Test the behavior when passing invalid input to the constructor.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorArgument() {
 new Base64InputStream(null, false);
 }

 /**
 * Test the behavior when passing null input to read() method.
 */
 @Test(expected = NullPointerException.class)
 public void testNullReadArgument() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 inputStream.read(null, 0, 1);
 }

 /**
 * Test the behavior when passing invalid offset and length to read(byte[], int, int) method.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidReadArguments() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 byte[] buffer = new byte[TEST_DATA.length];
 inputStream.read(buffer, TEST_DATA.length, TEST_DATA.length);
 }

 /**
 * Test the behavior when passing invalid lineLength argument to the constructor.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLineLengthArgument() {
 new Base64InputStream(new ByteArrayInputStream(TEST_DATA), -1, null);
 }

 /**
 * Test the behavior when passing null lineSeparator argument to the constructor.
 */
 @Test(expected = NullPointerException.class)
 public void testNullLineSeparatorArgument() {
 new Base64InputStream(new ByteArrayInputStream(TEST_DATA), 0, new byte[]{'N', 'U', 'L', 'L'});
 }

 /**
 * Test the behavior when passing invalid InputStream to the constructor.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputStreamArgument() {
 new Base64InputStream(null, false);
 }
}