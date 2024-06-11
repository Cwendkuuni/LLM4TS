import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '0' };
 long expected = 017755550L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) 'A', (byte) '5', (byte) '0' };
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[] { (byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) ' ', (byte) '0' };
 String expected = "myfile.tar.gz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "myfile.tar.gz";
 byte[] buffer = new byte[25];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[] { (byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) ' ', (byte) '0' };
 for (int i = 0; i < offset; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) ' ', (byte) '0' };
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[11];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ' };
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ' };
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 5678;
 byte[] buffer = new byte[11];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '1', (byte) '2', (byte) '3', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) ' ', (byte) '0' };
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[] { (byte) '1', (byte) '2', (byte) '3' };
 long expected = 6045891605251553808L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}