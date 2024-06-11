import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArArchiveInputStreamTest {

    private ArArchiveInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        ArchiveTestUtils.writeTestData("test-data.ar", outputStream);
        inputStream = new ArArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = inputStream.getNextArEntry();
        Assertions.assertEquals("entry1.txt", entry.getName());
        Assertions.assertEquals(11L, entry.getSize());

        entry = inputStream.getNextArEntry();
        Assertions.assertEquals("entry2.bin", entry.getName());
        Assertions.assertEquals(15L, entry.getSize());

        entry = inputStream.getNextArEntry();
        Assertions.assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = inputStream.getNextEntry();
        Assertions.assertEquals("entry1.txt", entry.getName());
        Assertions.assertEquals(11L, entry.getSize());

        entry = inputStream.getNextEntry();
        Assertions.assertEquals("entry2.bin", entry.getName());
        Assertions.assertEquals(15L, entry.getSize());

        entry = inputStream.getNextEntry();
        Assertions.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(8, bytesRead);
        assertEquals(ArchiveTestUtils.HEADER, new String(buffer).getBytes());

        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(16, bytesRead);
        assertEquals(ArArchiveEntry.NAME, new String(new String(buffer, 0, 16).trim()).getBytes());

        // read the rest of the entry
        while (bytesRead > 0) {
            bytesRead = inputStream.read(buffer, 0, buffer.length);
        }

        ArArchiveEntry entry = inputStream.getNextArEntry();
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(11, bytesRead);
        assertEquals("entry1.txt".getBytes(), buffer);

        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[8];
        assertEquals(true, ArArchiveInputStream.matches(signature, 8));

        signature[0] = (byte) 0x20;
        assertEquals(false, ArArchiveInputStream.matches(signature, 8));

        signature[1] = (byte) 0x67;
        assertEquals(false, ArArchiveInputStream.matches(signature, 8));
    }
}