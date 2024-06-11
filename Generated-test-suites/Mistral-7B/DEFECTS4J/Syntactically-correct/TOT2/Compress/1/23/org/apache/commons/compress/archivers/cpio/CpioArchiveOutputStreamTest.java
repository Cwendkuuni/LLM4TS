import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioUtilTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        outputStream = new FileOutputStream(temporaryFolder.newFile("test.cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (temporaryFolder.newFile("test.cpio").exists()) {
            temporaryFolder.delete();
        }
        entries.clear();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        String entryName = "testEntry";
        long fileSize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFileSize(fileSize);
        cpioArchiveOutputStream.putNextEntry(entry);

        // Basic functionality test
        assertEquals(entryName, cpioArchiveOutputStream.getName());
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
        assertNull(cpioArchiveOutputStream.getCpioEntry(entryName));

        // Edge cases
        String emptyEntryName = "";
        String nullEntryName = null;
        String longEntryName = "a" + CpioConstants.MAX_ARCHIVE_ENTRY_NAME_LENGTH + "b";

        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry()));
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry().setName(emptyEntryName)));
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry().setName(nullEntryName)));
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry().setName(longEntryName)));

        // Exceptions
        IOException ioException = new IOException("testException");
        cpioArchiveOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry()));
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(entry), ioException.getMessage());

        // Testing multiple entries
        String secondEntryName = "secondTestEntry";
        long secondFileSize = 20;
        CpioArchiveEntry secondEntry = new CpioArchiveEntry();
        secondEntry.setName(secondEntryName);
        secondEntry.setFileSize(secondFileSize);
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.putNextEntry(secondEntry);

        assertEquals(2, cpioArchiveOutputStream.size());
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry(entryName));
        assertEquals(secondEntry, cpioArchiveOutputStream.getCpioEntry(secondEntryName));
        assertNull(cpioArchiveOutputStream.getCpioEntry("nonExistentEntry"));
    }

    @Test
    public void testWrite() throws IOException {
        String entryName = "testEntry";
        long fileSize = 10;
        long writtenBytes = 5;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFileSize(fileSize);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = CpioUtilTest.generateTestData(writtenBytes);
        cpioArchiveOutputStream.write(testData, 0, writtenBytes);

        // Basic functionality test
        InputStream inputStream = new ByteArrayInputStream(testData);
        byte[] readData = new byte[writtenBytes];
        assertEquals(writtenBytes, inputStream.read(readData));
        assertEquals(new String(readData), new String(testData, 0, writtenBytes));

        // Edge cases
        byte[] emptyData = new byte[0];
        long negativeBytes = -1;
        long bytesOutOfBounds = fileSize + 1;

        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, (int) (writtenBytes + 1), 1));
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, -1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, 0, (int) (writtenBytes + 1)));
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(testData, 0, (int) (bytesOutOfBounds)));
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(emptyData, 0, 1));

        // Testing multiple entries
        String secondEntryName = "secondTestEntry";
        long secondFileSize = 20;
        long secondWrittenBytes = 8;
        CpioArchiveEntry secondEntry = new CpioArchiveEntry();
        secondEntry.setName(secondEntryName);
        secondEntry.setFileSize(secondFileSize);
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.putNextEntry(secondEntry);

        byte[] secondTestData = CpioUtilTest.generateTestData(secondWrittenBytes);
        cpioArchiveOutputStream.write(secondTestData, 0, secondWrittenBytes);

        inputStream = new ByteArrayInputStream(testData);
        byte[] firstReadData = new byte[writtenBytes];
        assertEquals(writtenBytes, inputStream.read(firstReadData));
        inputStream = new ByteArrayInputStream(secondTestData);
        byte[] secondReadData = new byte[secondWrittenBytes];
        assertEquals(secondWrittenBytes, inputStream.read(secondReadData));

        assertEquals(new String(firstReadData), new String(testData, 0, writtenBytes));
        assertEquals(new String(secondReadData), new String(secondTestData, 0, secondWrittenBytes));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "testEntry";
        long fileSize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFileSize(fileSize);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = CpioUtilTest.generateTestData(fileSize);
        cpioArchiveOutputStream.write(testData, 0, fileSize);
        cpioArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(testData);
        byte[] readData = new byte[fileSize];
        assertEquals(fileSize, inputStream.read(readData));
        assertEquals(new String(readData), new String(testData, 0, fileSize));

        assertNull(cpioArchiveOutputStream.getCpioEntry(entryName));
    }

    @Test
    public void testFinish() throws IOException {
        String entryName = "testEntry";
        long fileSize = 10;
        long writtenBytes = 5;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFileSize(fileSize);

        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testData = CpioUtilTest.generateTestData(writtenBytes);
        cpioArchiveOutputStream.write(testData, 0, writtenBytes);

        cpioArchiveOutputStream.finish();
        InputStream inputStream = new FileInputStream(temporaryFolder.newFile("test.cpio"));
        byte[] readData = new byte[fileSize + writtenBytes + 1 + 1];
        assertEquals(fileSize + writtenBytes + 1 + 1, inputStream.read(readData));
        inputStream.close();

        temporaryFolder.delete();
    }
}