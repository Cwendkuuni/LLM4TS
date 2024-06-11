import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {
    @Test
    public void testGetGroups() throws IOException, NullPointerException {
        final String newzini = "test/resources/newzini.ini";
        final String batchfile = "test/resources/batchfile.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups);
    }
}