import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World".getBytes(), 0, "Hello World".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[actualBytes.length + TarConstants.NAMELEN];
        System.arraycopy(actualBytes, 0, expectedBytes, 0, actualBytes.length);
        String fileName = "test.txt";
        int nameLength = fileName.getBytes(StandardCharsets.UTF_8).length;
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, expectedBytes, actualBytes.length, nameLength);

        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(expectedBytes));
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());

        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry("long_test_" + TarConstants.NAMELEN + "_name.txt");
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);
        tarArchiveOutputStream.write("Long file name".getBytes(), 0, "Long file name".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readLongFileNameEntry = new TarArchiveEntry(inputStream);
        assertEquals(longFileNameEntry.getName(), readLongFileNameEntry.getName());
        assertEquals(longFileNameEntry.getSize(), readLongFileNameEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testFinishAfterPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[actualBytes.length + TarConstants.NAMELEN + data.length];
        System.arraycopy(actualBytes, 0, expectedBytes, 0, actualBytes.length);
        String fileName = "test.txt";
        int nameLength = fileName.getBytes(StandardCharsets.UTF_8).length;
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, expectedBytes, actualBytes.length, nameLength);
        System.arraycopy(data, 0, expectedBytes, actualBytes.length + nameLength, data.length);

        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(expectedBytes));
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());
        byte[] readData = new byte[readEntry.getSize() - TarConstants.NAMELEN];
        readEntry.getInputStream().read(readData);
        assertTrue(new String(readData, StandardCharsets.UTF_8).equals("Hello World"));
    }
}