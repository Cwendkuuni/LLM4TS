package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity '" + namedEntity + "' should be known", Entities.isNamedEntity(namedEntity));
            Assert.assertEquals("Named entity '" + namedEntity + "' should map to character " + character, character, Entities.getCharacterByName(namedEntity));
        }
    }

    @Test
    public void testEscapeBaseMode() {
        String input = "&lt;&amp;&apos;Test&quot;<>&amp;";
        String expected = "&amp;lt;&amp;amp;&amp;apos;Test&amp;quot;&amp;&amp;lt;&amp;gt;&amp;amp;";
        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        Assert.assertEquals("Escape base mode should produce expected output", expected, output);
    }

    @Test
    public void testEscapeXhtmlMode() {
        String input = "&lt;&amp;&apos;Test&quot;<>&>&amp;";
        String expected = "&amp;lt;&amp;amp;&amp;apos;Test&amp;quot;";
        String output = Entities.escape(input, null, Entities.EscapeMode.xhtml);
        Assert.assertEquals("Escape xhtml mode should produce expected output", expected, output);
    }

    @Test
    public void testEscapeExtendedMode() {
        String input = "&lt;&amp;&apos;Test&quot;<>&>&amp;";
        String expected = "&lt;amp;&amp;apos;Test&quot;";
        String output = Entities.escape(input, null, Entities.EscapeMode.extended);
        Assert.assertEquals("Escape extended mode should produce expected output", expected, output);
    }

    @Test
    public void testUnescapeBaseMode() {
        String input = "&amp;lt;&amp;amp;&amp;apos;Test&amp;quot;&amp;&amp;lt;&amp;gt;&amp;amp;";
        String expected = "&lt;&amp;amp;&apos;Test&quot;<>&amp;gt;&amp;amp;";
        String output = Entities.unescape(input);
        Assert.assertEquals("Unescape base mode should produce expected output", expected, output);
    }

    @Test
    public void testUnescapeStrictMode() {
        String input = "&amp;lt;&amp;amp;&apos;Test&quot;<>&gt;&amp;amp;";
        String expected = "&lt;&amp;amp;&apos;Test&quot;";
        String output = Entities.unescape(input, true);
        Assert.assertEquals("Unescape strict mode should produce expected output", expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities.escape(null, null, new Entities.EscapeMode("nonexistent"));
    }
}