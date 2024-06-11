import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    @Test
    public void testEmptyArchive() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("empty.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
        inputStream.close();
    }

    @Test
    public void testSimpleEntry() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("test.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        Assert.assertEquals("test.txt", inputStream.getNextEntry().getName());

        byte[] buffer = new byte[1024];
        int read = inputStream.read(buffer, 0, buffer.length);
        Assert.assertTrue(read > 0);

        inputStream.close();
    }

    @Test
    public void testDirectoryEntry() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("test.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry directoryEntry = inputStream.getNextEntry();
        Assert.assertEquals("dir", directoryEntry.getName());
        Assert.assertTrue(directoryEntry.isDirectory());

        while (inputStream.getNextEntry() != null) {
            // do nothing
        }

        inputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidMagic() throws IOException {
        byte[] magicBytes = new byte[32];
        Arrays.fill(magicBytes, (byte) 0x01);

        InputStream is = new FileInputStream(new File("test.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);
        inputStream.read(magicBytes, 0, magicBytes.length);
        inputStream.close();

        DumpArchiveInputStream invalidMagicInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(magicBytes));
        invalidMagicInputStream.getNextEntry();
    }

    @Test(expected = EOFException.class)
    public void testEndOfArchive() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("test.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry nextEntry = inputStream.getNextEntry();
        while (nextEntry != null) {
            inputStream.getNextEntry();
            nextEntry = inputStream.getNextEntry();
        }

        inputStream.getNextEntry();
    }

    @Test
    public void testCompressedEntry() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("test.gz.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);
        inputStream.setRawStream(new GzipCompressorInputStream(is));

        DumpArchiveEntry nextEntry = inputStream.getNextEntry();
        Assert.assertEquals("compressed.txt", nextEntry.getName());

        byte[] buffer = new byte[1024];
        int read = inputStream.read(buffer, 0, buffer.length);
        Assert.assertTrue(read > 0);

        inputStream.close();
    }

    @Test
    public void testNonExistentEntry() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("test.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry nextEntry = inputStream.getNextEntry();
        String name = "nonexistent.txt";
        while (nextEntry != null && !nextEntry.getName().equals(name)) {
            nextEntry = inputStream.getNextEntry();
        }
        Assert.assertNull(nextEntry);

        inputStream.close();
    }

    @Test
    public void testIncompleteArchive() throws IOException, ArchiveException {
        InputStream is = new FileInputStream(new File("incomplete.dump"));
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        DumpArchiveEntry nextEntry = inputStream.getNextEntry();
        Assert.assertEquals("incomplete.txt", nextEntry.getName());

        inputStream.close();
    }
}