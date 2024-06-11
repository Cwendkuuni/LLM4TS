import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZipArchiveEntryTest {

    @Test
    public void testDefaultConstruction() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        assertEquals("", zipArchiveEntry.getName());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testConstructionWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testConstructionWithZipEntry() throws Exception {
        String entryName = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entryName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals(entryName, zipArchiveEntry.getName());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testConstructionWithAnotherZipArchiveEntry() throws Exception {
        String entryName = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entryName);
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry copiedZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
        assertEquals(entryName, copiedZipArchiveEntry.getName());
        assertEquals(originalZipArchiveEntry.getInternalAttributes(), copiedZipArchiveEntry.getInternalAttributes());
        assertEquals(originalZipArchiveEntry.getExternalAttributes(), copiedZipArchiveEntry.getExternalAttributes());
        assertArrayEquals(originalZipArchiveEntry.getExtraFields(), copiedZipArchiveEntry.getExtraFields());
        assertEquals(originalZipArchiveEntry.isSupportedCompressionMethod(), copiedZipArchiveEntry.isSupportedCompressionMethod());
        assertEquals(originalZipArchiveEntry.getMethod(), copiedZipArchiveEntry.getMethod());
    }

    @Test
    public void testConstructionWithFile() {
        String inputFile = "test.txt";
        File file = new File(inputFile);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(file.length(), zipArchiveEntry.getSize());
        assertEquals(file.lastModified(), zipArchiveEntry.getTime());
        assertNull(zipArchiveEntry.getInternalAttributes());
        assertNull(zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        int unixMode = 0644;
        zipArchiveEntry.setUnixMode(unixMode);
        assertEquals(unixMode, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.UNKNOWN);
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(0);
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        int internalAttributes = 1;
        zipArchiveEntry.setInternalAttributes(internalAttributes);
        assertEquals(internalAttributes, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        long externalAttributes = 1;
        zipArchiveEntry.setExternalAttributes(externalAttributes);
        assertEquals(externalAttributes, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtraField("TestExtraField", new byte[10]);
        zipArchiveEntry.addExtraField(zipExtraField);
        assertArrayEquals(new ZipExtraField[] {zipExtraField}, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] zipExtraFields = new ZipExtraField[] {new ZipExtraField("TestExtraField1", new byte[10]), new ZipExtraField("TestExtraField2", new byte[20])};
        zipArchiveEntry.setExtraFields(zipExtraFields);
        assertArrayEquals(zipExtraFields, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtraField("TestExtraField", new byte[10]);
        zipArchiveEntry.addExtraField(zipExtraField);
        assertSame(zipExtraField, zipArchiveEntry.getExtraField(new ZipShort("TestExtraField")));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.getExtraField(new ZipShort("TestExtraField"));
    }

    @Test
    public void testSetGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testMergeExtraFields() throws Exception {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtraField("TestExtraField1", "TestData1".getBytes());
        ZipExtraField zipExtraField2 = new ZipExtraField("TestExtraField2", "TestData2".getBytes());
        ZipExtraField zipExtraField3 = new ZipExtraField("TestExtraField3", "TestData3".getBytes());
        ZipExtraField[] zipExtraFields1 = new ZipExtraField[] {zipExtraField1, zipExtraField2};
        ZipExtraField[] zipExtraFields2 = new ZipExtraField[] {zipExtraField2, zipExtraField3};
        zipArchiveEntry.setExtraFields(zipExtraFields1);
        zipArchiveEntry.mergeExtraFields(zipExtraFields2, false);
        assertArrayEquals(new ZipExtraField[] {zipExtraField1, zipExtraField2, zipExtraField3}, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testSetLocalFileDataExtra() throws Exception {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] localFileDataExtra = "TestDataLocal".getBytes();
        zipArchiveEntry.setExtra(localFileDataExtra);
        byte[] mergedLocalFileDataExtra = ExtraFieldUtils.mergeLocalFileDataData(zipArchiveEntry.getExtraFields());
        assertArrayEquals(localFileDataExtra, mergedLocalFileDataExtra);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws Exception {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtra = "TestDataCentral".getBytes();
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtra);
        byte[] mergedCentralDirectoryExtra = ExtraFieldUtils.mergeCentralDirectoryData(zipArchiveEntry.getExtraFields());
        assertArrayEquals(centralDirectoryExtra, mergedCentralDirectoryExtra);
    }
}