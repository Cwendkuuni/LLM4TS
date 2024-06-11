import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Zip64ExtendedInformationExtraField} class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test for the constructor with both size values.
     */
    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the constructor with only the size values.
     */
    @Test
    public void testConstructorWithOnlySizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(originalSize, compressedSize, null, null);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the constructor with all four values.
     */
    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger rho = new ZipEightByteInteger(42);
        ZipLong diskStart = new ZipLong(42L);
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField(originalSize, compressedSize, rho, diskStart);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(rho, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        addSizes(data);
        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with all fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[2 * DWORD + DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger rho = new ZipEightByteInteger(42);
        ZipLong diskStart = new ZipLong(42L);

        addSizes(rawCentralDirectoryData);
        System.arraycopy(originalSize.getBytes(), 0,
                         rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0,
                         rawCentralDirectoryData, DWORD, DWORD);
        System.arraycopy(rho.getBytes(), 0, rawCentralDirectoryData,
                         2 * DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0,
                                                rawCentralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(rho, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data without the relative header
     * offset field.
     */
    @Test
    public void testParseFromCentralDirectoryDataWithoutRelativeHeaderOffset()
        throws ZipException {
        byte[] rawCentralDirectoryData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);

        addSizes(rawCentralDirectoryData);
        System.arraycopy(originalSize.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0,
                                                rawCentralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data without the disk start field.
     */
    @Test
    public void testParseFromCentralDirectoryDataWithoutDiskStart() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[2 * DWORD + DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger rho = new ZipEightByteInteger(42);

        addSizes(rawCentralDirectoryData);
        System.arraycopy(originalSize.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);
        System.arraycopy(rho.getBytes(), 0, rawCentralDirectoryData, 2 * DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0,
                                                rawCentralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(rho, extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with empty data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithEmptyData() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[0];
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0,
                                                rawCentralDirectoryData.length);
    }

    /**
     * Test parsing from local file data with empty data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithEmptyData() throws ZipException {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField extraField =
            new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
    }
}