import org.apache.commons.compress.archivers.test.SimpleTestArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        SimpleTestArchiveEntry entry = new SimpleTestArchiveEntry("test.txt", false, 0L);
        String expected = "- 0 test.txt";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }
}