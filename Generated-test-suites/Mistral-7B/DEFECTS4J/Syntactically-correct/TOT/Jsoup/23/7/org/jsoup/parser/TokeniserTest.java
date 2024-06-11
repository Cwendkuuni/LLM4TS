import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader characterReader;
    private ParseErrorList errors;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new ByteArrayInputStream( "<html><head><title>Test</title></head><body><h1>Hello World</h1></body>".getBytes()));
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, errors);
    }

    @Test
    public void testReadStartTag() throws IOException {
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Assert.assertEquals("title", token.tagName);
    }

    @Test
    public void testReadEndTag() throws IOException {
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.StartTag("head"));
        tokeniser.emit(new Token.StartTag("title"));

        tokeniser.emit(new Token.Character(' '));

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("title", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);
    }

    @Test
    public void testReadSelfClosingTag() throws IOException {
        tokeniser.emit(new Token.StartTag("img", new ArrayList<>(), true));
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName);
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacterReference() throws IOException {
        tokeniser.emitCharacterReference('x');
    }

    @Test
    public void testReadValidCharacterReference() throws IOException {
        tokeniser.emitCharacterReference('\u00A9');
        tokeniser.consumeCharacter(); // consume the copyright symbol
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("\u00A9", token.data);
    }

    @Test
    public void testReadValidNamedCharacterReference() throws IOException {
        tokeniser.emitCharacterReference("copyright");
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("\u00A9", token.data);
    }

    @Test
    public void testReadComment() throws IOException {
        tokeniser.emit(new Token.StartTag("script"));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        tokeniser.emit(new Token.EndTag("script"));

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Comment, token.type);
        Assert.assertEquals("<!--", token.data.substring(0, 4));

        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("-->", token.data);
    }

    @Test
    public void testReadDoctype() throws IOException {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Doctype, token.type);
        Assert.assertEquals("<!DOCTYPE html>", token.data);
    }

    @Test
    public void testReadErrorHandling() throws IOException {
        tokeniser.emit(new Token.Character('a'));
        tokeniser.error(TokeniserState.Data);
        tokeniser.consumeCharacter(); // consume 'a'

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Error, token.type);
        Assert.assertEquals("Unexpected character 'a' in input state [DATA]", token.error.getMessage());
    }

    @Test
    public void testReadEOFErrorHandling() throws IOException {
        // Consume all valid tokens to reach EOF
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.StartTag("head"));
        tokeniser.emit(new Token.StartTag("title"));
        tokeniser.emitCharacterReference('\u00A9');
        tokeniser.consumeCharacter(); // consume copyright symbol
        tokeniser.emit(new Token.EndTag("title"));
        tokeniser.emit(new Token.EndTag("head"));
        tokeniser.emit(new Token.EndTag("html"));

        // Read past EOF, expect error
        tokeniser.read();
        tokeniser.error(TokeniserState.Data);
    }
}