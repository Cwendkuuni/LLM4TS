package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

 private InputStream inputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x0F, (byte) 0x32, (byte) 0x1A, (byte) 0x55, (byte) 0x01});
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws IOException {
 inputStream.close();
 bitInputStream.close();
 }

 @Test
 public void testConstructor() {
 assertNotNull(bitInputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidByteOrder() {
 BitInputStream bitInputStreamWithInvalidByteOrder = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
 assertNull(bitInputStreamWithInvalidByteOrder);
 }

 @Test
 public void testClose() throws IOException {
 inputStream.close();
 assertFalse(inputStream.isOpen());
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithNegativeCount() throws IOException {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
 bitInputStream.readBits(64);
 }

 @Test
 public void testReadBitsWithSmallCount() throws IOException {
 assertEquals(0x0F & 0x01, bitInputStream.readBits(1));
 assertEquals(0x0F & 0x03, bitInputStream.readBits(2));
 assertEquals(0x0F & 0x1C, bitInputStream.readBits(6));
 assertEquals(0x32, bitInputStream.readBits(8));
 assertEquals(0x1A, bitInputStream.readBits(9));
 assertEquals(0x55, bitInputStream.readBits(12));
 assertEquals(0x01, bitInputStream.readBits(13));
 }

 @Test
 public void testReadBitsWithLongerCount() throws IOException {
 long bits = 0;
 long expected = 0x321A5501;
 for (int i = 0; i < 32; i++) {
 bits |= bitInputStream.readBits(1);
 }
 assertEquals(expected, bits);
 }

}