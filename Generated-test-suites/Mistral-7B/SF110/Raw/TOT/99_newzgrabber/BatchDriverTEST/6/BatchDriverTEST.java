import org.junit.Test;
import static org.junit.Assert.*; // for assertions
import Newzgrabber.BatchDriver;

public class BatchDriverTest {

    @Test
    public void testGetGroups() throws IOException {
        // Given: An instance of BatchDriver with valid data
        String newzini = "path/to/newzini.ini";
        String batchfile = "path/to/batchfile.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        // When: The getGroups() method is called
        String[] groups = batchDriver.getGroups();

        // Then: The size and content of the returned array should match the data in the ini files
        int expectedSize = batchDriver.BatchgroupsVector.size();
        assertEquals(expectedSize, groups.length);
        for (int i = 0; i < groups.length; i++) {
            String expectedGroup = batchDriver.BatchgroupsVector.elementAt(i)[0];
            assertEquals(expectedGroup, groups[i]);
        }

        // Clean up: Close the resources
        batchDriver = null;
    }
}