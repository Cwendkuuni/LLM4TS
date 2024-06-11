import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import Newzgrabber.BatchDriver;

public class GetGroupsTest {

    @Test
    public void testGetGroups() throws IOException {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] groups = batchDriver.getGroups();

        assertNotNull(groups);
        assertEquals(expectedGroups.length, groups.length);
        assertArrayEquals(expectedGroups, groups);
    }
}