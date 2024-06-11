import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

 @Test
 public void testDefaultBase64InputStream() throws Exception {
 Base64InputStream is = new Base64InputStream(new MockInputStream(TEST_DATA));
 byte[] data = new byte[10];
 int length = is.read(data, 0, data.length);
 assertArrayEquals(TEST_DATA, data, 0, length);
 assertFalse(is.hasNext());
 }

 @Test
 public void testBase64InputStreamWithEncodingEnabled() throws Exception {
 Base64OutputStream os = new Base64OutputStream(new MockOutputStream(), false, 76, new byte[]{0x0D, 0x0A});
 os.write(TEST_DATA);
 Base64InputStream is = new Base64InputStream(new MockInputStream(os.toByteArray()), true);
 byte[] data = new byte[13];
 int length = is.read(data, 0, data.length);
 String expected = "VG8gZXhwYW5kIGlzIGxpa2UgZW1icm91dA==";
 assertArrayEquals(expected.getBytes(), data, 0, length);
 assertFalse(is.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNegativeLength() throws Exception {
 Base64InputStream is = new Base64InputStream(new MockInputStream(TEST_DATA), true, -1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNullLineSeparator() throws Exception {
 Base64InputStream is = new Base64InputStream(new MockInputStream(TEST_DATA), true, 76, null);
 }

 @Test
 public void testBase64InputStreamWithCustomLineEnding() throws Exception {
 Base64InputStream is = new Base64InputStream(new MockInputStream(TEST_DATA), true, 76, new byte[]{0x0D, 0x0D});
 byte[] data = new byte[13];
 int length = is.read(data, 0, data.length);
 String expected = "VG8gZXhwYW5kIGlzIGxpa2UgZW1icm91dA==\r\n";
 assertArrayEquals(expected.getBytes(), data, 0, length);
 assertFalse(is.hasNext());
 }

 @Test
 public void testBase64InputStreamWithNoData() throws Exception {
 Base64InputStream is = new Base64InputStream(new MockInputStream(new byte[0][]));
 assertNull(is.read(new byte[1][], 0, 1));
 }
}

class MockInputStream implements InputStream {
 private byte[] data;
 private int offset;
 private int length;

 public MockInputStream(byte[] data) {
 this.data = data;
 this.length = data.length;
 }

 @Override
 public int read() throws IOException {
 if (offset >= length) {
 return -1;
 }
 return data[offset++];
 }

 @Override
 public int read(byte[] b, int off, int len) throws IOException {
 if (offset + len > length) {
 throw new IOException("Not enough data to read specified length");
 }
 System.arraycopy(data, offset, b, off, len);
 offset += len;
 return len;
 }
}

class MockOutputStream implements OutputStream {
 @Override
 public void write(int b) throws IOException {
 }
}