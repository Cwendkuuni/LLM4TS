package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";

    @org.junit.Test
    public void testWriteNewEntryWithValidData() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size());
        out.close();

        ArchiveInputStream in = new CpioFileArchiveInputStream(outputFile);
        ArchiveEntry entryRead = in.getNextEntry();
        assertEquals("test.txt", entryRead.getName());
        assertTrue(entryRead instanceof CpioArchiveEntry);
        assertEquals("test data", new String(in.readAllBytes()));
        in.close();
        outputFile.delete();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testWriteNewEntryWithDuplicateName() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);

        ArchiveEntry entry1 = new CpioArchiveEntry("test.txt", "test data");
        ArchiveEntry entry2 = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry1);
        out.putArchiveEntry(entry2);
        out.close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testWriteOldEntryWithInvalidFormat() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), (short) 0xFF);
        out.close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testWriteEntryWithNullEntryName() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry(null, "test data");
        out.putArchiveEntry(entry);
        out.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithInvalidEntrySize() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        entry.setSize(dataOut.size() + 1);
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size());
        out.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithDataLargerThanExpected() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size() + 1);
        out.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithInvalidChksum() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW_CRC);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size());
        out.close();

        ArchiveInputStream in = new CpioFileArchiveInputStream(outputFile);
        ArchiveEntry entryRead = in.getNextEntry();
        assertFalse(entryRead instanceof CpioArchiveEntry);
        in.close();
    }

    @org.junit.Test
    public void testWriteEntryWithValidDataAndChksum() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW_CRC);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size());
        out.close();

        ArchiveInputStream in = new CpioFileArchiveInputStream(outputFile);
        ArchiveEntry entryRead = in.getNextEntry();
        assertEquals("test.txt", entryRead.getName());
        assertTrue(entryRead instanceof CpioArchiveEntry);
        assertEquals("test data", new String(in.readAllBytes()));
        in.close();
        outputFile.delete();
    }

    @org.junit.Test
    public void testWriteEntryWithDataAndFilter() throws IOException {
        final File outputFile = new File(TEST_FILE_NAME);

        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        dataOut.write("test data".getBytes());

        ByteArrayInputStream dataIn = new ByteArrayInputStream(
                dataOut.toByteArray());
        ByteArrayOutputStream filteredOut = new ByteArrayOutputStream();

        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test data");
        out.putArchiveEntry(entry);
        out.write(dataOut.toByteArray(), 0, dataOut.size());
        out.filter(dataIn, filteredOut);
        out.close();

        ArchiveInputStream in = new CpioFileArchiveInputStream(outputFile);
        ArchiveEntry entryRead = in.getNextEntry();
        assertEquals("test.txt", entryRead.getName());
        assertTrue(entryRead instanceof CpioArchiveEntry);
        assertEquals("test data", new String(in.readAllBytes()));
        in.close();
        outputFile.delete();

        assertEquals("test data", new String(filteredOut.toByteArray()));
    }

}