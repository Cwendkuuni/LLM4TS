import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        String testData = "test-header\n" +
                "01010101010101010101\n" +
                "00000000000000000001\n" +
                "user\n" +
                "group\n" +
                "0664\n" +
                "10\n" +
                "test-data\n" +
                "0000803c\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8)));
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        gzipInputStream.close();
        outputStream.close();

        byte[] compressedData = outputStream.toByteArray();
        this.inputStream = new ByteArrayInputStream(compressedData);
        this.archiveInputStream = new ArArchiveInputStream(this.inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (this.archiveInputStream != null) {
            this.archiveInputStream.close();
        }
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = this.archiveInputStream.getNextArEntry();

        assertTrue("Expected entry to not be null", entry != null);
        assertEquals("Expected entry name to be 'test-data'", "test-data", entry.getName());
        assertEquals("Expected entry size to be 10", 10L, entry.getSize());

        this.archiveInputStream.getNextArEntry(); // consume next entry

        assertNull("Expected next entry to be null", this.archiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        this.archiveInputStream.getNextArEntry(); // consume first entry

        ArArchiveEntry entry = (ArArchiveEntry) this.archiveInputStream.getNextEntry();

        assertEquals("Expected entry to not be null", entry, this.archiveInputStream.getNextArEntry());
        assertEquals("Expected entry name to be 'test-data'", "test-data", entry.getName());
        assertEquals("Expected entry size to be 10", 10L, entry.getSize());

        assertNull("Expected next entry to be null", this.archiveInputStream.getNextEntry());
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[8];
        System.arraycopy(ArArchiveInputStream.HEADER.getBytes(), 0, signature, 0, signature.length);

        assertTrue("Expected signature to match", ArArchiveInputStream.matches(signature, 8));
    }

    @Test(expected = IOException.class)
    public void testFailedToReadArHeader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        archiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidArHeader() throws IOException {
        byte[] testData = "test-header\n" +
                "01010101010101010101\n" +
                "00000000000000000001\n" +
                "user\n" +
                "group\n" +
                "0664\n" +
                "11\n" + // invalid length
                "test-data\n" +
                "0000803c\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8)));
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        gzipInputStream.close();
        outputStream.close();

        byte[] compressedData = outputStream.toByteArray();
        InputStream invalidArchiveInputStream = new ByteArrayInputStream(compressedData);
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(invalidArchiveInputStream);
        testArchiveInputStream.getNextArEntry();
    }
}