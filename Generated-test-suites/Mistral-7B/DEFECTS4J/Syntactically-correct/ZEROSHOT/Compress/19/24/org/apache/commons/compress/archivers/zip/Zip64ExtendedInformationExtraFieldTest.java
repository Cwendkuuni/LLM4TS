import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        assertNull(test.getSize());
        assertNull(test.getCompressedSize());
        assertNull(test.getRelativeHeaderOffset());
        assertNull(test.getDiskStartNumber());
    }

    @Test
    public void testSizeSettersAndGetters() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField(size, null);
        assertEquals(size, test.getSize());

        test.setCompressedSize(null);
        test.setRelativeHeaderOffset(null);
        test.setDiskStartNumber(null);
        assertEquals(size, test.getSize());
        assertNull(test.getCompressedSize());
        assertNull(test.getRelativeHeaderOffset());
        assertNull(test.getDiskStartNumber());

        test.setCompressedSize(new ZipEightByteInteger(5678L));
        assertEquals(size, test.getSize());
        assertEquals(new ZipEightByteInteger(5678L), test.getCompressedSize());
        assertNull(test.getRelativeHeaderOffset());
        assertNull(test.getDiskStartNumber());

        test.setRelativeHeaderOffset(new ZipEightByteInteger(9012L));
        assertEquals(size, test.getSize());
        assertEquals(new ZipEightByteInteger(5678L), test.getCompressedSize());
        assertEquals(new ZipEightByteInteger(9012L), test.getRelativeHeaderOffset());
        assertNull(test.getDiskStartNumber());

        test.setDiskStartNumber(new ZipLong(3L));
        assertEquals(size, test.getSize());
        assertEquals(new ZipEightByteInteger(5678L), test.getCompressedSize());
        assertEquals(new ZipEightByteInteger(9012L), test.getRelativeHeaderOffset());
        assertEquals(new ZipLong(3L), test.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSizeNullConstructor() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSizeNullSetter() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.setSize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompressedSizeNullConstructor() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompressedSizeNullSetter() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.setCompressedSize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelativeHeaderOffsetNullConstructor() {
        new Zip64ExtendedInformationExtraField(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelativeHeaderOffsetNullSetter() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.setRelativeHeaderOffset(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiskStartNumberNullConstructor() {
        new Zip64ExtendedInformationExtraField(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiskStartNumberNullSetter() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.setDiskStartNumber(null);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        int offset = 0;

        // Set size
        System.arraycopy(size.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;

        // Set compressed size
        System.arraycopy(compressedSize.getBytes(), 0, buffer, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;

        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(size, test.getSize());
        assertEquals(compressedSize, test.getCompressedSize());
        assertNull(test.getRelativeHeaderOffset());
        assertNull(test.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[2 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        byte[] relativeHeaderOffsetBytes = new byte[ZipConstants.DWORD];
        System.arraycopy(new byte[4], 0, relativeHeaderOffsetBytes, 0, 4);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(relativeHeaderOffsetBytes);
        ZipLong diskStartNumber = new ZipLong(3L);

        // Set size
        System.arraycopy(size.getBytes(), 0, buffer, 0, ZipConstants.DWORD);

        // Set compressed size
        System.arraycopy(compressedSize.getBytes(), 0, buffer, ZipConstants.DWORD, ZipConstants.DWORD);

        // Set relative header offset
        System.arraycopy(relativeHeaderOffsetBytes, 0, buffer, 2 * ZipConstants.DWORD, ZipConstants.DWORD);

        // Set disk start number
        System.arraycopy(diskStartNumber.getBytes(), 0, buffer, 3 * ZipConstants.DWORD, ZipConstants.WORD);

        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(size, test.getSize());
        assertEquals(compressedSize, test.getCompressedSize());
        assertEquals(relativeHeaderOffset, test.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, test.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1234L), new ZipEightByteInteger(5678L));
        byte[] data = test.getLocalFileDataData();
        assertEquals(2 * ZipConstants.DWORD, data.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1234L), new ZipEightByteInteger(5678L),
            new ZipEightByteInteger(9012L), new ZipLong(3L));
        byte[] data = test.getCentralDirectoryData();
        int expectedLength = 3 * ZipConstants.DWORD + ZipConstants.WORD;
        assertEquals(expectedLength, data.length);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, test.getLocalFileDataLength());
        test.setSize(new ZipEightByteInteger(1234L));
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), test.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, test.getCentralDirectoryLength());
        test.setSize(new ZipEightByteInteger(1234L));
        test.setCompressedSize(new ZipEightByteInteger(5678L));
        assertEquals(new ZipShort((2 * ZipConstants.DWORD) + ZipConstants.WORD), test.getCentralDirectoryLength());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        Zip64ExtendedInformationExtraField test = new Zip64ExtendedInformationExtraField();
        test.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        test.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(1234L), test.getSize());
        assertEquals(new ZipEightByteInteger(5678L), test.getCompressedSize());
        assertEquals(new ZipEightByteInteger(9012L), test.getRelativeHeaderOffset());
        assertEquals(new ZipLong(3L), test.getDiskStartNumber());
    }
}