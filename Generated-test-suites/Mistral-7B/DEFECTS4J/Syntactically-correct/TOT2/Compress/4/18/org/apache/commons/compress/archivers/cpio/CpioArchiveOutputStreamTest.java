import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private static File testFile;
    private static CpioArchiveOutputStream outputStream;
    private static Map<String, CpioArchiveEntry> names;

    @BeforeClass
    public static void setUp() throws Exception {
        testFile = new File("test.cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
        names = new HashMap<>();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.finish();
            outputStream.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry_BasicUsage() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(entry);
        String testString = "Hello World";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes, 0, testBytes.length);
        outputStream.closeArchiveEntry();

        CpioArchiveEntry retrievedEntry = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals("testFile.txt", retrievedEntry.getName());
        assertEquals(CpioConstants.FORMAT_NEW, retrievedEntry.getFormat());
        assertEquals(1, retrievedEntry.getNumberOfLinks());
        assertEquals(-1, retrievedEntry.getTime());
        assertEquals(testString.length(), retrievedEntry.getSize());
        assertTrue(retrievedEntry.isData());

        byte[] retrievedData = new byte[testBytes.length];
        IOUtils.readFully(outputStream, retrievedData);
        assertEquals(testString, new String(retrievedData, StandardCharsets.UTF_8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntry_InvalidFormat() throws Exception {
        CpioArchiveOutputStream invalidFormatStream = new CpioArchiveOutputStream(new FileOutputStream("testInvalidFormat.cpio"), 123);
        ArchiveEntry invalidEntry = new CpioArchiveEntry("testFile.txt");
        invalidFormatStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_DuplicateEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_InvalidEntryName() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("");
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_InvalidEntryDataSize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        entry.setSize(10);
        outputStream.putArchiveEntry(entry);
        String testString = "Hello World";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes, 0, testBytes.length + 1);
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_InvalidEntryFormat() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        entry.setFormat((short) 123);
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_WriteMultipleEntries() throws Exception {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("testFile1.txt");
        outputStream.putArchiveEntry(entry1);
        String testString1 = "Hello World 1";
        byte[] testBytes1 = testString1.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes1, 0, testBytes1.length);
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("testFile2.txt");
        outputStream.putArchiveEntry(entry2);
        String testString2 = "Hello World 2";
        byte[] testBytes2 = testString2.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes2, 0, testBytes2.length);
        outputStream.closeArchiveEntry();

        CpioArchiveEntry retrievedEntry1 = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals("testFile1.txt", retrievedEntry1.getName());
        assertEquals(1, retrievedEntry1.getNumberOfLinks());
        assertEquals(-1, retrievedEntry1.getTime());
        assertEquals(testString1.length(), retrievedEntry1.getSize());
        assertTrue(retrievedEntry1.isData());

        byte[] retrievedData1 = new byte[testBytes1.length];
        IOUtils.readFully(outputStream, retrievedData1);
        assertEquals(testString1, new String(retrievedData1, StandardCharsets.UTF_8));

        CpioArchiveEntry retrievedEntry2 = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals("testFile2.txt", retrievedEntry2.getName());
        assertEquals(1, retrievedEntry2.getNumberOfLinks());
        assertEquals(-1, retrievedEntry2.getTime());
        assertEquals(testString2.length(), retrievedEntry2.getSize());
        assertTrue(retrievedEntry2.isData());

        byte[] retrievedData2 = new byte[testBytes2.length];
        IOUtils.readFully(outputStream, retrievedData2);
        assertEquals(testString2, new String(retrievedData2, StandardCharsets.UTF_8));
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(entry);
        String testString = "Hello World";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes, 0, testBytes.length);
        outputStream.finish();

        CpioArchiveEntry retrievedEntry = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals("testFile.txt", retrievedEntry.getName());
        assertEquals(1, retrievedEntry.getNumberOfLinks());
        assertEquals(-1, retrievedEntry.getTime());
        assertEquals(testString.length(), retrievedEntry.getSize());
        assertTrue(retrievedEntry.isData());

        byte[] retrievedData = new byte[testBytes.length];
        IOUtils.readFully(outputStream, retrievedData);
        assertEquals(testString, new String(retrievedData, StandardCharsets.UTF_8));

        CpioArchiveEntry trailerEntry = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals(CpioConstants.CPIO_TRAILER, trailerEntry.getName());
        assertEquals(1, trailerEntry.getNumberOfLinks());
        assertEquals(-1, trailerEntry.getTime());
        assertEquals(0, trailerEntry.getSize());
        assertFalse(trailerEntry.isData());
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(entry);
        String testString = "Hello World";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testBytes, 0, testBytes.length);
        outputStream.close();

        CpioArchiveEntry retrievedEntry = (CpioArchiveEntry) outputStream.getNextEntry();
        assertEquals("testFile.txt", retrievedEntry.getName());
        assertEquals(1, retrievedEntry.getNumberOfLinks());
        assertEquals(-1, retrievedEntry.getTime());
        assertEquals(testString.length(), retrievedEntry.getSize());
        assertTrue(retrievedEntry.isData());

        byte[] retrievedData = new byte[testBytes.length];
        IOUtils.readFully(outputStream, retrievedData);
        assertEquals(testString, new String(retrievedData, StandardCharsets.UTF_8));

        assertFalse(outputStream.hasNextEntry());
        assertFalse(outputStream.isOpen());
    }
}