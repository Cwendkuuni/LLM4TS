import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadSimpleCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("abc"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('a', token.charValue());
    }

    @Test
    public void testReadString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("abc"), new ParseErrorList());
        String str = "def";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('d', token.charValue());
    }

    @Test
    public void testReadTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName());
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("abc\u0000"), new ParseErrorList());
        tokeniser.read();
    }

    @Test
    public void testReadSelfClosingTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<img/>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertTrue(token.isSelfClosing());
    }

    @Test
    public void testReadEndTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html><body></body></html>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName());
    }

    @Test
    public void testReadEmptyElement() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<img/>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertTrue(token.isSelfClosing());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName());
    }

    @Test
    public void testReadCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("#x61;"), new ParseErrorList());
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{0x61}, codepoints);
    }

    @Test
    public void testReadNamedEntityReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("#amp;"), new ParseErrorList());
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{0x26}, codepoints);
    }

    @Test
    public void testReadComment() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!-- comment -->>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testReadDoctype() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"), new ParseErrorList());
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
    }
}