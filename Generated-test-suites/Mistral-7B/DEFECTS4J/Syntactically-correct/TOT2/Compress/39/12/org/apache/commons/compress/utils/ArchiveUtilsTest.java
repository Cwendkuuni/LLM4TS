import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test basic functionality
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String expected = "- 0 test.txt";
        String actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);

        // Test edge cases
        ArchiveEntry nullEntry = null;
        try {
            String nullEntryString = ArchiveUtils.toString(nullEntry);
            fail("Expected IllegalArgumentException with null ArchiveEntry");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ArchiveEntry emptyNameEntry = new ArchiveEntry("");
        String emptyNameEntryString = ArchiveUtils.toString(emptyNameEntry);
        assertEquals("d 0", emptyNameEntryString);
    }
}