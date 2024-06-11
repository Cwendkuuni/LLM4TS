import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBlockSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, -512);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512, -512);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        byte[] bytes = outputStream.toByteArray();
        int eofPosition = bytes.length - 2 * 512;
        assert (bytes[eofPosition] == 0 && bytes[eofPosition + 1] == 0);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        int eofPosition = bytes.length - 2 * 512;
        assert (bytes[eofPosition] == 0 && bytes[eofPosition + 1] == 0);
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        int recordSize = tarOutputStream.getRecordSize();
        Assert.assertEquals(512, recordSize);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutName() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();
        ArchiveEntry entry = new TarArchiveEntry(testFile, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] bytes = outputStream.toByteArray();
        String header = new String(bytes, 0, 512, StandardCharsets.US_ASCII);
        Assert.assertFalse(header.isEmpty());
        // Verify that the header contains the fileName
        int fileNameIndex = header.indexOf(testFile.getName());
        Assert.assertTrue(fileNameIndex > 0);
        tarOutputStream.closeArchiveEntry();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();
        ArchiveEntry entry = new TarArchiveEntry(testFile, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        String header = new String(bytes, 0, 512, StandardCharsets.US_ASCII);
        // Verify that the header contains the fileName
        int fileNameIndex = header.lastIndexOf(testFile.getName());
        Assert.assertTrue(fileNameIndex > 0);
        testFile.delete();
    }

    @Test
    public void testWrite() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();
        ArchiveEntry entry = new TarArchiveEntry(testFile, "test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        testFile.delete();

        byte[] bytes = outputStream.toByteArray();
        int eofPosition = bytes.length - 2 * 512;
        assert (bytes[eofPosition] == 0 && bytes[eofPosition + 1] == 0);

        // Verify that the data has been written to the archive
        int dataPosition = bytes.length - testData.length;
        assert (bytes[dataPosition] == testData[0]);
        for (int i = 1; i < testData.length; i++) {
            assert (bytes[dataPosition + i] == testData[i]);
        }
    }

    @Test
    public void testWriteWithPartialData() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();
        ArchiveEntry entry = new TarArchiveEntry(testFile, "test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        int dataToWrite = 4;
        tarOutputStream.write(testData, 0, dataToWrite);
        tarOutputStream.closeArchiveEntry();
        testFile.delete();

        byte[] bytes = outputStream.toByteArray();
        int eofPosition = bytes.length - 2 * 512;
        assert (bytes[eofPosition] == 0 && bytes[eofPosition + 1] == 0);

        // Verify that the data has been written to the archive
        int dataPosition = bytes.length - testData.length;
        for (int i = 0; i < dataToWrite; i++) {
            assert (bytes[dataPosition + i] == testData[i]);
        }
    }
}