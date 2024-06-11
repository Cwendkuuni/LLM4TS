import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndGetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(size, null);
        ZipEightByteInteger actualSize = testObject.getSize();
        Assertions.assertEquals(size, actualSize);
    }

    @Test
    public void testCreateAndGetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(null, compressedSize);
        ZipEightByteInteger actualCompressedSize = testObject.getCompressedSize();
        Assertions.assertEquals(compressedSize, actualCompressedSize);
    }

    @Test
    public void testCreateAndGetRelativeHeaderOffset() {
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4L);
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(null, null, relativeHeaderOffset, null);
        ZipEightByteInteger actualRelativeHeaderOffset = testObject.getRelativeHeaderOffset();
        Assertions.assertEquals(relativeHeaderOffset, actualRelativeHeaderOffset);
    }

    @Test
    public void testCreateAndGetDiskStartNumber() {
        ZipLong diskStart = new ZipLong(3L);
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(null, null, null, diskStart);
        ZipLong actualDiskStart = testObject.getDiskStartNumber();
        Assertions.assertEquals(diskStart, actualDiskStart);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        ZipShort actualLocalFileDataLength = testObject.getLocalFileDataLength();
        Assertions.assertEquals(new ZipShort(2 * DWORD), actualLocalFileDataLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4L);
        testObject.setRelativeHeaderOffset(relativeHeaderOffset);
        ZipLong diskStart = new ZipLong(3L);
        testObject.setDiskStartNumber(diskStart);
        ZipShort actualCentralDirectoryLength = testObject.getCentralDirectoryLength();
        Assertions.assertEquals(new ZipShort((size != null ? DWORD : 0)
                + (compressedSize != null ? DWORD : 0)
                + (relativeHeaderOffset != null ? DWORD : 0)
                + (diskStart != null ? WORD : 0)), actualCentralDirectoryLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        byte[] expectedData = new byte[2 * DWORD];
        testObject.addSizes(expectedData);
        byte[] actualData = testObject.getLocalFileDataData();
        Assertions.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4L);
        testObject.setRelativeHeaderOffset(relativeHeaderOffset);
        ZipLong diskStart = new ZipLong(3L);
        testObject.setDiskStartNumber(diskStart);
        byte[] expectedData = new byte[testObject.getCentralDirectoryLength().getValue()];
        int off = testObject.addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, off, WORD);
            off += WORD;
        }
        byte[] actualData = testObject.getCentralDirectoryData();
        Assertions.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] inputData = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        testObject.addSizes(inputData);
        testObject.parseFromLocalFileData(inputData, 0, inputData.length);
        Assertions.assertEquals(size, testObject.getSize());
        Assertions.assertEquals(compressedSize, testObject.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] inputData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4L);
        ZipLong diskStart = new ZipLong(3L);
        testObject.reparseCentralDirectoryData(true, true, true, true);
        System.arraycopy(inputData, 0, testObject.rawCentralDirectoryData, 0, inputData.length);
        testObject.parseFromCentralDirectoryData(testObject.rawCentralDirectoryData, 0, inputData.length);
        Assertions.assertEquals(size, testObject.getSize());
        Assertions.assertEquals(compressedSize, testObject.getCompressedSize());
        Assertions.assertEquals(relativeHeaderOffset, testObject.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, testObject.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStartField() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] inputData = new byte[2 * DWORD + WORD];
        ZipLong diskStart = new ZipLong(3L);
        testObject.reparseCentralDirectoryData(false, false, false, true);
        System.arraycopy(inputData, 0, testObject.rawCentralDirectoryData, 0, inputData.length);
        testObject.parseFromCentralDirectoryData(testObject.rawCentralDirectoryData, 0, inputData.length);
        Assertions.assertNull(testObject.getSize());
        Assertions.assertNull(testObject.getCompressedSize());
        Assertions.assertNull(testObject.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, testObject.getDiskStartNumber());
    }
}