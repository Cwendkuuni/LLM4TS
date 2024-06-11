import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '1', (byte) '0'};
 int offset = 0;
 int length = buffer.length;
 long expected = 0175510L;
 long actual = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) 'a', (byte) '1', (byte) '0'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
 int offset = 0;
 int length = buffer.length;
 String expected = "tar.value.object.tar.gz.platform.tar.gz";
 String actual = TarUtils.parseName(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "tar.value.object.tar.gz.platform.tar.gz";
 byte[] buffer = new byte[512];
 int offset = 0;
 int length = name.length() > 511 ? 511 : name.length();
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String actual = new String(buffer, 0, length);
 String expected = name;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123456789L;
 byte[] buffer = new byte[8];
 int offset = 0;
 int length = 7;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 for (int i = 0; i < length; ++i) {
 System.out.println("Byte " + i + " is " + (int) buffer[i] + " which is " + Integer.toOctalString(buffer[i]));
 }
 String expected = "012360127";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 123456789L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 9;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 for (int i = 0; i < length; ++i) {
 System.out.println("Byte " + i + " is " + (int) buffer[i] + " which is " + Integer.toOctalString(buffer[i]));
 }
 String expected = "012360127 \u0020\u0000";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789L;
 byte[] buffer = new byte[11];
 int offset = 0;
 int length = 10;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 for (int i = 0; i < length; ++i) {
 System.out.println("Byte " + i + " is " + (int) buffer[i] + " which is " + Integer.toOctalString(buffer[i]));
 }
 String expected = "012360127012 \u0020";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
 long expected = 1573845223L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseOctalWithTrailingSpace() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '1', (byte) '0', (byte) ' '};
 int offset = 0;
 int length = buffer.length;
 long expected = 0175510L;
 long actual = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, actual);
 }
}