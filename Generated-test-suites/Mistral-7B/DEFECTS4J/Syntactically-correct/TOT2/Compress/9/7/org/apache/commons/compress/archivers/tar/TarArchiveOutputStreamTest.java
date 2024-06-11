import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(testFile), 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Creating an ArchiveEntry and put it in the TarArchiveOutputStream
        File testInput = new File("testInput.txt");
        FileUtils.writeStringToFile(testInput, "Hello World!", StandardCharsets.UTF_8);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testOutput.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Writing test data to the TarArchiveOutputStream
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);

        // Closing the archive entry
        tarOutputStream.closeArchiveEntry();

        // Checking if the data was written correctly to the TarArchive
        TarArchiveEntry outputEntry = tarOutputStream.getNextEntry();
        Assert.assertEquals("testOutput.txt", outputEntry.getName());
        byte[] outputData = new byte[testData.length];
        tarOutputStream.read(outputData, 0, testData.length);
        Assert.assertArrayEquals(testData, outputData);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_ThrowsIOException() throws IOException {
        // Setting longFileMode to LONGFILE_TRUNCATE and trying to put an entry with a long name
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        File longFileNameFile = new File("longFileName_" + Long.MAX_VALUE + ".txt");
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileNameFile.getAbsoluteFile());
        tarOutputStream.putArchiveEntry(longFileNameEntry);
    }

    @Test
    public void testFinish() throws IOException {
        // Testing if the finish method writes the EOF record correctly
        tarOutputStream.finish();

        // Checking if the EOF record was written correctly to the TarArchive
        TarArchiveEntry eofEntry = tarOutputStream.getNextEntry();
        byte[] eofRecord = new byte[1024 * 2]; // 2 * 512 bytes = 2 * recordSize
        tarOutputStream.read(eofRecord, 0, eofRecord.length);
        byte[] expectedEofRecord = new byte[512][];
        for (byte[] record : expectedEofRecord) {
            Arrays.fill(record, (byte) 0);
        }
        Assert.assertArrayEquals(expectedEofRecord, eofRecord);
    }

    @Test(expected = IOException.class)
    public void testFinish_ThrowsIOException() throws IOException {
        // Testing if an IOException is thrown when finish is called multiple times
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        // Testing if the close method correctly closes the underlying OutputStream and the TarArchiveOutputStream
        tarOutputStream.close();
        Assert.assertTrue(testFile.length() == 0);
    }

    @Test(expected = IOException.class)
    public void testClose_ThrowsIOException() throws IOException {
        // Testing if an IOException is thrown when close is called multiple times
        tarOutputStream.close();
        tarOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        // Testing if the write method correctly writes data to the TarArchiveOutputStream
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        byte[] outputData = new byte[testData.length];
        tarOutputStream.read(outputData, 0, testData.length);
        Assert.assertArrayEquals(testData, outputData);
    }

    @Test(expected = IOException.class)
    public void testWrite_ThrowsIOException() throws IOException {
        // Testing if an IOException is thrown when writing data past the size specified in the header
        byte[] testData = new byte[1000];
        tarOutputStream.write(testData, 0, testData.length + 1);
    }
}