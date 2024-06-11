import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Basic test
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge cases
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity("\t"));
        assertFalse(Entities.isNamedEntity("\r"));
        assertFalse(Entities.isNamedEntity("\n"));
        
        // Exception handling
        try {
            Entities.isNamedEntity((Object) new Integer(42));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}