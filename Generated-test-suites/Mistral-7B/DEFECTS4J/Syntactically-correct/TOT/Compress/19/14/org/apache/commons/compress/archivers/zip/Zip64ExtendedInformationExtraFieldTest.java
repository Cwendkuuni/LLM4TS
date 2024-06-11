import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link Zip64ExtendedInformationExtraField} class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidConstruction() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        Assert.assertNotNull(extraField);
        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidParseFromLocalFileData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        byte[] data = new byte[4 * DWORD];
        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            Assert.fail("Unexpected ZipException: " + e.getMessage());
        }
        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testValidParseFromCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStartNumber = new ZipLong(0x5678);
        byte[] buffer = new byte[3 * DWORD + WORD];
        System.arraycopy(originalSize.getBytes(), 0, buffer, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, buffer, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, buffer, 2 * DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        } catch (ZipException e) {
            Assert.fail("Unexpected ZipException: " + e.getMessage());
        }
        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testEmptyField() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[0];
        try {
            extraField.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            // expected
        }
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testInvalidParseFromLocalFileData() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            Assert.assertEquals("Zip64 extended information must contain"
                               + " both size values in the local file header.",
                              e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testInvalidParseFromCentralDirectoryData() {
        byte[] buffer = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        } catch (ZipException e) {
            Assert.assertEquals("central directory zip64 extended information "
                               + "extra field's length doesn't match central "
                               + "directory data.  Expected length 24 but is "
                               + buffer.length, e.getMessage());
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        Assert.assertEquals(extraField.getSize(), new ZipEightByteInteger(1024));
        Assert.assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(512));
        Assert.assertEquals(extraField.getRelativeHeaderOffset(),
                           new ZipEightByteInteger(0x1234));
        Assert.assertEquals(extraField.getDiskStartNumber(), new ZipLong(0x5678));
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertEquals(extraField.getRelativeHeaderOffset(),
                           new ZipEightByteInteger(0x1234));
        Assert.assertEquals(extraField.getDiskStartNumber(), new ZipLong(0x5678));
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutOffset() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, true, true, false);
        Assert.assertEquals(extraField.getSize(), new ZipEightByteInteger(1024));
        Assert.assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(512));
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertEquals(extraField.getDiskStartNumber(), new ZipLong(0x5678));
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutDiskStart() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        Assert.assertEquals(extraField.getSize(), new ZipEightByteInteger(1024));
        Assert.assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(512));
        Assert.assertEquals(extraField.getRelativeHeaderOffset(),
                           new ZipEightByteInteger(0x1234));
        Assert.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutAnything() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[2 * DWORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                                                 centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, false);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());
    }
}