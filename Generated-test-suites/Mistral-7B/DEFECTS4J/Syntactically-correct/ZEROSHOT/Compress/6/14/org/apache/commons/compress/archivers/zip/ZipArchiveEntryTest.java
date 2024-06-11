import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testCreationWithName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testCreationWithZipEntry() throws ZipException {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testCreationWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry entry1 = new ZipArchiveEntry();
 ZipArchiveEntry entry2 = new ZipArchiveEntry(entry1);
 assertEquals(entry1, entry2);
 }

 @Test
 public void testCreationWithFile() {
 File file = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertTrue(entry.isSupportedCompressionMethod());
 entry.setMethod(ZipEntry.STORED);
 assertTrue(entry.isSupportedCompressionMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertTrue(entry.isSupportedCompressionMethod());
 entry.setMethod(-1);
 assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(-1);
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0, entry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(0xFFFF);
 assertEquals(0xFFFF, entry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0, entry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExternalAttributes(0xFFFFFFFFL);
 assertEquals(0xFFFFFFFFL, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testGetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(0, entry.getUnixMode());
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
 assertEquals(0, entry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtendedTimestamps();
 entry.setExtraFields(new ZipExtraField[] { extraField });
 assertEquals(1, entry.getExtraFields().length);
 assertEquals(extraField, entry.getExtraField(extraField.getHeaderId()));
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField1 = new ZipExtendedTimestamps();
 ZipExtraField extraField2 = new ZipExtendedTimestamps();
 entry.addExtraField(extraField1);
 entry.addExtraField(extraField2);
 assertEquals(2, entry.getExtraFields().length);
 assertEquals(extraField1, entry.getExtraField(extraField1.getHeaderId()));
 assertEquals(extraField2, entry.getExtraField(extraField2.getHeaderId()));
 }

 @Test
 public void testAddAsFirstExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField1 = new ZipExtendedTimestamps();
 ZipExtraField extraField2 = new ZipExtendedTimestamps();
 entry.addAsFirstExtraField(extraField1);
 assertEquals(1, entry.getExtraFields().length);
 assertEquals(extraField1, entry.getExtraField(extraField1.getHeaderId()));
 entry.addExtraField(extraField2);
 assertEquals(2, entry.getExtraFields().length);
 assertEquals(extraField2, entry.getExtraField(extraField2.getHeaderId()));
 assertEquals(extraField1, entry.getExtraField(extraField1.getHeaderId()));
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtendedTimestamps();
 entry.addExtraField(extraField);
 assertEquals(1, entry.getExtraFields().length);
 entry.removeExtraField(extraField.getHeaderId());
 assertEquals(0, entry.getExtraFields().length);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetExtraFieldNull() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(null, entry.getExtraField(new ZipShort(0x1234)));
 }

 @Test
 public void testSetExtra() {
 byte[] extraData = new byte[1024];
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField[] extraFields = { new ZipExtendedTimestamps() };
 entry.setExtra(extraData);
 entry.setExtraFields(extraFields);
 }

 @Test
 public void testSetCentralDirectoryExtra() {
 byte[] centralDirectoryExtraData = new byte[1024];
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField[] centralDirectoryExtraFields = { new ZipExtendedTimestamps() };
 entry.setCentralDirectoryExtra(centralDirectoryExtraData);
 entry.setExtraFields(centralDirectoryExtraFields);
 }

 @Test
 public void testGetLocalFileDataExtra() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] localFileDataExtra = entry.getLocalFileDataExtra();
 assertNotNull(localFileDataExtra);
 }

 @Test
 public void testGetCentralDirectoryExtra() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
 assertNotNull(centralDirectoryExtra);
 }

 @Test
 public void testEquals() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
 assertTrue(entry1.equals(entry1));
 assertTrue(entry1.equals(entry2));
 assertFalse(entry1.equals(entry3));
 }

 @Test
 public void testHashCode() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
 int hashCode1 = entry1.hashCode();
 int hashCode2 = entry2.hashCode();
 int hashCode3 = entry3.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }
}