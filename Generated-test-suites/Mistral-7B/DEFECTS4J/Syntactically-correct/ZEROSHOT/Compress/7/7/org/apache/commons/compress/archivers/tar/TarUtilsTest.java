import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '7'};
 int offset = 0;
 int length = buffer.length;
 long expected = 0177557L;
 long result = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '8'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 int offset = 0;
 int length = buffer.length;
 String expected = "my.tester.gzip";
 String result = TarUtils.parseName(buffer, offset, length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNameInvalidInput() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseName(buffer, offset, length);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "my.tester.gzip";
 byte[] buffer = new byte[name.length() + 1];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String result = new String(buffer, 0, length);
 assertEquals(name, result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 100;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "012";
 String result = new String(buffer, 0, length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringOverflow() {
 long value = 300;
 byte[] buffer = new byte[3];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 100;
 byte[] buffer = new byte[11];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expected = "012 \0";
 String result = new String(buffer, 0, length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesOverflow() {
 long value = 300;
 byte[] buffer = new byte[5];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 100;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 String expected = "012000 \0";
 String result = new String(buffer, 0, length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatCheckSumOctalBytesOverflow() {
 long value = 300;
 byte[] buffer = new byte[6];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x6e, (byte) 0x63, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x21, (byte) 0x00, (byte) 0x00};
 long checksum = TarUtils.computeCheckSum(buffer);
 long expectedChecksum = 0x7015a25L;
 assertEquals(expectedChecksum, checksum);
 }
}