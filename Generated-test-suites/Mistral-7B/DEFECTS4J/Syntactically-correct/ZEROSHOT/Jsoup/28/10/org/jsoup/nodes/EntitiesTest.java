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
 * JUnit 4 test file for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity_TrueCases() {
        // Testing isNamedEntity method with known named entities.
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(namedEntity));
            assertEquals(character, Entities.getCharacterByName(namedEntity));
        }
    }

    @Test
    public void testIsNamedEntity_FalseCases() {
        // Testing isNamedEntity method with invalid named entities.
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("123"));
        assertFalse(Entities.isNamedEntity("\u0000"));
    }

    @Test
    public void testGetCharacterByName_ValidCases() {
        // Testing getCharacterByName method with valid named entities.
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(namedEntity));
        }
    }

    @Test
    public void testGetCharacterByName_InvalidCases() {
        // Testing getCharacterByName method with invalid named entities.
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName("123"));
        assertNull(Entities.getCharacterByName("\u0000"));
    }

    @Test
    public void testEscape_ValidCases() {
        // Testing escape method with different InputSettings.
        String testString = "This is a test string & with special entities: &lt;&gt;&amp;&apos;&quot;";

        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.setEncoder(Character.isDefined((char) 0x0003C) ? Character.encodingFor(0x0003C) : null);
        xhtmlSettings.setEscapeMode(Entities.EscapeMode.xhtml);

        String xhtmlEscaped = Entities.escape(testString, xhtmlSettings);
        assertEquals("This is a test string &lt;&amp;&apos;&quot; &amp;gt;", xhtmlEscaped);

        Document.OutputSettings baseSettings = new Document.OutputSettings();
        baseSettings.setEncoder(null);
        baseSettings.setEscapeMode(Entities.EscapeMode.base);

        String baseEscaped = Entities.escape(testString, baseSettings);
        assertEquals("This is a test string &amp;&lt; &gt; &amp;amp; &apos; &quot;", baseEscaped);

        Document.OutputSettings extendedSettings = new Document.OutputSettings();
        extendedSettings.setEncoder(null);
        extendedSettings.setEscapeMode(Entities.EscapeMode.extended);

        String extendedEscaped = Entities.escape(testString, extendedSettings);
        assertEquals("This is a test string &amp;#60; &amp;#62; &amp;#38; &apos; &quot;", extendedEscaped);
    }

    @Test
    public void testEscape_InvalidCases() {
        // Testing escape method with invalid or null input.
        String nullInput = null;
        String emptyInput = "";
        String invalidInput = "This is an invalid input & string";

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEncoder(Character.isDefined((char) 0x0003C) ? Character.encodingFor(0x0003C) : null);
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);

        String nullEscaped = Entities.escape(nullInput, outputSettings);
        assertNull(nullEscaped);

        String emptyEscaped = Entities.escape(emptyInput, outputSettings);
        assertEquals("", emptyEscaped);

        String invalidEscaped = Entities.escape(invalidInput, outputSettings);
        assertEquals(invalidInput, invalidEscaped);
    }

    @Test
    public void testUnescape_ValidCases() {
        // Testing unescape method with valid inputs.
        String testString = "This is a test string &amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;";

        String xhtmlUnescaped = Entities.unescape(testString, false);
        assertEquals("This is a test string < & > &amp;", xhtmlUnescaped);

        String baseUnescaped = Entities.unescape(testString, true); // strict unescape
        assertEquals("This is a test string &lt; &gt; &amp; &apos; &quot;", baseUnescaped);

        String extendedUnescaped = Entities.unescape(testString, false);
        assertEquals("This is a test string &#60; &#62; &#38; &apos; &quot;", extendedUnescaped);
    }

    @Test
    public void testUnescape_InvalidCases() {
        // Testing unescape method with invalid inputs.
        String nullInput = null;
        String emptyInput = "";
        String invalidInput = "This is an invalid input & string";

        String nullUnescaped = Entities.unescape(nullInput, false);
        assertNull(nullUnescaped);

        String emptyUnescaped = Entities.unescape(emptyInput, false);
        assertEquals("", emptyUnescaped);

        String invalidUnescaped = Entities.unescape(invalidInput, false);
        assertEquals(invalidInput, invalidUnescaped);
    }
}