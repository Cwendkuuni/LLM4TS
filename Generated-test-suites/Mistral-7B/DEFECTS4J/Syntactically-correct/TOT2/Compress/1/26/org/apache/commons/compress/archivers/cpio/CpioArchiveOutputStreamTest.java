import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioOutput;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.out = new ByteArrayOutputStream();
        this.cpioOutput = new CpioArchiveOutputStream(this.out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void cleanup() throws IOException {
        this.cpioOutput.finish();
        this.cpioOutput.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(4);
        this.cpioOutput.putNextEntry(entry);
        assertNotNull(this.cpioOutput.cpioEntry);
        assertEquals("testfile", this.cpioOutput.cpioEntry.getName());
        assertEquals(CpioConstants.FORMAT_NEW, this.cpioOutput.entryFormat);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        this.cpioOutput.putNextEntry(entry);
        this.cpioOutput.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testBytes = "test".getBytes();
        this.cpioOutput.putNextEntry(new CpioArchiveEntry());
        this.cpioOutput.write(testBytes);
        byte[] expectedBytes = new byte[testBytes.length + 1];
        System.arraycopy(testBytes, 0, expectedBytes, 0, testBytes.length);
        expectedBytes[testBytes.length] = 0;
        byte[] outputBytes = this.out.toByteArray();
        assertArrayEquals(expectedBytes, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutEntry() throws IOException {
        this.cpioOutput.write("test".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        this.cpioOutput.putNextEntry(new CpioArchiveEntry());
        this.cpioOutput.write("test".getBytes());
        this.cpioOutput.finish();
        assertTrue(this.cpioOutput.finished);
        assertFalse(this.cpioOutput.closed);
        assertNull(this.cpioOutput.cpioEntry);
    }

    @Test(expected = IOException.class)
    public void testFinishWithEntry() throws IOException {
        this.cpioOutput.putNextEntry(new CpioArchiveEntry());
        this.cpioOutput.write("test".getBytes());
        this.cpioOutput.finish();
        this.cpioOutput.putNextEntry(new CpioArchiveEntry());
    }

    @Test
    public void testClose() throws IOException {
        this.cpioOutput.putNextEntry(new CpioArchiveEntry());
        this.cpioOutput.write("test".getBytes());
        this.cpioOutput.finish();
        assertTrue(this.cpioOutput.closed);
        assertFalse(this.cpioOutput.finished);
        assertNull(this.cpioOutput.cpioEntry);
    }
}