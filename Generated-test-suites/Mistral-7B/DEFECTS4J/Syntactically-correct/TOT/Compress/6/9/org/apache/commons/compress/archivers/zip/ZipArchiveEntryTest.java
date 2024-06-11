package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIRECTORY_NAME = "testDir/";
    private static final String TEST_ZIP_FILE_NAME = "test.zip";
    private static final int TEST_UNIX_MODE = 0755;

    private File testFile;
    private File testDirectory;
    private File testZipFile;
    private ZipArchiveEntry zipEntry;

    @Before
    public void setUp() throws Exception {
        testFile = new File(TEST_FILE_NAME);
        testFile.createNewFile();
        testDirectory = new File(TEST_DIRECTORY_NAME);
        testDirectory.mkdir();
        testZipFile = new File(TEST_ZIP_FILE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
        if (testDirectory.exists()) {
            testDirectory.delete();
        }
        if (testZipFile.exists()) {
            testZipFile.delete();
        }
    }

    @Test
    public void testBasicConstruction() {
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(TEST_FILE_NAME, entry.toString());
        assertEquals(-1, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testZipEntryConstruction() throws ZipException, IOException {
        File testFile2 = new File(TEST_FILE_NAME + "_copy");
        testFile2.createNewFile();
        ZipArchiveEntry testEntry = new ZipArchiveEntry(new File(TEST_FILE_NAME).getAbsolutePath());
        ZipArchiveEntry entry = new ZipArchiveEntry(testEntry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(TEST_FILE_NAME, entry.toString());
        assertEquals(-1, entry.getMethod());
        assertEquals(testEntry.getLastModifiedDate().getTime(), entry.getTime());
        assertEquals(testEntry.getCrc(), entry.getCrc());
        assertEquals(testEntry.getSize(), entry.getSize());
        assertEquals(TEST_UNIX_MODE, entry.getUnixMode());
        assertEquals(testEntry.getPlatform(), entry.getPlatform());
        assertEquals(testEntry.getInternalAttributes(), entry.getInternalAttributes());
        assertEquals(testEntry.getExternalAttributes(), entry.getExternalAttributes());
        assertEquals(testEntry.getExtraFields(), entry.getExtraFields());
        assertArrayEquals(testEntry.getLocalFileDataExtra(), entry.getLocalFileDataExtra());
        assertArrayEquals(testEntry.getCentralDirectoryExtra(), entry.getCentralDirectoryExtra());
        assertFalse(entry.isSupportedCompressionMethod());

        testFile2.delete();
    }

    @Test
    public void testClone() throws Exception {
        File testFile3 = new File(TEST_FILE_NAME + "_clone");
        testFile3.createNewFile();
        ZipArchiveEntry testEntry = new ZipArchiveEntry(new File(TEST_FILE_NAME).getAbsolutePath());
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) testEntry.clone();
        assertEquals("testFile.txt", clonedEntry.getName());
        assertEquals(TEST_FILE_NAME, clonedEntry.toString());
        assertEquals(-1, clonedEntry.getMethod());
        assertEquals(testEntry.getLastModifiedDate().getTime(), clonedEntry.getTime());
        assertEquals(testEntry.getCrc(), clonedEntry.getCrc());
        assertEquals(testEntry.getSize(), clonedEntry.getSize());
        assertEquals(TEST_UNIX_MODE, clonedEntry.getUnixMode());
        assertEquals(testEntry.getPlatform(), clonedEntry.getPlatform());
        assertEquals(testEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(testEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertEquals(testEntry.getExtraFields(), clonedEntry.getExtraFields());
        assertArrayEquals(testEntry.getLocalFileDataExtra(), clonedEntry.getLocalFileDataExtra());
        assertArrayEquals(testEntry.getCentralDirectoryExtra(), clonedEntry.getCentralDirectoryExtra());
        assertFalse(clonedEntry.isSupportedCompressionMethod());

        testFile3.delete();
    }

    @Test
    public void testSetUnixMode() throws Exception {
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setUnixMode(TEST_UNIX_MODE);
        assertEquals(TEST_UNIX_MODE, entry.getUnixMode());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testGetPlatform() throws Exception {
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TestUtils.writeTestExtraData(os, extraData);
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setExtraFields(new ZipExtraField[] {new GenericTestExtraField(0, extraData)});
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        byte[] actualExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualExtraData);
        byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
        assertArrayEquals(TestUtils.APPEND_TEST_EXTRA_DATA, centralDirectoryExtra);
    }

    @Test
    public void testAddExtraField() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        byte[] newExtraData = new byte[15];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TestUtils.writeTestExtraData(os, extraData);
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test adding a new extra field to an empty entry
        entry.addExtraField(new GenericTestExtraField(0, extraData));
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        byte[] actualExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualExtraData);

        // Test adding a new extra field to an existing entry
        entry.addExtraField(new GenericTestExtraField(1, newExtraData));
        assertEquals(2, entry.getExtraFields().length);
        extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        actualExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualExtraData);

        extraField = entry.getExtraField(new ZipShort(1));
        assertNotNull(extraField);
        assertEquals(1, extraField.getHeaderId().getValue());
        byte[] actualNewExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(newExtraData, actualNewExtraData);

        byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
        byte[] expectedCentralDirectoryExtra = TestUtils.APPEND_TEST_EXTRA_DATA;
        assertArrayEquals(expectedCentralDirectoryExtra, centralDirectoryExtra);
    }

    @Test
    public void testAddAsFirstExtraField() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        byte[] newExtraData = new byte[15];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TestUtils.writeTestExtraData(os, extraData);
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test adding a new extra field as first to an empty entry
        entry.addAsFirstExtraField(new GenericTestExtraField(0, extraData));
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        byte[] actualExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualExtraData);

        // Test adding a new extra field as first to an existing entry
        entry.addAsFirstExtraField(new GenericTestExtraField(1, newExtraData));
        assertEquals(2, entry.getExtraFields().length);
        extraField = entry.getExtraField(new ZipShort(1));
        assertNotNull(extraField);
        assertEquals(1, extraField.getHeaderId().getValue());
        byte[] actualNewExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(newExtraData, actualNewExtraData);

        extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        byte[] actualFirstExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualFirstExtraData);

        byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
        byte[] expectedCentralDirectoryExtra = TestUtils.APPEND_TEST_EXTRA_DATA;
        assertArrayEquals(expectedCentralDirectoryExtra, centralDirectoryExtra);
    }

    @Test
    public void testRemoveExtraField() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        byte[] newExtraData = new byte[15];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TestUtils.writeTestExtraData(os, extraData);
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test removing an extra field that does not exist
        entry.removeExtraField(new ZipShort(0));
        assertEquals(0, entry.getExtraFields().length);

        // Test removing an extra field that exists
        entry.addExtraField(new GenericTestExtraField(0, extraData));
        entry.removeExtraField(new ZipShort(0));
        assertEquals(0, entry.getExtraFields().length);

        // Test removing a second extra field
        entry.addExtraField(new GenericTestExtraField(1, newExtraData));
        entry.removeExtraField(new ZipShort(1));
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetNonExistingExtraField() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.getExtraField(new ZipShort(0));
    }

    @Test
    public void testSetExtra() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TestUtils.writeTestExtraData(os, extraData);
        byte[] localExtraData = os.toByteArray();
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setExtra(localExtraData);
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraField(new ZipShort(0));
        assertNotNull(extraField);
        assertEquals(0, extraField.getHeaderId().getValue());
        byte[] actualExtraData = extraField.getLocalFileDataData();
        assertArrayEquals(extraData, actualExtraData);

        byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
        assertArrayEquals(TestUtils.APPEND_TEST_EXTRA_DATA, centralDirectoryExtra);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException, ZipException {
        byte[] extraData = new byte[10];
        byte[] centralDirectoryExtra = new byte[10 + TestUtils.APPEND_TEST_EXTRA_DATA.length];
        System.arraycopy(TestUtils.APPEND_TEST_EXTRA_DATA, 0, centralDirectoryExtra, 0, TestUtils.APPEND_TEST_EXTRA_DATA.length);
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setCentralDirectoryExtra(centralDirectoryExtra);
        byte[] actualCentralDirectoryExtra = entry.getCentralDirectoryExtra();
        assertArrayEquals(centralDirectoryExtra, actualCentralDirectoryExtra);
    }
}