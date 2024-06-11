package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.TokenType.*;

import java.util.Arrays;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        Assert.assertEquals(TOKEN_TYPE_DOCTYPE, resetDoctypeToken.tokenType());
        Assert.assertNull(resetDoctypeToken.getName());
        Assert.assertNull(resetDoctypeToken.getPubSysKey());
        Assert.assertNull(resetDoctypeToken.getPublicIdentifier());
        Assert.assertNull(resetDoctypeToken.getSystemIdentifier());
        Assert.assertFalse(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Token resetTagToken = tagToken.reset();

        Assert.assertEquals(TOKEN_TYPE_START_TAG, resetTagToken.tokenType());
        Assert.assertNull(resetTagToken.getName());
        Assert.assertNull(resetTagToken.getAttributes());
        Assert.assertFalse(resetTagToken.isSelfClosing());

        tagToken.name("html");
        resetTagToken = tagToken.reset();

        Assert.assertEquals("html", resetTagToken.getName());
        Assert.assertNull(resetTagToken.getAttributes());
        Assert.assertFalse(resetTagToken.isSelfClosing());

        tagToken.selfClosing(true);
        resetTagToken = tagToken.reset();

        Assert.assertEquals("html", resetTagToken.getName());
        Assert.assertNull(resetTagToken.getAttributes());
        Assert.assertTrue(resetTagToken.isSelfClosing());

        tagToken.attributes(new Attributes().put("id", "root"));
        resetTagToken = tagToken.reset();

        Assert.assertEquals("html", resetTagToken.getName());
        Assert.assertEquals("root", resetTagToken.getAttributes().get("id"));
        Assert.assertFalse(resetTagToken.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        Assert.assertEquals(TOKEN_TYPE_COMMENT, resetCommentToken.tokenType());
        Assert.assertNull(resetCommentToken.getData());
        Assert.assertFalse(resetCommentToken.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Character characterToken = new Token.Character();
        Character resetCharacterToken = characterToken.reset();

        Assert.assertEquals(TOKEN_TYPE_CHARACTER, resetCharacterToken.tokenType());
        Assert.assertNull(resetCharacterToken.getData());

        characterToken.data("A");
        resetCharacterToken = characterToken.reset();

        Assert.assertEquals('A', resetCharacterToken.getData().charAt(0));
        Assert.assertEquals("A", resetCharacterToken.getData());
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("<![CDATA[Hello, World!]]>");
        Token resetCDataToken = cDataToken.reset();

        Assert.assertEquals(TOKEN_TYPE_CHARACTER, resetCDataToken.tokenType());
        Assert.assertEquals("Hello, World!", resetCDataToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        Assert.assertEquals(TOKEN_TYPE_EOF, resetEofToken.tokenType());
    }

    @Test
    public void testResetMultipleTokens() {
        Token doctypeToken = new Token.Doctype();
        Token tagToken = new Token.StartTag();
        Token commentToken = new Token.Comment();
        Token eofToken = new Token.EOF();

        Token[] tokens = {doctypeToken, tagToken, commentToken, eofToken};
        Token[] resetTokens = new Token[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];
            Token resetToken = token.reset();
            resetTokens[i] = resetToken;
        }

        for (int i = 0; i < resetTokens.length; i++) {
            Token token = tokens[i];
            Token resetToken = resetTokens[i];

            assertEquals(token.tokenType(), resetToken.tokenType());

            switch (TokenType.valueOf(token.tokenType())) {
                case DOCTYPE:
                    Assert.assertNull(resetToken.getName());
                    Assert.assertNull(resetToken.getPubSysKey());
                    Assert.assertNull(resetToken.getPublicIdentifier());
                    Assert.assertNull(resetToken.getSystemIdentifier());
                    Assert.assertFalse(resetToken.isForceQuirks());
                    break;
                case START_TAG:
                    Assert.assertNull(resetToken.getName());
                    Assert.assertNull(resetToken.getAttributes());
                    Assert.assertFalse(resetToken.isSelfClosing());
                    break;
                case END_TAG:
                    Assert.assertNull(resetToken.getName());
                    Assert.assertNull(resetToken.getAttributes());
                    Assert.assertFalse(resetToken.isSelfClosing());
                    break;
                case COMMENT:
                    Assert.assertNull(resetToken.getData());
                    Assert.assertFalse(resetToken.isBogus());
                    break;
                case CHARACTER:
                    Assert.assertNull(resetToken.getData());
                    break;
                case EOF:
                    break;
            }
        }
    }
}