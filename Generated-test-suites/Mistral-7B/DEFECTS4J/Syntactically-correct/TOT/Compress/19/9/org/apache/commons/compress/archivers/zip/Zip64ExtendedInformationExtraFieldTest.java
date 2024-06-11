package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipExtendedDataEditor;
import org.apache.commons.compress.archivers.zip.ZipExtendedDataInputStream;
import org.apache.commons.compress.archivers.zip.ZipExtendedDataOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test case for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test creating a new instance with both sizes.
     */
    @org.junit.Test
    public void testNewInstanceWithBothSizes() {
        long originalSize = 12345;
        long compressedSize = 6789;
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(originalSize),
                new ZipEightByteInteger(compressedSize));
        assertNotNull(extendedField);
        assertEquals(originalSize, extendedField.getSize().getValue());
        assertEquals(compressedSize, extendedField.getCompressedSize().getValue());
    }

    /**
     * Test creating a new instance with only compressed size.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithOnlyCompressedSize() {
        long compressedSize = 6789;
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(compressedSize));
    }

    /**
     * Test creating a new instance with only original size.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithOnlyOriginalSize() {
        long originalSize = 12345;
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(originalSize), null);
    }

    /**
     * Test parsing local file data.
     */
    @org.junit.Test
    public void testParseLocalFileData() throws IOException {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789);
        addSizes(localFileData);
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField();
        extendedField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize.getValue(), extendedField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extendedField.getCompressedSize().getValue());
    }

    /**
     * Test parsing central directory data.
     */
    @org.junit.Test
    public void testParseCentralDirectoryData() throws IOException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        long originalSize = 12345;
        long compressedSize = 6789;
        addSizes(centralDirectoryData);
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField();
        extendedField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize, extendedField.getSize().getValue());
        assertEquals(compressedSize, extendedField.getCompressedSize().getValue());
    }

    /**
     * Test re-parsing central directory data.
     */
    @org.junit.Test
    public void testReparseCentralDirectoryData() throws IOException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        long originalSize = 12345;
        long compressedSize = 6789;
        addSizes(centralDirectoryData);
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField();
        extendedField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipExtendedDataOutputStream out = new ZipExtendedDataOutputStream(baos);
        extendedField.writeTo(out);
        byte[] reParsedData = baos.toByteArray();
        extendedField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(originalSize, extendedField.getSize().getValue());
        assertEquals(compressedSize, extendedField.getCompressedSize().getValue());
    }

    /**
     * Test re-parsing central directory data with missing fields.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingFields()
            throws IOException {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField();
        extendedField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extendedField.reparseCentralDirectoryData(true, false, false, false);
    }

    /**
     * Test writing to a ZipFile.
     */
    @org.junit.Test
    public void testWriteToZipFile() throws IOException {
        long originalSize = 12345;
        long compressedSize = 6789;
        Zip64ExtendedInformationExtraField extendedField =
            new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(originalSize),
                new ZipEightByteInteger(compressedSize));
        try (ZipFile zipFile = new ZipFile("test.zip")) {
            ZipEntry entry = new ZipEntry("test.txt");
            ZipOutputStream out = new ZipOutputStream(zipFile.getOutputStream(entry));
            ZipExtendedDataOutputStream extendedOut =
                new ZipExtendedDataOutputStream(out);
            extendedField.writeTo(extendedOut);
            out.close();
            ZipExtendedDataInputStream extendedIn =
                new ZipExtendedDataInputStream(new ByteArrayInputStream(
                    zipFile.getInputStream(entry).readAllBytes()));
            Zip64ExtendedInformationExtraField readExtendedField =
                new Zip64ExtendedInformationExtraField();
            readExtendedField.parseFromCentralDirectoryData(
                    extendedIn.readAllBytes(), 0, extendedIn.available());
            assertEquals(originalSize, readExtendedField.getSize().getValue());
            assertEquals(compressedSize, readExtendedField.getCompressedSize().getValue());
        }
    }

    /**
     * Helper method to add sizes to a byte array.
     *
     * @param data the data to add sizes to
     */
    private void addSizes(byte[] data) {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789);
        int off = 0;
        if (originalSize != null) {
            System.arraycopy(originalSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
    }
}