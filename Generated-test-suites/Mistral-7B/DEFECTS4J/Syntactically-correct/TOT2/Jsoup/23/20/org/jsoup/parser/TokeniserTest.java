import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokenizerState;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        Tokeniser tokeniser = new Tokeniser(new StringReader("abc"), new ArrayList<ParseError>());
        Token token;

        // Test reading normal characters
        token = tokeniser.read();
        Assert.assertEquals("a", token.text());
        token = tokeniser.read();
        Assert.assertEquals("b", token.text());
        token = tokeniser.read();
        Assert.assertEquals("c", token.text());

        // Test reading replacement character
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume('3');
        tokeniser.reader.consume(';');
        token = tokeniser.read();
        Assert.assertEquals("\uFFFD", token.text()); // replacement character

        // Test reading invalid character reference
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('A'); // invalid character reference
        tokeniser.reader.consume(';');
        token = tokeniser.read();
        Assert.assertNull(token); // no token was emitted

        // Test reading named character reference
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.reader.consume('amp'); // entity name for "&"
        tokeniser.reader.consume(';');
        token = tokeniser.read();
        Assert.assertEquals("&", token.text());

        // Test reading character reference with extra characters
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume('3');
        tokeniser.reader.consume(';');
        tokeniser.reader.consume(' '); // extra space
        token = tokeniser.read();
        Assert.assertEquals("\uFFFD", token.text()); // replacement character
    }

    @Test
    public void testReadTag() {
        Tokeniser tokeniser = new Tokeniser(new StringReader("<html><head><title>Test</title></head><body></body></html>"), new ArrayList<ParseError>());
        Token token;

        // Test reading start tag
        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("<html>", token.text());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("<head>", token.text());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("<title>", token.text());

        // Test reading end tag
        tokeniser.emit(new Token.StartTag("<title>"));
        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("</title>", token.text());

        // Test reading self-closing tag
        tokeniser.emit(new Token.StartTag("<img src=\"image.jpg\" />"));
        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("/img>", token.text());
    }
}