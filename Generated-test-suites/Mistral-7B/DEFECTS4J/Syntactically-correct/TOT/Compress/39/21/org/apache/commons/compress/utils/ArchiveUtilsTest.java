import org.apache.commons.compress.archivers.test.TestUtils;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = TestUtils.createTestEntry("test.txt", true, 1024);
 String result = ArchiveUtils.toString(entry);
 String expected = "d 1024 test.txt";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBuffer() {
 String expected = "test";
 byte[] buffer = toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 Assert.assertTrue(result);
 }

 @Test
 public void testMatchAsciiBuffer_OffsetAndLength() {
 String expected = "test";
 byte[] buffer = toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 1, 3);
 Assert.assertTrue(result);
 }

 @Test
 public void testMatchAsciiBuffer_Mismatch() {
 String expected = "test";
 byte[] buffer = toAsciiBytes("test1".getBytes());
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 Assert.assertFalse(result);
 }

 @Test
 public void testIsEqual() {
 byte[] buffer1 = new byte[]{'a', 'b', 'c'};
 byte[] buffer2 = new byte[]{'a', 'b', 'c'};
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsEqual_DifferentLength() {
 byte[] buffer1 = new byte[]{'a', 'b', 'c'};
 byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd'};
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 Assert.assertFalse(result);
 }

 @Test
 public void testIsEqual_IgnoreTraillingNulls() {
 byte[] buffer1 = new byte[]{'a', 'b', 'c', 0, 0};
 byte[] buffer2 = new byte[]{'a', 'b', 'c', 0};
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsEqual_IgnoreTraillingNulls_DifferentArrays() {
 byte[] buffer1 = new byte[]{'a', 'b', 'c', 0, 0};
 byte[] buffer2 = new byte[]{'a', 'b', 'c'};
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsEqual_IgnoreTraillingNulls_Mismatch() {
 byte[] buffer1 = new byte[]{'a', 'b', 'c', 0, 0};
 byte[] buffer2 = new byte[]{'a', 'b', 'd'};
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
 Assert.assertFalse(result);
 }

 @Test
 public void testArrayZero() {
 byte[] buffer = new byte[10];
 boolean result = ArchiveUtils.isArrayZero(buffer, 5);
 Assert.assertTrue(result);
 }

 @Test
 public void testArrayZero_PartiallyZero() {
 byte[] buffer = new byte[10];
 Arrays.fill(buffer, 5, 10, (byte) 1);
 boolean result = ArchiveUtils.isArrayZero(buffer, 5);
 Assert.assertFalse(result);
 }

 @Test
 public void testSanitize() {
 String input = "\"\u0000\u0001\n\u007Ftest\u007F\u000B\u0002\u0003\u0004";
 String output = ArchiveUtils.sanitize(input);
 String expected = "????test???";
 Assert.assertEquals(expected, output);
 }

}