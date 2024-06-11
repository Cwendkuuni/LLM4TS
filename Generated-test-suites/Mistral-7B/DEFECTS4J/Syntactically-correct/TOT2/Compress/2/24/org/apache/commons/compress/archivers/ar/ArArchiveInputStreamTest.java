import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.apache.commons.compress.archivers.test.TestFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArArchiveInputStreamTest {

    private ArchiveOutputStream archive;
    private ArArchiveInputStream input;

    @Before
    public void setUp() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        archive = new ArchiveOutputStream("mem:ArArchiveInputStreamTest.tar.gz", output);

        archive.putNextEntry(new TestArchiveEntry("test.txt", "Test content."));
        archive.write("Test content.".getBytes(StandardCharsets.UTF_8));
        archive.closeEntry();
        archive.finish();

        final InputStream testStream = new ByteArrayInputStream(output.toByteArray());
        input = new ArArchiveInputStream(testStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(archive);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = input.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.getSize() == 14);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryAfterEOF() throws IOException {
        input.getNextArEntry();
        input.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = input.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.getSize() == 14);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidSignature() throws IOException {
        final byte[] signature = new byte[8];
        signature[0] = 0x3c;
        signature[1] = 0x61;
        signature[2] = 0x72;
        signature[3] = 0x63;
        signature[4] = 0x68;
        signature[5] = 0x3e;
        signature[6] = 0x0a;
        signature[7] = 0x01;

        final InputStream testStream = new ByteArrayInputStream(signature);
        ArArchiveInputStream testInput = new ArArchiveInputStream(testStream);
        testInput.getNextArEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int read = input.read(buffer);
        assertEquals(14, read);
        assertEquals("Test content.".getBytes(StandardCharsets.UTF_8)[0], buffer[0]);
    }

    @Test(expected = IOException.class)
    public void testReadAfterEOF() throws IOException {
        byte[] buffer = new byte[10];
        input.read(buffer);
        input.read(buffer);
    }

    @Test
    public void testClose() throws IOException {
        input.close();
        try {
            input.getNextArEntry();
            assert false;
        } catch (IOException e) {
        }
    }
}