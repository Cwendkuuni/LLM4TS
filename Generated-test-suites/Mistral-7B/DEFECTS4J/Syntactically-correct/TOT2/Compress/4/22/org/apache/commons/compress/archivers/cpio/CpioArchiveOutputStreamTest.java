import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Assert;
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

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String entryName = "testfile.txt";
        String testContent = "Test content";
        long expectedSize = testContent.getBytes(StandardCharsets.UTF_8).length;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(expectedSize);
        outputStream.putArchiveEntry(entry);

        byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);
        outputStream.write(testContentBytes, 0, testContentBytes.length);
        outputStream.closeArchiveEntry();

        byte[] expectedHeader = ArchiveUtils.readHeader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), 0);
        byte[] expectedData = new byte[expectedSize];
        System.arraycopy(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size() - expectedSize, expectedData, 0, expectedSize);

        CpioArchiveEntry retrievedEntry = (CpioArchiveEntry) outputStream.getNextEntry();
        Assert.assertEquals(entryName, retrievedEntry.getName());
        Assert.assertEquals(expectedSize, retrievedEntry.getSize());
        Assert.assertArrayEquals(expectedHeader, retrievedEntry.getHeader());
        Assert.assertArrayEquals(expectedData, outputStream.readAllBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, 1);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws Exception {
        String entryName = "testfile.txt";
        outputStream.putArchiveEntry(new CpioArchiveEntry(entryName));
        outputStream.putArchiveEntry(new CpioArchiveEntry(entryName));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntrySize() throws Exception {
        String entryName = "testfile.txt";
        long expectedSize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(expectedSize);
        outputStream.putArchiveEntry(entry);

        byte[] testContentBytes = new byte[expectedSize + 1];
        outputStream.write(testContentBytes, 0, testContentBytes.length);

        outputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        String entryName1 = "testfile1.txt";
        String entryContent1 = "Test content 1";
        long expectedSize1 = entryContent1.getBytes(StandardCharsets.UTF_8).length;

        String entryName2 = "testfile2.txt";
        String entryContent2 = "Test content 2";
        long expectedSize2 = entryContent2.getBytes(StandardCharsets.UTF_8).length;

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName1);
        entry1.setSize(expectedSize1);
        outputStream.putArchiveEntry(entry1);
        outputStream.write(entryContent1.getBytes(StandardCharsets.UTF_8), 0, expectedSize1);
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName2);
        entry2.setSize(expectedSize2);
        outputStream.putArchiveEntry(entry2);
        outputStream.write(entryContent2.getBytes(StandardCharsets.UTF_8), 0, expectedSize2);
        outputStream.closeArchiveEntry();

        outputStream.finish();
        byte[] expectedTrailer = ArchiveUtils.readHeader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), byteArrayOutputStream.size() - ArchiveConstants.MIN_TRAILER_SIZE);
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put(entryName1, new CpioArchiveEntry(entryName1, expectedSize1, null));
        entries.put(entryName2, new CpioArchiveEntry(entryName2, expectedSize2, null));
        Assert.assertEquals(entries, outputStream.getEntries());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        String entryName = "testfile.txt";
        String testContent = "Test content";
        long expectedSize = testContent.getBytes(StandardCharsets.UTF_8).length;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(expectedSize);
        outputStream.putArchiveEntry(entry);

        outputStream.finish();
        outputStream.write(testContent.getBytes(StandardCharsets.UTF_8), 0, testContent.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        String inputFileName = "testfile.txt";
        String entryName = "testfile.txt";

        File inputFile = new File(inputFileName);
        CpioArchiveEntry entry = outputStream.createArchiveEntry(inputFile, entryName);

        Assert.assertEquals(entryName, entry.getName());
        Assert.assertNull(entry.getData());
        Assert.assertNull(entry.getHeader());
        Assert.assertEquals(inputFile.length(), entry.getSize());
    }
}