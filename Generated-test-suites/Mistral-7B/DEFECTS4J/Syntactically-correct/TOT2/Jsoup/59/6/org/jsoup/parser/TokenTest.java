package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("doctype");
        doctype.reset();
        assertEquals(doctype.getClass().getSimpleName(), doctype.tokenType());
    }

    @Test
    public void testDoctypeEdgeCases() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("doctype");
        doctype.reset();

        doctype.getName("InvalidDoctype");
        assertEquals("doctype", doctype.tokenType());

        doctype.pubSysKey = "publicSysKey";
        assertEquals(null, doctype.getPubSysKey());
        doctype.reset();

        doctype.setForceQuirks(true);
        assertFalse(doctype.isForceQuirks());
        doctype.reset();
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name().equals("StartTag");
        tag.reset();
        assertEquals(tag.getClass().getSimpleName(), tag.tokenType());
    }

    @Test
    public void testTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        String attributeName = "attributeName";
        String attributeValue = "attributeValue";
        startTag.appendAttributeName(attributeName);
        startTag.appendAttributeValue(attributeValue);
        startTag.newAttribute();

        assertEquals(attributeName, startTag.getAttributes().getAttribute(attributeName).getName());
        assertEquals(attributeValue, startTag.getAttributes().getAttribute(attributeName).getValue());

        startTag.reset();
        assertEquals(null, startTag.getAttributes());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertEquals(TokenType.Doctype, token.tokenType());

        token = new Token.StartTag();
        assertTrue(token.isStartTag());
        assertEquals(TokenType.StartTag, token.tokenType());

        token = new Token.EndTag();
        assertTrue(token.isEndTag());
        assertEquals(TokenType.EndTag, token.tokenType());

        token = new Token.Comment();
        assertTrue(token.isComment());
        assertEquals(TokenType.Comment, token.tokenType());

        token = new Token.Character();
        assertTrue(token.isCharacter());
        assertEquals(TokenType.Character, token.tokenType());

        token = new Token.EOF();
        assertTrue(token.isEOF());
        assertEquals(TokenType.EOF, token.tokenType());
    }
}