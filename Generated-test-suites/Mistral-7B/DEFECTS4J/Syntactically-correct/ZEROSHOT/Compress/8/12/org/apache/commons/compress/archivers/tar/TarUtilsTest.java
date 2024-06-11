import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '4', '6', '0', '0', '0'};
 long expected = 0175460L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '4', '6', 'A', '0', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidLength() {
 byte[] buffer = new byte[]{'0', '7', '5'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 String expected = "hello.tar.gz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNameWithInvalidLength() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o'};
 TarUtils.parseName(buffer, 0, buffer.length);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "hello.tar.gz";
 byte[] buffer = new byte[30];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String result = new String(buffer, 0, offset);
 String expected = "hello.tar.gz";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 321;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "0113111";
 String result = new String(buffer, 0, 10);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 321;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0113111  ";
 String result = new String(buffer, 0, 12);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 4656352151452L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "7615257520567";
 String result = new String(buffer, 0, 13);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 8178216523845L;
 byte[] buffer = new byte[14];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "1776654111675 ";
 String result = new String(buffer, 0, 14);
 assertEquals(expected, result);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', ' ', '7', '5', ' '};
 long expected = 31175121554L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}