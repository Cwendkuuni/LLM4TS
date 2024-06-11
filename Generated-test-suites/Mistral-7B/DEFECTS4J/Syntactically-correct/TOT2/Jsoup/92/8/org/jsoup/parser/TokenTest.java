
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // Initial state

        doctype.reset();

        assertTrue(doctype.getName().isEmpty());
        assertNull(doctype.getPubSysKey());
        assertTrue(doctype.getPublicIdentifier().isEmpty());
        assertTrue(doctype.getSystemIdentifier().isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTaggetName() {
        StartTag startTag = new StartTag().name("html");
        EndTag endTag = new EndTag().name("html");

        assertEquals("html", startTag.name());
        assertEquals("html", endTag.name());
    }

    @Test
    public void testTagnewAttribute() {
        StartTag tag = new StartTag();
        tag.appendTagName("html");
        tag.newAttribute().appendAttributeName("class").appendAttributeValue("test");

        assertEquals(1, tag.getAttributes().size());
        assertEquals("class", tag.getAttributes().keySet().iterator().next());
        assertEquals("test", tag.getAttributes().get("class"));
    }

    @Test
    public void testTagToString() {
        StartTag startTag = new StartTag().name("html").attributes(new Attributes().put("class", "test"));
        EndTag endTag = new EndTag().name("html");

        assertEquals("<html class=\"test\">", startTag.toString());
        assertEquals("</html>", endTag.toString());
    }

    @Test
    public void testCharacterResetWithoutData() {
        Character character = new Character();
        character.reset();

        assertTrue(character.getData().isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterResetWithData() {
        Character character = new Character().data("test");
        character.reset();
        fail("Expected an AssertionError when trying to reset a Character token with data");
    }
}