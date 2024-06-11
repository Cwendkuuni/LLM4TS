import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));

        // Test for unknown named entities
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "Test <entity> &amp; &lt; &gt; &apos; char.";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "Test &amp;amp; &amp;lt; &amp;lt; &amp;gt; &amp;apos; char.";

        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testUnescape() {
        String input = "Test &amp;lt; &amp;gt; &amp;apos; &amp;#13; &amp;#x2032; char.";
        String expectedOutput = "Test <gt> >apos> \u000D \u2032 char.";

        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testEscapeWithDifferentEscapeModes() {
        String input = "Test <entity> &amp; &lt; &gt; &apos; char.";

        // Test for xhtml escape mode
        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.setEscapeMode(Entities.Entities.EscapeMode.xhtml);
        String xhtmlExpectedOutput = "Test &amp;amp; &amp;lt; &amp;lt; &amp;gt; &amp;apos; char.";

        assertEquals(xhtmlExpectedOutput, Entities.escape(input, xhtmlSettings, Entities.Entities.EscapeMode.xhtml));

        // Test for base escape mode
        Document.OutputSettings baseSettings = new Document.OutputSettings();
        String baseExpectedOutput = "Test &lt; &gt; &apos; &amp;#13; &amp;#x2032; char.";

        assertEquals(baseExpectedOutput, Entities.escape(input, baseSettings, Entities.Entities.EscapeMode.base));

        // Test for extended escape mode
        Document.OutputSettings extendedSettings = new Document.OutputSettings();
        String extendedExpectedOutput = "Test &#60; &#62; &#39; &#13; &#x2032; char.";

        assertEquals(extendedExpectedOutput, Entities.escape(input, extendedSettings, Entities.Entities.EscapeMode.extended));
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsNamedEntityWithNull() {
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByNameWithEmptyString() {
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testGetCharacterByNameWithNull() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeWithEmptyString() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = "";

        assertEquals("", Entities.escape(input, settings));
    }

    @Test
    public void testEscapeWithNull() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = null;

        assertNull(Entities.escape(input, settings));
    }

    @Test
    public void testUnescapeWithEmptyString() {
        String input = "";

        assertEquals("", Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithNull() {
        String input = null;

        assertNull(Entities.unescape(input));
    }
}