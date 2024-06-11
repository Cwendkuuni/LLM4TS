package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(Token.TokenType.Doctype, doctypeToken.tokenType());

        doctypeToken.asDoctype().reset();
        Assert.assertNull(doctypeToken.asDoctype().getName());
        Assert.assertNull(doctypeToken.asDoctype().getPubSysKey());
        Assert.assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.asDoctype().getName("html");
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertEquals("html", doctypeToken.asDoctype().normalName());

        doctypeToken.asDoctype().setPubSysKey("pubsyskey");
        Assert.assertEquals("pubsyskey", doctypeToken.asDoctype().getPubSysKey());

        doctypeToken.asDoctype().getPublicIdentifier().append("publicid");
        Assert.assertEquals("publicid", doctypeToken.asDoctype().getPublicIdentifier().toString());

        doctypeToken.asDoctype().getSystemIdentifier().append("systemid");
        Assert.assertEquals("systemid", doctypeToken.asDoctype().getSystemIdentifier().toString());

        doctypeToken.asDoctype().isForceQuirks(true);
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Assert.assertEquals(Token.TokenType.StartTag, startTagToken.tokenType());

        startTagToken.asStartTag().reset();
        Assert.assertNull(startTagToken.asStartTag().tagName());
        Assert.assertNull(startTagToken.asStartTag().normalName());
        Assert.assertNull(startTagToken.asStartTag().attributes);

        startTagToken.asStartTag().name("div").attributes(new Attributes().put("class", "class1 class2"));
        Assert.assertEquals("div", startTagToken.asStartTag().tagName());
        Assert.assertEquals("div", startTagToken.asStartTag().normalName());
        Assert.assertEquals("class1 class2", startTagToken.asStartTag().getAttributes().toString());

        startTagToken.asStartTag().nameAttr("class", new Attributes().put("class", "class3"));
        Assert.assertEquals("div", startTagToken.tagName());
        Assert.assertEquals("div", startTagToken.normalName());
        Assert.assertEquals("class3", startTagToken.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Assert.assertEquals(Token.TokenType.EndTag, endTagToken.tokenType());

        endTagToken.asEndTag().reset();
        Assert.assertNull(endTagToken.asEndTag().tagName());
        Assert.assertNull(endTagToken.asEndTag().normalName());

        endTagToken.asEndTag().name("div");
        Assert.assertEquals("div", endTagToken.asEndTag().tagName());
        Assert.assertEquals("div", endTagToken.asEndTag().normalName());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals(Token.TokenType.Comment, commentToken.tokenType());

        commentToken.asComment().reset();
        Assert.assertNull(commentToken.asComment().getData());

        commentToken.asComment().getData().append("This is a comment");
        Assert.assertEquals("This is a comment", commentToken.asComment().getData().toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Assert.assertEquals(Token.TokenType.Character, characterToken.tokenType());

        characterToken.asCharacter().reset();
        Assert.assertNull(characterToken.asCharacter().getData());

        characterToken.asCharacter().data("A");
        Assert.assertEquals("A", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Assert.assertEquals(Token.TokenType.EOF, eofToken.tokenType());
    }

    @Test
    public void testTokenTypes() {
        List<Token> tokens = Arrays.asList(
                new Token.Doctype(),
                new Token.StartTag(),
                new Token.EndTag(),
                new Token.Comment(),
                new Token.Character(),
                new Token.EOF()
        );

        for (Token token : tokens) {
            Assert.assertNotNull(token.tokenType());
        }

        for (TokenType tokenType : Token.TokenType.values()) {
            for (Token token : tokens) {
                if (token.tokenType().equals(tokenType)) {
                    Assert.assertTrue(token instanceof Token.Doctype && tokenType == Token.TokenType.Doctype
                            || token instanceof Token.StartTag && tokenType == Token.TokenType.StartTag
                            || token instanceof Token.EndTag && tokenType == Token.TokenType.EndTag
                            || token instanceof Token.Comment && tokenType == Token.TokenType.Comment
                            || token instanceof Token.Character && tokenType == Token.TokenType.Character
                            || token instanceof Token.EOF && tokenType == Token.TokenType.EOF);
                }
            }
        }
    }
}