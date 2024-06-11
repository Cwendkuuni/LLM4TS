import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base64InputStreamTest class.
 */
public class Base64InputStreamTest {

 private final byte[] base64EncodedData = { 61, 62, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 61, 62, 63 };
 private final byte[] base64DecodedData = { 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 65 };

 /**
 * Test constructor with default decoding.
 */
 @Test
 public void testDefaultDecoding() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[base64DecodedData.length];
 int bytesRead = is.read(buffer);
 assertEquals(bytesRead, base64DecodedData.length);
 assertArrayEquals(base64DecodedData, buffer);
 }

 /**
 * Test constructor with custom encoding.
 */
 @Test
 public void testCustomEncoding() throws IOException {
 Base64 base64 = new Base64(true);
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData), true, 72, null);
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) > 0) {
 os.write(buffer, 0, len);
 }
 byte[] decodedData = os.toByteArray();
 assertEquals(base64DecodedData.length, decodedData.length);
 assertArrayEquals(base64DecodedData, decodedData);
 }

 /**
 * Test reading single byte.
 */
 @Test
 public void testReadSingleByte() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[1];
 int readByte = is.read();
 assertNotNull(readByte);
 assertEquals(readByte, base64EncodedData[0]);
 }

 /**
 * Test reading empty input stream.
 */
 @Test(expected = IOException.class)
 public void testReadFromEmptyInputStream() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));
 is.read();
 fail("Should throw exception");
 }

 /**
 * Test reading beyond available data.
 */
 @Test(expected = IOException.class)
 public void testReadBeyondAvailableData() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[base64DecodedData.length + 1];
 int readBytes = is.read(buffer);
 fail("Should throw exception");
 }

 /**
 * Test marking not supported.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testMarkNotSupported() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 is.mark(0);
 fail("Should throw exception");
 }

 /**
 * Test reading null byte array.
 */
 @Test(expected = NullPointerException.class)
 public void testReadNullByteArray() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 is.read(null);
 fail("Should throw exception");
 }

 /**
 * Test reading negative offset.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadNegativeOffset() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[1];
 is.read(buffer, -1, 1);
 fail("Should throw exception");
 }

 /**
 * Test reading negative length.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadNegativeLength() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[1];
 is.read(buffer, 0, -1);
 fail("Should throw exception");
 }

 /**
 * Test reading larger than available data length.
 */
 @Test(expected = IOException.class)
 public void testReadLargerThanAvailableDataLength() throws IOException {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(base64EncodedData));
 byte[] buffer = new byte[base64DecodedData.length + 1];
 int readBytes = is.read(buffer);
 assertEquals(readBytes, base64DecodedData.length);
 }
}