package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;

public class ZipArchiveEntryTest {

    private static final int TEST_PLATFORM_UNIX = 3;
    private static final int TEST_PLATFORM_FAT = 0;
    private static final int TEST_SHORT_MASK = 0xFFFF;
    private static final int TEST_SHORT_SHIFT = 16;

    @SuppressWarnings("unchecked")
    private static Map<Integer, ZipExtraField> createExtraFields() {
        int headerId = 1;
        ZipExtraField extraField1 = new ZipExtendedInformation();
        extraField1.setHeaderId(headerId++);
        ZipExtraField extraField2 = new ZipFileName();
        extraField2.setHeaderId(headerId++);
        return new LinkedHashMap<Integer, ZipExtraField>() {{
            put(headerId - 2, extraField1);
            put(headerId - 1, extraField2);
        }};
    }

    @SuppressWarnings("unchecked")
    private static ZipExtraField[] createExtraFieldsArray() {
        return createExtraFields().values().toArray(new ZipExtraField[0]);
    }

    @SuppressWarnings("unchecked")
    private static byte[] createExtraFieldsData() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStream out = os) {
            for (ZipExtraField field : createExtraFieldsArray()) {
                ExtraFieldUtils.writeFieldData(field, out);
            }
        }
        return os.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private static byte[] createCentralDirectoryExtraData() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStream out = os) {
            ExtraFieldUtils.writeCentralDirectoryData(createExtraFieldsArray(), out);
        }
        return os.toByteArray();
    }

    private static ZipArchiveEntry createTestEntry(String name) {
        return new ZipArchiveEntry(name);
    }

    private static ZipArchiveEntry createTestEntryFromZipEntry(ZipEntry entry) throws ZipException {
        return new ZipArchiveEntry(entry);
    }

    private static ZipArchiveEntry createTestEntryFromZipArchiveEntry(ZipArchiveEntry entry) throws ZipException {
        return new ZipArchiveEntry(entry);
    }

    private static ZipArchiveEntry createTestEntryFromFile(File file, String entryName) {
        return new ZipArchiveEntry(file, entryName);
    }

    @org.junit.Test
    public void testNewInstance() {
        ZipArchiveEntry entry = createTestEntry("");
        assertEquals("", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertNull(entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getSize());
        assertNull(entry.getTime());
    }

    @org.junit.Test
    public void testNewInstanceFromZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = createTestEntryFromZipEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertNull(entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getSize());
        assertNull(entry.getTime());
    }

    @org.junit.Test
    public void testNewInstanceFromZipArchiveEntry() throws ZipException {
        ZipArchiveEntry original = createTestEntry("test.txt");
        ZipArchiveEntry entry = createTestEntryFromZipArchiveEntry(original);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertNull(entry.getMethod());
        assertEquals(original.getInternalAttributes(), entry.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), entry.getExternalAttributes());
        assertEquals(original.getPlatform(), entry.getPlatform());
        assertEquals(original.getSize(), entry.getSize());
        assertEquals(original.getTime(), entry.getTime());
    }

    @org.junit.Test
    public void testNewInstanceFromFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = createTestEntryFromFile(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertNull(entry.getMethod());
        assertEquals(TEST_PLATFORM_FAT, entry.getPlatform());
        assertEquals(file.length(), entry.getSize());
        assertNull(entry.getTime());
        try {
            entry.setTime(new Date(System.currentTimeMillis() - 1000));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetSupportedCompressionMethod() {
        ZipArchiveEntry entry = createTestEntry("");
        entry.setMethod(ZipEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(-1);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @org.junit.Test
    public void testGetMethod() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertNull(entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @org.junit.Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
        entry.setUnixMode(0644);
        assertEquals(0644, entry.getUnixMode());
    }

    @org.junit.Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertNull(entry.getUnixMode());
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @org.junit.Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertEquals(TEST_PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(TEST_PLATFORM_UNIX);
        assertEquals(TEST_PLATFORM_UNIX, entry.getPlatform());
    }

    @org.junit.Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertEquals(TEST_PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(TEST_PLATFORM_UNIX);
        assertEquals(TEST_PLATFORM_UNIX, entry.getPlatform());
    }

    @org.junit.Test
    public void testSetExtraFields() throws ZipException {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        ZipExtraField[] extraFields = createExtraFieldsArray();
        entry.setExtraFields(extraFields);
        assertEquals(extraFields.length, entry.getExtraFields().length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(extraFields[i].getHeaderId(), entry.getExtraField(extraFields[i].getHeaderId()).getHeaderId());
        }

        byte[] extraData = createExtraFieldsData();
        entry.setExtra(extraData);
        assertEquals(extraFields.length, entry.getExtraFields().length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(extraFields[i].getHeaderId(), entry.getExtraField(extraFields[i].getHeaderId()).getHeaderId());
        }

        byte[] centralDirectoryExtraData = createCentralDirectoryExtraData();
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);
        assertEquals(extraFields.length, entry.getExtraFields().length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(extraFields[i].getHeaderId(), entry.getExtraField(extraFields[i].getHeaderId()).getHeaderId());
        }
    }

    @org.junit.Test
    public void testGetSize() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertEquals(0, entry.getSize());
        entry.setSize(100);
        assertEquals(100, entry.getSize());
    }

    @org.junit.Test
    public void testSetTime() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertNull(entry.getTime());
        Date time = new Date(System.currentTimeMillis() - 1000);
        entry.setTime(time);
        assertEquals(time, entry.getTime());
    }

    @org.junit.Test
    public void testGetName() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @org.junit.Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = createTestEntry("test.txt");
        assertFalse(entry.isDirectory());
        entry = createTestEntry("test/");
        assertTrue(entry.isDirectory());
    }

    @org.junit.Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = createTestEntry("test1.txt");
        ZipArchiveEntry entry2 = createTestEntry("test1.txt");
        assertEquals(entry1.hashCode(), entry2.hashCode());

        ZipArchiveEntry entry3 = createTestEntry("test2.txt");
        assertNotEquals(entry1.hashCode(), entry3.hashCode());
    }

    @org.junit.Test
    public void testEquals() {
        ZipArchiveEntry entry1 = createTestEntry("test1.txt");
        ZipArchiveEntry entry2 = createTestEntry("test1.txt");
        assertTrue(entry1.equals(entry2));

        ZipArchiveEntry entry3 = createTestEntry("test2.txt");
        assertFalse(entry1.equals(entry3));

        ZipArchiveEntry entry4 = new ZipArchiveEntry(entry1);
        assertTrue(entry1.equals(entry4));

        entry4.setName("test3.txt");
        assertFalse(entry1.equals(entry4));

        entry4.setMethod(ZipEntry.DEFLATED);
        assertFalse(entry1.equals(entry4));

        entry4.setPlatform(TEST_PLATFORM_UNIX);
        assertFalse(entry1.equals(entry4));

        entry4.setSize(10);
        assertFalse(entry1.equals(entry4));

        entry4.setTime(new Date(System.currentTimeMillis() - 1000));
        assertFalse(entry1.equals(entry4));
    }
}