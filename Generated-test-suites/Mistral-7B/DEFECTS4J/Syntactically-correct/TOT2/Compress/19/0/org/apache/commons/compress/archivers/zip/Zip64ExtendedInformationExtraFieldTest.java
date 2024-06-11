import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidConstruction() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipShort diskStart = new ZipShort((short) 3);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1024L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            size, compressedSize, relativeHeaderOffset, diskStart);

        Assertions.assertEquals(HEADER_ID, extraField.getHeaderId());
        Assertions.assertEquals(size, extraField.getSize());
        Assertions.assertEquals(compressedSize, extraField.getCompressedSize());
        Assertions.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testEmptyConstruction() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assertions.assertNull(extraField.getSize());
        Assertions.assertNull(extraField.getCompressedSize());
        Assertions.assertNull(extraField.getRelativeHeaderOffset());
        Assertions.assertNull(extraField.getDiskStartNumber());
        Assertions.assertEquals(0, extraField.getLocalFileDataLength().getValue());
        Assertions.assertEquals(0, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParsingFromLocalFileHeaderData() {
        byte[] data = new byte[8 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        Assertions.assertEquals(size, extraField.getSize());
        Assertions.assertEquals(compressedSize, extraField.getCompressedSize());
        Assertions.assertNull(extraField.getRelativeHeaderOffset());
        Assertions.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingFromCentralDirectoryDataWithAllFields() {
        byte[] data = new byte[3 * DWORD + DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1024L);
        ZipLong diskStart = new ZipLong(5L);

        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        Assertions.assertEquals(size, extraField.getSize());
        Assertions.assertEquals(compressedSize, extraField.getCompressedSize());
        Assertions.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingFromCentralDirectoryDataWithOnlyDiskStart() {
        byte[] data = new byte[3 * DWORD];
        ZipLong diskStart = new ZipLong(5L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        Assertions.assertNull(extraField.getSize());
        Assertions.assertNull(extraField.getCompressedSize());
        Assertions.assertNull(extraField.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testThrowExceptionWhenParsingFromCentralDirectoryDataWithNoSizeInformation() {
        byte[] data = new byte[WORD];

        Assertions.assertThrows(ZipException.class, () -> {
            Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
            extraField.parseFromCentralDirectoryData(data, 0, data.length);
        });
    }
}