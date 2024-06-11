import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] testBuffer = new byte[]{'0', '7', '4', '4', '7', '5', '5', '5'};
 long expectedResult = 017447555L;
 long actualResult = TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
 assertEquals(expectedResult, actualResult);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] testBuffer = new byte[]{'0', '7', '4', '4', '7', '5', 'X', '5'};
 TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
 }

 // Test case for parseOctal with buffer length reached
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalBufferLengthReached() {
 byte[] testBuffer = new byte[]{'0', '7', '4', '4'};
 TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] testBuffer = new byte[]{'t', 'a', 'r', '.', 'a', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r'};
 String expectedResult = "tar.archive.tar";
 String actualResult = TarUtils.parseName(testBuffer, 0, testBuffer.length);
 assertEquals(expectedResult, actualResult);
 }

 // Test case for parseName with NUL found
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameNulFound() {
 byte[] testBuffer = new byte[]{'t', 'a', 'r', '.', 'a', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', (byte) 0};
 TarUtils.parseName(testBuffer, 0, testBuffer.length);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String testName = "testName.tar.gz";
 byte[] expectedBuffer = new byte[]{'t', 'e', 's', 't', 'N', 'a', 'm', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
 byte[] actualBuffer = new byte[expectedBuffer.length];
 int offset = TarUtils.formatNameBytes(testName, actualBuffer, 0, expectedBuffer.length);
 assertArrayEquals(expectedBuffer, actualBuffer);
 assertEquals(offset, expectedBuffer.length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long testValue = 01234567L;
 byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
 byte[] actualBuffer = new byte[expectedBuffer.length];
 TarUtils.formatUnsignedOctalString(testValue, actualBuffer, 0, expectedBuffer.length);
 assertArrayEquals(expectedBuffer, actualBuffer);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long testValue = 01234567L;
 byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
 byte[] actualBuffer = new byte[expectedBuffer.length];
 TarUtils.formatOctalBytes(testValue, actualBuffer, 0, expectedBuffer.length);
 assertArrayEquals(expectedBuffer, actualBuffer);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] testBuffer = new byte[]{'o', 'p', 't', 'i', 'o', 'n'};
 long expectedCheckSum = 292 * 256 + 191 + 198 + 155 + 196 + 112;
 long actualCheckSum = TarUtils.computeCheckSum(testBuffer);
 assertEquals(expectedCheckSum, actualCheckSum);
 }

}