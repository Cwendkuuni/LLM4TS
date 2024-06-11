import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u0000AR\u0003\u0012test.txt\u0000\u0000\u0012\u0000\u0000\u0000\u0000\u0000\u0012test.txt\u0000\u0000\u0012\u0000\u0000\u0000\u0000\u0000\u003c\u0021\u0072\u0061\u0068\u003e"}.itoBytes());
        ArArchiveInputStream input = new ArArchiveInputStream(inputStream);
        ArArchiveEntry entry = input.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(12L, entry.getSize());
        input.close();
    }
}