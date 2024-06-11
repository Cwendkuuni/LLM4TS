import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithZipEntry() throws ZipException {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry newEntry = new ZipArchiveEntry(originalEntry);
 assertEquals(originalEntry.getName(), newEntry.getName());
 }

 @Test
 public void testEmptyConstructor() {
 ZipArchiveEntry entry = new ZipArchiveEntry();
 assertNull(entry.getName());
 }

 @Test
 public void testConstructorWithFile() {
 File inputFile = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
 assertEquals("test.txt", entry.getName());
 assertEquals(inputFile.length(), entry.getSize());
 assertEquals(inputFile.lastModified(), entry.getTime());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(-1, entry.getMethod());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMethodNegativeNumber() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(-1);
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0, entry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(0x01);
 assertEquals(0x01, entry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0L, entry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExternalAttributes(0x12345678L);
 assertEquals(0x12345678L, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("Test Extra Field", (short) 0x01);
 entry.setExtraFields(new ZipExtraField[] { extraField });
 assertEquals(1, entry.getExtraFields().length);
 assertEquals(extraField, entry.getExtraField((short) 0x01));
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("Test Extra Field", (short) 0x01);
 entry.addExtraField(extraField);
 assertEquals(1, entry.getExtraFields().length);
 assertEquals(extraField, entry.getExtraField((short) 0x01));
 }

 @Test
 public void testAddAsFirstExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("Test Extra Field", (short) 0x01);
 entry.addAsFirstExtraField(extraField);
 assertEquals(1, entry.getExtraFields().length);
 assertEquals(extraField, entry.getExtraField((short) 0x01));
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("Test Extra Field", (short) 0x01);
 entry.addExtraField(extraField);
 entry.removeExtraField((short) 0x01);
 assertEquals(0, entry.getExtraFields().length);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testGetExtraFieldNull() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(null, entry.getExtraField((short) 0x01));
 }
}