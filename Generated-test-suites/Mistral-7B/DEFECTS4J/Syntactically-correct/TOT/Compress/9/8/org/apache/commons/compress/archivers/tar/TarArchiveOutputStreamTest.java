import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private File tempFile;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test-", null);
        tarStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        tarStream.finish();
        tarStream.close();
        tempFile.delete();
    }

    @Test
    public void testNormalOperation() throws IOException {
        // Write an entry
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "test data");
        tarStream.putArchiveEntry(testEntry);
        byte[] testData = "test data".getBytes();
        tarStream.write(testData, 0, testData.length);
        tarStream.closeArchiveEntry();

        // Read the entry back
        InputStream inputStream = new FileInputStream(tempFile);
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", readEntry.getName());
        assertEquals(testData.length, readEntry.getSize());
        byte[] buffer = new byte[testData.length];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(testData.length, bytesRead);
        assertEquals(testData, new String(buffer).getBytes());
        inputStream.close();

        // Test LongFileMode
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry("longFileName" + File.separator + "longFileName.txt", "long data");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(longFileNameEntry);
        byte[] longFileNameData = "long data".getBytes();
        tarStream.write(longFileNameData, 0, longFileNameData.length);
        tarStream.closeArchiveEntry();

        InputStream longFileNameInputStream = new FileInputStream(tempFile);
        TarArchiveEntry readLongFileNameEntry = new TarArchiveEntry(longFileNameInputStream);
        assertEquals("longFileName" + File.separator + "longFileName.txt", readLongFileNameEntry.getName());
        longFileNameInputStream.close();

        // Test write and close multiple entries
        TarArchiveEntry secondEntry = new TarArchiveEntry("test2.txt", "test data 2");
        tarStream.putArchiveEntry(secondEntry);
        byte[] secondTestData = "test data 2".getBytes();
        tarStream.write(secondTestData, 0, secondTestData.length);
        tarStream.closeArchiveEntry();

        TarArchiveEntry thirdEntry = new TarArchiveEntry("test3.txt", "test data 3");
        tarStream.putArchiveEntry(thirdEntry);
        byte[] thirdTestData = "test data 3".getBytes();
        tarStream.write(thirdTestData, 0, thirdTestData.length);
        tarStream.closeArchiveEntry();

        // Test write to closed stream
        byte[] closedStreamData = "closed data".getBytes();
        tarStream.write(closedStreamData, 0, closedStreamData.length);
        assertFalse(tarStream.isOpen());

        // Test write after finish
        tarStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        tarStream.putArchiveEntry(new TarArchiveEntry("test4.txt", "test data 4"));
        tarStream.finish();
        byte[] finishedStreamData = "finished data".getBytes();
        tarStream.write(finishedStreamData, 0, finishedStreamData.length);
        assertNull(tarStream.getArchiveEntry());
        assertFalse(tarStream.isOpen());
    }
}