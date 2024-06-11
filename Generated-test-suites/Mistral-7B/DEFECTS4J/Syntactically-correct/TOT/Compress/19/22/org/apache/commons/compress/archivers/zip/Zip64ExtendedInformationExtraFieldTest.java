package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField()}.
     */
    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField empty = new Zip64ExtendedInformationExtraField();
        assertNull(empty.getSize());
        assertNull(empty.getCompressedSize());
        assertNull(empty.getRelativeHeaderOffset());
        assertNull(empty.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger)}.
     */
    @Test
    public void testSizesConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        Zip64ExtendedInformationExtraField sizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, sizes.getSize());
        assertEquals(compressedSize, sizes.getCompressedSize());
        assertNull(sizes.getRelativeHeaderOffset());
        assertNull(sizes.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger, ZipEightByteInteger, ZipLong)}.
     */
    @Test
    public void testAllFieldsConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x2000);
        ZipLong diskStartNumber = new ZipLong(0x3000);
        Zip64ExtendedInformationExtraField allFields = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        assertEquals(size, allFields.getSize());
        assertEquals(compressedSize, allFields.getCompressedSize());
        assertEquals(relativeHeaderOffset, allFields.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, allFields.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getHeaderId()}.
     */
    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataLength()}.
     */
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getLocalFileDataLength());

        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD), field.getLocalFileDataLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryLength()}.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCentralDirectoryLength());

        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(ZipShort.valueOf((size.getValue() + compressedSize.getValue())
                + (size != null ? DWORD : 0)
                + (compressedSize != null ? DWORD : 0)
                + (size != null && compressedSize != null ? DWORD : 0)
                + (size != null && compressedSize != null && relativeHeaderOffset != null ? DWORD : 0)
                + (size != null && compressedSize != null && diskStart != null ? WORD : 0)), field.getCentralDirectoryLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataData()}.
     */
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getLocalFileDataData());

        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = field.getLocalFileDataData();
        assertArrayEquals(new byte[]{0, 0, 0, (byte) 0xdd, 0, 0, 0, (byte) 0xee,
                0, 0, 0, (byte) 0xff, 0, 0, 0, (byte) 0x10}, data);
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryData()}.
     */
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getCentralDirectoryData());

        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = field.getCentralDirectoryData();
        assertArrayEquals(new byte[]{0, 0, 0, (byte) 0xdd, 0, 0, 0, (byte) 0xee,
                0, 0, 0, (byte) 0xff, 0, 0, 0, (byte) 0x10, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0}, data);
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{0, 0, 0, (byte) 0xdd, 0, 0, 0, (byte) 0xee,
                0, 0, 0, (byte) 0xff, 0, 0, 0, (byte) 0x10, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        ZipEightByteInteger expectedSize = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(0x8000);
        assertEquals(expectedSize, field.getSize());
        assertEquals(expectedCompressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[]{0, 0, 0, (byte) 0xdd, 0, 0, 0, (byte) 0xee,
                0, 0, 0, (byte) 0xff, 0, 0, 0, (byte) 0x10};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());

        data = new byte[]{0, 0, 0, (byte) 0xdd, 0, 0, 0, (byte) 0xee,
                0, 0, 0, (byte) 0xff, 0, 0, 0, (byte) 0x10, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipEightByteInteger size = new ZipEightByteInteger(0x10000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x8000);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x2000);
        ZipLong diskStartNumber = new ZipLong(0x3000);
        field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }
}