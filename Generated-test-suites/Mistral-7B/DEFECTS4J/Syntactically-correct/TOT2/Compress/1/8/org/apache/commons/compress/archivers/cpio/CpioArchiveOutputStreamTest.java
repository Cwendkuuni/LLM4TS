import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private List<CpioArchiveEntry> entries = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byteArrayOutputStream = new ByteArrayOutputStream();
        entries.add(new CpioArchiveEntry());
        entries.get(0).setName("testfile1.txt");
        entries.get(0).setSize(10);
        cpioArchiveOutputStream.putNextEntry(entries.get(0));
        entries.add(new CpioArchiveEntry());
        entries.get(1).setName("testfile2.txt");
        entries.get(1).setSize(20);
        cpioArchiveOutputStream.putNextEntry(entries.get(1));
        cpioArchiveOutputStream.finish();
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
    }

    /**
     * Testing if the CpioArchiveOutputStream writes the correct header for each
     * entry in the cpio archive
     *
     * @throws IOException
     */
    @Test
    public void testWritesCorrectHeaders() throws IOException {
        byte[] output = outputStream.toByteArray();
        Assert.assertEquals(CpioConstants.MAGIC_NEW.length, output.length % 4);
        Assert.assertEquals(CpioConstants.MAGIC_NEW.getBytes().length, output.substring(0, CpioConstants.MAGIC_NEW.length).compareTo(CpioConstants.MAGIC_NEW.getBytes()));

        byte[] testfile1Header = new byte[CpioConstants.HEADER_SIZE];
        System.arraycopy(output, 0, testfile1Header, 0, testfile1Header.length);

        CpioArchiveEntry testfile1 = new CpioArchiveEntry();
        testfile1.setFormat(CpioConstants.FORMAT_NEW);
        testfile1.setName("testfile1.txt");
        testfile1.setSize(10);

        byte[] testfile1ExpectedHeader = CpioUtil.writeHeader(testfile1, false);

        Assert.assertArrayEquals(testfile1ExpectedHeader, testfile1Header);

        byte[] testfile2Header = new byte[CpioConstants.HEADER_SIZE + testfile1.getName().length() + 1];
        System.arraycopy(output, testfile1Header.length, testfile2Header, 0, testfile2Header.length);

        CpioArchiveEntry testfile2 = new CpioArchiveEntry();
        testfile2.setFormat(CpioConstants.FORMAT_NEW);
        testfile2.setName("testfile2.txt");
        testfile2.setSize(20);

        byte[] testfile2ExpectedHeader = CpioUtil.writeHeader(testfile2, false);

        Assert.assertArrayEquals(testfile2ExpectedHeader, testfile2Header);
    }

    /**
     * Testing writing data to CpioArchiveOutputStream
     *
     * @throws IOException
     */
    @Test
    public void testWritingData() throws IOException {
        byte[] testfile1Data = "Test Data 1".getBytes(StandardCharsets.UTF_8);
        byte[] testfile2Data = "Test Data 2".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(testfile1Data, 0, testfile1Data.length);
        cpioArchiveOutputStream.write(testfile2Data, 0, testfile2Data.length);
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        int testfile1Offset = 0;
        Assert.assertArrayEquals(testfile1Data, Arrays.copyOfRange(output, testfile1Offset, testfile1Offset + testfile1Data.length));
        testfile1Offset += testfile1Data.length;

        int testfile2Offset = testfile1Offset;
        Assert.assertArrayEquals(testfile2Data, Arrays.copyOfRange(output, testfile2Offset, testfile2Offset + testfile2Data.length));
        testfile2Offset += testfile2Data.length;

        Assert.assertTrue(testfile1Offset + testfile1Data.length + testfile2Data.length == output.length);
    }

    /**
     * Testing writing multiple entries with different sizes
     *
     * @throws IOException
     */
    @Test
    public void testWritingMultipleEntriesWithDifferentSizes() throws IOException {
        byte[] testfile1Data = "Test Data 1".getBytes(StandardCharsets.UTF_8);
        byte[] testfile2Data = "Test Data 2".getBytes(StandardCharsets.UTF_8);
        byte[] testfile3Data = "Test Data 3".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entries.get(0));
        cpioArchiveOutputStream.write(testfile1Data, 0, testfile1Data.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entries.get(1));
        cpioArchiveOutputStream.write(testfile2Data, 0, testfile2Data.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entries.get(1));
        cpioArchiveOutputStream.write(testfile3Data, 0, testfile3Data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        int testfile1Offset = 0;
        Assert.assertArrayEquals(testfile1Data, Arrays.copyOfRange(output, testfile1Offset, testfile1Offset + testfile1Data.length));
        testfile1Offset += testfile1Data.length;

        int testfile2Offset = testfile1Offset;
        Assert.assertArrayEquals(testfile2Data, Arrays.copyOfRange(output, testfile2Offset, testfile2Offset + testfile2Data.length));
        testfile2Offset += testfile2Data.length;

        int testfile3Offset = testfile2Offset;
        Assert.assertArrayEquals(testfile3Data, Arrays.copyOfRange(output, testfile3Offset, testfile3Offset + testfile3Data.length));
        testfile3Offset += testfile3Data.length;

        Assert.assertTrue(testfile1Offset + testfile1Data.length + testfile2Data.length + testfile3Data.length == output.length);
    }

    /**
     * Testing writing multiple entries with the same size
     *
     * @throws IOException
     */
    @Test
    public void testWritingMultipleEntriesWithTheSameSize() throws IOException {
        byte[] testfileData = "Test Data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entries.get(0));
        cpioArchiveOutputStream.write(testfileData, 0, testfileData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entries.get(1));
        cpioArchiveOutputStream.write(testfileData, 0, testfileData.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        int testfile1Offset = 0;
        Assert.assertArrayEquals(testfileData, Arrays.copyOfRange(output, testfile1Offset, testfile1Offset + testfileData.length));
        testfile1Offset += testfileData.length;

        int testfile2Offset = testfile1Offset;
        Assert.assertArrayEquals(testfileData, Arrays.copyOfRange(output, testfile2Offset, testfile2Offset + testfileData.length));
        testfile2Offset += testfileData.length;

        Assert.assertTrue(testfile1Offset + testfileData.length + testfileData.length == output.length);
    }

    /**
     * Testing writing empty entries
     *
     * @throws IOException
     */
    @Test
    public void testWritingEmptyEntries() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.finish();
        byte[] output = outputStream.toByteArray();

        Assert.assertEquals(0, output.length);
    }

    /**
     * Testing writing an entry with a negative size
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWritingNegativeEntrySize() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.write(-10, 0, 10);
        cpioArchiveOutputStream.finish();
    }

    /**
     * Testing writing an entry with a negative size and a non-existent entry
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWritingNegativeEntrySizeWithNonExistentEntry() throws IOException {
        cpioArchiveOutputStream.write(-10, 0, 10);
    }

    /**
     * Testing writing data to a closed stream
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWritingDataToClosedStream() throws IOException {
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.write(new byte[1], 0, 1);
    }

    /**
     * Testing writing data to a finished stream
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWritingDataToFinishedStream() throws IOException {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.write(new byte[1], 0, 1);
    }
}