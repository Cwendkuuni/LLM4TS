import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities in the full entity set
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test for unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in the base entity set
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test for unknown named entity
        assertFalse(Entities.isBaseNamedEntity("lt"));

        // Test for empty string
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities in the full entity set
        assertEquals(34, Entities.getCharacterByName("quot").intValue());
        assertEquals(60, Entities.getCharacterByName("lt").intValue());
        assertEquals(62, Entities.getCharacterByName("gt").intValue());
        assertEquals(38, Entities.getCharacterByName("amp").intValue());

        // Test for unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));

        // Test for empty string
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Test for simple cases
        assertEquals("&quot;", Entities.escape("\"", null));
        assertEquals("&amp;", Entities.escape("&", null));
        assertEquals("&lt;", Entities.escape("<", null));
        assertEquals("&gt;", Entities.escape(">", null));

        // Test for whitespace
        assertEquals("&nbsp;", Entities.escape(" ", null));
        assertEquals("&nbsp;&nbsp;", Entities.escape("  ", null));

        // Test for encoded characters
        assertEquals("&#123;", Entities.escape("\u007B", null));
        assertEquals("&#x7B;", Entities.escape("\u007B", null));

        // Test for invalid characters
        assertEquals("&#x0;", Entities.escape("\u0000", null));

        // Test for inAttribute and normaliseWhite flags
        assertEquals("foo&quot;bar", Entities.escape("foo\"bar", null, true, false, true));
        assertEquals("foo &quot;bar", Entities.escape("foo &\"bar", null, true, false, true));
        assertEquals("foo &nbsp;bar", Entities.escape("foo \tbar", null, true, false, true));
        assertEquals("foo &nbsp;&nbsp;bar", Entities.escape("foo \t\tbar", null, true, false, true));
    }

    @Test
    public void testUnescape() {
        // Test for simple cases
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));

        // Test for whitespace
        assertEquals(" ", Entities.unescape("&nbsp;"));

        // Test for invalid entities
        assertEquals("", Entities.unescape("&#x0;"));

        // Test for strict flag
        assertEquals("&amp;", Entities.unescape("&"));
    }

    @Test
    public void testCanEncode() {
        // Test for common character sets
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, ' '));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, ' '));

        // Test for non-ASCII characters
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u03B4'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\u03B4'));

        // Test for invalid character
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\uFFFF'));
    }
}