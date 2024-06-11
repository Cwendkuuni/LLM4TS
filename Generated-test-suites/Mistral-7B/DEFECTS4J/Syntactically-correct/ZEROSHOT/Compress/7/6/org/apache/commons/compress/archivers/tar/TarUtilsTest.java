import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '5'};
 long expected = 017555;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '8'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'd'};
 String expected = "myfile.txt.tar.gz.pod";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "myfile.txt.tar.gz.pod";
 byte[] buffer = new byte[TarUtils.formatNameBytes(name, new byte[100], 0, 100).length];
 TarUtils.formatNameBytes(name, buffer, 0, 100);
 String actual = new String(buffer);
 assertEquals(name, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '2'};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '2', (byte) ' ', (byte) '0'};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x60, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x73, (byte) 0x65, (byte) 0x6d, (byte) 0x65, (byte) 0x6d, (byte) 0x73, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x78, (byte) 0x65};
 long expected = 0x46492043656d6573656d7368656d617856;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}