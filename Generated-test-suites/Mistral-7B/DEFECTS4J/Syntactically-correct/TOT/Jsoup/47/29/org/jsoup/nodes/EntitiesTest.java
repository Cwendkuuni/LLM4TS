import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertTrue(entities.isNamedEntity("quot"));
        Assert.assertFalse(entities.isNamedEntity("X")); // invalid entity name
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(entities.isBaseNamedEntity("quot")); // quot is XHTML only
        Assert.assertFalse(entities.isBaseNamedEntity("gt")); // invalid entity in base set
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Character c1 = entities.getCharacterByName("lt");
        Character c2 = entities.getCharacterByName("amp");
        Character c3 = entities.getCharacterByName("quot");
        Assert.assertEquals('<', c1);
        Assert.assertEquals('&', c2);
        Assert.assertEquals('"', c3);
        // invalid entity name
        Assert.assertNull(entities.getCharacterByName("X"));
    }

    @Test
    public void testEscapeAndUnescape() {
        Entities entities = new Entities();
        String input = "&lt;<>&quot;&amp;  &gt;";
        String output = entities.escape(input, Document.OutputSettings.xhtml());
        String escapedOutput = "&amp;lt;&lt;(&quot;)&amp;amp; &amp;gt;";
        Assert.assertEquals(escapedOutput, output);

        String unescapedInput = "&amp;lt;&lt;(&quot;)&amp;amp; &amp;gt;";
        String unescapedOutput = entities.unescape(unescapedInput);
        Assert.assertEquals(input, unescapedOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithInvalidEscapeMode() {
        Entities entities = new Entities();
        entities.escape("input", new Document.OutputSettings("invalidEscapeMode"));
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.canEncode(CoreCharset.ascii, 'A'));
        Assert.assertTrue(entities.canEncode(CoreCharset.ascii, ' '));
        Assert.assertTrue(entities.canEncode(CoreCharset.utf, '\u00A9')); // copyright sign
        Assert.assertFalse(entities.canEncode(CoreCharset.utf, '\uD834\uDD1E')); // high surrogate
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> entitiesMap = entities.loadEntities("entities-custom.properties");
        Assert.assertNotNull(entitiesMap);
        Assert.assertTrue(entitiesMap.containsKey("eacute"));
        Assert.assertEquals('é', entitiesMap.get("eacute"));
    }
}