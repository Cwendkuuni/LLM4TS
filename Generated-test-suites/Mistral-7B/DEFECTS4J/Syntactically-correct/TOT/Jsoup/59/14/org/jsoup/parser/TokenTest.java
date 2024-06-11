package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;
import java.util.List;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "DTD HTML";
        doctype.publicIdentifier.append("DTD HTML HTML 4.01");
        doctype.systemIdentifier.append("/usr/local/DTDs/html.dtd");
        doctype.forceQuirks = true;

        Token expectedDoctype = new Doctype()
                .name("html")
                .pubSysKey("DTD HTML")
                .publicIdentifier("DTD HTML HTML 4.01")
                .systemIdentifier("/usr/local/DTDs/html.dtd")
                .forceQuirks(true);

        Assert.assertEquals(expectedDoctype, doctype);
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());

        doctype.reset();
        Assert.assertNull(doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertNull(doctype.getPublicIdentifier());
        Assert.assertNull(doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("html").attributes(new Attributes().put("lang", "en"));

        Token expectedStartTag = new StartTag()
                .name("html")
                .attributes(new Attributes().put("lang", "en"));

        Assert.assertEquals(expectedStartTag, startTag);
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());
        Assert.assertEquals("<html lang=\"en\">", startTag.toString());

        startTag.reset();
        Assert.assertNull(startTag.getName());
        Assert.assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag().name("html");

        Token expectedEndTag = new EndTag().name("html");

        Assert.assertEquals(expectedEndTag, endTag);
        Assert.assertEquals(TokenType.EndTag, endTag.tokenType());
        Assert.assertEquals("</html>", endTag.toString());

        endTag.reset();
        Assert.assertNull(endTag.getName());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment().data("--This is a comment");

        Token expectedComment = new Comment().data("--This is a comment");

        Assert.assertEquals(expectedComment, comment);
        Assert.assertEquals(TokenType.Comment, comment.tokenType());
        Assert.assertEquals("<!--This is a comment-->", comment.toString());

        comment.reset();
        Assert.assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacter() {
        Character character = new Character().data("A");

        Token expectedCharacter = new Character().data("A");

        Assert.assertEquals(expectedCharacter, character);
        Assert.assertEquals(TokenType.Character, character.tokenType());
        Assert.assertEquals("A", character.getData());

        character.reset();
        Assert.assertNull(character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new EOF();

        Token expectedEOF = new EOF();

        Assert.assertEquals(expectedEOF, eof);
        Assert.assertEquals(TokenType.EOF, eof.tokenType());

        eof.reset();
        Assert.assertEquals(TokenType.EOF, eof.tokenType());
    }

    @Test
    public void testResetToken() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");

        StartTag startTag = new StartTag();
        startTag.name("html");

        Comment comment = new Comment();
        comment.data("--This is a comment");

        Character character = new Character();
        character.data("A");

        EOF eof = new EOF();

        List<Token> tokens = Arrays.asList(doctype, startTag, comment, character, eof);

        for (Token token : tokens) {
            token.reset();
        }

        for (Token token : tokens) {
            Assert.assertEquals(TokenType.TokenType.EOF, token.tokenType());
        }
    }

    @Test
    public void testForceQuirksDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.forceQuirks = true;

        Token expectedDoctype = new Doctype()
                .name("html")
                .forceQuirks(true);

        Assert.assertEquals(expectedDoctype, doctype);
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());

        doctype.reset();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testEmptyAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.attributes(new Attributes().put("class", ""));

        Token expectedStartTag = new StartTag()
                .attributes(new Attributes().put("class", ""));

        Assert.assertEquals(expectedStartTag, startTag);
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());

        startTag.reset();
        Assert.assertNull(startTag.getAttributes());
        Assert.assertFalse(startTag.hasEmptyAttributeValue());
    }

    @Test
    public void testPendingAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test1 test2");

        Token expectedStartTag = new StartTag()
                .attributes(new Attributes().put("class", "test1 test2"));

        startTag.newAttribute();

        Assert.assertEquals(expectedStartTag, startTag);
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());

        startTag.reset();
        Assert.assertNull(startTag.getAttributes());
        Assert.assertFalse(startTag.hasPendingAttributeValue());
    }
}