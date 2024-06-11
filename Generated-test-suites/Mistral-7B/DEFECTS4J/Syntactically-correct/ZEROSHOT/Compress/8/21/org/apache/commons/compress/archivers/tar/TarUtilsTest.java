import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0'};
 long expected = 0175540L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', 'x', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) ' ', (byte) '0'};
 String expected = "tar.gz  .tar.xz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "myfile.txt";
 byte[] buffer = new byte[256];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String result = new String(buffer, 0, offset);
 assertEquals("myfile.txt", result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123456789L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "01234567541";
 String result = new String(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 123456789L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567541  ";
 String result = new String(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[12];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567541012345";
 String result = new String(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567541  ";
 String result = new String(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x90, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
 long expected = 0x1A8D24F47890L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}