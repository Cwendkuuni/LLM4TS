package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><script src=\"test.js\"></script></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        errors = tokeniser.getErrors();
    }

    @Test
    public void testRead_NormalCase() {
        Token token;
        for (int i = 0; i < 15; i++) {
            token = tokeniser.read();
            assertNotNull(token);
            if (token.type == Token.TokenType.StartTag) {
                Tag startTag = (Tag) token;
                assertEquals("html", startTag.tagName.toLowerCase());
                assertEquals(Tag.TagType.Start, startTag.tagType());
                if (i == 1) {
                    assertEquals(Tag.TagName.HTML, startTag.tagName());
                }
                if (i > 1) {
                    assertEquals("head", startTag.tagName().toLowerCase());
                }
            } else if (token.type == Token.TokenType.EndTag) {
                Tag endTag = (Tag) token;
                assertEquals("html", endTag.tagName().toLowerCase());
                assertEquals(Tag.TagType.End, endTag.tagType());
                assertEquals(Tag.TagName.HTML, endTag.tagName());
            } else if (token.type == Token.TokenType.Character) {
                Token.Character charToken = (Token.Character) token;
                assertEquals(Character.isLetterOrDigit(charToken.data().charAt(0)), true);
            } else if (token.type == Token.TokenType.Comment) {
                Token.Comment comment = (Token.Comment) token;
                assertEquals(comment.data(), "-->"); // empty comment
            }
        }
        assertFalse(tokeniser.getErrors().hasErrors());
        assertNull(tokeniser.read());
    }

    @Test
    public void testRead_SelfClosingFlagError() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<script/>".getBytes())), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read();
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        Tag endTag = (Tag) token;
        assertEquals("script", endTag.tagName().toLowerCase());
        assertEquals(Tag.TagType.End, endTag.tagType());
        assertEquals(Tag.TagName.SCRIPT, endTag.tagName());
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "Self closing flag not acknowledged");
    }

    @Test
    public void testRead_EmptyInput() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), new ParseErrorList());
        assertFalse(tokeniser.getErrors().hasErrors());
        assertNull(tokeniser.read());
    }

    @Test
    public void testRead_UnexpectedEndOfFile() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html>".getBytes())), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.EndDocument);
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "Unexpectedly reached end of file (EOF) in input state [EndDocument]");
    }

    @Test
    public void testRead_CharacterReferenceError() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html>&#x0;")), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        Token.Character charToken = (Token.Character) token;
        assertEquals(charToken.data(), String.valueOf(tokeniser.replacementChar));
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "character outside of valid range");
    }

    @Test
    public void testRead_CharacterReferenceError_InvalidCharacters() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html>&$#%")), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(codeRef);
        assertEquals(errors.size(), 3);
        ParseError error1 = errors.getError(0);
        assertEquals(error1.getMessage(), "Invalid character reference: invalid named referenece '%&#'");
        ParseError error2 = errors.getError(1);
        assertEquals(error2.getMessage(), "Invalid character reference: invalid named referenece '%$'");
        ParseError error3 = errors.getError(2);
        assertEquals(error3.getMessage(), "Invalid character reference: invalid named referenece '%#%'");
    }

    @Test
    public void testRead_CharacterReferenceError_InvalidNamedReference() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html>&foo;")), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(codeRef);
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "invalid named referenece '%foo'");
    }

    @Test
    public void testRead_CharacterReferenceError_MissingSemicolon() {
        tokeniser = new CharacterReader(new ByteArrayInputStream("<html>&#123;"));
        Tokeniser tokeniserUnderTest = new Tokeniser(tokeniser, new ParseErrorList());
        tokeniserUnderTest.consumeCharacterReference(null, false);
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "missing semicolon");
    }

    @Test
    public void testCreateTagPending() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body>")), new ParseErrorList());
        Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tag = tokeniser.read();
        assertEquals(tag.type, Token.TokenType.StartTag);
        assertEquals(tag.tagName(), "html");
        assertEquals(tag.tagName().toLowerCase(), "html");
        tagPending = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.error(TokeniserState.EndDocument);
        Tag endTag = tokeniser.read();
        assertEquals(endTag.type, Token.TokenType.EndTag);
        assertEquals(endTag.tagName(), "html");
        assertEquals(endTag.tagName().toLowerCase(), "html");
    }

    @Test
    public void testRead_InappropriateEndTag() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><script></script></head>")), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Tag startTag = tokeniser.createTagPending(true);
        Token endTag = tokeniser.read();
        assertNotNull(endTag);
        assertEquals(endTag.type, Token.TokenType.EndTag);
        assertEquals(endTag.tagName(), "title");
        assertFalse(tokeniser.isAppropriateEndTagToken());
        assertEquals(errors.size(), 1);
        ParseError error = errors.getError(0);
        assertEquals(error.getMessage(), "inappropriate end tag name: title");
    }

    @Test
    public void testRead_AppropriateEndTag() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><script><script>")), new ParseErrorList());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.read();
        tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.error(TokeniserState.EndDocument);
        tokeniser.read();
        tagPending = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        Token endTag = tokeniser.read();
        assertEquals(endTag.type, Token.TokenType.EndTag);
        assertEquals(endTag.tagName(), "script");
        assertEquals(endTag.tagName().toLowerCase(), "script");
        assertEquals(isAppropriateEndTagToken(), true);
        assertEquals(errors.size(), 0);
    }

    @Test
    public void testUnescapeEntities() {
        String input = "&amp;amp;amp;lt;p&gt;Hello, &quot;world!&quot; &amp; #123; &nbsp; &amp;#x61; &amp;#x6C; &amp;#x6C; &amp;#x6F; &amp;#x72;!";
        String expected = "&amp;amp;lt;p&gt;Hello, &quot;world!&quot; &amp; #123; &nbsp; a;l;e;";
        assertEquals(tokeniser.unescapeEntities(false), expected);
    }

    @Test
    public void testUnescapeEntities_InAttribute() {
        String input = "class=\"&nbsp;myClass&amp;#x2022;&quot;";
        String expected = "class=\"&nbsp;myClass&#x2022;\"";
        assertEquals(tokeniser.unescapeEntities(true), expected);
    }
}