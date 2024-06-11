import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        }
    }

    @Test
    public void testEmptyArchive() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(0, actual[i]);
            assertEquals(0, expected[i]);
        }
    }

    @Test
    public void testPutArchiveEntryWithSmallFile() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        byte[] fileContent = "This is a test file.".getBytes();

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] expectedHeader = tarArchiveEntry.getHeaderBytes();
        byte[] expectedContent = fileContent;
        byte[] actual = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actual[index++]);
        }
        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(expectedContent[i], actual[index++]);
        }
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "longFileNameThatIsLongerThanNameLenButLessThanMaxFileNameLen.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        byte[] fileContent = "This is a test file.".getBytes();

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileName.getBytes(), 0, fileName.length());
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] expectedHeader = tarArchiveEntry.getHeaderBytes();
        byte[] expectedFileName = fileName.getBytes();
        byte[] actual = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actual[index++]);
        }
        for (int i = 0; i < expectedFileName.length; i++) {
            assertEquals(expectedFileName[i], actual[index++]);
        }
        assertFalse(outputStream.toByteArray().hasRemaining());
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameTruncated() throws IOException {
        String fileName = "longFileNameThatIsLongerThanNameLenButLessThanMaxFileNameLen.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        byte[] fileContent = "This is a test file.".getBytes();

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileName.getBytes(), 0, TarConstants.NAMELEN);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] expectedHeader = tarArchiveEntry.getHeaderBytes();
        byte[] expectedFileName = fileName.getBytes();
        byte[] actual = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actual[index++]);
        }
        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            assertEquals(expectedFileName[i], actual[index++]);
        }
        assertNull(new String(outputStream.toByteArray(), "UTF-8").split("\\s+")[1]);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        String dirName = "testDir/";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(dirName);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedHeader = tarArchiveEntry.getHeaderBytes();
        byte[] actual = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actual[index++]);
        }
        assertTrue(new String(outputStream.toByteArray(), "UTF-8").contains("DR-"));
    }

    @Test
    public void testWriteWithLessBytesThanRecordSize() throws IOException {
        byte[] input = "This is a test".getBytes();

        tarArchiveOutputStream.write(input, 0, input.length);

        tarArchiveOutputStream.finish();
        byte[] expected = new byte[TarConstants.RECORD_SIZE - (input.length % TarConstants.RECORD_SIZE)];
        byte[] actual = new byte[outputStream.size()];
        outputStream.read(actual);

        System.arraycopy(input, 0, expected, 0, input.length);
        System.arraycopy(input, input.length, expected, input.length, expected.length - input.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testWriteWithMoreBytesThanRecordSize() throws IOException {
        byte[] input = "This is a test".getBytes();

        tarArchiveOutputStream.write(input, 0, input.length * 2);

        tarArchiveOutputStream.finish();
        byte[] expected = new byte[TarConstants.RECORD_SIZE];
        byte[] actual = new byte[outputStream.size()];
        outputStream.read(actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i % expected.length], actual[i]);
        }

        byte[] remaining = new byte[outputStream.size() - TarConstants.RECORD_SIZE];
        outputStream.read(remaining);
        assertEquals(input.length * 2, remaining.length);
    }

    @Test
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        byte[] input = "This is a test".getBytes();

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(input, 0, input.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.write(input, 0, input.length);
        });

        tarArchiveOutputStream.finish();
        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testWriteAfterFinish() throws IOException {
        byte[] input = "This is a test".getBytes();

        tarArchiveOutputStream.write(input, 0, input.length);
        tarArchiveOutputStream.finish();

        assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.write(input, 0, input.length);
        });

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = "This is a test".getBytes();

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(input, 0, input.length);

        tarArchiveOutputStream.close();

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

        assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.write(input, 0, input.length);
        });
    }

    @Test
    public void testFinish() throws IOException {
        byte[] input = "This is a test".getBytes();

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(input, 0, input.length);

        tarArchiveOutputStream.finish();

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 3];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntryWithFile() throws IOException {
        String fileName = "testFile.txt";
        byte[] fileContent = "This is a test file.".getBytes();
        File testFile = new File(fileName);
        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(testFile, fileName);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        byte[] expected = new byte[TarConstants.RECORD_SIZE * 2];
        byte[] actual = outputStream.toByteArray();

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

        IOUtils.closeQuietly(testFile.getInputStream());
        IOUtils.deleteQuietly(testFile);
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            tarArchiveOutputStream.createArchiveEntry(null, "testFile.txt");
        });
    }
}