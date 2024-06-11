import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateInstance() {
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertNotNull(zfe);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertEquals(16, zfe.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L),
            new ZipEightByteInteger(12345L), new ZipLong(67890L));
        assertEquals(32, zfe.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[16];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromLocalFileData(buffer, 0, 16);
        assertEquals(new ZipEightByteInteger(1024L), zfe.getSize());
        assertEquals(new ZipEightByteInteger(512L), zfe.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingBothSizes() {
        byte[] buffer = new byte[8];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromLocalFileData(buffer, 0, 8);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingOriginalSize() {
        byte[] buffer = new byte[12];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromLocalFileData(buffer, 0, 12);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingCompressedSize() {
        byte[] buffer = new byte[14];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromLocalFileData(buffer, 0, 14);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        byte[] data = zfe.getLocalFileDataData();
        assertEquals(16, data.length);
        assertEquals(1024L, ZipShort.getValue(data, 0));
        assertEquals(512L, ZipShort.getValue(data, 4));
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L),
            new ZipEightByteInteger(12345L), new ZipLong(67890L));
        byte[] data = zfe.getCentralDirectoryData();
        assertEquals(32, data.length);
        assertEquals(1024L, ZipShort.getValue(data, 0));
        assertEquals(512L, ZipShort.getValue(data, 4));
        assertEquals(12345L, ZipShort.getValue(data, 8));
        assertEquals(67890L, ZipLong.getValue(data, 16));
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingAllFields() {
        byte[] buffer = new byte[0];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 0);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingOriginalSize() {
        byte[] buffer = new byte[12];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 12);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingCompressedSize() {
        byte[] buffer = new byte[16];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 16);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingRelativeHeaderOffset() {
        byte[] buffer = new byte[20];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 20);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingDiskStartNumber() {
        byte[] buffer = new byte[22];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 22);
        throw new AssertionError("Expected ZipException but was not thrown");
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[32];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 32);
        zfe.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(1024L), zfe.getSize());
        assertEquals(new ZipEightByteInteger(512L), zfe.getCompressedSize());
        assertEquals(new ZipEightByteInteger(12345L), zfe.getRelativeHeaderOffset());
        assertEquals(new ZipLong(67890L), zfe.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataMissingOriginalSize() throws ZipException {
        byte[] buffer = new byte[28];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 28);
        zfe.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(null, zfe.getSize());
        assertEquals(new ZipEightByteInteger(512L), zfe.getCompressedSize());
        assertEquals(new ZipEightByteInteger(12345L), zfe.getRelativeHeaderOffset());
        assertEquals(new ZipLong(67890L), zfe.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataMissingCompressedSize() throws ZipException {
        byte[] buffer = new byte[28];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 28);
        zfe.reparseCentralDirectoryData(true, false, true, true);
        assertEquals(new ZipEightByteInteger(1024L), zfe.getSize());
        assertEquals(null, zfe.getCompressedSize());
        assertEquals(new ZipEightByteInteger(12345L), zfe.getRelativeHeaderOffset());
        assertEquals(new ZipLong(67890L), zfe.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataMissingRelativeHeaderOffset() throws ZipException {
        byte[] buffer = new byte[24];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 24);
        zfe.reparseCentralDirectoryData(true, true, false, true);
        assertEquals(new ZipEightByteInteger(1024L), zfe.getSize());
        assertEquals(new ZipEightByteInteger(512L), zfe.getCompressedSize());
        assertEquals(null, zfe.getRelativeHeaderOffset());
        assertEquals(new ZipLong(67890L), zfe.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataMissingDiskStartNumber() throws ZipException {
        byte[] buffer = new byte[20];
        Zip64ExtendedInformationExtraField zfe = new Zip64ExtendedInformationExtraField();
        zfe.parseFromCentralDirectoryData(buffer, 0, 20);
        zfe.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(new ZipEightByteInteger(1024L), zfe.getSize());
        assertEquals(new ZipEightByteInteger(512L), zfe.getCompressedSize());
        assertEquals(new ZipEightByteInteger(12345L), zfe.getRelativeHeaderOffset());
        assertEquals(null, zfe.getDiskStartNumber());
    }
}