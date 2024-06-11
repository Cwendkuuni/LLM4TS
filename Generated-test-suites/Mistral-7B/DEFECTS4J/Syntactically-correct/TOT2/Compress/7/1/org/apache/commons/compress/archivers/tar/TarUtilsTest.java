import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

 // Test for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7','5','5','7'};
 long expected = 017557;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test for parseOctal with invalid octal digits
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidOctalDigit() {
 byte[] buffer = new byte[]{'0', 'x','5','5','7'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test for parseOctal with null buffer
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalNullBuffer() {
 TarUtils.parseOctal(null, 0, 0);
 }

 // Test for parseOctal with negative offset
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalNegativeOffset() {
 byte[] buffer = new byte[]{'0', '7','5','5','7'};
 TarUtils.parseOctal(buffer, -1, buffer.length);
 }

 // Test for parseOctal with length less than the number of bytes to parse
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalLengthLessThanBytesToParse() {
 byte[] buffer = new byte[]{'0', '7','5','5','7', '0'};
 TarUtils.parseOctal(buffer, 0, 5);
 }

 // Test for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t','a','r','\0'};
 String expected = "tar";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test for parseName with null buffer
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameNullBuffer() {
 TarUtils.parseName(null, 0, 0);
 }

 // Test for parseName with negative offset
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameNegativeOffset() {
 byte[] buffer = new byte[]{'t','a','r','\0'};
 TarUtils.parseName(buffer, -1, buffer.length);
 }

 // Test for parseName with length less than the number of bytes to parse
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameLengthLessThanBytesToParse() {
 byte[] buffer = new byte[]{'t','a','r','\0'};
 TarUtils.parseName(buffer, 0, 3);
 }

 // Test for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "tar.gz";
 byte[] buffer = new byte[11];
 int offset = 0;
 int length = 11;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String result = new String(buffer, 0, length);
 assertEquals(name, result);
 }

 // Test for formatNameBytes with name longer than the buffer
 @Test
 public void testFormatNameBytesNameLongerThanBuffer() {
 String name = "tar.gz.backup";
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String result = new String(buffer, 0, length);
 assertEquals("tar.gz...", result);
 }

 // Test for formatNameBytes with null name
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesNullName() {
 TarUtils.formatNameBytes(null, new byte[10], 0, 10);
 }

 // Test for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "0113123";
 String result = new String(buffer, 0, length);
 assertEquals(expected, result);
 }

 // Test for formatUnsignedOctalString with value longer than the buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringValueLongerThanBuffer() {
 long value = 123456789;
 byte[] buffer = new byte[8];
 int offset = 0;
 int length = 8;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 }

 // Test for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 123;
 byte[] buffer = new byte[14];
 int offset = 0;
 int length = 14;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expected = "0113123 ";
 String result = new String(buffer, 0, length);
 assertEquals(expected, result);
 }

 // Test for formatOctalBytes with value longer than the buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesValueLongerThanBuffer() {
 long value = 123456789;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 }

 // Test for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x6e, (byte) 0x65, (byte) 0x78, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x2e, (byte) 0x67, (byte) 0x65, (byte) 0x74, (byte) 0x50, (byte) 0x0};
 long expected = 0x572151977227670L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }

 // Test for computeCheckSum with null buffer
 @Test(expected = IllegalArgumentException.class)
 public void testComputeCheckSumNullBuffer() {
 TarUtils.computeCheckSum(null);
 }

}