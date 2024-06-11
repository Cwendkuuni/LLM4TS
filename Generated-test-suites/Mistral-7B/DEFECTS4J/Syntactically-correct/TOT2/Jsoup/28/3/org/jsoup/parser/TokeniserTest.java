import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.charContent());

        tokeniser.emit(token); // consume token

        token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("b", token.charContent());
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName());

        tokeniser.emit(token); // consume token

        token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(">", token.charContent());
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader("<html></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName());

        tokeniser.emit(token); // consume token

        token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName());
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src='image.jpg'/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.tagName());
        Assert.assertTrue(token.selfClosing);

        tokeniser.emit(token); // consume token
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("<script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Assert.assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
        Assert.assertEquals("script", tokeniser.read().tagName());

        tokeniser.error(TokeniserState.Script);

        // This should print an error message
        // ...

        Assert.assertNull(tokeniser.read());
    }
}