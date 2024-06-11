import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        final String newzini = "path/to/newzini.ini";
        final String batchfile = "path/to/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"}; // replace with actual groups
        String[] actualGroups = bd.getGroups();
        assertNotNull(actualGroups);
        assertArrayEquals(expectedGroups, actualGroups);
    }
}