package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    private ArchiveInputStream inputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;
    private ZipEncoding zipEncoding;
    private byte[] testData;

    @Before
    public void setUp() throws ArchiveException, IOException {
        InputStream is = new ByteArrayInputStream(testData);
        zipEncoding = ZipEncodingHelper.getZipEncoding(null);
        inputStream = new ArchiveInputStream(is);
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);
    }

    /**
     * Test getNextEntry method with valid input.
     */
    @Test
    public void testGetNextEntryValidInput() throws IOException {
        ArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
    }

    /**
     * Test getNextEntry method with empty input.
     */
    @Test(expected = IOException.class)
    public void testGetNextEntryEmptyInput() throws IOException {
        inputStream.close();
        inputStream = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);
        dumpArchiveInputStream.getNextEntry();
    }

    /**
     * Test getNextEntry method with invalid input.
     */
    @Test(expected = IOException.class)
    public void testGetNextEntryInvalidInput() throws IOException {
        inputStream.close();
        inputStream = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.generateRandomData(1000)));
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);
        dumpArchiveInputStream.getNextEntry();
    }

    /**
     * Test getNextEntry method with end of input.
     */
    @Test
    public void testGetNextEntryEndOfInput() throws IOException {
        ArchiveEntry entry1 = dumpArchiveInputStream.getNextEntry();
        ArchiveEntry entry2 = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry2);
    }

    /**
     * Test read method with valid input.
     */
    @Test
    public void testReadValidInput() throws IOException {
        byte[] expectedData = {"ExpectedData1".getBytes()};
        byte[] readData = new byte[expectedData.length];
        int read = dumpArchiveInputStream.read(readData, 0, expectedData.length);
        Assert.assertEquals(expectedData.length, read);
        Assert.assertArrayEquals(expectedData, readData);
    }

    /**
     * Test read method with empty input.
     */
    @Test
    public void testReadEmptyInput() throws IOException {
        byte[] readData = new byte[10];
        int read = dumpArchiveInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(-1, read);
    }

    /**
     * Test read method with invalid input.
     */
    @Test
    public void testReadInvalidInput() throws IOException {
        byte[] readData = new byte[10];
        dumpArchiveInputStream.read(readData, 0, readData.length + 1);
    }

    /**
     * Test read method with end of input.
     */
    @Test
    public void testReadEndOfInput() throws IOException {
        byte[] readData = new byte[10];
        int read = dumpArchiveInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(-1, read);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
    }

    /**
     * Test constructor with null inputStream.
     */
    @Test(expected = ArchiveException.class)
    public void testConstructorNullInputStream() {
        new DumpArchiveInputStream(null, zipEncoding);
    }

    /**
     * Test constructor with null zipEncoding.
     */
    @Test
    public void testConstructorNullZipEncoding() {
        new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData);
        InputStream is = new ByteArrayInputStream(testData);
        dumpArchiveInputStream = new DumpArchiveInputStream(is, zipEncoding);
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(testData.length, entry.getSize());
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = ArchiveException.class)
    public void testConstructorInvalidInput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TestUtils.generateRandomData(100));
        InputStream is = new ByteArrayInputStream(TestUtils.generateRandomData(100));
        new DumpArchiveInputStream(is, zipEncoding);
    }

    /**
     * Test constructor with end of input.
     */
    @Test(expected = IOException.class)
    public void testConstructorEndOfInput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TestUtils.generateRandomData(100));
        inputStream.close();
        inputStream = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);
    }

    /**
     * Set up test data.
     */
    @BeforeClass
    public static void setUpTestData() {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] clriBytes = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] bitsBytes = new byte[DumpArchiveConstants.TP_SIZE];

        // Fill in header bytes.
        System.arraycopy(
                DumpArchiveUtil.toByteArray(DumpArchiveConstants.MAGIC), 0, headerBytes, 0, 8);
        System.arraycopy(
                DumpArchiveUtil.toByteArray(DumpArchiveConstants.VERSION), 0, headerBytes, 8, 4);
        System.arraycopy(
                DumpArchiveUtil.toByteArray(DumpArchiveConstants.OS), 0, headerBytes, 12, 4);

        // Fill in CLRI bytes.
        byte[] clriEntryHeader = DumpArchiveUtil.toByteArray(DumpArchiveConstants.SEGMENT_TYPE.CLRI);
        byte[] clriEntryData = new byte[DumpArchiveConstants.TP_SIZE];
        System.arraycopy(clriEntryData, 0, clriBytes, 0, clriEntryData.length);
        System.arraycopy(clriEntryHeader, 0, clriBytes, clriEntryData.length, 8);

        // Fill in BITS bytes.
        byte[] bitsEntryHeader = DumpArchiveUtil.toByteArray(DumpArchiveConstants.SEGMENT_TYPE.BITS);
        byte[] bitsEntryData = new byte[DumpArchiveConstants.TP_SIZE];
        System.arraycopy(bitsEntryData, 0, bitsBytes, 0, bitsEntryData.length);
        System.arraycopy(bitsEntryHeader, 0, bitsBytes, bitsEntryData.length, 8);

        // Fill in test data.
        testData = new byte[1024];
        System.arraycopy(testData, 0, testData, 0, 100);
        System.arraycopy(testData, 100, testData, 512, 512);
        System.arraycopy(testData, 513, testData, 1021, 12);

        // Set up test data.
        byte[] testDataWithHeader = new byte[headerBytes.length + clriBytes.length + bitsBytes.length + testData.length];
        System.arraycopy(headerBytes, 0, testDataWithHeader, 0, headerBytes.length);
        System.arraycopy(clriBytes, 0, testDataWithHeader, headerBytes.length, clriBytes.length);
        System.arraycopy(bitsBytes, 0, testDataWithHeader, headerBytes.length + clriBytes.length, bitsBytes.length);
        System.arraycopy(testData, 0, testDataWithHeader, headerBytes.length + clriBytes.length + bitsBytes.length, testData.length);
    }
}