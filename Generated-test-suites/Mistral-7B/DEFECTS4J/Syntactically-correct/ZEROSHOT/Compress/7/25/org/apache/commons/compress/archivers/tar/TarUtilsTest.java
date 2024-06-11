import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0'};
 long expected = 0175500L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseOctal_InvalidInput() {
 byte[] buffer = new byte[]{'1', '2', '3'};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
 String expected = "tar.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.txt";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'t', 'e', 's', 't', '.', 't', 'x', 't', 'x', 0}, buffer);
 assertEquals(10, offset);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345L;
 byte[] buffer = new byte[8];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '7', '5', '4', '5', '7', '5'}, buffer);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345L;
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '7', '5', '4', '5', '7', '5', ' ', '0'}, buffer);
 assertEquals(10, offset);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', 0x00, 0x00};
 long expected = 2893506533107331640L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}