import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size);
        ZipShort expectedLength = new ZipShort(2 * DWORD);
        ZipShort actualLength = extraField.getLocalFileDataLength();

        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        ZipShort expectedLength = new ZipShort((size.getValue() + compressedSize.getValue()) * DWORD
                                                + (relativeHeaderOffset != null ? DWORD : 0)
                                                + (diskStart != null ? WORD : 0));
        ZipShort actualLength = extraField.getCentralDirectoryLength();

        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);

        byte[] actualData = extraField.getLocalFileDataData();

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(2L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        byte[] expectedData = new byte[3 * DWORD + WORD];
        addSizes(expectedData);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, addSizes(expectedData), DWORD);
        System.arraycopy(diskStart.getBytes(), 0, expectedData, addSizes(expectedData) + DWORD, WORD);

        byte[] actualData = extraField.getCentralDirectoryData();

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger expectedSize = new ZipEightByteInteger(100L);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        addSizes(data);
        extraField.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(expectedSize, extraField.getSize());
        Assert.assertEquals(expectedCompressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithoutBothSizes() throws ZipException {
        byte[] data = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertEquals(extraField.getSize(), null);
        Assert.assertEquals(extraField.getCompressedSize(), null);
        Assert.assertEquals(extraField.getRelativeHeaderOffset(), null);
        Assert.assertEquals(extraField.getDiskStartNumber(), null);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(2L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 3 * DWORD, WORD);

        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber());
    }
}