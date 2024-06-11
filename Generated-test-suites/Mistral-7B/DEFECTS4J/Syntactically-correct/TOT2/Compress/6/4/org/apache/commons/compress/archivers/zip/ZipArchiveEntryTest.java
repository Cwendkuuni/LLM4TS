import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor_WithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructor_WithZipEntry() throws IOException {
        String name = "test.txt";
        byte[] extraData = new byte[10];
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        zipEntry.setExtra(extraData);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtra());
    }

    @Test
    public void testConstructor_WithZipArchiveEntry() throws IOException {
        String name = "test.txt";
        byte[] extraData = new byte[10];
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        zipEntry.setExtra(extraData);

        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry copyZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);

        Assert.assertEquals(name, copyZipArchiveEntry.getName());
        Assert.assertArrayEquals(extraData, copyZipArchiveEntry.getExtra());
    }

    @Test
    public void testConstructor_WithFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithNegativeMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(3);
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        zipArchiveEntry.setInternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        zipArchiveEntry.setInternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        zipArchiveEntry.setExternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        zipArchiveEntry.setExternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testAddExtraField() {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.addAsFirstExtraField(extraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testRemoveExtraField() {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(headerId);
        Assert.assertEquals(0, zipArchiveEntry.getExtraFields().length);
        Assert.assertNull(zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testGetExtraField() {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField actualExtraField = zipArchiveEntry.getExtraField(headerId);
        Assert.assertNotNull(actualExtraField);
        Assert.assertArrayEquals(extraData, actualExtraField.getLocalFileDataData());
    }

    @Test
    public void testSetExtra() throws IOException {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getLocalFileDataData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.addExtraField(extraField);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zipArchiveEntry.writeExtraTo(byteArrayOutputStream);
        byte[] actualExtraData = byteArrayOutputStream.toByteArray();
        Assert.assertArrayEquals(extraData, actualExtraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipShort headerId = new ZipShort(ZipShort.CRC32);
        ZipExtraField extraField = new ZipExtraField(headerId, new byte[10]);
        byte[] extraData = new byte[10];
        System.arraycopy(extraField.getCentralDirectoryData(), 0, extraData, 0, extraData.length);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
        ZipExtraField actualExtraField = zipArchiveEntry.getExtraField(headerId);
        Assert.assertNotNull(actualExtraField);
        Assert.assertArrayEquals(extraData, actualExtraField.getCentralDirectoryData());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertEquals("", zipArchiveEntry.getName());
        zipArchiveEntry.setName("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        Assert.assertFalse(zipArchiveEntry.isDirectory());
        zipArchiveEntry.setName("test.txt/");
        Assert.assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name);
        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        String name1 = "test.txt";
        String name2 = "test2.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name2);
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry(name1);
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String name = "test.txt";
        byte[] extraData = new byte[10];
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        zipEntry.setExtra(extraData);

        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();

        Assert.assertEquals(name, clonedZipArchiveEntry.getName());
        Assert.assertArrayEquals(extraData, clonedZipArchiveEntry.getExtra());
    }

    @Test
    public void testSetUnixMode_WithInvalidMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        try {
            zipArchiveEntry.setUnixMode(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}