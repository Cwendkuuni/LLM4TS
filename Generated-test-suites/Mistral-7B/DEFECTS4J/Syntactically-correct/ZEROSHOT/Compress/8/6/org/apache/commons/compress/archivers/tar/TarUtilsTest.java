import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[] {'0', '7', '7', '7', '0'};
 long expected = 07770L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[] {'0', '7', '7', '7', '8'}; // invalid byte '8'
 long offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[] {(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 String expected = "tar.op.tx.tar.gzip";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "mytarfile.tar.gz";
 byte[] buffer = new byte[name.length() + 1];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String actual = new String(buffer, 0, offset);
 String expected = "mytarfile.tar.gz";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "012345";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[13];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345 ";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[14];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0123456789012345 ";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[13];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0123456789012345  ";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }
}