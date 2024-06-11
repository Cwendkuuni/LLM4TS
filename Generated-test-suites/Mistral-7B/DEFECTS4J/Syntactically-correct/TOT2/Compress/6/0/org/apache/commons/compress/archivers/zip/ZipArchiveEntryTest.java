package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.utils.io.ByteSource;
import org.apache.commons.compress.utils.io.ByteTarget;
import org.apache.commons.compress.utils.test.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    private static final String TEST_FILE = "test.txt";
    private static final int TEST_METHOD = ZipEntry.STORED;
    private static final int TEST_INTERNAL_ATTRIBUTES = 0;
    private static final long TEST_EXTERNAL_ATTRIBUTES = 0x12345678L;
    private static final int TEST_PLATFORM = ZipArchiveEntry.PLATFORM_FAT;
    private static final byte[] TEST_EXTRA_DATA = new byte[]{(byte) 0x54, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x74};

    private ZipArchiveEntry entry;

    @Before
    public void setUp() throws IOException {
        entry = new ZipArchiveEntry(TEST_FILE);
        entry.setMethod(TEST_METHOD);
        entry.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
        entry.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
        entry.setPlatform(TEST_PLATFORM);
        entry.setUnixMode(0644);
        entry.setExtraFields(new ZipExtraField[]{new CustomExtraField(ZipShort.UNIX_USER, "user"), new CustomExtraField(ZipShort.UNIX_GROUP, "group")});
    }

    @Test
    public void testGettersAndSetters() {
        Assert.assertEquals(TEST_FILE, entry.getName());
        Assert.assertEquals(TEST_METHOD, entry.getMethod());
        Assert.assertEquals(TEST_INTERNAL_ATTRIBUTES, entry.getInternalAttributes());
        Assert.assertEquals(TEST_EXTERNAL_ATTRIBUTES, entry.getExternalAttributes());
        Assert.assertEquals(TEST_PLATFORM, entry.getPlatform());
        Assert.assertEquals("user", entry.getExtraField(ZipShort.UNIX_USER).getValue());
        Assert.assertEquals("group", entry.getExtraField(ZipShort.UNIX_GROUP).getValue());

        entry.setName("test2.txt");
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setInternalAttributes(0xFFFF);
        entry.setExternalAttributes(0x87654321L);
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);
        entry.setExtraFields(new ZipExtraField[]{new CustomExtraField(ZipShort.UNIX_USER, "user2"), new CustomExtraField(ZipShort.UNIX_GROUP, "group2")});

        Assert.assertEquals("test2.txt", entry.getName());
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
        Assert.assertEquals(0xFFFF, entry.getInternalAttributes());
        Assert.assertEquals(0x87654321L, entry.getExternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        Assert.assertEquals("user2", entry.getExtraField(ZipShort.UNIX_USER).getValue());
        Assert.assertEquals("group2", entry.getExtraField(ZipShort.UNIX_GROUP).getValue());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        Assert.assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(-1);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(TEST_FILE, entry.getName());

        entry.setName("test2.txt");
        Assert.assertEquals("test2.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(entry.isDirectory());

        entry.setName(TEST_FILE + "/");
        Assert.assertTrue(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(TEST_FILE.hashCode(), entry.hashCode());

        entry.setName("test2.txt");
        Assert.assertEquals("test2.txt".hashCode(), entry.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry2 = new ZipArchiveEntry(TEST_FILE);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setName("test2.txt");
        Assert.assertFalse(entry.equals(entry2));

        entry2.setName(TEST_FILE);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setMethod(ZipEntry.DEFLATED);
        Assert.assertFalse(entry.equals(entry2));

        entry2.setMethod(TEST_METHOD);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setInternalAttributes(0xFFFF);
        Assert.assertFalse(entry.equals(entry2));

        entry2.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setExternalAttributes(0x87654321L);
        Assert.assertFalse(entry.equals(entry2));

        entry2.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertFalse(entry.equals(entry2));

        entry2.setPlatform(TEST_PLATFORM);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setUnixMode(0755);
        Assert.assertFalse(entry.equals(entry2));

        entry2.setUnixMode(0644);
        Assert.assertTrue(entry.equals(entry2));

        entry2.setExtraFields(new ZipExtraField[]{new CustomExtraField(ZipShort.UNIX_USER, "user2"), new CustomExtraField(ZipShort.UNIX_GROUP, "group2")});
        Assert.assertFalse(entry.equals(entry2));

        entry2.setExtraFields(entry.getExtraFields());
        Assert.assertTrue(entry.equals(entry2));
    }

    @Test
    public void testParseExtraData() throws IOException {
        byte[] localExtraData = Arrays.copyOfRange(TEST_EXTRA_DATA, 0, 5);
        byte[] centralDirectoryExtraData = Arrays.copyOfRange(TEST_EXTRA_DATA, 0, TEST_EXTRA_DATA.length);

        InputStream inputStream = new ByteArrayInputStream(localExtraData);
        ByteTarget outputStream = new ByteArrayOutputStream();

        entry.parseExtraData(inputStream, outputStream);

        byte[] parsedLocalExtraData = outputStream.toByteArray();
        byte[] parsedCentralDirectoryExtraData = entry.getCentralDirectoryExtra();

        Assert.assertArrayEquals(localExtraData, parsedLocalExtraData);
        Assert.assertArrayEquals(centralDirectoryExtraData, parsedCentralDirectoryExtraData);
    }

    @Test
    public void testSetExtraFields() throws IOException {
        byte[] testExtraData = new byte[0];

        byte[] localExtraData = new byte[TEST_EXTRA_DATA.length];
        System.arraycopy(TEST_EXTRA_DATA, 0, localExtraData, 0, TEST_EXTRA_DATA.length);
        ByteSource source = ByteSource.wrap(localExtraData);
        entry.setExtraFields(new ZipExtraField[]{new CustomExtraField(ZipShort.UNIX_USER, "user"), new CustomExtraField(ZipShort.UNIX_GROUP, "group")}, source);

        byte[] parsedCentralDirectoryExtraData = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(localExtraData, parsedCentralDirectoryExtraData);

        byte[] centralDirectoryExtraData = new byte[TEST_EXTRA_DATA.length];
        System.arraycopy(TEST_EXTRA_DATA, 0, centralDirectoryExtraData, 0, TEST_EXTRA_DATA.length);
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);

        byte[] parsedLocalFileDataExtraData = entry.getLocalFileDataExtra();
        Assert.assertArrayEquals(localExtraData, parsedLocalFileDataExtraData);

        InputStream inputStream = new ByteArrayInputStream(centralDirectoryExtraData);
        ByteTarget outputStream = new ByteArrayOutputStream();

        entry.parseExtraData(inputStream, outputStream);

        byte[] parsedLocalExtraData = outputStream.toByteArray();
        Assert.assertArrayEquals(localExtraData, parsedLocalExtraData);
    }

    @Test
    public void testToString() {
        String expected = String.format("ZipArchiveEntry{name='%s', method=%d, internalAttributes=0x%x, platform=%d, externalAttributes=0x%x, unixMode=0644, extraFields=%s}",
                TEST_FILE, TEST_METHOD, TEST_INTERNAL_ATTRIBUTES, TEST_PLATFORM, TEST_EXTERNAL_ATTRIBUTES);
        Assert.assertEquals(expected, entry.toString());
    }
}

class CustomExtraField extends ZipExtraField {
    private final String value;

    public CustomExtraField(ZipShort headerId, String value) {
        super(headerId);
        this.value = value;
    }

    @Override
    public void parseFromLocalFileData(byte[] data, int offset, int length) {
        // Do nothing
    }

    @Override
    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
        // Do nothing
    }

    @Override
    public byte[] getLocalFileDataData() {
        // Do nothing
        return new byte[0];
    }

    @Override
    public byte[] getCentralDirectoryData() {
        // Do nothing
        return new byte[0];
    }

    @Override
    public String getValue() {
        return value;
    }
}