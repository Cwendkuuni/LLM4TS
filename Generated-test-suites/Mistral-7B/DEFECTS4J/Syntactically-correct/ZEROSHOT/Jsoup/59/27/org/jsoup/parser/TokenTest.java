package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.asDoctype().getName = "html";
        Assert.assertEquals("html", doctypeToken.tokenType());
        Assert.assertNull(doctypeToken.asDoctype().getPubSysKey());
        Assert.assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.asDoctype().setName("html");
        doctypeToken.asDoctype().setPublicIdentifier("public id");
        doctypeToken.asDoctype().setSystemIdentifier("system id");
        doctypeToken.asDoctype().setForceQuirks(true);

        Assert.assertEquals("html", doctypeToken.tokenType());
        Assert.assertEquals("public id", doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertEquals("system id", doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());

        Assert.assertEquals(doctypeToken.reset(), doctypeToken);
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Assert.assertEquals("StartTag", tagToken.tokenType());

        tagToken.asStartTag().name("div");
        Assert.assertEquals("div", tagToken.tokenType());
        Assert.assertNull(tagToken.asStartTag().getAttributes());

        tagToken.asStartTag().name("div").attributes(new Attributes().put("id", "test"));
        Assert.assertEquals("div", tagToken.tokenType());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("id"));

        tagToken.asStartTag().name("div").attributes(new Attributes().put("class", "test"));
        Assert.assertEquals("div", tagToken.tokenType());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertEquals(1, tagToken.asStartTag().getAttributes().size());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("class").getValue());

        tagToken.asStartTag().newAttribute();
        tagToken.asStartTag().name("div").attributes(new Attributes().put("id", "test").put("class", "test2"));
        Assert.assertEquals("div", tagToken.tokenType());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertEquals(2, tagToken.asStartTag().getAttributes().size());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("id").getValue());
        Assert.assertEquals("test2", tagToken.asStartTag().getAttributes().get("class").getValue());

        tagToken.asStartTag().finaliseTag();
        Assert.assertNull(tagToken.asStartTag().getAttributes());

        Assert.assertEquals(tagToken.reset(), tagToken);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals("Comment", commentToken.tokenType());

        commentToken.asComment().getData("-- This is a comment");
        Assert.assertEquals("-- This is a comment", commentToken.tokenType());

        Assert.assertEquals(commentToken.reset(), commentToken);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Assert.assertEquals("Character", characterToken.tokenType());

        characterToken.asCharacter().getData("A");
        Assert.assertEquals("A", characterToken.tokenType());

        Assert.assertEquals(characterToken.reset(), characterToken);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Assert.assertEquals("EOF", eofToken.tokenType());

        Assert.assertEquals(eofToken.reset(), eofToken);
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

        for (Class<? extends Token> tokenClass : Token.class.getDeclaredClasses()) {
            Token token = null;
            try {
                token = tokenClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(token);
            Assert.assertEquals(token.tokenType(), tokenClass.getSimpleName());
        }
    }
}