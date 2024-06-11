package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected = '<';
        Character actual = Entities.getCharacterByName("lt");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        String input = "&amp;<tag>&quot; &gt; &amp;#160; &nbsp; &amp;#128; 😃";
        String expected = "&amp;amp;&gt;<tag>&quot;&quot; &amp;gt; &amp;#x100; &nbsp; &amp;#x10000; &#x1f600;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;&gt;<tag>&quot;&quot; &amp;gt; &amp;#x100; &nbsp; &amp;#x10000; &#x1f600;";
        String expected = "&amp;<tag>&quot;&quot; > <tag> &amp;#160; &nbsp; &amp;#128; 😀";
        String output = Entities.unescape(input, false);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.ascii;
        char c = 'é';
        boolean expected = false;
        boolean actual = Entities.canEncode(coreCharset, c, null);
        Assert.assertFalse(actual);
    }
}