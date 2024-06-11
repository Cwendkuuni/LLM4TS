package org.apache.commons.compress.utils;

 import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
 import org.apache.commons.io.IOUtils;
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.nio.charset.StandardCharsets;
 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.util.zip.Adler32;
 import java.util.zip.Checksum;

 /**
  * Test class for {@link ChecksumCalculatingInputStream}
  */
 public class ChecksumCalculatingInputStreamTest {

 private InputStream input;
 private ChecksumCalculatingInputStream checksumInputStream;
 private byte[] testData;
 private long expectedChecksum;

 @Before
 public void setUp() throws IOException, NoSuchAlgorithmException {
 // Create test data
 testData = "Test data".getBytes(StandardCharsets.UTF_8);

 // Create mock checksum
 MockitoAnnotations.initMocks(this);
 MessageDigest mockDigest = mock(MessageDigest.class);
 when(mockDigest.digest(testData)).thenReturn(testData);
 Checksum adler32Checksum = new Adler32();
 adler32Checksum.update(testData);
 expectedChecksum = adler32Checksum.getValue();

 // Create input stream
 input = new ByteArrayInputStream(testData);
 }

 @Test
 public void testReadByte() throws IOException {
 // Given
 checksumInputStream = new ChecksumCalculatingInputStream(expectedChecksum, input);

 // When
 int readByte = checksumInputStream.read();

 // Then
 assertEquals(testData[0], readByte);
 verify(mockDigest, times(1)).update((byte) readByte);
 }

 @Test
 public void testReadByteArray() throws IOException {
 // Given
 byte[] testBuffer = new byte[testData.length];
 checksumInputStream = new ChecksumCalculatingInputStream(expectedChecksum, input);

 // When
 int readBytes = checksumInputStream.read(testBuffer);

 // Then
 assertEquals(testData.length, readBytes);
 assertArrayEquals(testData, testBuffer);
 verify(mockDigest, times(1)).update(testBuffer);
 }

 @Test
 public void testReadByteArrayWithLessBytes() throws IOException {
 // Given
 byte[] testBuffer = new byte[testData.length / 2];
 checksumInputStream = new ChecksumCalculatingInputStream(expectedChecksum, input);

 // When
 int readBytes = checksumInputStream.read(testBuffer);

 // Then
 assertEquals(testData.length / 2, readBytes);
 assertArrayEquals(Arrays.copyOfRange(testData, 0, testData.length / 2), testBuffer);
 verify(mockDigest, times(1)).update(testBuffer);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 // Given
 doThrow(new IOException("Test exception")).when(input).read();
 checksumInputStream = new ChecksumCalculatingInputStream(expectedChecksum, input);

 // When and Then
 checksumInputStream.read();
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(checksumInputStream);
 IOUtils.closeQuietly(input);
 }
 }