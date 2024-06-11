import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    // Add test cases for the remaining methods...
}