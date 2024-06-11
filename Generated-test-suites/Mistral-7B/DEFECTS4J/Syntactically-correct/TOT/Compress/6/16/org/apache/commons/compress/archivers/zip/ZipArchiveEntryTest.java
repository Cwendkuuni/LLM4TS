package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryCreation() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryCreationFromZipEntry() throws IOException {
        String name = "test.txt";
        File file = new File("test.txt");
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(file.length(), entry.getSize());
        Assert.assertEquals(file.lastModified(), entry.getTime());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryCreationFromZipArchiveEntry() throws IOException {
        String name = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertEquals(name, clonedEntry.getName());
        Assert.assertEquals(-1, clonedEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        Assert.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assert.assertArrayEquals(originalEntry.getExtra(), clonedEntry.getExtra());
    }

    @Test
    public void testZipArchiveEntryCreationFromFile() throws IOException {
        String name = "test.txt";
        File file = new File(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(file, name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(file.length(), entry.getSize());
        Assert.assertEquals(file.lastModified(), entry.getTime());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetSupportedCompressionMethod() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setMethod(ZipConstants.COMP_STORED);
        Assert.assertEquals(ZipConstants.COMP_STORED, entry.getMethod());
        entry.setMethod(ZipConstants.COMP_DEFLATED);
        Assert.assertEquals(ZipConstants.COMP_DEFLATED, entry.getMethod());
        entry.setMethod(-1);
        Assert.assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setInternalAttributes(ZipConstants.INT_FILE_TIME);
        Assert.assertEquals(ZipConstants.INT_FILE_TIME, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExternalAttributes(0x1234L);
        Assert.assertEquals(0x1234L, entry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(-1);
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetPlatformWithUnsupportedPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setPlatform(4);
        Assert.assertEquals(0, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField field = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[] {field});
        Assert.assertArrayEquals(new ZipExtraField[] {field}, entry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField field = new ZipExtendedTimestamps();
        entry.addExtraField(field);
        Assert.assertArrayEquals(new ZipExtraField[] {field}, entry.getExtraFields());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtraFieldWithNullField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.addExtraField(null);
    }

    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void testSetExtraFieldsWithNullArray() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExtraFields(null);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField field = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[] {field});
        entry.removeExtraField(ZipShort.LOCAL_FILE_NAME);
        Assert.assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraFieldWithNonExistentType() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.removeExtraField(ZipShort.COMMENT);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField field = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[] {field});
        Assert.assertEquals(field, entry.getExtraField(ZipShort.LOCAL_FILE_NAME));
        Assert.assertNull(entry.getExtraField(ZipShort.COMMENT));
    }

    @Test
    public void testSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] extraData = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream out = outputStream;
        entry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getExtra());
        byte[] result = entry.getExtra();
        Assert.assertEquals(extraData.length, result.length);
        Assert.assertArrayEquals(extraData, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] extraData = new byte[1024];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(extraData);
        entry.setExtra(inputStream);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] extraData = new byte[1024];
        entry.setCentralDirectoryExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getExtra());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] extraData = new byte[1024];
        entry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] extraData = new byte[1024];
        entry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getCentralDirectoryExtra());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
        Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(entry1.equals(entry1));
        Assert.assertTrue(entry1.equals(entry2));
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(entry1.equals(entry3));
    }

    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String result = entry.toString();
        Assert.assertEquals("ZipArchiveEntry{name='test.txt', size=0, method=Stored, compressedSize=0, cri=null, localFileData=<not set>}", result);
    }

    @Test
    public void testClone() throws IOException, CloneNotSupportedException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertEquals(originalEntry.getName(), clonedEntry.getName());
        Assert.assertEquals(originalEntry.getSize(), clonedEntry.getSize());
        Assert.assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        Assert.assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        Assert.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assert.assertArrayEquals(originalEntry.getExtra(), clonedEntry.getExtra());
        Assert.assertArrayEquals(originalEntry.getLocalFileDataExtra(), clonedEntry.getLocalFileDataExtra());
        Assert.assertArrayEquals(originalEntry.getCentralDirectoryExtra(), clonedEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testEqualsWithSameObject() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(entry.equals(entry));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Object other = new Object();
        Assert.assertFalse(entry.equals(other));
    }

    @Test
    public void testEqualsWithDifferentName() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testCloneWithNullExtra() throws IOException, CloneNotSupportedException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt") {
            @Override
            public byte[] getExtra() {
                return null;
            }
        };
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertNull(clonedEntry.getExtra());
        Assert.assertNull(clonedEntry.getLocalFileDataExtra());
        Assert.assertNull(clonedEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testCloneWithCustomExtraFields() throws IOException, CloneNotSupportedException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        originalEntry.setExtraFields(new ZipExtraField[] {new CustomExtraField()});
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertNotNull(clonedEntry.getExtraFields());
        Assert.assertEquals(1, clonedEntry.getExtraFields().length);
        Assert.assertEquals(CustomExtraField.class, clonedEntry.getExtraFields()[0].getClass());
    }
}