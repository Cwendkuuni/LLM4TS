package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // quot is in xhtml and base sets, but not in extended set
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt> &quot; test &gt;";
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, input, null, false, false, false);
        String actualOutput = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput.toString(), actualOutput);
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "<test attr='&lt; &amp; &quot; &gt;' />";
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, input, null, true, false, false);
        String actualOutput = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput.toString(), actualOutput);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;amp; &amp;quot; &amp;gt;";
        String expectedOutput = "&lt; &amp; &quot; &gt;";
        String actualOutput = Entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, actualOutput);

        String strictInput = "&amp;lt; &amp;amp; &amp;quot; &amp;gt; &";
        String actualOutputStrict = Entities.unescape(strictInput, true);
        Assert.assertEquals(expectedOutput, actualOutputStrict);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingResourceException() {
        Entities.loadEntities("nonexistent.properties");
    }
}