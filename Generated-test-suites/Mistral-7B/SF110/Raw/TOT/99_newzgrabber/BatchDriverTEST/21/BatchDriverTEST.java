import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        // Given
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        // When
        String[] groups = batchDriver.getGroups();

        // Then
        assertNotNull(groups);
        assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups);
    }
}