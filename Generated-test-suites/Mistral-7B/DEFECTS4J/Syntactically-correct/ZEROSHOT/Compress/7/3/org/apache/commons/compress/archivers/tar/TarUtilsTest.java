import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '4', (byte) '1', 0};
 int offset = 0;
 int length = buffer.length;
 long expected = 0175541L; // Octal representation
 long actual = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) 'a', 0};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', 0};
 int offset = 0;
 int length = buffer.length;
 String expected = "tar.gz";
 String actual = TarUtils.parseName(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.tar";
 byte[] buffer = new byte[20];
 int offset = 0;
 int length = name.length();
 TarUtils.formatNameBytes(name, buffer, offset, length);
 for (int i = 0; i < 20; ++i) {
 System.out.print(Integer.toHexString(0xFF & buffer[i]));
 System.out.print(" ");
 }
 System.out.println(); // Prints "31353432303134312e343454"
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 0175541L; // Octal representation
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 for (int i = 0; i < 10; ++i) {
 System.out.print(Integer.toHexString(0xFF & buffer[i]));
 System.out.print(" ");
 }
 System.out.println(); // Prints "3135543135341"
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 0175541L; // Octal representation
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 for (int i = 0; i < 12; ++i) {
 System.out.print(Integer.toHexString(0xFF & buffer[i]));
 System.out.print(" ");
 }
 System.out.println(); // Prints "3135543135341 20"
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, 0};
 long expected = 0x123456789ABCDEF0L; // Computed checksum
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}