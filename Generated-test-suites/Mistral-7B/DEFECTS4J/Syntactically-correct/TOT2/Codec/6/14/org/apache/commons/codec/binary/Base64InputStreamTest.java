import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private InputStream inputStream;
 private Base64InputStream base64InputStream;
 private byte[] inputData = "Hello World!".getBytes();

 @Test
 public void testBase64InputStreamWithDefaultConstructor() throws IOException {
 inputStream = new MockInputStream(inputData);
 base64InputStream = new Base64InputStream(inputStream);
 byte[] outputData = new byte[inputData.length * 4 / 3];
 assertEquals(inputData.length, base64InputStream.read(outputData));
 assertArrayEquals(inputData, Base64.decode(outputData));
 }

 @Test
 public void testBase64InputStreamWithEncodeConstructor() throws IOException {
 inputStream = new MockInputStream(inputData);
 base64InputStream = new Base64InputStream(inputStream, true);
 byte[] outputData = new byte[inputData.length * 4 / 3 + 2];
 assertEquals(inputData.length * 4 / 3 + 2, base64InputStream.read(outputData));
 assertArrayEquals(Base64.encodeBase64(inputData), outputData, 0, inputData.length * 4 / 3);
 }

 @Test
 public void testBase64InputStreamWithCustomLineLengthConstructor() throws IOException {
 inputStream = new MockInputStream(inputData);
 base64InputStream = new Base64InputStream(inputStream, true, 50);
 byte[] outputData = new byte[inputData.length * 4 / 3 + 2];
 assertEquals(inputData.length * 4 / 3 + 2, base64InputStream.read(outputData));
 byte[] decodedData = new byte[inputData.length];
 System.arraycopy(outputData, 0, decodedData, 0, inputData.length);
 String base64LineSeparator = "\r\n"; // CRLF by default
 String encodedData = new String(outputData, inputData.length * 4 / 3, 2, "ASCII");
 String expectedEncodedData = new String(inputData)
 .replaceAll("(.)(??=(.{76})+$)", "$1$2$3" + base64LineSeparator);
 assertEquals(expectedEncodedData, new String(Base64.encodeBase64(inputData), "ASCII"));
 assertArrayEquals(decodedData, Base64.decode(outputData, 0, decodedData.length));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNegativeLineLengthConstructor() {
 inputStream = new MockInputStream(inputData);
 try {
 base64InputStream = new Base64InputStream(inputStream, true, -1);
 } catch (IllegalArgumentException e) {
 fail("Did not throw IllegalArgumentException");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithNullLineSeparatorConstructor() {
 inputStream = new MockInputStream(inputData);
 try {
 base64InputStream = new Base64InputStream(inputStream, true, null);
 } catch (IllegalArgumentException e) {
 fail("Did not throw IllegalArgumentException");
 }
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithNullInputStreamConstructor() throws IOException {
 try {
 base64InputStream = new Base64InputStream(null, false);
 fail("Did not throw IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithInputStreamClosed() throws IOException {
 inputStream = new MockInputStream(inputData);
 base64InputStream = new Base64InputStream(inputStream, false);
 inputStream.close();
 try {
 base64InputStream.read();
 fail("Did not throw IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithEmptyInputStream() throws IOException {
 inputStream = new MockInputStream("");
 base64InputStream = new Base64InputStream(inputStream, false);
 try {
 base64InputStream.read();
 fail("Did not throw IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testBase64InputStreamWithNonBase64Data() throws IOException {
 inputStream = new MockInputStream(new byte[] { 0x01, 0x02, 0x03, 0xFF });
 base64InputStream = new Base64InputStream(inputStream, false);
 byte[] outputData = new byte[3];
 int readLength = base64InputStream.read(outputData);
 assertEquals(3, readLength);
 assertFalse(Base64.isBase64(outputData));
 }

}

class MockInputStream extends FilterInputStream {

 private byte[] data;
 private int position;

 public MockInputStream(byte[] data) {
 super(new ByteArrayInputStream(data));
 this.data = data;
 this.position = 0;
 }

 @Override
 public int read() throws IOException {
 if (position < data.length) {
 return data[position++];
 }
 return -1;
 }

 @Override
 public int read(byte[] b, int off, int len) throws IOException {
 if (position + len > data.length) {
 throw new IOException("End of stream reached");
 }
 System.arraycopy(data, position, b, off, len);
 position += len;
 return len;
 }

}