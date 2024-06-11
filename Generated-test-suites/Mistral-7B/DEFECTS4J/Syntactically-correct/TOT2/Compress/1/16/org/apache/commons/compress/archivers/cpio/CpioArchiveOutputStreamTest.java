import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Tests for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private final static String TEST_FILE_NAME = "testFile.cpio";

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioArchiveOutputStream.close();
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)} .
     * Verifies that a new CpioArchiveOutputStream instance can be created with a specified format.
     */
    @Test
    public void testCpioArchiveOutputStreamCreation() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        Assert.assertNotNull(cpioArchiveOutputStream);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} .
     * Verifies that a new entry with a specified name and size can be added to the CpioArchiveOutputStream.
     */
    @Test
    public void testPutNextEntry() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write("Test file content".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entry = new CpioArchiveEntry(inputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals("testFile.txt", entry.getName());
        Assert.assertEquals(15, entry.getSize());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()} .
     * Verifies that the finish method correctly writes the trailer entry to the CpioArchiveOutputStream.
     */
    @Test
    public void testFinish() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile1.txt"));
        cpioArchiveOutputStream.write("Test file 1 content".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile2.txt"));
        cpioArchiveOutputStream.write("Test file 2 content".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] header = new byte[CpioUtil.HEADER_SIZE];

        inputStream.read(header, 0, CpioUtil.HEADER_SIZE);

        Assert.assertTrue(CpioUtil.checkMagicHeader(header, CpioConstants.FORMAT_NEW));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} .
     * Verifies that data is correctly written to the CpioArchiveEntry.
     */
    @Test
    public void testWrite() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write("Test file content".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] buffer = new byte[15];

        inputStream.read(buffer, 0, 15);

        Assert.assertArrayEquals("Test file content".getBytes(), buffer);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()} .
     * Verifies that the CpioArchiveOutputStream is correctly closed.
     */
    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write("Test file content".getBytes());
        cpioArchiveOutputStream.close();

        Assert.assertFalse(cpioArchiveOutputStream.isOpen());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(int)} .
     * Verifies that an integer can be correctly written to the CpioArchiveOutputStream.
     */
    @Test
    public void testWriteInt() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write(123);
        cpioArchiveOutputStream.write(45);
        cpioArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] buffer = new byte[2];

        inputStream.read(buffer, 0, 2);

        Assert.assertArrayEquals(new byte[]{123, 45}, buffer);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} with duplicate entry name.
     * Verifies that an IOException is thrown when trying to add a duplicate entry to the CpioArchiveOutputStream.
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntryName() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with invalid offset and length.
     * Verifies that an IndexOutOfBoundsException is thrown when trying to write data with an invalid offset and length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteInvalidOffsetAndLength() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write(-1, 10, 15);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with invalid length.
     * Verifies that an IOException is thrown when trying to write data with an invalid length.
     */
    @Test(expected = IOException.class)
    public void testWriteInvalidLength() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write("Test file content".getBytes(), 10, 15);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} with incorrect data size.
     * Verifies that an IOException is thrown when the written data size does not match the expected size.
     */
    @Test(expected = IOException.class)
    public void testWriteIncorrectDataSize() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("testFile.txt"));
        cpioArchiveOutputStream.write("Test file content".getBytes(), 15, 10);
    }
}