import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.archiveentry.GenericTestArEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ArArchiveInputStreamTest {

    private InputStream testStream;
    private ArArchiveInputStream archiveStream;
    private ArchiveEntry entry;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"test.txt".getBytes()});
        outputStream.write(new byte[]{"01-Jan-2022 12:00:00".getBytes()});
        outputStream.write(new byte[]{"user".getBytes()});
        outputStream.write(new byte[]{"group".getBytes()});
        outputStream.write(new byte[]{(byte) 0644});
        outputStream.write(new byte[]{"12345".getBytes()});
        outputStream.write(ArArchiveEntry.TRAILER);
        testStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveStream = new ArArchiveInputStream(testStream);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        entry = archiveStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(12345L, entry.getSize());

        entry = archiveStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithError() throws IOException {
        byte[] invalidHeader = ArArchiveEntry.HEADER;
        invalidHeader[0] = (byte) 0x3c;
        InputStream errorStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream errorArchiveStream = new ArArchiveInputStream(errorStream);
        errorArchiveStream.getNextEntry();
    }

    @Test
    public void testGetNextEntryWithCustomArEntry() throws IOException {
        ArchiveInputStream expectedArchiveStream = new GenericTestArEntry();
        entry = archiveStream.getNextEntry();
        Assert.assertEquals(expectedArchiveStream, archiveStream.getNextEntry());
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];
        int readBytes;

        // read header
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(ArArchiveEntry.HEADER.length, readBytes);
        Assert.assertArrayEquals(ArArchiveEntry.HEADER, buffer);

        // read name
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(16, readBytes);
        String name = new String(buffer, 0, readBytes);
        Assert.assertEquals("test.txt", name.trim());

        // read lastmodified
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(12, readBytes);
        String lastmodified = new String(buffer, 0, readBytes);
        Assert.assertEquals("01-Jan-2022 12:00:00", lastmodified.trim());

        // read userid
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(6, readBytes);
        String userid = new String(buffer, 0, readBytes);
        Assert.assertEquals("user", userid);

        // read groupid
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(6, readBytes);
        String groupid = new String(buffer, 0, readBytes);
        Assert.assertEquals("group", groupid);

        // read filemode
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(8, readBytes);
        String filemode = new String(buffer, 0, readBytes);
        Assert.assertEquals("0644", filemode);

        // read length
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, readBytes);
        String length = new String(buffer, 0, readBytes);
        Assert.assertEquals("12345", length.trim());

        // read trailer
        readBytes = archiveStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(ArArchiveEntry.TRAILER.length, readBytes);
        Assert.assertArrayEquals(ArArchiveEntry.TRAILER, buffer);

        // check crc
        CRC32 crc = new CRC32();
        crc.update(ArArchiveEntry.HEADER);
        crc.update(new byte[]{"test.txt".getBytes()});
        crc.update(new byte[]{"01-Jan-2022 12:00:00".getBytes()});
        crc.update(new byte[]{"user".getBytes()});
        crc.update(new byte[]{"group".getBytes()});
        crc.update(new byte[]{(byte) 0644});
        crc.update(new byte[]{"12345".getBytes()});
        crc.update(ArArchiveEntry.TRAILER);
        long calculatedCrc = crc.getValue();
        long readCrc = archiveStream.readLong();
        Assert.assertEquals(calculatedCrc, readCrc);
    }

    @Test(expected = IOException.class)
    public void testReadBytesWithError() throws IOException {
        byte[] buffer = new byte[10];
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        // hit eof
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
        archiveStream.read(buffer, 0, buffer.length);
    }
}