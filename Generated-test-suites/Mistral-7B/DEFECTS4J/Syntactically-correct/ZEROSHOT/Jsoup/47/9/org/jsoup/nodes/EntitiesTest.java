package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            Assert.assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            Assert.assertTrue("Base named entity '" + name + "' should be known", Entities.isBaseNamedEntity(name));
            Assert.assertEquals("Base named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        // Test unknown entities
        Assert.assertFalse("Unknown entity 'xyz' should not be known", Entities.isNamedEntity("xyz"));
        Assert.assertNull("Unknown entity 'xyz' should not have a character mapping", Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = "&lt;hello&gt; world &amp; quote &apos; &quot; &apos; &amp; &gt; <>&quot;";
        String expectedOutput = "&amp;lt;hello&amp;gt; &amp;amp; quote &amp;apos; &amp;quot; &amp;apos; &amp;amp; &amp;gt; <&gt; &amp;&quot;";
        String output = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected output", expectedOutput, output);

        // Test escaping in attribute values
        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        input = "<img src=\"image.jpg\" title=\"& &amp; &gt; &quot; &apos; &quot;\">";
        expectedOutput = "<img src=\"image.jpg\" title=\"&amp;amp; &amp;amp; &amp;gt; &amp;quot; &amp;apos; &amp;quot;\">";
        output = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected output", expectedOutput, output);

        // Test normalization of whitespace
        outputSettings.setNormalizeWhitespace(true);
        input = " hello world ";
        expectedOutput = " hello world ";
        output = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected output", expectedOutput, output);

        outputSettings.setStripLeadingWhitespace(true);
        input = "   hello world   ";
        expectedOutput = "hello world";
        output = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected output", expectedOutput, output);

        // Test escaping of non-ASCII characters
        outputSettings.setEncoder(Character.isDefinedChar(0x1F000) ? Character.unicodeEncoder("UTF-16LE") : null);
        input = "\uD800\uDC00";
        expectedOutput = "&#x1F000;&#xDC00;";
        output = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;hello&amp;gt; world &amp;amp; quote &amp;apos; &amp;quot; &amp;apos; &amp;amp; &amp;gt; <&gt; &amp;&quot;";
        String expectedOutput = "<hello> world &amp; quote ' &quot; ' &amp; &gt; <>";
        String output = Entities.unescape(input);
        Assert.assertEquals("Unescaped output should match expected output", expectedOutput, output);

        // Test strict unescaping
        input = "&amp;lt;hello&amp;gt; world &amp;amp; quote &amp;apos; &amp;quot; &amp;apos; &amp;amp; &amp;gt; <&gt; &amp;&quot;";
        output = Entities.unescape(input, true);
        expectedOutput = "<hello> world &amp;amp; quote ' &quot; ' &amp;amp; &amp;gt; <&gt; &amp;&quot;";
        Assert.assertEquals("Unescaped output (strict) should match expected output", expectedOutput, output);
    }
}