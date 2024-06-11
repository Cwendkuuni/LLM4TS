import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for Base64InputStream class
 */
public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05};

 @Test
 public void testBase64InputStream_defaultConstructor() {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 byte[] result = new byte[TEST_DATA.length];
 int read = bis.read(result, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decodeConstructor() {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] result = new byte[TEST_DATA.length];
 int read = bis.read(result, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_encodeConstructor() {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 baos.write(bis.readAllBytes(), 0, bis.available());
 String encodedData = baos.toString();
 assertEquals("VG8gZXhhbXBsZSBuZWVkIGlzIGlzIHVwIHlvdSBmaWxlIGlzIHVwIHlvdSBzdHlsZXM=", encodedData);
 }

 @Test(expected = NullPointerException.class)
 public void testBase64InputStream_nullInputStream() {
 new Base64InputStream(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_negativeLineLength() {
 new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, -1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_nullLineSeparator() {
 new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, 0, null);
 }

 @Test
 public void testBase64InputStream_decode_EOF() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] result = new byte[TEST_DATA.length];
 int read = bis.read(result, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decode_emptyData() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
 byte[] result = new byte[0];
 int read = bis.read(result, 0, result.length);
 assertArrayEquals(new byte[0], result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decode_partialData() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x01, 0x02 }), false);
 byte[] result = new byte[2];
 int read = bis.read(result, 0, 2);
 assertArrayEquals(new byte[] { 0x01, 0x02 }, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_encode_EOF() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 while (read != -1) {
 read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 }
 String decodedData = new String(baos.toByteArray());
 String encodedData = new String(Base64.decodeBase64(decodedData.getBytes()));
 assertEquals(new String(TEST_DATA), encodedData);
 }

 @Test
 public void testBase64InputStream_encode_emptyData() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 assertEquals("", baos.toString());
 }

 @Test
 public void testBase64InputStream_encode_partialData() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x01 }), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 assertEquals("VG==", baos.toString());
 }

 @Test
 public void testBase64InputStream_decode_invalidData() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG==".getBytes()), false);
 byte[] result = new byte[1];
 int read = bis.read(result, 0, 1);
 assertNull(result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decode_invalidDataWithPadding() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG==".getBytes()), false);
 byte[] result = new byte[1];
 int read = bis.read(result, 0, 1);
 assertNull(result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decode_invalidDataWithoutPadding() throws IOException {
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG".getBytes()), false);
 byte[] result = new byte[1];
 int read = bis.read(result, 0, 1);
 assertNull(result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_encode_invalidData() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG==".getBytes()), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 assertEquals("VG==", baos.toString());
 }

 @Test
 public void testBase64InputStream_encode_invalidDataWithPadding() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG==".getBytes()), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 assertEquals("VG==", baos.toString());
 }

 @Test
 public void testBase64InputStream_encode_invalidDataWithoutPadding() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream("VG".getBytes()), true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 assertEquals("VG", baos.toString());
 }

 @Test
 public void testBase64InputStream_decode_lineLength() throws IOException {
 Base64 base64 = new Base64(50);
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false, 50, null);
 byte[] result = new byte[TEST_DATA.length];
 int read = bis.read(result, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_decode_lineSeparator() throws IOException {
 Base64 base64 = new Base64(0, new byte[] { 0x13, 0x10 });
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false, 0, new byte[] { 0x13, 0x10 });
 byte[] result = new byte[TEST_DATA.length];
 int read = bis.read(result, 0, TEST_DATA.length);
 assertArrayEquals(TEST_DATA, result);
 assertEquals(-1, bis.read());
 }

 @Test
 public void testBase64InputStream_encode_lineLength() throws IOException {
 Base64 base64 = new Base64(50);
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, 50, null);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 String decodedData = new String(baos.toByteArray());
 String encodedData = new String(Base64.decodeBase64(decodedData.getBytes()));
 assertEquals(new String(TEST_DATA), encodedData);
 }

 @Test
 public void testBase64InputStream_encode_lineSeparator() throws IOException {
 Base64 base64 = new Base64(0, new byte[] { 0x13, 0x10 });
 Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, 0, new byte[] { 0x13, 0x10 });
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 int read = bis.read(bis.singleByte, 0, 1);
 baos.write(bis.singleByte);
 String decodedData = new String(baos.toByteArray());
 String encodedData = new String(Base64.decodeBase64(decodedData.getBytes()));
 assertEquals(new String(TEST_DATA), encodedData);
 }
}