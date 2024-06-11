import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreationWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithoutBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, null);
    }

    @Test
    public void testParsingFromLocalFileData() {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        addSizes(data);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParsingFromLocalFileDataWithInvalidDataLength() {
        byte[] data = new byte[2 * DWORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParsingFromCentralDirectoryData() {
        byte[] data = new byte[3 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParsingFromCentralDirectoryDataWithInvalidDataLength() {
        byte[] data = new byte[3 * DWORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testReparsingCentralDirectoryData() {
        byte[] rawData = new byte[3 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100L);

        System.arraycopy(size.getBytes(), 0, rawData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, rawData, 2 * DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawData, 0, rawData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
    }

    @Test
    public void testReparsingCentralDirectoryDataWithMissingFields() {
        byte[] rawData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawData, 0, rawData.length);
        extraField.reparseCentralDirectoryData(true, false, true, false);
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(false, true, true, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());

        extraField.reparseCentralDirectoryData(false, false, true, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
    }
}