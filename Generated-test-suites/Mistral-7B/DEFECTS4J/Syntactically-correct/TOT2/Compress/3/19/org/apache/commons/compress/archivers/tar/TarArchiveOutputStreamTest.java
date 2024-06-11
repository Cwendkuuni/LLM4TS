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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarOutputStream tarOut;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntryWithValidNameAndSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        byte[] bytes = out.toByteArray();
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 0, 512 + 512)));
        assertEquals("test.txt", readEntry.getName());
        assertEquals(10, readEntry.getSize());
        assertTrue(Arrays.equals(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 512, 512 + 10)).readAllBytes(), "Hello, World!".getBytes(StandardCharsets.UTF_8)));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongName() throws IOException {
        String tooLongName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(tooLongName);
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNegativeSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(-10);
        tarOut.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDir/");
        entry.setSize(0);
        entry.setDirectory(true);
        tarOut.putArchiveEntry(entry);

        byte[] bytes = out.toByteArray();
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 0, 512 + 512)));
        assertEquals("testDir/", readEntry.getName());
        assertEquals(0, readEntry.getSize());
        assertNotNull(readEntry.isDirectory());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        byte[] bytes = out.toByteArray();
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 512, 512 + 512)));
        assertEquals("test.txt", readEntry.getName());
        assertEquals(10, readEntry.getSize());
        assertTrue(Arrays.equals(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 0, 512)).readAllBytes(), readEntry.getHeaderRecord()));
        assertTrue(Arrays.equals(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 512, 512 + 10)).readAllBytes(), "Hello, World!".getBytes(StandardCharsets.UTF_8)));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        tarOut.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLongFileNames() throws IOException {
        String longFileName = "longFileName" + TarConstants.NAMELEN + "long";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[512]); // Record header
        tarOut.write(longFileName.getBytes(StandardCharsets.UTF_8)); // Name record
        tarOut.write(new byte[1]); // Name record terminator
        tarOut.write(new byte[512]); // Data record header
        tarOut.write(new byte[10]); // Data record
        tarOut.closeArchiveEntry();

        byte[] bytes = out.toByteArray();
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(Arrays.copyOfRange(bytes, 0, 512 + 512)));
        assertEquals(longFileName, readEntry.getName());
    }
}