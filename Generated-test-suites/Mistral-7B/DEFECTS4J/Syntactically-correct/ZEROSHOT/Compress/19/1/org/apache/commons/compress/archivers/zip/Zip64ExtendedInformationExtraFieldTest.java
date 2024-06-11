package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

import org.junit.Test;

/**
 * Test class for Zip64ExtendedInformationExtraField.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test constructor with only size and compressedSize.
     */
    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test constructor with all fields.
     */
    @Test
    public void testConstructorWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 9876);
        ZipLong diskStart = new ZipLong((long) 7654321);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size,
                                                                                               compressedSize,
                                                                                               relativeHeaderOffset,
                                                                                               diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test getLocalFileDataLength().
     */
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        assertEquals(new ZipShort(DWORD), extraField.getLocalFileDataLength());
        extraField.setSize(new ZipEightByteInteger(DWORD));
        extraField.setCompressedSize(new ZipEightByteInteger(DWORD));
        assertEquals(new ZipShort(2 * DWORD), extraField.getLocalFileDataLength());
    }

    /**
     * Test getCentralDirectoryLength().
     */
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        assertEquals(new ZipShort(0), extraField.getCentralDirectoryLength());
        extraField.setSize(new ZipEightByteInteger(DWORD));
        extraField.setCompressedSize(new ZipEightByteInteger(DWORD));
        assertEquals(new ZipShort(3 * DWORD), extraField.getCentralDirectoryLength());

        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(DWORD));
        assertEquals(new ZipShort(4 * DWORD), extraField.getCentralDirectoryLength());

        extraField.setDiskStartNumber(new ZipLong(WORD));
        assertEquals(new ZipShort(5 * DWORD + WORD), extraField.getCentralDirectoryLength());
    }

    /**
     * Test getLocalFileDataData().
     */
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(DWORD),
                                                                                                 new ZipEightByteInteger(DWORD));

        byte[] result = extraField.getLocalFileDataData();
        byte[] expected = new byte[2 * DWORD];

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    /**
     * Test getCentralDirectoryData().
     */
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(DWORD),
                                                                                                 new ZipEightByteInteger(DWORD),
                                                                                                 new ZipEightByteInteger(DWORD),
                                                                                                 new ZipLong(WORD));

        byte[] result = extraField.getCentralDirectoryData();
        byte[] expected = new byte[3 * DWORD + WORD];

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    /**
     * Test parseFromLocalFileData().
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        // No data at all
        extraField.parseFromLocalFileData(new byte[0], 0, 0);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for size and compressedSize only
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        extraField.parseFromLocalFileData(buffer, 0, 2 * DWORD);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for all fields
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        new ZipEightByteInteger(DWORD).setValue(buffer, 8);
        new ZipLong(WORD).setValue(buffer, 12);
        extraField.parseFromLocalFileData(buffer, 0, 2 * DWORD + WORD);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(WORD), extraField.getDiskStartNumber());
    }

    /**
     * Test parseFromCentralDirectoryData().
     */
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        // No data at all
        extraField.parseFromCentralDirectoryData(buffer, 0, 0);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for size and compressedSize only
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        extraField.parseFromCentralDirectoryData(buffer, 0, 2 * DWORD);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for all fields
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        new ZipEightByteInteger(DWORD).setValue(buffer, 8);
        new ZipLong(WORD).setValue(buffer, 12);
        extraField.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(WORD), extraField.getDiskStartNumber());
    }

    /**
     * Test reparseCentralDirectoryData().
     */
    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        // No data at all
        extraField.parseFromCentralDirectoryData(buffer, 0, 0);
        extraField.reparseCentralDirectoryData(false, false, false, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for size and compressedSize only
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        extraField.parseFromCentralDirectoryData(buffer, 0, 2 * DWORD);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Data for all fields
        new ZipEightByteInteger(DWORD).setValue(buffer, 0);
        new ZipEightByteInteger(DWORD).setValue(buffer, 4);
        new ZipEightByteInteger(DWORD).setValue(buffer, 8);
        new ZipLong(WORD).setValue(buffer, 12);
        extraField.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(DWORD), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(WORD), extraField.getDiskStartNumber());
    }
}