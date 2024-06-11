import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Test suite for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("test.cpio");
        outputStream = new FileOutputStream(outputFile);
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a test entry
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setSize(5);
        testEntry.setFormat(CpioConstants.FORMAT_NEW);

        // Put the entry into the cpio stream
        cpioOutputStream.putArchiveEntry(testEntry);

        // Write test data
        byte[] testData = "test".getBytes();
        cpioOutputStream.write(testData, 0, testData.length);

        // Close the entry
        cpioOutputStream.closeArchiveEntry();

        // Check the written data
        byte[] expectedData = new byte[5];
        expectedData[0] = 'T';
        expectedData[1] = 'e';
        expectedData[2] = 's';
        expectedData[3] = 't';
        expectedData[4] = '\0';
        byte[] actualData = new byte[5];
        cpioOutputStream.read(actualData, 0, actualData.length);
        assertArrayEquals(expectedData, actualData);

        // Check the cpio header
        byte[] header = new byte[512];
        cpioOutputStream.read(header, 0, header.length);
        int magic = CpioUtil.readInt(header, 0, true);
        assertEquals(CpioConstants.MAGIC_NEW, magic);
        assertArrayEquals(testEntry.getName().getBytes(), CpioUtil.readString(header, 12, 255), 0);
        assertEquals(testEntry.getSize(), CpioUtil.readInt(header, 280, true));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFormatMismatch() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setSize(5);
        testEntry.setFormat(CpioConstants.FORMAT_OLD);
        cpioOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setSize(5);
        cpioOutputStream.putArchiveEntry(testEntry);
        cpioOutputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testFinish() throws IOException {
        // Write a trailer entry
        CpioArchiveEntry trailerEntry = new CpioArchiveEntry();
        trailerEntry.setName(CpioConstants.CPIO_TRAILER);
        cpioOutputStream.putArchiveEntry(trailerEntry);
        cpioOutputStream.finish();

        // Check the written data
        ByteArrayOutputStream trailerData = new ByteArrayOutputStream();
        cpioOutputStream.read(trailerData.toByteArray(), 0, 512);
        byte[] expectedTrailerData = CpioUtil.readString(CpioConstants.CPIO_TRAILER.getBytes(), 257, 2).getBytes();
        assertArrayEquals(expectedTrailerData, trailerData.toByteArray());
    }
}