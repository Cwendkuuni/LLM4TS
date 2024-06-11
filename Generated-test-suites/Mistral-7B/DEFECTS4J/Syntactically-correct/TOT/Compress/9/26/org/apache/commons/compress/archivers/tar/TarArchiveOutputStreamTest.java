import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarArchiveOutputStream tarOut;
    private ArchiveEntry entry;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("test", null);
        OutputStream out = new FileOutputStream(outputFile);
        tarOut = new TarArchiveOutputStream(out);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        if (tarOut != null) {
            tarOut.close();
        }
        if (outputFile != null && outputFile.exists()) {
            Files.delete(Paths.get(outputFile.getAbsolutePath()));
        }
    }

    @Test
    public void testCloseFinishedStream() throws IOException {
        tarOut.finish();
        tarOut.close();
        assertFalse(tarOut.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseUnfinishedStream() throws IOException {
        tarOut.close();
        tarOut.write(0);
    }

    @Test
    public void testFinishUnfinishedStream() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'T', 'e', 's', 't'});
        tarOut.close();
        tarOut.finish();
        byte[] bytes = IOUtils.toByteArray(Files.newInputStream(Paths.get(outputFile.getAbsolutePath())));
        int pos = 0;

        // check for expected structure:
        // 0000: 50 54 41 00 00 00 00 00 magic number
        // 0008: 20 00 00 00 version
        // 0012: 00 02 flags
        // 0014: 54 61 72 79 name length
        // 0018: 65 78 65 2e 74 65 78 74 name
        assertEquals(0x505441, bytes[pos++] << 8 | bytes[pos++]);
        assertEquals(0x20, bytes[pos++]);
        assertEquals(0x02, bytes[pos++]);
        assertEquals(0x54, bytes[pos++]);
        assertEquals(0x61, bytes[pos++]);
        assertEquals(0x72, bytes[pos++]);
        assertEquals(0x79, bytes[pos++]);
        assertEquals(0x65, bytes[pos++]);
        assertEquals(0x78, bytes[pos++]);
        assertEquals(0x2e, bytes[pos++]);
        assertEquals(0x74, bytes[pos++]);
        assertEquals(0x65, bytes[pos++]);
        assertEquals(0x78, bytes[pos++]);
        assertEquals(0x74, bytes[pos++]);
        assertEquals(0x00, bytes[pos++]);
        // name length plus 512 bytes follows
        // bytes[pos..] should be all zeroes
        for (int i = 0; i < 511; i++) {
            assertEquals(0, bytes[pos++]);
        }

        // another header should not be present
        assertNull(new TarArchiveEntry(new FileInputStream(outputFile)));

        // verify that written data is present
        byte[] expectedData = new byte[]{'T', 'e', 's', 't'};
        byte[] actualData = new byte[3];
        tarOut = new TarArchiveOutputStream(new FileOutputStream(outputFile));
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(expectedData);
        tarOut.close();
        IOUtils.toByteArray(Files.newInputStream(Paths.get(outputFile.getAbsolutePath())), actualData, 0, 3);
        assertEquals(expectedData[0], actualData[0]);
        assertEquals(expectedData[1], actualData[1]);
        assertEquals(expectedData[2], actualData[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBlockSize() throws IOException {
        new TarArchiveOutputStream(new FileOutputStream(outputFile), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRecordSize() throws IOException {
        new TarArchiveOutputStream(new FileOutputStream(outputFile), 1, -1);
    }

    @Test
    public void testPositiveBlockAndRecordSize() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(outputFile), 1024, 512);
        assertNotNull(tarOut);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        assertEquals(1, tarOut.getNextEntry());
        assertNotNull(entry.getName());
        assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'T', 'e', 's', 't'});
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "test_" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut.putArchiveEntry(entry);
        assertFalse(entry.isDirectory());
        assertEquals(longFileName.length(), entry.getName().length());
        assertEquals(longFileName, entry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = "test_" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarArchiveOutputStream.LONGFILE_ERROR);
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeTruncate() throws IOException {
        String longFileName = "test_" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeGnu() throws IOException {
        String longFileName = "test_" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOut = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDir");
        entry.setDirectory(true);
        tarOut.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'T', 'e', 's', 't'});
        byte[] expectedData = new byte[]{'T', 'e', 's', 't'};
        byte[] actualData = new byte[3];
        tarOut.close();
        IOUtils.toByteArray(Files.newInputStream(Paths.get(outputFile.getAbsolutePath())), actualData, 0, 3);
        assertEquals(expectedData[0], actualData[0]);
        assertEquals(expectedData[1], actualData[1]);
        assertEquals(expectedData[2], actualData[2]);

        // Test writing to a directory
        TarArchiveEntry dirEntry = new TarArchiveEntry("testDir");
        tarOut.putArchiveEntry(dirEntry);
        tarOut.write("testDir".getBytes());
        tarOut.close();
        IOUtils.toByteArray(Files.newInputStream(Paths.get(outputFile.getAbsolutePath())), actualData, 0, 512);
        // name length + 512 bytes follows
        for (int i = 0; i < 511; i++) {
            assertEquals(0, actualData[pos++]);
        }
    }
}