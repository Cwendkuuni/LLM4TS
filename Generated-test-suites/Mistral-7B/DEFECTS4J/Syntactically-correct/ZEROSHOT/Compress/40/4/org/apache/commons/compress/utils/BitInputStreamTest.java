package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BitInputStreamTest {

 private InputStream inputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x5A, (byte) 0x6F, (byte) 0x70, (byte) 0x75, (byte) 0x72});
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws Exception {
 bitInputStream.close();
 inputStream.close();
 }

 @Test
 public void testReadBitsWithValidCount() throws Exception {
 // Test reading 1 bit
 assertEquals(1L, bitInputStream.readBits(1));
 // Test reading 63 bits
 long expectedBits = 0b1011011011011011011011011011011011011011011011011;
 long actualBits = 0;
 for (int i = 0; i < 6; i++) {
 actualBits <<= 6;
 actualBits |= bitInputStream.readBits(6);
 }
 assertEquals(expectedBits, actualBits);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithNegativeCount() throws Exception {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithCountGreaterThanMaximum() throws Exception {
 bitInputStream.readBits(64);
 }

 @Test
 public void testClearBitCache() throws Exception {
 long bits = 0b1111111111111111;
 for (int i = 0; i < 12; i++) {
 bits <<= 8;
 bits |= bitInputStream.readBits(8);
 }
 assertEquals(bits, bitInputStream.readBits(64));
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(1));
 }
}