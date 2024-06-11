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
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("target/test-output.cpio");
        cpioArchiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Given
        String entryName = "testEntry";
        String testContent = "Test content";

        // When
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        entry.setSize(testContent.length());
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertEquals(1, cpioArchiveOutputStream.getNumberOfEntries());
        assertNotNull(cpioArchiveOutputStream.getNextEntry());
        assertEquals(entryName, cpioArchiveOutputStream.getNextEntry().getName());

        // Write test content
        ByteArrayOutputStream contentOutputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8), 0, testContent.length());

        // Close entry
        cpioArchiveOutputStream.closeArchiveEntry();

        // Verify written data
        byte[] expectedContent = CpioUtil.stringToByteArray(testContent, StandardCharsets.UTF_8);
        byte[] actualContent = CpioUtil.readAllBytes(outputFile);
        assertEquals(expectedContent.length, actualContent.length);
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        // Given
        String entryName = "testEntry";
        String testContent = "Test content";

        // When
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertEquals(1, cpioArchiveOutputStream.getNumberOfEntries());

        // When trying to add the same entry again
        entry = new CpioArchiveEntry("testFile.txt", entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then an IOException should be thrown
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        // Given
        String entryName = "testEntry";
        String testContent = "Test content";

        // When
        CpioArchiveOutputStream cpioArchiveOutputStreamWithInvalidFormat = new CpioArchiveOutputStream(new FileOutputStream(outputFile), 123);
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        cpioArchiveOutputStreamWithInvalidFormat.putArchiveEntry(entry); // Should not compile due to IllegalArgumentException

        // Then an IllegalArgumentException should be thrown
    }

    @Test
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        // Given
        String entryName = "testEntry";

        // When
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertEquals(1, cpioArchiveOutputStream.getNumberOfEntries());

        // When trying to add a null entry
        CpioArchiveEntry nullEntry = null;
        cpioArchiveOutputStream.putArchiveEntry(nullEntry);

        // Then an NullPointerException should be thrown
    }

    @Test
    public void testFinish() throws IOException {
        // Given
        String entryName = "testEntry";
        String testContent = "Test content";

        // When
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        entry.setSize(testContent.length());
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8), 0, testContent.length());
        cpioArchiveOutputStream.finish();

        // Then
        assertEquals(1, cpioArchiveOutputStream.getNumberOfEntries());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        // Given
        String entryName = "testEntry";
        String testContent = "Test content";

        // When
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", entryName);
        entry.setSize(testContent.length());
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertEquals(1, cpioArchiveOutputStream.getNumberOfEntries());

        // When trying to finish without closing the current entry
        cpioArchiveOutputStream.finish();

        // Then an IOException should be thrown
    }
}