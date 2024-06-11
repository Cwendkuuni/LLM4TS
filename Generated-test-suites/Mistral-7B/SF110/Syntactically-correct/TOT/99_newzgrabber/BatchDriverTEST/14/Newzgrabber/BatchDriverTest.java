import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        // Given
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");

        // When
        String[] groups = batchDriver.getGroups();

        // Then
        assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups);
        assertEquals("group1", groups[0]);
    }
}