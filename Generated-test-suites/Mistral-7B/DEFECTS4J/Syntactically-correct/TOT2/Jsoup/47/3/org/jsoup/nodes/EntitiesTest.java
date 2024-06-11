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
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;< &amp; >&quot;";
        String expectedOutput = "&amp;lt;&lt; &amp;amp; &amp;gt;&gt; &amp;quot;&quot;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;&lt; &amp;amp; &amp;gt;&gt; &amp;quot;&quot;";
        String expectedOutput = "&lt;&lt; < &amp; &gt;&gt; > &quot;&quot;";
        String output = Entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, output);
    }

    
}