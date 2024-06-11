import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Token.Character charToken = (Token.Character) token;
        assertEquals("A", charToken.data());
    }

    @Test
    public void testReadMultipleCharacters() {
        CharacterReader reader = new CharacterReader("AB");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Token.Character charToken = (Token.Character) token;
        assertEquals("AB", charToken.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNullInput() {
        CharacterReader reader = new CharacterReader(null);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read();
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.jpg\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();
        assertEquals("<img", startTag.tagName());
        assertEquals("img", startTag.tagName().toLowerCase());
        assertEquals(">", startTag.content().toString());
        assertEquals("/>", endTag.tagName());
        assertNull(endTag.content());
    }

    @Test
    public void testReadErrorState() {
        CharacterReader reader = new CharacterReader("<script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals("script", token.tagName());
        assertEquals("SCRIPT", token.tagName().toUpperCase());
        assertNotNull(errors.getErrors());
        assertEquals(1, errors.getErrors().size());
        ParseError error = errors.getErrors().get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(0, error.getColumnNumber());
        assertEquals("Unexpected character '<' in input state [Data]", error.getMessage());
    }

    @Test
    public void testReadEOF() {
        CharacterReader reader = new CharacterReader("test<");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.acknowledgeSelfClosingFlag();
        Token token = tokeniser.read();
        assertEquals("<", token.tagName());
        assertEquals("TOKEN_START_TAG", token.type().toString());
        assertEquals(1, errors.getErrors().size());
        ParseError error = errors.getErrors().get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(3, error.getColumnNumber());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
    }
}