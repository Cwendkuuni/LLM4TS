import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        // Test for unknown named entity
        assertFalse(Entities.isNamedEntity("unKnown"));
    }
}