package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class BitInputStreamTest {

 private InputStream inputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b11110000, (byte) 0b00001111});
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws IOException {
 if (bitInputStream != null) {
 bitInputStream.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testEmptyStream() throws IOException {
 final BitInputStream emptyStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);
 emptyStream.close();
 assertEquals(-1, emptyStream.readBits(1));
 }

 @Test
 public void testReadBits() throws IOException {
 final int count = 1;
 final long expected = 1L;
 assertEquals(expected, bitInputStream.readBits(count));
 assertEquals(expected, bitInputStream.readBits(count));

 final int count1 = 7;
 final long expected1 = 127L & 0xFFFFFF80L; // 11111111 00001111 11111100
 assertEquals(expected1, bitInputStream.readBits(count));

 final int count2 = 8;
 final long expected2 = -1L;
 assertEquals(expected2, bitInputStream.readBits(count));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCount() throws IOException {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCountGreaterThanMax() throws IOException {
 bitInputStream.readBits(64);
 }

 @Test
 public void testClearBitCache() throws IOException {
 final int count = 7;
 final long expected = 127L & 0xFFFFFF80L;
 bitInputStream.readBits(count);
 assertEquals(expected, bitInputStream.readBits(count));

 bitInputStream.clearBitCache();
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsFromDifferentBytes() throws IOException {
 final int count1 = 3;
 final int count2 = 5;
 final long expected1 = 15L & 0x0000FFFFL; // 00001111 01110111
 final long expected2 = 31L & 0xFFFFFFE0L; // 00111111 11110111 11100000
 assertEquals(expected1, bitInputStream.readBits(count1));
 assertEquals(expected2, bitInputStream.readBits(count2));
 }

 @Test
 public void testCloseStream() throws IOException {
 final int count = 1;
 final long expected = 1L;
 assertEquals(expected, bitInputStream.readBits(count));

 bitInputStream.close();
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsAfterCloseStream() throws IOException {
 final int count = 1;
 final long expected = 1L;
 assertEquals(expected, bitInputStream.readBits(count));

 bitInputStream.close();
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsWithDifferentByteOrders() throws IOException {
 final InputStream inputStreamBigEndian = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b11110000, (byte) 0b00001111});
 final BitInputStream bitInputStreamBigEndian = new BitInputStream(inputStreamBigEndian, ByteOrder.BIG_ENDIAN);
 final int count = 7;
 final long expected = 127L & 0x0000FF80L; // 11111111 00001111 00001111 00000000
 assertEquals(expected, bitInputStreamBigEndian.readBits(count));

 final ByteOrder byteOrderLittleEndian = ByteOrder.LITTLE_ENDIAN;
 final ByteOrder byteOrderBigEndian = ByteOrder.BIG_ENDIAN;
 final BitInputStream bitInputStreamLittleEndian = new BitInputStream(inputStream, byteOrderLittleEndian);
 final BitInputStream bitInputStreamBigEndian2 = new BitInputStream(inputStream, byteOrderBigEndian);
 assertNotSame(bitInputStreamLittleEndian, bitInputStreamBigEndian2);
 }
}