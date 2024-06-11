package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link BaseNCodecInputStream} class.
 */
public class BaseNCodecInputStreamTest {

 private static final byte[] TEST_DATA = {0x2A, 0x2B, 0x2C, 0x2D};

 private BaseNCodec baseNCodec;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Test
 public void testBaseNCodecInputStream() throws IOException {
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), baseNCodec, false);

 assertNotNull(inputStream);
 assertFalse(inputStream.markSupported());

 int read = inputStream.read();
 assertEquals(TEST_DATA[0], read);

 int readLength = inputStream.read(new byte[4], 0, 4);
 assertArrayEquals(new byte[] {TEST_DATA[0], TEST_DATA[1], TEST_DATA[2], TEST_DATA[3]}, new byte[4]);

 inputStream.close();
 }

 @Test
 public void testBaseNCodecInputStreamEncode() throws IOException {
 baseNCodec = new BaseNCodec(32, BaseNCodec.BASE32_STANDARD);
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(new byte[] {0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48});
 BaseNCodecInputStream inputStreamToTest = new BaseNCodecInputStream(inputStream, baseNCodec, true);

 // Test reading a single byte
 int read = inputStreamToTest.read();
 byte[] expected = baseNCodec.encode(new byte[] {0x41});
 assertArrayEquals(expected, new byte[] {read < 0 ? (byte) (256 + read) : read});

 // Test reading multiple bytes into an array
 byte[] buffer = new byte[7];
 inputStreamToTest.read(buffer, 0, buffer.length);
 byte[] encoded = baseNCodec.encode(TEST_DATA);
 assertArrayEquals(encoded, buffer);

 inputStreamToTest.close();
 }

 @Test(expected = NullPointerException.class)
 public void testBaseNCodecInputStreamNullInput() throws IOException {
 baseNCodec = new BaseNCodec();
 BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, baseNCodec, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBaseNCodecInputStreamNegativeOffset() throws IOException {
 baseNCodec = new BaseNCodec();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x41, 0x42, 0x43, 0x44});
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

 try {
 baseNCodecInputStream.read(new byte[] {0}, -1, 1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBaseNCodecInputStreamNegativeLength() throws IOException {
 baseNCodec = new BaseNCodec();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x41, 0x42, 0x43, 0x44});
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

 try {
 baseNCodecInputStream.read(new byte[] {0}, 0, -1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testBaseNCodecInputStreamSkip() throws IOException {
 baseNCodec = new BaseNCodec();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x41, 0x42, 0x43, 0x44});
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

 baseNCodecInputStream.skip(5);

 int read = baseNCodecInputStream.read();
 assertFalse(read > 0);
 }
}