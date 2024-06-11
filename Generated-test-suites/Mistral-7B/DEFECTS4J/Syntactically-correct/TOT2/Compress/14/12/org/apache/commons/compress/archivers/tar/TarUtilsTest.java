import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
 long expectedResult = 0123456776543210L;
 long actualResult = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_TrailingByteIsNotNUL() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '7', '6', '5', '4', '3', '2', '1', '1'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_LengthIsLessThanTwo() {
 byte[] buffer = new byte[]{'0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseOctalOrBinary_ValidInput_BinaryNumber() {
 byte[] buffer = new byte[]{0x81, 0x35, 0x9A, 0x75, 0x46, 0x23};
 long expectedResult = 3221225989L;
 long actualResult = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctalOrBinary_ValidInput_OctalNumber() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '6', '5', '4', '3', '2', '1'};
 long expectedResult = 012345677654321L;
 long actualResult = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinary_InvalidInput_BinaryNumberExceedsMaxValue() {
 byte[] buffer = new byte[]{0x81, 0x35, 0x9A, 0x75, 0x46, (byte) 0xFF, 0x23};
 TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 }

 @Test
 public void testParseBoolean_ValidInput() {
 byte[] buffer = new byte[]{0x01};
 boolean expectedResult = true;
 boolean actualResult = TarUtils.parseBoolean(buffer, 0);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBoolean_InvalidInput() {
 byte[] buffer = new byte[]{0x00};
 TarUtils.parseBoolean(buffer, 0);
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
 String expectedResult = "abcde";
 String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "abcdef";
 byte[] buffer = new byte[TarConstants.NAMELEN];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expectedBuffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f'};
 assertArrayEquals(expectedBuffer, Arrays.copyOfRange(buffer, 0, offset));
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 012345677654321L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '6'};
 assertArrayEquals(expectedBuffer, Arrays.copyOfRange(buffer, 0, 10));
 }

 @Test
 public void testFormatOctalBytes_ValidInput() {
 long value = 012345677654321L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '6', '5', '4'};
 assertArrayEquals(expectedBuffer, Arrays.copyOfRange(buffer, 0, 12));
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValidInput() {
 long value = 12345678901234567890L;
 byte[] buffer = new byte[10];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 byte[] expectedBuffer = new byte[]{0x85, 0x72, 0x61, 0x63, 0x5B, 0x4F, 0x42, 0x3D, 0x34, 0x29};
 assertArrayEquals(expectedBuffer, Arrays.copyOfRange(buffer, 0, 10));
 }

 @Test
 public void testFormatCheckSumOctalBytes_ValidInput() {
 long value = 123456789012345L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '6', '5', '4'};
 assertArrayEquals(expectedBuffer, Arrays.copyOfRange(buffer, 0, 12));
 }

 @Test
 public void testComputeCheckSum_ValidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expectedResult = 0x452DEABB166B5B5L;
 long actualResult = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, actualResult);
 }

}