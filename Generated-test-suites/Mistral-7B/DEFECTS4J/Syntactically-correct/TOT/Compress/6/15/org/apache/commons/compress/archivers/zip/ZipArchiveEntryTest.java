package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryCreationFromFile() throws IOException {
        File inputFile = new File("testfile.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "testfile.txt");
        assertEquals("testfile.txt", zipArchiveEntry.getName());
        assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testZipArchiveEntryCreationFromZipEntry() throws IOException {
        String name = "testfile.txt";
        byte[] comment = "A simple test file".getBytes();
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setComment(new String(comment));
        InputStream inputStream = new ByteArrayInputStream(comment);
        zipEntry.setExtra(inputStream);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("testfile.txt", zipArchiveEntry.getName());
        assertEquals(comment, zipArchiveEntry.getComment());
        assertEquals(comment.length, zipArchiveEntry.getSize());
        assertEquals(new Date().getTime(), zipArchiveEntry.getTime());
        assertEquals(0, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getPlatform());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryCreationFromZipArchiveEntry() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        ExtraFieldUtils.putExtraField(extraFields, new GeneralPurposeByteHeader(ZipShort.NEW_CRLF, "test".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);
        assertEquals("testfile.txt", zipArchiveEntry.getName());
        assertEquals(internalAttributes, zipArchiveEntry.getInternalAttributes());
        assertEquals(externalAttributes, zipArchiveEntry.getExternalAttributes());
        assertEquals(extraFields, zipArchiveEntry.getExtraFields());
        assertEquals(0, zipArchiveEntry.getPlatform());
        assertEquals(0, zipArchiveEntry.getMethod());

        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
        assertEquals(zipArchiveEntry, clonedZipArchiveEntry);
    }

    @Test
    public void testSetUnixMode() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testfile.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeNumber() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testfile.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testExtraFields() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.COMMENT, new GeneralPurposeStringHeader(ZipShort.COMMENT, "A simple test file".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);
        assertEquals("A simple test file", zipArchiveEntry.getComment());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        zipArchiveEntry.setExtra(outputStream.toByteArray());
        byte[] extraBytes = zipArchiveEntry.getExtra();
        InputStream inputStream = new ByteArrayInputStream(extraBytes);
        ZipArchiveEntry newZipArchiveEntry = new ZipArchiveEntry("testfile.txt", null, null, null);
        newZipArchiveEntry.setExtra(inputStream);
        assertEquals(zipArchiveEntry, newZipArchiveEntry);

        ZipExtraField zipExtraField = zipArchiveEntry.getExtraField(ZipShort.COMMENT);
        assertNotNull(zipExtraField);
        assertEquals("A simple test file", zipExtraField.getStringValue());

        // Test error scenario: unknown header id
        assertNull(zipArchiveEntry.getExtraField(ZipShort.TEST));
    }

    @Test
    public void testSetExtra() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.COMMENT, new GeneralPurposeStringHeader(ZipShort.COMMENT, "A simple test file".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] extraBytes = new byte[]{0x07, 0x08, 0x09};
        outputStream.write(extraBytes);
        zipArchiveEntry.setExtra(outputStream.toByteArray());
        InputStream inputStream = new ByteArrayInputStream(extraBytes);
        ZipArchiveEntry newZipArchiveEntry = new ZipArchiveEntry("testfile.txt", null, null, null);
        newZipArchiveEntry.setExtra(inputStream);
        assertEquals(zipArchiveEntry, newZipArchiveEntry);

        ZipExtraField zipExtraField = zipArchiveEntry.getExtraField(ZipShort.NEW_CRLF);
        assertNull(zipExtraField);

        // Test error scenario: unknown header id
        zipExtraField = zipArchiveEntry.getExtraField(ZipShort.TEST);
        assertNull(zipExtraField);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.COMMENT, new GeneralPurposeStringHeader(ZipShort.COMMENT, "A simple test file".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);

        byte[] extraBytes = new byte[]{0x07, 0x08, 0x09};
        zipArchiveEntry.setCentralDirectoryExtra(extraBytes);
        InputStream inputStream = new ByteArrayInputStream(extraBytes);
        ZipArchiveEntry newZipArchiveEntry = new ZipArchiveEntry("testfile.txt", null, null, null);
        newZipArchiveEntry.setCentralDirectoryExtra(extraBytes);
        assertEquals(zipArchiveEntry, newZipArchiveEntry);

        ZipExtraField zipExtraField = zipArchiveEntry.getExtraField(ZipShort.NEW_CRLF);
        assertNull(zipExtraField);

        // Test error scenario: unknown header id
        zipExtraField = zipArchiveEntry.getExtraField(ZipShort.TEST);
        assertNull(zipExtraField);
    }

    @Test
    public void testGetLocalFileDataExtra() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.COMMENT, new GeneralPurposeStringHeader(ZipShort.COMMENT, "A simple test file".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] extraBytes = new byte[]{0x07, 0x08, 0x09};
        outputStream.write(extraBytes);
        zipArchiveEntry.setExtra(outputStream.toByteArray());

        byte[] localExtraBytes = zipArchiveEntry.getLocalFileDataExtra();
        assertEquals(extraBytes, localExtraBytes);
    }

    @Test
    public void testGetCentralDirectoryExtra() throws IOException {
        String name = "testfile.txt";
        int internalAttributes = 0;
        long externalAttributes = 123456789012L;
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.COMMENT, new GeneralPurposeStringHeader(ZipShort.COMMENT, "A simple test file".getBytes()));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name, internalAttributes, externalAttributes, extraFields);

        byte[] extraBytes = new byte[]{0x07, 0x08, 0x09};
        zipArchiveEntry.setCentralDirectoryExtra(extraBytes);

        byte[] centralDirectoryExtraBytes = zipArchiveEntry.getCentralDirectoryExtra();
        assertEquals(extraBytes, centralDirectoryExtraBytes);
    }

    @Test
    public void testIsSupportedCompressionMethod() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testfile.txt");
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipArchiveEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipArchiveEntry.UNKNOWN);
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }
}