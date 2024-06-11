import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithValidData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(DWORD, 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(DWORD, 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(DWORD, 20);
        ZipLong diskStart = new ZipLong(WORD, 32);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);

        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testCreateWithoutRelativeHeaderOffset() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(DWORD, 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(DWORD, 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testCreateWithoutDiskStart() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(DWORD, 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(DWORD, 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(DWORD, 20);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset);

        Assert.assertNull(extraField.getDiskStartNumber());
        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(new byte[0], 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocalFileDataMissingBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(new byte[DWORD], 0, DWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocalFileDataMissingOriginalSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(new byte[2 * DWORD], 0, 2 * DWORD);
        extraField.parseFromLocalFileData(new byte[2 * DWORD + DWORD], 0, 3 * DWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocalFileDataMissingCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(new byte[2 * DWORD], 0, 2 * DWORD);
        extraField.parseFromLocalFileData(new byte[2 * DWORD + DWORD], 0, 3 * DWORD);
        extraField.parseFromLocalFileData(new byte[2 * DWORD + 2 * DWORD], 0, 4 * DWORD);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingAllFields() {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingOriginalSize() {
        byte[] data = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingCompressedSize() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingRelativeHeaderOffset() {
        byte[] data = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingDiskStart() {
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataAndReparse() {
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        Assert.assertNotNull(extraField.getSize());
        Assert.assertNotNull(extraField.getCompressedSize());
        Assert.assertNotNull(extraField.getRelativeHeaderOffset());
        Assert.assertNotNull(extraField.getDiskStartNumber());
    }
}