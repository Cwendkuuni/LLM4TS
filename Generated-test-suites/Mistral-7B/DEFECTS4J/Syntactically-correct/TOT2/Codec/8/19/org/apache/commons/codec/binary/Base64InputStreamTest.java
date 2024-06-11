import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 // Test constructor with default decoding
 @Test
 public void testDefaultDecodeConstructor() {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] expectedByteArray = {(byte) 0x42, (byte) 0x4d, (byte) 0x4f, (byte) 0x43, (byte) 0x41, (byte) 0x4d, (byte) 0x49, (byte) 0x4e, (byte) 0x44, (byte) 0x45, (byte) 0x4e};
 byte[] testByteArray = new byte[12];
 int bytesRead = is.read(testByteArray, 0, 12);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 // Test constructor with encoding
 @Test
 public void testEncodeConstructor() {
 Base64InputStream is = new Base64InputStream(createTestInputStream(), true);
 byte[] expectedByteArray = {(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3a};
 byte[] testByteArray = new byte[11];
 int bytesRead = is.read(testByteArray, 0, 11);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 // Test constructor with encoding and custom lineLength
 @Test
 public void testEncodeConstructorWithLineLength() {
 Base64InputStream is = new Base64InputStream(createTestInputStream(), true, 16, new byte[]{(byte) 0x0D, (byte) 0x0A});
 byte[] expectedByteArray = {(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3a, (byte) 0x0D, (byte) 0x0A};
 byte[] testByteArray = new byte[13];
 int bytesRead = is.read(testByteArray, 0, 13);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 // Test read method with EOF
 @Test(expected = IOException.class)
 public void testReadWithEOF() throws IOException {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray = new byte[1];
 is.read(testByteArray, 0, 1);
 is.read(testByteArray, 0, 1); // read twice to consume the buffer
 int bytesRead = is.read(testByteArray, 0, 1);
 fail("Expected IOException");
 }

 // Test read method with custom lineLength
 @Test
 public void testReadWithLineLength() throws IOException {
 Base64InputStream is = new Base64InputStream(createTestInputStream(), true, 16, new byte[]{(byte) 0x0D, (byte) 0x0A});
 byte[] expectedByteArray = {(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3a, (byte) 0x0D, (byte) 0x0A};
 byte[] testByteArray = new byte[13];
 int bytesRead = is.read(testByteArray, 0, 13);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 // Test read method with invalid input
 @Test(expected = NullPointerException.class)
 public void testReadWithNullByteArray() {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 is.read(null, 0, 1);
 }

 // Test read method with negative input
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithNegativeInput() {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray = new byte[1];
 is.read(testByteArray, -1, 1);
 }

 // Test read method with too long input
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithTooLongInput() {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray = new byte[15];
 is.read(testByteArray, 0, 15);
 }

 // Test read method with empty input
 @Test
 public void testReadWithEmptyInput() throws IOException {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray = new byte[1];
 int bytesRead = is.read(testByteArray, 0, 1);
 assertEquals(-1, bytesRead);
 }

 // Test read method with only one byte in input
 @Test
 public void testReadWithSingleByteInput() throws IOException {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray = new byte[1];
 int bytesRead = is.read(testByteArray, 0, 1);
 assertArrayEquals(new byte[]{(byte) 0x52}, testByteArray);
 }

 // Test read method with various input
 @Test
 public void testReadWithVariousInput() throws IOException {
 Base64InputStream is = new Base64InputStream(createTestInputStream());
 byte[] testByteArray1 = new byte[1];
 byte[] testByteArray2 = new byte[5];
 byte[] testByteArray3 = new byte[10];
 byte[] testByteArray4 = new byte[15];
 int bytesRead1 = is.read(testByteArray1, 0, 1);
 int bytesRead2 = is.read(testByteArray2, 0, 5);
 int bytesRead3 = is.read(testByteArray3, 0, 10);
 int bytesRead4 = is.read(testByteArray4, 0, 15);
 assertArrayEquals(new byte[]{(byte) 0x42}, testByteArray1);
 assertArrayEquals(new byte[]{(byte) 0x4d, (byte) 0x4f, (byte) 0x43, (byte) 0x41, (byte) 0x4d}, testByteArray2);
 assertArrayEquals(new byte[]{(byte) 0x49, (byte) 0x4e, (byte) 0x44, (byte) 0x45, (byte) 0x4e}, testByteArray3);
 assertEquals(-1, bytesRead4);
 }

 // Helper method to create test input stream
 private InputStream createTestInputStream() {
 return new ByteArrayInputStream(new byte[]{(byte) 0x42, (byte) 0x4d, (byte) 0x4f, (byte) 0x43, (byte) 0x41, (byte) 0x4d, (byte) 0x49, (byte) 0x4e, (byte) 0x44, (byte) 0x45, (byte) 0x4e});
 }
}