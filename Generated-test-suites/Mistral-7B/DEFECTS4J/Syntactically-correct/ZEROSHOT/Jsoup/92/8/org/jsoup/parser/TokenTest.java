package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

/**
 * Test class for Token abstract class and its subclasses.
 */
public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("html");
        doctype.getPublicIdentifier().append("public");
        doctype.getSystemIdentifier().append("system");
        doctype.setForceQuirks(true);

        Token.Doctype resetDoctype = doctype.reset();
        assertEquals(doctype.getClass(), resetDoctype.getClass());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.appendTagName("class");
        tag.appendAttributeValue("test");
        tag.setEmptyAttributeValue();

        Token.Tag resetTag = tag.reset();
        assertEquals(tag.getClass(), resetTag.getClass());
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals(0, resetTag.pendingAttributeValue.length());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        resetTag.finaliseTag();
        assertNull(resetTag.attributes);
    }

    @Test
    public void testTagNameAndNormalName() {
        Token.Tag tag = new Token.StartTag().name("Div");
        assertEquals("Div", tag.name());
        assertEquals("div", tag.normalName());

        Token.Tag tagLower = new Token.StartTag().name("DIV");
        assertEquals("DIV", tagLower.name());
        assertEquals("div", tagLower.normalName());

        Token.Tag tagUpper = new Token.StartTag().name("DIV");
        tagUpper.name("div");
        assertEquals("div", tagUpper.name());
        assertEquals("div", tagUpper.normalName());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag().name("div").appendTagName("class").appendAttributeValue("test");
        tag.finaliseTag();
        assertNotNull(tag.getAttributes());
        assertEquals(1, tag.getAttributes().size());
        assertEquals("test", tag.getAttributes().get("class"));
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        Token character = new Token.Character();
        assertTrue(character.isCharacter());
        Token endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
        Token startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}