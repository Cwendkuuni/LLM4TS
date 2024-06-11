import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    /**
     * Sets up for tests.
     *
     * @throws IOException on error
     */
    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarArchiveOutputStream.DEFAULT_BLOCK_SIZE,
                TarArchiveOutputStream.DEFAULT_RECORD_SIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Tears down after tests.
     *
     * @throws IOException on error
     */
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Tests constructor.
     *
     * @throws IOException on error
     */
    @Test
    public void testConstructor() throws IOException {
        assertNull(tarArchiveOutputStream.getArchiveEntry());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    /**
     * Tests setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Tests finish method.
     *
     * @throws IOException on error
     */
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test.txt"));
        tarArchiveOutputStream.write("Hello world!".getBytes(StandardCharsets.UTF_8), 0, "Hello world!".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] expectedHeader = new byte[512];
        byte[] expectedData = new byte[13];
        System.arraycopy(TarConstants.MAGIC, 0, expectedHeader, 0, TarConstants.MAGIC.length);
        TarArchiveEntry expectedArchiveEntry = new TarArchiveEntry("test.txt", "test.txt", 13, 0, null, new File("."));
        expectedArchiveEntry.writeHeader(expectedHeader, 0);
        System.arraycopy("test.txt\0".getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 512 - TarConstants.NAMELEN, TarConstants.NAMELEN);
        InputStream expectedInputStream = new ByteArrayInputStream("Hello world!".getBytes(StandardCharsets.UTF_8));
        byte[] recordBuf = new byte[TarArchiveOutputStream.DEFAULT_RECORD_SIZE];
        int numRead;
        while ((numRead = expectedInputStream.read(recordBuf)) > 0) {
            tarArchiveOutputStream.write(recordBuf, 0, numRead);
        }
        expectedInputStream.close();
        byte[] output = outputStream.toByteArray();
        byte[] expectedOutput = new byte[1024 + 512 + 512];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(expectedHeader, 0, expectedOutput, 512, expectedHeader.length);
        System.arraycopy(output, 0, expectedOutput, 1024, output.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(1, tarArchiveOutputStream.getNumberOfEntries());
        TarArchiveEntry actualArchiveEntry = tarArchiveOutputStream.getArchiveEntry();
        assertSame(expectedArchiveEntry, actualArchiveEntry);
    }

    /**
     * Tests close method.
     *
     * @throws IOException on error
     */
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test.txt"));
        tarArchiveOutputStream.write("Hello world!".getBytes(StandardCharsets.UTF_8), 0, "Hello world!".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        byte[] output = outputStream.toByteArray();
        byte[] expectedOutput = new byte[1024 + 512 + 512];
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC, 0, expectedHeader, 0, TarConstants.MAGIC.length);
        TarArchiveEntry expectedArchiveEntry = new TarArchiveEntry("test.txt", "test.txt", 13, 0, null, new File("."));
        expectedArchiveEntry.writeHeader(expectedHeader, 0);
        System.arraycopy("test.txt\0".getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 512 - TarConstants.NAMELEN, TarConstants.NAMELEN);
        InputStream expectedInputStream = new ByteArrayInputStream("Hello world!".getBytes(StandardCharsets.UTF_8));
        byte[] recordBuf = new byte[TarArchiveOutputStream.DEFAULT_RECORD_SIZE];
        int numRead;
        while ((numRead = expectedInputStream.read(recordBuf)) > 0) {
            tarArchiveOutputStream.write(recordBuf, 0, numRead);
        }
        expectedInputStream.close();
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(expectedHeader, 0, expectedOutput, 512, expectedHeader.length);
        System.arraycopy(output, 0, expectedOutput, 1024, output.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(1, tarArchiveOutputStream.getNumberOfEntries());
        assertNull(tarArchiveOutputStream.getArchiveEntry());
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Tests getRecordSize method.
     */
    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    /**
     * Tests putArchiveEntry method with a regular file.
     *
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryWithRegularFile() throws IOException {
        File testFile = new File("test.txt");
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test.txt"), testFile);
        byte[] recordBuf = new byte[TarArchiveOutputStream.DEFAULT_RECORD_SIZE];
        String text = "Hello world!";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] output = outputStream.toByteArray();
        byte[] expectedOutput = new byte[1024 + 512 + 512 + data.length];
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC, 0, expectedHeader, 0, TarConstants.MAGIC.length);
        TarArchiveEntry expectedArchiveEntry = new TarArchiveEntry("test.txt", "test.txt", data.length, 0, null, testFile);
        expectedArchiveEntry.writeHeader(expectedHeader, 0);
        System.arraycopy("test.txt\0".getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 512 - TarConstants.NAMELEN, TarConstants.NAMELEN);
        InputStream expectedInputStream = new ByteArrayInputStream(data);
        int numRead;
        while ((numRead = expectedInputStream.read(recordBuf)) > 0) {
            tarArchiveOutputStream.write(recordBuf, 0, numRead);
        }
        expectedInputStream.close();
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(expectedHeader, 0, expectedOutput, 512, expectedHeader.length);
        System.arraycopy(data, 0, expectedOutput, 1024 + 512, data.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(1, tarArchiveOutputStream.getNumberOfEntries());
        TarArchiveEntry actualArchiveEntry = tarArchiveOutputStream.getArchiveEntry();
        assertSame(expectedArchiveEntry, actualArchiveEntry);
    }

    /**
     * Tests putArchiveEntry method with a directory.
     *
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File testDir = new File("test");
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test", "test"), testDir);
    }

}