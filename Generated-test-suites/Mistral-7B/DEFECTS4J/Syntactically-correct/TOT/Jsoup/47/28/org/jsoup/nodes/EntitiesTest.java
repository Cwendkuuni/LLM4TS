import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

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
        Assert.assertTrue(Entities.isBaseNamedEntity("quot"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        Character gt = Entities.getCharacterByName("gt");
        Character amp = Entities.getCharacterByName("amp");
        Character quot = Entities.getCharacterByName("quot");

        Assert.assertEquals('<', lt);
        Assert.assertEquals('>', gt);
        Assert.assertEquals('&', amp);
        Assert.assertEquals('"', quot);
    }

    @Test
    public void testEscape() {
        String input = "&lt;p&gt;Hello &amp; world!&quot;";
        String expectedOutputXhtml = "&lt;p&gt;Hello &amp;amp; world!&quot;";
        String expectedOutputBase = "&lt;p&gt;Hello &amp;lt; world!&quot;";
        String expectedOutputExtended = "&lt;p&gt;Hello &lt; world!&quot;";

        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.escapeMode(Entities.EscapeMode.xhtml);

        Document.OutputSettings baseSettings = new Document.OutputSettings();
        baseSettings.escapeMode(Entities.EscapeMode.base);

        Document.OutputSettings extendedSettings = new Document.OutputSettings();
        extendedSettings.escapeMode(Entities.EscapeMode.extended);

        String xhtmlOutput = Entities.escape(input, xhtmlSettings);
        String baseOutput = Entities.escape(input, baseSettings);
        String extendedOutput = Entities.escape(input, extendedSettings);

        Assert.assertEquals(expectedOutputXhtml, xhtmlOutput);
        Assert.assertEquals(expectedOutputBase, baseOutput);
        Assert.assertEquals(expectedOutputExtended, extendedOutput);
    }

    @Test
    public void testUnescape() {
        String inputXhtml = "&lt;p&gt;Hello &amp;amp; world!&quot;";
        String inputBase = "&lt;p&gt;Hello &lt; world!&quot;";
        String inputExtended = "&lt;p&gt;Hello &#x3C; world!&#x22;";

        String expectedOutput = "&lt;p&gt;Hello < world!\"";

        String xhtmlOutput = Entities.unescape(inputXhtml);
        String baseOutput = Entities.unescape(inputBase, false); // false for non-strict
        String extendedOutput = Entities.unescape(inputExtended);

        Assert.assertEquals(expectedOutput, xhtmlOutput);
        Assert.assertEquals(expectedOutput, baseOutput);
        Assert.assertEquals(expectedOutput, extendedOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(null);
    }
}