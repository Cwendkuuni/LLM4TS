import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
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

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        String fileName = "testFile.txt";
        File file = new File("src/test/resources/" + fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}, 0, 10);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        expectedHeader[0] = TarConstants.MAGIC_TAR;
        expectedHeader[136] = TarConstants.FILETYPE_REGULAR;
        expectedHeader[144] = (byte) ((fileName.length() & 0xFF00) >> 8);
        expectedHeader[145] = (byte) (fileName.length() & 0xFF);
        expectedHeader[148] = (byte) ((file.length() & 0xFF000000) >> 24);
        expectedHeader[152] = (byte) ((file.length() & 0xFF0000) >> 16);
        expectedHeader[156] = (byte) ((file.length() & 0xFF00) >> 8);
        expectedHeader[158] = (byte) (file.length() & 0xFF);
        expectedHeader[254] = (byte) ((fileName.length() + 1) & 0xFF);
        expectedHeader[255] = 0;

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertEquals(expectedHeader, header);

        byte[] contents = new byte[10];
        assertTrue(tarOutputStream.outputStream.size() > 0);
        tarOutputStream.outputStream.read(contents, 0, 10);
        assertEquals(new String(contents), new String(new String(file.getText()).subSequence(0, 10)));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "testFileWithLongName.txt";
        File longFile = new File("src/test/resources/" + longFileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFile, longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.write(longFileName.getBytes(), 0, longFileName.length());
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        expectedHeader[0] = TarConstants.MAGIC_TAR;
        expectedHeader[136] = TarConstants.GNU_LONGLINK;

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertEquals(expectedHeader, header);

        byte[] nameBytes = longFileName.getBytes();
        assertEquals(nameBytes.length + 1, tarOutputStream.getBuffer().getBufferSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutGNUMode() throws IOException {
        String longFileName = "testFileWithLongName.txt";
        File longFile = new File("src/test/resources/" + longFileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFile, longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.write(longFileName.getBytes(), 0, longFileName.length());
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.closeArchiveEntry();

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertNull(header);
    }

    @Test
    public void testFinish() throws IOException {
        assertFalse(tarOutputStream.isFinished());
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertEquals(512 * 2, header.length);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(tarOutputStream.isClosed());
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertEquals(512 * 2, header.length);
    }

    @Test
    public void testIsDirectoryEntry() throws IOException {
        String fileName = "testDir/";
        File dir = new File("src/test/resources/" + fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(dir, fileName);
        archiveEntry.setDirectory(true);

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.finish();

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        byte[] expectedHeader = new byte[512];
        expectedHeader[136] = TarConstants.FILETYPE_DIR;

        assertEquals(expectedHeader, header);
    }

    @Test
    public void testWrite() throws IOException {
        String fileName = "testFile.txt";
        File file = new File("src/test/resources/" + fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}, 0, 10);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        expectedHeader[0] = TarConstants.MAGIC_TAR;
        expectedHeader[136] = TarConstants.FILETYPE_REGULAR;
        expectedHeader[144] = (byte) ((fileName.length() & 0xFF00) >> 8);
        expectedHeader[145] = (byte) (fileName.length() & 0xFF);
        expectedHeader[148] = (byte) ((file.length() & 0xFF000000) >> 24);
        expectedHeader[152] = (byte) ((file.length() & 0xFF0000) >> 16);
        expectedHeader[156] = (byte) ((file.length() & 0xFF00) >> 8);
        expectedHeader[158] = (byte) (file.length() & 0xFF);
        expectedHeader[254] = (byte) ((fileName.length() + 1) & 0xFF);
        expectedHeader[255] = 0;

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        assertEquals(expectedHeader, header);

        byte[] contents = new byte[10];
        assertTrue(tarOutputStream.outputStream.size() > 0);
        tarOutputStream.outputStream.read(contents, 0, 10);
        assertEquals(new String(contents), new String(new String(file.getText()).subSequence(0, 10)));
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        String longFileName = "testFileWithLongName.txt";
        File longFile = new File("src/test/resources/" + longFileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFile, longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.write(longFileName.getBytes(), 0, longFileName.length());
        tarOutputStream.write(new byte[0], 0, 1);
        tarOutputStream.closeArchiveEntry();

        byte[] header = tarOutputStream.getBuffer().getBuffer();
        byte[] expectedHeader = new byte[512];
        expectedHeader[0] = TarConstants.MAGIC_TAR;
        expectedHeader[136] = TarConstants.GNU_LONGLINK;

        assertEquals(expectedHeader, header);

        byte[] nameBytes = longFileName.getBytes();
        assertEquals(nameBytes.length + 1, tarOutputStream.getBuffer().getBufferSize());
    }
}