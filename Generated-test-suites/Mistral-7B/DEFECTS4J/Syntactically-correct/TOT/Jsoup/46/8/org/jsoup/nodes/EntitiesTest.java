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
        Assert.assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<tag name=&quot;lt&quot; attr=&apos;value&apos;/>";
        String output = Entities.escape(input, Document.OutputSettings.base());
        String expected = "&amp;amp;(&amp;lt;)(name=&amp;quot;lt&quot;)&amp;amp;( attr=&amp;apos;value&apos;)/";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;(&lt;)(name=&quot;lt&quot;)&amp;amp;( attr=&apos;value&apos;)/";
        String output = Entities.unescape(input, false);
        String expected = "&amp;<tag name=&quot;lt&quot; attr=&apos;value&apos;/>";
        Assert.assertEquals(expected, output);

        String strictOutput = Entities.unescape(input, true);
        Assert.assertEquals(input, strictOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities.unescape("", new Document.OutputSettings("invalidMode"));
    }

    @Test
    public void testCanEncode() {
        Assert.assertTrue(Entities.canEncode(CoreCharset.utf, 'A'));
        Assert.assertTrue(Entities.canEncode(CoreCharset.utf, '€'));
        Assert.assertFalse(Entities.canEncode(CoreCharset.ascii, '€'));
    }
}