import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new FileOutputStream(File.createTempFile("cpio", ".cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testFile1.txt");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        byte[] testFile1 = "Test File 1".getBytes();
        cpioArchiveOutputStream.write(testFile1, 0, testFile1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testFile2.txt");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry2);

        byte[] testFile2 = "Test File 2".getBytes();
        cpioArchiveOutputStream.write(testFile2, 0, testFile2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        byte[] cpioFile = readFileToByteArray(new File(outputStream.getName()));

        Map<String, CpioArchiveEntry> entriesMap = extractEntries(cpioFile);
        assertEquals(2, entriesMap.size());

        CpioArchiveEntry entry1Extracted = entriesMap.get("testFile1.txt");
        assertNotNull(entry1Extracted);
        assertEquals("testFile1.txt", entry1Extracted.getName());
        assertEquals(CpioConstants.FORMAT_NEW, entry1Extracted.getFormat());
        assertEquals(testFile1.length, entry1Extracted.getSize());

        CpioArchiveEntry entry2Extracted = entriesMap.get("testFile2.txt");
        assertNotNull(entry2Extracted);
        assertEquals("testFile2.txt", entry2Extracted.getName());
        assertEquals(CpioConstants.FORMAT_NEW, entry2Extracted.getFormat());
        assertEquals(testFile2.length, entry2Extracted.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testFile1.txt");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Trying to add a duplicate entry should throw an IOException
        cpioArchiveOutputStream.putArchiveEntry(entry1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat((short) 42);
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryWithNullName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Should not throw an exception
    }

    @Test
    public void testPutArchiveEntryWithEmptyName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setName("");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Should not throw an exception
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws IOException {
        CpioArchiveEntry entry = null;
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNonCpioArchiveEntry() throws IOException {
        Object nonCpioArchiveEntry = new Object();
        cpioArchiveOutputStream.putNextEntry(nonCpioArchiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);

        cpioArchiveOutputStream.closeArchiveEntry();

        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertEquals(testFile.length, cpioArchiveOutputStream.getWritten());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithOpenEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);

        cpioArchiveOutputStream.putNextEntry(entry); // Should throw an exception
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);

        byte[] writtenFile = new byte[testFile.length];
        cpioArchiveOutputStream.read(writtenFile, 0, testFile.length);

        assertArrayEquals(testFile, writtenFile);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithOpenEntryButNoData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(new byte[0], 0, 0);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testFile1.txt");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        byte[] testFile1 = "Test File 1".getBytes();
        cpioArchiveOutputStream.write(testFile1, 0, testFile1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testFile2.txt");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry2);

        byte[] testFile2 = "Test File 2".getBytes();
        cpioArchiveOutputStream.write(testFile2, 0, testFile2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] cpioFile = readFileToByteArray(new File(outputStream.getName()));

        Map<String, CpioArchiveEntry> entriesMap = extractEntries(cpioFile);
        assertEquals(2, entriesMap.size());

        CpioArchiveEntry entry1Extracted = entriesMap.get("testFile1.txt");
        assertNotNull(entry1Extracted);
        assertEquals("testFile1.txt", entry1Extracted.getName());
        assertEquals(CpioConstants.FORMAT_NEW, entry1Extracted.getFormat());
        assertEquals(testFile1.length, entry1Extracted.getSize());

        CpioArchiveEntry entry2Extracted = entriesMap.get("testFile2.txt");
        assertNotNull(entry2Extracted);
        assertEquals("testFile2.txt", entry2Extracted.getName());
        assertEquals(CpioConstants.FORMAT_NEW, entry2Extracted.getFormat());
        assertEquals(testFile2.length, entry2Extracted.getSize());

        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testFinishWithOpenEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);

        cpioArchiveOutputStream.finish(); // Should throw an exception
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testFile = "Test File".getBytes();
        cpioArchiveOutputStream.write(testFile, 0, testFile.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertNull(cpioArchiveOutputStream.getWritten());
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        byte[] bytes = new byte[((int) file.length())];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    private Map<String, CpioArchiveEntry> extractEntries(byte[] cpioFile)
            throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(
                "temp.cpio"));
        fileOutputStream.write(cpioFile);
        fileOutputStream.close();
        CpioArchiveOutputStream extractArchive = new CpioArchiveOutputStream(
                new FileInputStream("temp.cpio"));
        ArchiveEntry entry = null;
        while ((entry = extractArchive.getNextEntry()) != null) {
            byteArrayOutputStream.writeTo(extractArchive.getInputStream());
        }
        extractArchive.close();
        byteArrayOutputStream.close();
        byte[] extractedFile = byteArrayOutputStream.toByteArray();
        return ArchiveUtils.loadArchive(extractedFile);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}