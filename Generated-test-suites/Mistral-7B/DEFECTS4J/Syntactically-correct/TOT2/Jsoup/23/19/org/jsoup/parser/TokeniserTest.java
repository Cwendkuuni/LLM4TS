import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        this.tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        this.errors = tokeniser.errors;
    }

    @Test
    public void testReadCharacter() {
        tokeniser.reader.append("A");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("A", token.getData());
    }

    @Test
    public void testReadStartTag() {
        tokeniser.reader.append("<tag>");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.getData().getName());
    }

    @Test
    public void testReadEndTag() {
        tokeniser.reader.append("<tag></tag>");
        tokeniser.read(); // consume <tag>
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("tag", token.getData().getName());
    }

    @Test
    public void testReadSelfClosingTag() {
        tokeniser.reader.append("<tag />");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertTrue(token.isSelfClosing());
    }

    @Test
    public void testReadEmptyElement() {
        tokeniser.reader.append("<tag />");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertTrue(token.isSelfClosing());
        assertEquals("tag", token.getData().getName());
    }

    @Test(expected = AssertionError.class)
    public void testReadEndTagMismatch() {
        tokeniser.reader.append("<tag><tag></tag>");
        tokeniser.read(); // consume <tag>
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        tokeniser.read(); // consume <tag>
        tokeniser.read(); // consume </tag>
        tokeniser.read(); // consume <tag>
        tokeniser.read(); // consume >
        tokeniser.read(); // consume </tag> (mismatch)
    }

    @Test
    public void testReadCharacterReference() {
        tokeniser.reader.append("&amp;");
        CharacterReference characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', characterReference.getCharacter());
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
        tokeniser.reader.append("&apos;");
        CharacterReference characterReference = tokeniser.consumeCharacterReference('\'', false);
        assertEquals('\'', characterReference.getCharacter());
    }

    @Test(expected = AssertionError.class)
    public void testReadCharacterReferenceInvalid() {
        tokeniser.reader.append("&");
        tokeniser.consumeCharacterReference(null, false); // consume &
        tokeniser.read(); // consume ;
    }

    @Test
    public void testReadCharacterReferenceNamed() {
        tokeniser.reader.append("&nbsp;");
        CharacterReference characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A0', characterReference.getCharacter());
    }

    @Test
    public void testReadCharacterReferenceNamedWithAttributes() {
        tokeniser.reader.append("<p class='test'>&nbsp;</p>");
        CharacterReference characterReference = tokeniser.consumeCharacterReference(null, true);
        assertEquals('\u00A0', characterReference.getCharacter());
    }

    @Test(expected = AssertionError.class)
    public void testReadCharacterReferenceMissingSemicolon() {
        tokeniser.reader.append("&nbsp");
        tokeniser.consumeCharacterReference(null, false); // consume &
        tokeniser.read(); // consume n
        tokeniser.read(); // consume b
        tokeniser.read(); // consume s
        tokeniser.read(); // consume p
    }

    @Test
    public void testError() {
        tokeniser.reader.append("<script>");
        tokeniser.read(); // consume <
        tokeniser.read(); // consume s
        tokeniser.read(); // consume c
        tokeniser.read(); // consume r
        tokeniser.read(); // consume i
        tokeniser.read(); // consume p
        tokeniser.transition(TokeniserState.Script);
        tokeniser.read(); // consume {
        tokeniser.error(TokeniserState.Script);
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testEOF() {
        tokeniser.reader.append("<html>");
        tokeniser.read(); // consume <
        tokeniser.read(); // consume h
        tokeniser.read(); // consume t
        tokeniser.read(); // consume m
        tokeniser.read(); // consume l
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.read(); // consume >
        tokeniser.eofError(TokeniserState.StartTag);
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.reader.append("&");
        tokeniser.consumeCharacterReference(null, false); // consume &
        tokeniser.error("numeric reference with no numerals");
        assertTrue(errors.hasErrors());
    }
}