import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;

    @Before
    public void setup() throws IOException, NullPointerException {
        batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
        assertNotNull(actualGroups);
    }
}