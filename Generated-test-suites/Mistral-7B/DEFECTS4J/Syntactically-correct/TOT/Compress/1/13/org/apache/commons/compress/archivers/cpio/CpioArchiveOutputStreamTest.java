package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
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
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream out;
    private ByteArrayOutputStream buffer;

    @Before
    public void setUp() throws IOException {
        buffer = new ByteArrayOutputStream();
        out = new CpioArchiveOutputStream(buffer, CpioArchiveOutputStream.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    @Test
    public void testEmptyOutput() throws IOException {
        out.finish();
        assertEquals("", buffer.toString());
    }

    @Test
    public void testWriteEmptyEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();

        byte[] expected = {MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3], 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 7, 't', 'e', 's', 't', 0};
        byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteNonEmptyEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setSize(10);

        byte[] data = "Hello World".getBytes();
        out.putNextEntry(entry);
        out.write(data, 0, data.length);
        out.closeArchiveEntry();
        out.finish();

        byte[] expected = {MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3], 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0,
                0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 10, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r',
                 'l', 'd', 0};
        byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);

        InputStream in = new ByteArrayInputStream(actual);
        CpioArchiveEntry testEntry = new CpioArchiveEntry(in);
        assertEquals("test", testEntry.getName());
        assertEquals(10, testEntry.getSize());
        assertEquals("Hello World", new String(testEntry.getData()));
    }

    @Test
    public void testWriteDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        assertThrows(IOException.class, () -> out.putNextEntry(entry));
    }

    @Test
    public void testWriteInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY + 1);
        out.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> setUp());
    }

    @Test
    public void testWriteWithFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        out.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        out.write(data, 0, data.length);
        out.finish();

        byte[] expected = {MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3], 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0,
                0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r',
                 'l', 'd', 0, MAGIC_NEW_CRC[0], MAGIC_NEW_CRC[1], MAGIC_NEW_CRC[2],
                MAGIC_NEW_CRC[3], 8, 0, 0, 7, 't', 'e', 's', 't', 0};
        byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteWithFinishAndClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        out.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        out.write(data, 0, data.length);
        out.closeArchiveEntry();
        out.finish();

        byte[] expected = {MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3], 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0,
                0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r',
                 'l', 'd', 0, MAGIC_NEW_CRC[0], MAGIC_NEW_CRC[1], MAGIC_NEW_CRC[2],
                MAGIC_NEW_CRC[3], 8, 0, 0, 7, 't', 'e', 's', 't', 0, MAGIC_NEW_CRC[0],
                MAGIC_NEW_CRC[1], MAGIC_NEW_CRC[2], MAGIC_NEW_CRC[3], 8, 0, 0, 1, 'T', 'R',
                'A', 'I', 'L', 'E', 'R', '!!!', 0};
        byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }
}