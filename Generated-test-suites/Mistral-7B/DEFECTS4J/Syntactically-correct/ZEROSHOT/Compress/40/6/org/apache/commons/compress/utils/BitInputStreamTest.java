package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for the BitInputStream class.
 */
public class BitInputStreamTest {

 private BitInputStream bitInputStream;
 private ByteArrayInputStream byteArrayInputStream;

 @Before
 public void setUp() {
 byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56});
 bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56});
 ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
 BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
 Assert.assertNotNull(bitInputStream);
 }

 /**
 * Test close method.
 */
 @Test
 public void testClose() throws IOException {
 bitInputStream.close();
 byteArrayInputStream.close();
 }

 /**
 * Test clearBitCache method.
 */
 @Test
 public void testClearBitCache() {
 bitInputStream.clearBitCache();
 Assert.assertEquals(0, bitInputStream.bitsCached);
 Assert.assertEquals(0, bitInputStream.bitsCachedSize);
 }

 /**
 * Test readBits method with valid input.
 */
 @Test
 public void testReadBitsWithValidInput() throws IOException {
 // Test reading a single bit
 Assert.assertEquals(0L, bitInputStream.readBits(1));
 Assert.assertEquals(1L, bitInputStream.readBits(1));

 // Test reading multiple bits
 Assert.assertEquals(0x12L & 1L, bitInputStream.readBits(1)); // read LSB of first byte
 Assert.assertEquals(0x12L & 3L >> 1, bitInputStream.readBits(2)); // read MSB of first byte and LSB of second byte
 Assert.assertEquals((0x12L & 0xC0L) >> 6, bitInputStream.readBits(3)); // read MSB of first byte
 Assert.assertEquals((0x34L & 0x3F) << 3, bitInputStream.readBits(6)); // read first 3 bits of second byte

 // Test reading 64 bits (full long)
 Assert.assertEquals(0x123456789ABCDEF0L, bitInputStream.readBits(64));
 }

 /**
 * Test readBits method with invalid input.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithInvalidInput() throws IOException {
 // Test negative count
 bitInputStream.readBits(-1);

 // Test count greater than MAXIMUM_CACHE_SIZE
 bitInputStream.readBits(64);
 }

 /**
 * Test readBits method with end of stream.
 */
 @Test
 public void testReadBitsWithEndOfStream() throws IOException {
 // Read all available bits
 Assert.assertEquals(0x123456L, bitInputStream.readBits(21));

 // End of stream has been reached
 Assert.assertEquals(-1L, bitInputStream.readBits(1));
 bitInputStream.close();
 }
}