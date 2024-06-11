package org.jsoup.parser.test;

import org.junit.Assert;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().getPublicIdentifier().append("public");
        doctypeToken.asDoctype().getSystemIdentifier().append("/path/to/my/html.html");
        doctypeToken.asDoctype().isForceQuirks(true);

        Token expectedDoctypeToken = new Token.Doctype();
        expectedDoctypeToken.asDoctype().getName("html");
        expectedDoctypeToken.asDoctype().getPublicIdentifier().append("public");
        expectedDoctypeToken.asDoctype().getSystemIdentifier().append("/path/to/my/html.html");
        expectedDoctypeToken.asDoctype().isForceQuirks(true);

        Assert.assertEquals(expectedDoctypeToken.tokenType(), doctypeToken.tokenType());
        Assert.assertEquals(expectedDoctypeToken.getClass(), doctypeToken.getClass());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("html").attributes(new Attributes().put("class", "test"));

        Token expectedStartTagToken = new Token.StartTag();
        expectedStartTagToken.asStartTag().name("html").attributes(new Attributes().put("class", "test"));

        Assert.assertEquals(expectedStartTagToken.tokenType(), startTagToken.tokenType());
        Assert.assertEquals(expectedStartTagToken.getClass(), startTagToken.getClass());

        Assert.assertEquals("html", startTagToken.asStartTag().name());
        Assert.assertEquals("html", startTagToken.asStartTag().normalName());
        Assert.assertEquals(true, startTagToken.asStartTag().isSelfClosing());
        Assert.assertNotNull(startTagToken.asStartTag().getAttributes());
        Assert.assertEquals("test", startTagToken.asStartTag().getAttributes().get("class"));
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("html");

        Token expectedEndTagToken = new Token.EndTag();
        expectedEndTagToken.asEndTag().name("html");

        Assert.assertEquals(expectedEndTagToken.tokenType(), endTagToken.tokenType());
        Assert.assertEquals(expectedEndTagToken.getClass(), endTagToken.getClass());

        Assert.assertEquals("html", endTagToken.asEndTag().name());
        Assert.assertEquals("html", endTagToken.asEndTag().normalName());
        Assert.assertEquals(false, endTagToken.asEndTag().isSelfClosing());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData().append("This is a comment.");

        Token expectedCommentToken = new Token.Comment();
        expectedCommentToken.asComment().getData().append("This is a comment.");

        Assert.assertEquals(expectedCommentToken.tokenType(), commentToken.tokenType());
        Assert.assertEquals(expectedCommentToken.getClass(), commentToken.getClass());

        Assert.assertEquals("<!--This is a comment-->", commentToken.toString());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().data("&amp;");

        Token expectedCharacterToken = new Token.Character();
        expectedCharacterToken.asCharacter().data("&amp;");

        Assert.assertEquals(expectedCharacterToken.tokenType(), characterToken.tokenType());
        Assert.assertEquals(expectedCharacterToken.getClass(), characterToken.getClass());

        Assert.assertEquals("&amp;", characterToken.getData());
        Assert.assertEquals("&amp;", characterToken.toString());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        Token expectedEofToken = new Token.EOF();

        Assert.assertEquals(expectedEofToken.tokenType(), eofToken.tokenType());
        Assert.assertEquals(expectedEofToken.getClass(), eofToken.getClass());

        Assert.assertFalse(eofToken.isDoctype());
        Assert.assertFalse(eofToken.isStartTag());
        Assert.assertFalse(eofToken.isEndTag());
        Assert.assertFalse(eofToken.isComment());
        Assert.assertFalse(eofToken.isCharacter());
    }

    @org.junit.Test
    public void testTokenTypeParsing() {
        String[] tokenTypes = {"Doctype", "StartTag", "EndTag", "Comment", "Character", "EOF"};
        TokenType[] expectedTokenTypes = TokenType.values();

        for (String tokenType : tokenTypes) {
            TokenType actualTokenType = TokenType.valueOf(tokenType);
            Assert.assertEquals(expectedTokenTypes[Arrays.asList(tokenTypes).indexOf(tokenType)], actualTokenType);
        }
    }
}