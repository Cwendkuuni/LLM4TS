import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private ByteArrayInputStream inputStream;
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        errors = new ParseErrorList();
        reader = new CharacterReader(inputStream);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadSimpleHtml() {
        inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        tokeniser.read();
        Token expectedToken = new Token.StartTag("html", null, null, null, null, false, null, null, null, 1, false, null);
        Assert.assertEquals(expectedToken.tagName, tokeniser.lastStartTag.tagName);

        tokeniser.read();
        expectedToken = new Token.StartTag("head", null, null, null, null, false, null, null, null, 1, false, null);
        Assert.assertEquals(expectedToken.tagName, tokeniser.lastStartTag.tagName);

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("head", tokeniser.appropriateEndTagName());

        tokeniser.read();
        expectedToken = new Token.StartTag("body", null, null, null, null, false, null, null, null, 1, false, null);
        Assert.assertEquals(expectedToken.tagName, tokeniser.lastStartTag.tagName);

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("body", tokeniser.appropriateEndTagName());

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("html", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testReadSelfClosingTag() {
        inputStream = new ByteArrayInputStream("<img src=\"image.jpg\" />".getBytes());
        tokeniser.read();
        Token expectedToken = new Token.StartTag("img", null, null, null, null, true, null, null, null, 1, null, null);
        Assert.assertEquals(expectedToken.tagName, tokeniser.lastStartTag.tagName);

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("img", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testReadEmptyElement() {
        inputStream = new ByteArrayInputStream("<br />".getBytes());
        tokeniser.read();
        Token expectedToken = new Token.StartTag("br", null, null, null, null, true, null, null, null, 1, null, null);
        Assert.assertEquals(expectedToken.tagName, tokeniser.lastStartTag.tagName);

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("br", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testReadComment() {
        inputStream = new ByteArrayInputStream("<!-- This is a comment -->".getBytes());
        tokeniser.read();
        Token expectedToken = new Token.Comment("This is a comment");
        Assert.assertEquals(expectedToken.type, tokeniser.emitPending.type);
        Assert.assertEquals(expectedToken.getCommentText(), tokeniser.emitPending.getCommentText());
    }

    @Test
    public void testReadCharacterReference() {
        inputStream = new ByteArrayInputStream("<p>&#169; &copy; 2022</p>".getBytes());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals('\u00A9', tokeniser.emitPending.charValue);

        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals('\u0025', tokeniser.emitPending.charValue);

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals(' ', tokeniser.emitPending.charValue);

        tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
        Assert.assertEquals("p", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
        inputStream = new ByteArrayInputStream("<p>&#169; &copy; 2022&#x20AC;</p>".getBytes());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals('\u00A9', tokeniser.emitPending.charValue);

        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals('\u0025', tokeniser.emitPending.charValue);

        tokeniser.read();
        Assert.assertEquals(TokenType.Character, tokeniser.emitPending.type);
        Assert.assertEquals("\u0020AC", tokeniser.emitPending.getData());
    }

    @Test
    public void testReadNamedCharacterReference() {
        inputStream = new ByteArrayInputStream("<p>&copy;</p>".getBytes());
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals('\u00A9', tokeniser.emitPending.charValue);
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacterReference() {
        inputStream = new ByteArrayInputStream("<p>&foo;</p>".getBytes());
        tokeniser.read();
        tokeniser.read();
    }

    @Test
    public void testReadEOF() {
        inputStream = new ByteArrayInputStream("<p><script src=\"script.js\"></script></p>".getBytes());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.error(TokeniserState.Data);
        Assert.assertEquals(11, errors.size());
        ParseError expectedError = errors.get(0);
        Assert.assertEquals("Unexpected character 's' in input state [Data]", expectedError.getMessage());
        Assert.assertEquals(11, expectedError.getLineNumber());
        Assert.assertEquals(15, expectedError.getColumnNumber());
    }
}