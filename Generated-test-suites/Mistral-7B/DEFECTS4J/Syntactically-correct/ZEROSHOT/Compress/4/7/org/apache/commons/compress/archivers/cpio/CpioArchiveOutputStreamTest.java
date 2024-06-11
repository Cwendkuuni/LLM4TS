import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testWriteSingleEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String filePath = "testFile.txt";
        String entryName = "testEntry.txt";
        File inputFile = new File(filePath);

        ArchiveEntry entry = archiver.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);

        byte[] fileContent = "Hello World!".getBytes();
        archiver.putArchiveEntry(entry);
        archiver.write(fileContent, 0, fileContent.length);
        archiver.closeArchiveEntry();

        archiver.finish();
        archiver.close();

        byte[] expectedOutput = generateExpectedOutput("testSingleEntry.cpio");
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String filePath1 = "testFile1.txt";
        String entryName1 = "testEntry1.txt";
        File inputFile1 = new File(filePath1);

        String filePath2 = "testFile2.txt";
        String entryName2 = "testEntry2.txt";
        File inputFile2 = new File(filePath2);

        ArchiveEntry entry1 = archiver.createArchiveEntry(inputFile1, entryName1);
        assertNotNull(entry1);

        byte[] fileContent1 = "Hello World!".getBytes();
        archiver.putArchiveEntry(entry1);
        archiver.write(fileContent1, 0, fileContent1.length);
        archiver.closeArchiveEntry();

        ArchiveEntry entry2 = archiver.createArchiveEntry(inputFile2, entryName2);
        assertNotNull(entry2);

        byte[] fileContent2 = "Test CpioArchiveOutputStream!".getBytes();
        archiver.putArchiveEntry(entry2);
        archiver.write(fileContent2, 0, fileContent2.length);
        archiver.closeArchiveEntry();

        archiver.finish();
        archiver.close();

        byte[] expectedOutput = generateExpectedOutput("testMultipleEntries.cpio");
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    private byte[] generateExpectedOutput(String fileName) {
        // Add your code here to generate the expected output for the given file name
        // You may need to read the expected output file from a resource or a test data folder
        // and return it as a byte array
        return new byte[0];
    }
}