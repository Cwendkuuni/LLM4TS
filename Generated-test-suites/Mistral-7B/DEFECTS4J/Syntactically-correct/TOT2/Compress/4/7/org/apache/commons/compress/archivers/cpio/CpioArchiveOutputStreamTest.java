import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream out;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.cpio");
        out = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Hello World!";

        File testFileToWrite = new File("testFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(testFileToWrite, testFileName);
        cpioOutputStream.putArchiveEntry(entry);

        byte[] fileContent = testFileToWrite.getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(fileContent, 0, fileContent.length);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedCpioHeader = new byte[32];
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), 0, expectedCpioHeader, 0, 8);
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.FORMAT_NEW), 0, expectedCpioHeader, 8, 2);
        System.arraycopy(ArchiveUtils.toAsciiBytes(testFileName.getBytes(StandardCharsets.UTF_8)), 0, expectedCpioHeader, 10, testFileName.length() + 1);
        System.arraycopy(ArchiveUtils.toAsciiBytes(Long.toString(testFile.length()).getBytes(StandardCharsets.UTF_8)), 0, expectedCpioHeader, 11, 11);

        byte[] actualCpioHeader = out.toByteArray();
        assertEquals("Unexpected CPIO header", expectedCpioHeader, actualCpioHeader, 0);

        byte[] actualContent = new byte[testFileContent.length()];
        cpioOutputStream.read(actualContent, 0, testFileContent.length());
        assertEquals("Unexpected file content", testFileContent, new String(actualContent, StandardCharsets.UTF_8));

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntries();
        assertNotNull("Unexpected null entries", entries);
        assertEquals("Unexpected size of entries", 1, entries.size());
        assertEquals("Unexpected entry name", testFileName, entries.keySet().iterator().next());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), (short) 5);
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Hello World!";

        File testFileToWrite = new File("testFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(testFileToWrite, testFileName);
        cpioOutputStream.putArchiveEntry(entry);

        byte[] fileContent = testFileToWrite.getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(fileContent, 0, fileContent.length);
        cpioOutputStream.closeArchiveEntry();

        entry = new CpioArchiveEntry(testFileToWrite, testFileName);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        String testFileName = "testFile.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.close();

        byte[] testFileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseEntry() throws IOException {
        String testFileName = "testFile.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.write(1, 0, 1);
    }

}