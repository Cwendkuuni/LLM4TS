import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndGetLocalFileData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        byte[] localFileData = extraField.getLocalFileDataData();

        ZipShort headerId = extraField.getHeaderId();
        ZipShort localFileDataLength = extraField.getLocalFileDataLength();

        assertEquals(headerId.getValue(), 0x0001);
        assertEquals(localFileDataLength.getValue(), 2 * ZipConstants.DWORD);
        assertEquals(localFileData.length, 2 * ZipConstants.DWORD);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      0x00, 0x01, 0x00, 0x00,
                                      0x00, 0x01, 0x00, 0x00,
                                      0x00, 0x01, 0x00, 0x00}, localFileData);
        assertEquals(originalSize.getValue(),
                     ZipEightByteInteger.fromBytes(localFileData, 0).getValue());
        assertEquals(compressedSize.getValue(),
                     ZipEightByteInteger.fromBytes(localFileData, 4 * ZipConstants.DWORD).getValue());
    }

    @Test
    public void testCreateAndGetCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(123456L);
        ZipLong diskStart = new ZipLong(123456789012345L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                                                                                               relativeHeaderOffset, diskStart);
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        ZipShort headerId = extraField.getHeaderId();
        ZipShort centralDirectoryLength = extraField.getCentralDirectoryLength();

        assertEquals(headerId.getValue(), 0x0001);
        assertEquals(centralDirectoryLength.getValue(),
                     2 * ZipConstants.DWORD + 3 * ZipConstants.DWORD + WORD);
        assertEquals(centralDirectoryData.length, centralDirectoryLength.getValue());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                      (byte) 0x00, (byte) 0x04, 0x34, 0x56,
                                      (byte) 0x42, 0x34, 0x56, 0x78,
                                      (byte) 0x41, 0x34, 0x56, 0x78, 0x90,
                                      (byte) 0x24, 0x35, 0x34, 0x56, 0x78},
                centralDirectoryData);
        assertEquals(originalSize.getValue(),
                     ZipEightByteInteger.fromBytes(centralDirectoryData, 0).getValue());
        assertEquals(compressedSize.getValue(),
                     ZipEightByteInteger.fromBytes(centralDirectoryData, 2 * ZipConstants.DWORD).getValue());
        assertEquals(relativeHeaderOffset.getValue(),
                     ZipEightByteInteger.fromBytes(centralDirectoryData, 4 * ZipConstants.DWORD).getValue());
        assertEquals(diskStart.getValue(),
                     ZipLong.fromBytes(centralDirectoryData, 5 * ZipConstants.DWORD).getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                          0x00, 0x01, 0x00, 0x00,
                                          0x00, 0x01, 0x00, 0x00,
                                          0x00, 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        ZipShort headerId = extraField.getHeaderId();
        ZipShort localFileDataLength = extraField.getLocalFileDataLength();

        assertEquals(headerId.getValue(), 0x0001);
        assertEquals(localFileDataLength.getValue(), 2 * ZipConstants.DWORD);
        assertEquals(extraField.getSize().getValue(), 1024L);
        assertEquals(extraField.getCompressedSize().getValue(), 512L);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingSizes() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Zip64 extended information must contain both size values in the local file header.");
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(extraField.getSize().getValue(), 1024L);
        assertEquals(extraField.getCompressedSize().getValue(), 512L);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Zip64 extended information must contain both size values in the local file header.");
    }

    @Test
    public void testParseFromCentralDirectoryDataPartialData() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, 13);
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataAllDataPresent() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x04, 0x34, 0x56,
                                                   (byte) 0x42, 0x34, 0x56, 0x78,
                                                   (byte) 0x41, 0x34, 0x56, 0x78, 0x90,
                                                   (byte) 0x24, 0x35, 0x34, 0x56, 0x78};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(extraField.getSize().getValue(), 1024L);
        assertEquals(extraField.getCompressedSize().getValue(), 512L);
        assertEquals(extraField.getRelativeHeaderOffset().getValue(), 123456L);
        assertEquals(extraField.getDiskStartNumber().getValue(), 123456789012345L);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        byte[] reParsedCentralDirectoryData = new byte[centralDirectoryData.length];
        extraField.reparseCentralDirectoryData(true, true, true, true, reParsedCentralDirectoryData);

        assertEquals(centralDirectoryData, reParsedCentralDirectoryData);
    }

    @Test
    public void testReparseCentralDirectoryDataMissingSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00,
                                                   (byte) 0x00, (byte) 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        byte[] reParsedCentralDirectoryData = new byte[centralDirectoryData.length];
        extraField.reparseCentralDirectoryData(true, false, false, false, reParsedCentralDirectoryData);

        assertArrayEquals(new byte[0], reParsedCentralDirectoryData);
    }
}