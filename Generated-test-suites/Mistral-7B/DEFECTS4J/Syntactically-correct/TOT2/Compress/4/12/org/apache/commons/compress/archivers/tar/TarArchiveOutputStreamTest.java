package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    /**
     * Sets up the test environment.
     *
     * @throws IOException on failure
     */
    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    /**
     * Tears down the test environment.
     *
     * @throws IOException on failure
     */
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Tests the constructor with the default block and record size.
     */
    @Test
    public void testConstructorDefault() {
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals("Expected default record size", TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    /**
     * Tests the constructor with specified block and record size.
     */
    @Test
    public void testConstructorCustom() {
        int blockSize = 1024;
        int recordSize = 512;
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), blockSize, recordSize);
        assertEquals("Expected specified record size", recordSize, tarArchiveOutputStream.getRecordSize());
    }

    /**
     * Tests setting long file mode.
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertFalse("Expected long file mode not to be error", tarArchiveOutputStream.isLongFileModeError());
    }

    /**
     * Tests writing an entry with a short name.
     */
    @Test
    public void testWriteShortEntry() throws IOException {
        String name = "testShortEntry.txt";
        TarArchiveEntry entry = new TarArchiveEntry(name);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNotNull("Expected entry to be present", archiveEntry);
        assertEquals("Expected entry name to match", name, archiveEntry.getName());
        byte[] entryData = ((TarArchiveEntry) archiveEntry).getInputStream().readAllBytes();
        assertArrayEquals("Expected entry data to match", data, entryData);
    }

    /**
     * Tests writing an entry with a long name.
     */
    @Test
    public void testWriteLongEntry() throws IOException {
        String name = "testLongEntryWithLongFileName.txt";
        TarArchiveEntry entry = new TarArchiveEntry(name);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNotNull("Expected entry to be present", archiveEntry);
        assertEquals("Expected entry name to match", name, archiveEntry.getName());
        byte[] entryData = ((TarArchiveEntry) archiveEntry).getInputStream().readAllBytes();
        assertArrayEquals("Expected entry data to match", data, entryData);
    }

    /**
     * Tests writing multiple entries.
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        String name1 = "testEntry1.txt";
        String name2 = "testEntry2.txt";

        TarArchiveEntry entry1 = new TarArchiveEntry(name1);
        TarArchiveEntry entry2 = new TarArchiveEntry(name2);

        tarArchiveOutputStream.putArchiveEntry(entry1);
        byte[] data1 = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data1, 0, data1.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putArchiveEntry(entry2);
        byte[] data2 = "Hello Test!".getBytes();
        tarArchiveOutputStream.write(data2, 0, data2.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry1 = tarArchiveOutputStream.getNextEntry();
        assertNotNull("Expected entry1 to be present", archiveEntry1);
        assertEquals("Expected entry1 name to match", name1, archiveEntry1.getName());
        byte[] entryData1 = ((TarArchiveEntry) archiveEntry1).getInputStream().readAllBytes();
        assertArrayEquals("Expected entry1 data to match", data1, entryData1);

        ArchiveEntry archiveEntry2 = tarArchiveOutputStream.getNextEntry();
        assertNotNull("Expected entry2 to be present", archiveEntry2);
        assertEquals("Expected entry2 name to match", name2, archiveEntry2.getName());
        byte[] entryData2 = ((TarArchiveEntry) archiveEntry2).getInputStream().readAllBytes();
        assertArrayEquals("Expected entry2 data to match", data2, entryData2);
    }

    /**
     * Tests writing to a closed stream.
     */
    @Test(expected = IOException.class)
    public void testWriteToClosedStream() throws IOException {
        tarArchiveOutputStream.close();
        byte[] data = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    /**
     * Tests writing data larger than the entry size.
     */
    @Test(expected = IOException.class)
    public void testWriteDataLargerThanEntrySize() throws IOException {
        String name = "testEntry.txt";
        TarArchiveEntry entry = new TarArchiveEntry(name);
        tarArchiveOutputStream.putArchiveEntry(entry);
        int entrySize = 10;
        byte[] data = new byte[entrySize + 1];
        tarArchiveOutputStream.write(data, 0, data.length);
    }
}