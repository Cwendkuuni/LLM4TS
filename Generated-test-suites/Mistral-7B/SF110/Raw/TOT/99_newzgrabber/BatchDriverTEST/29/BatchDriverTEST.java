import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        // Given: A valid BatchDriver instance
        String newzini = "test/resources/newzini.ini";
        String batchfile = "test/resources/batchfile.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        // When: The getGroups method is called
        String[] groups = batchDriver.getGroups();

        // Then: The size of the returned array should match the number of groups in the Batchfile
        int expectedSize = batchDriver.BatchgroupsVector.size();
        assertEquals(expectedSize, groups.length);

        // And: Each element in the array should be a valid newsgroups name
        for (String group : groups) {
            assertTrue(batchDriver.NewsgroupsHash.containsKey(group));
        }
    }
}