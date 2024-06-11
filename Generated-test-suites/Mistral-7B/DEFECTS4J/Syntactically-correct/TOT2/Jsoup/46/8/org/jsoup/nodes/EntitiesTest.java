import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;

import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty and null inputs
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known base named entities
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity(" quot"));
        assertTrue(Entities.isBaseNamedEntity("gt"));

        // Test for unknown named entities
        assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for empty and null inputs
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertEquals('>', Entities.getCharacterByName("gt"));

        // Test for unknown named entities
        assertNull(Entities.getCharacterByName("unknown"));

        // Test for empty and null inputs
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeMethod_Default() {
        String input = "This is a test string with & < > and \" characters.";
        String expectedOutput = "This is a test string with &amp; &lt; &gt; &quot; characters.";

        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethod_XHTML() {
        String input = "This is a test string with & < > and \" characters.";
        String expectedOutput = "This is a test string with &amp; &lt; &gt; &quot; characters.";

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);

        String output = Entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethod_InvalidOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(null);

        Entities.escape("", outputSettings);
    }

    @Test
    public void testCanEncode() {
        // Test for ASCII characters
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, ' '));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, '!'));

        // Test for non-ASCII characters
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'é'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'µ'));

        // Test for UTF characters
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'µ'));

        // Test for unknown character sets
        assertFalse(Entities.canEncode(null, 'é'));
    }
}