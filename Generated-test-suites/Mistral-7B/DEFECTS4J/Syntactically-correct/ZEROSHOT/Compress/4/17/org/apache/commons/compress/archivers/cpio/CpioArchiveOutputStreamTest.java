import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private File tempFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("cpioTest", null);
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry("testData.txt", entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        ArchiveEntry testEntry = outputStream.getNextEntry();
        assertNotNull(testEntry);
        assertEquals(entryName, testEntry.getName());
        assertEquals(entry.getSize(), testEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry("testData.txt", entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        ArchiveEntry invalidEntry = new File("invalidFile.txt");
        outputStream.putArchiveEntry(invalidEntry);
    }

    @Test
    public void testFinish() throws IOException {
        String entryName1 = "testEntry1";
        String entryName2 = "testEntry2";
        byte[] testData1 = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] testData2 = "Java Test!".getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("testData1.txt", entryName1);
        entry1.setSize(testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("testData2.txt", entryName2);
        entry2.setSize(testData2.length);

        outputStream.putArchiveEntry(entry1);
        outputStream.write(testData1, 0, testData1.length);
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry2);
        outputStream.write(testData2, 0, testData2.length);
        outputStream.closeArchiveEntry();

        outputStream.finish();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CpioArchiveInputStream inputStream = new CpioArchiveInputStream(new FileInputStream(tempFile));

        ArchiveEntry testEntry;
        while ((testEntry = inputStream.getNextEntry()) != null) {
            byte[] entryData = new byte[testEntry.getSize()];
            inputStream.read(entryData);

            if (testEntry.getName().equals(entryName1)) {
                assertEquals(testData1, entryData);
            } else if (testEntry.getName().equals(entryName2)) {
                assertEquals(testData2, entryData);
            }

            inputStream.closeArchiveEntry();
        }

        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry = new CpioArchiveEntry("testData.txt", entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);

        outputStream.close();

        ArchiveEntry testEntry = outputStream.getNextEntry();
        assertNotNull(testEntry);
        assertEquals(entryName, testEntry.getName());
        assertEquals(entry.getSize(), testEntry.getSize());

        assertEquals(HashMap.class, outputStream.getNames().getClass());
    }

}