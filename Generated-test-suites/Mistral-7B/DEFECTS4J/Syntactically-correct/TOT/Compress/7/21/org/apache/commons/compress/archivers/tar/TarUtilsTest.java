import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0'};
 long expected = 01234567;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', 'A', '4', '5', '6', '7', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 String expected = "tar.entry.gz";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "tar.entry.gz";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'t', 'a', 'r', '.', 'e', 'n', 't', 'r', 'y', '.', 'g', 'z', 0, 0, 0, 0, 0};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0', ' '};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringLongValue() {
 long value = 123456789012345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', ' ', '1', '2', '3', '4', '5'};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}