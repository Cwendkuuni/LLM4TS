import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        addSizes(localFileData, originalSize, compressedSize);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
            assertEquals(originalSize, extraField.getSize());
            assertEquals(compressedSize, extraField.getCompressedSize());
        } catch (ZipException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithoutBothSizes() {
        byte[] localFileData = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
            fail("Expected ZipException: " + LFH_MUST_HAVE_BOTH_SIZES_MSG);
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testGetCentralDirectoryDataWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100L);
        ZipLong diskStart = new ZipLong(1L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                relativeHeaderOffset, diskStart);

        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        int expectedCentralDirectoryDataLength = 2 * DWORD + DWORD + DWORD + WORD;
        assertEquals(expectedCentralDirectoryDataLength, centralDirectoryData.length);

        int off = 0;
        assertEquals(originalSize, new ZipEightByteInteger(centralDirectoryData, off));
        off += DWORD;
        assertEquals(compressedSize, new ZipEightByteInteger(centralDirectoryData, off));
        off += DWORD;
        assertEquals(relativeHeaderOffset, new ZipEightByteInteger(centralDirectoryData, off));
        off += DWORD;
        assertEquals(diskStart, new ZipLong(centralDirectoryData, off));
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];

        System.arraycopy(new ZipEightByteInteger(1024L).getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(512L).getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(100L).getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(new ZipLong(1L).getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
            assertEquals(new ZipEightByteInteger(1024L), extraField.getSize());
            assertEquals(new ZipEightByteInteger(512L), extraField.getCompressedSize());
            assertEquals(new ZipEightByteInteger(100L), extraField.getRelativeHeaderOffset());
            assertEquals(new ZipLong(1L), extraField.getDiskStartNumber());
        } catch (ZipException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithoutAllFields() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[2 * DWORD];

        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
            fail("Expected ZipException: " + LFH_MUST_HAVE_BOTH_SIZES_MSG);
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStartNumber() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];

        System.arraycopy(new ZipLong(1L).getBytes(), 0, centralDirectoryData, centralDirectoryData.length - WORD, WORD);

        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
            assertEquals(null, extraField.getSize());
            assertEquals(null, extraField.getCompressedSize());
            assertEquals(null, extraField.getRelativeHeaderOffset());
            assertEquals(new ZipLong(1L), extraField.getDiskStartNumber());
        } catch (ZipException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private void addSizes(byte[] data, ZipEightByteInteger originalSize, ZipEightByteInteger compressedSize) {
        int off = 0;
        if (originalSize != null) {
            System.arraycopy(originalSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
    }
}