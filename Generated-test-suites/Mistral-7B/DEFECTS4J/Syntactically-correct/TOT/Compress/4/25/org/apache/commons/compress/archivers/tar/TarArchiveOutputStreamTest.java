import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream os;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-test", ".tar");
        os = new FileOutputStream(tempFile);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws Exception {
        if (os != null) {
            os.close();
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        // Create a test entry
        File fileToTest = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry("testFolder/testFile.txt", fileToTest);

        // Write the entry to the archive
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write some data to the entry
        byte[] data = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Read the data back from the archive
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("testFolder/testFile.txt", archiveEntry.getName());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(tarArchiveOutputStream, byteArrayOutputStream, data.length);
        Assert.assertArrayEquals(data, byteArrayOutputStream.toByteArray());
        IOUtils.closeQuietly(tarArchiveOutputStream);
        IOUtils.closeQuietly(archiveEntry.getInputStream());
        IOUtils.closeQuietly(byteArrayOutputStream.toByteArrayInputstream());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        // Create a test entry
        File fileToTest = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry("testFolder/testFile.txt", fileToTest);

        // Write the entry to the archive
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Do not close the entry
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(512));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(512));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(512));
        tarArchiveOutputStream.close();
    }
}