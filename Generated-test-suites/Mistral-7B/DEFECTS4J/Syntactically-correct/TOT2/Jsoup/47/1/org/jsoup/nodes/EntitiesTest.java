package org.jsoup.nodes.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known base named entities
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test for unknown named entities
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for unknown named entities
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;Hello&gt; World &amp; &quot; &apos;";
        String expectedOutput = "&amp;lt;Hello&amp;gt; &amp;amp; &amp;quot; &amp;apos;";

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, input, null, false, false, false);

        String output = accum.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        // Test for encodable ASCII characters
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, ' '));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, '!'));

        // Test for non-ASCII characters using UTF-8 encoding
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', encoder));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '𠮷', encoder));

        // Test for unencodable characters
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uD800')); // High Surrogate
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uDC00')); // Low Surrogate
    }
}