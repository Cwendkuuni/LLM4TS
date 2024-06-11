import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMissingSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMissingCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, compressedSize);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800L);
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[3 * DWORD];
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStartNumber = new ZipLong(0x5678L);
        byte[] data = new byte[3 * DWORD + WORD];
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        System.arraycopy(diskStartNumber.getBytes(), 0, data, 3 * DWORD, WORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStartNumber = new ZipLong(0x5678L);
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingFields() {
        byte[] centralDirectoryData = new byte[24];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(false, true, true, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, false, true, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, false, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), extraField.getDiskStartNumber());
    }
}