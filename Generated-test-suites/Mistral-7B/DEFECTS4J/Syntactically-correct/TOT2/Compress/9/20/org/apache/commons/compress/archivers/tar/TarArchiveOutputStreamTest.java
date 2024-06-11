import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TarArchiveOutputStream} class.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test", null);
        outputStream = new FileOutputStream(tempFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.finish();
            tarOutputStream.close();
        }

        if (outputStream != null) {
            outputStream.close();
        }

        if (tempFile != null && tempFile.exists()) {
            IOUtils.deleteQuietly(tempFile);
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);
        tarOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.close();

        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream2.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream2.close();

        TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream3.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream3.close();
    }

    @Test
    public void testFinish() throws IOException {
        assertFalse(tarOutputStream.isFinished());
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(tarOutputStream.isClosed());
        tarOutputStream.finish();
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());

        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        assertFalse(tarOutputStream.isFinished());
        assertFalse(tarOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final String entryName = "test.txt";
        final byte[] entryBytes = "Hello, World!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entryBytes, 0, entryBytes.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        tarOutputStream.putArchiveEntry(readEntry);
        byte[] readBytes = new byte[entryBytes.length];
        tarOutputStream.read(readBytes, 0, readBytes.length);
        assertEquals(entryBytes, readBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNull() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryClassCastException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("nonTarArchiveEntry".getBytes());
        ArchiveEntry archiveEntry = inputStream;
        try {
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            tarOutputStream.putArchiveEntry(archiveEntry);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            //expected
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryIOException() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), "test.txt");
        byte[] entryBytes = "Hello, World!".getBytes();
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entryBytes, 0, entryBytes.length);
        tarOutputStream.putArchiveEntry(entry); //second call should fail
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        assertFalse(tarOutputStream.isEntryClosed());
        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertTrue(tarOutputStream.isEntryClosed());

        byte[] entryBytes = "Hello, World!".getBytes();
        tarOutputStream.write(entryBytes, 0, entryBytes.length);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.isEntryClosed());
    }

    @Test
    public void testWrite() throws IOException {
        final String entryName = "test.txt";
        final byte[] entryBytes = "Hello, World!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entryBytes, 0, entryBytes.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        tarOutputStream.putArchiveEntry(readEntry);
        byte[] readBytes = new byte[entryBytes.length];
        int read = tarOutputStream.read(readBytes, 0, entryBytes.length);
        assertEquals(entryBytes.length, read);
        assertEquals(entryBytes, readBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        final String entryName = "test.txt";
        final byte[] entryBytes = "Hello, World!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entryBytes, 0, entryBytes.length);
        tarOutputStream.write(entryBytes, 0, entryBytes.length); // second write should fail
    }

    @Test
    public void testFinishAndClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("./"), "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello, World!".getBytes(), 0, "Hello, World!".getBytes().length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new File(tempFile.getAbsolutePath()));
        TarArchiveEntry entryRead = tarInputStream.getNextEntry();
        assertTrue(entryRead.equals(readEntry));
        tarInputStream.closeEntry();
        tarInputStream.close();
    }
}