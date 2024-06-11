import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

 private DeflateCompressorInputStream testStream;
 private InputStream testInputStream;

 @Before
 public void setUp() throws IOException {
 testInputStream = new MockInputStream();
 testStream = new DeflateCompressorInputStream(testInputStream);
 }

 @Test
 public void testConstructorValidInputStream() throws IOException {
 DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(testInputStream);
 Assert.assertNotNull(deflatStream);
 }

 @Test
 public void testConstructorValidInputStreamAndParameters() throws IOException {
 DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(testInputStream, new DeflateParameters());
 Assert.assertNotNull(deflatStream);
 }

 @Test
 public void testReadValidByteReturn() throws IOException {
 byte[] readByte = new byte[1];
 int read = testStream.read(readByte, 0, 1);
 Assert.assertEquals(1, read);
 Assert.assertEquals(1, readByte[0]);
 }

 @Test
 public void testReadNegativeByteReturn() throws IOException {
 int read = testStream.read();
 Assert.assertEquals(-1, read);
 }

 @Test
 public void testReadValidByteArrayValidOffsetValidLength() throws IOException {
 byte[] readByte = new byte[3];
 int read = testStream.read(readByte, 0, 3);
 Assert.assertEquals(3, read);
 }

 @Test
 public void testReadEmptyBuffer() throws IOException {
 byte[] readByte = new byte[0];
 int read = testStream.read(readByte, 0, 0);
 Assert.assertEquals(-1, read);
 }

 @Test
 public void testReadNullByteArray() throws IOException {
 int read = testStream.read(null, 0, 1);
 Assert.assertThrows(NullPointerException.class, () -> testStream.read(null, 0, 1));
 }

 @Test
 public void testReadNegativeLength() throws IOException {
 int read = testStream.read(new byte[1], 0, -1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> testStream.read(new byte[1], 0, -1));
 }

 @Test
 public void testReadClosedStream() throws IOException {
 testStream.close();
 int read = testStream.read();
 Assert.assertEquals(-1, read);
 }

 @Test
 public void testSkipValidLongNumber() throws IOException {
 long skip = 3L;
 long skipped = testStream.skip(skip);
 Assert.assertEquals(skip, skipped);
 }

 @Test
 public void testSkipNegativeLongNumber() throws IOException {
 long skip = -3L;
 long skipped = testStream.skip(skip);
 Assert.assertEquals(0, skipped);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> testStream.skip(skip));
 }

 @Test
 public void testAvailableValidPositiveInteger() throws IOException {
 int available = 3;
 int availableBytes = testStream.available();
 Assert.assertEquals(available, availableBytes);
 }

 @Test
 public void testClose() throws IOException {
 testStream.close();
 }

 @Test
 public void testCloseWithIOException() throws IOException {
 testStream.close();
 TestUtils.expectIOException(testStream::read, testStream);
 }

 @Test
 public void testCloseAfterReading() throws IOException {
 testStream.read();
 testStream.close();
 }
}