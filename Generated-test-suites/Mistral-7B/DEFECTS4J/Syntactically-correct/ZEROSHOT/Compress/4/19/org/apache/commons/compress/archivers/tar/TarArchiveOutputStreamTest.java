import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 256;
    private static final int RECORD_SIZE = 512;
    private static final int LONG_FILE_NAME_LENGTH = TarConstants.NAMELEN + 1;

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream(1024);
        tarOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), BLOCK_SIZE, RECORD_SIZE);
        assertNull(tarOutputStream.getArchiveEntry());
        assertFalse(tarOutputStream.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOutputStream() {
        new TarArchiveOutputStream(null, BLOCK_SIZE, RECORD_SIZE);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        String entryName = "testEntry.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);

        assertEquals(entryName, tarOutputStream.getArchiveEntry().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNonTarArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new JarEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntryWithValidFileNameAndData() throws IOException {
        String entryName = "testEntry.txt";
        String data = "Hello, World!";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);

        ByteArrayOutputStream entryDataOutputStream = new ByteArrayOutputStream(data.length());
        entryDataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        entryDataOutputStream.close();

        byte[] entryData = entryDataOutputStream.toByteArray();
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(entryData, 0, entryData.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[RECORD_SIZE];
        int bytesRead;

        // Read the header record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        TarArchiveEntry headerEntry = new TarArchiveEntry(new ByteArrayInputStream(buffer));
        assertEquals(entryName, headerEntry.getName());
        assertEquals(entryName.length(), headerEntry.getSize());
        assertEquals("testEntry.txt", headerEntry.getName());
        assertEquals("testEntry.txt", headerEntry.getMethodName());

        // Read the data record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        assertEquals(entryData.length, bytesRead);
        assertEquals(data, new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));

        gzipInputStream.close();
        inputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        String entryName = "testEntry.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.finish();
        tarOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[RECORD_SIZE];
        int bytesRead;

        // Read the header record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        TarArchiveEntry headerEntry = new TarArchiveEntry(new ByteArrayInputStream(buffer));
        assertEquals(entryName, headerEntry.getName());
        assertEquals(entryName.length(), headerEntry.getSize());
        assertEquals("testEntry.txt", headerEntry.getName());
        assertEquals("testEntry.txt", headerEntry.getMethodName());

        // Read the data record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);

        // Read the EOF record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);

        gzipInputStream.close();
        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.close();

        assertFalse(tarOutputStream.isOpen());
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        String entryName = "testEntry.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);

        String data = "Hello, World!";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        int bytesWritten = tarOutputStream.write(dataBytes, 0, dataBytes.length);
        assertEquals(dataBytes.length, bytesWritten);

        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[RECORD_SIZE];
        int bytesRead;

        // Read the header record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        TarArchiveEntry headerEntry = new TarArchiveEntry(new ByteArrayInputStream(buffer));
        assertEquals(entryName, headerEntry.getName());
        assertEquals(entryName.length() + dataBytes.length, headerEntry.getSize());
        assertEquals("testEntry.txt", headerEntry.getName());
        assertEquals("testEntry.txt", headerEntry.getMethodName());

        // Read the data record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        assertEquals(dataBytes.length, bytesRead);
        assertEquals(data, new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));

        gzipInputStream.close();
        inputStream.close();
    }

    @Test
    public void testWriteWithInvalidDataLength() throws IOException {
        String entryName = "testEntry.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);

        String data = "Hello, World!";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        int bytesWritten = tarOutputStream.write(dataBytes, 0, dataBytes.length + 1);
        assertEquals(dataBytes.length, bytesWritten);

        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[RECORD_SIZE];
        int bytesRead;

        // Read the header record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        TarArchiveEntry headerEntry = new TarArchiveEntry(new ByteArrayInputStream(buffer));
        assertEquals(entryName, headerEntry.getName());
        assertEquals(entryName.length() + dataBytes.length, headerEntry.getSize());
        assertEquals("testEntry.txt", headerEntry.getName());
        assertEquals("testEntry.txt", headerEntry.getMethodName());

        // Read the data record.
        bytesRead = gzipInputStream.read(buffer, 0, buffer.length);
        assertEquals(dataBytes.length, bytesRead);
        assertEquals(data, new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));

        gzipInputStream.close();
        inputStream.close();
    }
}