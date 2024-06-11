package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertTrue(Entities.isNamedEntity(name.toUpperCase()));
            assertTrue(Entities.isNamedEntity(Character.toString(key)));
            assertTrue(Entities.isNamedEntity(Character.toString(key).toUpperCase()));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity(null));

        // Test edge cases
        String nameWithNumber = "lt5";
        assertFalse(Entities.isNamedEntity(nameWithNumber));
        String nameWithSpace = "lt  ";
        assertFalse(Entities.isNamedEntity(nameWithSpace));
        String nameWithAsterisk = "lt*";
        assertFalse(Entities.isNamedEntity(nameWithAsterisk));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            assertEquals(key, Entities.getCharacterByName(name));
            assertEquals(key, Entities.getCharacterByName(Character.toString(key)));
            assertEquals(key, Entities.getCharacterByName(name.toUpperCase()));
            assertEquals(key, Entities.getCharacterByName(name.toUpperCase().charAt(0) + name.substring(1)));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(" "));
        assertNull(Entities.getCharacterByName(null));

        // Test edge cases
        String nameWithNumber = "lt5";
        assertNull(Entities.getCharacterByName(nameWithNumber));
        String nameWithSpace = "lt  ";
        assertNull(Entities.getCharacterByName(nameWithSpace));
        String nameWithAsterisk = "lt*";
        assertNull(Entities.getCharacterByName(nameWithAsterisk));
    }

    @Test
    public void testEscape() {
        // Test escape method
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEncoder(Character.isDefinedChar(0x1F000) ? Character. encodingForUnicode("UTF-16LE") : Character. encodingForUnicode("UTF-8"));
        outputSettings.setEscapeMode(Entities.EscapeMode.extended);

        String input = "This string contains some &, <, >, ' and \" entities to test.";
        String expected = "This string contains some &amp;, &lt;, &gt;, &apos; and &quot; entities to test.";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        // Test different escape modes
        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        expected = "This string contains some &amp;amp;, &lt;lt;, &gt;gt;, &apos;apos; and &quot;quot; entities to test.";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        expected = "This string contains some &amp;, &lt;, &gt;, &apos; and &quot; entities to test.";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        // Test different character encodings
        outputSettings.setEncoder(Character. encodingForUnicode("UTF-16BE"));
        expected = "This string contains some &#x26amp;, &#x26lt;, &#x26gt;, &#x27apos; and &#x27quot; entities to test.";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        outputSettings.setEncoder(Character. encodingForUnicode("UTF-32BE"));
        expected = "This string contains some &#x10000026;, &#x10000026;lt;, &#x10000026;gt;, &#x10000027;apos; and &#x10000027;quot; entities to test.";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        // Test unescape method
        String input = "This string contains some &amp;lt;gt;, &apos; and &quot; entities to test.";
        String expected = "This string contains some <>,\' and \" entities to test.";
        String output = Entities.unescape(input);
        assertEquals(expected, output);

        // Test different modes
        String inputStrict = "This string contains some &amp;#x26;lt;gt;, &apos; and &quot; entities to test.";
        String expectedStrict = "This string contains some <>,\' and \" entities to test.";
        String outputStrict = Entities.unescape(inputStrict, true);
        assertEquals(expectedStrict, outputStrict);

        String inputUnescaped = "This string contains some &#x26;lt;gt;, &apos; and &quot; entities to test.";
        String expectedUnescaped = "This string contains some <>,\' and \" entities to test.";
        String outputUnescaped = Entities.unescape(inputUnescaped);
        assertEquals(expectedUnescaped, outputUnescaped);

        // Test invalid entities
        String inputInvalid = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String outputInvalid = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String expectedInvalid = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String outputInvalidStrict = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String expectedInvalidStrict = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String outputInvalidUnescaped = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String expectedInvalidUnescaped = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String outputInvalidStrictUnescaped = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        String expectedInvalidStrictUnescaped = "This string contains some &amp;xyz and &amp;xyz entities to test.";
        assertEquals(expectedInvalid, Entities.unescape(inputInvalid));
        assertEquals(expectedInvalidStrict, Entities.unescape(inputInvalid, true));
        assertEquals(expectedInvalidUnescaped, Entities.unescape(inputInvalid, false));
        assertEquals(expectedInvalidStrictUnescaped, Entities.unescape(inputInvalid, true, false));
    }

    @Test
    public void testIsNamedEntityWithEmptyMap() {
        // Test isNamedEntity method with empty map
        Map<Character, String> emptyMap = new HashMap<>();
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("", emptyMap));
        assertFalse(Entities.isNamedEntity(" ", emptyMap));
        assertFalse(Entities.isNamedEntity(null, emptyMap));
    }

    @Test
    public void testGetCharacterByNameWithEmptyMap() {
        // Test getCharacterByName method with empty map
        Map<Character, String> emptyMap = new HashMap<>();
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(" "));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("", emptyMap));
        assertNull(Entities.getCharacterByName(" ", emptyMap));
        assertNull(Entities.getCharacterByName(null, emptyMap));
    }
}