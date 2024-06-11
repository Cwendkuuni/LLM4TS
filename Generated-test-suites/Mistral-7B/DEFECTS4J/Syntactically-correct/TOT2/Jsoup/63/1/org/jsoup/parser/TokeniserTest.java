import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testReadNormalCharacters() {
        CharacterReader reader = new CharacterReader("abcdef");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token;

        token = tokeniser.read();
        Assert.assertEquals("a", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        Assert.assertEquals("b", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        Assert.assertEquals("c", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        Assert.assertEquals("d", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        Assert.assertEquals("e", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        Assert.assertEquals("f", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);

        // Test reading empty string
        tokeniser.emit(new Token.Character());
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.jpg\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertEquals("<img", token.tagName());
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertTrue(token.isSelfClosing());

        token = tokeniser.read();
        Assert.assertEquals("/img", token.tagName());
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
    }

    // Add more test cases for other methods and edge cases here
}