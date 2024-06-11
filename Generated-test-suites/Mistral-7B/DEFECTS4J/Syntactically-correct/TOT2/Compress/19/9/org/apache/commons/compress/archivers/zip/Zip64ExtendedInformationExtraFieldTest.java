import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraFieldConstruction() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(2048L);
        ZipLong diskStart = new ZipLong(3072L);

        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField(size, compressedSize,
                        relativeHeaderOffset, diskStart);

        Assert.assertEquals(size, zip64ExtraField.getSize());
        Assert.assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testValidZip64ExtendedInformationExtraFieldGettersSetters() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(2048L);
        ZipLong diskStart = new ZipLong(3072L);

        zip64ExtraField.setSize(size);
        Assert.assertEquals(size, zip64ExtraField.getSize());

        zip64ExtraField.setCompressedSize(compressedSize);
        Assert.assertEquals(compressedSize, zip64ExtraField.getCompressedSize());

        zip64ExtraField.setRelativeHeaderOffset(relativeHeaderOffset);
        Assert.assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());

        zip64ExtraField.setDiskStartNumber(diskStart);
        Assert.assertEquals(diskStart, zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        zip64ExtraField.setSize(size);
        zip64ExtraField.setCompressedSize(compressedSize);

        byte[] expectedData = new byte[2 * DWORD];
        zip64ExtraField.addSizes(expectedData);

        byte[] actualData = zip64ExtraField.getLocalFileDataData();

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(2048L);
        ZipLong diskStart = new ZipLong(3072L);

        zip64ExtraField.setSize(size);
        zip64ExtraField.setCompressedSize(compressedSize);
        zip64ExtraField.setRelativeHeaderOffset(relativeHeaderOffset);
        zip64ExtraField.setDiskStartNumber(diskStart);

        byte[] expectedData =
                new byte[2 * DWORD + DWORD + WORD]; // 2 * size + offset + diskStart

        int offset = zip64ExtraField.addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, offset, WORD);
        }

        byte[] actualData = zip64ExtraField.getCentralDirectoryData();

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        byte[] data = new byte[2 * DWORD];
        zip64ExtraField.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingSizes() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        byte[] data = new byte[DWORD];
        zip64ExtraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidSizesOrder() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        byte[] data = new byte[3 * DWORD + WORD];
        zip64ExtraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        byte[] rawCentralDirectoryData = new byte[centralDirectoryData.length];
        System.arraycopy(centralDirectoryData, 0, rawCentralDirectoryData, 0, centralDirectoryData.length);

        zip64ExtraField.reparseCentralDirectoryData(true, true, true, true);

        byte[] expectedData = new byte[2 * DWORD + DWORD + WORD];
        int offset = zip64ExtraField.addSizes(expectedData);

        if (size != null) {
            System.arraycopy(size.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, offset, WORD);
        }

        Assert.assertArrayEquals(expectedData, rawCentralDirectoryData);
    }
}