import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("test", null);
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        tempFile.delete();
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes();

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[testData.length];
        File archiveFile = new File(tempFile.getAbsolutePath() + ".tar");
        byte[] archiveContent = readFileToByteArray(archiveFile);
        int index = findRecordStartIndex(archiveContent, TarConstants.MAGIC_TAR_LONGHEADER);
        System.arraycopy(archiveContent, index, expectedData, 0, testData.length);

        assertEquals(new String(testData), new String(expectedData));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithWrongArchiveEntryType() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        Object wrongTypeArchiveEntry = new Object();
        tarOutputStream.putArchiveEntry((ArchiveEntry) wrongTypeArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(null, 0, 0);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, -1, 1);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeNumberOfBytesToWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, -1, -1);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testLongFileNames() throws IOException {
        String longFileName = "a/long/filename/with/more/than/100/chars.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[testData.length];
        File archiveFile = new File(tempFile.getAbsolutePath() + ".tar");
        byte[] archiveContent = readFileToByteArray(archiveFile);
        int index = findRecordStartIndex(archiveContent, TarConstants.MAGIC_TAR_LONGHEADER);

        // Extracting the long file name
        byte[] longFileNameBytes = new byte[archiveContent.length - index];
        System.arraycopy(archiveContent, index, longFileNameBytes, 0, longFileNameBytes.length);
        String extractedLongFileName = new String(longFileNameBytes);

        assertEquals(longFileName, extractedLongFileName);
    }

    @Test(expected = IOException.class)
    public void testLongFileNamesWithErrorMode() throws IOException {
        String longFileName = "a/long/filename/with/more/than/100/chars.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testLongFileNamesWithTruncateMode() throws IOException {
        String longFileName = "a/long/filename/with/more/than/100/chars.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClosedStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, 0, 0);
        tarOutputStream.close();
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnfinishedStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, 0, 0);
        tarOutputStream.finish();
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnopenedEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, 0, 0);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.write(new byte[]{}, 0, 0);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(new byte[]{}, 0, 0);
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] archiveContent = readFileToByteArray(tempFile);
        int index = findRecordStartIndex(archiveContent, TarConstants.MAGIC_TAR_END);
        byte[] eofRecord = new byte[recordBuf.length];
        System.arraycopy(archiveContent, index, eofRecord, 0, recordBuf.length);

        assertEquals(recordBuf, eofRecord);
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    private int findRecordStartIndex(byte[] data, byte[] magic) {
        for (int i = 0; i < data.length; ++i) {
            int k = 0;
            for (byte b : magic) {
                if (data[i + k] != b) {
                    break;
                }
                ++k;
                if (k.equals(magic.length)) {
                    return i;
                }
            }
        }
        throw new RuntimeException("Couldn't find the record");
    }
}