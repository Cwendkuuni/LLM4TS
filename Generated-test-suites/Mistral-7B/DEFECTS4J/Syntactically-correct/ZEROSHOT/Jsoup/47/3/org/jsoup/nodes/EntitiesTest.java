package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Test class for Entities.java
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();

        // Test with valid named entities
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("gt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("quot"));

        // Test with invalid named entities
        assertFalse(entities.isNamedEntity("invalidEntity"));
        assertFalse(entities.isNamedEntity(""));
        assertFalse(entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();

        // Test with valid base named entities
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("gt"));
        assertTrue(entities.isBaseNamedEntity("quot"));

        // Test with invalid base named entities
        assertFalse(entities.isBaseNamedEntity("invalidEntity"));
        assertFalse(entities.isBaseNamedEntity(""));
        assertFalse(entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();

        // Test with valid named entities
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('>', entities.getCharacterByName("gt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('"', entities.getCharacterByName("quot"));

        // Test with invalid named entities
        assertNull(entities.getCharacterByName("invalidEntity"));
        assertNull(entities.getCharacterByName(""));
        assertNull(entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeAndUnescape() {
        Entities entities = new Entities();

        // Test with simple characters
        assertEquals("&lt;Hello &amp; World&gt;", entities.escape("<Hello & World>", Entities.EscapeMode.base));
        assertEquals("<Hello & World>", entities.unescape("&lt;Hello &amp; World&gt;"));

        // Test with named entities
        assertEquals("&lt;lt&gt;<gt&gt;&amp;amp;quot;&quot;", entities.escape("&lt;lt&gt;<gt&gt;&amp;amp;quot;\",&quot;", Entities.EscapeMode.base));
        assertEquals("&lt;lt&gt;<gt&gt;&amp;amp;quot;&quot;", entities.escape("&lt;lt&gt;<gt&gt;&amp;amp;quot;&quot;", Entities.EscapeMode.base));

        // Test with surrogate pairs
        String string = "\uD800\uDC00";
        assertEquals("&#x" + Integer.toHexString(Character.charToInt(string.codePointAt(0))) +
                Integer.toHexString(Character.charToInt(string.codePointAt(1))) + ";",
                entities.escape(string, Entities.EscapeMode.base));
        assertEquals("\uD800\uDC00", entities.unescape(entities.escape(string, Entities.EscapeMode.base)));

        // Test with invalid characters
        String invalidString = "\uFFFF";
        assertEquals("&#x" + Integer.toHexString(Character.charToInt(invalidString.codePointAt(0))) + ";",
                entities.escape(invalidString, Entities.EscapeMode.base));
        try {
            entities.unescape(entities.escape(invalidString, Entities.EscapeMode.base));
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEscapeModes() {
        Entities entities = new Entities();

        // Test with xhtml entities
        assertEquals("&lt;lt&gt;&amp;amp;&quot;quot;&gt;<gt;&gt;", entities.escape("<lt>lt&amp;quot;quot>&gt;>&gt;", Entities.EscapeMode.xhtml));
        assertEquals("<lt>lt&quot;quot>&gt;>&gt;", entities.unescape("&lt;lt&gt;&amp;amp;&quot;quot;&gt;<gt>&gt;"));

        // Test with base entities
        assertEquals("&lt;lt&gt;&amp;amp;&quot;quot;&gt;<gt>&gt;", entities.escape("&lt;lt&gt;&amp;amp;&quot;quot;&gt;<gt>&gt;", Entities.EscapeMode.base));
        assertEquals("<lt>lt&quot;quot>&gt;>&gt;", entities.unescape("&lt;lt&gt;&amp;amp;&quot;quot;&gt;<gt>&gt;"));

        // Test with extended entities
        assertEquals("&lt;lt&gt;&#x26;amp;&#x22;quot;&#x3E;gt&gt;<gt>&#x3E;gt&gt;", entities.escape("&lt;lt&gt;&&amp;\"&gt;>&gt;", Entities.EscapeMode.extended));
        assertEquals("<lt>lt&amp;\" quot>&gt;>&gt;", entities.unescape("&lt;lt&gt;&#x26;amp;&#x22;quot;&#x3E;gt&gt;<gt>&#x3E;gt&gt;"));
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();

        // Test with valid characters
        for (int i = 0; i < Character.MAX_VALUE; i++) {
            char c = (char) i;
            assertTrue(entities.canEncode(c));
        }
    }

    @Test
    public void testCanEncodeWithFallback() {
        Entities entities = new Entities();

        // Test with invalid characters
        char invalid = '\uFFFF';
        assertFalse(entities.canEncode(invalid));

        // Test with non-ASCII characters
        char nonAscii = '\u00E9';
        assertTrue(entities.canEncode(nonAscii));
    }
}