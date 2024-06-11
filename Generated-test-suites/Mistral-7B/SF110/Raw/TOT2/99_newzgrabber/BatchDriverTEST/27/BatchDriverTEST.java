import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }
}