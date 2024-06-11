import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("test.txt");
 String actual = ArchiveUtils.toString(entry);
 String expected = "-\t0\t test.txt";
 assertEquals(expected, actual);

 entry.setDirectory(true);
 actual = ArchiveUtils.toString(entry);
 expected = "d\t0\t test.txt";
 assertEquals(expected, actual);
 }

 @Test
 public void testMatchAsciiBuffer() {
 byte[] buffer1 = "test".getBytes();
 String expected = "test";
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length);
 assertTrue(actual);
 }

 @Test
 public void testMatchAsciiBufferFalse() {
 byte[] buffer1 = "test".getBytes();
 String expected = "test2";
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length);
 assertFalse(actual);
 }

 @Test
 public void testToAsciiBytes() {
 String inputString = "test";
 byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
 byte[] expected = "test".getBytes(CharsetNames.US_ASCII);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testToAsciiString() {
 byte[] buffer = "test".getBytes();
 String actual = ArchiveUtils.toAsciiString(buffer);
 String expected = "test";
 assertEquals(expected, actual);
 }

 @Test
 public void testIsEqual() {
 byte[] buffer1 = "test".getBytes();
 byte[] buffer2 = "test".getBytes();
 boolean actual = ArchiveUtils.isEqual(buffer1, buffer1.length, buffer2, 0, buffer2.length, false);
 assertTrue(actual);

 byte[] buffer3 = "test2".getBytes();
 actual = ArchiveUtils.isEqual(buffer1, buffer1.length, buffer2, 0, buffer2.length, false);
 assertFalse(actual);
 }

 @Test
 public void testIsEqualWithNull() {
 byte[] buffer1 = "test".getBytes();
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 1, buffer1.length);
 boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(actual);

 byte[] buffer3 = "test2".getBytes();
 actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
 assertFalse(actual);
 }

 @Test
 public void testIsArrayZero() {
 byte[] buffer = new byte[10];
 boolean actual = ArchiveUtils.isArrayZero(buffer, 5);
 assertFalse(actual);

 byte[] buffer2 = new byte[10];
 Arrays.fill(buffer2, (byte) 0);
 actual = ArchiveUtils.isArrayZero(buffer2, 10);
 assertTrue(actual);
 }

 @Test
 public void testSanitize() {
 String input = "test\u0001\u0002";
 String actual = ArchiveUtils.sanitize(input);
 String expected = "test\u0001\u0002...........";
 assertEquals(expected, actual);
 }
}