package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for BitInputStream class
 */
public class BitInputStreamTest {

 private InputStream inputStream;
 private BitInputStream bitInputStream;
 private byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(testData);
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws IOException {
 bitInputStream.close();
 inputStream.close();
 }

 @Test
 public void testConstructor() {
 InputStream in = null;
 try {
 new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
 fail("Expected IllegalArgumentException when inputStream is null.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 in = new ByteArrayInputStream(new byte[] {});
 try {
 new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
 fail("Expected IllegalArgumentException when inputStream has no data.");
 } catch (IllegalArgumentException e) {
 // expected
 }

 new BitInputStream(new ByteArrayInputStream(testData), ByteOrder.LITTLE_ENDIAN);
 new BitInputStream(new ByteArrayInputStream(testData), ByteOrder.BIG_ENDIAN);
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 long readBits = bitInputStream.readBits(10);
 assert readBits == -1;
 }

 @Test
 public void testReadBits() throws IOException {
 testReadBits(1, 1);
 testReadBits(1, 7);
 testReadBits(1, 8);
 testReadBits(1, 9);
 testReadBits(1, 63);
 testReadBits(1, 64);
 testReadBits(1, 65); // expected IOException
 testReadBits(1, 64, ByteOrder.BIG_ENDIAN);
 }

 @Test
 public void testReadBitsMultipleBytes() throws IOException {
 testReadBits(3, 3);
 testReadBits(3, 4);
 testReadBits(3, 5);
 testReadBits(3, 6);
 testReadBits(3, 7);
 testReadBits(3, 8);
 testReadBits(3, 9);
 testReadBits(3, 12);
 testReadBits(3, 13); // expected IOException
 testReadBits(3, 16);
 testReadBits(3, 17); // expected IOException
 testReadBits(3, 24);
 testReadBits(3, 25); // expected IOException
 testReadBits(3, 32);
 testReadBits(3, 33); // expected IOException
 testReadBits(3, 40);
 testReadBits(3, 41); // expected IOException
 testReadBits(3, 48);
 testReadBits(3, 49); // expected IOException
 testReadBits(3, 56);
 testReadBits(3, 57); // expected IOException
 testReadBits(3, 63);
 testReadBits(3, 64, ByteOrder.BIG_ENDIAN);
 }

 @Test
 public void testReadBitsMultipleBytesWithPartialLastByte() throws IOException {
 testReadBits(4, 3);
 testReadBits(4, 4);
 testReadBits(4, 5);
 testReadBits(4, 6);
 testReadBits(4, 7);
 testReadBits(4, 8);
 testReadBits(4, 9);
 testReadBits(4, 12);
 testReadBits(4, 13); // expected IOException
 testReadBits(4, 16);
 testReadBits(4, 17); // expected IOException
 testReadBits(4, 24);
 testReadBits(4, 25); // expected IOException
 testReadBits(4, 32);
 testReadBits(4, 33); // expected IOException
 testReadBits(4, 40);
 testReadBits(4, 41); // expected IOException
 testReadBits(4, 48);
 testReadBits(4, 49); // expected IOException
 testReadBits(4, 56);
 testReadBits(4, 57); // expected IOException
 testReadBits(4, 63);
 testReadBits(4, 64, ByteOrder.BIG_ENDIAN);
 }

 @Test(expected = IOException.class)
 public void testReadBitsNegativeCount() throws IOException {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IOException.class)
 public void testReadBitsBeyondEndOfStream() throws IOException {
 bitInputStream.clearBitCache();
 long readBits = bitInputStream.readBits(1);
 assert readBits == 0x01;
 long readBits2 = bitInputStream.readBits(1);
 assert readBits2 == -1;
 }

 @Test
 public void testReadBitsWithCache() throws IOException {
 bitInputStream.clearBitCache();
 long cache = 0x00;
 long readBits1 = bitInputStream.readBits(3);
 cache |= readBits1 << 3;
 long readBits2 = bitInputStream.readBits(5);
 cache |= readBits2 << (3 + 5);
 long readBits3 = bitInputStream.readBits(1);
 cache |= readBits3 << (3 + 5 + 1);
 assert (cache & 0xFFFFFFFFFFFFFL) == 0x010203L;
 }

 @Test
 public void testReadBitsWithBigEndian() throws IOException {
 InputStream inputStreamBigEndian = new ByteArrayInputStream(testData);
 BitInputStream bitInputStreamBigEndian = new BitInputStream(inputStreamBigEndian, ByteOrder.BIG_ENDIAN);
 testReadBits(1, 1, bitInputStreamBigEndian);
 testReadBits(1, 7, bitInputStreamBigEndian);
 testReadBits(1, 8, bitInputStreamBigEndian);
 testReadBits(1, 9, bitInputStreamBigEndian);
 testReadBits(1, 63, bitInputStreamBigEndian);
 testReadBits(1, 64, bitInputStreamBigEndian);
 testReadBits(1, 65, bitInputStreamBigEndian); // expected IOException
 testReadBits(1, 64, bitInputStreamBigEndian, ByteOrder.LITTLE_ENDIAN);
 testReadBits(1, 64, bitInputStreamBigEndian, ByteOrder.BIG_ENDIAN);
 }

 private void testReadBits(int expectedCount, int readCount, BitInputStream bitInputStream) throws IOException {
 testReadBits(expectedCount, readCount, bitInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 private void testReadBits(int expectedCount, int readCount, BitInputStream bitInputStream, ByteOrder byteOrder) throws IOException {
 long readBits = 0;
 for (int i = 0; i < expectedCount; i++) {
 long bit = bitInputStream.readBits(1);
 readBits |= (bit << i);
 }
 fairlyEqual(readBits, testData2LongArray(readCount), "Unexpected result reading " + readCount + " bits.");
 }

 private long[] testData2LongArray(int length) {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 for (int i = 0; i < length / 8; i++) {
 outputStream.write(testData[i]);
 }
 if (length % 8 > 0) {
 outputStream.write(testData[length / 8]);
 }
 byte[] data = outputStream.toByteArray();
 long[] result = new long[data.length / 8 + (length % 8 > 0 ? 1 : 0)];
 System.arraycopy(data, 0, result, 0, result.length * 8);
 return result;
 }

}