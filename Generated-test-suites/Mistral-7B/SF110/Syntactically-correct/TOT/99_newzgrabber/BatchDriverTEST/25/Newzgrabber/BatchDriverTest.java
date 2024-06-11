import org.junit.Assert;
import org.junit.Test;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        // Given an instance of BatchDriver
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");

        // When calling the getGroups() method
        String[] groups = batchDriver.getGroups();

        // Then the size of the groups array should match the number of groups in the BatchgroupsVector
        Assert.assertArrayEquals(new String[]{"Group1", "Group2", "Group3"}, groups);
    }
}