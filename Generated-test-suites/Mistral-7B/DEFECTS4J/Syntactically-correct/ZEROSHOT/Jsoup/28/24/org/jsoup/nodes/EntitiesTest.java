package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Entities test file.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            assertTrue("Character '" + character + "' should be a named entity", Entities.isNamedEntity(Character.toString(character)));
        }

        assertFalse("Empty string should not be a named entity", Entities.isNamedEntity(""));
        assertFalse("Null should not be a named entity", Entities.isNamedEntity(null));
        assertFalse("Special character '&' should not be a named entity", Entities.isNamedEntity("&"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' should have character '" + character + "'", character, Entities.getCharacterByName(name));
            assertEquals("Character '" + character + "' should have named entity '" + name + "'", name, Entities.getCharacterByName(Character.toString(character)));
        }
    }

    @Test
    public void testEscape() {
        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            Map<Character, String> map = escapeMode.getMap();
            String source = "&lt;&gt;&amp;&apos;&quot;";
            String expected = "";

            switch (escapeMode) {
                case xhtml:
                    expected += "&lt;";
                    break;
                case base:
                    expected += "&lt;&gt;&amp;&apos;&quot;";
                    break;
                case extended:
                    expected += "&#60;&#62;&#38;&#39;&#34;";
                    break;
            }

            String result = Entities.escape(source, null, escapeMode);
            assertEquals("Escaping '" + source + "' with mode " + escapeMode + " should result in '" + expected + "'", expected, result);
        }
    }

    @Test
    public void testUnescape() {
        String source = "&lt;lt&gt;lt&amp;amp;amp;lt;&apos;apos&apos;&apos;&quot;quot&quot;";
        String expected = "<lt><lt>&amp;&amp;amp;<lt>'&apos;&apos;&apos;" + "\u00022";

        String result = Entities.unescape(source);
        assertEquals("Unescaping '" + source + "' should result in '" + expected + "'", expected, result);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull("entities-base.properties should be loaded", entities);
        assertEquals("entities-base.properties should have 8 entries", 8, entities.size());

        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' should have character '" + character + "'", character, Entities.getCharacterByName(name));
            assertEquals("Character '" + character + "' should have named entity '" + name + "'", name, Entities.getCharacterByName(Character.toString(character)));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<String, Character>() {{
            put("quot", '\u00022');
            put("amp", '\u00026');
            put("apos", '\u00027');
            put("lt", '\u0003C');
            put("gt", '\u0003E');
        }};

        Map<Character, String> result = Entities.toCharacterKey(entities);

        for (Map.Entry<Character, String> entry : result.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertEquals("Character '" + character + "' should have named entity '" + name + "'", name, Entities.getCharacterByName(Character.toString(character)));
            assertEquals("Named entity '" + name + "' should have character '" + character + "'", character, Entities.getCharacterByName(name));
        }
    }
}