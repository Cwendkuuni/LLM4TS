import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '7', '7', '5', '0'};
 int offset = 0;
 int length = 5;
 long expectedValue = 017750L;
 long actualValue = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParseOctal_TrailingNull() {
 byte[] buffer = new byte[]{'0', '7', '7', '5'};
 int offset = 0;
 int length = 4;
 long expectedValue = 01775L;
 long actualValue = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParseOctal_InvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '7', '5', '8'}; // byte '8' is not valid in octal
 int offset = 0;
 int length = 5;

 assertThrows(IllegalArgumentException.class, () -> {
 TarUtils.parseOctal(buffer, offset, length);
 });
 }

 @Test
 public void testParseOctal_MissingTrailingNull() {
 byte[] buffer = new byte[]{'0', '7', '7', '5'};
 int offset = 0;
 int length = 4;

 assertThrows(IllegalArgumentException.class, () -> {
 TarUtils.parseOctal(buffer, offset, length);
 });
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'r', (byte) 'o', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't', (byte) 's', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) 'a', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 int offset = 0;
 int length = buffer.length;
 String expectedName = "tar.file.test.compresses.zip.data.tar.gz";
 String actualName = TarUtils.parseName(buffer, offset, length);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "tar.file.test.compresses.zip.data.tar.gz";
 byte[] buffer = new byte[512];
 int offset = 0;
 int length = name.length();
 int actualOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
 byte[] expectedBuffer = new byte[]{'t', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.', 't', '.', 't', 'e', 's', 't', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', '.', 'z', 'i', 'p', '.', 'p', 'r', 'o', 'j', 'e', 'c', 't', 's', '.', 'd', 'a', 't', 'a', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 for (int i = 0; i < length; i++) {
 assertEquals(expectedBuffer[i], buffer[i + offset - length]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 123456789L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 byte[] expectedBuffer = new byte[]{'0', '1', '0', '1', '1', '1', '1', '1', '1', '1'};
 for (int i = 0; i < length; i++) {
 assertEquals(expectedBuffer[i], buffer[i + offset]);
 }
 }

 @Test
 public void testFormatOctalBytes_ValidInput() {
 long value = 123456789L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 byte[] expectedBuffer = new byte[]{'0', '1', '0', '1', '1', '1', '1', '1', '1', '1', ' ', '0'};
 for (int i = 0; i < length; i++) {
 assertEquals(expectedBuffer[i], buffer[i + offset]);
 }
 }

 @Test
 public void testComputeCheckSum_ValidInput() {
 byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.', 't', '.', 't', 'e', 's', 't', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', '.', 'z', 'i', 'p', '.', 'p', 'r', 'o', 'j', 'e', 'c', 't', 's', '.', 'd', 'a', 't', 'a', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 long checksum = TarUtils.computeCheckSum(buffer);
 long expectedChecksum = 0x586e9541c1b5edfL;
 assertEquals(expectedChecksum, checksum);
 }

 @Test
 public void testFormatCheckSumOctalBytes_ValidInput() {
 long value = 123456789L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 byte[] expectedBuffer = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', ' '};
 for (int i = 0; i < length; i++) {
 assertEquals(expectedBuffer[i], buffer[i + offset]);
 }
 }
}