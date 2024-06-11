import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod(){
 ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
 ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");

 assertEquals("- 2000 main.c\n d 100 testfiles\n", ArchiveUtils.toString(archiveEntry));
 assertEquals("d 100 testfiles\n", ArchiveUtils.toString(directoryEntry));
 }

 @Test
 public void testMatchAsciiBufferSimpleCase(){
 String expected = "Hello World";
 byte[] buffer = toAsciiBytes(expected);

 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @Test
 public void testMatchAsciiBufferNullScenario(){
 String expected = null;
 byte[] buffer = toAsciiBytes("Hello World");

 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @Test
 public void testMatchAsciiBufferSizeMismatchScenario(){
 String expected = "Hello World";
 byte[] buffer = new byte[10];

 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @Test
 public void testMatchAsciiBufferOffsetScenario(){
 String expected = "Hello World";
 byte[] buffer = toAsciiBytes(expected);
 int offset = 5;

 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, buffer.length));
 }

 @Test
 public void testToStringMethodEdgeCase(){
 ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
 archiveEntry.setSize(-1);

 // Test for negative size
 assertEquals("d  ?????????????????  main.c\n", ArchiveUtils.toString(archiveEntry));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testToAsciiBytesNullScenario(){
 String inputString = null;

 ArchiveUtils.toAsciiBytes(inputString);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testToAsciiBytesSpecialCharacterScenario(){
 String inputString = "\u0000";

 ArchiveUtils.toAsciiBytes(inputString);
 }

 @Test
 public void testIsEqualSimpleCase(){
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = toAsciiBytes("Hello World".getBytes());

 assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 @Test
 public void testIsEqualSizeMismatchScenario(){
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = new byte[10];

 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 @Test
 public void testIsEqualOffsetScenario(){
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = new byte[10];
 int offset = 5;

 assertFalse(ArchiveUtils.isEqual(buffer1, offset, buffer1.length, buffer2, 0, buffer2.length));
 }

 @Test
 public void testIsEqualTrailingNullsScenario(){
 byte[] buffer1 = toAsciiBytes("Hello World\0".getBytes());
 byte[] buffer2 = toAsciiBytes("Hello World".getBytes());

 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 }

 @Test
 public void testIsEqualTrailingNullsOtherScenario(){
 byte[] buffer1 = toAsciiBytes("Hello World\0".getBytes());
 byte[] buffer2 = new byte[10];

 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 }

 @Test
 public void testSanitizeSimpleCase(){
 String s = "Hello World";

 assertEquals("Hello World", ArchiveUtils.sanitize(s));
 }

 @Test
 public void testSanitizeSpecialCharactersCase(){
 String s = "Hello \u0000 World!";

 assertEquals("Hello ? World!", ArchiveUtils.sanitize(s));
 }

 @Test
 public void testSanitizeLongStringCase(){
 String s = "Hello World".repeat(255);

 assertEquals(s.substring(0, 254) + "?", ArchiveUtils.sanitize(s));
 }

 @Test
 public void testSanitizeEmptyStringCase(){
 String s = "";

 assertEquals("", ArchiveUtils.sanitize(s));
 }

 @Test
 public void testSanitizeNullStringCase(){
 String s = null;

 assertEquals("", ArchiveUtils.sanitize(s));
 }
}