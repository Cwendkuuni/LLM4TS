package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndGetValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1024);
        ZipLong diskStart = new ZipLong(123456);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                originalSize, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[8 * DWORD];
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(100);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);

        System.arraycopy(expectedOriginalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, localFileData, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] localFileData = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        byte[] localFileData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] centralDirectoryData = new byte[3 * DWORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidData() {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(100);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(1024);
        ZipLong expectedDiskStart = new ZipLong(123456);

        System.arraycopy(expectedOriginalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(expectedRelativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(expectedDiskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(100);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(1024);
        ZipLong expectedDiskStart = new ZipLong(123456);

        System.arraycopy(expectedOriginalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(expectedRelativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(expectedDiskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, false, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, true, false);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, false, false);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), extraField.getDiskStartNumber());
    }
}