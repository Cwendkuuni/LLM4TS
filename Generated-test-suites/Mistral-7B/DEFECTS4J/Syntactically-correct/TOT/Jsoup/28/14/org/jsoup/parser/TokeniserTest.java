import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser createTestSubject(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(stream), new ArrayList<>());
        return tokeniser;
    }

    @Test
    public void testEmptyInput() {
        Tokeniser tokeniser = createTestSubject("");
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadSimpleText() {
        Tokeniser tokeniser = createTestSubject("Hello World!");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("H", token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("e", token.characters.get(0));
        // ...
        Assert.assertEquals(TokenType.EndOfInput, token.type, "Unexpected token after 'World'");
    }

    @Test
    public void testReadSimpleHtml() {
        Tokeniser tokeniser = createTestSubject("<html><body><h1>Hello World!</h1></body></html>");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("h1", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadSelfClosingTag() {
        Tokeniser tokeniser = createTestSubject("<img src=\"image.png\" />");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.tagName);
        Assert.assertEquals(true, token.selfClosing);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(' ', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Attribute, token.type);
        Assert.assertEquals("src", token.tag.getAttributeName());
        Assert.assertEquals("image.png", token.tag.getAttributeValue());
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName);
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadComplexHtml() {
        Tokeniser tokeniser = createTestSubject("<html><head><title>Hello World!</title></head><body><div><h1>Header</h1><p>Text<a href=\"http://example.com\">Link</a></p></div></body></html>");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("head", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("title", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("title", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("div", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("h1", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("h1", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("p", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('T', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('e', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('x', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(' ', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("a", token.tagName);
        // ...
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('H', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('t', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('t', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('p', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('/', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('a', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('>', token.characters.get(0));
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("div", token.tagName);
        Assert.assertEquals(TokenType.EndTag, token.read().type);
        Assert.assertEquals(TokenType.EndTag, token.read().type);
        Assert.assertEquals(TokenType.EndTag, token.read().type);
        Assert.assertEquals(TokenType.EndTag, token.read().type);
        Assert.assertEquals(TokenType.EndOfInput, token.read().type);
    }

    @Test
    public void testErrorHandling() {
        Tokeniser tokeniser = createTestSubject("<html><body><script type=\"text/javascript\">");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("script", token.tagName);
        Assert.assertNull(tokeniser.read()); // null character should not be present
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Script, state);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('T', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('h', token.characters.get(0));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('e', token.characters.get(0));
        Assert.assertNull(tokeniser.read()); // '>' character should not be present
        ParseError error = tokeniser.errors.get(0);
        Assert.assertEquals("Unexpected character '<' in script input state [SCRIPT]", error.getMessage());
        Assert.assertEquals(ParseError.ErrorType.UnexpectedCharacter, error.getType());
        Assert.assertEquals(0, tokeniser.errors.size());
        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testReadCharacterReference() {
        Tokeniser tokeniser = createTestSubject("&amp;");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('&', token.characters.get(0));
        Assert.assertEquals('a', token.characters.get(1));
        Assert.assertEquals('m', token.characters.get(2));
        Assert.assertEquals('p', token.characters.get(3));
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadCharacterReferenceWithUnknownEntity() {
        Tokeniser tokeniser = createTestSubject("&foobar;");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.characters.get(0));
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadCharacterReferenceWithInvalidHexSequence() {
        Tokeniser tokeniser = createTestSubject("&#xABC;");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.characters.get(0));
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacter() {
        Tokeniser tokeniser = createTestSubject("&#123456;");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.characters.get(0));
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacterOutOfRange() {
        Tokeniser tokeniser = createTestSubject("&#1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.characters.get(0));
        Assert.assertEquals(TokenType.EndOfInput, tokeniser.read().type);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = createTestSubject("<html>");
        Token.Tag tag = tokeniser.createTagPending(true);
        Assert.assertEquals("html", tag.tagName);
        tokeniser.emitTagPending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = createTestSubject("<!-- Comment -->");
        Token.Comment comment = tokeniser.createCommentPending();
        Assert.assertEquals("Comment", comment.data);
        tokeniser.emitCommentPending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Comment, token.type);
        Assert.assertEquals("Comment", token.comment.data);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = createTestSubject("<!DOCTYPE html>");
        Token.Doctype doctype = tokeniser.createDoctypePending();
        Assert.assertEquals("html", doctype.publicId);
        Assert.assertEquals("", doctype.systemId);
        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Doctype, token.type);
        Assert.assertEquals("html", token.doctype.publicId);
        Assert.assertEquals("", token.doctype.systemId);
    }

    @Test
    public void testAppropriateEndTagToken() {
        Tokeniser tokeniser = createTestSubject("<html><body><div><h1>Header</h1></div></body></html>");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("div", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("h1", token.tagName);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('H', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('e', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('a', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('d', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('e', token.characters.get(0));
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('r', token.characters.get(0));
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("h1", token.tagName);
        Assert.assertEquals(true, tokeniser.isAppropriateEndTagToken());
        Assert.assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testInappropriateEndTagToken() {
        Tokeniser tokeniser = createTestSubject("<html><body><div><h1>Header</div></body></html>");
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
        Assert.assertEquals(null, tokeniser.appropriateEndTagName());
    }
}