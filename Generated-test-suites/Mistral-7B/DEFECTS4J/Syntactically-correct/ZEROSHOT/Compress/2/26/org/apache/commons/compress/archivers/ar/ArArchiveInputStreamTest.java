package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        String testData = "name length userid groupid filemode\n" +
                "test.txt  42 644 1001 1001\n" +
                "test2.txt 56 644 1002 1002\n" +
                "test3.txt 70 644 1003 1003\n" +
                "test4.txt 78 644 1004 1004\n" +
                "test5.txt 86 644 1005 1005\n" +
                "TRAILER\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.write(testData, outputStream, "UTF-8");
        byte[] testDataBytes = outputStream.toByteArray();

        ByteArrayInputStream testDataInputStream = new ByteArrayInputStream(testDataBytes);
        GZIPInputStream gzipInputStream = new GZIPInputStream(testDataInputStream);

        inputStream = new ArArchiveStreamFactory().createArchiveInputStream(gzipInputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry;

        for (int i = 0; i < 5; i++) {
            entry = (ArArchiveEntry) inputStream.getNextEntry();
            assertTrue(entry != null);
            assertTrue(entry.getName().endsWith(".txt"));
            assertTrue(entry.getSize() > 0);
        }

        entry = (ArArchiveEntry) inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry;

        for (int i = 0; i < 5; i++) {
            entry = (ArArchiveEntry) inputStream.getNextEntry();
            assertTrue(entry != null);
            assertTrue(entry.getName().endsWith(".txt"));
            assertTrue(entry.getSize() > 0);
        }

        entry = (ArArchiveEntry) inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNextEntryLob() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry(); // should throw an exception
    }
}