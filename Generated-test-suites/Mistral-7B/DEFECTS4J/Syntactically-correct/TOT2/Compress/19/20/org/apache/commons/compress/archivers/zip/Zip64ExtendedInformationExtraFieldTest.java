import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() {
        byte[] localFileData = new byte[16];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, 4, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingFirstSize() {
        byte[] localFileData = new byte[12];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Should throw ZipException");
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingSecondSize() {
        byte[] localFileData = new byte[12];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Should throw ZipException");
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[24];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x3456);
        ZipLong diskStart = new ZipLong((short) 0x7890);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 12, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 16, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingAllFields() {
        byte[] centralDirectoryData = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Should throw ZipException");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingRelativeHeaderOffset() {
        byte[] centralDirectoryData = new byte[12];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipLong diskStart = new ZipLong((short) 0x7890);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 12, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Should throw ZipException");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingDiskStart() {
        byte[] centralDirectoryData = new byte[16];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x3456);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 12, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Should throw ZipException");
    }
}