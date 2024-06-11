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
     * Test constructor with both sizes present.
     */
    @Test
    public void testConstructorBothSizesPresent() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test constructor with only compressed size present.
     */
    @Test
    public void testConstructorCompressedSizePresent() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, compressedSize);
        assertNull(extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test constructor with all fields present.
     */
    @Test
    public void testConstructorAllFieldsPresent() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x11223344);
        ZipLong diskStart = new ZipLong(0x55aa55aa55aaL);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[]{
            0x00, 0x01, 0x00, 0x00, // header ID
            0x00, 0x00, 0x12, 0x34, 0x56, 0x78, 0x00, 0x00, // original size
            0x00, 0x00, 0x87, 0x65, 0x43, 0x21, 0x00, 0x00, // compressed size
            0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x00, 0x00, 0x55, 0xaa, 0x55, 0xaa, 0x55, 0xaa // relative header offset and disk start
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(new ZipEightByteInteger((short) 0x12345678), extraField.getSize());
        assertEquals(new ZipEightByteInteger((short) 0x87654321), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger((short) 0x11223344), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x55aa55aa55aaL), extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data with all fields present.
     */
    @Test
    public void testParseFromCentralDirectoryDataAllFieldsPresent() {
        byte[] centralDirectoryData = new byte[]{(byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x56, 0x78, 0x00, 0x00, 0x08, 0x00, 0x00, 0x87, 0x65, 0x43, 0x21,
            0x00, 0x00, 0x04, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x00, 0x00, 0x04, 0x00, 0x00, 0x55, 0xaa, 0x55, 0xaa, 0x55, 0xaa};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(new ZipEightByteInteger((short) 0x12345678), extraField.getSize());
        assertEquals(new ZipEightByteInteger((short) 0x87654321), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger((short) 0x11223344), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x55aa55aa55aaL), extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data with only compressed size present.
     */
    @Test
    public void testParseFromCentralDirectoryDataCompressedSizePresent() {
        byte[] centralDirectoryData = new byte[]{(byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x87, 0x65, 0x43, 0x21, 0x00, 0x00, 0x04, 0x00, 0x00, 0x55, 0xaa, 0x55, 0xaa};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(null, extraField.getSize());
        assertEquals(new ZipEightByteInteger((short) 0x87654321), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test getters and setters.
     */
    @Test
    public void testGettersAndSetters() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        extraField.setSize(originalSize);
        assertEquals(originalSize, extraField.getSize());
        extraField.setCompressedSize(compressedSize);
        assertEquals(compressedSize, extraField.getCompressedSize());
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x11223344));
        assertEquals(new ZipEightByteInteger((short) 0x11223344), extraField.getRelativeHeaderOffset());
        extraField.setDiskStartNumber(new ZipLong(0x55aa55aa55aaL));
        assertEquals(new ZipLong(0x55aa55aa55aaL), extraField.getDiskStartNumber());
    }
}