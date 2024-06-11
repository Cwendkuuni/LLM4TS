package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntryBuilder;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_NEW;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream archiveOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        archiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidArguments() throws IOException {
        // Given
        CpioArchiveEntry entry = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build();

        // When
        archiveOutputStream.putNextEntry(entry);

        // Then
        assertEquals(1, archiveOutputStream.getNumberOfEntries());
        assertEquals("test.txt", archiveOutputStream.getNameIndex(0));
        assertNull(archiveOutputStream.getNextEntry());
        assertTrue(archiveOutputStream.isInFile());
        assertEquals(0, archiveOutputStream.size());
    }

    @Test
    public void testPutNextEntryWithDuplicateName() throws IOException {
        // Given
        CpioArchiveEntry entry1 = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build();

        archiveOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build();

        // When - Assertion
        assertThrows(IOException.class, () -> archiveOutputStream.putNextEntry(entry2));
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        // Given
        CpioArchiveEntry entry = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(-1) // Invalid format
                .build();

        // When - Assertion
        assertThrows(IllegalArgumentException.class, () -> archiveOutputStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws IOException {
        // Given

        // When - Assertion
        assertThrows(NullPointerException.class, () -> archiveOutputStream.putNextEntry(null));
    }

    @Test
    public void testWriteToOutputStream() throws IOException {
        // Given
        CpioArchiveEntry entry = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build();
        archiveOutputStream.putNextEntry(entry);

        byte[] testData = "Hello, World!".getBytes();
        archiveOutputStream.write(testData, 0, testData.length);

        // When
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Then
        byte[] result = new byte[testData.length];
        int read = inputStream.read(result, 0, testData.length);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, result);
    }

    @Test
    public void testFinish() throws IOException {
        // Given
        CpioArchiveEntry entry1 = CpioArchiveEntryBuilder.newBuilder()
                .setName("test1.txt")
                .setFormat(FORMAT_NEW)
                .build();
        archiveOutputStream.putNextEntry(entry1);

        byte[] testData1 = "Hello, World!".getBytes();
        archiveOutputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = CpioArchiveEntryBuilder.newBuilder()
                .setName("test2.txt")
                .setFormat(FORMAT_NEW)
                .build();
        archiveOutputStream.putNextEntry(entry2);

        byte[] testData2 = "Goodbye, World!".getBytes();
        archiveOutputStream.write(testData2, 0, testData2.length);

        archiveOutputStream.finish();

        // When
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        int read = 0;
        byte[] buffer = new byte[1024];

        while ((read = inputStream.read(buffer)) != -1) {
            entries.put(new String(buffer, 0, read), archiveOutputStream.getInputFileEntry(read));
        }

        // Then
        assertEquals(2, archiveOutputStream.getNumberOfEntries());
        CpioArchiveEntry entry1FromFile = entries.get("test1.txt");
        CpioArchiveEntry entry2FromFile = entries.get("test2.txt");

        assertEquals("test1.txt", entry1FromFile.getName());
        assertEquals("test2.txt", entry2FromFile.getName());
        assertEquals(testData1.length + testData2.length, archiveOutputStream.size());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Given
        CpioArchiveEntry entry = CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build();
        archiveOutputStream.putNextEntry(entry);

        byte[] testData = "Hello, World!".getBytes();
        archiveOutputStream.write(testData, 0, testData.length);

        // When
        archiveOutputStream.closeArchiveEntry();

        // Then
        assertFalse(archiveOutputStream.isInFile());
        assertNull(archiveOutputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        // Given
        archiveOutputStream.putNextEntry(CpioArchiveEntryBuilder.newBuilder()
                .setName("test.txt")
                .setFormat(FORMAT_NEW)
                .build());

        // When
        archiveOutputStream.close();

        // Then
        assertTrue(archiveOutputStream.isClosed());
        assertNull(archiveOutputStream.getNextEntry());
    }
}