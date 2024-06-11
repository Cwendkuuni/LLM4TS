import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream testStream;
    private ArchiveStreamFactory factory;
    private Path testFile;

    @Before
    public void setup() throws IOException {
        String testData = "Test Data\n";
        testFile = Files.createTempFile("test", ".ar");
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw");
             FileChannel channel = raf.getChannel()) {
            List<ArArchiveEntry> entries = new ArrayList<>();
            entries.add(new ArArchiveEntry("TestEntry.txt", 11L, null, null, null, null));
            ArArchiveEntry entry = entries.get(0);
            byte[] header = ArArchiveEntry.HEADER.getBytes();
            byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
            byte[] name = entry.getName().getBytes(StandardCharsets.UTF_8);
            byte[] length = Long.toString(entry.getSize()).getBytes(StandardCharsets.UTF_8);
            byte[] userid = null;
            byte[] groupid = null;
            byte[] filemode = null;
            byte[] lastmodified = Long.toString(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8);
            byte[] data = testData.getBytes(StandardCharsets.UTF_8);

            long headerLength = header.length + name.length + length.length
                    + userid.length + groupid.length + filemode.length + trailer.length;
            channel.position(headerLength);
            channel.write(header);
            channel.write(name);
            channel.write(length);
            channel.write(userid != null ? userid : new byte[0]);
            channel.write(groupid != null ? groupid : new byte[0]);
            channel.write(filemode != null ? filemode : new byte[0]);
            channel.write(lastmodified);
            channel.write(length);
            channel.write(trailer);
            channel.write(data);
        }
        factory = new ArchiveStreamFactory();
        testStream = (ArArchiveInputStream) factory.createArchiveInputStream(testFile.toFile());
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = testStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("TestEntry.txt", entry.getName());
        Assert.assertEquals(11L, entry.getSize());
        entry = testStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = testStream.getNextEntry();
        Assert.assertEquals(entry.getClass(), ArArchiveEntry.class);
        Assert.assertNotNull(entry);
        Assert.assertEquals("TestEntry.txt", entry.getName());
        Assert.assertEquals(11L, entry.getSize());
        entry = testStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        testStream.close();
        testStream = null;
        ArArchiveEntry entry = testStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithClosedStream() throws IOException {
        testStream.close();
        ArArchiveEntry entry = testStream.getNextArEntry();
        Assert.assertNull(entry);
        testStream.getNextArEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read = testStream.read(buffer);
        Assert.assertNotNull(read);
        Assert.assertEquals(1, read);
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        int read = testStream.read(null);
        Assert.assertNull(read);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int read = testStream.read(buffer);
        Assert.assertEquals(0, read);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int read = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(11, read);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] data = testStream.readAllBytes();
        Assert.assertNotNull(data);
        String testData = new String(data, StandardCharsets.UTF_8);
        Assert.assertEquals("Test Data\n", testData);
    }

    @Test
    public void testMatches() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        testStream.mark(1);
        testStream.read(new byte[1], 0, 1);
        testStream.reset();
        byte[] signature = new byte[8];
        testStream.read(signature);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testIOExceptionWhenInvalidArFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Invalid Ar File".getBytes(StandardCharsets.UTF_8));
        Assert.assertThrows(IOException.class, () -> new ArArchiveInputStream(inputStream));
    }

    @Test
    public void testIOExceptionWhenEOF() throws IOException {
        ArArchiveInputStream testStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertThrows(IOException.class, testStream::getNextArEntry);
    }

    @Test
    public void testIOExceptionWhenInvalidEntry() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(ArArchiveEntry.HEADER.getBytes());
            outputStream.write(new byte[16]);
            outputStream.write(new byte[12]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[8]);
            outputStream.write(new byte[10]);
            outputStream.write(ArArchiveEntry.TRAILER.getBytes());
            outputStream.write(new byte[4]);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ArArchiveInputStream testStream = new ArArchiveInputStream(inputStream);
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfStream() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw");
             FileChannel channel = raf.getChannel()) {
            long headerLength = ArArchiveEntry.HEADER.getBytes().length;
            channel.position(channel.size() - 1);
            channel.writeByte(0x0a);
            channel.position(headerLength);
            channel.write(ArArchiveEntry.HEADER.getBytes());
            testStream = (ArArchiveInputStream) factory.createArchiveInputStream(testFile.toFile());
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEof() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(ArArchiveEntry.HEADER.getBytes());
            outputStream.write(new byte[16]);
            outputStream.write(new byte[12]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[8]);
            outputStream.write(new byte[10]);
            byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
            outputStream.write(trailer, 0, trailer.length - 1);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ArArchiveInputStream testStream = new ArArchiveInputStream(inputStream);
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfFile() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
            raf.setLength(ArArchiveEntry.HEADER.getBytes().length + ArArchiveEntry.TRAILER.getBytes().length);
            testStream = (ArArchiveInputStream) factory.createArchiveInputStream(testFile.toFile());
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfStreamWithData() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
            long headerLength = ArArchiveEntry.HEADER.getBytes().length;
            raf.setPosition(raf.length() - 1);
            raf.writeByte(0x0a);
            raf.setPosition(headerLength);
            raf.write(ArArchiveEntry.HEADER.getBytes());
            raf.write(ArArchiveEntry.TRAILER.getBytes());
            ArArchiveInputStream testStream = new ArArchiveInputStream(testFile.toFile());
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEofWithData() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(ArArchiveEntry.HEADER.getBytes());
            outputStream.write(new byte[16]);
            outputStream.write(new byte[12]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[8]);
            outputStream.write(new byte[10]);
            byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
            outputStream.write(trailer, 0, trailer.length - 1);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ArArchiveInputStream testStream = new ArArchiveInputStream(inputStream);
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfFileWithData() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
            long headerLength = ArArchiveEntry.HEADER.getBytes().length;
            raf.setLength(headerLength + ArArchiveEntry.TRAILER.getBytes().length - 1);
            ArArchiveInputStream testStream = new ArArchiveInputStream(testFile.toFile());
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfStreamWithDataAndMark() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
            long headerLength = ArArchiveEntry.HEADER.getBytes().length;
            raf.setPosition(raf.length() - 1);
            raf.writeByte(0x0a);
            raf.setPosition(headerLength);
            raf.write(ArArchiveEntry.HEADER.getBytes());
            raf.write(ArArchiveEntry.TRAILER.getBytes());
            ArArchiveInputStream testStream = new ArArchiveInputStream(testFile.toFile());
            testStream.mark(1);
            testStream.read(new byte[1], 0, 1);
            testStream.reset();
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEofWithDataAndMark() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(ArArchiveEntry.HEADER.getBytes());
            outputStream.write(new byte[16]);
            outputStream.write(new byte[12]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[6]);
            outputStream.write(new byte[8]);
            outputStream.write(new byte[10]);
            byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
            outputStream.write(trailer, 0, trailer.length - 1);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ArArchiveInputStream testStream = new ArArchiveInputStream(inputStream);
            testStream.mark(1);
            testStream.read(new byte[1], 0, 1);
            testStream.reset();
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @Test
    public void testIOExceptionWhenUnexpectedEndOfFileWithDataAndMark() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
            long headerLength = ArArchiveEntry.HEADER.getBytes().length;
            raf.setLength(headerLength + ArArchiveEntry.TRAILER.getBytes().length - 1);
            ArArchiveInputStream testStream = new ArArchiveInputStream(testFile.toFile());
            testStream.mark(1);
            testStream.read(new byte[1], 0, 1);
            testStream.reset();
            Assert.assertThrows(IOException.class, testStream::getNextArEntry);
        }
    }

    @After
    public void cleanup() throws IOException {
        testStream.close();
        Files.delete(testFile);
    }
}