package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Test escape method
        String input = "&lt;Hello&amp;World&gt;";
        String expected = "&amp;lt;Hello&amp;amp;amp;amp;World&amp;gt;";

        String output = Entities.escape(input, null, Entities.EscapeMode.extended);
        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        // Test unescape method
        String input = "&amp;lt;Hello&amp;amp;amp;amp;World&amp;gt;";
        String expected = "&lt;HelloWorld&gt;";

        String output = Entities.unescape(input);
        assertEquals(expected, output);
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        // Test isNamedEntity method with empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByNameWithNullName() {
        // Test getCharacterByName method with null name
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testGetCharacterByNameWithInvalidName() {
        // Test getCharacterByName method with invalid name
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testLoadEntities() {
        // Test loadEntities method
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.getCharacterByName("quot"), entities.get("quot"));
        assertEquals(Entities.getCharacterByName("apos"), entities.get("apos"));
        assertEquals(Entities.getCharacterByName("amp"), entities.get("amp"));
        assertEquals(Entities.getCharacterByName("lt"), entities.get("lt"));
        assertEquals(Entities.getCharacterByName("gt"), entities.get("gt"));
    }

    @Test
    public void testToCharacterKey() {
        // Test toCharacterKey method
        Map<String, Character> entities = new HashMap<>();
        entities.put("quot", '\u00022');
        entities.put("amp", '\u00026');
        entities.put("apos", '\u00027');
        entities.put("lt", '\u0003C');
        entities.put("gt", '\u0003E');

        Map<Character, String> reversedEntities = Entities.toCharacterKey(entities);
        assertEquals("quot", reversedEntities.get('\u00022'));
        assertEquals("amp", reversedEntities.get('\u00026'));
        assertEquals("apos", reversedEntities.get('\u00027'));
        assertEquals("lt", reversedEntities.get('\u0003C'));
        assertEquals("gt", reversedEntities.get('\u0003E'));
    }
}