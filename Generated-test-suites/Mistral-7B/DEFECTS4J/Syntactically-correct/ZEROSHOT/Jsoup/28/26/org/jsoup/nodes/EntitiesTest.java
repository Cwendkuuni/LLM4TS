package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Entities class.
 *
 * @author your_name
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity '" + name + "' should exist.", entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should have value '" + value + "'.", value, entities.getCharacterByName(name));
        }

        // Test unknown named entity
        assertFalse("Unknown named entity 'nonexistent' should not exist.", entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String sampleString = "&lt;Hello&amp; World&gt;<SpecialCharacter>!";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.encoder((CharsetEncoder) java.nio.charset.StandardCharsets.UTF_8::new);
        outputSettings.escapeMode(Entities.Entities.EscapeMode.extended);

        String escapedString = entities.escape(sampleString, outputSettings);
        String expectedEscapedString = "&amp;lt;Hello&amp;amp;amp;amp;amp;amp;nbsp;amp;AMP;Acirc;Atilde;Auml;Otilde;Uuml;szlig;OElig;AElig;laquo;rdquo;ndash;mdash;lsquo;rsquo;&#0153;!";
        assertEquals("Escaped string should be '" + expectedEscapedString + "'.", expectedEscapedString, escapedString);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String sampleString = "&amp;lt;Hello&amp;amp;amp;amp;amp;amp;nbsp;amp;AMP;Acirc;Atilde;Auml;Otilde;Uuml;szlig;OElig;AElig;laquo;rdquo;ndash;mdash;lsquo;rsquo;&#0153;!";

        String unescapedString = entities.unescape(sampleString);
        String expectedUnescapedString = "&lt;Hello&amp; World&gt;&copy;!";
        assertEquals("Unescaped string should be '" + expectedUnescapedString + "'.", expectedUnescapedString, unescapedString);
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        Entities entities = new Entities();
        assertFalse("An empty string should not contain named entities.", entities.isNamedEntity(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNullName() {
        Entities entities = new Entities();
        entities.isNamedEntity(null);
    }

    @Test
    public void testGetCharacterByNameWithEmptyName() {
        Entities entities = new Entities();
        assertNull("An empty name should not have a character value.", entities.getCharacterByName(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNullName() {
        Entities entities = new Entities();
        entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeWithNullString() {
        Entities entities = new Entities();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.encoder((CharsetEncoder) java.nio.charset.StandardCharsets.UTF_8::new);
        outputSettings.escapeMode(Entities.Entities.EscapeMode.extended);

        String escapedString = entities.escape(null, outputSettings);
        assertEquals("A null string should be returned unchanged.", null, escapedString);
    }

    @Test
    public void testUnescapeWithNullString() {
        Entities entities = new Entities();
        String escapedString = null;

        String unescapedString = entities.unescape(escapedString);
        assertEquals("A null string should be returned unchanged.", null, unescapedString);
    }

    @Test
    public void testUnescapeWithEmptyString() {
        Entities entities = new Entities();
        String escapedString = "";

        String unescapedString = entities.unescape(escapedString);
        assertEquals("An empty string should be returned unchanged.", "", unescapedString);
    }
}