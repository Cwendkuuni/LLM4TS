import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ZipArchiveEntryTest {

    @Test
    public void testBasicCreation() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(0, entry.getSize());
        Assert.assertNull(entry.getTime());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertNull(entry.getInternalAttributes());
        Assert.assertNull(entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreationFromZipEntry() throws IOException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipEntry zipEntry = zipFile.getEntry("test.txt");
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertEquals(zipEntry.getSize(), archiveEntry.getSize());
        Assert.assertEquals(zipEntry.getTime(), archiveEntry.getTime());
        Assert.assertEquals(zipEntry.getMethod(), archiveEntry.getMethod());
        Assert.assertEquals(zipEntry.getPlatform(), archiveEntry.getPlatform());
        Assert.assertEquals(zipEntry.getCompressedSize(), archiveEntry.getCompressedSize());
        Assert.assertEquals(zipEntry.getCrc(), archiveEntry.getCrc());
        Assert.assertNotNull(archiveEntry.getExtraFields());
        zipFile.close();
    }

    @Test
    public void testCreationFromZipArchiveEntry() throws IOException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipEntry zipEntry = zipFile.getEntry("test.txt");
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clone = new ZipArchiveEntry(archiveEntry);
        Assert.assertNotNull(clone);
        Assert.assertEquals(archiveEntry.getName(), clone.getName());
        Assert.assertEquals(archiveEntry.getSize(), clone.getSize());
        Assert.assertEquals(archiveEntry.getTime(), clone.getTime());
        Assert.assertEquals(archiveEntry.getMethod(), clone.getMethod());
        Assert.assertEquals(archiveEntry.getPlatform(), clone.getPlatform());
        Assert.assertEquals(archiveEntry.getCompressedSize(), clone.getCompressedSize());
        Assert.assertEquals(archiveEntry.getCrc(), clone.getCrc());
        Assert.assertArrayEquals(archiveEntry.getExtra(), clone.getExtra());
        Assert.assertArrayEquals(archiveEntry.getLocalFileDataExtra(), clone.getLocalFileDataExtra());
        Assert.assertArrayEquals(archiveEntry.getCentralDirectoryExtra(), clone.getCentralDirectoryExtra());
        Assert.assertEquals(archiveEntry.getInternalAttributes(), clone.getInternalAttributes());
        Assert.assertEquals(archiveEntry.getExternalAttributes(), clone.getExternalAttributes());
        Assert.assertEquals(archiveEntry.getExtraFields(), clone.getExtraFields());
        zipFile.close();
    }

    @Test
    public void testCreationFromFile() throws IOException {
        File testFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "test.txt");
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(testFile.length(), entry.getSize());
        Assert.assertEquals(testFile.lastModified(), entry.getTime().getTime());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getCompressedSize());
        Assert.assertEquals(0, entry.getCrc());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(-1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPlatformWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(4);
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getInternalAttributes());
        entry.setInternalAttributes(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getInternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInternalAttributesWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(123);
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0);
        Assert.assertEquals(0, entry.getExternalAttributes());
        entry.setExternalAttributes(1);
        Assert.assertEquals(1, entry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExternalAttributesWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(123);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestampsExtraField();
        entry.addExtraField(extraField);
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertArrayEquals(extraField.getBytes(), entry.getExtra());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[]{new ZipExtendedTimestampsExtraField(), new ZipCommentExtraField("Test Comment")};
        entry.setExtraFields(extraFields);
        Assert.assertEquals(2, entry.getExtraFields().length);
        Assert.assertArrayEquals(extraFields[0].getBytes(), entry.getExtra());
        Assert.assertArrayEquals(extraFields[1].getBytes(), entry.getExtra());
    }

    @Test
    public void testMergeExtraFields() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] localExtra = new byte[]{0x01, 0x02, 0x03};
        byte[] centralExtra = new byte[]{0x04, 0x05, 0x06};
        entry.setExtra(localExtra);
        entry.setCentralDirectoryExtra(centralExtra);
        Assert.assertArrayEquals(Arrays.copyOf(localExtra, localExtra.length + centralExtra.length), entry.getExtra());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(-1);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        entry.setName("test2.txt");
        Assert.assertEquals("test2.txt", entry.getName());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
        Assert.assertEquals(entry2.hashCode(), entry2.hashCode());
        Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(entry1.equals(entry1));
        Assert.assertTrue(entry1.equals(entry2));
        Assert.assertTrue(entry2.equals(entry1));
        Assert.assertFalse(entry1.equals(null));
        Assert.assertFalse(entry1.equals(""));
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(entry1.equals(entry3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = entry.clone();
        Assert.assertNotNull(clone);
        Assert.assertEquals(entry.getName(), clone.getName());
        Assert.assertEquals(entry.getSize(), clone.getSize());
        Assert.assertEquals(entry.getTime(), clone.getTime());
        Assert.assertEquals(entry.getMethod(), clone.getMethod());
        Assert.assertEquals(entry.getPlatform(), clone.getPlatform());
        Assert.assertEquals(entry.getCompressedSize(), clone.getCompressedSize());
        Assert.assertEquals(entry.getCrc(), clone.getCrc());
        Assert.assertArrayEquals(entry.getExtra(), clone.getExtra());
        Assert.assertArrayEquals(entry.getLocalFileDataExtra(), clone.getLocalFileDataExtra());
        Assert.assertArrayEquals(entry.getCentralDirectoryExtra(), clone.getCentralDirectoryExtra());
        Assert.assertArrayEquals(entry.getExtraFields(), clone.getExtraFields());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[]{};
        entry.setLocalFileDataExtra(extra);
        Assert.assertArrayEquals(extra, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extra = new byte[]{};
        entry.setCentralDirectoryExtra(extra);
        Assert.assertArrayEquals(extra, entry.getCentralDirectoryExtra());
    }
}