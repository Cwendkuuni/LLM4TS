package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertFalse(entities.isNamedEntity("nt")); // non-existent entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("nt")); // non-existent entity
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("nt")); // non-existent entity
    }

    @Test
    public void testEscape_Normal() {
        Entities entities = new Entities();
        String input = "&amp;<lt>gt&quot;";
        String expectedOutput = "&amp; &lt; &gt; &quot;";

        StringBuilder accumulator = new StringBuilder(input.length() * 2);
        Entities.escape(accumulator, input, null, false, false, false);

        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testEscape_Attribute() {
        Entities entities = new Entities();
        String input = "<script src='&amp;script&gt;alert(&quot;hello&quot;)';>";
        String expectedOutput = "&lt;script src='&amp;script&gt;alert(&amp;quot;hello&quot;)&amp;gt;";

        StringBuilder accumulator = new StringBuilder(input.length() * 2);
        Entities.escape(accumulator, input, null, true, false, false);

        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp;<lt>&gt;\"";

        String output = entities.unescape(entities.escape(input, null));

        assertEquals(expectedOutput, output);
    }
}