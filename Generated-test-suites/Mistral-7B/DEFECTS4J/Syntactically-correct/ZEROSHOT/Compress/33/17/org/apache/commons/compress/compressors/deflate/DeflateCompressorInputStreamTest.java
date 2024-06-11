import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream deflateCompressorInputStream;
 private byte[] testData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x2e, (byte) 0x03, (byte) 0x40, (byte) 0x9b, (byte) 0xeb, (byte) 0x3b, (byte) 0x3c, (byte) 0x43, (byte) 0x61, (byte) 0x62, (byte) 0x63};

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(testData);
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
 }

 @Test
 public void testRead() throws IOException {
 byte[] byteArray = new byte[testData.length];
 int read = deflateCompressorInputStream.read(byteArray, 0, byteArray.length);
 assertEquals(testData.length, read);
 assertArrayEquals(testData, byteArray);
 }

 @Test
 public void testReadSingleByte() throws IOException {
 byte[] byteArray = new byte[1];
 int read = deflateCompressorInputStream.read();
 assertEquals(testData[0], read);
 }

 @Test
 public void testSkip() throws IOException {
 long skip = deflateCompressorInputStream.skip(testData.length - 1);
 assertEquals(testData.length - 1, skip);
 }

 @Test
 public void testAvailable() throws IOException {
 byte[] byteArray = new byte[testData.length];
 int read = deflateCompressorInputStream.read(byteArray, 0, byteArray.length);
 long available = deflateCompressorInputStream.available();
 assertEquals(testData.length - read, available);
 }

 @Test
 public void testClose() throws IOException {
 deflateCompressorInputStream.close();
 assertEquals(0, inputStream.available());
 }

 @Test
 public void testDeflateCompressorInputStreamWithZlibHeader() throws IOException {
 byte[] testDataWithZlibHeader = new byte[]{(byte) 0x78, 0x01, 0x00, 0x00, 0x78, 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x2e, (byte) 0x03, (byte) 0x40, (byte) 0x9b, (byte) 0xeb, (byte) 0x3b, (byte) 0x3c, (byte) 0x43, (byte) 0x61, (byte) 0x62, (byte) 0x63};
 InputStream inputStreamWithZlibHeader = new ByteArrayInputStream(testDataWithZlibHeader);
 DeflateCompressorInputStream deflateCompressorInputStreamWithZlibHeader = new DeflateCompressorInputStream(inputStreamWithZlibHeader, new DeflateParameters());
 byte[] byteArrayWithZlibHeader = new byte[testDataWithZlibHeader.length];
 int readWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.read(byteArrayWithZlibHeader, 0, byteArrayWithZlibHeader.length);
 assertEquals(testData.length, readWithZlibHeader);
 assertArrayEquals(testData, byteArrayWithZlibHeader, 4); // skip 4 first bytes, the zlib header
 }
}