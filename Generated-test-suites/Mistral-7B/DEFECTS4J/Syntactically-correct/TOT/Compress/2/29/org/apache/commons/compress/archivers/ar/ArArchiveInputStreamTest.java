import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
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
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        String header = "!<arch>\n" +
                "  Sun Apr 25 07:40:27 2021\n" +
                "  myuser mygroup\n" +
                "  a\n" +
                "  16\n" +
                "  text.txt\n" +
                "  text/plain\n" +
                "  123\n" +
                "  \n" +
                "!<eoh>\n" +
                "text/plain\n" +
                "Hello world!";

        inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.UTF_8));
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (archiveInputStream != null) {
            archiveInputStream.close();
        }
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("text.txt", entry.getName());
        assertEquals("text/plain", entry.getSize(), entry.getCompressedSize());

        entry = archiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithInvalidHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("!<arch>\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("a\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("text.txt\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("text/plain\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("123\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("!<eoh>\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("Hello world!".getBytes(StandardCharsets.UTF_8));

        InputStream invalidInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInputStream);
        ArArchiveEntry entry = invalidArchiveInputStream.getNextArEntry(); // Exception should be thrown here
    }

    @Test
    public void testGetNextEntryWithEmptyArchive() throws IOException {
        String header = "!<arch>\n" +
                "!<eoh>\n";

        inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.UTF_8));
        archiveInputStream = new ArArchiveInputStream(inputStream);

        assertNull(archiveInputStream.getNextArEntry());
    }
}