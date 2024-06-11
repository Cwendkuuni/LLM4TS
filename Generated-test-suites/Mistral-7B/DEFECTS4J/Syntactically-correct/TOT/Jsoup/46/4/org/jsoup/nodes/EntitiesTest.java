import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // normal cases
        assertTrue(isNamedEntity("lt"));
        assertTrue(isNamedEntity("amp"));
        assertTrue(isNamedEntity("quot"));
        // edge cases
        assertFalse(isNamedEntity("gt")); // GT is not a named entity in this class
        assertFalse(isNamedEntity("x")); // X is not a named entity
        assertFalse(isNamedEntity("")); // Empty string is not a named entity
        // error cases
        try {
            isNamedEntity(null);
            fail("isNamedEntity should not accept null as argument");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        // normal cases
        assertTrue(isBaseNamedEntity("amp"));
        assertTrue(isBaseNamedEntity("lt"));
        assertTrue(isBaseNamedEntity("quot"));
        // edge cases
        assertFalse(isBaseNamedEntity("gt")); // GT is not a base named entity
        assertFalse(isBaseNamedEntity("x")); // X is not a base named entity
        assertFalse(isBaseNamedEntity("")); // Empty string is not a base named entity
        // error cases
        try {
            isBaseNamedEntity(null);
            fail("isBaseNamedEntity should not accept null as argument");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetCharacterByName() {
        // normal cases
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));
        assertEquals('"', getCharacterByName("quot"));
        // edge cases
        assertNull(getCharacterByName("gt")); // GT does not have a character representation in this class
        assertNull(getCharacterByName("x")); // X does not have a character representation
        assertNull(getCharacterByName("")); // Empty string does not have a character representation
        // error cases
        try {
            getCharacterByName(null);
            fail("getCharacterByName should not accept null as argument");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEscapeMethod() {
        // normal cases
        String input = "The quick brown fox jumps over the lazy dog.";
        String output = escape(input, new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("The quick brown fox jumps over the lazy dog.", input);
        assertEquals("The quick brown fox jumps over the lazy dog.", unescape(output));
        // edge cases
        String inputWithNamedEntities = "The &lt;strong&gt;quick&lt;/strong&gt; brown fox jumps over the &amp;lazy&amp; dog.";
        String outputWithNamedEntities = escape(inputWithNamedEntities, new Document.OutputSettings().escapeMode(EscapedMode.base));
        assertEquals("The &amp;lt;strong&amp;gt;quick&amp;amp;gt;&lt;/strong&amp;gt; brown fox jumps over the &amp;amp;lazy&amp;amp;gt; dog.", outputWithNamedEntities);
        assertEquals("The <strong>quick</strong> brown fox jumps over the lazy dog.", unescape(outputWithNamedEntities));
        // error cases
        try {
            escape(null, new Document.OutputSettings());
            fail("escape should not accept null as argument");
        } catch (NullPointerException e) {
            // expected
        }
    }
}