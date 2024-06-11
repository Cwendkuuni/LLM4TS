import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '6', (byte) '7'};
 long expected = 01234140L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '8', (byte) '3', (byte) '4', (byte) '7'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) '.', (byte) 's', (byte) 'a', (byte) 'm', (byte) 'p', (byte) 'l', (byte) 'e', (byte) 0};
 String expected = "tar.ochcks.sample";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "MyTestFile.txt";
 byte[] buffer = new byte[TarUtils.formatNameBytes(name, new byte[1024], 0, 1024).length];
 TarUtils.formatNameBytes(name, buffer, 0, 1024);
 String actual = new String(buffer);
 String expected = "MyTestFile.txt";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
 String expected = "01234567";
 String actual = new String(buffer);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringWithTooSmallBuffer() {
 long value = 1234567;
 byte[] buffer = new byte[5];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x63, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x53, (byte) 0x74, (byte) 0x72, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x31, (byte) 0, (byte) 0};
 long expected = 1456198150L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

}