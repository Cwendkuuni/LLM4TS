package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tests for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    private ZipArchiveEntry entry;
    private ZipExtraField testExtraField;

    /**
     * Sets up a new ZipArchiveEntry instance for testing.
     */
    @BeforeEach
    public void setUp() {
        entry = new ZipArchiveEntry("test.txt");
        testExtraField = new ZipExtraField(ZipShort.LOCAL_FILE_HEADER, "Test Extra Field");
    }

    /**
     * Tests constructor from ZipEntry.
     *
     * @throws IOException on error
     */
    @Test
    public void testFromZipEntry() throws IOException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        Assertions.assertEquals("test.txt", entry.getName());
        Assertions.assertNull(entry.getExtraFields());
        Assertions.assertEquals(-1, entry.getMethod());
    }

    /**
     * Tests constructor from another ZipArchiveEntry.
     */
    @Test
    public void testFromAnotherZipArchiveEntry() {
        ZipArchiveEntry entryToClone = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = new ZipArchiveEntry(entryToClone);
        Assertions.assertEquals("test.txt", clonedEntry.getName());
        Assertions.assertArrayEquals(entryToClone.getExtra(), clonedEntry.getExtra());
        Assertions.assertEquals(entryToClone.getMethod(), clonedEntry.getMethod());
        Assertions.assertEquals(entryToClone.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assertions.assertEquals(entryToClone.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assertions.assertEquals(entryToClone.getExtraFields(), clonedEntry.getExtraFields());
    }

    /**
     * Tests constructor from a File.
     */
    @Test
    public void testFromFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        Assertions.assertEquals("test.txt", entry.getName());
        Assertions.assertEquals(inputFile.length(), entry.getSize());
        Assertions.assertEquals(inputFile.lastModified(), entry.getTime());
    }

    /**
     * Tests getters and setters for internal attributes.
     */
    @Test
    public void testInternalAttributes() {
        entry.setInternalAttributes(ZipEntry.FLAG_DELETED | ZipEntry.FLAG_UNCHANGED);
        int internalAttributes = entry.getInternalAttributes();
        Assertions.assertEquals(ZipEntry.FLAG_DELETED | ZipEntry.FLAG_UNCHANGED, internalAttributes);
        entry.setInternalAttributes(0);
    }

    /**
     * Tests getters and setters for external attributes.
     */
    @Test
    public void testExternalAttributes() {
        entry.setExternalAttributes(0x01020304050607L);
        long externalAttributes = entry.getExternalAttributes();
        Assertions.assertEquals(0x01020304050607L, externalAttributes);
        entry.setExternalAttributes(0);
    }

    /**
     * Tests getters and setters for Unix mode.
     */
    @Test
    public void testUnixMode() {
        entry.setUnixMode(0755);
        int unixMode = entry.getUnixMode();
        Assertions.assertEquals(0755, unixMode);
        entry.setUnixMode(0);
    }

    /**
     * Tests getters and setters for platform.
     */
    @Test
    public void testPlatform() {
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        int platform = entry.getPlatform();
        Assertions.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, platform);
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
    }

    /**
     * Tests setting and getting extra fields.
     */
    @Test
    public void testExtraFields() {
        entry.setExtraFields(new ZipExtraField[]{testExtraField});
        ZipExtraField[] extraFields = entry.getExtraFields();
        Assertions.assertEquals(1, extraFields.length);
        Assertions.assertEquals(testExtraField, extraFields[0]);
        entry.setExtraFields(new ZipExtraField[0]);
    }

    /**
     * Tests setting and getting a single extra field.
     */
    @Test
    public void testSingleExtraField() {
        entry.addExtraField(testExtraField);
        ZipExtraField extraField = entry.getExtraField(testExtraField.getHeaderId());
        Assertions.assertEquals(testExtraField, extraField);
        entry.removeExtraField(testExtraField.getHeaderId());
        extraField = entry.getExtraField(testExtraField.getHeaderId());
        Assertions.assertNull(extraField);
    }

    /**
     * Tests setting and getting multiple extra fields.
     */
    @Test
    public void testMultipleExtraFields() {
        entry.addExtraField(testExtraField);
        ZipExtraField[] extraFields = entry.getExtraFields();
        Assertions.assertEquals(1, extraFields.length);
        entry.addExtraField(new ZipExtraField(ZipShort.LOCAL_FILE_HEADER, "Another Test Extra Field"));
        extraFields = entry.getExtraFields();
        Assertions.assertEquals(2, extraFields.length);
        entry.removeExtraField(testExtraField.getHeaderId());
        extraFields = entry.getExtraFields();
        Assertions.assertEquals(1, extraFields.length);
    }

    /**
     * Tests setting the extra data.
     */
    @Test
    public void testSetExtraData() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] extraData = {"Test Extra Data".getBytes()};
        entry.setExtra(extraData);
        Assertions.assertArrayEquals(extraData, entry.getExtra());
        byte[] centralDirectoryExtraData = {"Central Directory Extra Data".getBytes()};
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);
        Assertions.assertArrayEquals(centralDirectoryExtraData, entry.getCentralDirectoryExtra());
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry equalEntry = new ZipArchiveEntry("test.txt");
        Assertions.assertTrue(entry.equals(equalEntry));
        ZipArchiveEntry notEqualEntry = new ZipArchiveEntry("anotherTest.txt");
        Assertions.assertFalse(entry.equals(notEqualEntry));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        String name = "test.txt";
        ZipArchiveEntry entry1 = new ZipArchiveEntry(name);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(name);
        Assertions.assertEquals(entry1.hashCode(), entry2.hashCode());
    }

    /**
     * Tests getLastModifiedDate method.
     */
    @Test
    public void testGetLastModifiedDate() {
        Date lastModifiedDate = new Date(System.currentTimeMillis());
        entry.setTime(lastModifiedDate.getTime());
        Date returnedLastModifiedDate = entry.getLastModifiedDate();
        Assertions.assertEquals(lastModifiedDate, returnedLastModifiedDate);
    }
}