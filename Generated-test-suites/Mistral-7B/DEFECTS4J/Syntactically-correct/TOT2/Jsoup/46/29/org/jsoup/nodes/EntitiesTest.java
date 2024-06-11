import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic functionality test
        assertTrue(isNamedEntity("lt"));
        // Edge case: checking for an unknown entity
        assertFalse(isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic functionality test
        assertTrue(isBaseNamedEntity("amp"));
        // Edge case: checking for an unknown entity
        assertFalse(isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Basic functionality test
        assertEquals('&', getCharacterByName("amp"));
        // Edge case: checking for an unknown entity
        assertNull(getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String originalString = "Hello & World! <> \"";
        Document.OutputSettings out = new Document.OutputSettings();

        // Basic functionality test
        assertEquals("Hello &amp; World! &lt;&gt; &quot;", escape(originalString, out));

        // Edge case: testing with an empty string
        assertEquals("", escape("", out));

        // Edge case: testing with null string
        assertNull(escape(null, out));

        // Exception handling: checking for null output settings
        try {
            escape(null, "test", null, false, false, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testUnescape() {
        String escapedString = "Hello &amp; World! &lt;&gt; &quot;";

        // Basic functionality test
        assertEquals("Hello & World! <> \"", unescape(escapedString));

        // Edge case: testing with an empty string
        assertEquals("", unescape(""));

        // Edge case: testing with null string
        assertNull(unescape(null));

        // Edge case: testing with an empty list of entities
        assertNull(unescape("", new ArrayList<>()));

        // Edge case: testing with a list containing an unknown entity
        assertNull(unescape("&unknown", new ArrayList<>()));

        // Edge case: testing with a list containing a special entity
        assertEquals("Hello & World! <> \"", unescape(escapedString, Arrays.asList("amp", "lt", "gt", "quot")));
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        // Basic functionality test
        assertTrue(canEncode(CoreCharset.utf, 'a', encoder));

        // Edge case: testing with a non-ASCII character
        assertTrue(canEncode(CoreCharset.utf, 'é', encoder));

        // Edge case: testing with a surrogate pair
        assertFalse(canEncode(CoreCharset.utf, '\uD800', encoder));

        // Exception handling: checking for null encoder
        try {
            canEncode(CoreCharset.utf, 'a', null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadEntities() {
        Properties properties = loadEntities("entities-base.properties");

        // Basic functionality test
        Map<String, Character> entities = properties;
        assertEquals(Character.valueOf('&'), entities.get("amp"));
        assertEquals(Character.valueOf('<'), entities.get("lt"));

        // Edge case: testing with a file not found
        try {
            loadEntities("nonexistentfile.properties");
            fail("Expected a MissingResourceException");
        } catch (MissingResourceException e) {
            // expected
        }
    }

}