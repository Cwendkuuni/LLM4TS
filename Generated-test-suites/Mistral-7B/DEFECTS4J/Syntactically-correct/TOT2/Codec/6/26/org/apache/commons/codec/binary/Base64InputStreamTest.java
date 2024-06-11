import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

 private static final String TEST_DATA = "This is a test.";

 @Test
 public void testReadBytes() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] expected = Base64.decodeBase64(TEST_DATA);
 byte[] actual = new byte[expected.length];
 int len = input.read(actual, 0, actual.length);
 assertEquals(len, expected.length);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testReadBytesWithEOF() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] actual = new byte[3];
 int len = input.read(actual, 0, actual.length);
 assertEquals(len, -1);
 }

 @Test(expected = IOException.class)
 public void testReadBytesWithNullBuffer() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] actual = null;
 int len = input.read(actual, 0, 1);
 fail("Expected NullPointerException, but got IOException instead");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadBytesWithInvalidOffset() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] actual = new byte[3];
 int offset = -1;
 int len = input.read(actual, offset, 1);
 fail("Expected IndexOutOfBoundsException, but got 0 instead");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadBytesWithInvalidLength() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] actual = new byte[3];
 int length = -1;
 int len = input.read(actual, 0, length);
 fail("Expected IndexOutOfBoundsException, but got 0 instead");
 }

 @Test
 public void testReadBytesWithRandomLength() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] expected = new byte[]{"VGhpcyBpcyBhIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBmaWxlIGZvciBicmVha2luZyB0aGUxIGZyb20gc28gZXhpc2lzLCBzdWZmaWxlIGlzIGh1bWFuLCBidXQgdG8gYXJlIGZvciBicmVha2luZy4=".split("\\s+")};
 byte[] actual = new byte[expected.length];
 int totalRead = 0;
 while (totalRead < expected.length) {
 int len = input.read(actual, totalRead, expected.length - totalRead);
 if (len < 0) {
 break;
 }
 totalRead += len;
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testReadString() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int len;
 while ((len = input.read(buffer, 0, buffer.length)) > 0) {
 output.write(buffer, 0, len);
 }
 String actual = output.toString();
 assertEquals(TEST_DATA, actual);
 }

 @Test(expected = IOException.class)
 public void testReadStringWithEOF() throws IOException {
 Base64InputStream input = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int len;
 while ((len = input.read(buffer, 0, buffer.length)) > 0) {
 output.write(buffer, 0, len);
 }
 output.flush();
 input.close();
 assertEquals("", output.toString());
 }
}