import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveInputStream.HEADER);
        outputStream.write(new byte[]{"test".getBytes(), 0}); // name
        outputStream.write(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10}); // length
        outputStream.write(ArArchiveInputStream.TRAILER);
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (arArchiveInputStream != null) {
            arArchiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertEquals(new ArArchiveEntry("test", 10), arArchiveInputStream.getNextEntry());
        assertEquals(null, arArchiveInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryInvalidEntry() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11});
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry(); // should throw an exception
    }

    @Test
    public void testMatches() {
        assertTrue(ArArchiveInputStream.matches(new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0, 0, 0, 0, 0, 0, 0, 0, 10}, 20));
        assertFalse(ArArchiveInputStream.matches(new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0, 0, 0, 0, 0, 0, 0, 0, 11}, 20));
    }

}