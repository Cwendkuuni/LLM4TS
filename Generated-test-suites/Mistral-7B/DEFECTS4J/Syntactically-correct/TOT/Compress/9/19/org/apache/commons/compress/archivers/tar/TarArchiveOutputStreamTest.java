import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File tempDir;
    private OutputStream out;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempDir = File.createTempDirectory("TarArchiveOutputStreamTest", null);
        out = new FileOutputStream(new File(tempDir, "test.tar"));
        tarOutputStream = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        out.close();
        tempDir.delete();
    }

    @Test
    public void testPutArchiveEntryWithValidFileEntry() throws IOException {
        File inputFile = new File("src/test/resources/sample.txt");
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, "sample.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedHeader = Arrays.copyOfRange(entry.getHeader(), 0, 512);
        tarOutputStream.write(expectedHeader);
        byte[] fileContent = new byte[1024];
        File file = new File("src/test/resources/sample.txt");
        FileInputStream inputStream = new FileInputStream(file);
        int bytesRead;
        while ((bytesRead = inputStream.read(fileContent)) != -1) {
            tarOutputStream.write(fileContent, 0, bytesRead);
        }
        tarOutputStream.closeArchiveEntry();
        inputStream.close();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] actual = bos.toByteArray();
        tarOutputStream.finish();
        assertFalse(Arrays.equals(expectedHeader, actual));
        assertEquals(expectedHeader.length + fileContent.length, actual.length);
        File archiveFile = new File(tempDir, "test.tar");
        byte[] archiveContent = Files.readAllBytes(archiveFile.toPath());
        assertTrue(Arrays.equals(actual, archiveContent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullFileEntry() throws IOException {
        TarArchiveEntry entry = null;
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFileName() throws IOException {
        String invalidFileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/sample.txt"), invalidFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN * 2]);
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/sample.txt"), longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = Arrays.copyOfRange(entry.getHeader(), 0, 512);
        tarOutputStream.write(expectedHeader);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(longFileNameBytes);
        tarOutputStream.write(0); // NUL terminator
        byte[] fileContent = new byte[1024];
        File file = new File("src/test/resources/sample.txt");
        FileInputStream inputStream = new FileInputStream(file);
        int bytesRead;
        while ((bytesRead = inputStream.read(fileContent)) != -1) {
            tarOutputStream.write(fileContent, 0, bytesRead);
        }
        tarOutputStream.closeArchiveEntry();
        inputStream.close();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] actual = bos.toByteArray();
        tarOutputStream.finish();
        assertFalse(Arrays.equals(expectedHeader, actual));
        assertEquals(expectedHeader.length + longFileNameBytes.length + fileContent.length, actual.length);
        File archiveFile = new File(tempDir, "test.tar");
        byte[] archiveContent = Files.readAllBytes(archiveFile.toPath());
        assertTrue(Arrays.equals(actual, archiveContent));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN * 2]);
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/sample.txt"), longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/sample.txt"), "sample.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }
}