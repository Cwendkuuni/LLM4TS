package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ZipArchiveEntry.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor with name.
     */
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test constructor with ZipEntry.
     */
    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        String name = "test.txt";
        ZipEntry zipEntry = new ZipEntry(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test constructor with ArchiveEntry.
     */
    @Test
    public void testConstructorWithArchiveEntry() throws ZipException {
        String name = "test.txt";
        ArchiveEntry archiveEntry = new ZipArchiveEntry(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(archiveEntry);
        assertEquals(name, entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test constructor with File.
     */
    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        // TODO: Add assertions for size, time, and other fields
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test cloning.
     */
    @Test
    public void testClone() throws Exception {
        String name = "test.txt";
        ZipArchiveEntry original = new ZipArchiveEntry(name);
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        assertEquals(original, cloned);
    }

    /**
     * Test isSupportedCompressionMethod.
     */
    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    /**
     * Test getMethod.
     */
    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    /**
     * Test setMethod.
     */
    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertThrows(IllegalArgumentException.class, () -> entry.setMethod(-1));
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    /**
     * Test getInternalAttributes.
     */
    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());
        entry.setInternalAttributes(1);
        assertEquals(1, entry.getInternalAttributes());
    }

    /**
     * Test setInternalAttributes.
     */
    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(1);
        assertEquals(1, entry.getInternalAttributes());
    }

    /**
     * Test getExternalAttributes.
     */
    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
        entry.setExternalAttributes(1);
        assertEquals(1, entry.getExternalAttributes());
    }

    /**
     * Test setExternalAttributes.
     */
    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(1);
        assertEquals(1, entry.getExternalAttributes());
    }

    /**
     * Test setUnixMode.
     */
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
    }

    /**
     * Test getPlatform.
     */
    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    /**
     * Test setPlatform.
     */
    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    /**
     * Test setExtraFields.
     */
    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.setExtraFields(fields);
        assertEquals(fields.length, entry.getExtraFields().length);
    }

    /**
     * Test getExtraFields.
     */
    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.setExtraFields(fields);
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(fields.length, result.length);
        assertEquals(fields[0].getHeaderId(), result[0].getHeaderId());
        assertEquals(fields[0].getValue().length, result[0].getValue().length);
    }

    /**
     * Test addExtraField.
     */
    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.addExtraField(fields[0]);
        assertEquals(1, entry.getExtraFields().length);
    }

    /**
     * Test addAsFirstExtraField.
     */
    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.addAsFirstExtraField(fields[0]);
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField first = entry.getExtraFields()[0];
        assertEquals(fields[0].getHeaderId(), first.getHeaderId());
        assertEquals(fields[0].getValue().length, first.getValue().length);
    }

    /**
     * Test removeExtraField.
     */
    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.setExtraFields(fields);
        entry.removeExtraField(ZipShort.US_ASCII);
        assertEquals(0, entry.getExtraFields().length);
    }

    /**
     * Test getExtraField.
     */
    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        entry.setExtraFields(fields);
        ZipExtraField result = entry.getExtraField(ZipShort.US_ASCII);
        assertEquals(fields[0], result);
    }

    /**
     * Test setExtra.
     */
    @Test
    public void testSetExtra() throws RuntimeException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[10];
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtraField(ZipShort.US_ASCII, "test", "test value".getBytes());
        ZipExtraField[] result = ExtraFieldUtils.parse(extra, true);
        entry.setExtraFields(result);
        assertEquals(fields.length, result.length);
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test isDirectory.
     */
    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());
        entry.setName("test/");
        assertTrue(entry.isDirectory());
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        int expected = "test.txt".hashCode();
        int actual = entry.hashCode();
        assertEquals(expected, actual);
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));

        String name = "test.txt";
        ZipArchiveEntry entry3 = new ZipArchiveEntry(name);
        ZipArchiveEntry entry4 = new ZipArchiveEntry(name);
        assertTrue(entry3.equals(entry4));

        ZipArchiveEntry entry5 = new ZipArchiveEntry("test2.txt");
        assertFalse(entry1.equals(entry5));

        ZipArchiveEntry entry6 = new Object();
        assertFalse(entry1.equals(entry6));
    }

    /**
     * Test equals with null.
     */
    @Test
    public void testEqualsWithNull() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.equals(null));
    }

    /**
     * Test equals with different class.
     */
    @Test
    public void testEqualsWithDifferentClass() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.equals(new Object()));
    }

    /**
     * Test getLastModifiedDate.
     */
    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Date expected = new Date(System.currentTimeMillis());
        Date actual = entry.getLastModifiedDate();
        assertEquals(expected, actual);
    }
}