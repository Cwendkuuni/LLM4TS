package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Tokenizer;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;

import java.util.Arrays;

public class TokeniserTest {

    @Test
    public void testTokeniserEmptyInput() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
        Assert.assertNull(token.charPosition());
    }

    @Test
    public void testTokeniserSingleCharacter() {
        String input = "A";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('A', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertNull(token.selfClosing());
        Assert.assertNull(token.attributes());
    }

    @Test
    public void testTokeniserSingleTag() {
        String input = "<tag>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertNotNull(tag);
        Assert.assertEquals("tag", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNull(tag.attributes());
    }

    @Test
    public void testTokeniserSingleTagWithAttributes() {
        String input = "<tag attr1='val1' attr2='val2'/>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, startTag.type);
        Tag tag = startTag.tag();
        Assert.assertEquals("tag", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNotNull(tag.attributes());
        Assert.assertEquals(2, tag.attributes().size());
        Assert.assertEquals("attr1", tag.attributes().get("attr1").getKey());
        Assert.assertEquals("val1", tag.attributes().get("attr1").getValue());
        Assert.assertEquals("attr2", tag.attributes().get("attr2").getKey());
        Assert.assertEquals("val2", tag.attributes().get("attr2").getValue());

        Token endTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.END_TAG, endTag.type);
        Assert.assertEquals(tag.name(), endTag.tag().name());
        Assert.assertNull(endTag.tag().tagNamePrefix());
        Assert.assertNull(endTag.tag().namespaceURI());
        Assert.assertNull(endTag.tag().prefix());
        Assert.assertNull(endTag.attributes());
    }

    @Test
    public void testTokeniserSelfClosingTag() {
        String input = "<tag selfClosing='true'/>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, startTag.type);
        Tag tag = startTag.tag();
        Assert.assertEquals("tag", tag.name());
        Assert.assertTrue(tag.selfClosing());
        Assert.assertNull(tag.attributes());

        Token endTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.END_TAG, endTag.type);
        Assert.assertEquals(tag.name(), endTag.tag().name());
        Assert.assertFalse(endTag.tag().selfClosing());
        Assert.assertNull(endTag.attributes());
    }

    @Test
    public void testTokeniserCharacterReference() {
        String input = "<p>&#160;</p>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertEquals("p", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNull(tag.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('\u00A0', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokeniserNamedCharacterReference() {
        String input = "<p>&nbsp;</p>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertEquals("p", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNull(tag.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('\u00A0', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokeniserInvalidCharacterReference() {
        String input = "<p>&#foo;</p>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertEquals("p", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNull(tag.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.ERROR, token.type);
        Assert.assertEquals("invalid character reference: invalid named referenece 'foo'", token.error().getMessage());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokeniserDoctypeDeclaration() {
        String input = "<!DOCTYPE html>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.DOCTYPE, token.type);
        Doctype doctype = token.doctype();
        Assert.assertEquals("html", doctype.name());
        Assert.assertNull(doctype.publicIdentifier());
        Assert.assertNull(doctype.systemIdentifier());
        Assert.assertNull(doctype.internalSubset());
        Assert.assertNull(doctype.externalSubset());
    }

    @Test
    public void testTokeniserComment() {
        String input = "<!-- comment -->";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertEquals("!--", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNotNull(tag.attributes());
        Assert.assertEquals("xml-comment-text", tag.attributes().get("xml:space").getValue());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals(' ' , token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('-', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('-', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('-', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CHAR, token.type);
        Assert.assertEquals('>', token.charPosition());
        Assert.assertNull(token.tag());
        Assert.assertNull(token.data());
        Assert.assertFalse(token.selfClosing());
        Assert.assertNull(token.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokeniserInvalidInput() {
        String input = "<p&>";
        CharacterReader reader = new CharacterReader(input) {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.START_TAG, token.type);
        Assert.assertNull(token.charPosition());
        Tag tag = token.tag();
        Assert.assertEquals("p", tag.name());
        Assert.assertNull(tag.tagNamePrefix());
        Assert.assertNull(tag.namespaceURI());
        Assert.assertNull(tag.prefix());
        Assert.assertNull(tag.attributes());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.ERROR, token.type);
        Assert.assertEquals("Unexpected character '&' in input state [Data]", token.error().getMessage());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EOF, token.type);
    }
}