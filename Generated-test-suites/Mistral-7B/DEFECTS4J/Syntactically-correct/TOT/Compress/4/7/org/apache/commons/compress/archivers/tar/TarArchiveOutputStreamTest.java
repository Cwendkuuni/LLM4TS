import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit", null);
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        tempFile.delete();
    }

    @Test
    public void testWriteSingleFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");
        tarOutputStream.putNextEntry(entry);
        byte[] data = "This is a test file".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        File outputFile = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertFalse(outputFile.exists());
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        TarOutputStream testTarOutputStream = new TarOutputStream(expectedOutput);
        testTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        testTarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        testTarOutputStream.write(data, 0, data.length);
        testTarOutputStream.closeArchiveEntry();
        testTarOutputStream.finish();
        testTarOutputStream.close();
        ByteArrayInputStream actualOutput = new ByteArrayInputStream(tempFile.length() > 0 ? new File(tempFile.getAbsolutePath()).getBytes() : new byte[0]);
        assertEquals("Test data doesn't match.", expectedOutput.toString(), actualOutput.toString());
        assertNotNull("Output file doesn't exist.", outputFile.exists());
        assertFalse("Output file should not be a directory.", outputFile.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("longFileNameThatIsTooLongForTar.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testWriteLongFileNameWithLongFileMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("longFileNameThatIsTooLongForTar.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putNextEntry(entry);
        byte[] data = "This is a test file".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        File outputFile = new File(tempFile.getAbsolutePath() + "/longFileNameThatIsTooLongForTar.txt");
        assertFalse(outputFile.exists());
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        newTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry newEntry = new TarArchiveEntry("longFileNameThatIsTooLongForTar.txt");
        newEntry.setSize(10);
        newEntry.setName("longFileNameThatIsTooLongForTar.txt");
        newTarOutputStream.putNextEntry(newEntry);
        newTarOutputStream.write(data, 0, data.length);
        newTarOutputStream.closeArchiveEntry();
        newTarOutputStream.finish();
        newTarOutputStream.close();
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        TarOutputStream testTarOutputStream = new TarOutputStream(expectedOutput);
        testTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        testTarOutputStream.putNextEntry(new TarArchiveEntry("longFileNameThatIsTooLongForTar.txt"));
        testTarOutputStream.write(data, 0, data.length);
        testTarOutputStream.closeArchiveEntry();
        testTarOutputStream.finish();
        testTarOutputStream.close();
        ByteArrayInputStream actualOutput = new ByteArrayInputStream(tempFile.length() > 0 ? new File(tempFile.getAbsolutePath()).getBytes() : new byte[0]);
        assertEquals("Test data doesn't match.", expectedOutput.toString(), actualOutput.toString());
        assertNotNull("Output file doesn't exist.", outputFile.exists());
        assertFalse("Output file should not be a directory.", outputFile.isDirectory());
    }

    @Test
    public void testWriteTwoFiles() throws IOException {
        TarArchiveEntry firstEntry = new TarArchiveEntry("test1.txt");
        firstEntry.setSize(10);
        firstEntry.setName("test1.txt");
        tarOutputStream.putNextEntry(firstEntry);
        byte[] firstData = "This is the first test file".getBytes();
        tarOutputStream.write(firstData, 0, firstData.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry secondEntry = new TarArchiveEntry("test2.txt");
        secondEntry.setSize(10);
        secondEntry.setName("test2.txt");
        tarOutputStream.putNextEntry(secondEntry);
        byte[] secondData = "This is the second test file".getBytes();
        tarOutputStream.write(secondData, 0, secondData.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        TarOutputStream testTarOutputStream = new TarOutputStream(expectedOutput);

        testTarOutputStream.putNextEntry(new TarArchiveEntry("test1.txt"));
        testTarOutputStream.write(firstData, 0, firstData.length);
        testTarOutputStream.closeArchiveEntry();

        testTarOutputStream.putNextEntry(new TarArchiveEntry("test2.txt"));
        testTarOutputStream.write(secondData, 0, secondData.length);
        testTarOutputStream.closeArchiveEntry();

        testTarOutputStream.finish();
        testTarOutputStream.close();

        ByteArrayInputStream actualOutput = new ByteArrayInputStream(tempFile.length() > 0 ? new File(tempFile.getAbsolutePath()).getBytes() : new byte[0]);
        assertEquals("Test data doesn't match.", expectedOutput.toString(), actualOutput.toString());
        assertNotNull("Output file doesn't exist.", new File(tempFile.getAbsolutePath()).exists());
        assertFalse("Output file should not be a directory.", new File(tempFile.getAbsolutePath()).isDirectory());
    }
}