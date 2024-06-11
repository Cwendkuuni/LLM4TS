import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '0'};
 int offset = 0;
 int length = 5;
 long expected = 017550L;
 long actual = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '1', (byte) '8'};
 int offset = 0;
 int length = 2;
 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) 0};
 int offset = 0;
 int length = buffer.length;
 String expected = "hello.tx.tar.gz.tar.xz";
 String actual = TarUtils.parseName(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "hello.txt";
 byte[] buffer = new byte[32];
 int offset = 0;
 int length = name.length();
 int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
 byte[] expected = new byte[]{
 'h', 'e', 'l', 'l', 'o', '.', 't', 'x', 't',
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
 };
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 byte[] expected = new byte[]{
 '0', '0', '2', '7', '7', '5', '5', '5', '0', ' '
 };
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 byte[] expected = new byte[]{
 '0', '0', '2', '7', '7', '5', '5', '5', '0', ' ', ' '
 };
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
 long expected = 0x12345678L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

}