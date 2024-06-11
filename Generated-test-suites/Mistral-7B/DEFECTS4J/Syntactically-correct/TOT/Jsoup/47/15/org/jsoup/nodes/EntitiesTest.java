package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue(Entities.isNamedEntity(entityName));
            assertEquals(entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character entity = entry.getKey();
            String entityName = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(entityName));
            assertEquals(entity, Entities.getCharacterByName(entityName));
        }

        String invalidEntityName = "invalidEntity";
        assertFalse(Entities.isBaseNamedEntity(invalidEntityName));
        assertNull(Entities.getCharacterByName(invalidEntityName));
    }

    @Test
    public void testEscape() {
        StringBuilder accum = new StringBuilder();
        String input = "&lt;p&gt;&amp; &quot;quoted&quot; string &gt;<br>&#10; &nbsp; &nbsp; tab &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        Entities.escape(accum, input, out, false, false, false);
        String expectedOutput = "&lt;p&gt;&amp; &amp;quot;quoted&quot; string &gt;&lt;br&gt;&#xA; &nbsp; &nbsp; &#x9; &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        assertEquals(expectedOutput, accum.toString());

        out = new Document.OutputSettings(Entities.EscapeMode.base);
        accum.setLength(0);
        Entities.escape(accum, input, out, false, false, false);
        expectedOutput = "&amp;lt;p&gt;&amp;amp; &amp;#34;quoted&amp;#34; string &amp;gt;&amp;lt;br&gt;&#xA; &nbsp; &nbsp; &amp;#x9; &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        assertEquals(expectedOutput, accum.toString());

        out = new Document.OutputSettings(Entities.EscapeMode.extended);
        accum.setLength(0);
        Entities.escape(accum, input, out, false, false, false);
        expectedOutput = "&amp;lt;p&gt;&amp;amp; &amp;#34;quoted&amp;#34; string &gt;&amp;lt;br&gt;&#xA; &nbsp; &nbsp; &amp;#x9; &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;p&gt;&amp;amp; &amp;#34;quoted&amp;#34; string &gt;&amp;lt;br&gt;&#xA; &nbsp; &nbsp; &amp;#x9; &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        String expectedOutput = "&lt;p&gt;&amp; &quot;quoted&quot; string &gt;&lt;br&gt;&#xA; &nbsp; &nbsp; tab &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        input = "&lt;p&gt;&amp; &quot;quoted&quot; string &gt;&lt;br&gt;&#xA; &nbsp; &nbsp; tab &nbsp;&nbsp; &lt;!DOCTYPE html&gt;";
        output = Entities.unescape(input, true);
        assertEquals("&lt;p&gt;&lt;p&gt;&amp; &quot;quoted&quot; string &gt;&lt;br&gt;&#xA; &nbsp; &nbsp; tab &nbsp;&nbsp; &lt;!DOCTYPE html&gt;", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Document.OutputSettings out = new Document.OutputSettings(new Enum("InvalidEscapeMode"));
        // this test should fail with an IllegalArgumentException
        Entities.escape(new StringBuilder(), "", out, false, false, false);
    }
}