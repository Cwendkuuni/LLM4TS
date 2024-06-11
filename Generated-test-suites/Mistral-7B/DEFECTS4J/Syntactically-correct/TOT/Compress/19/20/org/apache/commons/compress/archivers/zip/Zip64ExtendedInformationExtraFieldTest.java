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
     * Test constructor with valid sizes.
     */
    @Test
    public void testValidSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(54321098L);
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    /**
     * Test constructor with null sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullSizes() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(1234567890L);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(543210980L);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
    }

    /**
     * Test parsing from local file data with insufficient data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInsufficientData() {
        byte[] localFileData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(localFileData, 0,
                                                                        localFileData.length);
    }

    /**
     * Test parsing from central directory data.
     */
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x20, (byte) 0x21,
            (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27,
            (byte) 0x28, (byte) 0x29};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);

        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(1234567890L);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(543210980L);
        ZipEightByteInteger expectedRelativeHeaderOffset =
            new ZipEightByteInteger(3 * DWORD);
        ZipLong expectedDiskStart = new ZipLong(0x1234567890123456L);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data with insufficient data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataInsufficientData() {
        byte[] centralDirectoryData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                                                 centralDirectoryData.length);
    }

    /**
     * Test re-parsing central directory data.
     */
    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x20, (byte) 0x21,
            (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27,
            (byte) 0x28, (byte) 0x29};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);

        extraField.reparseCentralDirectoryData(true, true, true, true);

        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(1234567890L);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(543210980L);
        ZipEightByteInteger expectedRelativeHeaderOffset =
            new ZipEightByteInteger(3 * DWORD);
        ZipLong expectedDiskStart = new ZipLong(0x1234567890123456L);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test re-parsing central directory data with missing fields.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataMissingFields() {
        byte[] centralDirectoryData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);

        extraField.reparseCentralDirectoryData(false, true, true, true);
    }
}