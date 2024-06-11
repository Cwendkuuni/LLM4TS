import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidValue() {
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(2 * 512, outputStream.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishBeforePutArchiveEntry() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "test.txt";
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertNotNull(entry);
        assertEquals(fileName, entry.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String fileName = "test.txt";
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // Write some content
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(2 * 512 + 512, bytes.length);

        int eofIndex = 2 * 512;
        for (int i = 0; i < eofIndex; ++i) {
            assertEquals(0, bytes[i]);
        }
    }

    @Test
    public void testWrite() throws IOException {
        String fileName = "test.txt";
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        int eofIndex = 2 * 512;

        for (int i = 0; i < eofIndex; ++i) {
            assertEquals(0, bytes[i]);
        }

        for (int i = eofIndex; i < bytes.length; ++i) {
            assertEquals(data[i - (eofIndex - data.length)], bytes[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteOutOfBounds() throws IOException {
        String fileName = "test.txt";
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length + 1);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String fileName1 = "test1.txt";
        String fileName2 = "test2.txt";

        TarArchiveEntry entry1 = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName1);
        tarArchiveOutputStream.putArchiveEntry(entry1);
        byte[] data1 = "First entry content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data1, 0, data1.length);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry entry2 = tarArchiveOutputStream.createArchiveEntry(new File(""), fileName2);
        tarArchiveOutputStream.putArchiveEntry(entry2);
        byte[] data2 = "Second entry content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data2, 0, data2.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();
        int eofIndex = 2 * 512;

        for (int i = 0; i < eofIndex; ++i) {
            assertEquals(0, bytes[i]);
        }

        int pos = eofIndex;
        for (int i = 0; i < data1.length; ++i) {
            assertEquals(data1[i], bytes[pos++]);
        }
        for (int i = 0; i < data2.length; ++i) {
            assertEquals(data2[i], bytes[pos++]);
        }

        for (int i = pos; i < bytes.length; ++i) {
            assertEquals(0, bytes[i]);
        }
    }
}