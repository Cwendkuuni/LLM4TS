package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test for {@link ZipArchiveEntry} class.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testBasicConstruction() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNull(entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testConstructionFromZipEntry() throws Exception {
        String name = "test.txt";
        byte[] extra = new byte[]{0x07, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setExtra(extra);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        ZipExtraField extraField = new ZipGeneralPurposeByteHeader(0x07, 0x01);
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.ZIP_GEN_PURP_BYTES, extraField);
        Assert.assertEquals(extraFields.toArray(new ZipExtraField[0]), entry.getExtraFields());
    }

    @Test
    public void testConstructionFromAnotherZipArchiveEntry() throws Exception {
        String name = "test.txt";
        byte[] extra = new byte[]{0x07, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(name);
        originalEntry.setInternalAttributes(1);
        originalEntry.setExternalAttributes(1);
        originalEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        originalEntry.addExtraField(new ZipGeneralPurposeByteHeader(0x07, 0x01));

        ZipArchiveEntry entry = new ZipArchiveEntry(originalEntry);

        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(originalEntry.getMethod(), entry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), entry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getPlatform(), entry.getPlatform());
        Assert.assertEquals(originalEntry.getExternalAttributes(), entry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), entry.getExtraFields());
    }

    @Test
    public void testConstructionFromFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(file.length(), entry.getSize());
        Assert.assertEquals(new Date(file.lastModified()), entry.getLastModifiedDate());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeNumber() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(-1);
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED | ZipEntry.COMPRESSION_METHOD_UNCOMPRESSED_OTHER);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.UNCOMPRESSED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.BZIP2);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());

        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.UNCOMPRESSED);
        Assert.assertEquals(ZipEntry.UNCOMPRESSED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getInternalAttributes());

        entry.setInternalAttributes(0xFFFF);
        Assert.assertEquals(0xFFFF, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x0102);
        Assert.assertEquals(0x0102, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getExternalAttributes());

        entry.setExternalAttributes(0x01020304);
        Assert.assertEquals(0x01020304, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x01020304);
        Assert.assertEquals(0x01020304, entry.getExternalAttributes());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] extraFields = {
                new ZipGeneralPurposeByteHeader(0x07, 0x01),
                new ZipGeneralPurposeStringHeader("test1"),
                new ZipSpecificAppTeXTag(0x01)
        };
        entry.setExtraFields(extraFields);
        Assert.assertEquals(extraFields.length, entry.getExtraFields().length);
        for (int i = 0; i < extraFields.length; i++) {
            ZipExtraField expectedExtraField = extraFields[i];
            ZipExtraField actualExtraField = entry.getExtraField(expectedExtraField.getHeaderId());
            Assert.assertEquals(expectedExtraField, actualExtraField);
        }

        ByteArrayOutputStream extraDataStream = new ByteArrayOutputStream();
        ExtraFieldUtils.writeFields(extraDataStream, extraFields);
        entry.setExtra(extraDataStream.toByteArray());
        Assert.assertArrayEquals(extraDataStream.toByteArray(), entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField expectedExtraField = new ZipGeneralPurposeByteHeader(0x07, 0x01);
        entry.setExtraField(expectedExtraField.getHeaderId(), expectedExtraField);
        ZipExtraField actualExtraField = entry.getExtraField(expectedExtraField.getHeaderId());
        Assert.assertEquals(expectedExtraField, actualExtraField);
    }

    @Test
    public void testSetAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] extraFields = {
                new ZipGeneralPurposeByteHeader(0x07, 0x01),
                new ZipGeneralPurposeStringHeader("test1"),
                new ZipSpecificAppTeXTag(0x01)
        };
        entry.addAsFirstExtraField(extraFields[1]);
        Assert.assertEquals(extraFields[1].getHeaderId(), entry.getExtraFields()[0].getHeaderId());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] extraFields = {
                new ZipGeneralPurposeByteHeader(0x07, 0x01),
                new ZipGeneralPurposeStringHeader("test1"),
                new ZipSpecificAppTeXTag(0x01)
        };
        entry.addExtraField(extraFields[0]);
        entry.addExtraField(extraFields[1]);
        entry.addExtraField(extraFields[2]);

        entry.removeExtraField(ZipShort.ZIP_GEN_PURP_STRING);
        Assert.assertEquals(2, entry.getExtraFields().length);

        entry.removeExtraField(ZipShort.ZIP_SPEC_APP_TE_X);
        Assert.assertEquals(1, entry.getExtraFields().length);

        entry.removeExtraField(ZipShort.ZIP_GEN_PURP_BYTES);
        Assert.assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
    }
}