import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);

        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertNotNull(tarOutputStream2);

        TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertNotNull(tarOutputStream3);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        byte[] bytes = outputStream.toByteArray();
        int eofIndex = bytes.length - 2 * 512;
        byte[] eofRecord = new byte[512];
        System.arraycopy(bytes, eofIndex, eofRecord, 0, 512);
        assertArrayEquals(new byte[512], eofRecord);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        int headerIndex = bytes.length - entry.getSize() - 1 - 512;
        byte[] headerRecord = new byte[512];
        System.arraycopy(bytes, headerIndex, headerRecord, 0, 512);
        TarHeader header = new TarHeader(headerRecord);
        assertEquals("test.txt", header.getName());
        assertEquals(entry.getSize(), header.getSize());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerIndex = bytes.length - entry.getSize() - 1 - 512;
        byte[] headerRecord = new byte[512];
        System.arraycopy(bytes, headerIndex, headerRecord, 0, 512);
        TarHeader header = new TarHeader(headerRecord);
        assertEquals("test.txt", header.getName());
        assertEquals(13, header.getSize());

        byte[] contentIndex = bytes.length - 1 - 512;
        byte[] contentRecord = new byte[13];
        System.arraycopy(bytes, contentIndex, contentRecord, 0, 13);
        String content = new String(contentRecord, 0, 13);
        assertEquals("test content", content);
    }

    @Test
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("test content".getBytes());
        tarOutputStream.finish(); // this makes the test fail
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        byte[] bytes = outputStream.toByteArray();
        TarHeader header = new TarHeader(bytes, 0, bytes.length);
        assertEquals("test.txt", header.getName());
        assertEquals(13, header.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("test content".getBytes());
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
        tarOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int contentIndex = bytes.length - 1 - 512;
        byte[] contentRecord = new byte[13];
        System.arraycopy(bytes, contentIndex, contentRecord, 0, 13);
        String content = new String(contentRecord, 0, 13);
        assertEquals("test content", content);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] content = "test content".getBytes();
        tarOutputStream.write(content, 2, 3);
        tarOutputStream.write(content, 5, 4);
        tarOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerIndex = bytes.length - entry.getSize() - 1 - 512;
        byte[] headerRecord = new byte[512];
        System.arraycopy(bytes, headerIndex, headerRecord, 0, 512);
        TarHeader header = new TarHeader(headerRecord);
        assertEquals("test.txt", header.getName());
        assertEquals(13 + 3 + 4, header.getSize());

        int contentIndex = bytes.length - 1 - 512;
        byte[] contentRecord = new byte[13 + 3 + 4];
        System.arraycopy(bytes, contentIndex, contentRecord, 0, 13 + 3 + 4);
        String content = new String(contentRecord, 0, 13 + 3 + 4);
        assertEquals("test contenttest content", content);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = Files.createTempFile("test", "txt").toFile();
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        Files.write(Paths.get(inputFile.getAbsolutePath()), "test content".getBytes());
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        byte[] bytes = outputStream.toByteArray();
        int headerIndex = bytes.length - entry.getSize() - 1 - 512;
        byte[] headerRecord = new byte[512];
        System.arraycopy(bytes, headerIndex, headerRecord, 0, 512);
        TarHeader header = new TarHeader(headerRecord);
        assertEquals("test.txt", header.getName());
        assertEquals(inputFile.length(), header.getSize());

        Files.delete(Paths.get(inputFile.getAbsolutePath()));
    }
}