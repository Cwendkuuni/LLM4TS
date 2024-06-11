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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_LONG_FILE_NAME = "longTestFileNameWith SpecialChars.txt";
    private static final String TEST_DIRECTORY_NAME = "testDir";
    private static final int LONG_FILE_SIZE = 1024;
    private static final int SMALL_FILE_SIZE = 512;
    private static final int RECORD_SIZE = TarArchiveOutputStream.TAR_RECORD_SIZE;

    private File testFile;
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testBasicUsage() throws IOException {
        // Write a small file
        File fileToWrite = new File(TEST_FILE_NAME);
        InputStream inputStream = new FileInputStream(fileToWrite);
        TarArchiveEntry entry = new TarArchiveEntry(fileToWrite, TEST_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] buffer = new byte[SMALL_FILE_SIZE];
        int length = inputStream.read(buffer, 0, buffer.length);
        tarArchiveOutputStream.write(buffer, 0, length);
        tarArchiveOutputStream.closeArchiveEntry();
        inputStream.close();

        // Write a directory
        entry = new TarArchiveEntry(new File(TEST_DIRECTORY_NAME), TEST_DIRECTORY_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive and save it to a file
        tarArchiveOutputStream.finish();
        testFile = new File("test.tar");
        byte[] tarBytes = outputStream.toByteArray();
        IOUtils.write(tarBytes, testFile);

        // Test the created archive
        InputStream testInputStream = new FileInputStream(testFile);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(testInputStream);
        TarArchiveEntry testEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(TEST_FILE_NAME, testEntry.getName());
        byte[] testBuffer = new byte[SMALL_FILE_SIZE];
        int testLength = tarArchiveInputStream.read(testBuffer, 0, testBuffer.length);
        assertEquals(SMALL_FILE_SIZE, testLength);
        tarArchiveInputStream.close();
        testFile.delete();
    }

    @Test
    public void testLongFileName() throws IOException {
        // Write a long file name
        TarArchiveEntry entry = new TarArchiveEntry(new File(TEST_LONG_FILE_NAME), TEST_LONG_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] longFileNameBytes = TEST_LONG_FILE_NAME.getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(longFileNameBytes);
        tarArchiveOutputStream.write(0);
        closeArchiveEntry();

        // Finish the archive and save it to a file
        tarArchiveOutputStream.finish();
        byte[] tarBytes = outputStream.toByteArray();
        InputStream testInputStream = new ByteArrayInputStream(tarBytes);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(testInputStream);
        TarArchiveEntry testEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(TEST_LONG_FILE_NAME, testEntry.getName());
        tarArchiveInputStream.close();
    }

    @Test
    public void testLongFileSize() throws IOException {
        // Create a large file to write
        File largeFileToWrite = new File(TEST_FILE_NAME);
        byte[] largeFileContent = new byte[LONG_FILE_SIZE];

        // Write a large file
        InputStream largeFileInput
                = new FileInputStream(largeFileToWrite);
        TarArchiveEntry entry = new TarArchiveEntry(largeFileToWrite, TEST_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        int bytesRead;
        while ((bytesRead = largeFileInput.read(largeFileContent, 0, largeFileContent.length)) > -1) {
            tarArchiveOutputStream.write(largeFileContent, 0, bytesRead);
        }
        tarArchiveOutputStream.closeArchiveEntry();
        largeFileInput.close();

        // Finish the archive and save it to a file
        tarArchiveOutputStream.finish();
        testFile = new File("test.tar");
        byte[] tarBytes = outputStream.toByteArray();
        IOUtils.write(tarBytes, testFile);

        // Test the created archive
        InputStream testInputStream = new FileInputStream(testFile);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(testInputStream);
        TarArchiveEntry testEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(TEST_FILE_NAME, testEntry.getName());
        byte[] testBuffer = new byte[LONG_FILE_SIZE];
        int testLength = tarArchiveInputStream.read(testBuffer, 0, testBuffer.length);
        assertEquals(LONG_FILE_SIZE, testLength);
        tarArchiveInputStream.close();
        testFile.delete();
    }

    @Test
    public void testExceptionHandling() {
        // Test if an IOException is thrown when writing past the file size
        TarArchiveEntry entry = new TarArchiveEntry(new File(TEST_FILE_NAME), TEST_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] fileContent = new byte[SMALL_FILE_SIZE];
        tarArchiveOutputStream.write(fileContent);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(fileContent));
        tarArchiveOutputStream.closeArchiveEntry();

        // Test if a ClassCastException is thrown when putting a non-TarArchiveEntry
        Object nonTarArchiveEntry = new Object();
        assertThrows(ClassCastException.class, () -> tarArchiveOutputStream.putNextEntry(nonTarArchiveEntry));

        // Test if an IOException is thrown when the long file mode is not set
        TarArchiveOutputStream longFileNameUnsetTarArchiveOutputStream
                = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(TEST_LONG_FILE_NAME), TEST_LONG_FILE_NAME);
        longFileNameUnsetTarArchiveOutputStream.putNextEntry(longFileNameEntry);
        assertThrows(IOException.class, () -> longFileNameUnsetTarArchiveOutputStream.write(TEST_LONG_FILE_NAME.getBytes()));
        longFileNameUnsetTarArchiveOutputStream.close();

        // Test if an IOException is thrown when writing an empty record
        byte[] emptyRecord = new byte[0];
        tarArchiveOutputStream.write(emptyRecord);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(emptyRecord));
    }

    @Test
    public void testCloseArchiveOutOfOrder() throws IOException {
        // Write a file
        File fileToWrite = new File(TEST_FILE_NAME);
        InputStream inputStream = new FileInputStream(fileToWrite);
        TarArchiveEntry entry = new TarArchiveEntry(fileToWrite, TEST_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] buffer = new byte[SMALL_FILE_SIZE];
        int length = inputStream.read(buffer, 0, buffer.length);
        tarArchiveOutputStream.write(buffer, 0, length);
        tarArchiveOutputStream.closeArchiveEntry();
        inputStream.close();

        // Close the output stream prematurely
        tarArchiveOutputStream.close();

        // Try to write to the stream again
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putNextEntry(entry);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(buffer));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        // Write a file
        File fileToWrite = new File(TEST_FILE_NAME);
        InputStream inputStream = new FileInputStream(fileToWrite);
        TarArchiveEntry entry = new TarArchiveEntry(fileToWrite, TEST_FILE_NAME);
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] buffer = new byte[SMALL_FILE_SIZE];
        int length = inputStream.read(buffer, 0, buffer.length);
        tarArchiveOutputStream.write(buffer, 0, length);

        // Put another entry without closing the previous one
        TarArchiveEntry entry2 = new TarArchiveEntry(new File(TEST_DIRECTORY_NAME), TEST_DIRECTORY_NAME);
        tarArchiveOutputStream.putNextEntry(entry2);

        // Finish the archive
        tarArchiveOutputStream.finish();

        // Test the created archive
        InputStream testInputStream = new FileInputStream(testFile);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(testInputStream);

        TarArchiveEntry testEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(TEST_FILE_NAME, testEntry.getName());
        byte[] testBuffer = new byte[SMALL_FILE_SIZE];
        int testLength = tarArchiveInputStream.read(testBuffer, 0, testBuffer.length);
        assertEquals(SMALL_FILE_SIZE, testLength);

        testEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(TEST_DIRECTORY_NAME, testEntry.getName());
        assertNull(testEntry.getSize());
        assertNull(testEntry.isDirectory());
        tarArchiveInputStream.close();
        testFile.delete();
    }

    @Test
    public void testWrite_SmallBuffer() throws IOException {
        byte[] smallBuffer = new byte[RECORD_SIZE / 2];

        // Write a small buffer
        tarArchiveOutputStream.write(smallBuffer);
        tarArchiveOutputStream.flush();

        // Read the written bytes
        byte[] readBuffer = new byte[RECORD_SIZE / 2];
        int bytesRead = tarArchiveOutputStream.read(readBuffer, 0, readBuffer.length);

        // Check if all bytes were read
        assertEquals(bytesRead, smallBuffer.length);
        assertArrayEquals(smallBuffer, readBuffer);
    }

    @Test
    public void testWrite_LargeBuffer() throws IOException {
        byte[] largeBuffer = new byte[RECORD_SIZE * 3];

        // Write a large buffer
        tarArchiveOutputStream.write(largeBuffer, 0, largeBuffer.length);
        tarArchiveOutputStream.flush();

        // Read the written bytes
        byte[] readBuffer = new byte[RECORD_SIZE * 3];
        int bytesRead = tarArchiveOutputStream.read(readBuffer, 0, readBuffer.length);

        // Check if all bytes were read
        assertEquals(bytesRead, largeBuffer.length);
        assertArrayEquals(largeBuffer, readBuffer);
    }

    @Test
    public void testWrite_MultipleSmallBuffers() throws IOException {
        List<byte[]> smallBuffers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            smallBuffers.add(new byte[RECORD_SIZE / 2]);
        }

        // Write multiple small buffers
        for (byte[] buffer : smallBuffers) {
            tarArchiveOutputStream.write(buffer);
            tarArchiveOutputStream.flush();
        }

        // Read all the written bytes
        byte[] readBuffer = new byte[RECORD_SIZE * 10];
        int bytesRead = tarArchiveOutputStream.read(readBuffer, 0, readBuffer.length);

        // Check if all bytes were read
        assertEquals(bytesRead, readBuffer.length);
        for (int i = 0; i < smallBuffers.size(); i++) {
            byte[] smallBuffer = smallBuffers.get(i);
            assertEquals(smallBuffer[0], readBuffer[i * RECORD_SIZE / 2]);
            for (int j = 1; j < smallBuffer.length; j++) {
                assertEquals(smallBuffer[j], readBuffer[(i * RECORD_SIZE / 2) + j]);
            }
        }
    }
}