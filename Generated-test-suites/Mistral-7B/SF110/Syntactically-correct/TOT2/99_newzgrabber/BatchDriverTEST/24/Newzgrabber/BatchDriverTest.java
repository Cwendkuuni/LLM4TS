import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {

    @Test
    public void testGetGroups() throws IOException, NullPointerException {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertNotNull(actualGroups);
        assertArrayEquals(expectedGroups, actualGroups);
    }
}