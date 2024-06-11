package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;

public class CpioArchiveOutputStreamTest {

    private static final byte[] TEST_DATA = "Test data".getBytes(StandardCharsets.UTF_8);

    @org.junit.Test
    public void testNewInstance() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {
            assertNotNull(cpioOut);
        }
    }

    @org.junit.Test
    public void testWriteNewEntryWithValidData() throws IOException {
        String entryName = "testEntry.txt";
        String expectedName = entryName;
        long expectedSize = 11;
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
            cpioEntry.setName(entryName);
            cpioEntry.setFileSize(expectedSize);
            cpioOut.putNextEntry(cpioEntry);
            cpioOut.write(TEST_DATA, 0, TEST_DATA.length);
            cpioOut.closeArchiveEntry();

            ArchiveEntry archiveEntry = cpioOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals(expectedName, archiveEntry.getName());
            assertEquals(expectedSize, archiveEntry.getSize());
            assertFalse(archiveEntry.isDirectory());
        }
    }

    @org.junit.Test
    public void testWriteOldEntryWithValidData() throws IOException {
        String entryName = "testEntry.bin";
        long expectedSize = 11;
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_BINARY)) {

            CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
            cpioEntry.setName(entryName);
            cpioEntry.setFileSize(expectedSize);
            cpioOut.putNextEntry(cpioEntry);
            cpioOut.write(TEST_DATA, 0, TEST_DATA.length);
            cpioOut.closeArchiveEntry();

            ArchiveEntry archiveEntry = cpioOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals(expectedSize, archiveEntry.getSize());
            assertTrue(archiveEntry.isDirectory());
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithDuplicateName() throws IOException {
        String entryName = "testEntry.txt";
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            CpioArchiveEntry cpioEntry1 = new CpioArchiveEntry();
            cpioEntry1.setName(entryName);
            cpioOut.putNextEntry(cpioEntry1);

            CpioArchiveEntry cpioEntry2 = new CpioArchiveEntry();
            cpioEntry2.setName(entryName);
            cpioOut.putNextEntry(cpioEntry2);

        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithNegativeSize() throws IOException {
        String entryName = "testEntry.txt";
        long expectedSize = -1;
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
            cpioEntry.setName(entryName);
            cpioEntry.setFileSize(expectedSize);
            cpioOut.putNextEntry(cpioEntry);

        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithNullName() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
            cpioEntry.setName(null);
            cpioOut.putNextEntry(cpioEntry);

        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteEntryWithEmptyName() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
            cpioEntry.setName("");
            cpioOut.putNextEntry(cpioEntry);

        }
    }

    @org.junit.Test
    public void testWriteTrailerEntry() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream(1024);
             CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW)) {

            cpioOut.finish();

            ArchiveEntry archiveEntry = cpioOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals("TRAILER!!!", archiveEntry.getName());
            assertFalse(archiveEntry.isDirectory());
            assertEquals(-1, archiveEntry.getSize());

        }
    }
}