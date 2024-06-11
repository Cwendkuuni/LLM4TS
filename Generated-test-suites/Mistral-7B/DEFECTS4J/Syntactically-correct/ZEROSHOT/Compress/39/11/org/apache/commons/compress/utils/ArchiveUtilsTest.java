import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("test.txt");
 String result = ArchiveUtils.toString(entry);
 String expected = "- 0 test.txt";
 assertEquals(expected, result);

 entry.setDirectory(true);
 result = ArchiveUtils.toString(entry);
 expected = "d 0 test.txt";
 assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBuffer() {
 String expected = "test";
 byte[] buffer1 = toAsciiBytes(expected);
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 3);
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2));

 expected += "test";
 buffer2 = Arrays.copyOfRange(buffer1, 0, 7);
 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
 }

 @Test
 public void testIsEqual() {
 byte[] buffer1 = new byte[]{'T', 'e', 's', 't', 0};
 byte[] buffer2 = new byte[]{'T', 'e', 's', 't', 0};
 byte[] buffer3 = new byte[]{'T', 'e', 's', 't', 't'};
 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));

 buffer1 = new byte[]{'T', 'e', 's', 't', 0};
 buffer2 = new byte[]{'T', 'e', 's', 't'};
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

 buffer1 = new byte[]{'T', 'e', 's', 't', 0};
 buffer2 = new byte[]{'T', 'e', 's', 't', 0};
 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
 }

 @Test
 public void testIsEqualWithNull() {
 byte[] buffer1 = new byte[]{'T', 'e', 's', 't', 0};
 byte[] buffer2 = new byte[]{'T', 'e', 's', 't', 0, 0, 0};
 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

 buffer1 = new byte[]{'T', 'e', 's', 't', 0};
 buffer2 = new byte[]{'T', 'e', 's', 't', 0, 0, 0, 0};
 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

 buffer1 = new byte[]{'T', 'e', 's', 't', 0, 0};
 buffer2 = new byte[]{'T', 'e', 's', 't', 0};
 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 }

 @Test
 public void testisArrayZero() {
 byte[] a = new byte[10];
 assertFalse(ArchiveUtils.isArrayZero(a, 0));

 byte[] b = new byte[10];
 Arrays.fill(b, (byte) 0);
 assertTrue(ArchiveUtils.isArrayZero(b, 0));
 assertTrue(ArchiveUtils.isArrayZero(b, 0, b.length));
 }

 @Test
 public void testSanitize() {
 String str1 = "Test\u0001String with ? and ¥";
 String str2 = ArchiveUtils.sanitize(str1);
 String expected = "Test???????????????????????????String with ? and ¥";
 assertEquals(expected, str2);
 }
}