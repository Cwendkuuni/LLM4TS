import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token.Doctype();
    }

    @Test
    public void testDoctypeReset() {
        token.newDoctype("html", null, null, false);
        token.reset();
        assertNull(token.getName());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        token.newDoctype("html", "HTML", "public", null);
        assertEquals("html", token.getName());
        assertEquals("HTML", token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        token = new Token.StartTag();
        token.name("div");
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.attributes);
    }

    @Test
    public void testStartTagGetters() {
        token = new Token.StartTag();
        token.name("div").attr("id", "test");
        assertEquals("div", token.name());
        assertEquals("div", token.normalName());
        assertEquals("id", token.getAttributes().keySet().iterator().next());
        assertEquals("test", token.getAttributes().getValue("id"));
    }

    @Test
    public void testEndTagReset() {
        token = new Token.EndTag();
        token.name("div");
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName());
    }

    @Test
    public void testEndTagGetters() {
        token = new Token.EndTag();
        token.name("div");
        assertEquals("div", token.name());
    }

    @Test
    public void testCommentReset() {
        token = new Token.Comment();
        token.data("This is a comment");
        token.reset();
        assertNull(token.tokenType());
        assertEquals("", token.getData());
    }

    @Test
    public void testCommentGetter() {
        token = new Token.Comment();
        token.data("This is a comment");
        assertEquals("Comment", token.tokenType().name());
        assertEquals("This is a comment", token.getData());
    }

    @Test
    public void testCharacterReset() {
        token = new Token.Character();
        token.data("A");
        token.reset();
        assertNull(token.tokenType());
        assertEquals("", token.getData());
    }

    @Test
    public void testCharacterGetter() {
        token = new Token.Character();
        token.data("A");
        assertEquals("Character", token.tokenType().name());
        assertEquals("A", token.getData());
    }

    @Test
    public void testEOFReset() {
        token = new Token.EOF();
        token.reset();
        assertTrue(token.isEOF());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenReset() {
        token = new Object();
        token.reset();
        fail("Expected AssertionError, but was: " + token.tokenType());
    }
}