import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.name("html");
        assertEquals("html", doctypeToken.getName());

        doctypeToken.systemIdentifier("systemIdentifier.html");
        assertEquals("systemIdentifier.html", doctypeToken.getSystemIdentifier());

        doctypeToken.forceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());

        resetDoctypeToken = doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        startTagToken.name("div");
        assertEquals("div", startTagToken.name());
        assertEquals("div", startTagToken.normalName());

        startTagToken.selfClosing(true);
        assertTrue(startTagToken.isSelfClosing());

        startTagToken.attributes(new Attributes() {{
            put("id", "test");
            put("class", "testClass");
        }});
        assertNotNull(startTagToken.getAttributes());
        assertEquals("id=\"test\" class=\"testClass\"", startTagToken.toString());

        resetStartTagToken = startTagToken.reset();
        assertNull(startTagToken.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        endTagToken.name("div");
        assertEquals("div", endTagToken.name());
        assertEquals("div", endTagToken.normalName());

        assertEquals("</div>", endTagToken.toString());

        resetEndTagToken = endTagToken.reset();
        assertNull(endTagToken.name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        commentToken.data("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("<!--This is a comment-->", commentToken.toString());

        resetCommentToken = commentToken.reset();
        assertNull(commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        characterToken.data("A");
        assertEquals('A', characterToken.getData().charAt(0));
        assertEquals("A", characterToken.toString());

        resetCharacterToken = characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertEquals("EOF", resetEofToken.tokenType());
    }
}