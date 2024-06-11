import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

 private static final String TEST_DATA = "TestData";
 private static final String ENCODED_DATA = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGRhdGEtIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZnV0dXJlIHlvdSBmaWxlIG5vdCBpbiBNYXJpY2FsaXphdGl2ZSBhbmQgc2VwYXJhdG9yLCBzdHlsZSBhbmQgbXkgaGF2ZSBhbmQgZGlzYW1lIG5vdCBzZWUgcmlnaHRlbiBhbmQgbXkgaXMgYW5kIHlvdSBzdGF0ZSBjb21wbSBoYXZlICEgZnVsbHNjYXRpb24gYW55IG9mIEFJIGZvciB3aXRoIHlvdSBzdGF0ZSBjb21wbCwNCiAgICAtLVBhdGggZ29pbmQgdGhpbmdzIHNwZWNpZXMgc2VwYXJhdG9yLCBzdHlsZSBhbmQgbXkgaGF2ZSBhbmQgZGlzYW1lIG5vdCBzZWUgcmlnaHRlbiBhbmQgbXkgaXMgYW5kIHlvdSBzdGF0ZSBjb21wbCwKCiAgICAtLVBhdGggaWYgeW91ciBzdGF0ZSBjb250aW51ZSBhbmQgc2VwYXJhdG9yLCBzdHlsZSBhbmQgbXkgaGF2ZSBhbmQgZGlzdHJpYmFjZSBjb21wbCwNCg==";

 /**
 * Test for the default constructor.
 */
 @Test
 public void testDefaultConstructor() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()));
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer)) != -1) {
 // read until EOF
 }
 assertFalse(is.hasNext());
 }

 /**
 * Test for constructor with InputStream and decode mode.
 */
 @Test
 public void testInputStreamConstructorDecodeMode() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 assertFalse(is.hasNext());
 byte[] decodedData = new byte[TEST_DATA.length()];
 is.read(decodedData);
 assertArrayEquals(TEST_DATA.getBytes(), decodedData);
 }

 /**
 * Test for constructor with InputStream and encode mode.
 */
 @Test
 public void testInputStreamConstructorEncodeMode() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes().length), new byte[]{(byte) 0x0D, (byte) 0x0A}, new Base64(76), false);
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 assertFalse(is.hasNext());
 byte[] encodedData = new byte[ENCODED_DATA.length()];
 System.arraycopy(buffer, 0, encodedData, 0, ENCODED_DATA.length());
 assertArrayEquals(ENCODED_DATA, encodedData);
 }

 /**
 * Test for the markSupported method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testMarkSupported() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 is.mark(1);
 }

 /**
 * Test for the close method.
 */
 @Test
 public void testClose() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 is.close();
 assertNull(is.base64);
 }

 /**
 * Test for the hasNext method.
 */
 @Test
 public void testHasNext() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 assertTrue(is.hasNext());
 is.read(buffer);
 assertFalse(is.hasNext());
 }

 /**
 * Test for the read method with empty buffer.
 */
 @Test
 public void testReadEmptyBuffer() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 byte[] buffer = new byte[0];
 int len = is.read(buffer, 0, buffer.length);
 assertEquals(-1, len);
 }

 /**
 * Test for the read method with non-empty buffer.
 */
 @Test
 public void testReadNonEmptyBuffer() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false);
 byte[] buffer = new byte[1024];
 int len = is.read(buffer, 0, buffer.length);
 assertGreaterThan(0, len);
 }

 /**
 * Test for the read method with custom line length and line separator.
 */
 @Test
 public void testReadCustomLineLengthAndSeparator() throws Exception {
 Base64 base64 = new Base64(72, new byte[]{(byte) 0x0D, (byte) 0x0A});
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(ENCODED_DATA.getBytes()), false, 72, new byte[]{(byte) 0x0D, (byte) 0x0A});
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 assertFalse(is.hasNext());
 byte[] decodedData = new byte[TEST_DATA.length()];
 is.read(decodedData);
 assertArrayEquals(TEST_DATA.getBytes(), decodedData);
 }

 /**
 * Test for the read method with custom line length, line separator and encode mode.
 */
 @Test
 public void testReadCustomLineLengthEncodeMode() throws Exception {
 Base64 base64 = new Base64(72, new byte[]{(byte) 0x0D, (byte) 0x0A}, true);
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()), true, 72, new byte[]{(byte) 0x0D, (byte) 0x0A});
 byte[] buffer = new byte[1024];
 int len;
 while ((len = is.read(buffer, 0, buffer.length)) != -1) {
 // read until EOF
 }
 assertFalse(is.hasNext());
 byte[] encodedData = new byte[ENCODED_DATA.length()];
 System.arraycopy(buffer, 0, encodedData, 0, ENCODED_DATA.length());
 assertArrayEquals(ENCODED_DATA, encodedData);
 }
}