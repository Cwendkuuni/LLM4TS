import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraField() {
        ZipShort headerId = new ZipShort(0x0001);
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100L);
        ZipLong diskStartNumber = new ZipLong(1234567890123L);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(
            originalSize, compressedSize, relativeHeaderOffset, diskStartNumber);

        // Test getters
        Assert.assertEquals(headerId, zip64ExtraField.getHeaderId());
        Assert.assertEquals(originalSize, zip64ExtraField.getSize());
        Assert.assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStartNumber, zip64ExtraField.getDiskStartNumber());

        // Test parsing from local file data
        byte[] localFileData = zip64ExtraField.getLocalFileDataData();
        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(zip64ExtraField, parsedExtraField);

        // Test parsing from central directory data
        byte[] centralDirectoryData = zip64ExtraField.getCentralDirectoryData();
        parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(zip64ExtraField, parsedExtraField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizesInLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = ZipException.class)
    public void testInvalidCentralDirectoryDataLength() {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.fail("Expected ZipException");
    }

    @Test(expected = ZipException.class)
    public void testInvalidCentralDirectoryDataFormat() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        System.arraycopy(new byte[]{0x00, 0x01, 0x00, 0x01}, 0, centralDirectoryData, 0, 4);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.fail("Expected ZipException");
    }
}