import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

 private Base64InputStream inputStream;
 private ByteArrayOutputStream outputStream;
 private byte[] inputData;
 private byte[] expectedOutput;

 @Before
 public void setUp() {
 inputData = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testBase64InputStreamDecoding() throws IOException {
 // create Base64InputStream for decoding
 inputStream = new Base64InputStream(new ByteArrayInputStream(inputData), false);
 // read data from input stream and write to output stream
 int readBytes;
 byte[] buffer = new byte[inputData.length];
 while ((readBytes = inputStream.read(buffer, 0, inputData.length)) != -1) {
 outputStream.write(buffer, 0, readBytes);
 }
 outputStream.flush();
 // check if output stream has the same data as input data
 byte[] actualOutput = outputStream.toByteArray();
 Assert.assertArrayEquals(inputData, actualOutput);
 }

 @Test
 public void testBase64InputStreamEncoding() throws IOException {
 // create Base64InputStream for encoding
 inputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true);
 // read data from input stream and write to output stream
 byte[] buffer = new byte[inputData.length + 4]; // 4 bytes for line separators
 int readBytes;
 while ((readBytes = inputStream.read(buffer, 0, buffer.length)) != -1) {
 outputStream.write(buffer, 0, readBytes);
 }
 outputStream.flush();
 // check if output stream has the same data as encoded input data
 expectedOutput = new byte[]{(byte) 0x40, (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x46, (byte) 0x46};
 byte[] actualOutput = outputStream.toByteArray();
 Assert.assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamMarkSupported() throws IOException {
 // markSupported should return false
 inputStream = new Base64InputStream(new ByteArrayInputStream(inputData), false);
 inputStream.mark(1);
 }
}