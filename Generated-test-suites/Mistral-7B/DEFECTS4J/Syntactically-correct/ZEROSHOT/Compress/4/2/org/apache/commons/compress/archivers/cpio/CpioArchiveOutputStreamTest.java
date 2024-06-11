import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("CpioArchiveOutputStreamTest.cpio");
        outputFile.deleteOnExit();
    }

    @After
    public void tearDown() throws Exception {
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithNewFormat() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioStream.putArchiveEntry(entry);
        File testFile = new File("testFile.txt");
        byte[] testContent = testFile.getTextFileContent();
        cpioStream.write(testContent, 0, testContent.length);
        cpioStream.closeArchiveEntry();
        cpioStream.finish();
        cpioStream.close();

        InputStream inputStream = new FileInputStream(outputFile);
        byte[] expectedBytes = new byte[testContent.length + ArchiveUtils.estimateCPIOHeaderSize(testFileName, testContent.length, CpioConstants.FORMAT_NEW)];
        InputStream input = new ByteArrayInputStream(expectedBytes);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            inputStream.read(buffer, 0, read);
        }
        assertEquals(expectedBytes.length, inputStream.available());
        inputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithNewFormatAndCRC() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);

        cpioStream.putArchiveEntry(entry);
        File testFile = new File("testFile.txt");
        byte[] testContent = testFile.getTextFileContent();
        cpioStream.write(testContent, 0, testContent.length);
        cpioStream.closeArchiveEntry();
        cpioStream.finish();
        cpioStream.close();

        InputStream inputStream = new FileInputStream(outputFile);
        byte[] expectedBytes = new byte[testContent.length + ArchiveUtils.estimateCPIOHeaderSize(testFileName, testContent.length, CpioConstants.FORMAT_NEW_CRC)];
        InputStream input = new ByteArrayInputStream(expectedBytes);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            inputStream.read(buffer, 0, read);
        }
        assertEquals(expectedBytes.length, inputStream.available());
        inputStream.close();

        long crc = 0;
        for (byte b : testContent) {
            crc += b;
        }
        assertEquals(crc, CpioArchiveOutputStream.readCrc(expectedBytes));
    }

    @Test
    public void testPutArchiveEntryWithOldFormat() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_OLD_ASCII);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        cpioStream.putArchiveEntry(entry);
        File testFile = new File("testFile.txt");
        byte[] testContent = testFile.getTextFileContent();
        cpioStream.write(testContent, 0, testContent.length);
        cpioStream.closeArchiveEntry();
        cpioStream.finish();
        cpioStream.close();

        InputStream inputStream = new FileInputStream(outputFile);
        byte[] expectedBytes = new byte[ArchiveUtils.estimateCPIOHeaderSize(testFileName, testContent.length, CpioConstants.FORMAT_OLD_ASCII) + testContent.length];
        InputStream input = new ByteArrayInputStream(expectedBytes);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            inputStream.read(buffer, 0, read);
        }
        assertEquals(expectedBytes.length, inputStream.available());
        inputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithOldFormatAndCRC() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        cpioStream.putArchiveEntry(entry);
        File testFile = new File("testFile.txt");
        byte[] testContent = testFile.getTextFileContent();
        cpioStream.write(testContent, 0, testContent.length);
        cpioStream.closeArchiveEntry();
        cpioStream.finish();
        cpioStream.close();

        InputStream inputStream = new FileInputStream(outputFile);
        byte[] expectedBytes = new byte[ArchiveUtils.estimateCPIOHeaderSize(testFileName, testContent.length, CpioConstants.FORMAT_OLD_BINARY) + testContent.length];
        InputStream input = new ByteArrayInputStream(expectedBytes);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            inputStream.read(buffer, 0, read);
        }
        assertEquals(expectedBytes.length, inputStream.available());
        inputStream.close();

        long crc = 0;
        for (byte b : testContent) {
            crc += b;
        }
        byte[] testCrcBytes = CpioArchiveOutputStream.intToSwappedBytes(getCrcAsInt(crc));
        byte[] expectedCrcBytes = new byte[2];
        System.arraycopy(testCrcBytes, 0, expectedCrcBytes, 0, testCrcBytes.length);
        byte[] cpioCrcBytes = new byte[2];
        inputStream.read(cpioCrcBytes, 0, cpioCrcBytes.length);
        assertArrayEquals(expectedCrcBytes, cpioCrcBytes);
        inputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithUnsupportedFormat() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile));

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat((short) 0x1234);

        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content for CpioArchiveOutputStreamTest";
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName);
        entry1.setSize(testFileContent.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        cpioStream.putArchiveEntry(entry1);
        cpioStream.write(testFileContent.getBytes());
        cpioStream.finishEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName);
        entry2.setSize(testFileContent.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        try {
            cpioStream.putArchiveEntry(entry2);
        } finally {
            cpioStream.finish();
            cpioStream.close();
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = cpioStream.createArchiveEntry(testFile, "testFile.txt");
        assertNotNull(entry);
        assertEquals(testFile.getName(), ((CpioArchiveEntry) entry).getName());

        cpioStream.close();
    }

    private static int getCrcAsInt(long crc) {
        int result = 0;
        while (crc != 0) {
            result = (result << 8) | ((int) (crc & 0xFF));
            crc >>= 8;
        }
        return result;
    }
}