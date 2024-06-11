package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * EntitiesTest class to test all methods in the Entities class.
 *
 * @author yourname
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        for (String testString : new String[]{"", "&test;", "test&", "test < > & &amp; &quot; &apos; &lt; &gt;"}) {
            String escapedString = Entities.escape(testString, Document.OutputSettings.xhtml(), null);
            assertEquals(testString, Entities.unescape(escapedString));
        }

        String testString = "test &lt; &gt; &amp; &quot; &apos;";
        String expected = "test &lt; &gt; &amp; &quot; &apos;";
        String escapedString = Entities.escape(testString, Document.OutputSettings.xhtml(), null);
        assertEquals(expected, escapedString);

        String testString2 = "test &lt; &gt; &amp; &quot; &apos; <script>alert('test')</script> &";
        String expected2 = "test &lt; &gt; &amp; &quot; &apos; &lt;script&gt;alert('test')&lt;/script&gt; &amp;";
        String escapedString2 = Entities.escape(testString2, Document.OutputSettings.xhtml(), null);
        assertEquals(expected2, escapedString2);
    }

    @Test
    public void testUnescape() {
        String testString = "test &lt; &gt; &amp; &quot; &apos;";
        String expected = "test < > & \" '";
        String escapedString = "test &lt; &gt; &amp; &quot; &apos;";
        String unescapedString = Entities.unescape(escapedString);
        assertEquals(expected, unescapedString);

        String testString2 = "test &lt; &gt; &amp; &quot; &apos; <script>alert('test')</script> &";
        String expected2 = "test < > & \" ' <script>alert('test')</script> ";
        String escapedString2 = "test &lt; &gt; &amp; &quot; &apos; &lt;script&gt;alert('test')&lt;/script&gt; &amp;";
        String unescapedString2 = Entities.unescape(escapedString2);
        assertEquals(expected2, unescapedString2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNull() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNull() {
        Entities.getCharacterByName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithEmptyString() {
        Entities.getCharacterByName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithUnknownEntity() {
        Entities.getCharacterByName("unknown");
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> map = new HashMap<String, Character>();
        Entities.loadEntities("entities-base.properties", map);
        Map<Character, String> keyMap = Entities.toCharacterKey(map);
        for (Map.Entry<Character, String> entry : keyMap.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertEquals(name, Entities.getCharacterByName(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }
}