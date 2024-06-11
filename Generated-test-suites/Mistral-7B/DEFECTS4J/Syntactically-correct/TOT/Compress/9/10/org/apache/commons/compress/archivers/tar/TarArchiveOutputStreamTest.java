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
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testWriteEmptyArchive() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expected = new byte[2 * TarConstants.BLOCKSIZE];
        byte[] actual = outputStream.toByteArray();

        assertEquals("Empty archive should be 2 blocks of zeros", expected, actual);
    }

    @Test
    public void testWriteSingleFileEntry() throws IOException {
        final String fileName = "testFile.txt";
        final String fileContent = "This is a test file.";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileBytes, 0, fileBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[fileBytes.length];
        inputStream.read(readContent);

        assertEquals("File contents should be identical", new String(fileBytes, StandardCharsets.UTF_8), new String(readContent, StandardCharsets.UTF_8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteInvalidFileEntry() throws IOException {
        final String fileName = "testFile.txt";
        final TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        throw new RuntimeException("Should throw an IOException");
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileName() throws IOException {
        final String fileName = "a".repeat(TarConstants.NAMELEN + 1);
        final TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(ArchiveUtils.toAsciiBytes(fileName), 0, fileName.length());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertNull("Long file name should be null", readEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithoutGNU() throws IOException {
        final String fileName = "a".repeat(TarConstants.NAMELEN + 1);
        final TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        throw new RuntimeException("Should throw an IOException");
    }
}