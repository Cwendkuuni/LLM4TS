import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set
        Assert.assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeMethod() {
        String input = "&lt;<&amp; &quot; lt &gt;gt &amp; quot &amp;amp; &gt;gt &gt; &quot; &quot;&quot;";
        String expectedOutput = "&amp;lt;&lt; &amp;amp; &amp;quot; lt &amp;gt;&gt; &amp;gt;&gt; &amp;quot; &amp;quot;&quot;";
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.base);
        String output = Entities.escape(input, out);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;lt;&lt; &amp;amp; &amp;quot; lt &amp;gt;&gt; &amp;gt;&gt; &amp;quot; &amp;quot;&quot;";
        String expectedOutput = "&lt;&lt; < &quot; lt &gt;&gt; > &quot; &quot;&quot;";
        String output = Entities.unescape(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNull() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNull() {
        Entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeMethodWithEmptyString() {
        String input = "";
        String output = Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.base));
        Assert.assertEquals("", output);
    }

    @Test
    public void testEscapeMethodWithWhiteSpace() {
        String input = " foo   bar   baz ";
        String expectedOutput = " foo &nbsp; &nbsp; bar &nbsp; &nbsp; baz ";
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        String output = Entities.escape(input, out);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithSpecialChars() {
        String input = "& < > \" & & &amp; &gt; &lt; &quot; ";
        String expectedOutput = "&amp; &lt; &amp; &amp; &amp;amp; &gt; &amp;lt; &amp;quot; &amp;quot; &amp;quot;&quot;";
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.base);
        String output = Entities.escape(input, out);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = java.nio.charset.Charset.forName("UTF-8").newEncoder();
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', encoder));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', encoder));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'é', encoder));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Assert.assertEquals('&', entities.get("amp"));
        Assert.assertEquals('>', entities.get("gt"));
        Assert.assertEquals('<', entities.get("lt"));
        Assert.assertEquals('"', entities.get("quot"));
    }
}