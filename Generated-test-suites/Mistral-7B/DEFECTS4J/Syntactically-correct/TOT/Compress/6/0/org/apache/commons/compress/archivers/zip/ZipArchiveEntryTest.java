package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() throws ZipException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        assertEquals(originalEntry, clonedEntry);
    }

    @Test
    public void testZipArchiveEntryConstructorWithFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipArchiveEntryWithNegativeMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(-1);
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(1);
        assertEquals(1, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(1);
        assertEquals(1, entry.getExternalAttributes());
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
        ZipExtraField[] fields = new ZipExtraField[] {new ZipStringHeader(ZipShort.LOCAL_FILE_NAME, "name.txt")};
        entry.setExtraFields(fields);
        assertNotNull(entry.getExtraFields());
        assertEquals(1, entry.getExtraFields().length);
        assertEquals("name.txt", entry.getExtraField(ZipShort.LOCAL_FILE_NAME).getStringValue());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = new ZipStringHeader(ZipShort.LOCAL_FILE_NAME, "name.txt");
        entry.addExtraField(field);
        assertEquals("name.txt", entry.getExtraField(ZipShort.LOCAL_FILE_NAME).getStringValue());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = new ZipStringHeader(ZipShort.LOCAL_FILE_NAME, "name.txt");
        entry.addExtraField(field);
        assertNotNull(entry.getExtraFields());
        assertEquals(1, entry.getExtraFields().length);
        assertEquals("name.txt", entry.getExtraField(ZipShort.LOCAL_FILE_NAME).getStringValue());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = new ZipStringHeader(ZipShort.LOCAL_FILE_NAME, "name.txt");
        entry.addAsFirstExtraField(field);
        assertNotNull(entry.getExtraFields());
        assertEquals(1, entry.getExtraFields().length);
        assertEquals("name.txt", entry.getExtraField(ZipShort.LOCAL_FILE_NAME).getStringValue());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = new ZipStringHeader(ZipShort.LOCAL_FILE_NAME, "name.txt");
        entry.addExtraField(field);
        entry.removeExtraField(ZipShort.LOCAL_FILE_NAME);
        assertNull(entry.getExtraField(ZipShort.LOCAL_FILE_NAME));
    }

    @Test
    public void testSetExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        entry.setExtra(extra);
        byte[] extraFromEntry = entry.getLocalFileDataExtra();
        assertArrayEquals(extra, extraFromEntry);
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralExtra = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        entry.setCentralDirectoryExtra(centralExtra);
        byte[] extraFromEntry = entry.getCentralDirectoryExtra();
        assertArrayEquals(centralExtra, extraFromEntry);
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test1.txt");
        assertEquals(entry3.hashCode(), entry1.hashCode());
        assertNotEquals(entry3.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test1.txt");
        assertTrue(entry1.equals(entry3));
        assertFalse(entry1.equals(entry2));
    }
}