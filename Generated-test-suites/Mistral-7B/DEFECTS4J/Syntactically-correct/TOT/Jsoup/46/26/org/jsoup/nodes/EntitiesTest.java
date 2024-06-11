import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        Character amp = Entities.getCharacterByName("amp");

        Assert.assertEquals('<', lt.intValue());
        Assert.assertEquals('&', amp.intValue());
    }

    @Test
    public void testEscapeCharacter() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "ab&c<d>e&f", Entities.EscapeMode.base, false, false, false);
        Assert.assertEquals("ab&amp;c&lt;d&gt;e&amp;f", accum.toString());
    }

    @Test
    public void testEscapeString() {
        StringBuilder accum = new StringBuilder();
        String input = "ab & c < d > e & f";

        Entities.escape(accum, input, Entities.EscapeMode.base, false, false, false);
        Assert.assertEquals("ab &amp;amp; c &amp;lt; &amp;lt; d &amp;gt; &amp;gt; e &amp;amp; f", accum.toString());

        Entities.escape(accum, input, Entities.EscapeMode.xhtml, false, false, false);
        Assert.assertEquals("ab &amp; c &lt; d &gt; e &amp;", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities.escape(new StringBuilder(), "input", new Entities.EscapeMode("invalid"), false, false, false);
    }

    @Test
    public void testCanEncode() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\u00E9', null));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u00E9', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'A', null));
    }

    @Test
    public void testUnescapeCharacter() {
        Character lt = Character.valueOf('<');
        Character amp = Character.valueOf('&');

        Character unescapedLt = Entities.getCharacterByName("lt");
        Character unescapedAmp = Entities.getCharacterByName("amp");

        Assert.assertEquals(unescapedLt, lt);
        Assert.assertEquals(unescapedAmp, amp);

        String input = "&lt; & &gt;";
        String expected = "< & >";

        String result = Entities.unescape(input, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescapeStringWithStrict() {
        String input = "&lt; & &gt;";
        String expected = "< & >";

        String result = Entities.unescape(input, true);
        Assert.assertEquals(expected, result);

        String inputWithTrailingSemicolon = input + ";";
        String expectedWithTrailingSemicolon = "< & >;";

        result = Entities.unescape(inputWithTrailingSemicolon, true);
        Assert.assertEquals(expectedWithTrailingSemicolon, result);
    }
}