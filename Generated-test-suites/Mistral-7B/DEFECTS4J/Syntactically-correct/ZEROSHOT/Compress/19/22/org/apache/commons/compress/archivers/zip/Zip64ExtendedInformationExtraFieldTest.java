import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateInstanceWithValidValues() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234L);
        ZipLong diskStart = new ZipLong(5678L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testCreateInstanceWithNullValues() {
        try {
            Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("size or compressedSize is null", e.getMessage());
        }

        try {
            Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("size, compressedSize, relativeHeaderOffset or diskStart is null", e.getMessage());
        }
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, null, null, null);

        assertEquals(new ZipShort(DWORD * 2), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234L);
        ZipLong diskStart = new ZipLong(5678L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(new ZipShort((int) (2 * DWORD + DWORD + DWORD + WORD)), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);

        byte[] data = field.getLocalFileDataData();
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        assertArrayEquals(expectedData, data);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234L);
        ZipLong diskStart = new ZipLong(5678L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        byte[] data = field.getCentralDirectoryData();
        int expectedOffset = 0;
        addSizes(data, expectedOffset);
        if (relativeHeaderOffset != null) {
            int offset = expectedOffset;
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
            assertEquals((int) (expectedOffset + DWORD), data.length);
        }
        if (diskStart != null) {
            int offset = expectedOffset;
            System.arraycopy(diskStart.getBytes(), 0, data, offset, WORD);
            offset += WORD;
            assertEquals((int) (expectedOffset + WORD), data.length);
        }
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);

        addSizes(data);
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(null, field.getRelativeHeaderOffset());
        assertEquals(null, field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithValidData() {
        byte[] data = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234L);
        ZipLong diskStart = new ZipLong(5678L);

        addSizes(data);
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithInvalidData() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();

        try {
            field.parseFromCentralDirectoryData(data, 0, data.length);
            fail();
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 24 but is 16", e.getMessage());
        }
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234L);
        ZipLong diskStart = new ZipLong(5678L);

        addSizes(data);
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);

        field.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithInvalidData() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();

        try {
            field.parseFromCentralDirectoryData(data, 0, data.length);
            field.reparseCentralDirectoryData(true, true, true, true);
            fail();
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 24 but is 16", e.getMessage());
        }
    }

    private void addSizes(byte[] data, int offset) {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512L);

        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
    }
}