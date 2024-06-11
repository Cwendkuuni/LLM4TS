import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.*;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "testData.dump";

    @Test
    public void testDumpArchiveInputStream() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertEquals(1, summary.getArchiveVolumeSize());
        Assert.assertEquals(1, summary.getEntriesInArchive());

        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry", entry.getName());
        Assert.assertEquals("testEntry", entry.getSimpleName());
        Assert.assertEquals("testEntry/", entry.getDirectoryName());
        Assert.assertEquals(10, entry.getSize());
        Assert.assertEquals(1, entry.getInode());
        Assert.assertEquals(1, entry.getParentInode());

        byte[] buffer = new byte[10];
        int length = dumpArchiveInputStream.read(buffer);
        Assert.assertEquals(10, length);
        Assert.assertArrayEquals(new byte[]{"t", "e", "s", "t", "E", "n", "t", "r", "y", "."}, buffer);

        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testEOFException() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        dumpArchiveInputStream.getNextEntry();
    }

    @Test
    public void testCloseStream() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.close();

        dumpArchiveInputStream.getNextEntry();
    }

    @Test
    public void testGetPath() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        String path = dumpArchiveInputStream.getPath(entry);
        Assert.assertEquals("/testEntry", path);

        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadDirectoryEntry() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.readDirectoryEntry(entry);

        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");
        dumpArchiveInputStream.getNextEntry();
    }

    @Test
    public void testUnrecognizedFormatException() throws IOException {
        InputStream inputStream = getTestDataInputStream();
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");
        dumpArchiveInputStream.readCLRI();
        dumpArchiveInputStream.readBITS();
        dumpArchiveInputStream.close();
        inputStream.close();
    }

    private InputStream getTestDataInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream);

        DumpArchiveEntry entry = new DumpArchiveEntry("testEntry", "testEntry/", 1, 1, "UTF-8", (byte) 0);
        dumpArchiveOutputStream.putNextEntry(entry);
        dumpArchiveOutputStream.write(new byte[]{'t', 'e', 's', 't', 'E', 'n', 't', 'r', 'y', '.'}, 0, 10);
        dumpArchiveOutputStream.closeEntry();
        dumpArchiveOutputStream.finish();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}

class DumpArchiveOutputStream {
    // Placeholder for writing DumpArchiveOutputStream.
}