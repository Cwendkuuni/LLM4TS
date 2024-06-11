package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithSizeAndCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024), new ZipEightByteInteger(512));
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(new ZipEightByteInteger(1024), actualSize);
        assertEquals(new ZipEightByteInteger(512), actualCompressedSize);
    }

    @Test
    public void testNewInstanceWithAllFields() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024), new ZipEightByteInteger(512),
                new ZipEightByteInteger(4096), new ZipLong(0x1234567890L));
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        ZipEightByteInteger actualRelativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong actualDiskStart = extraField.getDiskStartNumber();
        assertEquals(new ZipEightByteInteger(1024), actualSize);
        assertEquals(new ZipEightByteInteger(512), actualCompressedSize);
        assertEquals(new ZipEightByteInteger(4096), actualRelativeHeaderOffset);
        assertEquals(new ZipLong(0x1234567890L), actualDiskStart);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullSize() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullCompressedSize() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024), null);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidDataLength() {
        byte[] localFileData = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(localFileData, 0, 0));
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithLessThanTwoDwords() {
        byte[] localFileData = new byte[2];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(localFileData, 0, 2));
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] localFileData = new byte[8];
        ZipShort sizeShort = new ZipShort(new ZipEightByteInteger(1024).getValue());
        ZipShort compressedSizeShort = new ZipShort(new ZipEightByteInteger(512).getValue());
        System.arraycopy(sizeShort.getBytes(), 0, localFileData, 0, 2);
        System.arraycopy(compressedSizeShort.getBytes(), 0, localFileData, 2, 2);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, 8);
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(new ZipEightByteInteger(1024), actualSize);
        assertEquals(new ZipEightByteInteger(512), actualCompressedSize);
    }
}