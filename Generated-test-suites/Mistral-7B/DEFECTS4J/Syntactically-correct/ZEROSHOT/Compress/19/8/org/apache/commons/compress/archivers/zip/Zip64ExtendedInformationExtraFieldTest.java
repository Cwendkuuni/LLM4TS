import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateInstanceWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(42L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(21L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    @Test
    public void testCreateInstanceWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(42L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(21L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(2L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort((short) 0), extraField.getLocalFileDataLength());

        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(42L), new ZipEightByteInteger(21L));
        Assert.assertEquals(new ZipShort((short) (2 * ZipConstants.DWORD)), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort((short) 0), extraField.getCentralDirectoryLength());

        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(42L), new ZipEightByteInteger(21L));
        Assert.assertEquals(new ZipShort((short) (2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.DWORD)), extraField.getCentralDirectoryLength());

        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(42L), new ZipEightByteInteger(21L), new ZipEightByteInteger(1L), new ZipLong(2L));
        Assert.assertEquals(new ZipShort((short) (2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.WORD + ZipConstants.WORD)), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(new byte[0], extraField.getLocalFileDataData());

        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(42L), new ZipEightByteInteger(21L));
        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        extraField.addSizes(expectedData);
        Assert.assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());

        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(42L), new ZipEightByteInteger(21L));
        byte[] expectedData = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.DWORD];
        extraField.addSizes(expectedData);
        if (extraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0, expectedData, extraField.getLocalFileDataLength().getValue(), ZipConstants.DWORD);
        }
        if (extraField.getDiskStart() != null) {
            System.arraycopy(extraField.getDiskStart().getBytes(), 0, expectedData, extraField.getLocalFileDataLength().getValue() + ZipConstants.DWORD, ZipConstants.WORD);
        }
        Assert.assertArrayEquals(expectedData, extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[2 * ZipConstants.DWORD];
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(42L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(21L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.DWORD];
        size = new ZipEightByteInteger(42L);
        compressedSize = new ZipEightByteInteger(21L);
        relativeHeaderOffset = new ZipEightByteInteger(1L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.WORD];
        size = new ZipEightByteInteger(42L);
        compressedSize = new ZipEightByteInteger(21L);
        relativeHeaderOffset = new ZipEightByteInteger(1L);
        diskStart = new ZipLong(2L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[0];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[2 * ZipConstants.DWORD];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(42L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(21L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        size = new ZipEightByteInteger(42L);
        compressedSize = new ZipEightByteInteger(21L);
        relativeHeaderOffset = new ZipEightByteInteger(1L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());

        data = new byte[3 * ZipConstants.DWORD + 2 * ZipConstants.WORD];
        size = new ZipEightByteInteger(42L);
        compressedSize = new ZipEightByteInteger(21L);
        relativeHeaderOffset = new ZipEightByteInteger(1L);
        diskStart = new ZipLong(2L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[3 * ZipConstants.DWORD + 2 * ZipConstants.WORD];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        Assert.assertEquals(extraField.getSize(), new ZipEightByteInteger(42L));
        Assert.assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(21L));
        Assert.assertEquals(extraField.getRelativeHeaderOffset(), new ZipEightByteInteger(1L));
        Assert.assertEquals(extraField.getDiskStartNumber(), new ZipLong(2L));
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[2 * ZipConstants.DWORD];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, false, false);
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithInvalidLength() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD - 1];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
    }
}