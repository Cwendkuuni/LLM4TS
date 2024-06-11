package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test creating a new {@link Zip64ExtendedInformationExtraField} instance
     * with valid original size and compressed size.
     */
    @Test
    public void testCreateWithValidSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((int) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512);
        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    /**
     * Test creating a new {@link Zip64ExtendedInformationExtraField} instance
     * with null original size or compressed size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullSizes() {
        ZipEightByteInteger originalSize = null;
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
    }

    /**
     * Test parsing valid local file data.
     */
    @Test
    public void testParseValidLocalFileData() {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03,
                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x05,
                (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x07,
                (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x09};

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(new ZipEightByteInteger((int) 0x02000005), extraField.getSize());
        assertEquals(new ZipEightByteInteger((int) 0x01000004), extraField.getCompressedSize());
    }

    /**
     * Test parsing invalid local file data.
     */
    @Test(expected = ZipException.class)
    public void testParseInvalidLocalFileData() {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03};

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    /**
     * Test creating a new {@link Zip64ExtendedInformationExtraField} instance
     * from central directory data.
     */
    @Test
    public void testCreateFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02,
                (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06,
                (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a,
                (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e,
                (byte) 0x0f, (byte) 0x10, (byte) 0x11, (byte) 0x12,
                (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16,
                (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1a,
                (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e};

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);

        assertEquals(new ZipEightByteInteger((int) 0x12345678), extraField.getSize());
        assertEquals(new ZipEightByteInteger((int) 0x11111111), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger((int) 0x0abcdef0), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong((int) 0x1234), extraField.getDiskStartNumber());
    }

    /**
     * Test parsing central directory data with missing fields.
     */
    @Test(expected = ZipException.class)
    public void testParseMissingFieldsCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02,
                (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06,
                (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a};

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);
    }

    /**
     * Test parsing central directory data with all fields.
     */
    @Test
    public void testParseAllFieldsCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[]{(byte) 0x01, (byte) 0x02,
                (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06,
                (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a,
                (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e,
                (byte) 0x0f, (byte) 0x10, (byte) 0x11, (byte) 0x12,
                (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16,
                (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1a,
                (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e,
                (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
                (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);

        assertEquals(new ZipEightByteInteger((int) 0x12345678), extraField.getSize());
        assertEquals(new ZipEightByteInteger((int) 0x11111111), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger((int) 0x0abcdef0), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong((int) 0x1234), extraField.getDiskStartNumber());
    }
}