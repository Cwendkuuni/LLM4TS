import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Tests constructor with valid sizes.
     */
    @Test
    public void testValidSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(20L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(10L);
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        ZipShort headerId = extraField.getHeaderId();
        ZipShort localFileDataLength = extraField.getLocalFileDataLength();
        byte[] localFileData = extraField.getLocalFileDataData();
        Assert.assertEquals(new ZipShort(ZipShort.toUnsignedShort(0x0001)), headerId);
        Assert.assertEquals(new ZipShort(3 * ZipConstants.DWORD), localFileDataLength);
        Assert.assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x01,
                                           (byte) 0x00, (byte) 0x00,
                                           (byte) 0x00, (byte) 0x14,
                                           (byte) 0x00, (byte) 0x08,
                                           (byte) 0x00, (byte) 0x0a,
                                           (byte) 0x00, (byte) 0x02,
                                           (byte) 0x00, (byte) 0x06,
                                           (byte) 0x00, (byte) 0x0c}, localFileData);
    }

    /**
     * Tests constructor with null sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullSizes() {
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(null, null);
    }

    /**
     * Tests parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01,
                                           (byte) 0x00, (byte) 0x14,
                                           (byte) 0x00, (byte) 0x08,
                                           (byte) 0x00, (byte) 0x0a,
                                           (byte) 0x00, (byte) 0x02,
                                           (byte) 0x00, (byte) 0x06,
                                           (byte) 0x00, (byte) 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        ZipEightByteInteger size = extraField.getSize();
        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        Assert.assertEquals(new ZipEightByteInteger(20L), size);
        Assert.assertEquals(new ZipEightByteInteger(10L), compressedSize);
    }

    /**
     * Tests parsing from local file data with missing sizes.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingSizes() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    /**
     * Tests parsing from central directory data.
     */
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData =
            new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08,
                       (byte) 0x00, (byte) 0x0a, (byte) 0x00, (byte) 0x02,
                       (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        ZipEightByteInteger size = extraField.getSize();
        Assert.assertEquals(new ZipEightByteInteger(0L), size);
    }

    /**
     * Tests parsing from central directory data with only disk start number.
     */
    @Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumber()
        throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x00,
                                                 (byte) 0x00, (byte) 0x08,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        ZipLong diskStartNumber = extraField.getDiskStartNumber();
        Assert.assertEquals(new ZipLong(0xFFFFL), diskStartNumber);
    }

    /**
     * Tests re-parsing central directory data.
     */
    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData =
            new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08,
                       (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x0a,
                       (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x06,
                       (byte) 0x00, (byte) 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        ZipEightByteInteger size = extraField.getSize();
        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong diskStartNumber = extraField.getDiskStartNumber();
        Assert.assertEquals(new ZipEightByteInteger(20L), size);
        Assert.assertEquals(new ZipEightByteInteger(10L), compressedSize);
        Assert.assertEquals(new ZipEightByteInteger(8L), relativeHeaderOffset);
        Assert.assertEquals(new ZipLong(0L), diskStartNumber);
    }

    /**
     * Tests re-parsing central directory data without uncompressed size.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithoutUncompressedSize()
        throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x00,
                                                 (byte) 0x00, (byte) 0x08,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x0a, (byte) 0x02,
                                                 (byte) 0x00, 0x06, (byte) 0x00,
                                                 (byte) 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);
    }

    /**
     * Tests re-parsing central directory data without compressed size.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithoutCompressedSize()
        throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x00,
                                                 (byte) 0x00, (byte) 0x08,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x14, (byte) 0x0a,
                                                 (byte) 0x02, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x06, (byte) 0x00,
                                                 (byte) 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);
    }

    /**
     * Tests re-parsing central directory data without relative header offset.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithoutRelativeHeaderOffset()
        throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x00,
                                                 (byte) 0x00, (byte) 0x08,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x14, (byte) 0x0a,
                                                 (byte) 0x02, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x06, (byte) 0xFF,
                                                 (byte) 0x00, 0x0c};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
    }

    /**
     * Tests re-parsing central directory data without disk start number.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithoutDiskStartNumber()
        throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x00,
                                                 (byte) 0x00, (byte) 0x08,
                                                 (byte) 0x00, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x14, (byte) 0x0a,
                                                 (byte) 0x02, 0xFF, 0xFF, 0xFF,
                                                 (byte) 0x00, 0x06, (byte) 0x00,
                                                 (byte) 0xFF};
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
    }
}