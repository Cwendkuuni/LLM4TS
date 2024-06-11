import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for DeflateCompressorInputStream
 */
public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream deflateInputStream;
 private byte[] originalData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0xe5, (byte) 0xf1, (byte) 0xff};

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(originalData);
 deflateInputStream = new DeflateCompressorInputStream(inputStream);
 }

 @After
 public void tearDown() throws IOException {
 if (deflateInputStream != null) {
 deflateInputStream.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testReadByte() throws IOException {
 byte[] readData = new byte[originalData.length];
 int readByte;
 for (int i = 0; i < originalData.length; i++) {
 readByte = deflateInputStream.read();
 readData[i] = (byte) readByte;
 Assert.assertEquals("Byte read from stream doesn't match expected data", originalData[i], readByte);
 }
 Assert.assertFalse("End of stream reached before reading all data", deflateInputStream.read() != -1);
 }

 @Test
 public void testReadByteArray() throws IOException {
 byte[] buffer = new byte[originalData.length];
 int bytesRead = deflateInputStream.read(buffer);
 Assert.assertEquals("Number of bytes read doesn't match expected data", originalData.length, bytesRead);
 Assert.assertArrayEquals("Bytes read from stream don't match expected data", originalData, buffer);
 }

 @Test
 public void testSkip() throws IOException {
 long bytesToSkip = 3;
 long bytesSkipped = deflateInputStream.skip(bytesToSkip);
 Assert.assertEquals("Number of bytes skipped doesn't match expected data", bytesToSkip, bytesSkipped);
 }

 @Test
 public void testAvailable() throws IOException {
 int bytesAvailable = deflateInputStream.available();
 Assert.assertEquals("Number of bytes available to read doesn't match expected data", originalData.length - deflateInputStream.getByteCount(), bytesAvailable);
 }

 @Test
 public void testClose() throws IOException {
 deflateInputStream.close();
 }

 @Test(expected = IOException.class)
 public void testNegativeLengthRead() throws IOException {
 deflateInputStream.read(-1);
 }

 @Test(expected = IOException.class)
 public void testNegativeLengthSkip() throws IOException {
 deflateInputStream.skip(-1);
 }

 @Test
 public void testCreateWithZlibHeader() throws IOException {
 byte[] zlibData = new byte[]{(byte) 0x78, (byte) 0x01, (byte) 0x00, (byte) 0xf9, (byte) 0x37, 0x61, 0x78, 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0xe5, (byte) 0xf1, (byte) 0xff, 0x00, 0x00};
 InputStream zlibInputStream = new ByteArrayInputStream(zlibData);
 DeflateCompressorInputStream zlibDeflateInputStream = new DeflateCompressorInputStream(zlibInputStream, new DeflateParameters());
 byte[] readData = new byte[originalData.length];
 int readByte;
 for (int i = 0; i < originalData.length; i++) {
 readByte = zlibDeflateInputStream.read();
 readData[i] = (byte) readByte;
 }
 Assert.assertFalse("End of stream reached before reading all data", zlibDeflateInputStream.read() != -1);
 Assert.assertArrayEquals("Bytes read from stream don't match expected data", originalData, readData);
 }

 @Test(expected = IOException.class)
 public void testCreateWithoutZlibHeaderWithZlibHeaderInput() throws IOException {
 InputStream zlibInputStream = new ByteArrayInputStream(new byte[]{0x78, 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0xe5, (byte) 0xf1, (byte) 0xff});
 DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(zlibInputStream, new DeflateParameters(true));
 deflateInputStream.read();
 }
}