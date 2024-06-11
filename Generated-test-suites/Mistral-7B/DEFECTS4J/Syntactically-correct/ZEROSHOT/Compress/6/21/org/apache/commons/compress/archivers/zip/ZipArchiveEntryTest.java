package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test class for the {@link ZipArchiveEntry} class.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor with name.
     */
    @org.junit.Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertSame(ZipEntry.STORED, entry.getMethod());
        assertFalse(entry.isDirectory());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
    }

    /**
     * Test constructor with ZipEntry.
     */
    @org.junit.Test
    public void testConstructorWithZipEntry() throws ZipException {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setExtra(extra);
        zipEntry.setMethod(ZipEntry.STORED);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertEquals(extra, entry.getLocalFileDataExtra());
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraFields()[0];
        assertEquals(ZipShort.APPLICATION_EXTENDED, extraField.getHeaderId());
        assertEquals(extra, extraField.getData());
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertFalse(entry.isDirectory());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
    }

    /**
     * Test constructor with ZipArchiveEntry.
     */
    @org.junit.Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        byte[] extraLocal = new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09 };
        ZipExtraField extraField = new ZipExtraField(ZipShort.APPLICATION_EXTENDED,
                extra);
        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(extraField);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
        ZipArchiveEntry clone = new ZipArchiveEntry(entry);
        assertEquals("test.txt", clone.getName());
        assertEquals(extra, clone.getLocalFileDataExtra());
        assertEquals(1, clone.getExtraFields().length);
        assertEquals(extra, clone.getExtraFields()[0].getData());
        assertEquals(ZipEntry.STORED, clone.getMethod());
        assertFalse(clone.isDirectory());
        assertEquals(0, clone.getInternalAttributes());
        assertEquals(0, clone.getExternalAttributes());
        assertFalse(clone.isSupportedCompressionMethod());
        assertEquals(0, clone.getPlatform());
    }

    /**
     * Test constructor with File.
     */
    @org.junit.Test
    public void testConstructorWithFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(new Date(inputFile.lastModified()), entry.getTime());
        assertFalse(entry.isDirectory());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() throws ZipException {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        byte[] extraLocal = new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09 };
        ZipExtraField extraField = new ZipExtraField(ZipShort.APPLICATION_EXTENDED,
                extra);
        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(extraField);
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        original.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
        original.setInternalAttributes(0x0a);
        original.setExternalAttributes(0x0b);
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals("test.txt", clone.getName());
        assertEquals(extra, clone.getLocalFileDataExtra());
        assertEquals(1, clone.getExtraFields().length);
        assertEquals(extra, clone.getExtraFields()[0].getData());
        assertEquals(ZipEntry.STORED, clone.getMethod());
        assertFalse(clone.isDirectory());
        assertEquals(0x0a, clone.getInternalAttributes());
        assertEquals(0x0b, clone.getExternalAttributes());
        assertFalse(clone.isSupportedCompressionMethod());
        assertEquals(0, clone.getPlatform());
    }

    /**
     * Test getInternalAttributes method.
     */
    @org.junit.Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());
        entry.setInternalAttributes(0x0a);
        assertEquals(0x0a, entry.getInternalAttributes());
    }

    /**
     * Test setInternalAttributes method.
     */
    @org.junit.Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x0a);
        assertEquals(0x0a, entry.getInternalAttributes());
    }

    /**
     * Test getExternalAttributes method.
     */
    @org.junit.Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
        entry.setExternalAttributes(0x0a);
        assertEquals(0x0a, entry.getExternalAttributes());
    }

    /**
     * Test setExternalAttributes method.
     */
    @org.junit.Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x0a);
        assertEquals(0x0a, entry.getExternalAttributes());
    }

    /**
     * Test getPlatform method.
     */
    @org.junit.Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getPlatform());
        entry.setPlatform(3);
        assertEquals(3, entry.getPlatform());
    }

    /**
     * Test setPlatform method.
     */
    @org.junit.Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getPlatform());
        entry.setPlatform(3);
        assertEquals(3, entry.getPlatform());
    }

    /**
     * Test getName method.
     */
    @org.junit.Test
    public void testgetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test isDirectory method.
     */
    @org.junit.Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());
        entry = new ZipArchiveEntry("test/");
        assertTrue(entry.isDirectory());
    }

    /**
     * Test equals method.
     */
    @org.junit.Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));
        assertSame(entry1, entry2);

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = null;
        assertFalse(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test2.txt");
        assertFalse(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test.txt");
        entry2.setName("test2.txt");
        assertFalse(entry1.equals(entry2));
    }

    /**
     * Test hashCode method.
     */
    @org.junit.Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test2.txt");
        assertNotSame(entry1.hashCode(), entry2.hashCode());

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test.txt");
        entry2.setName("test2.txt");
        assertNotSame(entry1.hashCode(), entry2.hashCode());
    }

    /**
     * Test getMethod method.
     */
    @org.junit.Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test setMethod method.
     */
    @org.junit.Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    /**
     * Test isSupportedCompressionMethod method.
     */
    @org.junit.Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    /**
     * Test getLocalFileDataExtra method.
     */
    @org.junit.Test
    public void testGetLocalFileDataExtra() {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setLocalFileDataExtra(extra);
        assertEquals(extra, entry.getLocalFileDataExtra());
    }

    /**
     * Test setLocalFileDataExtra method.
     */
    @org.junit.Test
    public void testSetLocalFileDataExtra() {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        byte[] extraLocal = new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09 };
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        InputStream input = new ByteArrayInputStream(extra);
        entry.setLocalFileDataExtra(input);
        assertEquals(extra, entry.getLocalFileDataExtra());
    }

    /**
     * Test getCentralDirectoryExtra method.
     */
    @org.junit.Test
    public void testGetCentralDirectoryExtra() {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setCentralDirectoryExtra(extra);
        assertEquals(extra, entry.getCentralDirectoryExtra());
    }

    /**
     * Test setCentralDirectoryExtra method.
     */
    @org.junit.Test
    public void testSetCentralDirectoryExtra() throws IOException {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setCentralDirectoryExtra(output.toByteArray());
        assertEquals(extra, entry.getCentralDirectoryExtra());
    }

    /**
     * Test mergeExtraFields method.
     */
    @org.junit.Test
    public void testMergeExtraFields() throws ZipException {
        byte[] extra = new byte[] { 0x08, 0x01, 0x02, 0x03, 0x04 };
        byte[] extraLocal = new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09 };
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.APPLICATION_EXTENDED,
                extra);
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.APPLICATION_EXTENDED,
                extraLocal);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.mergeExtraFields(new ZipExtraField[] { extraField1, extraField2 },
                true);
        assertEquals(2, entry.getExtraFields().length);
        assertEquals(extra, entry.getExtraFields()[0].getData());
        assertEquals(extraLocal, entry.getExtraFields()[1].getData());

        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        entry2.mergeExtraFields(new ZipExtraField[] { extraField1, extraField2 },
                false);
        assertEquals(2, entry2.getExtraFields().length);
        assertEquals(extra, entry2.getExtraFields()[0].getData());
        assertEquals(extraLocal, entry2.getExtraFields()[1].getData());
    }
}