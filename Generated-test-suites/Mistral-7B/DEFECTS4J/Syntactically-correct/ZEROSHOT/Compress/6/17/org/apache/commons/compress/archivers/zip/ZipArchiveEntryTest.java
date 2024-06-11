import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals("entryName.txt", zipArchiveEntry.getName());
        assertNull(zipArchiveEntry.getMethod());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("entryName.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("entryName.txt", zipArchiveEntry.getName());
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() throws ZipException, CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("entryName.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getInternalAttributes(), clone.getInternalAttributes());
        assertEquals(original.getPlatform(), clone.getPlatform());
        assertEquals(original.getExternalAttributes(), clone.getExternalAttributes());
        assertEquals(original.getExtraFields(), clone.getExtraFields());
        assertEquals(original.isDirectory(), clone.isDirectory());
    }

    @Test
    public void testZipArchiveEntryConstructorWithFile() {
        File inputFile = new File("testFile.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "entryName.txt");
        assertEquals("entryName.txt", zipArchiveEntry.getName());
        assertNull(zipArchiveEntry.getMethod());
        assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(-1);
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(100);
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertNull(zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertNull(zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntr = new ZipArchiveEntry("entryName.txt");
        assertEquals(0, zipArchiveEntr.getInternalAttributes());

        zipArchiveEntr.setInternalAttributes(0x01);
        assertEquals(0x01, zipArchiveEntr.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntr = new ZipArchiveEntry("entryName.txt");
        assertEquals(0, zipArchiveEntr.getInternalAttributes());

        zipArchiveEntr.setInternalAttributes(0x01);
        assertEquals(0x01, zipArchiveEntr.getInternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(0L, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(1L);
        assertEquals(1L, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(0L, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(1L);
        assertEquals(1L, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        assertEquals(0755, zipArchiveEntry.getUnixMode());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        assertEquals(0, zipArchiveEntry.getUnixMode());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFieldsEmpty() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        byte[] extra = new byte[0];
        zipArchiveEntry.setExtra(extra);
        assertNull(zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testSetExtraFieldsNonNull() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {new ZipDecimalNumberField("Test", new BigDecimal("123.45"))};
        zipArchiveEntry.setExtraFields(extraFields);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField extraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.COMMENT_NAME));
        assertEquals("Test", extraField.getName());
        assertEquals(new BigDecimal("123.45"), extraField.getValue());
    }

    @Test
    public void testAddExtraField() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {new ZipUnicodeExtensionField("Test", new String("1234567890"))};
        zipArchiveEntry.setExtraFields(extraFields);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.UNIX_NAME));
        assertNull(existingExtraField);

        ZipExtraField newExtraField = new ZipUnicodeExtensionField("NewTest", new String("9876543210"));
        zipArchiveEntry.addExtraField(newExtraField);
        assertEquals(2, zipArchiveEntry.getExtraFields().length);
        existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.UNIX_NAME));
        assertNull(existingExtraField);
        existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.UNIX_NAME_UTF8));
        assertEquals("NewTest", existingExtraField.getName());
        assertEquals(new String("9876543210"), existingExtraField.getValue());
    }

    @Test
    public void testAddAsFirstExtraField() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {new ZipUnicodeExtensionField("Test", new String("1234567890")), new ZipUnicodeExtensionField("Test2", new String("8765432109"))};
        zipArchiveEntry.setExtraFields(extraFields);
        assertEquals(2, zipArchiveEntry.getExtraFields().length);
        ZipExtraField existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.UNIX_NAME_UTF8));
        assertEquals("Test2", existingExtraField.getName());
        assertEquals(new String("8765432109"), existingExtraField.getValue());

        ZipExtraField newExtraField = new ZipUnicodeExtensionField("NewTest", new String("9876543210"));
        zipArchiveEntry.addAsFirstExtraField(newExtraField);
        assertEquals(3, zipArchiveEntry.getExtraFields().length);
        existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.UNIX_NAME_UTF8));
        assertEquals("NewTest", existingExtraField.getName());
        assertEquals(new String("9876543210"), existingExtraField.getValue());
    }

    @Test
    public void testRemoveExtraField() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {new ZipUnicodeExtensionField("Test", new String("1234567890")), new ZipUnicodeExtensionField("Test2", new String("8765432109"))};
        zipArchiveEntry.setExtraFields(extraFields);
        assertEquals(2, zipArchiveEntry.getExtraFields().length);

        zipArchiveEntry.removeExtraField(new ZipShort(ZipShort.UNIX_NAME_UTF8));
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField existingExtraField = zipArchiveEntry.getExtraField(new ZipShort(ZipShort.COMMENT_NAME));
        assertNull(existingExtraField);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        zipArchiveEntry.getExtraField(new ZipShort(ZipShort.COMMENT_NAME));
        fail();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldByInvalidHeaderId() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {new ZipUnicodeExtensionField("Test", new String("1234567890"))};
        zipArchiveEntry.setExtraFields(extraFields);
        zipArchiveEntry.getExtraField(new ZipShort(Short.MIN_VALUE));
        fail();
    }
}