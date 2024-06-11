import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveEntryTest {

 @Before
 public void setUp() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveEntry defaultEntry = (ZipArchiveEntry) zipFile.getEntry("test.txt");
 ZipArchiveEntry newEntry = new ZipArchiveEntry("newEntry.txt");
 newEntry.setSize(10);
 newEntry.setTime(System.currentTimeMillis());
 zipFile.putNextEntry(newEntry);
 zipFile.writeBytes("Hello World!".getBytes(), 0, "Hello World!".length());
 zipFile.closeEntry();
 zipFile.close();
 }

 @Test
 public void testCreateEntry() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertEquals("test.txt", entry.getName());
 Assert.assertFalse(entry.isDirectory());
 Assert.assertEquals(-1, entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testCreateEntryFromZipEntry() throws Exception {
 ZipEntry zipEntry = new ZipFile("test.zip").getEntry("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 Assert.assertEquals("test.txt", entry.getName());
 Assert.assertFalse(entry.isDirectory());
 Assert.assertEquals(-1, entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testCreateEntryFromZipArchiveEntry() throws Exception {
 ZipArchiveEntry entry = new ZipArchiveEntry(new ZipArchiveEntry("test.txt"));
 Assert.assertEquals("test.txt", entry.getName());
 Assert.assertFalse(entry.isDirectory());
 Assert.assertEquals(-1, entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testCreateDirectoryEntry() {
 ZipArchiveEntry entry = new ZipArchiveEntry("/testDir");
 Assert.assertEquals("/testDir", entry.getName());
 Assert.assertTrue(entry.isDirectory());
 Assert.assertEquals(-1, entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testSetSize() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setSize(10);
 Assert.assertEquals(10, entry.getSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeSize() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setSize(-1);
 }

 @Test
 public void testSetTime() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 long time = System.currentTimeMillis();
 entry.setTime(time);
 Assert.assertEquals(time, entry.getTime());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 int mode = 0644;
 entry.setUnixMode(mode);
 Assert.assertEquals(mode, entry.getUnixMode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUnixModeWithInvalidMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 int mode = 0655;
 entry.setUnixMode(mode);
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPlatformWithInvalidPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(5);
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new GenericZipExtraField("Test", "Test Value");
 entry.setExtraFields(new ZipExtraField[] { extraField });
 Assert.assertNotNull(entry.getExtraFields());
 Assert.assertEquals(1, entry.getExtraFields().length);
 Assert.assertEquals("Test Value", entry.getExtraField(new ZipShort(0)).getValue());
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new GenericZipExtraField("Test", "Test Value");
 entry.addExtraField(extraField);
 Assert.assertNotNull(entry.getExtraFields());
 Assert.assertEquals(1, entry.getExtraFields().length);
 Assert.assertEquals("Test Value", entry.getExtraField(new ZipShort(0)).getValue());
 }

 @Test
 public void testAddAsFirstExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new GenericZipExtraField("Test", "Test Value");
 entry.addAsFirstExtraField(extraField);
 Assert.assertNotNull(entry.getExtraFields());
 Assert.assertEquals(1, entry.getExtraFields().length);
 Assert.assertEquals("Test Value", entry.getExtraField(new ZipShort(0)).getValue());
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new GenericZipExtraField("Test", "Test Value");
 entry.addExtraField(extraField);
 entry.removeExtraField(new ZipShort(0));
 Assert.assertNull(entry.getExtraFields());
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testRemoveNonExistentExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.removeExtraField(new ZipShort(0));
 }
}