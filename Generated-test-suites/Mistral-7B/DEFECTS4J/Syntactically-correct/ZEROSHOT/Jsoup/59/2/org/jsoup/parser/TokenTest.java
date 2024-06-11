import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        TokenType tokenType = resetDoctypeToken.isDoctype();
        assertTrue(tokenType == TokenType.Doctype);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType tokenType = resetStartTagToken.isStartTag();
        assertTrue(tokenType == TokenType.StartTag);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType tokenType = resetEndTagToken.isEndTag();
        assertTrue(tokenType == TokenType.EndTag);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType tokenType = resetCommentToken.isComment();
        assertTrue(tokenType == TokenType.Comment);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        TokenType tokenType = resetCharacterToken.isCharacter();
        assertTrue(tokenType == TokenType.Character);
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidTokenCast() {
        Token anyToken = new Token.Doctype();
        Token invalidToken = (Token.EndTag) anyToken;
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidTokenCast2() {
        Token anyToken = new Token.Comment();
        Token invalidToken = (Token.StartTag) anyToken;
    }

    @Test
    public void testDoctypeTokenResetAttributes() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName().append("html");
        doctypeToken.asDoctype().setForceQuirks(true);

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertNull(resetDoctypeToken.asDoctype().getName());
        assertFalse(resetDoctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagTokenResetAttributes() {
        Token startTagToken = new Token.StartTag().name("html");

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertNull(resetStartTagToken.getName());
    }

    @Test
    public void testEndTagTokenResetAttributes() {
        Token endTagToken = new Token.EndTag().name("html");

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());
        assertNull(resetEndTagToken.getName());
    }

    @Test
    public void testCommentTokenResetAttributes() {
        Token commentToken = new Token.Comment().data("This is a comment");

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());
        assertNull(resetCommentToken.getData());
    }

    @Test
    public void testCharacterTokenResetAttributes() {
        Token characterToken = new Token.Character().data('!');

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());
        assertNull(resetCharacterToken.getData());
    }

    @Test
    public void testStartTagTokenNewAttribute() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("html").newAttribute().set("id", "page");

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertTrue(resetStartTagToken.getAttributes() == null);
    }

    @Test
    public void testStartTagTokenFinaliseTag() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("html").newAttribute().set("id", "page");
        startTagToken.asStartTag().finaliseTag();

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertTrue(resetStartTagToken.getAttributes() == null);
    }
}