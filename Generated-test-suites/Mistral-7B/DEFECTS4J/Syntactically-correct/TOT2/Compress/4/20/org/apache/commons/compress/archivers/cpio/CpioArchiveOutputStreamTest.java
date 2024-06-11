import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.cpio");
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(
                new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test for putNextEntry method with valid inputs
     * @throws IOException
     */
    @Test
    public void testPutNextEntryValidInputs() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = cpioArchiveOutputStream.getEntries();
        assertEquals(1, entries.size());
        CpioArchiveEntry readEntry = entries.get("testfile");
        assertEquals("testfile", readEntry.getName());
        assertEquals(testData.length, readEntry.getSize());
    }

    /**
     * Test for putNextEntry method with invalid input - null entry name
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNullEntryName() throws IOException {
        cpioArchiveOutputStream.putNextEntry(null);
    }

    /**
     * Test for putNextEntry method with invalid input - null entry data
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNullEntryData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entry);
    }

    /**
     * Test for putNextEntry method with duplicate entry name
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntryName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entry);
    }

    /**
     * Test for write method with valid inputs
     * @throws IOException
     */
    @Test
    public void testWriteValidInputs() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] outputBytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        CpioArchiveOutputStream testCpioArchiveOutputStream = new CpioArchiveOutputStream(inputStream);

        CpioArchiveEntry readEntry = testCpioArchiveOutputStream.getNextEntry();
        assertEquals("testfile", readEntry.getName());
        assertEquals(testData.length, readEntry.getSize());
        byte[] readData = new byte[testData.length];
        testCpioArchiveOutputStream.read(readData, 0, testData.length);
        assertArrayEquals(testData, readData);
    }

    /**
     * Test for write method with invalid input - null data
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNullData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(null, 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test for finish method with valid inputs
     * @throws IOException
     */
    @Test
    public void testFinishValidInputs() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream testCpioArchiveOutputStream = new CpioArchiveOutputStream(inputStream);

        assertFalse(testCpioArchiveOutputStream.hasNextEntry());
        cpioArchiveOutputStream.close();
        testFile.delete();
    }

    /**
     * Test for finish method with invalid input - already finished
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.finish();

        cpioArchiveOutputStream.finish();
    }

    /**
     * Test for getEntries method with valid inputs
     * @throws IOException
     */
    @Test
    public void testGetEntriesValidInputs() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        byte[] testData1 = "test data 1".getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        byte[] testData2 = "test data 2".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry1);
        cpioArchiveOutputStream.write(testData1, 0, testData1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(entry2);
        cpioArchiveOutputStream.write(testData2, 0, testData2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        Map<String, CpioArchiveEntry> entries = cpioArchiveOutputStream.getEntries();
        assertEquals(2, entries.size());
        CpioArchiveEntry readEntry1 = entries.get("testfile1");
        assertEquals("testfile1", readEntry1.getName());
        assertEquals(testData1.length, readEntry1.getSize());

        CpioArchiveEntry readEntry2 = entries.get("testfile2");
        assertEquals("testfile2", readEntry2.getName());
        assertEquals(testData2.length, readEntry2.getSize());
    }

    /**
     * Test for getNextEntry method with empty archive
     * @throws IOException
     */
    @Test
    public void testGetNextEntryEmptyArchive() throws IOException {
        cpioArchiveOutputStream.finish();

        CpioArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        assertNull(readEntry);
    }

    /**
     * Test for getNextEntry method with valid inputs
     * @throws IOException
     */
    @Test
    public void testGetNextEntryValidInputs() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        CpioArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        assertEquals("testfile", readEntry.getName());
        assertEquals(testData.length, readEntry.getSize());
        byte[] readData = new byte[testData.length];
        cpioArchiveOutputStream.read(readData, 0, testData.length);
        assertArrayEquals(testData, readData);
    }

    /**
     * Test for getNextEntry method with no more entries
     * @throws IOException
     */
    @Test
    public void testGetNextEntryNoMoreEntries() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        byte[] testData1 = "test data 1".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.putNextEntry(entry1);
        cpioArchiveOutputStream.write(testData1, 0, testData1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        CpioArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        assertEquals("testfile1", readEntry.getName());
        assertEquals(testData1.length, readEntry.getSize());

        cpioArchiveOutputStream.getNextEntry();
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }
}