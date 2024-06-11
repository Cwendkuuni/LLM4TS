import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.getFullMap();
        // Test with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        // Test with invalid named entities
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.getBaseMap();
        // Test with valid base named entities
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        // Test with invalid base named entities
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.getFullMap();
        // Test with valid named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        // Test with invalid named entities
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        StringBuilder expected = new StringBuilder("<h1>Hello & World!</h1>");
        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, expected.toString());
        assertEquals("<h1>Hello &amp; World!</h1>", actual.toString());
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        StringBuilder expected = new StringBuilder("<h1>Hello &lt; World!</h1>");
        StringBuilder actual = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        Entities.escape(actual, expected.toString(), settings);
        assertEquals("<h1>Hello &lt; World!</h1>", actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithInvalidOutputSettings() {
        Document.OutputSettings settings = new Document.OutputSettings(-1);
        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, "Hello World!", settings);
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;";
        String expected = "<h1>Hello & World!</h1>";
        String actual = Entities.unescape(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithInvalidInput() {
        String input = "&invalidEntity;";
        String actual = Entities.unescape(input);
    }
}