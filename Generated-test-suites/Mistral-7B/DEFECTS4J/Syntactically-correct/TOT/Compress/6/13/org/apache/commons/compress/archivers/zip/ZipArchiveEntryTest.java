import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstruction() {
        // Test empty constructor
        ZipArchiveEntry emptyEntry = new ZipArchiveEntry();
        Assert.assertNull(emptyEntry.getName());
        Assert.assertEquals(-1, emptyEntry.getMethod());
        Assert.assertEquals(0, emptyEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, emptyEntry.getPlatform());
        Assert.assertEquals(0, emptyEntry.getExternalAttributes());
        Assert.assertNull(emptyEntry.getExtraFields());

        // Test constructor with name
        ZipArchiveEntry nameOnlyEntry = new ZipArchiveEntry("testEntry.txt");
        Assert.assertEquals("testEntry.txt", nameOnlyEntry.getName());
        Assert.assertEquals(-1, nameOnlyEntry.getMethod());
        Assert.assertEquals(0, nameOnlyEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, nameOnlyEntry.getPlatform());
        Assert.assertEquals(0, nameOnlyEntry.getExternalAttributes());
        Assert.assertNull(nameOnlyEntry.getExtraFields());

        // Test constructor with ZipEntry
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(new File("test.zip"));
            ZipEntry zipEntry = zipFile.getEntry("testEntry.txt");
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
            Assert.assertEquals("testEntry.txt", zipArchiveEntry.getName());
            Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
            Assert.assertEquals(ZipEntry.GENERIC_FLAG, zipArchiveEntry.getInternalAttributes());
            Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
            Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
            Assert.assertNull(zipArchiveEntry.getExtraFields());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Test constructor with ZipArchiveEntry
        ZipArchiveEntry cloneEntry = null;
        try {
            cloneEntry = new ZipArchiveEntry(new ZipArchiveEntry("testEntry.txt"));
            Assert.assertEquals("testEntry.txt", cloneEntry.getName());
            Assert.assertEquals(-1, cloneEntry.getMethod());
            Assert.assertEquals(0, cloneEntry.getInternalAttributes());
            Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, cloneEntry.getPlatform());
            Assert.assertEquals(0, cloneEntry.getExternalAttributes());
            Assert.assertNull(cloneEntry.getExtraFields());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test constructor with File
        File file = new File("testFile.txt");
        ZipArchiveEntry fileEntry = new ZipArchiveEntry(file, "testFile.txt");
        Assert.assertEquals("testFile.txt", fileEntry.getName());
        Assert.assertEquals(-1, fileEntry.getMethod());
        Assert.assertEquals(0, fileEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, fileEntry.getPlatform());
        Assert.assertEquals(file.length(), fileEntry.getSize());
        Assert.assertEquals(file.lastModified(), fileEntry.getTime());
        Assert.assertNull(fileEntry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntrySettersAndGetters() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testEntry.txt");

        // Test setting and getting method
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());

        // Test setting and getting internal attributes
        entry.setInternalAttributes(ZipEntry.GENERIC_FLAG | ZipEntry.FILE_NAME_IN_ASCII);
        Assert.assertEquals(ZipEntry.GENERIC_FLAG | ZipEntry.FILE_NAME_IN_ASCII, entry.getInternalAttributes());

        // Test setting and getting platform
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());

        // Test setting and getting external attributes
        entry.setExternalAttributes(0x0201);
        Assert.assertEquals(0x0201, entry.getExternalAttributes());

        // Test setting and getting Unix permissions
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());

        // Test setting and getting extra fields
        ZipExtraField extraField = new ZipExtraField(ZipShort.UNIX_USER, "user:testuser");
        entry.addExtraField(extraField);
        Assert.assertNotNull(entry.getExtraField(ZipShort.UNIX_USER));
        Assert.assertEquals("user:testuser", entry.getExtraField(ZipShort.UNIX_USER).getValue());
    }

    @Test
    public void testZipArchiveEntryMethods() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testEntry.txt");

        // Test isSupportedCompressionMethod
        Assert.assertTrue(entry.isSupportedCompressionMethod());

        // Test equals
        ZipArchiveEntry equalEntry = new ZipArchiveEntry("testEntry.txt");
        Assert.assertTrue(entry.equals(equalEntry));
        Assert.assertFalse(entry.equals(null));
        Assert.assertFalse(entry.equals(""));

        // Test hashCode
        int hashCode1 = entry.hashCode();
        ZipArchiveEntry equalEntry2 = new ZipArchiveEntry("testEntry.txt");
        Assert.assertEquals(hashCode1, equalEntry2.hashCode());
        String differentNameEntry = new ZipArchiveEntry("testEntry2.txt");
        Assert.assertNotEquals(hashCode1, differentNameEntry.hashCode());

        // Test clone
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        Assert.assertEquals(entry, clonedEntry);
        Assert.assertNotSame(entry, clonedEntry);

        // Test getName
        Assert.assertEquals("testEntry.txt", entry.getName());

        // Test isDirectory
        Assert.assertFalse(entry.isDirectory());

        // Test getLocalFileDataExtra and setLocalFileDataExtra
        byte[] localExtraData = {0x01, 0x02, 0x03};
        entry.setLocalFileDataExtra(localExtraData);
        byte[] localExtraDataFromEntry = entry.getLocalFileDataExtra();
        Assert.assertArrayEquals(localExtraData, localExtraDataFromEntry);

        // Test getCentralDirectoryExtra and setCentralDirectoryExtra
        byte[] centralExtraData = {0x01, 0x02, 0x03};
        entry.setCentralDirectoryExtra(centralExtraData);
        byte[] centralExtraDataFromEntry = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(centralExtraData, centralExtraDataFromEntry);

        // Test getLocalFileDataExtraData and setLocalFileDataExtraData
        byte[] localExtraDataData = {0x01, 0x02, 0x03};
        entry.setLocalFileDataExtraData(localExtraDataData);
        byte[] localExtraDataDataFromEntry = entry.getLocalFileDataExtraData();
        Assert.assertArrayEquals(localExtraDataData, localExtraDataDataFromEntry);

        // Test getCentralDirectoryExtraData and setCentralDirectoryExtraData
        byte[] centralExtraDataData = {0x01, 0x02, 0x03};
        entry.setCentralDirectoryExtraData(centralExtraDataData);
        byte[] centralExtraDataDataFromEntry = entry.getCentralDirectoryExtraData();
        Assert.assertArrayEquals(centralExtraDataData, centralExtraDataDataFromEntry);

        // Test getExtraFields and addExtraField
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        ZipExtraField extraField = new ZipExtraField(ZipShort.UNIX_USER, "user:testuser");
        entry.addExtraField(extraField);
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(extraField, entry.getExtraFields()[0]);

        // Test getExtraFields and addExtraField (with existing extra field)
        entry.setExtraFields(new ZipExtraField[]{new ZipExtraField(ZipShort.UNIX_GROUP, "group:testgroup")});
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.UNIX_GROUP, "group:testgroup2");
        entry.addExtraField(extraField2);
        Assert.assertEquals(2, entry.getExtraFields().length);
        Assert.assertEquals(extraField, entry.getExtraFields()[0]);
        Assert.assertEquals(extraField2, entry.getExtraFields()[1]);

        // Test getExtraFields and addAsFirstExtraField
        entry.setExtraFields(new ZipExtraField[]{new ZipExtraField(ZipShort.UNIX_USER, "user:testuser2")});
        entry.addAsFirstExtraField(extraField);
        Assert.assertEquals(2, entry.getExtraFields().length);
        Assert.assertEquals(extraField, entry.getExtraFields()[0]);
        Assert.assertEquals(extraField2, entry.getExtraFields()[1]);

        // Test getExtraFields and removeExtraField
        entry.removeExtraField(ZipShort.UNIX_GROUP);
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(extraField, entry.getExtraFields()[0]);

        // Test getExtraFields and getExtraField
        ZipExtraField extraFieldFromGetExtraFields = entry.getExtraField(ZipShort.UNIX_USER);
        Assert.assertEquals(extraField, extraFieldFromGetExtraFields);

        // Test getExtraFields and getExtraField (non-existing extra field)
        Assert.assertNull(entry.getExtraField(ZipShort.UNUSED));
    }
}