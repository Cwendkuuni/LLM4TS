import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
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
import static org.junit.Assert.assertNull;

/**
 * TarArchiveOutputStreamTest class to test all methods in TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        }
    }

    @Test
    public void testConstructor() throws IOException {
        assertNull(tarArchiveOutputStream.getInputStream());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBufferSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(123);
    }

    @Test
    public void testFinish() throws IOException {
        final String fileName = "testFile.txt";
        final byte[] fileContent = "Hello World!".getBytes();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream expectedTarStream = new ByteArrayInputStream(createExpectedTarFile(fileName, fileContent.length));
        TarArchiveEntry actualArchiveEntry = tarArchiveOutputStream.getInputStream().readArchiveEntry();

        assertEquals(archiveEntry.getName(), actualArchiveEntry.getName());
        assertEquals(archiveEntry.getSize(), actualArchiveEntry.getSize());
        assertEquals(fileContent.length, actualArchiveEntry.getSize());
        assertEquals(expectedTarStream.readAllBytes(), tarArchiveOutputStream.getInputStream().readAllBytes());
    }

    @Test(expected = IOException.class)
    public void testFinishMissingCloseArchiveEntry() throws IOException {
        final String fileName = "testFile.txt";
        final byte[] fileContent = "Hello World!".getBytes();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        final String fileName = "testFile.txt";
        final byte[] fileContent = "Hello World!".getBytes();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        assertFalse(tarArchiveOutputStream.isOpen());
        assertNull(tarArchiveOutputStream.getInputStream());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseMissingFinish() throws IOException {
        final String fileName = "testFile.txt";
        final byte[] fileContent = "Hello World!".getBytes();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidSize() throws IOException {
        final String fileName = "testFile.txt";
        final TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(-1, 0, 0);
    }

    @Test
    public void testWrite() throws IOException {
        final String fileName = "testFile.txt";
        final byte[] fileContent = "Hello World!".getBytes();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream expectedTarStream = new ByteArrayInputStream(createExpectedTarFile(fileName, fileContent.length));
        assertEquals(expectedTarStream.readAllBytes(), tarArchiveOutputStream.getInputStream().readAllBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteMissingPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.write(new byte[]{"Hello", "World!"}, 0, 7);
    }

    @Test(expected = IOException.class)
    public void testWriteMissingCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        final String fileName = "testFile.txt";
        final TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File(fileName), fileName);

        assertEquals(fileName, archiveEntry.getName());
        assertEquals(new File(fileName).length(), archiveEntry.getSize());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryInvalidFile() throws IOException {
        tarArchiveOutputStream.createArchiveEntry(null, "testFile.txt");
    }

    private byte[] createExpectedTarFile(String fileName, int fileSize) {
        ByteArrayOutputStream expectedTarOutputStream = new ByteArrayOutputStream();
        TarOutputStream tarOutputStream = new TarOutputStream(expectedTarOutputStream);

        TarArchiveEntry expectedArchiveEntry = new TarArchiveEntry(new File(fileName), fileName);
        expectedArchiveEntry.setSize(fileSize);
        expectedArchiveEntry.setMode(0644);
        expectedArchiveEntry.setModTime(System.currentTimeMillis());

        try {
            tarOutputStream.putNextEntry(expectedArchiveEntry);
            tarOutputStream.write(new byte[fileSize], 0, fileSize);
            tarOutputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                tarOutputStream.finish();
                tarOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return expectedTarOutputStream.toByteArray();
    }
}