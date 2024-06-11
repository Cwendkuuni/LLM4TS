import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private InputStream inputStream;
 private Base64InputStream base64InputStream;
 private Base64 base64;

 @Test
 public void testBase64InputStream_Constructor_Default() {
 inputStream = new java.io.ByteArrayInputStream(new byte[]{"test".getBytes()});
 base64InputStream = new Base64InputStream(inputStream);

 byte[] result = new byte[4];
 int bytesRead = base64InputStream.read(result, 0, 4);
 assertEquals(3, bytesRead);
 assertArrayEquals(new byte[]{"test".getBytes()[0], "test".getBytes()[1], "test".getBytes()[2]}, result);
 }

 @Test
 public void testBase64InputStream_Constructor_Encode() {
 inputStream = new java.io.ByteArrayInputStream(new byte[]{"test".getBytes()});
 base64 = new Base64(true);
 base64InputStream = new Base64InputStream(inputStream, true, -1, null);

 byte[] result = new byte[5];
 int bytesRead = base64InputStream.read(result, 0, 5);
 assertEquals(4, bytesRead);

 byte[] expected = new byte[]{'T', 'e', 's', 't'};
 assertArrayEquals(expected, Arrays.copyOfRange(result, 0, 4));
 }

 @Test(expected = NullPointerException.class)
 public void testBase64InputStream_Constructor_NullInputStream() {
 base64InputStream = new Base64InputStream(null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_Constructor_NegativeLineLength() {
 base64 = new Base64(-1);
 base64InputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[]{"test".getBytes()}), false, -1, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_Constructor_NegativeLineSeparatorLength() {
 byte[] lineSeparator = new byte[1];
 base64 = new Base64(-1, lineSeparator);
 base64InputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[]{"test".getBytes()}), false, 76, lineSeparator);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBase64InputStream_read_Default() throws IOException {
 inputStream = new java.io.ByteArrayInputStream(new byte[]{"test\r\ntest\r\ntest".getBytes()});
 base64InputStream = new Base64InputStream(inputStream);

 byte[] result = new byte[3];
 int bytesRead = base64InputStream.read(result, 0, 3);
 assertEquals(3, bytesRead);
 assertArrayEquals(new byte[]{'t', 'e', 's'}, result);
 }

 @Test
 public void testBase64InputStream_read_CustomLineSeparator() throws IOException {
 byte[] lineSeparator = new byte[]{0x0D, 0x0A};
 inputStream = new java.io.ByteArrayInputStream(new byte[]{"test\r\ntest\r\ntest".getBytes()});
 base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);

 byte[] result = new byte[3];
 int bytesRead = base64InputStream.read(result, 0, 3);
 assertEquals(3, bytesRead);
 assertArrayEquals(new byte[]{'t', 'e', 's'}, result);
 }

 @Test
 public void testBase64InputStream_read_EOF() throws IOException {
 inputStream = new java.io.ByteArrayInputStream(new byte[]{"test\r\ntest\r\n".getBytes()});
 base64InputStream = new Base64InputStream(inputStream, false, 76, null);

 byte[] result = new byte[3];
 int bytesRead = base64InputStream.read(result, 0, 3);
 assertEquals(2, bytesRead);
 assertArrayEquals(new byte[]{'t', 'e'}, result);

 bytesRead = base64InputStream.read(result, 0, 3);
 assertEquals(-1, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStream_read_NegativeByteArrayLength() throws IOException {
 byte[] result = new byte[3];
 int bytesRead = base64InputStream.read(result, 1, 3);
 fail("Should throw IOException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBase64InputStream_read_NegativeOffset() throws IOException {
 byte[] result = new byte[3];
 int bytesRead = base64InputStream.read(result, -1, 3);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class) public void testBase64InputStream_read_NegativeLength() throws IOException { byte[] result = new byte[3]; int bytesRead = base64InputStream.read(result, 0, -1); fail("Should throw IndexOutOfBoundsException"); }

 @Test
 public void testBase64InputStream_read_InputStreamClosed() throws IOException { inputStream = new java.io.ByteArrayInputStream(new byte[]{"test\r\ntest\r\ntest".getBytes()}); base64InputStream = new Base64InputStream(inputStream, false, 76, null); inputStream.close();

 byte[] result = new byte[3]; int bytesRead = base64InputStream.read(result, 0, 3); assertEquals(-1, bytesRead); }
}