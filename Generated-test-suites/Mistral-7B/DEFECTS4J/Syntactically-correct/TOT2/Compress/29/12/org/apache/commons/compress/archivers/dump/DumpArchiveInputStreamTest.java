import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveUtil.writeHeader(header, 1, 1, 1, 1, ZipEncoding.UTF_8.name());

        assertTrue(DumpArchiveInputStream.matches(header, header.length));
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveUtil.writeHeader(header, 1, 1, 1, 1, null);

        new DumpArchiveInputStream(new ByteArrayInputStream(header));
    }

    @Test(expected = IOException.class)
    public void testNoHeader() throws IOException {
        new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEncoding() throws IOException {
        new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]), null);
    }

    @Test
    public void testGetSummary() throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveUtil.writeHeader(header, 1, 1, 1, 1, ZipEncoding.UTF_8.name());

        DumpArchiveInputStream in = new DumpArchiveInputStream(new ByteArrayInputStream(header));
        DumpArchiveSummary summary = in.getSummary();

        assertEquals(1, summary.getMagicNumber());
        assertEquals(1, summary.getVolumeLabelLength());
        assertEquals(1, summary.getVolumeVersion());
        assertEquals("UTF-8", summary.getEncoding());

        in.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DumpArchiveUtil.writeHeader(out.toByteArray(), 1, 1, 1, 1, ZipEncoding.UTF_8.name());
        DumpArchiveUtil.writeCLRI(out, 1, DumpArchiveConstants.TP_SIZE, 1, 1);
        DumpArchiveUtil.writeNAME(out, "test.txt", 1, 1, 1, 7);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(in);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        assertEquals(0, entry.getEntrySize());
        assertFalse(entry.isDirectory());

        inputStream.close();
    }

    @Test
    public void testGetNextDirectoryEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DumpArchiveUtil.writeHeader(out.toByteArray(), 1, 1, 1, 1, ZipEncoding.UTF_8.name());
        DumpArchiveUtil.writeCLRI(out, 1, DumpArchiveConstants.TP_SIZE, 1, 1);
        DumpArchiveUtil.writeDIR(out, "testdir/", 1, 1, 2, 4, 0x04, 0);
        DumpArchiveUtil.writeDIR(out, "testfile.txt", 1, 1, 1, 7, 0x020, 2);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(in);

        DumpArchiveEntry directory = inputStream.getNextEntry();
        assertEquals("testdir/", directory.getName());
        assertEquals("testdir", directory.getSimpleName());
        assertEquals(0, directory.getOffset());
        assertTrue(directory.isDirectory());

        inputStream.close();

        DumpArchiveEntry file = inputStream.getNextEntry();
        assertEquals("testfile.txt", file.getName());
        assertEquals("testfile.txt", file.getSimpleName());
        assertEquals(0, file.getOffset());
        assertFalse(file.isDirectory());
    }

    @Test
    public void testReadFile() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DumpArchiveUtil.writeHeader(out.toByteArray(), 1, 1, 1, 1, ZipEncoding.UTF_8.name());
        DumpArchiveUtil.writeCLRI(out, 1, DumpArchiveConstants.TP_SIZE, 1, 1);
        DumpArchiveUtil.writeNAME(out, "test.txt", 1, 1, 1, 7);
        DumpArchiveUtil.writeFILE(out, "Hello World!".getBytes());

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(in);

        byte[] buffer = new byte[13];
        int len = inputStream.read(buffer);
        assertEquals(13, len);
        assertEquals("Hello World!".getBytes(), Arrays.copyOfRange(buffer, 0, len));

        inputStream.close();
    }

    @Test
    public void testGetNextEntryAfterEOF() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DumpArchiveUtil.writeHeader(out.toByteArray(), 1, 1, 1, 1, ZipEncoding.UTF_8.name());
        DumpArchiveUtil.writeCLRI(out, 1, DumpArchiveConstants.TP_SIZE, 1, 1);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(in);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNull(entry);

        inputStream.close();
    }

    @Test
    public void testReadAfterEOF() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DumpArchiveUtil.writeHeader(out.toByteArray(), 1, 1, 1, 1, ZipEncoding.UTF_8.name());
        DumpArchiveUtil.writeCLRI(out, 1, DumpArchiveConstants.TP_SIZE, 1, 1);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(in);

        byte[] buffer = new byte[1];
        int len = inputStream.read(buffer);
        assertEquals(-1, len);

        inputStream.close();
    }
}