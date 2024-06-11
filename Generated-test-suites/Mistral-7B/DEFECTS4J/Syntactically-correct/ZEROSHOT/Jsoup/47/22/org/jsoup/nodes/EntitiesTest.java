package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity '" + name + "' should be known.", Entities.isNamedEntity(name));
            Assert.assertEquals("Named entity '" + name + "' should have the correct character.", character, Entities.getCharacterByName(name));
        }

        // Test unknown named entities
        Assert.assertFalse("Unknown named entity 'noname' should not be known.", Entities.isNamedEntity("noname"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> baseEntities = Entities.base;

        // Test known base named entities
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Base named entity '" + name + "' should be known.", Entities.isBaseNamedEntity(name));
            Assert.assertEquals("Base named entity '" + name + "' should have the correct character.", character, Entities.getCharacterByName(name));
        }

        // Test unknown base named entities
        Assert.assertFalse("Unknown base named entity 'noname' should not be known.", Entities.isBaseNamedEntity("noname"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();

        // Test basic escaping
        String input = "&amp;<lt>&gt;\"";
        String expectedOutput = "&amp;amp; &lt; &gt; &quot;";
        String output = Entities.escape(input, null);
        Assert.assertEquals("Input '&amp;<lt>&gt;&quot; should be escaped to '" + expectedOutput + "'.", expectedOutput, output);

        // Test escaping with different output settings
        Document.OutputSettings outputSettingsXHTML = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        String outputXHTML = Entities.escape(input, outputSettingsXHTML);
        Assert.assertEquals("Input '&amp;<lt>&gt;&quot; should be escaped to '" + "&amp; &lt; &gt; &quot;" + "' for XHTML output.", "&amp; &lt; &gt; &quot;", outputXHTML);

        Document.OutputSettings outputSettingsBase = new Document.OutputSettings(Entities.EscapeMode.base);
        String outputBase = Entities.escape(input, outputSettingsBase);
        Assert.assertEquals("Input '&amp;<lt>&gt;&quot; should be escaped to '" + "&amp;amp; &lt; &gt; &quot;" + "' with base entities.", "&amp;amp; &lt; &gt; &quot;", outputBase);

        Document.OutputSettings outputSettingsExtended = new Document.OutputSettings(Entities.EscapeMode.extended);
        String outputExtended = Entities.escape(input, outputSettingsExtended);
        Assert.assertEquals("Input '&amp;<lt>&gt;&quot; should be escaped to '" + "&amp; &lt; &gt; &quot;" + "' with extended entities.", "&amp; &lt; &gt; &quot;", outputExtended);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        // Test basic unescaping
        String inputEscaped = "&amp;amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp;<lt>&gt;&quot;";
        String output = Entities.unescape(inputEscaped, false);
        Assert.assertEquals("Input '&amp;amp; &lt; &gt; &quot;' should be unescaped to '" + expectedOutput + "'.", expectedOutput, output);

        // Test strict unescaping
        String inputEscapedStrict = "&amp;amp; &lt; &gt; &quot; &amp; &lt; &gt; &quot; &amp; &amp; &quot; &amp; &quot; &amp; &quot; &amp; &quot;";
        String outputStrict = Entities.unescape(inputEscapedStrict, true);
        Assert.assertEquals("Input '&amp;amp; &lt; &gt; &quot; &amp; &lt; &gt; &quot; &amp; &amp; &quot; &amp; &quot; &amp; &quot; &amp; &quot;' should be unescaped to '&amp;<lt>&gt;&quot; &amp;<lt> &gt; &amp;<quot; &amp; &quot; &amp; &quot; &amp; &quot; &amp; &quot;' with strict unescaping.", "&amp;<lt>&gt;&quot; &amp;<lt> &gt; &amp;<quot; &amp; &quot; &amp; &quot; &amp; &quot; &amp; &quot; &amp; &quot;", outputStrict);
    }
}