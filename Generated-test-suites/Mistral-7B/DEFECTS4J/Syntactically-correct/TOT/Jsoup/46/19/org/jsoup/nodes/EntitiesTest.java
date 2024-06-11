import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertFalse(entities.isNamedEntity("invalidEntitiy"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Assert.assertEquals('<', entities.getCharacterByName("lt"));
        Assert.assertEquals('&', entities.getCharacterByName("amp"));
        Assert.assertNull(entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        StringBuilder escapedString = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = "&<>\"'&nbsp;";

        entities.escape(escapedString, input, settings, false, false, false);
        Assert.assertEquals("&amp;&lt;&gt;&quot;'&nbsp;", escapedString.toString());

        settings.setEscapeMode(Entities.EscapeMode.xhtml);
        escapedString.setLength(0);
        entities.escape(escapedString, input, settings, false, false, false);
        Assert.assertEquals("&amp;lt;&amp;gt;&amp;quot;", escapedString.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;>&gt;&quot;'&nbsp;";

        String output = entities.unescape(input);
        Assert.assertEquals("<>\"' ", output);

        String inputStrict = "&lt;&gt;&quot;'&nbsp;";
        String outputStrict = entities.unescape(inputStrict, true);
        Assert.assertEquals("< > \" ' ", outputStrict);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        Assert.assertTrue(entities.canEncode(charset, 'a'));
        Assert.assertTrue(entities.canEncode(charset, ' '));
        Assert.assertTrue(entities.canEncode(charset, 'é')); // UTF-8 can encode é
        Assert.assertFalse(entities.canEncode(charset, '€')); // UTF-8 can't encode € directly

        // Fallback encoder test
        Assert.assertTrue(entities.canEncode(charset, '€', encoder)); // Encoder can encode €
    }
}