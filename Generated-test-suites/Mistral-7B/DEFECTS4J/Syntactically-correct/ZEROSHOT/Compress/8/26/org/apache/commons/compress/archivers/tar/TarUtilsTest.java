import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '1', (byte) '0'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(077710L, result);

 buffer = new byte[]{(byte) '0', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5'};
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(0555555L, result);

 buffer = new byte[]{(byte) '1', (byte) '0'};
 try {
 TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 buffer = new byte[]{};
 try {
 TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{
 ('\u0000').codeValue(), 'm', 'y', '.', 'T', 'e', 's', 't', '\u0000', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '\u0000'
 };
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("my.Test.tar", result);

 buffer = new byte[]{('a').codeValue(), 'b'.codeValue()};
 result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("ab", result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "my.Test.tar";
 byte[] buffer = new byte[20];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(20, offset);
 for (int i = 0; i < buffer.length; ++i) {
 if (i < name.length()) {
 assertEquals(name.charAt(i), (char) buffer[i]);
 } else {
 assertEquals((byte) 0, buffer[i]);
 }
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 255;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals(1, buffer.length);
 assertEquals((byte) '7', buffer[0]);

 value = 65535;
 buffer = new byte[11];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals(2, buffer.length);
 assertEquals((byte) '7', buffer[0]);
 assertEquals((byte) '7', buffer[1]);

 value = 16777215;
 buffer = new byte[13];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals(3, buffer.length);
 assertEquals((byte) '7', buffer[0]);
 assertEquals((byte) '7', buffer[1]);
 assertEquals((byte) '7', buffer[2]);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 255;
 byte[] buffer = new byte[6];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(6, buffer.length);
 assertEquals((byte) '0', buffer[0]);
 assertEquals((byte) '1', buffer[1]);
 assertEquals((byte) '7', buffer[2]);
 assertEquals((byte) '7', buffer[3]);
 assertEquals((byte) '1', buffer[4]);
 assertEquals((byte) '0', buffer[5]);

 value = 65535;
 buffer = new byte[7];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(7, buffer.length);
 assertEquals((byte) '0', buffer[0]);
 assertEquals((byte) '1', buffer[1]);
 assertEquals((byte) '1', buffer[2]);
 assertEquals((byte) '7', buffer[3]);
 assertEquals((byte) '7', buffer[4]);
 assertEquals((byte) '7', buffer[5]);
 assertEquals((byte) '1', buffer[6]);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 255;
 byte[] buffer = new byte[5];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(5, buffer.length);
 assertEquals((byte) '0', buffer[0]);
 assertEquals((byte) '1', buffer[1]);
 assertEquals((byte) '7', buffer[2]);
 assertEquals((byte) '1', buffer[3]);
 assertEquals((byte) '0', buffer[4]);

 value = 65535;
 buffer = new byte[6];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(6, buffer.length);
 assertEquals((byte) '0', buffer[0]);
 assertEquals((byte) '1', buffer[1]);
 assertEquals((byte) '1', buffer[2]);
 assertEquals((byte) '1', buffer[3]);
 assertEquals((byte) '7', buffer[4]);
 assertEquals((byte) '7', buffer[5]);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 255;
 byte[] buffer = new byte[7];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(7, buffer.length);
 assertEquals((byte) '0', buffer[0]);
 assertEquals((byte) '1', buffer[1]);
 assertEquals((byte) '7', buffer[2]);
 assertEquals((byte) '7', buffer[3]);
 assertEquals((byte) '1', buffer[4]);
 assertEquals((byte) '0', buffer[5]);
 assertEquals((byte) '0', buffer[6]);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{
 (byte) 0x67, (byte) 0x72, (byte) 0x6F, (byte) 0x75, (byte) 0x74, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0, (byte) 0x83, (byte) 0xCC, (byte) 0x5D,
 (byte) 0x6F, (byte) 0x6F, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0, (byte) 0x00, (byte) 0, (byte) 0xC7, (byte) 0x5C, (byte) 0x49, (byte) 0x53, (byte) 0x41, (byte) 0x4E,
 (byte) 0x42, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0, (byte) 0x0B, (byte) 0x78, (byte) 0x5F, (byte) 0x41, (byte) 0x53, (byte) 0x53, (byte) 0x48,
 (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0, (byte) 0x00, (byte) 0x2B, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0
 };
 long checksum = TarUtils.computeCheckSum(buffer);
 long newChecksum = 0;
 for (byte b : buffer) {
 newChecksum += TarUtils.BYTE_MASK & b;
 }
 assertEquals(checksum, newChecksum);
 }
}