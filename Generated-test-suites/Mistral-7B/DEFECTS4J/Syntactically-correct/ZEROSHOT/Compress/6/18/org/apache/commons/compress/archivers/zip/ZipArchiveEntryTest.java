import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.io.ByteStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateEntryWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assertions.assertEquals("test.txt", entry.getName());
        Assertions.assertNull(entry.getMethod());
        Assertions.assertEquals(0, entry.getInternalAttributes());
        Assertions.assertEquals(0, entry.getExternalAttributes());
        Assertions.assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreateEntryWithFile() {
        File inputFile = new File("testFile.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        Assertions.assertEquals("test.txt", entry.getName());
        Assertions.assertEquals(inputFile.length(), entry.getSize());
        Assertions.assertEquals(inputFile.lastModified(), entry.getTime());
        Assertions.assertNull(entry.getMethod());
        Assertions.assertEquals(0, entry.getInternalAttributes());
        Assertions.assertEquals(0, entry.getExternalAttributes());
        Assertions.assertNull(entry.getExtraFields());
    }

    @Test
    public void testCloneEntry() throws Exception {
        File inputFile = new File("testFile.txt");
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(inputFile, "test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assertions.assertEquals(originalEntry.getName(), clonedEntry.getName());
        Assertions.assertEquals(originalEntry.getSize(), clonedEntry.getSize());
        Assertions.assertEquals(originalEntry.getTime(), clonedEntry.getTime());
        Assertions.assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        Assertions.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assertions.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assertions.assertArrayEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assertions.assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assertions.assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED);
        Assertions.assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assertions.assertEquals(0, entry.getInternalAttributes());
        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_NAMED);
        Assertions.assertEquals(ZipEntry.FLAG_UNIX_NAMED, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_NAMED);
        Assertions.assertEquals(ZipEntry.FLAG_UNIX_NAMED, entry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x12345678L);
        Assertions.assertEquals(0x12345678L, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assertions.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assertions.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assertions.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assertions.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[] {extraField});
        Assertions.assertArrayEquals(new ZipExtraField[] {extraField}, entry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[] {extraField});
        Assertions.assertEquals(extraField, entry.getExtraField(new ZipShort(extraField.getHeaderId())));
        Assertions.assertNull(entry.getExtraField(new ZipShort(1)));
    }

    @Test
    public void testSetExtra() throws Exception {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[] {0x01, 0x02, 0x03};
        ZipExtraField extraField = new ZipExtendedTimestamps();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        extraField.writeTo(os);
        entry.setExtra(extraData);
        Assertions.assertArrayEquals(extraData, entry.getExtra());
        Assertions.assertArrayEquals(extraData, entry.getLocalFileDataExtra());
        Assertions.assertArrayEquals(extraData, entry.getCentralDirectoryExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws Exception {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[] {0x01, 0x02, 0x03};
        ZipExtraField extraField = new ZipExtendedTimestamps();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        extraField.writeTo(os);
        entry.setCentralDirectoryExtra(extraData);
        Assertions.assertArrayEquals(extraData, entry.getCentralDirectoryExtra());
    }
}

class ZipExtendedTimestamps extends ZipExtendedInformation {
    public ZipExtendedTimestamps() {
        setHeaderId(ZipShort.CREATE_TIME);
    }

    @Override
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws IOException {
        Date date = new Date(Bytes.readInt24(data, offset));
        setCreateTime(date);
    }

    @Override
    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws IOException {
        Date date = new Date(Bytes.readInt24ToLong(data, offset));
        setCreateTime(date);
    }

    @Override
    public byte[] getLocalFileDataData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        writeTo(os);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] getCentralDirectoryData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        writeTo(os);
        return outputStream.toByteArray();
    }
}