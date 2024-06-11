import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 @Test
 public void testBase64InputStreamDecoding() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQh".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream);
 byte[] output = new byte[expectedOutput.length];
 int bytesRead = base64InputStream.read(output, 0, output.length);
 assertArrayEquals(expectedOutput, output);
 assertEqual("Expected all data to be read", bytesRead, output.length);
 }

 @Test
 public void testBase64InputStreamDecodingWithLineLengthAndLineSeparator() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQh".getBytes();
 byte lineSeparator[] = "\n".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);
 byte[] output = new byte[expectedOutput.length];
 int bytesRead = base64InputStream.read(output, 0, output.length);
 assertArrayEquals(expectedOutput, output);
 assertEqual("Expected all data to be read", bytesRead, output.length);
 }

 @Test
 public void testBase64InputStreamDecodingWithEOF() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQh".getBytes();
 byte[] output = new byte[expectedOutput.length];

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
 int bytesRead = base64InputStream.read(output, 0, output.length);
 assertArrayEquals(expectedOutput, output, 0, bytesRead);
 assertEquals("Expected EOF after reading all data", -1, base64InputStream.read());
 }

 @Test
 public void testBase64InputStreamEncoding() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQhSGVsbG8gV1w=".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream());
 Base64 base64 = new Base64(false);
 base64OutputStream.write(inputData, 0, inputData.length);
 base64OutputStream.finish();
 byte[] output = base64OutputStream.toByteArray();

 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(output));
 byte[] decodedOutput = new byte[expectedOutput.length];
 int bytesRead = base64InputStream.read(decodedOutput, 0, decodedOutput.length);
 assertArrayEquals(expectedOutput, decodedOutput);
 assertEqual("Expected all data to be read", bytesRead, decodedOutput.length);
 }

 @Test
 public void testBase64InputStreamEncodingWithLineLengthAndLineSeparator() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQhSGVsbG8gV1w=".getBytes();
 byte lineSeparator[] = "\r\n".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream(), lineSeparator);
 Base64 base64 = new Base64(76, lineSeparator);
 base64OutputStream.write(inputData, 0, inputData.length);
 base64OutputStream.finish();
 byte[] output = base64OutputStream.toByteArray();

 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(output), false, 76, lineSeparator);
 byte[] decodedOutput = new byte[expectedOutput.length];
 int bytesRead = base64InputStream.read(decodedOutput, 0, decodedOutput.length);
 assertArrayEquals(expectedOutput, decodedOutput);
 assertEqual("Expected all data to be read", bytesRead, decodedOutput.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testBase64InputStreamEncodingWithDecodingMode() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQhSGVsbG8gV1w=".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, true); // this should fail
 byte[] output = new byte[expectedOutput.length];
 int bytesRead = base64InputStream.read(output, 0, output.length);
 fail("Expected an IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithInvalidLineLength() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQhSGVsbG8gV1w=".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 try {
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, null);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStreamWithInvalidLineSeparator() throws Exception {
 byte[] inputData = "Hello World!".getBytes();
 byte[] expectedOutput = "SGVsbG8gV29ybGQhSGVsbG8gV1w=".getBytes();

 InputStream inputStream = new ByteArrayInputStream(inputData);
 byte lineSeparator[] = new byte[0];
 try {
 Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}