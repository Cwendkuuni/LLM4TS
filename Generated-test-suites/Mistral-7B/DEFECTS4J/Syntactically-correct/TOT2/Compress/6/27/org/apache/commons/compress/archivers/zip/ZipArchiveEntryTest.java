import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithStringArg() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertNotNull(entry);
 Assertions.assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithZipEntryArg() {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 Assertions.assertNotNull(entry);
 Assertions.assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithFileArg() throws Exception {
 File inputFile = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
 Assertions.assertNotNull(entry);
 Assertions.assertEquals("test.txt", entry.getName());
 Assertions.assertEquals(inputFile.length(), entry.getSize());
 Assertions.assertEquals(inputFile.lastModified(), entry.getTime(), 0L);
 }

 @Test
 public void testCloneMethod() throws Exception {
 ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
 Assertions.assertNotSame(originalEntry, clonedEntry);
 Assertions.assertEquals(originalEntry.getName(), clonedEntry.getName());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertEquals(-1, entry.getMethod());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipArchiveEntry.DEFLATED);
 Assertions.assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertEquals(0, entry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP);
 Assertions.assertEquals(ZipEntry.FLAG_UNIX_GROUP, entry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertEquals(0, entry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExternalAttributes(0x0001);
 Assertions.assertEquals(0x0001, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0644);
 Assertions.assertEquals(0644, entry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assertions.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 Assertions.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField", new byte[10]);
 entry.setExtraFields(new ZipExtraField[] { extraField });
 Assertions.assertEquals(1, entry.getExtraFields().length);
 }

 @Test
 public void testGetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField",new byte[10]);
 entry.setExtraFields(new ZipExtraField[] { extraField });
 ZipExtraField[] extraFields = entry.getExtraFields();
 Assertions.assertEquals(1, extraFields.length);
 Assertions.assertEquals("TestExtraField", extraFields[0].getName());
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField", new byte[10]);
 entry.addExtraField(extraField);
 ZipExtraField[] extraFields = entry.getExtraFields();
 Assertions.assertEquals(1, extraFields.length);
 Assertions.assertEquals("TestExtraField", extraFields[0].getName());
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField", new byte[10]);
 entry.addExtraField(extraField);
 entry.removeExtraField(new ZipShort(3));
 ZipExtraField[] extraFields = entry.getExtraFields();
 Assertions.assertNull(extraFields[0]);
 }

 @Test
 public void testGetExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField", new byte[10]);
 entry.addExtraField(extraField);
 ZipExtraField field = entry.getExtraField(new ZipShort(3));
 Assertions.assertEquals(extraField, field);
 }

 @Test
 public void testSetExtraData() throws Exception {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[10];
 entry.setExtra(extraData);
 }

 @Test(expected = RuntimeException.class)
 public void testSetExtraDataWithInvalidFormat() throws Exception {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[1];
 entry.setExtra(extraData);
 }
}