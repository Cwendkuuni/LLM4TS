import org.apache.commons.compress.archivers.zip.*;
import org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testConstructorWithOnlyCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, compressedSize);
        assertEquals(0, extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testConstructorWithOnlyOriginalSize() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, null);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(0, extraField.getCompressedSize().getValue());
    }

    @Test
    public void testConstructorWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234L);
        ZipLong diskStart = new ZipLong(5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new Zip64ExtendedInformationExtraField();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSizes() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getLocalFileDataLength().getValue());

        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField extraFieldWithSize = new Zip64ExtendedInformationExtraField(originalSize, null);
        assertEquals(2 * DWORD, extraFieldWithSize.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());

        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraFieldWithSizes = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals((originalSize.getValue() + compressedSize.getValue() + DWORD)
                , extraFieldWithSizes.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[8 * DWORD];

        // Test with no data
        Zip64ExtendedInformationExtraField extraFieldNoData = new Zip64ExtendedInformationExtraField();
        extraFieldNoData.parseFromLocalFileData(localFileData, 0, 0);

        // Test with data for all fields
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234L);
        ZipLong diskStart = new ZipLong(5678);
        int offset = 0;

        System.arraycopy(originalSize.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(diskStart.getBytes(), 0, localFileData, offset, WORD);

        Zip64ExtendedInformationExtraField extraFieldWithAllData = new Zip64ExtendedInformationExtraField();
        extraFieldWithAllData.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize.getValue(), extraFieldWithAllData.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraFieldWithAllData.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraFieldWithAllData.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraFieldWithAllData.getDiskStartNumber().getValue());

        // Test with no size fields
        offset += DWORD;
        Zip64ExtendedInformationExtraField extraFieldNoSizes = new Zip64ExtendedInformationExtraField();
        extraFieldNoSizes.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(0, extraFieldNoSizes.getSize().getValue());
        assertEquals(0, extraFieldNoSizes.getCompressedSize().getValue());
        assertEquals(0, extraFieldNoSizes.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraFieldNoSizes.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];

        // Test with no data
        Zip64ExtendedInformationExtraField extraFieldNoData = new Zip64ExtendedInformationExtraField();
        extraFieldNoData.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        // Test with data for size fields
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        int offset = 0;

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);

        Zip64ExtendedInformationExtraField extraFieldWithSizes = new Zip64ExtendedInformationExtraField();
        extraFieldWithSizes.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize.getValue(), extraFieldWithSizes.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraFieldWithSizes.getCompressedSize().getValue());
        assertEquals(0, extraFieldWithSizes.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraFieldWithSizes.getDiskStartNumber().getValue());

        // Test with data for all fields
        ZipEightByteInteger originalSize2 = new ZipEightByteInteger(2048L);
        ZipEightByteInteger compressedSize2 = new ZipEightByteInteger(1024L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234L);
        ZipLong diskStart = new ZipLong(5678);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, offset, WORD);

        Zip64ExtendedInformationExtraField extraFieldWithAllData = new Zip64ExtendedInformationExtraField();
        extraFieldWithAllData.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize2.getValue(), extraFieldWithAllData.getSize().getValue());
        assertEquals(compressedSize2.getValue(), extraFieldWithAllData.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraFieldWithAllData.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraFieldWithAllData.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        int offset = 0;
        ZipEightByteInteger uncompressedSize = new ZipEightByteInteger(1024L);
        System.arraycopy(uncompressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(uncompressedSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());

        // Test with only size fields present
        System.arraycopy(centralDirectoryData, 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        ZipEightByteInteger uncompressedSize2 = new ZipEightByteInteger(2048L);
        System.arraycopy(uncompressedSize2.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, false, false);
        assertEquals(uncompressedSize2.getValue(), extraField.getSize().getValue());
        assertEquals(0, extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());

        // Test with no size fields present
        System.arraycopy(centralDirectoryData, 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, false);
        assertEquals(0, extraField.getSize().getValue());
        assertEquals(0, extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());
    }
}