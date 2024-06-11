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

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream os;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-", ".tar");
        os = new FileOutputStream(tempFile);
        tarOutputStream = new TarArchiveOutputStream(os);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        os.close();
        IOUtils.closeQuietly(tempFile.toPath().toFile().deleteOnExit());
    }

    // Test case for putArchiveEntry method with a valid TarArchiveEntry
    @Test
    public void testPutArchiveEntryWithValidTarArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testBytes = "Hello World!".getBytes();

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testBytes, 0, testBytes.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry retrievedEntry = tarOutputStream.getNextEntry();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = tarOutputStream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, length);
        }

        assertEquals("Expected 'Hello World!' but was '" + new String(baos.toByteArray()) + "'",
                "Hello World!", new String(baos.toByteArray()));
        retrievedEntry.close();
    }

    // Test case for putArchiveEntry method with large file names
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.close();

        File tarFile = new File(tempFile.getAbsolutePath());
        ByteArrayInputStream bais = new ByteArrayInputStream(longFileName.getBytes());
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new FileOutputStream(new File(tempFile.getAbsolutePath() + "/newfile.txt")));
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = bais.read(buffer)) != -1) {
            tarOutput.putNextEntry(new TarArchiveEntry(new File("newfile.txt")));
            tarOutput.write(buffer, 0, length);
            tarOutput.closeArchiveEntry();
        }

        tarOutput.close();

        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(tarFile));
        TarArchiveEntry retrievedEntry = tarInputStream.getNextEntry();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (retrievedEntry != null) {
            if (retrievedEntry.getName().equals(longFileName)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = tarInputStream.read(buffer, 0, buffer.length)) != -1) {
                    baos.write(buffer, 0, length);
                }

                assertEquals("Expected '" + longFileName + "' but was '"
                        + new String(baos.toByteArray()) + "'",
                        longFileName, new String(baos.toByteArray()));
                break;
            }
            retrievedEntry = tarInputStream.getNextEntry();
        }

        tarInputStream.close();
    }

    // Test case for putArchiveEntry method with invalid file names
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithInvalidFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.tar\0");
        tarOutputStream.putArchiveEntry(entry);
    }

    // Test case for closeArchiveEntry method with incomplete data
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testBytes = "Hello World!".getBytes();

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testBytes, 0, testBytes.length / 2);
        tarOutputStream.closeArchiveEntry();
    }

    // Test case for write method with incomplete data
    @Test(expected = IOException.class)
    public void testWriteWithIncompleteData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] testBytes = "Hello World!".getBytes();
        tarOutputStream.write(testBytes, 0, testBytes.length - 1);
    }

    // Test case for write method with valid data
    @Test
    public void testWriteWithValidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] testBytes = "Hello World!".getBytes();
        tarOutputStream.write(testBytes, 0, testBytes.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry retrievedEntry = tarOutputStream.getNextEntry();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = tarOutputStream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, length);
        }

        assertEquals("Expected 'Hello World!' but was '" + new String(baos.toByteArray()) + "'",
                "Hello World!", new String(baos.toByteArray()));
        retrievedEntry.close();
    }
}