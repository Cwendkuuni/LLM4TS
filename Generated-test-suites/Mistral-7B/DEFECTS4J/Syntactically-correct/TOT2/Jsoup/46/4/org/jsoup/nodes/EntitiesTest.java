import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertFalse(entities.isNamedEntity("gt")); // edge case: not a named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("gt")); // edge case: not a base named entity
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertNull(entities.getCharacterByName("gt")); // edge case: not a named entity
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, "Hello & World! < tag >", null);
        String expectedOutput = "Hello &amp; World! &lt; tag &gt;";

        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings xhtmlSettings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        Entities.escape(accumulator, "Hello & World! < tag >", xhtmlSettings);
        String expectedOutput = "Hello &amp; World! &lt;tag&gt;";

        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeMethodNullStringInput() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodEmptyStringInput() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, "", null);
    }

    @Test
    public void testCanEncodeFunctionality() {
        Character c = '€';
        CoreCharset charset = CoreCharset.utf;
        boolean result = Entities.canEncode(charset, c, null);
        assertTrue(result);
    }

    @Test
    public void testCanEncodeEdgeCases() {
        Character c = 0x1F00;
        CoreCharset charset = CoreCharset.ascii;
        boolean result = Entities.canEncode(charset, c, null);
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCanEncodeNullCharInput() {
        Character c = null;
        CoreCharset charset = CoreCharset.utf;
        boolean result = Entities.canEncode(charset, c, null);
    }

    @Test
    public void testUnescapeMethodWithDefaultSettings() {
        String input = "Hello &amp; World! &lt;tag&gt;";
        String expectedOutput = "Hello & World! <tag>";
        String output = Entities.unescape(input, false);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodWithStrictSettings() {
        String input = "Hello &amp; World! &lt;tag&gt;";
        String expectedOutput = "Hello &amp; World! &lt;tag&gt;";
        String output = Entities.unescape(input, true);

        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodNullStringInput() {
        String input = null;
        String output = Entities.unescape(input, false);
    }
}