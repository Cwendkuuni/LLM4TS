import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Positive test case with a known named entity
        assertTrue(Entities.isNamedEntity("lt"));

        // Negative test case with an unknown named entity
        assertFalse(Entities.isNamedEntity("xx"));

        // Edge case test with an empty string
        assertFalse(Entities.isNamedEntity(""));
    }
}