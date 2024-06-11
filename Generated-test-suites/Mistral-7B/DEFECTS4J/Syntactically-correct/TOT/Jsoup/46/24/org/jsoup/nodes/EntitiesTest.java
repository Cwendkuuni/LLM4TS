import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in base entity set
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test for unknown named entities
        assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for empty string
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for unknown named entities
        assertNull(Entities.getCharacterByName("unknown"));

        // Test for empty string
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Test escaping a simple string with no special characters
        String input = "Hello World!";
        String expected = "Hello World!";
        String output = Entities.escape(input, null);
        assertEquals(expected, output);

        // Test escaping a string with special characters
        input = "& < > \"";
        expected = "&amp; &lt; &gt; &quot;";
        output = Entities.escape(input, null);
        assertEquals(expected, output);

        // Test escaping a string with special characters in an attribute
        input = "<script src='script.js'>";
        expected = "&lt;script src=&quot;script.js&quot;&gt;";
        output = Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        // Test unescaping a simple string with no special characters
        String input = "Hello World!";
        String expected = "Hello World!";
        String output = Entities.unescape(input);
        assertEquals(expected, output);

        // Test unescaping a string with special characters
        input = "&amp; &lt; &gt; &quot;";
        expected = "& &lt; &gt; \"";
        output = Entities.unescape(input);
        assertEquals(expected, output);

        // Test unescaping a string with entities in an attribute
        input = "&lt;script src=&quot;script.js&quot;&gt;";
        expected = "<script src=\"script.js\">";
        output = Entities.unescape(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullString() {
        Entities.escape(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullString() {
        Entities.unescape(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullOutputSettings() {
        Entities.escape("", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        Entities.unescape(null);
    }
}