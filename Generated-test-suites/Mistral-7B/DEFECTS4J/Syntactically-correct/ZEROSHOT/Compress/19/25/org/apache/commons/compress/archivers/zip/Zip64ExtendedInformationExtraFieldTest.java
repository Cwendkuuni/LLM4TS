import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateZip64ExtraFieldWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        ZipShort headerId = extraField.getHeaderId();
        assertEquals(ZipConstants.DWORD, headerId.getValue());
        assertEquals(ZipConstants.ZIP64, headerId.getBytes()[0] & 0xFF);

        int localFileDataLength = extraField.getLocalFileDataLength().getValue();
        assertEquals(2 * ZipConstants.DWORD, localFileDataLength);

        byte[] localFileData = extraField.getLocalFileDataData();
        assertEquals(2 * ZipConstants.DWORD, localFileData.length);

        ZipEightByteInteger sizeFromLocalData = new ZipEightByteInteger(localFileData, 0);
        assertEquals(size.getValue(), sizeFromLocalData.getValue());

        ZipEightByteInteger compressedSizeFromLocalData = new ZipEightByteInteger(localFileData, DWORD);
        assertEquals(compressedSize.getValue(), compressedSizeFromLocalData.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZip64ExtraFieldWithoutBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZip64ExtraFieldWithoutBothSizes2() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, compressedSize);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        System.arraycopy(size.getBytes(), 0, localFileData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        ZipShort headerId = extraField.getHeaderId();
        assertEquals(ZipConstants.DWORD, headerId.getValue());
        assertEquals(ZipConstants.ZIP64, headerId.getBytes()[0] & 0xFF);

        ZipEightByteInteger parsedSize = extraField.getSize();
        assertEquals(size.getValue(), parsedSize.getValue());

        ZipEightByteInteger parsedCompressedSize = extraField.getCompressedSize();
        assertEquals(compressedSize.getValue(), parsedCompressedSize.getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] localFileData = new byte[ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData2() {
        byte[] localFileData = new byte[2 * ZipConstants.DWORD - ZipConstants.WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidData() {
        byte[] centralDirectoryData = new byte[ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithPartialData2() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingSizes() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingOffset() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(centralDirectoryData, 0, extraField.rawCentralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);

        assertNull(extraField.getRelativeHeaderOffset());
        assertNotNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingDiskStartNumber() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        System.arraycopy(centralDirectoryData, 0, extraField.rawCentralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);

        assertNotNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        assertEquals(0, extraField.getLocalFileDataLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(2 * ZipConstants.DWORD, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        assertEquals(0, extraField.getCentralDirectoryLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(2048L);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, null);

        ZipLong diskStartNumber = new ZipLong(3072L);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);

        assertEquals(2 * ZipConstants.DWORD + DWORD + WORD, extraField.getCentralDirectoryLength().getValue());
    }
}