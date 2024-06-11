import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntry_ValidEntry() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(MAGIC_NEW.length, out.size());
        // Verify that the header has been written
        final byte[] header = new byte[entry.getName().length() + 1];
        out.read(header, 0, header.length);
        assertEquals(entry.getName().getBytes().length + 1, header.length);
        assertEquals(CpioConstants.FORMAT_NEW, header[0] & 0xFF);
        assertEquals(entry.getName().charAt(0), header[1]);
        for (int i = 0; i < entry.getName().length(); i++) {
            assertEquals(entry.getName().charAt(i), header[i + 2]);
        }
    }

    @Test(expected = IOException.class)
    public void testPutNextEntry_DuplicateEntry() throws IOException {
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
    }

    @Test
    public void testPutNextEntry_SetFormat() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioArchiveOutputStream.putNextEntry(entry);
        final byte[] header = new byte[entry.getName().length() + 1];
        out.read(header, 0, header.length);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, header[0] & 0xFF);
    }

    @Test
    public void testWrite_ValidData() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        final byte[] data = "test data".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        assertEquals(entry.getName().getBytes().length + data.length + 1, out.size());
        final InputStream input = new ByteArrayInputStream(out.toByteArray());
        final byte[] expected = new byte[data.length];
        input.read(expected);
        assertArrayEquals(data, expected);
    }

    @Test
    public void testWrite_OutOfBounds() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        final byte[] data = new byte[1024 * 1024 * 1024];
        cpioArchiveOutputStream.write(data, 1024 * 1024, data.length - 1024 * 1024);
        assertFalse(cpioArchiveOutputStream.hasError());
        cpioArchiveOutputStream.close();
        final InputStream input = new FileOutputStream("output.cpio").getChannel()
                .map(FileChannel.MapMode.READ_ONLY, 0, out.size())
                .map(FileChannel.MapMode.READ_ONLY, 0, data.length);
        final byte[] expected = new byte[data.length - 1024 * 1024];
        input.read(expected);
        assertArrayEquals(data, expected);
    }

    @Test(expected = IOException.class)
    public void testWrite_ClosedStream() throws IOException {
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
    }

    @Test
    public void testFinish() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("test data".getBytes());
        cpioArchiveOutputStream.finish();
        assertEquals("TRAILER!!!".getBytes().length, out.size() % 4);
    }

    @Test
    public void testClose() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("test data".getBytes());
        cpioArchiveOutputStream.close();
        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertTrue(cpioArchiveOutputStream.closed);
    }
}