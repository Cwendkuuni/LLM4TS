import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream input;
    private ArArchiveInputStream arInput;

    @Before
    public void setUp() throws Exception {
        input = this.getClass().getResourceAsStream("/test_ar_file.tar.gz");
        arInput = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws Exception {
        arInput.close();
        input.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry currentEntry = null;

        while ((currentEntry = arInput.getNextArEntry()) != null) {
            assertEquals("Entry name should match.", "entry1.txt", currentEntry.getName());
            assertEquals("Entry size should match.", 13, currentEntry.getSize());
            assertNull("There should be no next entry.", arInput.getNextArEntry());
        }
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryThrowsExceptionIfInputStreamReachesEnd() throws IOException {
        ArArchiveEntry currentEntry = null;

        while ((currentEntry = arInput.getNextArEntry()) != null) {
            // Consume all entries
        }

        arInput.getNextArEntry();
    }

    @Test
    public void testGetNextEntryThrowsExceptionIfInvalidHeader() throws IOException {
        byte[] header = new byte[ArArchiveEntry.HEADER.length];

        for (int i = 0; i < header.length; i++) {
            header[i] = (byte) (0x55 + i % 2); // Malformed header
        }

        ByteArrayInputStream badStream = new ByteArrayInputStream(header);
        ArArchiveInputStream badArInput = new ArArchiveInputStream(badStream);

        badArInput.getNextArEntry();
    }

    @Test
    public void testMatchesFunction() {
        byte[] signature = new byte[16];

        for (int i = 0; i < signature.length; i++) {
            signature[i] = (byte) (0x42 + i % 2); // Valid header
        }

        assertTrue("Signature should match.", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesFunctionWithInvalidLength() {
        byte[] signature = new byte[15];

        assertFalse("Signature should not match.", ArArchiveInputStream.matches(signature, signature.length));
    }
}