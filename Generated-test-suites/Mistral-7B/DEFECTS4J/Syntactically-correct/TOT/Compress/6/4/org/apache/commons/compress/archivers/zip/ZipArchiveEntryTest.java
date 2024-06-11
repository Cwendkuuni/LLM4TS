package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test class for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    /**
     * Test method for {@link ZipArchiveEntry#ZipArchiveEntry(String)} with valid name.
     */
    @Test
    public void testZipArchiveEntryWithValidName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test method for {@link ZipArchiveEntry#ZipArchiveEntry(java.util.zip.ZipEntry)} with valid ZipEntry.
     */
    @Test
    public void testZipArchiveEntryWithValidZipEntry() throws IOException {
        String name = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, entry.getName());
    }

    /**
     * Test method for {@link ZipArchiveEntry#ZipArchiveEntry(ZipArchiveEntry)} with valid ZipArchiveEntry.
     */
    @Test
    public void testZipArchiveEntryWithValidZipArchiveEntry() throws IOException {
        String name = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry original = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry entry = new ZipArchiveEntry(original);
        assertEquals(original.getName(), entry.getName());
    }

    /**
     * Test method for {@link ZipArchiveEntry#ZipArchiveEntry()} with empty name.
     */
    @Test
    public void testZipArchiveEntryWithEmptyName() {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertNull(entry.getName());
    }

    /**
     * Test method for {@link ZipArchiveEntry#ZipArchiveEntry(File, String)} with valid File and name.
     */
    @Test
    public void testZipArchiveEntryWithValidFileAndName() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test method for {@link ZipArchiveEntry#setUnixMode(int)} and {@link #getUnixMode()}.
     */
    @Test
    public void testSetAndGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    /**
     * Test method for {@link ZipArchiveEntry#getPlatform()}.
     */
    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    /**
     * Test method for {@link ZipArchiveEntry#setExtraFields(ZipExtraField[])} and {@link #getExtraFields()}.
     */
    @Test
    public void testSetAndGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField customField1 = new CustomExtraField("CustomField1", "Value1");
        ZipExtraField customField2 = new CustomExtraField("CustomField2", "Value2");

        ZipExtraField[] fields = {customField1, customField2};
        entry.setExtraFields(fields);

        ZipExtraField[] resultFields = entry.getExtraFields();

        assertEquals(2, resultFields.length);
        assertEquals(customField1, resultFields[0]);
        assertEquals(customField2, resultFields[1]);
    }

    /**
     * Test method for {@link ZipArchiveEntry#addExtraField(ZipExtraField)} and {@link #getExtraFields()}.
     */
    @Test
    public void testAddExtraFieldAndGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField customField1 = new CustomExtraField("CustomField1", "Value1");
        entry.addExtraField(customField1);

        ZipExtraField[] resultFields = entry.getExtraFields();

        assertEquals(1, resultFields.length);
        assertEquals(customField1, resultFields[0]);
    }

    /**
     * Test method for {@link ZipArchiveEntry#removeExtraField(ZipShort)} and {@link #getExtraFields()}.
     */
    @Test
    public void testRemoveExtraFieldAndGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField customField1 = new CustomExtraField("CustomField1", "Value1");
        entry.addExtraField(customField1);

        ZipShort customFieldId = new ZipShort((short) 0x0102);
        entry.removeExtraField(customFieldId);

        ZipExtraField[] resultFields = entry.getExtraFields();

        assertEquals(0, resultFields.length);
    }

    /**
     * Custom extra field for testing.
     */
    public static class CustomExtraField extends ZipExtraField {

        public CustomExtraField(String name, String value) {
            super(ZipShort.createShort(0x0102), name, value.getBytes());
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws IOException {
            // Empty implementation
        }

        @Override
        public void parseFromLocalFileData(byte[] data, int offset, int length) throws IOException {
            // Empty implementation
        }

    }

    /**
     * Test method for {@link ZipArchiveEntry#toString()}
     */
    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String expected = "ZipArchiveEntry{name='test.txt', size=0, compressedSize=0, method=(-1), internalAttributes=0, platform=PLATFORM_FAT, externalAttributes=0, extraFields={}, lastModifiedDate=Sun Jan 01 00:00:00 CET 1970, cryptography=null}";
        assertEquals(expected, entry.toString());
    }

    /**
     * Test method for {@link ZipArchiveEntry#clone()} and {@link #equals(Object)}
     */
    @Test
    public void testCloneAndEquals() throws IOException {
        String name = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry original = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry cloned = original.clone();

        Map<ZipShort, ZipExtraField> originalExtraFields = original.getExtraFieldsMap();
        Map<ZipShort, ZipExtraField> clonedExtraFields = cloned.getExtraFieldsMap();

        assertEquals(original, cloned);

        // Changing the original extra fields to check if cloned one is still equal
        originalExtraFields.put(new ZipShort(0x0102), new CustomExtraField("CustomField1", "Value1"));
        assertFalse(original.equals(cloned));

        // Changing the cloned extra fields to check if original is still equal to itself
        clonedExtraFields.put(new ZipShort(0x0102), new CustomExtraField("CustomField2", "Value2"));
        assertFalse(original.equals(cloned));
    }

}