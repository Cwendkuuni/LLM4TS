import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for ZipArchiveEntry class.
 */
public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", zipEntry.getName());
 }

 @Test
 public void testConstructorWithZipEntry() throws ZipException {
 ZipEntry zipEntry = new ZipEntry("test.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testConstructorWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(zipArchiveEntry1);
 assertEquals("test.txt", zipArchiveEntry2.getName());
 assertNotSame(zipArchiveEntry1, zipArchiveEntry2);
 }

 @Test
 public void testConstructorWithFileAndEntryName() {
 File testFile = new File("test.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testFile, "test.txt");
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setUnixMode(0755);
 assertEquals(0755, zipArchiveEntry.getUnixMode());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
 }

 @Test
 public void testSetExtraFieldsWithParseableData() {
 byte[] extraData = TestUtils.getTestResourceAsBytes("ZipExtraFields/general_purpose_byte.bin");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExtra(extraData);
 ZipExtraField[] expectedExtraFields = ExtraFieldUtils.parse(extraData, true);
 ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
 assertArrayEquals(expectedExtraFields, actualExtraFields);
 }

 @Test(expected = RuntimeException.class)
 public void testSetExtraWithInvalidData() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 byte[] invalidData = {(byte) 0x01};
 zipArchiveEntry.setExtra(invalidData);
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
 }

 @Test
 public void testGetLocalFileDataExtra() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 byte[] extraData = TestUtils.getTestResourceAsBytes("ZipExtraFields/general_purpose_byte.bin");
 zipArchiveEntry.setExtra(extraData);
 byte[] actualExtraData = zipArchiveEntry.getLocalFileDataExtra();
 assertArrayEquals(extraData, actualExtraData);
 }

 @Test
 public void testGetCentralDirectoryExtra() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 byte[] extraData = TestUtils.getTestResourceAsBytes("ZipExtraFields/general_purpose_byte.bin");
 zipArchiveEntry.setCentralDirectoryExtra(extraData);
 byte[] actualExtraData = zipArchiveEntry.getCentralDirectoryExtra();
 assertArrayEquals(extraData, actualExtraData);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
 assertNotSame(original, clone);
 assertEquals(original.getName(), clone.getName());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
 }

 @Test
 public void testNotSupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(255); // Unsupported method
 assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setInternalAttributes(0);
 assertEquals(0, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExternalAttributes(1);
 assertEquals(1, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField(ZipShort.CRC, TestUtils.getTestResourceAsBytes("ZipExtraFields/text.bin"));
 zipArchiveEntry.addExtraField(extraField);
 ZipExtraField actualExtraField = zipArchiveEntry.getExtraField(ZipShort.CRC);
 assertEquals(extraField, actualExtraField);
 }

 @Test
 public void testAddAsFirstExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField(ZipShort.CRC, TestUtils.getTestResourceAsBytes("ZipExtraFields/text.bin"));
 zipArchiveEntry.addAsFirstExtraField(extraField);
 ZipExtraField actualExtraField = zipArchiveEntry.getExtraField(ZipShort.CRC);
 assertEquals(extraField, actualExtraField);
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipShort headerId = ZipShort.COMMENT;
 ZipExtraField extraField = new ZipExtraField(headerId, TestUtils.getTestResourceAsBytes("ZipExtraFields/text.bin"));
 zipArchiveEntry.addExtraField(extraField);
 zipArchiveEntry.removeExtraField(headerId);
 assertNull(zipArchiveEntry.getExtraField(headerId));
 }
}