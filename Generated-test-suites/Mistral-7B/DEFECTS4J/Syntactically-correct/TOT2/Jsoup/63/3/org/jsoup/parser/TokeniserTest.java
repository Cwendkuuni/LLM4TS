import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead_SimpleCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('a', token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('b', token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndOfFile, token.type);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testRead_SelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.png\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Token.StartTag startTag = (Token.StartTag) token;
        Assert.assertEquals("img", startTag.tagName);
        Assert.assertTrue(startTag.isSelfClosing());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", ((Token.EndTag) token).tagName);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&x");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read(); // consume '&'
        tokeniser.read(); // consume 'x'
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals(34, error.getLineNumber());
        Assert.assertEquals("Invalid character reference: invalid named reference reference 'x'", error.getMessage());
    }

    @Test
    public void testRead_Empty() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Assert.assertEquals(Token.TokenType.EndOfFile, tokeniser.read().type);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testRead_InvalidState() {
        CharacterReader reader = new CharacterReader("<html><body>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(TokeniserState.Script);
        tokeniser.read(); // consume '<'
        tokeniser.read(); // consume 's'
        tokeniser.read(); // consume 'c'
        tokeniser.read(); // consume 'r'
        tokeniser.read(); // consume 'i'
        tokeniser.read(); // consume 'p'
        tokeniser.read(); // consume 't'
        tokeniser.read(); // consume '>'
        Assert.assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
        Assert.assertEquals(Token.TokenType.EndOfFile, tokeniser.read().type);
        Assert.assertFalse(errors.hasErrors());

        Tokeniser tokeniser2 = new Tokeniser(reader, errors);
        tokeniser2.transition(TokeniserState.Script);
        tokeniser2.consumeCharacterReference(null, false);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals(1, error.getLineNumber());
        Assert.assertEquals("Invalid character reference: missing semicolon", error.getMessage());
    }
}