package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.io.ByteArrayInputStream;
import org.apache.commons.compress.io.ByteArrays;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "testdata.dump";

    /**
     * Test that the constructor with default encoding works correctly.
     */
    @Test
    public void testDefaultConstructor() throws IOException, ArchiveException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testfile", entry.getName());

        inputStream.close();
    }

    /**
     * Test that the constructor with custom encoding works correctly.
     */
    @Test
    public void testCustomConstructor() throws IOException, ArchiveException {
        String encoding = "UTF-8";
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream, encoding);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testfile", entry.getName());

        inputStream.close();
    }

    /**
     * Test that an empty archive is correctly handled.
     */
    @Test
    public void testEmptyArchive() throws IOException, ArchiveException {
        byte[] testData = new byte[0];
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        Assert.assertNull(inputStream.getNextEntry());
        inputStream.close();
    }

    /**
     * Test that an archive with an invalid format is correctly handled.
     */
    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        byte[] testData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        new DumpArchiveInputStream(testInputStream);
    }

    /**
     * Test that an archive with an unexpected EOF is correctly handled.
     */
    @Test(expected = EOFException.class)
    public void testUnexpectedEof() throws IOException, ArchiveException {
        byte[] testData = readFile(TEST_DATA);
        byte[] subData = Arrays.copyOfRange(testData, 0, 100);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(subData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        Assert.assertNull(inputStream.getNextEntry());

        inputStream.close();
    }

    /**
     * Test that the close method correctly handles the case where no entry has been read yet.
     */
    @Test
    public void testCloseNoEntry() throws IOException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        inputStream.close();
    }

    /**
     * Test that the close method correctly handles the case where an entry has been read.
     */
    @Test
    public void testCloseReadEntry() throws IOException, ArchiveException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());

        inputStream.close();
    }

    /**
     * Test that the read method correctly handles the case where an entry has been read.
     */
    @Test
    public void testReadEntry() throws IOException, ArchiveException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());

        byte[] data = new byte[10];
        int bytesRead = inputStream.read(data, 0, data.length);
        Assert.assertEquals(data.length, bytesRead);

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testfile", entry.getName());

        bytesRead = inputStream.read(data, 0, data.length);
        Assert.assertEquals(entry.getSize(), bytesRead);

        inputStream.close();
    }

    /**
     * Test that the read method correctly handles the case where no entry has been read yet.
     */
    @Test
    public void testReadNoEntry() throws IOException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        int bytesRead = inputStream.read(new byte[10], 0, 10);
        Assert.assertEquals(-1, bytesRead);

        inputStream.close();
    }

    /**
     * Test that the read method correctly handles the case where the end of the entry has been reached.
     */
    @Test
    public void testReadEndOfEntry() throws IOException, ArchiveException {
        byte[] testData = readFile(TEST_DATA);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(testInputStream);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());

        byte[] data = new byte[entry.getSize()];
        int bytesRead = inputStream.read(data, 0, data.length);
        Assert.assertEquals(entry.getSize(), bytesRead);

        int bytesRead2 = inputStream.read(new byte[1], 0, 1);
        Assert.assertEquals(-1, bytesRead2);

        inputStream.close();
    }

    /**
     * Reads a file into a byte array.
     *
     * @param fileName the name of the file to read
     * @return the contents of the file as a byte array
     */
    private byte[] readFile(String fileName) throws IOException {
        return ByteArrays.toByteArray(Files.readAllBytes(Paths.get(fileName)));
    }

    static {
        if (ZipEncoding.class == null) {
            ZipEncodingHelper.ensureZipEncoding();
        }
    }
}