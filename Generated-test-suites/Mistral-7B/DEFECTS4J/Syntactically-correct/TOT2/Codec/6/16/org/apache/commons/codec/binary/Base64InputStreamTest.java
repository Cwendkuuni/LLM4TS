import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

 @Test
 public void testBase64InputStream() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCB3aGF0IHVwIHlvdSBmaWxlIGRhdGEtdG8=".getBytes()));
 byte[] expectedBytes = new byte[]{'V', 'G', '8', 'g', 'Z', 'X', 'J', 'y', 'I', 'G', 'l', 'z', 'I', 'G', 'h', '1', 'b', 'W', 'F', 'u', 'L', 'C', 'B', '3', 'a', 'G', 'F', '0', 'I', 'H', 'V', 'w', 'I', 'H', 'l', 'v', 'd', 'S', 'B', 'm', 'a', 'W', 'x', 'l', 'I', 'G', 'R', 'h', 'd', 'G', 'E', 't', 'd', 'G', '8', '='.getBytes()};
 byte[] actualBytes = new byte[inputStream.available()];
 inputStream.read(actualBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNullInputStream() {
 new Base64InputStream(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNegativeDoEncodeValue() {
 new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true); // doEncode should be false for InputStream
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNegativeLineLengthValue() {
 new Base64InputStream(new ByteArrayInputStream("test".getBytes()), false, -1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNullLineSeparator() {
 new Base64InputStream(new ByteArrayInputStream("test".getBytes()), false, 76, null);
 }

 @Test
 public void testBase64InputStreamWithEmptyInput() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));
 assertEquals(0, inputStream.available());
 }

 @Test
 public void testBase64InputStreamWithInputLessThanAvailableBytes() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCB3aGF0IHVwIHlvdSBmaWxlIGRhdGEtdG8=".getBytes()));
 byte[] expectedBytes = new byte[]{'V', 'G'};
 byte[] actualBytes = new byte[2];
 int readBytes = inputStream.read(actualBytes);
 assertEquals(2, readBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testBase64InputStreamWithInputGreaterThanAvailableBytes() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCB3aGF0IHVwIHlvdSBmaWxlIGRhdGEtdG8=".getBytes()));
 byte[] expectedBytes = new byte[]{'V', 'G', '8', 'g'};
 byte[] actualBytes = new byte[3];
 int readBytes = inputStream.read(actualBytes);
 assertEquals(3, readBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testBase64InputStreamWithDoEncode() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
 base64OutputStream.write("test".getBytes());
 base64OutputStream.finish();
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true);
 byte[] expectedBytes = new byte[]{'t', 'e', 's', 't'};
 byte[] actualBytes = new byte[4];
 int readBytes = inputStream.read(actualBytes);
 assertEquals(4, readBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testBase64InputStreamWithLineLength() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 48);
 base64OutputStream.write("test".getBytes());
 base64OutputStream.finish();
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), false, 48, null);
 byte[] expectedBytes = new byte[]{'t', 'e', 's', 't'};
 byte[] actualBytes = new byte[4];
 int readBytes = inputStream.read(actualBytes);
 assertEquals(4, readBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testBase64InputStreamWithLineSeparator() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes());
 base64OutputStream.write("test".getBytes());
 base64OutputStream.finish();
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), false, 76, "\r\n".getBytes());
 byte[] expectedBytes = new byte[]{'t', 'e', 's', 't'};
 byte[] actualBytes = new byte[4];
 int readBytes = inputStream.read(actualBytes);
 assertEquals(4, readBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }
}