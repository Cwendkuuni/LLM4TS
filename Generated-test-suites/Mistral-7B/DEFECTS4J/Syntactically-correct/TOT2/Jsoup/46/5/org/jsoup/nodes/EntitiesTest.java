package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> fullEntities = Entities.full;
        Map<String, Character> baseEntities = Entities.base;

        // Test known named entities
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("isNamedEntity should return true for the known named entity " + name, Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        // Test known named entities in the base set
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("isBaseNamedEntity should return true for the known named entity in the base set " + name, Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        // Test unknown named entities
        assertFalse("isNamedEntity should return false for unknown named entity", Entities.isNamedEntity("unknownEntity"));
        assertFalse("isBaseNamedEntity should return false for unknown named entity", Entities.isBaseNamedEntity("unknownEntity"));
    }

    @Test
    public void testEscapeMethod() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String inputString = "This is a & test & with some & <characters> & like & < & > & ' & \" & &amp; & &gt; & &quot;.";
        Entities.escape(accum, inputString, null, false, false, false);
        String expectedOutput = "This is a &amp; test &amp; with some &amp; &lt;characters&gt; &amp; like &amp; &lt; &amp; &gt; &amp; &amp;quot; &amp; &amp;amp; &amp; &amp;gt; &amp; &amp;quot;.";
        assertEquals("escape method output does not match expected output", expectedOutput, accum.toString());
    }

    @Test
    public void testEscapeMethodInAttribute() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String inputString = "<html><title>This is a & test & with some & <characters> & like & < & > & ' & \" & &amp; & &gt; & &quot;.</title></html>";
        Entities.escape(accum, inputString, null, true, false, false);
        String expectedOutput = "<html><title>This is a &amp; test &amp; with some &amp; &lt;characters&gt; &amp; like &amp; &lt; &amp; &gt; &amp; &amp;quot; &amp; &amp;amp; &amp; &amp;gt; &amp; &amp;quot;</title></html>";
        assertEquals("escape method output does not match expected output when in attribute", expectedOutput, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        Entities.escape(accum, null, null, false, false, false);
    }

    @Test
    public void testCanEncodeMethod() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.ascii;

        // Test ASCII characters
        for (char c = 0; c < 128; c++) {
            assertTrue("canEncode should return true for ASCII character " + (int) c, Entities.canEncode(coreCharset, c, null));
        }

        // Test non-ASCII characters
        char nonAsciiChar = 0x1F5;
        assertFalse("canEncode should return false for non-ASCII character " + nonAsciiChar, Entities.canEncode(coreCharset, nonAsciiChar, null));
    }

    @Test
    public void testUnescapeMethod() {
        Entities entities = new Entities();
        String inputString = "This is a &amp; test &amp; with some &amp; &lt;characters&gt; &amp; like &amp; &lt; &amp; &gt; &amp; &amp;quot; &amp; &amp;amp; &amp; &amp;gt; &amp; &amp;quot;.";
        String expectedOutput = "This is a test with some <characters> like < > ' " + "\" & & & >.";

        String outputString = Entities.unescape(inputString, false);
        assertEquals("unescape method output does not match expected output", expectedOutput, outputString);
    }

    @Test
    public void testUnescapeMethodWithStrict() {
        Entities entities = new Entities();
        String inputString = "This is a &amp;&amp;test &amp;with some &amp; &lt;characters&gt;&gt; &amp; like &amp; &lt; &amp; &gt; &amp; &amp;quot; &amp; &amp;amp; &amp; &amp;gt; &amp; &amp;quot;.";
        String expectedOutput = "This is a test with some <characters> like < > ' \" & & & >.";

        String outputString = Entities.unescape(inputString, true);
        assertEquals("unescape method output does not match expected output when strict is true", expectedOutput, outputString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullInput() {
        Entities entities = new Entities();
        String inputString = null;
        Entities.unescape(inputString, false);
    }
}