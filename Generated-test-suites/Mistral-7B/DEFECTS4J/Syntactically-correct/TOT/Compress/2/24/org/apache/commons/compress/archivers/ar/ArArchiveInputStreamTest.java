import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_ValidEntry_ReturnsExpectedEntry() throws IOException, DataFormatException {
        // Given
        byte[] header = "!<arch>\n" +
                "  type 5\n" +
                "name name1.txt\n" +
                "time 65535\n" +
                "uid uid1\n" +
                "gid gid1\n" +
                "mode 0644\n" +
                "size 23\n" +
                "xfill 0\n" +
                "!<data>\n" +
                "abcdefghijk";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.US_ASCII));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        // When
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        // Then
        Assertions.assertNotNull(entry);
        Assertions.assertEquals("name1.txt", entry.getName());
        Assertions.assertEquals(23, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_EndOfStream_ReturnsNull() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        // When
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        // Then
        Assertions.assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_MalformedHeader_ThrowsIOException() throws IOException {
        // Given
        byte[] header = "!<arch>\n" +
                "  type 5\n" +
                "name name1.txt\n" +
                "time 65535\n" +
                "uid uid1\n" +
                "gid gid1\n" +
                "mode 0644\n" +
                "size 23\n" +
                "xfill 0\n" +
                "!<data>\n" +
                "abcdefghijk\n" + // Extra line
                "!<trailer>\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.US_ASCII));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        // When & Then
        Assertions.assertThrows(IOException.class, archiveInputStream::getNextArEntry);
    }

    @Test
    public void testGetNextArEntry_InvalidMagicNumber_ThrowsIOException() throws IOException {
        // Given
        byte[] header = "!<arch2>\n" +
                "  type 5\n" +
                "name name1.txt\n" +
                "time 65535\n" +
                "uid uid1\n" +
                "gid gid1\n" +
                "mode 0644\n" +
                "size 23\n" +
                "xfill 0\n" +
                "!<data>\n" +
                "abcdefghijk";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.US_ASCII));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        // When & Then
        Assertions.assertThrows(IOException.class, archiveInputStream::getNextArEntry);
    }

    @Test
    public void testGetNextArEntry_MissingTrailer_ThrowsIOException() throws IOException, DataFormatException {
        // Given
        byte[] header = "!<arch>\n" +
                "  type 5\n" +
                "name name1.txt\n" +
                "time 65535\n" +
                "uid uid1\n" +
                "gid gid1\n" +
                "mode 0644\n" +
                "size 23\n" +
                "xfill 0\n" +
                "!<data>\n" +
                "abcdefghijk";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.US_ASCII));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        // When & Then
        archiveInputStream.read(new byte[1]); // Read the last byte of the data section
        Assertions.assertThrows(IOException.class, archiveInputStream::getNextArEntry);
    }
}