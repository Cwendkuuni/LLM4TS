package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test isNamedEntity method with invalid named entities
        assertFalse(Entities.isNamedEntity("invalidEntity"));

        // Test isNamedEntity method with empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method with valid named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals("'", Entities.getCharacterByName("apos"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test getCharacterByName method with invalid named entities
        assertNull(Entities.getCharacterByName("invalidEntity"));

        // Test getCharacterByName method with empty string
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Test escape method with different input strings and output settings
        String inputString1 = "This is a test string with <special> characters: &lt; &gt; &amp; &apos; &quot;";
        String expectedOutput1_xhtml = "This is a test string with &lt; &gt; &amp; &apos; &quot;";
        String expectedOutput1_base = "This is a test string with &#60; &#62; &#38; &#39; &#34;";
        String expectedOutput1_extended = "This is a test string with &lt; &gt; &amp; &apos; &quot;";

        String inputString2 = "This is another test string without any special characters.";
        String expectedOutput2 = inputString2;

        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            String output = Entities.escape(inputString1, null, escapeMode);
            assertEquals(escapeMode == Entities.EscapeMode.xhtml ? expectedOutput1_xhtml : (escapeMode == Entities.EscapeMode.base ? expectedOutput1_base : expectedOutput1_extended), output);
            String output2 = Entities.escape(inputString2, null, escapeMode);
            assertEquals(expectedOutput2, output2);
        }
    }

    @Test
    public void testUnescape() {
        // Test unescape method with different input strings and strict mode
        String inputString1 = "This is a test string with &lt; &gt; &amp; &apos; &quot;";
        String expectedOutput1 = "This is a test string with <special> characters: &lt; &gt; &amp; &apos; &quot;";

        String inputString2 = "This is another test string without any special characters.";
        String expectedOutput2 = inputString2;

        String output1_xhtml = Entities.unescape(expectedOutput1, true);
        assertEquals(inputString1, output1_xhtml);

        String output1_base = Entities.unescape(expectedOutput1, false);
        assertEquals(inputString1, output1_base);

        String output2 = Entities.unescape(expectedOutput2, false);
        assertEquals(expectedOutput2, output2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        Entities.unescape(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithInvalidInput() {
        Entities.unescape("This is an invalid input string", false);
    }
}