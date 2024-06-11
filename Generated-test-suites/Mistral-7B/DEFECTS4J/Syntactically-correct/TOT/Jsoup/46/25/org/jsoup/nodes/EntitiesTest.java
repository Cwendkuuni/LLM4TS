import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // quot is not in base set according to the class definition
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;<amp>&quot;";
        String expected = "&amp;lt;&amp;amp;&amp;quot;";

        String escaped = Entities.escape(input, null);
        String unescaped = Entities.unescape(escaped);

        Assert.assertEquals(expected, unescaped);
    }

    @Test
    public void testEscapeWithAttributes() {
        String input = "<script type=\"text/javascript\">";
        String expected = "&lt;script type=&quot;text/javascript&quot;&gt;";

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, input, null, true, false, false);

        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'ß')); // umlaut
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.utf, 0x1F601)); // emoji
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 0x1F601));
    }
}