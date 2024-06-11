import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.finish();
            outputStream.close();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        assertNull(tarOutputStream.getRecordSize());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(449, tarOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, outputStream.getLongFileMode());

        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, outputStream.getLongFileMode());

        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, outputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "test.txt";
        String fileContent = "Hello World!";

        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        outputStream.putArchiveEntry(archiveEntry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(), 0, fileContent.length());
        outputStream.closeArchiveEntry();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals(fileName, readEntry.getName());
        assertEquals(fileContent.length(), readEntry.getSize());

        String readContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertEquals(fileContent, readContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileNameThatIsLongerThanNameLEN.txt";

        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        outputStream.putArchiveEntry(archiveEntry);

        // This test relies on the default longFileMode which is LONGFILE_ERROR
        outputStream.write(longFileName.getBytes());
        outputStream.write(0);
        outputStream.closeArchiveEntry();

        // The test should throw an IOException because of the long file name
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", readEntry.getName());
        assertEquals(13, readEntry.getSize());

        assertTrue(outputStream.isFinished());
        assertFalse(outputStream.isWritable());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.finish();

        // This test should throw an IOException because finish() has already been called
        outputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", readEntry.getName());
        assertEquals(13, readEntry.getSize());

        assertFalse(outputStream.isFinished());
        assertFalse(outputStream.isWritable());
        assertFalse(outputStream.isOpen());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);

        byte[] writeBytes = "Hello World!".getBytes();
        outputStream.write(writeBytes);
        outputStream.closeArchiveEntry();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", readEntry.getName());
        assertEquals(13, readEntry.getSize());

        String readContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertEquals("Hello World!", readContent);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.close();

        // This test should throw an IOException because the stream is closed
        outputStream.write("Hello World!".getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteBeforePutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));

        // This test should throw an IOException because putArchiveEntry has not been called yet
        tarOutputStream.write("Hello World!".getBytes());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(archiveEntry);

        byte[] writeBytes = "Hello World!".getBytes();
        outputStream.write(writeBytes);
        outputStream.flush();
        outputStream.closeArchiveEntry();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", readEntry.getName());
        assertEquals(13, readEntry.getSize());

        String readContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertEquals("Hello World!", readContent);
    }
}