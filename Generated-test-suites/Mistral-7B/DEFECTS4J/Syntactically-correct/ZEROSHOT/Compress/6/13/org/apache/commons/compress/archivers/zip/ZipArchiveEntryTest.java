package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    private ZipArchiveEntry zipEntry;

    @Before
    public void setup() {
        String name = "test.txt";
        zipEntry = new ZipArchiveEntry(name);
        zipEntry.setMethod(ZipEntry.STORED);
        zipEntry.setInternalAttributes(ZipEntry.FILE_FLAG);
        zipEntry.setExternalAttributes(0x12345678L);
        zipEntry.setUnixMode(0755);
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new ZipLocalFileDescriptorsExtraField(1, new byte[16]));
        extraFields.add(new ZipUnicodeVersionExtraField(1, new byte[2]));
        zipEntry.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("test.txt", zipEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(zipEntry.isDirectory());
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(ZipEntry.STORED, zipEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        zipEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        Assert.assertEquals(ZipEntry.FILE_FLAG, zipEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        zipEntry.setInternalAttributes(ZipEntry.DIRECTORY);
        Assert.assertEquals(ZipEntry.DIRECTORY, zipEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        Assert.assertEquals(0x12345678L, zipEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        long newExternalAttributes = 0x23456789L;
        zipEntry.setExternalAttributes(newExternalAttributes);
        Assert.assertEquals(newExternalAttributes, zipEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        Assert.assertEquals(0755, zipEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        Assert.assertEquals(0755, zipEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());
    }

    @Test
    public void testGetExtraFields() {
        ZipExtraField[] extraFields = zipEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[0], extraFields);
    }

    @Test
    public void testSetExtraFields() {
        List<ZipExtraField> newExtraFields = new ArrayList<>();
        newExtraFields.add(new ZipLocalFileDescriptorsExtraField(1, new byte[16]));
        newExtraFields.add(new ZipUnicodeVersionExtraField(1, new byte[2]));
        zipEntry.setExtraFields(newExtraFields.toArray(new ZipExtraField[0]));

        ZipExtraField[] result = zipEntry.getExtraFields();
        Assert.assertEquals(newExtraFields.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(result[i].getHeaderId(), newExtraFields.get(i).getHeaderId());
        }
    }

    @Test
    public void testAddExtraField() {
        List<ZipExtraField> newExtraFields = new ArrayList<>();
        newExtraFields.add(new ZipLocalFileDescriptorsExtraField(1, new byte[16]));
        zipEntry.addExtraField(newExtraFields.get(0));

        ZipExtraField[] result = zipEntry.getExtraFields();
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(result[0].getHeaderId(), newExtraFields.get(0).getHeaderId());
    }

    @Test
    public void testAddAsFirstExtraField() {
        List<ZipExtraField> newExtraFields = new ArrayList<>();
        newExtraFields.add(new ZipLocalFileDescriptorsExtraField(1, new byte[16]));
        zipEntry.addAsFirstExtraField(newExtraFields.get(0));

        ZipExtraField[] result = zipEntry.getExtraFields();
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(result[0].getHeaderId(), newExtraFields.get(0).getHeaderId());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveExtraField() {
        ZipShort type = new ZipShort((short) 1);
        zipEntry.removeExtraField(type);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraField() {
        ZipShort type = new ZipShort((short) 1);
        Assert.assertNull(zipEntry.getExtraField(type));
    }

    @Test
    public void testSetLocalFileDataExtra() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ZipExtraField localExtraField = new ZipLocalFileDescriptorsExtraField(1, new byte[16]);
        zipEntry.setExtra(localExtraField.getLocalFileDataData());

        byte[] result = zipEntry.getLocalFileDataExtra();
        Assert.assertArrayEquals(localExtraField.getLocalFileDataData(), result);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ZipExtraField centralDirectoryExtraField = new ZipUnicodeVersionExtraField(1, new byte[2]);
        byteStream.write(centralDirectoryExtraField.getCentralDirectoryData());
        zipEntry.setCentralDirectoryExtra(byteStream.toByteArray());

        byte[] result = zipEntry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(centralDirectoryExtraField.getCentralDirectoryData(), result);
    }

    @Test
    public void testGetLastModifiedDate() {
        long time = System.currentTimeMillis();
        zipEntry.setTime(time);
        Date date = zipEntry.getLastModifiedDate();
        Assert.assertEquals(DateUtils.formatDate(new Date(time)), date.toString());
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals("test.txt".hashCode(), zipEntry.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry other = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(zipEntry.equals(other));

        other.setName(zipEntry.getName());
        Assert.assertTrue(zipEntry.equals(other));
    }
}