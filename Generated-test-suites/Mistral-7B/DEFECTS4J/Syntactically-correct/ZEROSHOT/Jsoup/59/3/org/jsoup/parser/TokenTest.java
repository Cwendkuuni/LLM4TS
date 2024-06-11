package org.jsoup.parser;

import org.junit.Test;
import org.junit. Assert;

import static org.jsoup.parser.TokenType.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.tokenType());
        token.reset();
        Assert.assertNull(token.getName());
        Assert.assertNull(token.getPubSysKey());
        Assert.assertNull(token.getPublicIdentifier());
        Assert.assertNull(token.getSystemIdentifier());
        Assert.assertFalse(token.isForceQuirks());
    }

    @Test
    public void testDoctypeTokenWithData() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName().append("html");
        doctypeToken.getPublicIdentifier().append("Public ID:");
        doctypeToken.getSystemIdentifier().append("System ID:");
        doctypeToken.isForceQuirks(true);
        Token resetDoctypeToken = doctypeToken.reset();
        Assert.assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());
        Assert.assertNull(resetDoctypeToken.getName());
        Assert.assertNull(resetDoctypeToken.getPubSysKey());
        Assert.assertNull(resetDoctypeToken.getPublicIdentifier());
        Assert.assertNull(resetDoctypeToken.getSystemIdentifier());
        Assert.assertFalse(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, token.tokenType());
        token.reset();
        Assert.assertNull(token.tagName);
        Assert.assertNull(token.normalName);
        Assert.assertNull(token.pendingAttributeName);
        Assert.assertNull(token.pendingAttributeValue);
        Assert.assertNull(token.pendingAttributeValueS);
        Assert.assertFalse(token.hasEmptyAttributeValue);
        Assert.assertFalse(token.hasPendingAttributeValue);
        Assert.assertFalse(token.selfClosing);
        Assert.assertNull(token.attributes);
    }

    @Test
    public void testStartTagTokenWithData() {
        Token startTagToken = new Token.StartTag();
        startTagToken.tagName = "div";
        startTagToken.normalName = "div";
        startTagToken.selfClosing = true;
        startTagToken.newAttribute();
        startTagToken.appendAttributeName("class");
        startTagToken.appendAttributeValue("someClass");
        startTagToken.newAttribute();
        startTagToken.appendAttributeName("id");
        startTagToken.appendAttributeValue("someId");
        Token resetStartTagToken = startTagToken.reset();
        Assert.assertEquals(TokenType.StartTag, resetStartTagToken.tokenType());
        Assert.assertNull(resetStartTagToken.tagName);
        Assert.assertNull(resetStartTagToken.normalName);
        Assert.assertNull(resetStartTagToken.pendingAttributeName);
        Assert.assertNull(resetStartTagToken.pendingAttributeValue);
        Assert.assertNull(resetStartTagToken.pendingAttributeValueS);
        Assert.assertFalse(resetStartTagToken.hasEmptyAttributeValue);
        Assert.assertFalse(resetStartTagToken.hasPendingAttributeValue);
        Assert.assertFalse(resetStartTagToken.selfClosing);
        Assert.assertNull(resetStartTagToken.attributes);
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, token.tokenType());
        token.reset();
        Assert.assertNull(token.tagName);
        Assert.assertNull(token.normalName);
        Assert.assertFalse(token.selfClosing);
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        Assert.assertEquals(TokenType.Character, token.tokenType());
        token.reset();
        Assert.assertNull(token.getData());
    }

    @Test
    public void testCharacterTokenWithData() {
        Token characterToken = new Token.Character();
        characterToken.data("ACharacter");
        Token resetCharacterToken = characterToken.reset();
        Assert.assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        Assert.assertNull(resetCharacterToken.getData());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, token.tokenType());
        token.reset();
        Assert.assertFalse(token.bogus);
    }

    @Test
    public void testCommentTokenWithData() {
        Token commentToken = new Token.Comment();
        commentToken.getData().append("This is a comment");
        Token resetCommentToken = commentToken.reset();
        Assert.assertEquals(TokenType.Comment, resetCommentToken.tokenType());
        Assert.assertFalse(resetCommentToken.bogus);
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, token.tokenType());
        token.reset();
        // EOF token cannot be reset
    }
}