import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', 0};
 long expected = 0177141L; // Octal representation of the byte sequence
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidByte() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '8', (byte) '7', 0}; // Non-octal byte '8'
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) ' ', 0};
 String expected = "myfile.tar.gz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "myfile.tar.gz";
 byte[] buffer = new byte[32];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(32, length);
 String result = new String(buffer, 0, length);
 assertEquals("myfile.tar.gz", result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 01234567L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 // Check that the first 8 bytes are '0' followed by the octal digits of the value
 for (int i = 0; i < 8; i++) {
 assertEquals((byte) '0', buffer[offset + i]);
 }
 // The remaining 2 bytes are space and null
 assertEquals((byte) ' ', buffer[offset + 8]);
 assertEquals(0, buffer[offset + 9]);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 01234567L;
 byte[] buffer = new byte[11];
 int offset = 0;
 int length = 11;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 // The first 9 bytes are the octal digits of the value followed by a space
 for (int i = 0; i < 9; i++) {
 assertEquals((byte) (i < 7 ? '0' + (byte) (value & 7) : ' '), buffer[offset + i]);
 }
 // The last 2 bytes are a null and a space
 assertEquals(0, buffer[offset + 9]);
 assertEquals((byte) ' ', buffer[offset + 10]);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x63, (byte) 0x69, (byte) 0x73, (byte) 0x74, (byte) 0x31, (byte) 0x36, (byte) 0x35, (byte) 0x37, (byte) 0x34, (byte) 0x33, (byte) 0x32, 0};
 long expected = 0x68756c01657263697331363534332dL; // Compute the checksum using the formula in the code
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}