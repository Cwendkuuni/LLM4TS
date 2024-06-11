import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testReadNormalCharacters() {
        CharacterReader reader = new CharacterReader("abcdef");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        for (int i = 0; i < 6; i++) {
            token = tokeniser.read();
            assertEquals("Character " + (char) reader.current(), token.data(), token.toString());
            reader.next();
        }

        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("SelfClosingTag [img]", token.toString());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadFalseSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("StartTag [img]", token.toString());
        assertEquals(TokeniserState.Empty, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("EndTag [img]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader("&#10;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("Character [\u000A]", token.toString());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&#xabc;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("Character [\uFFFD]", token.toString());
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: invalid character reference: 'abc'", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidCharacterSequence() {
        CharacterReader reader = new CharacterReader("&abcd;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("Character [&abcd]", token.toString());
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: 'abcd'", errors.get(0).getMessage());
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- This is a comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("Comment [This is a comment]", token.toString());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadEndTagWithoutStartTag() {
        CharacterReader reader = new CharacterReader("<body></body>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("StartTag [body]", token.toString());
        assertEquals(TokeniserState.StartTag, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("EndTag [/body]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadEndTagWithoutCorrespondingStartTag() {
        CharacterReader reader = new CharacterReader("<body><p></p></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("StartTag [body]", token.toString());
        assertEquals(TokeniserState.StartTag, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("StartTag [p]", token.toString());
        assertEquals(TokeniserState.StartTag, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("EndTag [/body]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("EndTag [/html]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader("<a href='#abc'>Link</a>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("StartTag [a]", token.toString());
        assertEquals(TokeniserState.StartTag, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("Attribute [href]", token.toString());
        assertEquals(TokeniserState.Attribute, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("Character [#]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("Character [a]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());

        token = tokeniser.read();
        assertEquals("EndTag [/a]", token.toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadEndTagWithExtraCharacter() {
        CharacterReader reader = new CharacterReader("<body><p></p></html>x");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            // Consume all tokens until EOF
        }

        assertEquals(TokeniserState.Error, tokeniser.getState());
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Unexpected character 'x' in state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testReadEOF() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(TokeniserState.Error, tokeniser.getState());
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in state [Error]", errors.get(0).getMessage());
    }
}