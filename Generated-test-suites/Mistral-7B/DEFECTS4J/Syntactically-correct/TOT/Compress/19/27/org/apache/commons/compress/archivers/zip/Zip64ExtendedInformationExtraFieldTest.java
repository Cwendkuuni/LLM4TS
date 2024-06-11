import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyExtraField() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = extraField.getLocalFileDataData();
        assertEquals(0, localFileData.length);

        try {
            extraField.parseFromLocalFileData(localFileData, 0, 0);
            fail("Expected ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testValidExtraField() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] localFileData = extraField.getLocalFileDataData();

        assertEquals(2 * DWORD, localFileData.length);

        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(size, parsedExtraField.getSize());
        assertEquals(compressedSize, parsedExtraField.getCompressedSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizeInExtraField() {
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingCompressedSizeInExtraField() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = null;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test
    public void testValidCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        int expectedLength = (extraField.getSize() != null ? DWORD : 0)
                + (extraField.getCompressedSize() != null ? DWORD : 0)
                + (extraField.getRelativeHeaderOffset() != null ? DWORD : 0)
                + (extraField.getDiskStart() != null ? WORD : 0);
        assertEquals(expectedLength, centralDirectoryData.length);

        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(extraField.getSize(), extraField.getSize());
        assertEquals(extraField.getCompressedSize(), extraField.getCompressedSize());
        if (extraField.getRelativeHeaderOffset() != null) {
            assertEquals(extraField.getRelativeHeaderOffset().getValue(),
                    extraField.getRelativeHeaderOffset().getValue());
        }
        if (extraField.getDiskStart() != null) {
            assertEquals(extraField.getDiskStartNumber().getValue(),
                    extraField.getDiskStartNumber().getValue());
        }
    }

    @Test(expected = ZipException.class)
    public void testInvalidCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidDataLength() {
        byte[] localFileData = new byte[]{1, 2, 3, 4, 5};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        try {
            extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
            fail("Expected ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] localFileData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 1, 2, 3, 4};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(new ZipEightByteInteger(1L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(2L), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(3L), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(4L), extraField.getDiskStartNumber());
    }
}