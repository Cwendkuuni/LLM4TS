import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        Assert.assertTrue("lt should be a named entity", Entities.isNamedEntity("lt"));
        Assert.assertTrue("amp should be a named entity", Entities.isNamedEntity("amp"));
        Assert.assertTrue("apos should be a named entity", Entities.isNamedEntity("apos"));
        Assert.assertTrue("quot should be a named entity", Entities.isNamedEntity("quot"));
        Assert.assertFalse("nonesuch should not be a named entity", Entities.isNamedEntity("nonesuch"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        Character lt = Entities.getCharacterByName("lt");
        Character amp = Entities.getCharacterByName("amp");
        Character apos = Entities.getCharacterByName("apos");
        Character quot = Entities.getCharacterByName("quot");

        Assert.assertEquals("<", lt);
        Assert.assertEquals("&", amp);
        Assert.assertEquals("'", apos);
        Assert.assertEquals("\"", quot);
    }

    @Test
    public void testEscape() {
        String input = "&amp;&lt;&gt;&apos;&quot;";
        String expected = "&amp;&amp; &lt;&gt; &apos;&apos; &quot;&quot;";

        String output = Entities.escape(input, null, Entities.Entities.extended);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;&amp; &lt;&gt; &apos;&apos; &quot;&quot;";
        String expected = "&amp;&lt;&gt;&apos;&quot;";

        String output = Entities.unescape(input);

        Assert.assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeInvalid() {
        String input = "&invalid";

        Entities.unescape(input);
    }
}