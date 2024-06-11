import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertSame(doctypeToken, resetDoctypeToken);

        doctypeToken.getName().append("html");
        assertEquals("html", doctypeToken.name());

        doctypeToken.getPublicIdentifier().append("publicID");
        assertEquals("publicID", doctypeToken.getPublicIdentifier());

        doctypeToken.getSystemIdentifier().append("systemID");
        assertEquals("systemID", doctypeToken.getSystemIdentifier());

        assertTrue(doctypeToken.isDoctype());
        assertFalse(doctypeToken.isStartTag());
        assertFalse(doctypeToken.isEndTag());
        assertFalse(doctypeToken.isComment());
        assertFalse(doctypeToken.isCharacter());
        assertFalse(doctypeToken.isEOF());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag().name("div");
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.name());
        assertNull(startTagToken.normalName());
        assertFalse(startTagToken.isDoctype());
        assertFalse(startTagToken.isStartTag());
        assertFalse(startTagToken.isEndTag());
        assertFalse(startTagToken.isComment());
        assertFalse(startTagToken.isCharacter());
        assertFalse(startTagToken.isEOF());

        Token resetStartTagToken = startTagToken.reset();
        assertSame(startTagToken, resetStartTagToken);

        startTagToken.newAttribute().appendName("class").appendValue("test");
        assertFalse(startTagToken.hasPendingAttributeValue());
        assertEquals("class=test", startTagToken.toString());

        startTagToken.appendAttributeName("style");
        startTagToken.appendAttributeValue("color:red");
        assertEquals("div class=test style=\"color:red\"", startTagToken.toString());

        assertTrue(startTagToken.isStartTag());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag().name("div");
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("div", endTagToken.name());
        assertNull(endTagToken.normalName());
        assertFalse(endTagToken.isDoctype());
        assertFalse(endTagToken.isStartTag());
        assertFalse(endTagToken.isEndTag());
        assertFalse(endTagToken.isComment());
        assertFalse(endTagToken.isCharacter());
        assertFalse(endTagToken.isEOF());

        Token resetEndTagToken = endTagToken.reset();
        assertSame(endTagToken, resetEndTagToken);

        assertTrue(endTagToken.isEndTag());
        assertEquals("</div>", endTagToken.toString());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertSame(commentToken, resetCommentToken);

        commentToken.getData().append("--test");
        assertEquals("<!--test-->", commentToken.toString());

        assertTrue(commentToken.isComment());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertSame(characterToken, resetCharacterToken);

        characterToken.data("&amp;");
        assertEquals("&amp;", characterToken.getData());
        assertEquals("&amp;", characterToken.toString());

        assertTrue(characterToken.isCharacter());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertSame(eofToken, resetEOFToken);

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());
    }
}