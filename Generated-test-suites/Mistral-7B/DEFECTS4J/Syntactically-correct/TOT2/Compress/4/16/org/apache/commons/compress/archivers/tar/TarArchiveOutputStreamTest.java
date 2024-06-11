import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * TarArchiveOutputStreamTest.
 */
public class TarArchiveOutputStreamTest {
    private OutputStream out;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(out);
    }

    /**
     * Test putArchiveEntry with valid parameters.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryValid() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = new byte[TarConstants.HEADER_SIZE];
        archiveEntry.writeEntryHeader(expectedHeader);
        tarArchiveOutputStream.write(expectedHeader);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry with long file name.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "longFileNameThatIsLongerThanNameLen";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, "test content");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedLongFileNameHeader = new byte[TarConstants.LONG_FILE_NAME_SIZE + TarConstants.HEADER_SIZE];
        System.arraycopy(archiveEntry.getLongFileNameHeader(), 0, expectedLongFileNameHeader, 0, TarConstants.LONG_FILE_NAME_SIZE);
        archiveEntry.writeEntryHeader(new byte[TarConstants.HEADER_SIZE]);
        tarArchiveOutputStream.write(expectedLongFileNameHeader);
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.write(archiveEntry.getName().getBytes()); // TODO is it correct to use the default charset here?
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry with too long file name.
     * @throws IOException on error
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryTooLongFileName() throws IOException {
        String tooLongFileName = "testFileNameThatIsLongerThanNameLen";
        ArchiveEntry archiveEntry = new TarArchiveEntry(tooLongFileName, "test content");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test finish method.
     * @throws IOException on error
     */
    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[2 * TarConstants.HEADER_SIZE];
        tarArchiveOutputStream.writeEOFRecord();
        byte[] output = out.toByteArray();
        assertArrayEquals(expectedEOFRecord, output, 0, 2 * TarConstants.HEADER_SIZE);
    }

    /**
     * Test close method.
     * @throws IOException on error
     */
    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.close();
        byte[] output = out.toByteArray();
        ArchiveEntry inputArchiveEntry = new TarArchiveEntry(new ByteArrayInputStream(output), "test.txt");
        assertEquals(archiveEntry.getName(), inputArchiveEntry.getName());
        assertEquals(archiveEntry.getSize(), inputArchiveEntry.getSize());
    }
}