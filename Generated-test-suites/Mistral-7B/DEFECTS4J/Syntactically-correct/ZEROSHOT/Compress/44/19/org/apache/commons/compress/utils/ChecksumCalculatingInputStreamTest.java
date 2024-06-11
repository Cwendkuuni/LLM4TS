import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Test Class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

 private ChecksumCalculatingInputStream checksumCalculatingInputStream;
 private ByteArrayOutputStream byteArrayOutputStream;
 private ByteArrayInputStream byteArrayInputStream;
 private Checksum checksum;
 private byte[] expectedData;
 private long expectedChecksum;

 @Before
 public void setUp() {
 checksum = MessageDigest.getInstance("MD5");
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 expectedData = new byte[]{1, 2, 3};
 expectedChecksum = 1L; // MD5 checksum for expectedData
 checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, byteArrayOutputStream);
 }

 @After
 public void tearDown() throws IOException {
 checksumCalculatingInputStream.close();
 byteArrayOutputStream.reset();
 }

 @Test
 public void testReadSingleByte() throws IOException {
 final int expectedByte = expectedData[0];
 final int actualByte = checksumCalculatingInputStream.read();
 Assert.assertEquals(expectedByte, actualByte);
 }

 @Test(expected = IOException.class)
 public void testReadSingleByte_ExhaustedStream() throws IOException {
 final int expectedByte = expectedData[0];
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue()); // Checksum should match expected value
 Assert.assertThrows(IOException.class, () -> checksumCalculatingInputStream.read());
 }

 @Test
 public void testReadByteArray() throws IOException {
 final int expectedLength = expectedData.length;
 final int actualLength = checksumCalculatingInputStream.read(expectedData, 0, expectedLength);
 Assert.assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IOException.class)
 public void testReadByteArray_ExhaustedStream() throws IOException {
 byte[] actualData = new byte[expectedData.length];
 checksumCalculatingInputStream.read(actualData, 0, expectedData.length);
 checksumCalculatingInputStream.read(actualData, 0, expectedData.length);
 checksumCalculatingInputStream.read(actualData, 0, expectedData.length);
 Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue()); // Checksum should match expected value
 Assert.assertThrows(IOException.class, () -> checksumCalculatingInputStream.read(expectedData, 0, expectedData.length));
 }

 @Test
 public void testSkip() throws IOException {
 final long expectedSkipped = 2;
 final long actualSkipped = checksumCalculatingInputStream.skip(expectedSkipped);
 Assert.assertEquals(expectedSkipped, actualSkipped);
 }

 @Test(expected = IOException.class)
 public void testSkip_ExhaustedStream() throws IOException {
 checksumCalculatingInputStream.skip(1);
 Assert.assertThrows(IOException.class, () -> checksumCalculatingInputStream.skip(Long.MAX_VALUE));
 }

 @Test
 public void testGetValue() {
 final long actualChecksum = checksumCalculatingInputStream.getValue();
 Assert.assertEquals(expectedChecksum, actualChecksum);
 }
}