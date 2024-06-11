package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

 private byte[] testData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
 private InputStream testInput = new ByteArrayInputStream(testData);
 private Checksum expectedChecksum = Checksums.create("MD5");

 @Test
 public void testMD5ChecksumCalculation() throws Exception {
 ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(expectedChecksum, testInput);
 int read = 0;
 byte[] readBuffer = new byte[testData.length];

 do {
 read = checksumStream.read(readBuffer, 0, readBuffer.length);
 } while (read != -1);

 long actualChecksum = checksumStream.getValue();

 assertEquals("MD5 Checksum doesn't match", expectedChecksum.getValue(), actualChecksum);
 }

 @Test(expected = IOException.class)
 public void testReadThrowsIOException() throws Exception {
 ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(expectedChecksum, testInput);
 testInput = new ByteArrayInputStream(new byte[0]);

 checksumStream.read();
 }

 @Test(expected = IOException.class)
 public void testCloseThrowsIOException() throws Exception {
 ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(expectedChecksum, testInput);

 checksumStream.close();

 testInput.close();
 }

}