import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyExtraField() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(new byte[0], extraField.getLocalFileDataData());
        Assert.assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
    }

    @Test
    public void testBasicExtraField() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger((int) originalSize),
                new ZipEightByteInteger((int) compressedSize)
        );
        byte[] expectedLocalData = new byte[2 * ZipConstants.DWORD];
        addSizes(expectedLocalData);
        Assert.assertArrayEquals(expectedLocalData, extraField.getLocalFileDataData());
        byte[] expectedCentralData = new byte[4 * ZipConstants.DWORD];
        addSizes(expectedCentralData);
        Assert.assertArrayEquals(expectedCentralData, extraField.getCentralDirectoryData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizeInLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                null,
                new ZipEightByteInteger((int) 12345)
        );
        Assert.assertArrayEquals(new byte[0], extraField.getLocalFileDataData());
        Assert.assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizeInCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(
                new byte[]{},
                0,
                0
        );
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidCentralDirectoryData() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[12];
        int offset = 0;
        addSizes(centralDirectoryData);
        System.arraycopy(originalSize > 0xFFFFFFFF ? new ZipLong(originalSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        offset += 4;
        System.arraycopy(compressedSize > 0xFFFFFFFF ? new ZipLong(compressedSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(originalSize, extraField.getSize().getValue());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidLocalFileDataWithOffset() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        long relativeHeaderOffset = 0x12345678L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD];
        int offset = 0;
        addSizes(localFileData);
        System.arraycopy(originalSize > 0xFFFFFFFF ? new ZipLong(originalSize).getBytes() : new byte[0], 0, localFileData, offset, 4);
        offset += 4;
        System.arraycopy(compressedSize > 0xFFFFFFFF ? new ZipLong(compressedSize).getBytes() : new byte[0], 0, localFileData, offset, 4);
        offset += 4;
        System.arraycopy(new ZipLong(relativeHeaderOffset).getBytes(), 0, localFileData, offset, 4);
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(originalSize, extraField.getSize().getValue());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset().getValue());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidLocalFileDataWithDiskStart() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        long diskStart = 0x1234567890L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[2 * ZipConstants.DWORD + 2 * ZipConstants.WORD];
        int offset = 0;
        addSizes(localFileData);
        System.arraycopy(originalSize > 0xFFFFFFFF ? new ZipLong(originalSize).getBytes() : new byte[0], 0, localFileData, offset, 4);
        offset += 4;
        System.arraycopy(compressedSize > 0xFFFFFFFF ? new ZipLong(compressedSize).getBytes() : new byte[0], 0, localFileData, offset, 4);
        offset += 4;
        System.arraycopy(new ZipShort((short) ((int) (diskStart >> 16))).getBytes(), 0, localFileData, offset, 2);
        offset += 2;
        System.arraycopy(new ZipShort((short) ((int) diskStart)).getBytes(), 0, localFileData, offset, 2);
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(originalSize, extraField.getSize().getValue());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testValidCentralDirectoryDataWithOffset() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        long relativeHeaderOffset = 0x12345678L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[4 * ZipConstants.DWORD + ZipConstants.DWORD];
        int offset = 0;
        addSizes(centralDirectoryData);
        System.arraycopy(originalSize > 0xFFFFFFFF ? new ZipLong(originalSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        offset += 4;
        System.arraycopy(compressedSize > 0xFFFFFFFF ? new ZipLong(compressedSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        offset += 4;
        System.arraycopy(new ZipLong(relativeHeaderOffset).getBytes(), 0, centralDirectoryData, offset, 4);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        Assert.assertEquals(originalSize, extraField.getSize().getValue());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset().getValue());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidCentralDirectoryDataWithDiskStart() {
        long originalSize = 123456789L;
        long compressedSize = 678912345L;
        long diskStart = 0x1234567890L;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[4 * ZipConstants.DWORD + 2 * ZipConstants.WORD];
        int offset = 0;
        addSizes(centralDirectoryData);
        System.arraycopy(originalSize > 0xFFFFFFFF ? new ZipLong(originalSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        offset += 4;
        System.arraycopy(compressedSize > 0xFFFFFFFF ? new ZipLong(compressedSize).getBytes() : new byte[0], 0, centralDirectoryData, offset, 4);
        offset += 4;
        System.arraycopy(new ZipShort((short) ((int) (diskStart >> 16))).getBytes(), 0, centralDirectoryData, offset, 2);
        offset += 2;
        System.arraycopy(new ZipShort((short) ((int) diskStart)).getBytes(), 0, centralDirectoryData, offset, 2);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        Assert.assertEquals(originalSize, extraField.getSize().getValue());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStartNumber().getValue());
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, 4);
            off += 4;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, 4);
            off += 4;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, 4);
            off += 4;
        }
        if (diskStartNumber != null) {
            System.arraycopy(diskStartNumber.getBytes(), 0, data, off, 4);
            off += 4;
            System.arraycopy(new ZipShort((short) ((int) (diskStartNumber.getValue() >> 16))).getBytes(), 0, data, off, 2);
            off += 2;
            System.arraycopy(new ZipShort((short) ((int) (diskStartNumber.getValue() & 0xFFFF))).getBytes(), 0, data, off, 2);
            off += 2;
        }
    }
}