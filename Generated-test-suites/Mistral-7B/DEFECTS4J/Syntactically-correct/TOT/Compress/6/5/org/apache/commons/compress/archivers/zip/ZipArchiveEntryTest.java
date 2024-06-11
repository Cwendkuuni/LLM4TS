package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiveEntryTest {

    private static final String TEST_FILE = "test.txt";
    private File inputFile;

    @Before
    public void setUp() throws IOException {
        inputFile = new File(TEST_FILE);
        if (inputFile.exists()) {
            inputFile.delete();
        }
        inputFile.createNewFile();
        IOUtils.write("Hello World", inputFile, null);
    }

    @Test
    public void testCreateEntryFromFile() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, TEST_FILE);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(inputFile.length(), entry.getSize());
        Assert.assertEquals(new Date(inputFile.lastModified()), entry.getTime());
    }

    @Test
    public void testCreateEntryFromZipEntry() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.zip");
        ZipFile zipFile = new ZipFile(inputStream);
        ZipEntry zipEntry = zipFile.getEntry("test.txt");
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertEquals(ZipEntry.STORED, archiveEntry.getMethod());
        Assert.assertNull(archiveEntry.getInternalAttributes());
        Assert.assertNull(archiveEntry.getExternalAttributes());
        Assert.assertNull(archiveEntry.getExtraFields());
    }

    @Test(expected = ZipException.class)
    public void testCreateEntryWithInvalidName() {
        new ZipArchiveEntry("", TEST_FILE);
    }

    @Test(expected = ZipException.class)
    public void testCreateEntryWithNullName() {
        new ZipArchiveEntry(null, TEST_FILE);
    }

    @Test
    public void testClone() throws IOException {
        ZipArchiveEntry original = new ZipArchiveEntry(inputFile, TEST_FILE);
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        Assert.assertEquals(original.getName(), cloned.getName());
        Assert.assertEquals(original.getSize(), cloned.getSize());
        Assert.assertEquals(original.getTime(), cloned.getTime());
        Assert.assertEquals(original.getMethod(), cloned.getMethod());
        Assert.assertArrayEquals(original.getExtra(), cloned.getExtra());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setUnixMode(0755);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetUnixModeWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(123456789);
        Assert.assertEquals(0, entry.getUnixMode());
    }

    @Test
    public void testGetAndSetMethod() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.close();
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(12345);
    }

    @Test(expected = ZipException.class)
    public void testSetExtraWithInvalidData() throws IOException {
        byte[] invalidData = {0x01, (byte) 0xFF};
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        try {
            entry.setExtra(invalidData);
        } catch (ZipException e) {
            Assert.assertEquals("Invalid extra data: unexpected end of data", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSetExtraWithValidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] validData = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
        entry.setExtra(validData);
        Assert.assertArrayEquals(validData, entry.getExtra());
    }

    @Test
    public void testSetExtraWithValidDataParsed() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] validData = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
        entry.setExtra(validData);
        ZipExtraField extraField = entry.getExtraFields()[0];
        Assert.assertEquals(ZipShort.LOCAL_FILE_COMMENT, extraField.getHeaderId());
        Assert.assertEquals("test.txt", new String(extraField.getByteData(), 0, extraField.getByteData().length));
    }

    @Test
    public void testAddRemoveExtraField() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(new ZipExtraField[]{new ZipExtendedInformation("Test", "Test Data")});
        Assert.assertArrayEquals(1, entry.getExtraFields().length);
        entry.removeExtraField(ZipShort.LOCAL_FILE_COMMENT);
        Assert.assertArrayEquals(0, entry.getExtraFields().length);
    }

    @Test
    public void testAddExtraFieldWithValidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] validData = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
        entry.addExtraField(new ZipExtendedInformation("Test", "Test Data"));
        Assert.assertArrayEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraFields()[0];
        Assert.assertEquals(ZipShort.LOCAL_FILE_COMMENT, extraField.getHeaderId());
        Assert.assertEquals("Test", extraField.getCommentName());
        Assert.assertEquals("Test Data", extraField.getCommentValue());
        Assert.assertArrayEquals(validData, extraField.getByteData());
    }

    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("ZipArchiveEntry{name='test.txt'}", entry.toString());
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
        Assert.assertTrue(entry1.equals(entry2));
        Assert.assertFalse(entry1.equals(new Object()));
    }

    @After
    public void tearDown() throws IOException {
        if (inputFile.exists()) {
            inputFile.delete();
        }
    }
}