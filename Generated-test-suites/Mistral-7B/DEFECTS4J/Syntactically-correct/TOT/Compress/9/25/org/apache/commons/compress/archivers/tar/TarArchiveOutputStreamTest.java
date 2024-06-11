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
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {
    private File tempFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-test-", null);
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
            tempFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final String fileName = "test_file.txt";
        final String entryName = "test_directory/test_file.txt";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(new File("src/test/resources/test_file.txt")), entryName);
        final byte[] sourceFileContent = IOUtils.toByteArray(new File("src/test/resources/test_file.txt"));

        tarArchiveOutputStream.write(sourceFileContent, 0, sourceFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        final String testDirectoryEntryName = "test_directory/";
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(new File("src/test/resources/test_directory")), testDirectoryEntryName);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(new File("src/test/resources/test_file.txt")), entryName);
        tarArchiveOutputStream.write(sourceFileContent, 0, sourceFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(tempFile.getAbsolutePath()));
        assertNotNull(archiveEntry);
        assertEquals(entryName, archiveEntry.getName());

        tarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        tarArchiveOutputStream.extractEntry(archiveEntry);
        final File extractedFile = archiveEntry.isDirectory()
                ? new File(System.getProperty("java.io.tmpdir") + File.separator + entryName)
                : new File(System.getProperty("java.io.tmpdir") + File.separator + entryName);

        byte[] expectedContent = new byte[sourceFileContent.length];
        IOUtils.copy(new File("src/test/resources/test_file.txt"), new File(expectedContent));
        assertEquals(new String(expectedContent, StandardCharsets.UTF_8), new String(IOUtils.toByteArray(new File(extractedFile.getAbsolutePath())), StandardCharsets.UTF_8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidFile() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new File("non_existent_file"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "long_" + new String(new char[TarConstants.NAMELEN + 1]) + "_name.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/" + longFileName), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }
}