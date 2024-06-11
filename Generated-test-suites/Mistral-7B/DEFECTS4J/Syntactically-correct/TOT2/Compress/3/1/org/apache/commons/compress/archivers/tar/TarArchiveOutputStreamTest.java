import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        String entryName = "test.txt";
        File entryFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryFile, entryName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = Arrays.copyOfRange(archiveEntry.getHeader(), 0, TarConstants.STND_REC_HDR_LEN);
        tarOutputStream.write(entryFile.getName().getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(entryFile.length());
        tarOutputStream.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] actualHeader = Arrays.copyOfRange(actualBytes, 0, TarConstants.STND_REC_HDR_LEN);

        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String entryName = "longFileNameThatIsLongerThanTarNameLengthLimit.txt";
        File entryFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryFile, entryName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedLongLinkName = entryName.getBytes(StandardCharsets.UTF_8);
        byte[] expectedEntryHeader = Arrays.copyOfRange(archiveEntry.getHeader(), 0, TarConstants.STND_REC_HDR_LEN);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] actualLongLinkName = Arrays.copyOfRange(actualBytes, 0, expectedLongLinkName.length + 1);
        byte[] actualEntryHeader = Arrays.copyOfRange(actualBytes, TarConstants.STND_REC_HDR_LEN, TarConstants.STND_REC_HDR_LEN + TarConstants.GNU_LONGLINK_HDR_LEN);

        assertEquals(expectedLongLinkName, actualLongLinkName);
        assertEquals(expectedEntryHeader, actualEntryHeader);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndTruncate() throws IOException {
        String entryName = "longFileNameThatIsLongerThanTarNameLengthLimit.txt";
        File entryFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryFile, entryName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedEntryHeader = Arrays.copyOfRange(archiveEntry.getHeader(), 0, TarConstants.STND_REC_HDR_LEN);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] actualEntryHeader = Arrays.copyOfRange(actualBytes, 0, TarConstants.STND_REC_HDR_LEN);

        assertEquals(expectedEntryHeader, actualEntryHeader);
        assertFalse(Arrays.equals(entryName.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(actualBytes, TarConstants.STND_REC_HDR_LEN, actualBytes.length)));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndError() throws IOException {
        String entryName = "longFileNameThatIsLongerThanTarNameLengthLimit.txt";
        File entryFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryFile, entryName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        assertThrows(IOException.class, () -> tarOutputStream.write(entryName.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "test.txt";
        File entryFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(entryFile, entryName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = Arrays.copyOfRange(archiveEntry.getHeader(), 0, TarConstants.STND_REC_HDR_LEN);
        tarOutputStream.write(entryFile.getName().getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(entryFile.length());
        tarOutputStream.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] actualHeader = Arrays.copyOfRange(actualBytes, 0, TarConstants.STND_REC_HDR_LEN);

        assertEquals(expectedHeader, actualHeader);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }
}