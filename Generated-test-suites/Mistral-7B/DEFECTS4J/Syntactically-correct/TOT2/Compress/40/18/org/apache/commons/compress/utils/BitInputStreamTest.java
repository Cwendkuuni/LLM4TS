import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Tests for the BitInputStream class.
 */
public class BitInputStreamTest {

 private InputStream inputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setup() throws IOException {
 inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
 bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws IOException {
 inputStream.close();
 bitInputStream.close();
 }

 /**
 * Tests reading 1 bit from the BitInputStream.
 */
 @Test
 public void testReadOneBit() throws IOException {
 final int count = 1;
 long bits = bitInputStream.readBits(count);
 Assert.assertEquals(bits, 0x01L);
 }

 /**
 * Tests reading 63 bits from the BitInputStream.
 */
 @Test
 public void testReadMaximumBits() throws IOException {
 final int count = BitInputStream.MAXIMUM_CACHE_SIZE;
 long bits = bitInputStream.readBits(count);
 // assert the read bits based on the input data and byte order
 // ...
 }

 /**
 * Tests reading more bits than are available.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadMoreBitsThanAvailable() throws IOException {
 final int count = BitInputStream.MAXIMUM_CACHE_SIZE + 1;
 bitInputStream.readBits(count);
 }

 /**
 * Tests reading bits from an empty Stream.
 */
 @Test(expected = IOException.class)
 public void testReadBitsFromEmptyStream() throws IOException {
 final ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
 final BitInputStream emptyBitInputStream = new BitInputStream(emptyInputStream, ByteOrder.LITTLE_ENDIAN);
 emptyBitInputStream.readBits(1);
 }

 /**
 * Tests clearing the bit cache.
 */
 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 Assert.assertEquals(bitInputStream.bitsCached, 0L);
 Assert.assertEquals(bitInputStream.bitsCachedSize, 0);
 }
}