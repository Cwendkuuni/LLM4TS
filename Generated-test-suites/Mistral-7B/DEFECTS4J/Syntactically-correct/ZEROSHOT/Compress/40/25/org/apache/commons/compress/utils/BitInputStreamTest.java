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
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d});
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws Exception {
 if (bitInputStream != null) {
 bitInputStream.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testEmptyBitStream() throws Exception {
 BitInputStream emptyBitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);
 emptyBitInputStream.close();
 assertEquals(-1, emptyBitInputStream.readBits(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCount() throws Exception {
 bitInputStream.readBits(-2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountBiggerThanMaximum() throws Exception {
 bitInputStream.readBits(64);
 }

 @Test
 public void testReadBits() throws Exception {
 long expected = 0b1111010111101101;
 long readBits = bitInputStream.readBits(10);
 assertEquals(expected, readBits);
 }

 @Test
 public void testReadBitsFromEndOfStream() throws Exception {
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testClearBitCache() throws Exception {
 bitInputStream.clearBitCache();
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsFromByteOrderLittleEndian() throws Exception {
 long expected = 0b0111110111101101;
 long readBits = bitInputStream.readBits(8);
 long readBits2 = bitInputStream.readBits(2);
 long readBits3 = bitInputStream.readBits(6);
 assertEquals(expected, readBits << 2 | readBits2 | readBits3);
 }

 @Test
 public void testReadBitsFromByteOrderBigEndian() throws Exception {
 ByteArrayInputStream inputStreamBE = new ByteArrayInputStream(new byte[]{0x3c, 0x2b, 0x1a, 0x4d});
 BitInputStream bitInputStreamBE = new BitInputStream(inputStreamBE, ByteOrder.BIG_ENDIAN);
 long expected = 0b1111010111101101;
 long readBits = bitInputStreamBE.readBits(10);
 assertEquals(expected, readBits);
 }
}