package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCpioArchiveOutputStreamConstruction() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertNull(cpioArchiveOutputStream.getEntry());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        CpioArchiveEntry actualEntry = cpioArchiveOutputStream.getEntry();
        assertEquals(testEntry.getName(), actualEntry.getName());
        assertEquals(testEntry.getFormat(), actualEntry.getFormat());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        cpioArchiveOutputStream.write("Test Data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        byte[] actualData = cpioArchiveOutputStream.getBuffer();
        assertArrayEquals(expectedData, actualData);

        // Verify the header
        CpioArchiveEntry expectedCpioArchiveEntry = new CpioArchiveEntry();
        expectedCpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        expectedCpioArchiveEntry.setName(testFileName);
        expectedCpioArchiveEntry.setSize(testData.length);

        CpioArchiveEntry actualCpioArchiveEntry = new CpioArchiveEntry(actualEntry);
        assertEquals(expectedCpioArchiveEntry.getName(), actualCpioArchiveEntry.getName());
        assertEquals(expectedCpioArchiveEntry.getFormat(), actualCpioArchiveEntry.getFormat());
        assertEquals(expectedCpioArchiveEntry.getSize(), actualCpioArchiveEntry.getSize());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile1 = new File("testFile1.txt");
        String testFileName1 = "testFile1.txt";
        byte[] testData1 = "Test Data 1".getBytes();

        ArchiveEntry testEntry1 = new CpioArchiveEntry(testFile1, testFileName1);
        cpioArchiveOutputStream.putArchiveEntry(testEntry1);
        cpioArchiveOutputStream.write(testData1);
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFile2 = new File("testFile2.txt");
        String testFileName2 = "testFile2.txt";
        byte[] testData2 = "Test Data 2".getBytes();

        ArchiveEntry testEntry2 = new CpioArchiveEntry(testFile2, testFileName2);
        cpioArchiveOutputStream.putArchiveEntry(testEntry2);
        cpioArchiveOutputStream.write(testData2);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] expectedTrailer = ArchiveUtils.readFileToByteArray(new File("test-trailer.cpio"));
        byte[] actualTrailer = cpioArchiveOutputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        assertFalse(cpioArchiveOutputStream.isFinished());
        assertTrue(cpioArchiveOutputStream.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.close();

        assertTrue(cpioArchiveOutputStream.isFinished());
        assertTrue(cpioArchiveOutputStream.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with duplicate entry.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(testEntry));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with null entry.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.putArchiveEntry(null));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with invalid format.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 42);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);

        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(testEntry));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * invalid offset.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithInvalidOffset() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        int invalidOffset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, invalidOffset, testData.length));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * invalid length.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithInvalidLength() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        int invalidLength = testData.length + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, 0, invalidLength));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * null data.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithNullData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = null;

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.write(testData, 0, testData.length));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * no data.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithNoData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = new byte[0];

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        int offset = 0;
        int length = 0;
        cpioArchiveOutputStream.write(testData, offset, length);

        byte[] expectedData = new byte[0];
        byte[] actualData = cpioArchiveOutputStream.getBuffer();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * valid data.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithValidData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData = "Test Data".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        int offset = 0;
        int length = testData.length;
        cpioArchiveOutputStream.write(testData, offset, length);

        byte[] expectedData = testData;
        byte[] actualData = cpioArchiveOutputStream.getBuffer();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with
     * multiple writes.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWriteWithMultipleWrites() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        byte[] testData1 = "Test Data 1".getBytes();
        byte[] testData2 = "Test Data 2".getBytes();

        ArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        int offset = 0;
        int length1 = testData1.length;
        cpioArchiveOutputStream.write(testData1, offset, length1);

        int length2 = testData2.length;
        cpioArchiveOutputStream.write(testData2, offset, length2);

        byte[] expectedData = new byte[testData1.length + testData2.length];
        System.arraycopy(testData1, 0, expectedData, 0, testData1.length);
        System.arraycopy(testData2, 0, expectedData, testData1.length, testData2.length);
        byte[] actualData = cpioArchiveOutputStream.getBuffer();
        assertArrayEquals(expectedData, actualData);
    }
}