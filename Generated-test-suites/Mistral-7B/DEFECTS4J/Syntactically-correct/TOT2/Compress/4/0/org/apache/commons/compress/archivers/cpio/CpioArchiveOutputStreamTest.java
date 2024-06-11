import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("test.cpio");
        cpioArchiveOutputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
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
    public void testPutArchiveEntryAndWriteFile() throws IOException {
        // Test putting an archive entry and writing a file
        String testFileName = "testFile.txt";
        String testFileContent = "Hello World!";
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(testFileName);
        archiveEntry.setSize(testFileContent.length());
        archiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFile = new File(outputFile, testFileName);
        String readFileContent = new String(ArchiveUtils.toByteArray(testFile), StandardCharsets.UTF_8);

        assertEquals(testFileContent, readFileContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        // Test putting an archive entry with a duplicate name
        String testFileName = "testFile.txt";
        CpioArchiveEntry archiveEntry1 = new CpioArchiveEntry();
        archiveEntry1.setName(testFileName);
        archiveEntry1.setSize(10);
        archiveEntry1.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry1);

        CpioArchiveEntry archiveEntry2 = new CpioArchiveEntry();
        archiveEntry2.setName(testFileName);
        archiveEntry2.setSize(20);
        archiveEntry2.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        // Test putting an archive entry with an invalid format
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testFile.txt");
        archiveEntry.setSize(10);
        archiveEntry.setFormat(123);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteToClosedStream() throws IOException {
        // Test writing to a closed stream
        cpioArchiveOutputStream.close();

        byte[] testBytes = {1, 2, 3};
        cpioArchiveOutputStream.write(testBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidFormat() throws IOException {
        // Test constructing with an invalid format
        new CpioArchiveOutputStream(new FileOutputStream(outputFile), 123);
    }
}