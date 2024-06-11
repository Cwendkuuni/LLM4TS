import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test case for isNamedEntity method with valid named entity
        assertTrue(Entities.isNamedEntity("lt"));

        // Test case for isNamedEntity method with invalid named entity
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test case for isBaseNamedEntity method with valid named entity in base set
        assertTrue(Entities.isBaseNamedEntity("amp"));

        // Test case for isBaseNamedEntity method with invalid named entity
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test case for getCharacterByName method with valid named entity
        assertEquals(Character.forInt(0x0003C), Entities.getCharacterByName("lt"));

        // Test case for getCharacterByName method with invalid named entity
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        // Test case for escape method with normal string and default settings
        assertEquals("&lt;Hello&gt; World&quot;", Entities.escape("<Hello> World\"", null));

        // Test case for escape method with string in an attribute and normalize white spaces
        assertEquals(" <p class=\"myClass\">&lt;script src=\"script.js\"&gt;</p>", Entities.escape("<p class=\"myClass\"> <script src=\"script.js\"></script>", null, true, true, false));

        // Test case for escape method with string containing surrogate pairs
        assertEquals("𠮷𠮷𠮷", Entities.escape("𠮷𠮷𠮷", null));
    }

    @Test
    public void testUnescape() {
        // Test case for unescape method with a simple string
        assertEquals("<Hello> World\"", Entities.unescape("&lt;Hello&gt; World&quot;"));

        // Test case for unescape method with a string containing named entities
        assertEquals("<script src=\"script.js\"></script>", Entities.unescape("&lt;script src=&quot;script.js&quot;>&gt;"));
    }
}