import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        BatchDriver batchedriver = new BatchDriver("newzini.ini", "batchfile.ini");
        assertNotNull(batchedriver.getGroups());
        assertEquals(3, batchedriver.getGroups().length);
        assertArrayEquals(new String[]{"group1", "group2", "group3"}, batchedriver.getGroups());

        // Test with empty ini file
        BatchDriver batchedriverEmpty = new BatchDriver("empty.ini", "batchfile.ini");
        assertNull(batchedriverEmpty.getGroups());
    }
}