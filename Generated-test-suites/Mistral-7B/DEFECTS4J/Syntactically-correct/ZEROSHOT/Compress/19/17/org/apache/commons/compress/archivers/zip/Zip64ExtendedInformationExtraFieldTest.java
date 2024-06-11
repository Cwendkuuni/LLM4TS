import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateInstanceWithValidSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithNullSizes() {
        ZipEightByteInteger originalSize = null;
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        ZipShort length = extraField.getLocalFileDataLength();
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        ZipShort length = extraField.getCentralDirectoryLength();
        assertEquals(new ZipShort((originalSize != null ? ZipConstants.DWORD : 0)
                                + (compressedSize != null ? ZipConstants.DWORD : 0)
                                + (relativeHeaderOffset != null ? ZipConstants.DWORD : 0)
                                + (diskStart != null ? ZipConstants.WORD : 0)), length);
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        byte[] data = extraField.getLocalFileDataData();
        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        addSizes(expectedData);
        assertArrayEquals(expectedData, data);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        byte[] data = extraField.getCentralDirectoryData();
        int off = addSizes(new byte[3 * ZipConstants.DWORD + ZipConstants.WORD])[0];
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, off, ZipConstants.WORD);
            off += ZipConstants.WORD;
        }
        assertEquals(3 * ZipConstants.DWORD + ZipConstants.WORD, data.length);
        assertArrayEquals(data, extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        extraField.parseFromLocalFileData(buffer, offset, buffer.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidDataLength() {
        byte[] buffer = new byte[ZipConstants.DWORD];
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, offset, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        byte[] buffer = new byte[2 * ZipConstants.DWORD];
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, offset, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidDataLength() {
        byte[] buffer = new byte[3 * ZipConstants.DWORD];
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(buffer, offset, 2 * ZipConstants.DWORD);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithRelativeHeaderOffset() {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, buffer, offset + 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithDiskStart() {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, buffer, offset + 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, buffer, offset + 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, buffer, ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(originalSize.getBytes(), 0, buffer, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, buffer, ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
    }
}