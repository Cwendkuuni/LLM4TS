package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            assertTrue(Entities.isNamedEntity(entry.getValue()));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testXhtmlByVal() {
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        Map<Character, Character> expectedXhtmlEntities = new HashMap<>();
        expectedXhtmlEntities.put('&', "amp");
        expectedXhtmlEntities.put('<', "lt");
        expectedXhtmlEntities.put('>', "gt");
        expectedXhtmlEntities.put('"', "quot");
        expectedXhtmlEntities.put("'", "apos");

        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            assertEquals(expectedXhtmlEntities.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void testBaseByVal() {
        Map<Character, String> baseEntities = Entities.baseByVal;
        Map<Character, Character> expectedBaseEntities = new HashMap<>();
        expectedBaseEntities.put('&', '&');
        expectedBaseEntities.put('<', '<');
        expectedBaseEntities.put('>', '>');
        expectedBaseEntities.put('"', '"');
        expectedBaseEntities.put("'", '\'');

        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            assertEquals(expectedBaseEntities.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
        assertEquals(xhtmlEntities(), map.keySet());
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> map = Entities.EscapeMode.base.getMap();
        assertEquals(baseEntities(), map.keySet());
    }

    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> map = Entities.EscapeMode.extended.getMap();
        assertEquals(fullEntities(), map.keySet());
    }

    @Test
    public void testEscape() {
        String input = "&amp; <lt> <gt> &apos; &quot;";
        String expectedOutput = "&amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot;";

        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;gt; &amp;quot; &apos; &amp;#x26; &amp;#x3C3;";
        String expectedOutput = "<>&\" '&#x26;Õ";

        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&amp;lt; &amp;gt; &amp;quot; &apos; &amp;#x26; &amp;#x3C3;";
        String expectedOutput = "<>&\" '&amp;#x26;Õ";

        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesMissingResource() {
        Entities.loadEntities("missing.properties");
    }

    private Map<Character, Character> baseEntities() {
        Map<Character, Character> baseEntities = new HashMap<>();
        baseEntities.put('&', '&');
        baseEntities.put('<', '<');
        baseEntities.put('>', '>');
        baseEntities.put('"', '"');
        baseEntities.put("'", '\'');
        return baseEntities;
    }

    private Map<Character, String> xhtmlEntities() {
        Map<Character, String> xhtmlEntities = new HashMap<>();
        xhtmlEntities.put('&', "amp");
        xhtmlEntities.put('<', "lt");
        xhtmlEntities.put('>', "gt");
        xhtmlEntities.put('"', "quot");
        xhtmlEntities.put("'", "apos");
        return xhtmlEntities;
    }

    private Map<Character, String> fullEntities() {
        Map<Character, String> fullEntities = new HashMap<>();
        fullEntities.putAll(Entities.xhtmlByVal);
        fullEntities.putAll(Entities.base);
        return fullEntities;
    }
}