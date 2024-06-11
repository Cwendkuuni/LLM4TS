import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws Exception {
        InputStream input = null; // Provide a valid InputStream here
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertNotNull(arArchiveEntry);
    }
}