package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream(new byte[0]);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 inputStream.close();
 outputStream.close();
 }

 /**
 * Test constructor with null InputStream.
 */
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullInputStream() {
 new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
 }

 /**
 * Test constructor with null ByteOrder.
 */
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullByteOrder() {
 new BitInputStream(inputStream, null);
 }

 /**
 * Test reading 0 bits from the empty stream.
 */
 @Test
 public void testRead0Bits() throws IOException {
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 assertEquals(0, bitInputStream.readBits(0));
 }

 /**
 * Test reading less than 1 bit from the empty stream.
 */
 @Test
 public void testReadLessThan1Bit() throws IOException {
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 assertEquals(0, bitInputStream.readBits(1));
 }

 /**
 * Test reading more than the available bits from the empty stream.
 */
 @Test(expected = IOException.class)
 public void testReadMoreThanAvailableBits() throws IOException {
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 bitInputStream.readBits(8);
 }

 /**
 * Test reading bits from a stream with provided bytes.
 */
 @Test
 public void testReadBitsFromStream() throws IOException {
 outputStream.write(new byte[]{(byte) 0b10111111, (byte) 0b01111000, (byte) 0b11110111, (byte) 0b11111111});
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

 assertEquals(0b10111111, bitInputStream.readBits(8));
 assertEquals(0b11110000, bitInputStream.readBits(8));
 assertEquals(0b11111111, bitInputStream.readBits(8));

 assertFalse(bitInputStream.hasRemainingBits());
 }

 /**
 * Test reading bits from a stream with provided bytes in big endian order.
 */
 @Test
 public void testReadBitsFromStreamBigEndian() throws IOException {
 outputStream.write(new byte[]{(byte) 0b11111111, (byte) 0b01111000, (byte) 0b10111111, (byte) 0b11110111});
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

 assertEquals(0b11111111, bitInputStream.readBits(8));
 assertEquals(0b10111111, bitInputStream.readBits(8));
 assertEquals(0b01111000, bitInputStream.readBits(8));

 assertFalse(bitInputStream.hasRemainingBits());
 }

 /**
 * Test clearing bit cache.
 */
 @Test
 public void testClearBitCache() throws IOException {
 final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 bitInputStream.readBits(1);
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(1));
 }
}