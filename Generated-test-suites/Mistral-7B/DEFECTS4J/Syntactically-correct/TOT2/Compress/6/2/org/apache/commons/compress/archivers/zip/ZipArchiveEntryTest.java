package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateEntryWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testCreateEntryWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(ZipEntry.GENERIC_MASK);
        Assert.assertEquals(ZipEntry.GENERIC_MASK, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(ZipEntry.FILE_PERMISSION);
        Assert.assertEquals(ZipEntry.FILE_PERMISSION, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0644);
        Assert.assertEquals(0644, entry.getUnixMode());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        Assert.assertNotSame(original, clone);
        Assert.assertEquals(original.getName(), clone.getName());
        Assert.assertEquals(original.getMethod(), clone.getMethod());
        Assert.assertEquals(original.getInternalAttributes(), clone.getInternalAttributes());
        Assert.assertEquals(original.getExternalAttributes(), clone.getExternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFieldsEmpty() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[0];
        entry.setExtra(extra);
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetExtraFieldsValid() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[]{0x01, 0x02, 0x03};
        ZipExtraField[] fields = {new ZipExtendedInfo("TestInfo")};
        entry.setExtraFields(fields);
        entry.setExtra(extra);
        Assert.assertArrayEquals(extra, entry.getRawExtraData());
        Assert.assertArrayEquals(extra, entry.getLocalFileDataExtra());
        Assert.assertNotNull(entry.getExtraFields());
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals("TestInfo", entry.getExtraFields()[0].getName());
    }

    @Test
    public void testSetExtraFieldsInvalidLength() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[]{0x01, 0x02, 0x03};
        ZipExtraField[] fields = {new ZipExtendedInfo("TestInfo")};
        try {
            entry.setExtraFields(fields);
            entry.setExtra(extra);
            Assert.fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.dir/");
        Assert.assertTrue(entry.isDirectory());
        entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertTrue(entry1.equals(entry2));
        Assert.assertFalse(entry1.equals(entry3));
        Assert.assertFalse(entry1.equals(null));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        long time = System.currentTimeMillis();
        entry.setTime(time);
        Date date = entry.getLastModifiedDate();
        Assert.assertEquals(new Date(time), date);
    }

    @Test
    public void testGetSize() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        long size = 1024;
        entry.setSize(size);
        Assert.assertEquals(size, entry.getSize());
    }

    @Test
    public void testReadData() throws IOException {
        String data = "Hello, World!";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(data.getBytes());
        byte[] bytes = output.toByteArray();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setSize(bytes.length);
        entry.setData(bytes);
        InputStream input = entry.getInputStream();
        byte[] readData = new byte[data.length()];
        int len = input.read(readData);
        Assert.assertEquals(data.length(), len);
        Assert.assertArrayEquals(data.getBytes(), readData);
        IOUtils.closeQuietly(input);
    }

    @Test(expected = IOException.class)
    public void testReadDataNullData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        InputStream input = entry.getInputStream();
        int len = input.read(new byte[1]);
        Assert.fail();
    }

    @Test(expected = ZipException.class)
    public void testSetMethodInvalid() throws IOException, ZipException {
        byte[] bytes = new byte[]{0x04};
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtra(bytes);
        entry.setMethod(4);
    }

    @Test(expected = ZipException.class)
    public void testSetExternalAttributesInvalid() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(Short.MAX_VALUE);
    }
}