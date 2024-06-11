import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        String namedEntity = "lt";
        assertTrue(Entities.isNamedEntity(namedEntity));
        assertEquals(Character.forInteger(0x003C), Entities.getCharacterByName(namedEntity));
    }

    @Test
    public void testIsNamedEntityInvalid() {
        Map<String, Character> full = Entities.full;
        String invalidEntity = "invalid";
        assertFalse(Entities.isNamedEntity(invalidEntity));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        Character baseNamedEntity = '&';
        assertTrue(Entities.isBaseNamedEntity(baseNamedEntity.toString()));
        assertEquals(baseNamedEntity, Entities.getCharacterByName(baseNamedEntity.toString()));
    }

    @Test
    public void testIsBaseNamedEntityInvalid() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        Character invalidNamedEntity = 'X';
        assertFalse(Entities.isBaseNamedEntity(invalidNamedEntity.toString()));
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        StringBuilder accumulator = new StringBuilder();
        String input = "&Hello &World! < > &\"";
        Entities.escape(accumulator, input, null, false, false, false);
        String expectedOutput = "&amp;Hello &amp;amp;World! &amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        StringBuilder accumulator = new StringBuilder();
        String input = "&lt; &gt; &amp; &quot;";
        Entities.escape(accumulator, input, null, true, false, false);
        String expectedOutput = "&amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, null, null, false, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullOutput() {
        StringBuilder accumulator = null;
        String input = "&Hello &World! < > &\"";
        Entities.escape(accumulator, input, null, false, false, false);
    }

    @Test
    public void testUnescapeMethodWithDefaultSettings() {
        String input = "&amp;Hello &amp;amp;World! &amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        String expectedOutput = "&Hello &World! < > & &\"";
        assertEquals(expectedOutput, Entities.unescape(input, false));
    }

    @Test
    public void testUnescapeMethodWithStrictSettings() {
        String input = "&amp;Hello &amp;amp;World! &lt; &gt; &amp;amp; &amp;quot;";
        String expectedOutput = "Hello World! < > & \"";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullInput() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = Entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, actualOutput);
        String actualOutputStrict = Entities.unescape(input, true);
        Assert.assertEquals(expectedOutput, actualOutputStrict);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = Character.encodingPreferredAvailable(coreCharset.name());
        for (char c = ' '; c <= '~'; c++) {
            assertTrue(Entities.canEncode(coreCharset, c, encoder));
        }

        for (char c = '\u0080'; c <= '\u009F'; c++) {
            assertTrue(Entities.canEncode(coreCharset, c, encoder));
        }

        char c = '\u00A0';
        assertFalse(Entities.canEncode(coreCharset, c, encoder));
    }

    @Test
    public void testLoadEntities() {
        String filename = "entities-base.properties";
        Map<String, Character> entities = Entities.loadEntities(filename);
        Character entity = Character.forInteger(0x0003C);
        assertTrue(entities.containsKey(entity.toString()));
        assertEquals(entity, entities.get(entity.toString()));
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesInvalid() {
        String filename = "invalid.properties";
        Entities.loadEntities(filename);
    }
}