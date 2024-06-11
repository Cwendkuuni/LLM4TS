import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testNormalUseCase() throws IOException {
        String testFileName = "testFile.txt";
        File testFile = new File("testFile.txt");
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, testFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(testFileContent, 0, testFileContent.length);
        tarOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();

        // Verify the output is correct
        TarArchiveEntry expectedEntry = new TarArchiveEntry(testFile, testFileName);
        byte[] expectedHeader = expectedEntry.getHeader(tarOutputStream.getBlockSize(), tarOutputStream.getRecordSize());
        byte[] expectedContent = testFileContent;

        byte[] expectedOutput = new byte[output.length];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(expectedContent, 0, expectedOutput, expectedHeader.length, expectedContent.length);

        assertArrayEquals(expectedOutput, output);

        // Verify the entry is correctly read back
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry readEntry = (TarArchiveEntry) newTarOutputStream.getNextEntry();
        assertEquals(testFileName, readEntry.getName());
        assertEquals(expectedContent.length, readEntry.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBlockSize() throws IOException {
        new TarArchiveOutputStream(new ByteArrayOutputStream(), -1024);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRecordSize() throws IOException {
        new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, -1024);
    }

    @Test(expected = IOException.class)
    public void testWritingEmptyEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(new File("emptyFile.txt"), longFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.closeArchiveEntry();

        String errorMessage = "file name '" + longFileName + "' is too long ( > "
                + TarConstants.NAMELEN + " bytes)";
        tarOutputStream.close();
        assertTrue(tarOutputStream.getMessage().contains(errorMessage));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testClosingStreamAfterPuttingEntry() throws IOException {
        String testFileName = "testFile.txt";
        File testFile = new File("testFile.txt");
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, testFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(testFileContent, 0, testFileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        // Verify the archive is correctly written
        byte[] output = outputStream.toByteArray();
        TarArchiveEntry expectedEntry = new TarArchiveEntry(testFile, testFileName);
        byte[] expectedHeader = expectedEntry.getHeader(tarOutputStream.getBlockSize(), tarOutputStream.getRecordSize());
        byte[] expectedContent = testFileContent;

        byte[] expectedOutput = new byte[output.length];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(expectedContent, 0, expectedOutput, expectedHeader.length, expectedContent.length);

        assertArrayEquals(expectedOutput, output);

        // Verify the stream is closed
        assertFalse(tarOutputStream.isOpen());
    }

    @Test
    public void testWritingMultipleEntries() throws IOException {
        String entry1Name = "entry1.txt";
        String entry2Name = "entry2.txt";
        File entry1File = new File("entry1.txt");
        File entry2File = new File("entry2.txt");
        byte[] entry1Content = "Test content 1".getBytes(StandardCharsets.UTF_8);
        byte[] entry2Content = "Test content 2".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry1 = tarOutputStream.createArchiveEntry(entry1File, entry1Name);
        TarArchiveEntry entry2 = tarOutputStream.createArchiveEntry(entry2File, entry2Name);

        tarOutputStream.putNextEntry(entry1);
        tarOutputStream.write(entry1Content, 0, entry1Content.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putNextEntry(entry2);
        tarOutputStream.write(entry2Content, 0, entry2Content.length);
        tarOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();

        // Verify the output is correct
        TarArchiveEntry expectedEntry1 = new TarArchiveEntry(entry1File, entry1Name);
        byte[] expectedHeader1 = expectedEntry1.getHeader(tarOutputStream.getBlockSize(), tarOutputStream.getRecordSize());
        byte[] expectedContent1 = entry1Content;

        TarArchiveEntry expectedEntry2 = new TarArchiveEntry(entry2File, entry2Name);
        byte[] expectedHeader2 = expectedEntry2.getHeader(tarOutputStream.getBlockSize(), tarOutputStream.getRecordSize());
        byte[] expectedContent2 = entry2Content;

        byte[] expectedOutput = new byte[output.length];

        int header1Length = expectedHeader1.length;
        int header2Length = expectedHeader2.length;
        int content1Length = expectedContent1.length;
        int content2Length = expectedContent2.length;

        System.arraycopy(expectedHeader1, 0, expectedOutput, 0, header1Length);
        System.arraycopy(expectedContent1, 0, expectedOutput, header1Length, content1Length);
        System.arraycopy(expectedHeader2, 0, expectedOutput, header1Length + content1Length, header2Length);
        System.arraycopy(expectedContent2, 0, expectedOutput, header1Length + content1Length + header2Length, content2Length);

        assertArrayEquals(expectedOutput, output);

        // Verify the entries are correctly read back
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(inputStream);

        TarArchiveEntry readEntry1 = (TarArchiveEntry) newTarOutputStream.getNextEntry();
        assertEquals(entry1Name, readEntry1.getName());
        assertEquals(expectedContent1.length, readEntry1.getSize());

        byte[] readContent1 = new byte[expectedContent1.length];
        int readLength1 = newTarOutputStream.read(readContent1, 0, readContent1.length);
        assertArrayEquals(expectedContent1, readContent1);

        TarArchiveEntry readEntry2 = (TarArchiveEntry) newTarOutputStream.getNextEntry();
        assertEquals(entry2Name, readEntry2.getName());
        assertEquals(expectedContent2.length, readEntry2.getSize());

        byte[] readContent2 = new byte[expectedContent2.length];
        int readLength2 = newTarOutputStream.read(readContent2, 0, readContent2.length);
        assertArrayEquals(expectedContent2, readContent2);
    }

    @Test
    public void testWritingLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN]) + "b";
        File testFile = new File("testFile.txt");

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, longFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        String errorMessage = "file name '" + longFileName + "' is too long ( > "
                + TarConstants.NAMELEN + " bytes)";
        tarOutputStream.close();
        assertTrue(tarOutputStream.getMessage().contains(errorMessage));
    }

    @Test
    public void testWritingEntryWithZeroSize() throws IOException {
        String testFileName = "testFile.txt";
        File testFile = new File("testFile.txt");

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, testFileName);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();

        TarArchiveEntry expectedEntry = new TarArchiveEntry(testFile, testFileName);
        byte[] expectedHeader = expectedEntry.getHeader(tarOutputStream.getBlockSize(), tarOutputStream.getRecordSize());

        byte[] expectedOutput = new byte[output.length];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);

        assertArrayEquals(expectedOutput, output);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry readEntry = (TarArchiveEntry) newTarOutputStream.getNextEntry();
        assertEquals(testFileName, readEntry.getName());
        assertNull(readEntry.getSize());
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }
}