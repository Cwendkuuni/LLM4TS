import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private InputStream inputStream;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        errors = new ArrayList<>();
        tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList(errors));
    }

    @Test
    public void testReadEmptyInput() {
        inputStream = new ByteArrayInputStream("".getBytes());
        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testReadSingleCharacter() {
        inputStream = new ByteArrayInputStream("a".getBytes());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.toString());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadMultipleCharacters() {
        inputStream = new ByteArrayInputStream("ab".getBytes());
        Token token1 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token1.type);
        Assert.assertEquals("a", token1.toString());
        Token token2 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token2.type);
        Assert.assertEquals("b", token2.toString());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingTag() {
        inputStream = new ByteArrayInputStream("<img src=\"image.png\" />".getBytes());
        Token startTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("img", startTag.tagName);
        Assert.assertFalse(startTag.selfClosing);
        Token endTag = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("img", endTag.tagName);
        Assert.assertFalse(endTag.selfClosing);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidCharacterReference() {
        inputStream = new ByteArrayInputStream("&z;".getBytes());
        tokeniser.read(); // consume '&'
        tokeniser.read(); // consume 'z'
        tokeniser.read(); // consume ';'
        Assert.assertNull(tokeniser.read());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference: invalid named referenece '%z'", errors.get(0).getMessage());
    }

    @Test
    public void testReadValidCharacterReference() {
        inputStream = new ByteArrayInputStream("&#65;".getBytes());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("E", token.toString());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadComment() {
        inputStream = new ByteArrayInputStream("<!-- This is a comment -->".getBytes());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertEquals("This is a comment", token.toString());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadMultipleTokens() {
        inputStream = new ByteArrayInputStream("<p><i>Hello</i> world</p>".getBytes());
        Token token1 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token1.type);
        Assert.assertEquals("p", token1.tagName);
        Token token2 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token2.type);
        Assert.assertEquals("i", token2.tagName);
        Token token3 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token3.type);
        Assert.assertEquals("H", token3.toString());
        Token token4 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token4.type);
        Assert.assertEquals("e", token4.toString());
        // ...
        Token tokenN = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, tokenN.type);
        Assert.assertEquals("i", tokenN.tagName);
        Token tokenNPlus1 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, tokenNPlus1.type);
        Assert.assertEquals("p", tokenNPlus1.tagName);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidInput() {
        inputStream = new ByteArrayInputStream("<script>alert('Hello, world!');</script>".getBytes());
        tokeniser.read(); // consume '<'
        tokeniser.read(); // consume 's'
        tokeniser.read(); // consume 'c'
        tokeniser.read(); // consume 'r'
        tokeniser.read(); // consume 'i'
        tokeniser.read(); // consume 'p'
        tokeniser.read(); // consume 't'
        tokeniser.read(); // consume '>'
        tokeniser.read(); // consume 'a'
        tokeniser.read(); // consume 'l'
        tokeniser.read(); // consume 'e'
        tokeniser.read(); // consume 'r'
        tokeniser.read(); // consume 't'
        tokeniser.read(); // consume '('
        Assert.assertNull(tokeniser.read());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character '(' in input state [Script]", errors.get(0).getMessage());
    }

    @Test
    public void testReadUnexpectedEndOfInput() {
        inputStream = new ByteArrayInputStream("<p>".getBytes());
        tokeniser.read();
        tokeniser.read();
        Assert.assertNull(tokeniser.read());
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }
}