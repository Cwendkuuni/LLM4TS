package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

/**
 * Test class for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(Token.Doctype.class, doctypeToken.getClass());
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals(Token.Doctype.class, resetDoctypeToken.getClass());

        TokenType doctypeType = resetDoctypeToken.isDoctype();
        assertTrue(doctypeType == TokenType.Doctype);

        String doctypeName = new Token.Doctype().getName();
        assertEquals("", doctypeName);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals(Token.StartTag.class, startTagToken.getClass());
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals(Token.StartTag.class, resetStartTagToken.getClass());

        TokenType startTagType = resetStartTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        String startTagName = new Token.StartTag().name();
        assertEquals("", startTagName);

        Token startTagWithName = new Token.StartTag().name("html");
        assertEquals("html", startTagWithName.name());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals(Token.EndTag.class, endTagToken.getClass());
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals(Token.EndTag.class, resetEndTagToken.getClass());

        TokenType endTagType = resetEndTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        String endTagName = new Token.EndTag().name();
        assertEquals("", endTagName);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals(Token.Comment.class, commentToken.getClass());
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals(Token.Comment.class, resetCommentToken.getClass());

        TokenType commentType = resetCommentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        String commentData = new Token.Comment().getData();
        assertEquals("", commentData);

        Token commentWithData = new Token.Comment().data("This is a comment.");
        assertEquals("This is a comment.", commentWithData.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals(Token.Character.class, characterToken.getClass());
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals(Token.Character.class, resetCharacterToken.getClass());

        TokenType characterType = resetCharacterToken.isCharacter();
        assertTrue(characterType == TokenType.Character);

        String characterData = new Token.Character().getData();
        assertEquals("", characterData);

        Token characterWithData = new Token.Character().data("A");
        assertEquals("A", characterWithData.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals(Token.EOF.class, eofToken.getClass());
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertEquals(Token.EOF.class, resetEOFToken.getClass());

        TokenType eofType = resetEOFToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }

    @Test
    public void testResetToken() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token eofToken = new Token.EOF();

        // Test resetting a Doctype token
        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertTrue(resetDoctypeToken.isDoctype());

        // Test resetting a StartTag token
        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertTrue(resetStartTagToken.isStartTag());

        // Test resetting an EndTag token
        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());
        assertTrue(resetEndTagToken.isEndTag());

        // Test resetting a Comment token
        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());
        assertTrue(resetCommentToken.isComment());

        // Test resetting a Character token
        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());
        assertTrue(resetCharacterToken.isCharacter());

        // Test resetting an EOF token
        Token resetEOFToken = eofToken.reset();
        assertEquals("EOF", resetEOFToken.tokenType());
        assertTrue(resetEOFToken.isEOF());
    }
}