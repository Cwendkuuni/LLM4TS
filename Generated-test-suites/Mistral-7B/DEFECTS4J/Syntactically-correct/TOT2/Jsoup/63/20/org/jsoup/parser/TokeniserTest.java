import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead_Basic() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('a', token.charSequences()[0].charAt(0));
    }

    @Test
    public void testRead_SelfClosingFlag() {
        CharacterReader reader = new CharacterReader("<p/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.acknowledgeSelfClosingFlag();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName());
    }

    @Test
    public void testRead_ValidString() {
        CharacterReader reader = new CharacterReader("abcdef");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token1 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token1.type);
        Assert.assertEquals('a', token1.charSequences()[0].charAt(0));
        Token token2 = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token2.type);
        Assert.assertEquals('b', token2.charSequences()[0].charAt(0));
        // ...
        Token tokenN = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, tokenN.type);
        Assert.assertEquals('f', tokenN.charSequences()[0].charAt(0));
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("#xABC");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{Tokeniser.replacementChar}, codeRef);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.charSequences()[0].charAt(0));
    }

    @Test
    public void testRead_ValidCharacterReference() {
        CharacterReader reader = new CharacterReader("#x2E");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals(new int[]{0x2E}, codeRef);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals((char) 0x2E, token.charSequences()[0].charAt(0));
    }

    @Test
    public void testRead_ValidNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&deg;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{0x00B0}, codeRef);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals((char) 0x00B0, token.charSequences()[0].charAt(0));
    }

    @Test
    public void testRead_InvalidNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&InvalidReference;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{Tokeniser.replacementChar}, codeRef);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(Tokeniser.replacementChar, token.charSequences()[0].charAt(0));
    }

    @Test
    public void testRead_InputEmpty() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testRead_InputEndOfFile() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token1 = tokeniser.read();
        // ... same as testRead_ValidString()
        Token tokenN = tokeniser.read();
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testError_UnexpectedCharacter() {
        CharacterReader reader = new CharacterReader("abc><");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read(); // read 'a'
        tokeniser.read(); // read 'b'
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testError_UnexpectedEndOfFile() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(TokeniserState.AfterEndTag);
        tokeniser.eofError(TokeniserState.AfterEndTag);
    }

    @Test
    public void testCharacterReferenceError_InvalidReferenceWithoutSemicolon() {
        CharacterReader reader = new CharacterReader("#xABC");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.characterReferenceError("numeric reference with no numerals");
    }
}