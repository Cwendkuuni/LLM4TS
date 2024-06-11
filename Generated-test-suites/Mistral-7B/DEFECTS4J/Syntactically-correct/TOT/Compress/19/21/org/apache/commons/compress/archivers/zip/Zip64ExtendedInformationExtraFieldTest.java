package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

import org.junit.Test;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /** Test data for positive cases. */
    @Test
    public void testPositiveCases() {
        ZipShort headerId = ZipShort.valueOf(0x0001);

        // Test empty case
        Zip64ExtendedInformationExtraField empty = new Zip64ExtendedInformationExtraField();
        assertEquals(headerId, empty.getHeaderId());
        assertEquals(0, empty.getLocalFileDataLength().getValue());
        assertEquals(0, empty.getCentralDirectoryLength().getValue());
        assertEquals(0, empty.getLocalFileDataData().length);
        assertEquals(0, empty.getCentralDirectoryData().length);

        // Test case with both sizes
        ZipEightByteInteger size = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x9abcdef0L);
        Zip64ExtendedInformationExtraField bothSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(headerId, bothSizes.getHeaderId());
        assertEquals(2 * DWORD, bothSizes.getLocalFileDataLength().getValue());
        assertEquals(2 * DWORD + DWORD, bothSizes.getCentralDirectoryLength().getValue());
        assertEquals(2 * DWORD, bothSizes.getLocalFileDataData().length);
        assertEquals(2 * DWORD + DWORD, bothSizes.getCentralDirectoryData().length);

        // Test case with only sizes
        Zip64ExtendedInformationExtraField onlySizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(headerId, onlySizes.getHeaderId());
        assertEquals(2 * DWORD, onlySizes.getLocalFileDataLength().getValue());
        assertEquals(2 * DWORD + DWORD, onlySizes.getCentralDirectoryLength().getValue());
        assertEquals(2 * DWORD, onlySizes.getLocalFileDataData().length);
        assertEquals(2 * DWORD + DWORD, onlySizes.getCentralDirectoryData().length);

        // Test case with all fields
        ZipEightByteInteger rho = new ZipEightByteInteger(0x1234L);
        ZipLong ds = new ZipLong(0x1234567890L);
        Zip64ExtendedInformationExtraField allFields = new Zip64ExtendedInformationExtraField(size, compressedSize, rho, ds);
        assertEquals(headerId, allFields.getHeaderId());
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, allFields.getCentralDirectoryLength().getValue());
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, allFields.getCentralDirectoryData().length);

        assertNotNull(allFields.getSize());
        assertEquals(size.getValue(), allFields.getSize().getValue());
        assertNotNull(allFields.getCompressedSize());
        assertEquals(compressedSize.getValue(), allFields.getCompressedSize().getValue());
        assertNotNull(allFields.getRelativeHeaderOffset());
        assertEquals(rho.getValue(), allFields.getRelativeHeaderOffset().getValue());
        assertNotNull(allFields.getDiskStartNumber());
        assertEquals(ds.getValue(), allFields.getDiskStartNumber().getValue());
    }

    /** Test data for negative cases. */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCases() {
        Zip64ExtendedInformationExtraField empty = new Zip64ExtendedInformationExtraField();
        empty.setSize(null);

        ZipEightByteInteger size = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = null;
        Zip64ExtendedInformationExtraField onlyCompressedSizeIsNull = new Zip64ExtendedInformationExtraField(size, compressedSize);

        ZipEightByteInteger rho = null;
        Zip64ExtendedInformationExtraField onlyRelativeHeaderOffsetIsNull = new Zip64ExtendedInformationExtraField(size, compressedSize, rho);

        ZipLong ds = null;
        Zip64ExtendedInformationExtraField onlyDiskStartNumberIsNull = new Zip64ExtendedInformationExtraField(size, compressedSize, rho, ds);

        Zip64ExtendedInformationExtraField noSizesInLocalFileHeader = new Zip64ExtendedInformationExtraField();
        noSizesInLocalFileHeader.parseFromLocalFileData(new byte[0], 0, 0);
    }
}