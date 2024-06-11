import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry archiveEntry = new TestArchiveEntry("testfile.txt", "testcontent".getBytes(), false, 10);
 String expectedOutput = "- 10 testfile.txt";
 String actualOutput = ArchiveUtils.toString(archiveEntry);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testMatchAsciiBufferMethod() {
 String expectedString = "teststring";
 byte[] buffer = expectedString.getBytes();
 assertTrue(ArchiveUtils.matchAsciiBuffer(expectedString, buffer));
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferMethodWithInvalidEncoding() {
 String expectedString = "teststring";
 byte[] buffer = new byte[10];
 try {
 ArchiveUtils.matchAsciiBuffer(expectedString, buffer);
 } catch (UnsupportedEncodingException e) {
 // expected, no need to assert
 }
 }

 @Test
 public void testMatchAsciiBufferMethodWithMismatchedBuffers() {
 byte[] buffer1 = "teststring1".getBytes();
 byte[] buffer2 = "teststring2".getBytes();
 assertFalse(ArchiveUtils.matchAsciiBuffer(new String(buffer1), buffer2));
 }

 @Test
 public void testToStringMethodWithNullEntry() {
 assertNull(ArchiveUtils.toString(null));
 }

 @Test
 public void testIsEqualMethodWithEqualBuffers() {
 byte[] buffer1 = "teststring1".getBytes();
 byte[] buffer2 = "teststring1".getBytes();
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqualMethodWithDifferentLengthBuffers() {
 byte[] buffer1 = "teststring1".getBytes();
 byte[] buffer2 = "teststring2".getBytes();
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqualMethodWithIgnoringTrailingNulls() {
 byte[] buffer1 = "teststring1\0".getBytes();
 byte[] buffer2 = "teststring1".getBytes();
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, true));
 }

 @Test
 public void testIsEqualMethodWithNullsInFirstBuffer() {
 byte[] buffer1 = "teststring1\0\0".getBytes();
 byte[] buffer2 = "teststring1".getBytes();
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqualMethodWithNullsInSecondBuffer() {
 byte[] buffer1 = "teststring1".getBytes();
 byte[] buffer2 = "teststring1\0\0".getBytes();
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqualMethodWithNullsInBothBuffers() {
 byte[] buffer1 = "teststring1\0\0".getBytes();
 byte[] buffer2 = "teststring1\0\0".getBytes();
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, 0, buffer2.length, true));
 }

 @Test
 public void testIsArrayZeroMethod() {
 byte[] array = new byte[10];
 assertTrue(ArchiveUtils.isArrayZero(array, 5));
 }

 @Test
 public void testIsArrayZeroMethodWithNonZeroValues() {
 byte[] array = new byte[10];
 array[5] = 42;
 assertFalse(ArchiveUtils.isArrayZero(array, 5));
 }

 @Test
 public void testSanitizeMethod() {
 String testString = "test\u0001string";
 String expectedOutput = "test????string";
 String actualOutput = ArchiveUtils.sanitize(testString);
 assertEquals(expectedOutput, actualOutput);
 }

}