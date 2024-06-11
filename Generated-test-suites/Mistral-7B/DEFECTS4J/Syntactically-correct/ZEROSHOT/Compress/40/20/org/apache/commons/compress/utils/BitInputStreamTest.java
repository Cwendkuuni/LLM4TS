package org.apache.commons.compress.utils;

 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 import java.io.ByteArrayInputStream;
 import java.io.IOException;

 /**
 * Test class for BitInputStream.
 */
 public class BitInputStreamTest {

 private ByteArrayInputStream in;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws IOException {
 in = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
 bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
 }

 @Test
 public void testReadBits() throws IOException {
 // Test reading 1 bit
 Assert.assertEquals(1L, bitInputStream.readBits(1));
 // Test reading 6 bits
 Assert.assertEquals(30L, bitInputStream.readBits(6));
 // Test reading 7 bits from the last byte
 Assert.assertEquals(127L & 0xFFFFFF80L, bitInputStream.readBits(7));
 // Test reading all bits from the first byte
 Assert.assertEquals(0x01020304L, bitInputStream.readBits(32));
 // Test reading all bits from the second byte
 Assert.assertEquals(0x02030400L, bitInputStream.readBits(32));
 // Test reading all bits from the third byte
 Assert.assertEquals(0x03040000L, bitInputStream.readBits(32));
 // Test reading all bits from the fourth byte
 Assert.assertEquals(0x04000000L, bitInputStream.readBits(32));
 // Test reading past the end of the stream
 Assert.assertEquals(-1L, bitInputStream.readBits(1));
 }

 @Test
 public void testClearBitCache() throws IOException {
 // Read all bits from the stream
 bitInputStream.readBits(32);
 bitInputStream.readBits(32);
 bitInputStream.readBits(32);
 bitInputStream.readBits(32);
 // Clear the bit cache
 bitInputStream.clearBitCache();
 // Test reading 1 bit
 Assert.assertEquals(1L, bitInputStream.readBits(1));
 }
 }