import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = driver.getGroups();
        assertEquals(expectedGroups.length, actualGroups.length);
        for (int i = 0; i < expectedGroups.length; i++) {
            assertEquals(expectedGroups[i], actualGroups[i]);
        }
    }
}