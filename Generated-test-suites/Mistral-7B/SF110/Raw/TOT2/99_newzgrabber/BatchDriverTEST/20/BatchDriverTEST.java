import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
        assertEquals(expectedGroups.length, actualGroups.length);
    }
}