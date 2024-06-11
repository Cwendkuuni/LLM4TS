import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream buffer;

    @Before
    public void setUp() throws Exception {
        buffer = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(buffer, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        outputStream.putNextEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(buffer.toByteArray());
        assertEquals(11, IOUtils.sizeOf(inputStream));

        Map<String, ArchiveEntry> entries = outputStream.getEntries();
        ArchiveEntry archiveEntry = entries.get("test.txt");
        assertNotNull(archiveEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals(10, archiveEntry.getSize());
        assertFalse(archiveEntry.isDirectory());
    }

    @Test
    public void testPutNextEntryWithInvalidEntryFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> {
            entry.setFormat(-1);
            outputStream.putNextEntry(entry);
        });
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        try {
            outputStream.putNextEntry(entry);
            assertThrows(IOException.class, () -> {
                outputStream.putNextEntry(entry);
            });
        } finally {
            outputStream.closeArchiveEntry();
        }
    }

    @Test
    public void testPutNextEntryWithInvalidEntrySize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(-10);
        outputStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> {
            outputStream.write("Hello World!".getBytes());
        });
    }

    @Test
    public void testPutNextEntryWithInvalidEntryName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(null);
        entry.setSize(10);
        assertThrows(NullPointerException.class, () -> {
            outputStream.putNextEntry(entry);
        });

        entry.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            outputStream.putNextEntry(entry);
        });

        entry.setName("test\0txt");
        assertThrows(IllegalArgumentException.class, () -> {
            outputStream.putNextEntry(entry);
        });
    }

    @Test
    public void testPutNextEntryWithInvalidEntryMode() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        try {
            entry.setMode(-1);
            outputStream.putNextEntry(entry);
        } finally {
            outputStream.closeArchiveEntry();
        }
    }

    @Test
    public void testPutNextEntryWithUnsupportedFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        try {
            entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
            outputStream.putNextEntry(entry);
        } finally {
            outputStream.closeArchiveEntry();
        }
    }

    @Test
    public void testWriteDataToEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] data = "Hello World!".getBytes();
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(buffer.toByteArray());
        byte[] result = new byte[data.length];
        IOUtils.readFully(inputStream, result);

        assertArrayEquals(data, result);
    }

    @Test
    public void testWriteDataToEntryWithInvalidDataLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] data = "Hello World!".getBytes();
        try {
            outputStream.write(data, 0, data.length + 1);
        } finally {
            outputStream.closeArchiveEntry();
        }
    }

    @Test
    public void testWriteDataToEntryWithInvalidEntrySize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        outputStream.putNextEntry(entry);

        byte[] data = "Hello World!".getBytes();
        try {
            outputStream.write(data, 0, data.length);
        } finally {
            outputStream.closeArchiveEntry();
        }

        assertFalse(outputStream.finished);
    }

    @Test
    public void testFinish() throws IOException {
        outputStream.finish();
        assertTrue(outputStream.finished);
    }

    @Test
    public void testFinishTwice() throws IOException {
        outputStream.finish();
        outputStream.finish();
        assertTrue(outputStream.finished);
    }

    @Test
    public void testClose() throws IOException {
        outputStream.close();
        assertTrue(outputStream.closed);
    }

    @Test
    public void testCloseTwice() throws IOException {
        outputStream.close();
        outputStream.close();
        assertTrue(outputStream.closed);
    }

    @Test
    public void testCloseAfterFinish() throws IOException {
        outputStream.finish();
        outputStream.close();
        assertTrue(outputStream.closed);
        assertTrue(outputStream.finished);
    }
}