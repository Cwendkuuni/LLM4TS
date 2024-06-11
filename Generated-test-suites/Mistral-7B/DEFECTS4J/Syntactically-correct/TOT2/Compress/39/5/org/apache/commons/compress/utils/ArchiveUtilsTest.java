import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArchiveUtilsTest {

    @Test
    public void testToString() throws Exception {
        ArchiveEntry entry = new TestArchiveEntry("test.txt", true, 1024, "text/plain".getBytes());
        String result = ArchiveUtils.toString(entry);
        assertEquals("- 1024 test.txt", result);
    }
}