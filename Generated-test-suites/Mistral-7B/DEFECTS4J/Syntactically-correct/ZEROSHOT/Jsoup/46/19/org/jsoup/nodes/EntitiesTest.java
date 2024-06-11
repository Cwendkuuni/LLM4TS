package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.Test;

/**
 * Test class for Entities class
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("quot"));
        assertFalse(entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertTrue(entities.isBaseNamedEntity("quot"));
        assertFalse(entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('"', entities.getCharacterByName("quot"));
        assertNull(entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        String input = "&lt;<&quot;>&amp; &nbsp; &amp;lt; &amp;gt; &amp;quot;";
        String expectedOutput = "&amp;lt;&lt; &amp;amp;&quot; &amp;amp; &amp;gt;&gt; &amp;amp;quot;";

        String output = entities.escape(input, out);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String input = "&amp;lt;&lt; &amp;amp;&quot; &amp;amp; &amp;gt;&gt; &amp;amp;quot;";
        String expectedOutput = "&lt;&lt; \" & &gt;&gt; \"";

        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.utf;

        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        assertTrue(entities.canEncode(coreCharset, 'A', encoder));
        assertTrue(entities.canEncode(coreCharset, 'a', encoder));
        assertTrue(entities.canEncode(coreCharset, 'é', encoder));
        assertTrue(entities.canEncode(coreCharset, '\u00A0', encoder));
        assertFalse(entities.canEncode(coreCharset, '€', encoder));
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> entitiesMap = entities.loadEntities("entities-full.properties");
        assertEquals('©', entitiesMap.get("copy"));
        assertEquals('®', entitiesMap.get("reg"));
        assertNull(entitiesMap.get("invalidEntity"));
    }
}