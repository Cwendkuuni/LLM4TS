package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};

        for (String name : names) {
            assertTrue(entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};

        for (String name : names) {
            assertTrue(entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};
        Character[] expected = {'<', '>', '&', '"'};

        for (int i = 0; i < names.length; i++) {
            Character actual = entities.getCharacterByName(names[i]);
            assertEquals(expected[i], actual);
        }
    }

    @Test
    public void testIsNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        String invalidName = "invalidEntity";
        assertFalse(entities.isNamedEntity(invalidName));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;<&gt;lorem &amp; ipsum&quot;";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        String expected = "&lt;&lt; &gt;&gt;lorem &amp;amp; ipsum &quot;&quot;";
        String result = entities.escape(input, out);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&lt;&lt; &gt;&gt;lorem &amp;amp; ipsum &quot;&quot;";
        String result = entities.unescape(input);

        assertEquals("&lt;<&gt;lorem &amp; ipsum&quot;", result);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        char c = 'é';
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = null; // no fallback encoder for this test

        assertTrue(entities.canEncode(charset, c, encoder));
    }

    @Test
    public void testCannotEncode() {
        Entities entities = new Entities();
        char c = '\uD800'; // high surrogate
        CoreCharset charset = CoreCharset.ascii;
        CharsetEncoder encoder = null;

        assertFalse(entities.canEncode(charset, c, encoder));
    }

}