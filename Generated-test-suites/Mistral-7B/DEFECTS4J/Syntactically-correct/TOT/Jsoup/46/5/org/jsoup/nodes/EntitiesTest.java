import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("quot"));
        assertFalse(entities.isNamedEntity("null"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals(Character.forInt(0x003C), entities.getCharacterByName("lt"));
        assertEquals(Character.forInt(0x0026), entities.getCharacterByName("amp"));
        assertEquals(Character.forInt(0x0022), entities.getCharacterByName("quot"));
        assertNull(entities.getCharacterByName("null"));
    }

    @Test
    public void testEscapeSimple() {
        Document.OutputSettings out = new Document.OutputSettings();
        String string = "&amp;<tag>&quot;";
        String escaped = Entities.escape(string, out);
        assertEquals("&amp;&amp;&lt;tag&gt;&amp;quot;", escaped);
    }

    @Test
    public void testEscapeComplex() {
        Document.OutputSettings out = new Document.OutputSettings();
        String string = "&lt;tag&quot; &amp; &gt; &amp; &quot; &amp;#x26; &amp;#x3C;";
        String escaped = Entities.escape(string, out);
        assertEquals("&amp;&lt;tag&gt; &amp; &amp;&gt; &amp; &amp;quot; &amp;#x26; &amp;#x3C;", escaped);
    }

    @Test
    public void testUnescapeSimple() {
        String string = "&amp;<tag>&quot; &amp; &gt; &amp; &quot; &amp;#x26; &amp;#x3C;";
        String unescaped = Entities.unescape(string, false);
        assertEquals("&lt;tag&quot; &amp; &gt; &amp; &quot; &amp;#x26; &amp;#x3C;", unescaped);
    }

    @Test
    public void testUnescapeWithTrailingSemicolon() {
        String string = "&amp;<tag>&quot; &amp; &gt; &amp; &quot; &amp;#x26;&#x3B; &amp;#x3C;&#x3B;";
        String unescaped = Entities.unescape(string, true);
        assertEquals("&lt;tag&quot; &amp; &gt; &amp; &quot; &amp;#x26;&#xA; &amp;#x3C;&#xA;", unescaped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(new EnumValue<>("invalidMode"));
    }
}