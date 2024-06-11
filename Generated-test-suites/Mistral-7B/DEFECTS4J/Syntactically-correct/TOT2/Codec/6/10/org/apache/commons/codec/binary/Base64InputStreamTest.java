import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

 @Test
 public void testBase64InputStream() {
 Base64 base64 = new Base64();
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);

 byte[] expectedData = base64.decode(base64.encode(TEST_DATA));
 byte[] readData = new byte[TEST_DATA.length];
 int actualLength = base64InputStream.read(readData, 0, TEST_DATA.length);

 assertEquals(TEST_DATA.length, actualLength);
 assertArrayEquals(expectedData, readData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputStream() {
 new Base64InputStream(null, false);
 }

 @Test
 public void testEmptyInputStream() throws IOException {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
 byte[] readData = new byte[0];
 int actualLength = base64InputStream.read(readData, 0, 0);

 assertNull(readData);
 assertEquals(-1, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeOffset() {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] readData = new byte[TEST_DATA.length];
 int offset = -1;
 int length = TEST_DATA.length;
 int actualLength = base64InputStream.read(readData, offset, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLength() {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] readData = new byte[TEST_DATA.length];
 int offset = 0;
 int length = -1;
 int actualLength = base64InputStream.read(readData, offset, length);
 }

 @Test(expected = IOException.class)
 public void testThrowsIOException() throws IOException, InterruptedException {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] readData = new byte[TEST_DATA.length];
 int offset = 0;
 int length = TEST_DATA.length;
 int actualLength = base64InputStream.read(readData, offset, length);

 assertArrayEquals(TEST_DATA, readData);
 Thread.sleep(100); // Let the underlying InputStream buffer be cleared
 actualLength = base64InputStream.read(readData, offset, length);

 assertEquals(-1, actualLength);
 }
}