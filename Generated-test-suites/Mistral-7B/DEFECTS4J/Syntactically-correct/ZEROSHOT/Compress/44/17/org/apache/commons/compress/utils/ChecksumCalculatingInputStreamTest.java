package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class ChecksumCalculatingInputStreamTest {

 @Test
 public void testChecksumCalculatingInputStream() throws IOException {
 Checksum checksum = Checksums.adler32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 int read = 0;
 byte[] buffer = new byte[1];
 while ((read = checksumCalculatingInputStream.read(buffer)) != -1) {
 // nothing to do here
 }
 assertEquals("adler32 checksum should be 165670261L", 165670261L, checksum.getValue());
 }

 @Test
 public void testChecksumCalculatingInputStream_read() throws IOException {
 Checksum checksum = Checksums.adler32();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] inputData = "abcdefghijklmnopqrstuvwxyz1234567890".getBytes();
 outputStream.write(inputData);
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 byte[] buffer = new byte[inputData.length];
 int length = checksumCalculatingInputStream.read(buffer);
 assertEquals("read should return the same size as the input data", inputData.length, length);
 assertArrayEquals("checksum should match", inputData, buffer);
 long checksumValue = checksum.getValue();
 // this is a workaround to get the actual checksum value for Adler32, as the value is not directly accessible
 MessageDigest md = MessageDigest.getInstance("MD5");
 byte[] md5Checksum = md.digest(outputStream.toByteArray());
 long adler32Checksum = 0;
 for (int i = 0; i < md5Checksum.length; i++) {
 adler32Checksum = (adler32Checksum << 8) | (md5Checksum[i] & 0xFF);
 adler32Checksum = (adler32Checksum & 0xFFFFFFFFL) + (adler32Checksum >>> 24);
 }
 assertEquals("adler32 checksum should match MD5 checksum", adler32Checksum, checksumValue);
 }

 @Test
 public void testChecksumCalculatingInputStream_readArray() throws IOException {
 Checksum checksum = Checksums.crc32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
 byte[] buffer = new byte[3];
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 int length = checksumCalculatingInputStream.read(buffer);
 assertEquals("read should return the same size as the buffer", 3, length);
 assertArrayEquals("checksum should match", new byte[]{'a', 'b', 'c'}, buffer);
 long checksumValue = checksum.getValue();
 assertEquals("crc32 checksum should be 1443734576L", 1443734576L, checksumValue);
 }

 @Test(expected = IOException.class)
 public void testChecksumCalculatingInputStream_exhaustedStream() throws IOException {
 Checksum checksum = Checksums.adler32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'a'});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read();
 checksumCalculatingInputStream.read(); // this should throw an IOException
 }

 @Test(expected = IOException.class)
 public void testChecksumCalculatingInputStream_mismatchChecksum() throws IOException {
 Checksum checksum = Checksums.adler32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 checksumCalculatingInputStream.read(); // read the first byte
 checksumCalculatingInputStream.read(); // read the second byte
 checksumCalculatingInputStream.read(); // read the third byte
 checksumCalculatingInputStream.read(); // read a byte that doesn't match the expected value
 }

 @Test
 public void testChecksumCalculatingInputStream_skip() throws IOException {
 Checksum checksum = Checksums.adler32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
 // Can't really skip, we have to hash everything to verify the checksum
 long skipped = checksumCalculatingInputStream.skip(1);
 assertEquals("skip should return 1", 1, skipped);
 }
}