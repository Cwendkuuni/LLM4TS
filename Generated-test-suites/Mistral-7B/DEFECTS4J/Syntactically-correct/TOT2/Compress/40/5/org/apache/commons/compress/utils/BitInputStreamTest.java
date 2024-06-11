import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

 private InputStream testInputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws Exception {
 testInputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
 bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws Exception {
 bitInputStream.close();
 testInputStream.close();
 }

 /**
 * Test constructor with null InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullInputStream() {
 new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
 }

 /**
 * Test constructor with null ByteOrder
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullByteOrder() {
 new BitInputStream(testInputStream, null);
 }

 /**
 * Test readBits with valid count
 */
 @Test
 public void testReadBitsWithValidCount() throws IOException {
 // Test reading 1 bit
 final long bit1 = bitInputStream.readBits(1);
 assertEquals(1, bit1);

 // Test reading 63 bits
 final long bits63 = bitInputStream.readBits(63);
 assertEquals(0x0102030405L, bits63);
 }

 /**
 * Test readBits with invalid count
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithInvalidCount() throws IOException {
 // Test reading a negative number of bits
 bitInputStream.readBits(-1);

 // Test reading a number of bits greater than 63
 bitInputStream.readBits(64);
 }

 /**
 * Test clearBitCache
 */
 @Test
 public void testClearBitCache() throws IOException {
 // Test clearing the cache when no bits have been read
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(1));

 // Test clearing the cache after reading some bits
 bitInputStream.readBits(1);
 bitInputStream.readBits(1);
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(2));
 }

 /**
 * Test readBits after EOF
 */
 @Test(expected = IOException.class)
 public void testReadBitsAfterEOF() throws IOException {
 // Read all available bits
 bitInputStream.readBits(16);

 // Try reading more bits
 bitInputStream.readBits(1);
 }
}