import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: empty string
        assertFalse(Entities.isNamedEntity(""));

        // Edge case: null input
        assertFalse(Entities.isNamedEntity(null));

        // Exception handling: invalid character
        assertFalse(Entities.isNamedEntity("!"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test
        assertTrue(Entities.isBaseNamedEntity("amp"));

        // Edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Edge case: null input
        assertFalse(Entities.isBaseNamedEntity(null));

        // Edge case: non-base entity
        assertFalse(Entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        // Basic test
        assertEquals(34, Entities.getCharacterByName("quot").charValue());

        // Edge case: invalid name
        assertNull(Entities.getCharacterByName("invalid"));

        // Edge case: null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeMethod() {
        // Basic test
        String originalString = "&amp;<tag>&quot;";
        String escapedString = Entities.escape(originalString, null);
        String expectedEscapedString = "&amp;amp;(&amp;lt;)type(&amp;quot;)";
        assertEquals(expectedEscapedString, escapedString);

        // Edge case: empty string
        String emptyString = Entities.escape("", null);
        assertEquals("", emptyString);

        // Edge case: null input
        String nullInput = Entities.escape(null, null);
        assertNull(nullInput);

        // Exception handling: null escapeMode
        try {
            Entities.escape(null, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testUnescapeMethod() {
        // Basic test
        String originalString = "&amp;amp;(&amp;lt;)type(&amp;quot;)";
        String unescapedString = Entities.unescape(originalString);
        String expectedUnescapedString = "&amp;<tag>&quot;";
        assertEquals(expectedUnescapedString, unescapedString);

        // Edge case: empty string
        String emptyString = Entities.unescape("", false);
        assertEquals("", emptyString);

        // Edge case: null input
        String nullInput = Entities.unescape(null, false);
        assertNull(nullInput);

        // Edge case: strict parsing
        String strictString = "&amp;gt;<tag>&amp;quot;";
        String unescapedStringStrict = Entities.unescape(strictString, true);
        String expectedUnescapedStringStrict = "<tag>&quot;";
        assertEquals(expectedUnescapedStringStrict, unescapedStringStrict);

        // Exception handling: null input
        try {
            Entities.unescape(null, false);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCanEncodeMethod() {
        // Basic test
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));

        // Edge case: invalid charset
        assertFalse(Entities.canEncode(null, 'a'));

        // Edge case: null character
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, null));
    }

    @Test
    public void testCoreCharsetEnum() {
        // Basic test
        assertEquals(Entities.CoreCharset.ascii, Entities.CoreCharset.byName("ASCII"));

        // Edge case: invalid charset name
        assertNull(Entities.CoreCharset.byName("INVALID"));

        // Edge case: null charset name
        assertNull(Entities.CoreCharset.byName(null));
    }
}