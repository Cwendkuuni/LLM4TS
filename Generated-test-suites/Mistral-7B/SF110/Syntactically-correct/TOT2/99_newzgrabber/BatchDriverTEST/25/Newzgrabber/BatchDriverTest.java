import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
        assertNotNull(batchDriver.getGroups());
    }
}