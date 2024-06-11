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
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithFileName() {
        String fileName = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(fileName);
        Assert.assertEquals(fileName, zipArchiveEntry.getName());
        Assert.assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        String fileName = "test.txt";
        File testFile = new File(fileName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new File(fileName));
        Assert.assertEquals(fileName, zipArchiveEntry.getName());
        Assert.assertFalse(zipArchiveEntry.isDirectory());
        Assert.assertEquals(testFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(new Date(testFile.lastModified()), zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testConstructorWithDirectoryPathAndFileName() {
        String directoryPath = "test";
        String fileName = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new File(directoryPath, fileName));
        Assert.assertEquals(directoryPath + File.separator + fileName, zipArchiveEntry.getName());
        Assert.assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testClone() throws Exception {
        String fileName = "test.txt";
        File testFile = new File(fileName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new File(fileName));
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
        Assert.assertEquals(zipArchiveEntry.getName(), clonedEntry.getName());
        Assert.assertEquals(zipArchiveEntry.getSize(), clonedEntry.getSize());
        Assert.assertEquals(zipArchiveEntry.getLastModifiedDate(), clonedEntry.getLastModifiedDate());
        Assert.assertArrayEquals(zipArchiveEntry.getExtra(), clonedEntry.getExtra());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        long externalAttributes = 0xFFFFFFFFL;
        zipArchiveEntry.setExternalAttributes(externalAttributes);
        Assert.assertEquals(externalAttributes, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipShort headerId = ZipShort.create(0x0001);
        ZipExtraField extraField = new GeneralPurposeByteArrayField("Test", new byte[10]);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(headerId, zipArchiveEntry.getExtraField(headerId).getHeaderId());
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getData().toString());
    }

    @Test
    public void testSetExtra() throws ZipException {
        ZipShort headerId = ZipShort.create(0x0001);
        ZipExtraField extraField = new GeneralPurposeByteArrayField("Test", new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getData().array(), 0, extraData, 0, extraData.length);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExtra(extraData);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(headerId, zipArchiveEntry.getExtraField(headerId).getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtra());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipShort headerId = ZipShort.create(0x0001);
        ZipExtraField extraField = new GeneralPurposeByteArrayField("Test", new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getData().array(), 0, extraData, 0, extraData.length);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipShort headerId = ZipShort.create(0x0001);
        ZipExtraField extraField = new GeneralPurposeByteArrayField("Test", new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getData().array(), 0, extraData, 0, extraData.length);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(headerId, zipArchiveEntry.getExtraField(headerId).getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testgetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertEquals("", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        String fileName = "test.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(fileName);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(fileName);
        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }
}