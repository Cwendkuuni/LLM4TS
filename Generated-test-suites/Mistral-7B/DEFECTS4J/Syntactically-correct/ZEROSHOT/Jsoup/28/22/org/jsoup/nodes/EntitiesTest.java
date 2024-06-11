package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Entities class.
 *
 * @author Johnson
 */
public class EntitiesTest {

    /**
     * Test method for {@link Entities#isNamedEntity(String)} .
     */
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    /**
     * Test method for {@link Entities#getCharacterByName(String)} .
     */
    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    /**
     * Test method for {@link Entities#escape(String, Document.OutputSettings)} .
     */
    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();

        String input = "&lt;test&amp; &quot;test&quot; &gt;test&gt;";
        String expectedOutput = "&amp;lt;test&amp;amp; &amp;quot;test&amp;quot; &amp;gt;test&amp;gt;";

        String output = Entities.escape(input, settings);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link Entities#unescape(String)} .
     */
    @Test
    public void testUnescape() {
        String input = "&amp;lt;test&amp;amp; &amp;quot;test&amp;quot; &amp;gt;test&amp;gt;";
        String expectedOutput = "&lt;test&gt; \"test\" &gt;test&gt;";

        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link Entities#unescape(String, boolean)} .
     */
    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;lt;test&amp;lt; &amp;quot;test&amp;quot; &amp;gt;test&amp;gt;";
        String expectedOutput = "&lt;test&lt; &quot;test&quot; &gt;test&gt;";

        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link Entities#xhtmlByVal} .
     */
    @Test
    public void testXhtmlByVal() {
        Map<Character, String> xhtmlMap = Entities.xhtmlByVal;

        assertEquals("quot", xhtmlMap.get((char) 0x00022));
        assertEquals("amp", xhtmlMap.get((char) 0x00026));
        assertEquals("apos", xhtmlMap.get((char) 0x00027));
        assertEquals("lt", xhtmlMap.get((char) 0x0003C));
        assertEquals("gt", xhtmlMap.get((char) 0x0003E));
    }

    /**
     * Test method for {@link Entities#loadEntities(String)} .
     */
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = new HashMap<String, Character>();
        entities = Entities.loadEntities("entities-base.properties"); // most common / default

        assertEquals((char) 0x00022, entities.get("quot"));
        assertEquals((char) 0x00026, entities.get("amp"));
        assertEquals((char) 0x00027, entities.get("apos"));
        assertEquals((char) 0x0003C, entities.get("lt"));
        assertEquals((char) 0x0003E, entities.get("gt"));
    }

    /**
     * Test method for {@link Entities#toCharacterKey(Map<String, Character>)} .
     */
    @Test
    public void testToCharacterKey() {
        Map<Character, String> entities = new HashMap<Character, String>();
        Map<String, Character> properties = new HashMap<String, Character>();
        properties.put("quot", '\"');
        properties.put("amp", '&');

        entities = Entities.toCharacterKey(properties);

        assertEquals('\"', entities.get((char) 0x00022));
        assertEquals('&', entities.get((char) 0x00026));
    }
}