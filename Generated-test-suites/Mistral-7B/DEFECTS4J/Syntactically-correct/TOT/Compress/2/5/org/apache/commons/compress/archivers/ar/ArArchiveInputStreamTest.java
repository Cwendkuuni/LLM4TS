import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private static final byte[] AR_HEADER = new byte[]{(byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x63, (byte) 0x0a, (byte) 0x3e};
    private static final byte[] AR_ENTRY_HEADER = new byte[]{(byte) 0x07, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0a, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x08, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x64, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x3e, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x08, (byte) 0x01, (byte) 0x06, (byte) 0x02, (byte) 0x01, (byte) 0x03, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x0a, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x0a};
    private static final byte[] AR_TRAILER = new byte[]{(byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x07, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x08, (byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x08, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0a};

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] inputData = new byte[AR_HEADER.length + AR_ENTRY_HEADER.length + AR_TRAILER.length];
        System.arraycopy(AR_HEADER, 0, inputData, 0, AR_HEADER.length);
        System.arraycopy(AR_ENTRY_HEADER, 0, inputData, AR_HEADER.length, AR_ENTRY_HEADER.length);
        System.arraycopy(AR_TRAILER, 0, inputData, AR_HEADER.length + AR_ENTRY_HEADER.length, AR_TRAILER.length);

        InputStream inputStream = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertEquals(new ArArchiveEntry("test.txt", 16), entry);

        entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryError() throws IOException {
        byte[] inputData = new byte[AR_HEADER.length];
        System.arraycopy(AR_HEADER, 0, inputData, 0, AR_HEADER.length);

        InputStream inputStream = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry();
    }
}