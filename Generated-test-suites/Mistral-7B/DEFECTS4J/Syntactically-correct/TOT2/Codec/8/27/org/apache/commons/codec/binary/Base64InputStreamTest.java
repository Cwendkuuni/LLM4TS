import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private InputStream input;
 private Base64InputStream base64InputStream;

 @Test
 public void testBase64InputStreamConstruction() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 base64InputStream = new Base64InputStream(input);
 assertNotNull(base64InputStream);
 }

 @Test
 public void testReadMethod() throws IOException {
 byte[] expected = {-61, -73, 62, -88, 65, 76, -61, 76, -61, 68, 61, 76, 61, -73, 65, 73, 73};
 byte[] buffer = new byte[17];
 int bytesRead = base64InputStream.read(buffer);
 assertArrayEquals(expected, buffer, 0, bytesRead);
 }

 @Test
 public void testReadMethodWithEmptyInput() throws IOException {
 byte[] buffer = new byte[1];
 int bytesRead = base64InputStream.read(buffer);
 assertEquals(-1, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testReadMethodWithNullBuffer() throws IOException {
 base64InputStream.read(null);
 }

 @Test
 public void testReadMethodWithInvalidOffset() {
 byte[] buffer = new byte[5];
 try {
 base64InputStream.read(buffer, 10, 1);
 fail("Expected an IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testReadMethodWithNegativeLength() {
 byte[] buffer = new byte[5];
 try {
 base64InputStream.read(buffer, 0, -1);
 fail("Expected an IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadMethodWithInvalidBufferSize() {
 byte[] buffer = new byte[5];
 try {
 base64InputStream.read(buffer, 0, buffer.length + 1);
 fail("Expected an IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testReadAllBytesMethod() throws IOException {
 byte[] expected = {-61, -73, 62, -88, 65, 76, -61, 76, -61, 68, 61, 76, 61, -73, 65, 73, 73};
 byte[] actual = base64InputStream.readAllBytes();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMarkSupportedMethod() {
 assertFalse(base64InputStream.markSupported());
 }

 @Test(expected = IOException.class)
 public void testCloseMethod() throws IOException {
 base64InputStream.close();
 input.close();
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 base64InputStream = new Base64InputStream(input);
 base64InputStream.close();
 }

 @Test
 public void testResetMethod() throws IOException {
 base64InputStream.mark(1);
 base64InputStream.reset();
 byte[] buffer = new byte[1];
 int bytesRead = base64InputStream.read(buffer);
 assertEquals(-1, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testReadAfterReset() throws IOException {
 base64InputStream.mark(1);
 base64InputStream.reset();
 byte[] buffer = new byte[1];
 int bytesRead = base64InputStream.read(buffer);
 fail("Expected an IOException");
 }

 @Test
 public void testEncodeMode() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 base64InputStream = new Base64InputStream(input, true);
 byte[] expected = {-62, -65, 63, 68, 61, 76, 65, 76, -65, 76, -65, 76, -65, 76, -65, 76, -65, 76};
 byte[] buffer = new byte[20];
 int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
 assertArrayEquals(expected, buffer, 0, bytesRead);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeModeWithInvalidInput() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 base64InputStream = new Base64InputStream(input, true, 2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeModeWithInvalidLineLength() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 base64InputStream = new Base64InputStream(input, true, -1, null);
 }

 @Test
 public void testDecodeMode() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 Base64 base64 = new Base64(false);
 byte[] expected = {-61, -73, 62, -88, 65, 76, -61, 76, -61, 68, 61, 76, 61, -73, 65, 73, 73};
 base64InputStream = new Base64InputStream(input, false);
 byte[] buffer = new byte[17];
 int bytesRead = base64InputStream.read(buffer);
 assertArrayEquals(expected, buffer, 0, bytesRead);
 }

 @Test
 public void testDecodeModeWithInvalidInput() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 Base64 base64 = new Base64(false);
 base64InputStream = new Base64InputStream(input, false, 2, null);
 }

 @Test
 public void testDecodeModeWithInvalidLineLength() {
 input = new java.io.ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
 Base64 base64 = new Base64(2, new byte[] {-1, -1});
 base64InputStream = new Base64InputStream(input, false, 2, null);
 }
}