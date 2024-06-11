package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.entry.ZipShort;
import org.apache.commons.compress.archivers.zip.format.LocalFileHeader;
import org.apache.commons.compress.archivers.zip.format.ZipEntryHeader;
import org.apache.commons.compress.archivers.zip.format.ZipExtraField;
import org.apache.commons.compress.archivers.zip.format.ZipExtraFieldHeader;
import org.apache.commons.compress.archivers.zip.format.ZipException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ZipArchiveEntryTest {

    @Test
    public void testNewInstanceWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getSize());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getPlatform());
    }

    @Test
    public void testNewInstanceWithFile() throws IOException {
        File testFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(testFile.length(), entry.getSize());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getPlatform());
    }

    @Test
    public void testClone() throws IOException, ZipException {
        File testFile = new File("test.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(testFile, "test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getSize(), clone.getSize());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getInternalAttributes(), clone.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), clone.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), clone.getExtraFields());
        assertEquals(original.getPlatform(), clone.getPlatform());
    }

    @Test
    public void testNewInstanceWithZipEntry() throws IOException, ZipException {
        File testFile = new File("test.txt");
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertNull(archiveEntry.getSize());
        assertEquals(zipEntry.getMethod(), archiveEntry.getMethod());
        assertNull(archiveEntry.getInternalAttributes());
        assertNull(archiveEntry.getExternalAttributes());
        assertNull(archiveEntry.getExtraFields());
        assertNull(archiveEntry.getPlatform());
    }

    @Test
    public void testNewInstanceWithZipArchiveEntry() throws IOException, ZipException {
        File testFile = new File("test.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(testFile, "test.txt");
        ZipArchiveEntry copy = new ZipArchiveEntry(original);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getSize(), copy.getSize());
        assertEquals(original.getMethod(), copy.getMethod());
        assertEquals(original.getInternalAttributes(), copy.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), copy.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), copy.getExtraFields());
        assertEquals(original.getPlatform(), copy.getPlatform());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testGetAndSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x0201);
        assertEquals(0x0201, entry.getInternalAttributes());
    }

    @Test
    public void testGetAndSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x0101);
        assertEquals(0x0101, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);

        // check the unix mode is correctly set
        assertEquals(0755, entry.getUnixMode());

        // check the platform is set to UNIX
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);

        // check the unix mode is correctly retrieved
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);

        // check the platform is correctly set
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());

        // check the unix mode is not set anymore
        assertEquals(0, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);

        // check the platform is correctly retrieved
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testGetAndSetExtraFields() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(LocalFileHeader.X_APP_EXT_ENCRYPTION);
        ZipExtraField field = new ZipExtraFieldHeader(headerId, new byte[0]);
        entry.setExtraFields(new ZipExtraField[]{field});
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField retrievedField = entry.getExtraField(headerId);
        assertEquals(field, retrievedField);
    }

    @Test
    public void testAddExtraField() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(LocalFileHeader.X_APP_EXT_ENCRYPTION);
        ZipExtraField field = new ZipExtraFieldHeader(headerId, new byte[0]);
        entry.addExtraField(field);
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField retrievedField = entry.getExtraField(headerId);
        assertEquals(field, retrievedField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtraFieldNull() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtraFieldInvalidHeaderId() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort((short) 0xFFFF);
        entry.addExtraField(new ZipExtraFieldHeader(headerId, new byte[0]));
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(LocalFileHeader.X_APP_EXT_ENCRYPTION);
        ZipExtraField field = new ZipExtraFieldHeader(headerId, new byte[0]);
        entry.setExtraFields(new ZipExtraField[]{field});
        assertEquals(1, entry.getExtraFields().length);
        entry.removeExtraField(headerId);
        assertNull(entry.getExtraField(headerId));
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraFieldNotFound() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(LocalFileHeader.X_APP_EXT_ENCRYPTION);
        entry.removeExtraField(headerId);
    }

    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String toString = entry.toString();
        assertTrue(toString.contains("name=" + entry.getName()));
        assertTrue(toString.contains("size=" + entry.getSize()));
        assertTrue(toString.contains("method=" + entry.getMethod()));
        assertTrue(toString.contains("internalAttributes=" + entry.getInternalAttributes()));
        assertTrue(toString.contains("externalAttributes=" + entry.getExternalAttributes()));
        assertTrue(toString.contains("extraFields=" + Arrays.toString(entry.getExtraFields())));
        assertTrue(toString.contains("platform=" + entry.getPlatform()));
    }

    @Test
    public void testGetLocalFileDataExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        entry.setExtra(extraData);
        byte[] localDataExtra = entry.getLocalFileDataExtra();
        assertArrayEquals(extraData, localDataExtra);
    }

    @Test
    public void testGetCentralDirectoryExtra() throws ZipException, IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        LocalFileHeader localHeader = new LocalFileHeader(entry.getName().getBytes(), entry.getSize(), entry.getMethod(), entry.getInternalAttributes(), entry.getExternalAttributes(), new Date(), extraData);
        byte[] centralDirectoryExtra = localHeader.getExtraFieldData();
        entry.setCentralDirectoryExtra(centralDirectoryExtra);
        byte[] centralDataExtra = entry.getCentralDirectoryExtra();
        assertArrayEquals(extraData, centralDataExtra);
    }
}