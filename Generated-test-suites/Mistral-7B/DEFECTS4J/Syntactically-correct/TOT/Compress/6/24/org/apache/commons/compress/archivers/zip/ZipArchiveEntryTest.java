package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testNewEntryWithValidName() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertNull(entry.getName());
        Assert.assertEquals(false, entry.isDirectory());
    }

    @Test
    public void testNewEntryWithValidNameAndFile() {
        String name = "test.txt";
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(file.length(), entry.getSize());
        Assert.assertEquals(file.lastModified(), entry.getTime());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertNull(entry.getName());
        Assert.assertEquals(file.isDirectory(), entry.isDirectory());
    }

    @Test
    public void testNewEntryWithValidNameAndInputStream() {
        String name = "test.txt";
        byte[] data = "Test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveEntry entry = new ZipArchiveEntry(name, inputStream);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(data.length, entry.getSize());
        Assert.assertEquals(new Date(), entry.getTime());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertNull(entry.getName());
        Assert.assertEquals(false, entry.isDirectory());
    }

    @Test
    public void testNewEntryWithNullName() {
        try {
            new ZipArchiveEntry(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Name can't be null", e.getMessage());
        }
    }

    @Test
    public void testNewEntryWithEmptyName() {
        try {
            new ZipArchiveEntry("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Name can't be empty", e.getMessage());
        }
    }

    @Test
    public void testNewEntryWithValidNameAndExtraFields() {
        String name = "test.txt";
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.UNIX_USER, "user1");
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.UNIX_GROUP, "group1");
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.UNIX_USER, extraField1);
        extraFields.put(ZipShort.UNIX_GROUP, extraField2);
        ZipArchiveEntry entry = new ZipArchiveEntry(name, null, extraFields);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertEquals(extraFields, entry.getExtraFields());
        Assert.assertNull(entry.getName());
        Assert.assertEquals(false, entry.isDirectory());
    }

    @Test
    public void testNewEntryWithValidNameAndExtraFieldsAndData() {
        String name = "test.txt";
        byte[] data = "Test data".getBytes();
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.UNIX_USER, "user1");
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.UNIX_GROUP, "group1");
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.UNIX_USER, extraField1);
        extraFields.put(ZipShort.UNIX_GROUP, extraField2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ExtraFieldUtils.writeFields(byteArrayOutputStream, extraFields);
        } catch (IOException e) {
            Assert.fail();
        }
        ZipArchiveEntry entry = new ZipArchiveEntry(name, null, extraFields, byteArrayOutputStream.toByteArray());
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(data.length, entry.getSize());
        Assert.assertEquals(new Date(), entry.getTime());
        Assert.assertEquals(extraFields, entry.getExtraFields());
        Assert.assertNull(entry.getName());
        Assert.assertEquals(false, entry.isDirectory());
    }

    @Test
    public void testClone() throws IOException, CloneNotSupportedException {
        String name = "test.txt";
        byte[] data = "Test data".getBytes();
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.UNIX_USER, "user1");
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.UNIX_GROUP, "group1");
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.UNIX_USER, extraField1);
        extraFields.put(ZipShort.UNIX_GROUP, extraField2);
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(name, inputStream, extraFields);
        ZipArchiveEntry cloneEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertEquals(name, cloneEntry.getName());
        Assert.assertEquals(-1, cloneEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), cloneEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getPlatform(), cloneEntry.getPlatform());
        Assert.assertEquals(originalEntry.getExternalAttributes(), cloneEntry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), cloneEntry.getExtraFields());
        Assert.assertEquals(originalEntry.getSize(), cloneEntry.getSize());
        Assert.assertEquals(originalEntry.getTime(), cloneEntry.getTime());
        Assert.assertArrayEquals(originalEntry.getLocalFileDataExtra(), cloneEntry.getLocalFileDataExtra());
        Assert.assertEquals(originalEntry.isDirectory(), cloneEntry.isDirectory());
    }
}