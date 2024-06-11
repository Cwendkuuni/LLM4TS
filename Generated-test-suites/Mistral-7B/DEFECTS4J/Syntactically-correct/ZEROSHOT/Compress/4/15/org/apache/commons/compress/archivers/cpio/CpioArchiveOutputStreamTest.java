import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private Map<String, CpioArchiveEntry> names;

    @Before
    public void setUp() throws Exception {
        outputStream = new FileOutputStream(new File("test.cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        names = new HashMap<>();
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
        outputStream.close();
        new File("test.cpio").delete();
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("entry1.txt", "text/plain");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        assertEquals("entry1.txt", cpioArchiveOutputStream.getNextEntry().getName());
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws Exception {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("entry1.txt", "text/plain");
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry1));
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(null));
    }

    @Test
    public void testPutArchiveEntryWithEntryNotInstanceOfCpioArchiveEntry() throws Exception {
        ArchiveEntry invalidEntry = new ArchiveEntry("invalidEntry.txt");
        assertThrows(ClassCastException.class, () -> cpioArchiveOutputStream.putArchiveEntry(invalidEntry));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry("entry1.txt", "text/plain");
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testWriteToStream() throws Exception {
        byte[] data = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("entry1.txt", "text/plain");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(data, 0, data.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArchiveEntry nextEntry = cpioArchiveOutputStream.getNextEntry();
        byte[] readData = new byte[data.length];
        int read = 0;
        while ((read = inputStream.read(readData)) != -1) {
            cpioArchiveOutputStream.write(readData, 0, read);
        }
        inputStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveEntry nextOutputEntry = cpioArchiveOutputStream.getNextEntry();
        int remaining = data.length;
        while (remaining > 0) {
            int read = cpioArchiveOutputStream.read(readData, 0, readData.length);
            outputStream.write(readData, 0, read);
            remaining -= read;
        }
        assertEquals(data, outputStream.toByteArray());
        outputStream.close();
    }

    @Test
    public void testPutNextEntryAfterClose() throws Exception {
        cpioArchiveOutputStream.finish();
        assertFalse(cpioArchiveOutputStream.canWrite());
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("entry1.txt", "text/plain")));
    }

    @Test
    public void testFinishWithoutClosingStream() throws Exception {
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testCloseWithoutFinish() throws Exception {
        cpioArchiveOutputStream.close();
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testCloseAfterFinish() throws Exception {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryAfterFinish() throws Exception {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("entry1.txt", "text/plain"));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws Exception {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.write(new byte[1024], 0, 1024);
    }
}