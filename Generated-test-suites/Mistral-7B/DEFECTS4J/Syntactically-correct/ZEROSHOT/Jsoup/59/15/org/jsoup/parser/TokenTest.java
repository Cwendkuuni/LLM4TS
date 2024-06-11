package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

/**
 * Test the Token class and its subclasses.
 */
public class TokenTest {

    /**
     * Test the Doctype class.
     */
    @Test
    public void testDoctype() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();

        assertEquals("Doctype", token.tokenType());
        assertSame(token, resetToken);

        token.getName().append("html");
        assertEquals("html", token.getName());

        token.setForceQuirks(true);
        assertTrue(token.isForceQuirks());

        token.getPubSysKey() = "testpubsyskey";
        assertEquals("testpubsyskey", token.getPubSysKey());

        token.getPublicIdentifier().append("testpublicid");
        assertEquals("testpublicid", token.getPublicIdentifier().toString());

        token.getSystemIdentifier().append("testsystemid");
        assertEquals("testsystemid", token.getSystemIdentifier().toString());
    }

    /**
     * Test the Tag class.
     */
    @Test
    public void testTag() {
        Token tagToken = new Token.Tag();
        Token resetTagToken = tagToken.reset();

        assertEquals("Tag", tagToken.tokenType());
        assertSame(tagToken, resetTagToken);

        tagToken.newAttribute();
        tagToken.appendAttributeName("testattr").append("=").append("testvalue");
        tagToken.newAttribute();

        Attributes attributes = tagToken.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("testattr");
        assertEquals("testvalue", attribute.getValue());

        tagToken.finaliseTag();
        assertEquals(2, attributes.size());
        assertNull(tagToken.getPendingAttributeName());
        assertFalse(tagToken.hasPendingAttributeValue());
        assertNull(tagToken.getPendingAttributeValueS());
        assertFalse(tagToken.hasEmptyAttributeValue());
        assertNull(tagToken.getPendingAttributeValue());
    }

    /**
     * Test the StartTag class.
     */
    @Test
    public void testStartTag() {
        Token startTagToken = new Token.StartTag();
        Token resetStartTagToken = startTagToken.reset();

        assertEquals("StartTag", startTagToken.tokenType());
        assertSame(startTagToken, resetStartTagToken);

        startTagToken.name("testtag");
        assertEquals("testtag", startTagToken.name());

        Attributes attributes = new Attributes();
        startTagToken.newAttribute().appendName("testattr").append("=").append("testvalue");
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("testattr");
        assertEquals("testvalue", attribute.getValue());

        startTagToken.finaliseTag();
        assertEquals(1, attributes.size());
        assertEquals("testtag", startTagToken.name());
        assertEquals("testtag", startTagToken.normalName());
        assertTrue(startTagToken.isSelfClosing());

        String tagString = startTagToken.toString();
        assertEquals("<testtag testattr=\"testvalue\">", tagString);
    }

    /**
     * Test the EndTag class.
     */
    @Test
    public void testEndTag() {
        Token endTagToken = new Token.EndTag();
        Token resetEndTagToken = endTagToken.reset();

        assertEquals("EndTag", endTagToken.tokenType());
        assertSame(endTagToken, resetEndTagToken);

        endTagToken.name("testtag");
        assertEquals("testtag", endTagToken.name());

        String tagString = endTagToken.toString();
        assertEquals("</testtag>", tagString);
    }

    /**
     * Test the Comment class.
     */
    @Test
    public void testComment() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals("Comment", commentToken.tokenType());
        assertSame(commentToken, resetCommentToken);

        commentToken.getData().append("testcomment");
        assertEquals("testcomment", commentToken.getData().toString());

        String commentString = commentToken.toString();
        assertEquals("<!--testcomment-->", commentString);
    }

    /**
     * Test the Character class.
     */
    @Test
    public void testCharacter() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals("Character", characterToken.tokenType());
        assertSame(characterToken, resetCharacterToken);

        characterToken.data("testchar");
        assertEquals("testchar", characterToken.getData());

        String characterString = characterToken.toString();
        assertEquals("testchar", characterString);
    }

    /**
     * Test the EOF class.
     */
    @Test
    public void testEOF() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertEquals("EOF", eofToken.tokenType().toString());
        assertSame(eofToken, resetEofToken);

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());

        String eofString = eofToken.toString();
        assertEquals("", eofString);
    }
}