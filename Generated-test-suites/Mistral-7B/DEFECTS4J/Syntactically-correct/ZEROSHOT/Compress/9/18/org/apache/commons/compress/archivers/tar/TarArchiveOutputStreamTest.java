import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", null);
        outputStream = new CountingOutputStream(new FileOutputStream(tempFile));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @After
    public void tearDown() throws IOException {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testCreateAndFinishArchive() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        testEntry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        TarArchiveOutputStream expectedTarArchiveOutputStream = new TarArchiveOutputStream(expectedOutput);
        expectedTarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntryToWrite = new TarArchiveEntry("test.txt");
        testEntryToWrite.setSize(10);
        expectedTarArchiveOutputStream.putArchiveEntry(testEntryToWrite);

        // Write header
        testEntryToWrite.writeHeader(expectedTarArchiveOutputStream.getBuffer());
        expectedTarArchiveOutputStream.write(testEntryToWrite.getName().getBytes());
        expectedTarArchiveOutputStream.write(testEntryToWrite.isDirectory() ? (byte) 0 : (byte) 1);
        expectedTarArchiveOutputStream.writeLong(testEntryToWrite.getSize());
        expectedTarArchiveOutputStream.writeLong(testEntryToWrite.getMtime());
        expectedTarArchiveOutputStream.writeLong(testEntryToWrite.getMtime());
        expectedTarArchiveOutputStream.writeBytes(testEntryToWrite.getChecksum());

        // Write data
        byte[] data = "Hello World!".getBytes();
        expectedTarArchiveOutputStream.write(data, 0, data.length);

        // Write EOF record
        expectedTarArchiveOutputStream.writeEOFRecord();
        expectedTarArchiveOutputStream.finish();
        expectedTarArchiveOutputStream.close();

        ByteArrayInputStream actualInput = new ByteArrayInputStream(tempFile.getBytes());
        TarArchiveOutputStream actualTarArchiveOutputStream = new TarArchiveOutputStream(actualInput);

        byte[] buffer = new byte[expectedOutput.size()];
        actualTarArchiveOutputStream.read(buffer);

        assertEquals(expectedOutput.toByteArray(), buffer);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        testEntry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Should throw IOException here
        tarArchiveOutputStream.write("Hello World!".getBytes());
    }
}