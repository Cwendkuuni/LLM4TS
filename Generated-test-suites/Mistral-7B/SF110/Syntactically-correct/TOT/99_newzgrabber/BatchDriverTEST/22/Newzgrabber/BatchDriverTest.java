import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        // Given
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
         assertNotNull(batchDriver);

        // When
        String[] groups = batchDriver.getGroups();

        // Then
        assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups);
        batchDriver.close();
    }
}