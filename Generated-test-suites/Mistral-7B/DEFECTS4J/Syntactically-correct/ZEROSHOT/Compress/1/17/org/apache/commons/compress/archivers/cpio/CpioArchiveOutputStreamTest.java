package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for the {@code CpioArchiveOutputStream} class
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream testOutputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        testOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(testOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
    }

    /**
     * Test constructor with valid format
     */
    @Test
    public void testCtorValidFormat() {
        // Create an instance with valid format
        cpioOutputStream = new CpioArchiveOutputStream(testOutputStream, CpioConstants.FORMAT_NEW);

        // Assert that the instance is correctly initialized
        assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
        assertFalse(cpioOutputStream.isFinished());
        assertNull(cpioOutputStream.getCpioEntry());
        assertEquals(0, cpioOutputStream.getWritten());
    }

    /**
     * Test constructor with invalid format
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtorInvalidFormat() {
        // Create an instance with invalid format
        cpioOutputStream = new CpioArchiveOutputStream(testOutputStream, (short) 0xFFFF);
    }

    /**
     * Test method putNextEntry with valid CpioArchiveEntry
     */
    @Test
    public void testPutNextEntryValidCpioEntry() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);

        // Put the valid CpioArchiveEntry
        cpioOutputStream.putNextEntry(entry);

        // Assert that the putNextEntry method was executed correctly
        assertEquals(entry, cpioOutputStream.getCpioEntry());
        assertEquals(entryName, cpioOutputStream.getCpioEntry().getName());
        assertEquals(entrySize, cpioOutputStream.getCpioEntry().getSize());
        assertFalse(cpioOutputStream.isFinished());
    }

    /**
     * Test method putNextEntry with invalid CpioArchiveEntry (duplicate entry)
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryInvalidCpioEntryDuplicateEntry() throws IOException {
        // Create a CpioArchiveEntry and put it in the output stream
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Try to put the same CpioArchiveEntry again
        cpioOutputStream.putNextEntry(entry);
    }

    /**
     * Test method putNextEntry with null CpioArchiveEntry
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNullCpioEntry() {
        // Try to put a null CpioArchiveEntry
        cpioOutputStream.putNextEntry(null);
    }

    /**
     * Test method putNextEntry with CpioArchiveEntry with invalid format
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryInvalidCpioEntryFormat() throws IOException {
        // Create a CpioArchiveEntry with invalid format
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize, (short) 0xFFFF);

        // Try to put the CpioArchiveEntry with invalid format
        cpioOutputStream.putNextEntry(entry);
    }

    /**
     * Test method write with valid data
     */
    @Test
    public void testWriteValidData() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Assert that the data was written correctly
        byte[] expectedData = data.getBytes();
        byte[] actualData = ((ByteArrayOutputStream) testOutputStream).toByteArray();
        assertEquals(expectedData.length, actualData.length);
        assertTrue(new String(actualData).equals(data));
    }

    /**
     * Test method write with invalid offset or length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteInvalidOffsetOrLength() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream with invalid offset or length
        byte[] data = "Test data".getBytes();
        cpioOutputStream.write(data, 1, data.length + 1);
    }

    /**
     * Test method closeArchiveEntry with valid CpioArchiveEntry
     */
    @Test
    public void testCloseArchiveEntryValidCpioEntry() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Close the current CpioArchiveEntry
        cpioOutputStream.closeArchiveEntry();

        // Assert that the closeArchiveEntry method was executed correctly
        assertNull(cpioOutputStream.getCpioEntry());
        assertTrue(cpioOutputStream.isFinished());
    }

    /**
     * Test method closeArchiveEntry with invalid CpioArchiveEntry (entry is not
     * the current one)
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidCpioEntry() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry1.txt";
        long entrySize = 10;
        CpioArchiveEntry entry1 = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry1);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Create another valid CpioArchiveEntry
        String entryName2 = "testEntry2.txt";
        long entrySize2 = 20;
        CpioArchiveEntry entry2 = new CpioArchiveEntry(entryName2, entrySize2);
        cpioOutputStream.putNextEntry(entry2);

        // Try to close the first CpioArchiveEntry
        cpioOutputStream.closeArchiveEntry();
    }

    /**
     * Test method finish with valid data
     */
    @Test
    public void testFinishValidData() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Finish the output stream
        cpioOutputStream.finish();

        // Assert that the finish method was executed correctly
        assertTrue(cpioOutputStream.isFinished());
        assertEquals(entrySize + CpioConstants.TRAILER_SIZE, testOutputStream.size());
    }

    /**
     * Test method finish with invalid data (entry is not the current one)
     */
    @Test(expected = IOException.class)
    public void testFinishInvalidData() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry1.txt";
        long entrySize = 10;
        CpioArchiveEntry entry1 = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry1);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Create another valid CpioArchiveEntry
        String entryName2 = "testEntry2.txt";
        long entrySize2 = 20;
        CpioArchiveEntry entry2 = new CpioArchiveEntry(entryName2, entrySize2);
        cpioOutputStream.putNextEntry(entry2);

        // Try to finish the output stream
        cpioOutputStream.finish();
    }

    /**
     * Test method close with valid data
     */
    @Test
    public void testCloseValidData() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Close the output stream
        cpioOutputStream.close();

        // Assert that the close method was executed correctly
        assertTrue(cpioOutputStream.isClosed());
        assertEquals(entrySize + CpioConstants.TRAILER_SIZE, testOutputStream.size());
    }

    /**
     * Test method close with invalid data (stream is already closed)
     */
    @Test(expected = IOException.class)
    public void testCloseInvalidData() throws IOException {
        // Create a valid CpioArchiveEntry
        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName, entrySize);
        cpioOutputStream.putNextEntry(entry);

        // Write some data into the output stream
        String data = "Test data";
        cpioOutputStream.write(data.getBytes());

        // Close the output stream
        cpioOutputStream.close();

        // Try to close the output stream again
        cpioOutputStream.close();
    }
}