import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidOctalString() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', 'NUL'};
 int offset = 0;
 int length = 10;
 long expectedResult = 012345675L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_TrailingNUL() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', 'NUL', 'NUL'};
 int offset = 0;
 int length = 11;
 long expectedResult = 012345675L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_TrailingSpace() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', ' ', 'NUL'};
 int offset = 0;
 int length = 11;
 long expectedResult = 012345675L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_EmptyBuffer() {
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = 0;
 long expectedResult = 0L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_InvalidOctalString() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', 'A', '5', '6', '7', '8', 'NUL'};
 int offset = 0;
 int length = 11;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.parseOctal(buffer, offset, length);
 });
 }

 @Test
 public void testParseOctalOrBinary_ValidOctalString() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int length = 9;
 long expectedResult = 012345675L;
 long actualResult = TarUtils.parseOctalOrBinary(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctalOrBinary_ValidBinaryString() {
 byte[] buffer = new byte[]{(byte) 0x80, 0x30, 0x23, 0xAB, 0xCD, 0xEF};
 int offset = 0;
 int length = 6;
 long expectedResult = 0x3023ABCDEF;
 long actualResult = TarUtils.parseOctalOrBinary(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctalOrBinary_InvalidOctalOrBinaryString() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', 'A', '5'};
 int offset = 0;
 int length = 7;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.parseOctalOrBinary(buffer, offset, length);
 });
 }

 @Test
 public void testParseBoolean_ValidBooleanByte() {
 byte[] buffer = new byte[]{1};
 int offset = 0;
 boolean expectedResult = true;
 boolean actualResult = TarUtils.parseBoolean(buffer, offset);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseBoolean_InvalidBooleanByte() {
 byte[] buffer = new byte[]{0};
 int offset = 0;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.parseBoolean(buffer, offset);
 });
 }

 @Test
 public void testParseName_ValidEntryName() {
 byte[] buffer = new byte[]{'.'.charAt(0), 'a'.charAt(0), 'b'.charAt(0), 'c'.charAt(0), 'd'.charAt(0), 'e'.charAt(0), '.'.charAt(0), '.'.charAt(0)};
 int offset = 0;
 int length = 8;
 String expectedResult = "abc.de./.";
 String actualResult = TarUtils.parseName(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseName_TruncatedName() {
 byte[] buffer = new byte[]{'.'.charAt(0), 'a'.charAt(0), 'b'.charAt(0), 'c'.charAt(0), 'd'.charAt(0)};
 int offset = 0;
 int length = 5;
 String expectedResult = "abc.d";
 String actualResult = TarUtils.parseName(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatNameBytes_ValidName() {
 String name = "abc.de./.";
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 int expectedResult = TarUtils.formatNameBytes(name, buffer, offset, length);
 Assertions.assertEquals(expectedResult, offset + length);
 }

 @Test
 public void testFormatNameBytes_TruncatedName() {
 String name = "abc.d";
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 int expectedResult = TarUtils.formatNameBytes(name, buffer, offset, length);
 Assertions.assertEquals(expectedResult, offset + 5); // Name is truncated to "abc.d"
 }

 @Test
 public void testFormatUnsignedOctalString_ValidValue() {
 long value = 12345675L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expectedResult = "012345675";
 String actualResult = new String(buffer, 0, 10);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatUnsignedOctalString_ValueTooLarge() {
 long value = TarConstants.MAXID * 2L;
 byte[] buffer = new byte[TarConstants.UIDLEN];
 int offset = 0;
 int length = TarConstants.UIDLEN;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 });
 }

 @Test
 public void testFormatOctalBytes_ValidValue() {
 long value = 12345675L;
 byte[] buffer = new byte[11];
 int offset = 0;
 int length = 11;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expectedResult = "012345675 ";
 String actualResult = new String(buffer, 0, 11);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatLongOctalBytes_ValidValue() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatLongOctalBytes(value, buffer, offset, length);
 String expectedResult = "01234567890";
 String actualResult = new String(buffer, 0, 12);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValidValue() {
 long value = 1234567890L;
 byte[] buffer = new byte[TarConstants.UIDLEN];
 int offset = 0;
 int length = TarConstants.UIDLEN;
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
 String expectedResult = "01234567890";
 String actualResult = new String(buffer, 0, TarConstants.UIDLEN);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValueTooLarge_Octal() {
 long value = TarConstants.MAXID * 2L;
 byte[] buffer = new byte[TarConstants.UIDLEN];
 int offset = 0;
 int length = TarConstants.UIDLEN;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
 });
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValueTooLarge_Binary() {
 long value = TarConstants.MAXID * 2L + 1L;
 byte[] buffer = new byte[TarConstants.UIDLEN];
 int offset = 0;
 int length = TarConstants.UIDLEN;
 Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
 });
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
 long expectedResult = 171 * 256L + 185L;
 long actualResult = TarUtils.computeCheckSum(buffer);
 Assertions.assertEquals(expectedResult, actualResult);
 }
}