import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.commons.compress.utils.IOUtils.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraField() {
        Zip8ByteInteger expectedOriginalSize = new Zip8ByteInteger(0x12345678L);
        Zip8ByteInteger expectedCompressedSize = new Zip8ByteInteger(0x9abcdef0L);
        Zip8ByteInteger expectedRelativeHeaderOffset = new Zip8ByteInteger(0x110000L);
        ZipLong expectedDiskStartNumber = new ZipLong(0x2345678901234567L);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(
                expectedOriginalSize, expectedCompressedSize,
                expectedRelativeHeaderOffset, expectedDiskStartNumber);

        ZipShort headerId = testObject.getHeaderId();
        assertEquals(HEADER_ID, headerId);

        ZipShort localFileDataLength = testObject.getLocalFileDataLength();
        assertEquals(new ZipShort(sizeof(Zip8ByteInteger) * 4), localFileDataLength);

        byte[] localFileData = testObject.getLocalFileDataData();
        byte[] expectedLocalFileData = new byte[sizeof(Zip8ByteInteger) * 4];
        System.arraycopy(expectedOriginalSize.getBytes(), 0, expectedLocalFileData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, expectedLocalFileData, DWORD, DWORD);
        System.arraycopy(expectedRelativeHeaderOffset.getBytes(), 0, expectedLocalFileData, 2 * DWORD, DWORD);
        System.arraycopy(expectedDiskStartNumber.getBytes(), 0, expectedLocalFileData, 3 * DWORD, WORD);
        assertArrayEquals(expectedLocalFileData, localFileData);

        ZipShort centralDirectoryLength = testObject.getCentralDirectoryLength();
        assertEquals(new ZipShort(sizeof(Zip8ByteInteger) * 4 + sizeof(ZipLong)), centralDirectoryLength);

        byte[] centralDirectoryData = testObject.getCentralDirectoryData();
        byte[] expectedCentralDirectoryData = new byte[sizeof(Zip8ByteInteger) * 4 + sizeof(ZipLong)];
        System.arraycopy(expectedOriginalSize.getBytes(), 0, expectedCentralDirectoryData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, expectedCentralDirectoryData, DWORD, DWORD);
        System.arraycopy(expectedRelativeHeaderOffset.getBytes(), 0, expectedCentralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(expectedDiskStartNumber.getBytes(), 0, expectedCentralDirectoryData, 3 * DWORD, WORD);
        assertArrayEquals(expectedCentralDirectoryData, centralDirectoryData);

        try {
            testObject.parseFromLocalFileData(expectedLocalFileData, 0, expectedLocalFileData.length);
            Zip8ByteInteger actualOriginalSize = testObject.getSize();
            assertEquals(expectedOriginalSize, actualOriginalSize);
            Zip8ByteInteger actualCompressedSize = testObject.getCompressedSize();
            assertEquals(expectedCompressedSize, actualCompressedSize);
            Zip8ByteInteger actualRelativeHeaderOffset = testObject.getRelativeHeaderOffset();
            assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);
            ZipLong actualDiskStartNumber = testObject.getDiskStartNumber();
            assertEquals(expectedDiskStartNumber, actualDiskStartNumber);
        } catch (ZipException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();

        ZipShort headerId = testObject.getHeaderId();
        assertEquals(HEADER_ID, headerId);

        ZipShort localFileDataLength = testObject.getLocalFileDataLength();
        assertEquals(new ZipShort(0), localFileDataLength);

        byte[] localFileData = testObject.getLocalFileDataData();
        assertEquals(new byte[0], localFileData);

        ZipShort centralDirectoryLength = testObject.getCentralDirectoryLength();
        assertEquals(new ZipShort(0), centralDirectoryLength);

        byte[] centralDirectoryData = testObject.getCentralDirectoryData();
        assertEquals(new byte[0], centralDirectoryData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZip64ExtendedInformationExtraFieldMissingBothSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(null);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryExtraFieldData = new byte[24];
        System.arraycopy(new Zip8ByteInteger(0x12345678L).getBytes(), 0, centralDirectoryExtraFieldData, 0, DWORD);
        System.arraycopy(new Zip8ByteInteger(0x9abcdef0L).getBytes(), 0, centralDirectoryExtraFieldData, DWORD, DWORD);
        System.arraycopy(new Zip8ByteInteger(0x110000L).getBytes(), 0, centralDirectoryExtraFieldData, 2 * DWORD, DWORD);
        System.arraycopy(new ZipLong(0x2345678901234567L).getBytes(), 0, centralDirectoryExtraFieldData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryExtraFieldData, 0, centralDirectoryExtraFieldData.length);

        Zip8ByteInteger actualOriginalSize = testObject.getSize();
        assertEquals(new Zip8ByteInteger(0x12345678L), actualOriginalSize);
        Zip8ByteInteger actualCompressedSize = testObject.getCompressedSize();
        assertEquals(new Zip8ByteInteger(0x9abcdef0L), actualCompressedSize);
        Zip8ByteInteger actualRelativeHeaderOffset = testObject.getRelativeHeaderOffset();
        assertEquals(new Zip8ByteInteger(0x110000L), actualRelativeHeaderOffset);
        ZipLong actualDiskStartNumber = testObject.getDiskStartNumber();
        assertEquals(new ZipLong(0x2345678901234567L), actualDiskStartNumber);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingSizes() throws ZipException {
        byte[] centralDirectoryExtraFieldData = new byte[12];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryExtraFieldData, 0, centralDirectoryExtraFieldData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingOffset() throws ZipException {
        byte[] centralDirectoryExtraFieldData = new byte[16];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryExtraFieldData, 0, centralDirectoryExtraFieldData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingDiskStart() throws ZipException {
        byte[] centralDirectoryExtraFieldData = new byte[20];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryExtraFieldData, 0, centralDirectoryExtraFieldData.length);
    }
}