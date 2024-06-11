import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Basic test
        assertEquals(true, Entities.isNamedEntity("lt"));

        // Edge cases
        assertEquals(false, Entities.isNamedEntity("")); // empty string
        assertEquals(false, Entities.isNamedEntity("a")); // invalid named entity
        assertEquals(true, Entities.isNamedEntity("amp"));

        // Exception handling
        try {
            Entities.isNamedEntity(null);
            fail("Expected IllegalArgumentException, but got none");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test
        assertEquals(true, Entities.isBaseNamedEntity("amp"));

        // Edge cases
        assertEquals(false, Entities.isBaseNamedEntity("")); // empty string
        assertEquals(false, Entities.isBaseNamedEntity("lt")); // invalid named entity
        assertEquals(true, Entities.isBaseNamedEntity("quot"));

        // Exception handling
        try {
            Entities.isBaseNamedEntity(null);
            fail("Expected IllegalArgumentException, but got none");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test other methods similarly with appropriate edge cases and exception handling
}