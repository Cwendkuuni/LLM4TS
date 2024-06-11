import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(0123456L, result);
 }

 @Test
 public void testParseOctal_InvalidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', 'x'};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 't', 'a', 'r'};
 String name = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("tar.tar", name);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "MyName.ext";
 byte[] buffer = new byte[11];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'M', 'y', 'N', 'a', 'm', 'e', '.', 'e', 'x', 't', 0}, buffer);
 assertEquals(10, offset);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123456789;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '5', '6'}, buffer);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 123456789;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '5', '6', ' ', '0'}, buffer);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = -123456789;
 byte[] buffer = new byte[9];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'7', '7', '7', '6', '5', '6', '5', '4', '3'}, buffer);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 't', 'a', 'r'};
 long checksum = TarUtils.computeChecksum(buffer);
 long computedChecksum = 0;
 for (byte b : buffer) {
 computedChecksum += TarUtils.BYTE_MASK & b;
 }
 assertEquals(checksum, computedChecksum);
 }
}