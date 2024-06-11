import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        String name = "html";
        Token expectedDoctypeToken = new Token.Doctype()
                .getName(name)
                .pubSysKey("system-pub-id")
                .publicIdentifier("public-id")
                .systemIdentifier("system-id")
                .isForceQuirks(true);
        Token actualDoctypeToken = doctypeToken.asDoctype();
        assertEquals(expectedDoctypeToken.getName(), actualDoctypeToken.getName());
        assertEquals(expectedDoctypeToken.getPubSysKey(), actualDoctypeToken.getPubSysKey());
        assertEquals(expectedDoctypeToken.getPublicIdentifier(), actualDoctypeToken.getPublicIdentifier());
        assertEquals(expectedDoctypeToken.getSystemIdentifier(), actualDoctypeToken.getSystemIdentifier());
        assertEquals(expectedDoctypeToken.isForceQuirks(), actualDoctypeToken.isForceQuirks());

        doctypeToken.reset();
        assertFalse(doctypeToken.isDoctype());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        String tagName = "div";
        Token expectedStartTagToken = new Token.StartTag().name(tagName);
        Token actualStartTagToken = startTagToken.asStartTag();
        assertEquals(expectedStartTagToken.getName(), actualStartTagToken.getName());

        startTagToken.reset();
        assertFalse(startTagToken.isStartTag());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        String tagName = "div";
        Token expectedEndTagToken = new Token.EndTag().name(tagName);
        Token actualEndTagToken = endTagToken.asEndTag();
        assertEquals(expectedEndTagToken.getName(), actualEndTagToken.getName());

        endTagToken.reset();
        assertFalse(endTagToken.isEndTag());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        StringBuilder data = new StringBuilder("This is a comment");
        Token expectedCommentToken = new Token.Comment().getData(data.toString());
        Token actualCommentToken = commentToken.asComment();
        assertEquals(expectedCommentToken.getData(), actualCommentToken.getData());

        commentToken.reset();
        assertFalse(commentToken.isComment());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        String data = "!";
        Token expectedCharacterToken = new Token.Character().data(data);
        Token actualCharacterToken = characterToken.asCharacter();
        assertEquals(expectedCharacterToken.getData(), actualCharacterToken.getData());

        characterToken.reset();
        assertFalse(characterToken.isCharacter());
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("[CDATA[Hello, World!]]");
        assertEquals("Character", cDataToken.tokenType());

        String data = "Hello, World!";
        Token expectedCDataToken = new Token.CData().data(data);
        Token actualCDataToken = cDataToken.asCharacter();
        assertEquals(expectedCDataToken.getData(), actualCDataToken.getData());

        cDataToken.reset();
        assertFalse(cDataToken.isCData());
    }

    @Test
    public void testResetTokens() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();

        doctypeToken.reset();
        assertFalse(doctypeToken.isDoctype());

        startTagToken.reset();
        assertFalse(startTagToken.isStartTag());

        endTagToken.reset();
        assertFalse(endTagToken.isEndTag());

        commentToken.reset();
        assertFalse(commentToken.isComment());

        characterToken.reset();
        assertFalse(characterToken.isCharacter());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        assertEquals(TokenType.EOF, eofToken.type);

        eofToken.reset();
        assertFalse(eofToken.isEOF());
    }
}