import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader characterReader;
    private ParseErrorList parseErrorList;

    @Before
    public void setUp() {
        String input = "<html><head><title>Test</title></head><body><div><p>Test</p></div></body></html>";
        characterReader = new CharacterReader(new ByteArrayInputStream(input.getBytes()));
        parseErrorList = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, parseErrorList);
    }

    @Test
    public void testTokeniser_read_EmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testTokeniser_read_StartTag() throws IOException {
        Token token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.getType());
    }

    @Test
    public void testTokeniser_read_EndTag() throws IOException {
        tokeniser.read();
        tokeniser.read();
        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.getType());
    }

    @Test
    public void testTokeniser_read_Character() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokeniser_read_SelfClosingFlagNotAcknowledged() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.emitPending();
        tokeniser.read();
    }

    @Test
    public void testTokeniser_emit_Character() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        CharSequence charSequence = "abc";
        tokeniser.emit(new Token.Character(charSequence.toString()));
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.getType());
        assertEquals(charSequence, token.getData());
    }

    @Test
    public void testTokeniser_emit_StartTag() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Token startTag = new Token.StartTag("div");
        tokeniser.emit(startTag);
        Token token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.getType());
        assertEquals("div", token.getData().tagName);
    }

    @Test
    public void testTokeniser_emit_EndTag() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Token endTag = new Token.EndTag("div");
        tokeniser.emit(endTag);
        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.getType());
        assertEquals("div", token.getData().tagName);
    }

    @Test
    public void testTokeniser_emit_Comment() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.emit(new Token.Comment("<!-- Test -->"));
        Token token = tokeniser.read();
        assertEquals(TokenType.Comment, token.getType());
    }

    @Test
    public void testTokeniser_emit_Doctype() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.createDoctypePending();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(TokenType.Doctype, token.getType());
    }

    @Test
    public void testTokeniser_getState() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        TokenState expectedState = new TokeniserState("Data");
        TokenState actualState = tokeniser.getState();
        assertEquals(expectedState, actualState);
    }

    @Test
    public void testTokeniser_transition() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.transition(new TokeniserState("Script"));
        TokenState expectedState = new TokeniserState("Script");
        TokenState actualState = tokeniser.getState();
        assertEquals(expectedState, actualState);
    }

    @Test
    public void testTokeniser_acknowledgeSelfClosingFlag() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read();
        Token tagStart = new Token.StartTag("script");
        tokeniser.emitPending(tagStart);
        tokeniser.read();
        assertNotNull(tagStart.selfClosing);
    }

    @Test
    public void testTokeniser_consumeCharacterReference() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Character ch = tokeniser.consumeCharacterReference(null, false);
        assertNull(ch);

        tokeniser.consume("#x41");
        Character expectedChar = 'A';
        Character actualChar = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expectedChar, actualChar);

        tokeniser.consume("#x26");
        Character expectedChar2 = '&';
        Character actualChar2 = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expectedChar2, actualChar2);

        tokeniser.consume("#x0041");
        Character expectedChar3 = 'A';
        Character actualChar3 = tokeniser.consumeCharacterReference('\u0004', false);
        assertEquals(expectedChar3, actualChar3);

        tokeniser.consume("#133");
        Character expectedChar4 = '\u007B';
        Character actualChar4 = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expectedChar4, actualChar4);

        tokeniser.consume("#name");
        Character expectedChar5 = ' '; // replace with actual character
        Character actualChar5 = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expectedChar5, actualChar5);

        tokeniser.consume("#name;");
        Character expectedChar6 = ' '; // replace with actual character
        Character actualChar6 = tokeniser.consumeCharacterReference(null, false);
        assertNull(actualChar6);
    }

    @Test(expected = IllegalStateException.class)
    public void testTokeniser_consumeCharacterReference_InvalidCharacter() throws IOException {
        tokeniser.consume("#");
    }

    @Test(expected = IllegalStateException.class)
    public void testTokeniser_consumeCharacterReference_InAttribute() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.consume("name=\"value#x00A1\"");
    }

    @Test
    public void testTokeniser_createTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tagPending);
    }

    @Test
    public void testTokeniser_emitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
    }

    @Test
    public void testTokeniser_createCommentPending() {
        tokeniser.createCommentPending();
        Token comment = tokeniser.emitCommentPending();
        assertEquals(TokenType.Comment, comment.getType());
    }

    @Test
    public void testTokeniser_isAppropriateEndTagToken() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testTokeniser_isAppropriateEndTagToken_DifferentTag() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertFalse(result);
    }

    @Test
    public void testTokeniser_appropriateEndTagName() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        String result = tokeniser.appropriateEndTagName();
        assertEquals("div", result);
    }

    @Test
    public void testTokeniser_error() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.Script);
        String errorMessage = "Unexpected character 'a' in input state [Script]";
        assertEquals(errorMessage, tokeniser.parseErrorList.get(0).getMessage());
    }

    @Test
    public void testTokeniser_eofError() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.eofError(TokeniserState.Data);
        String errorMessage = "Unexpectedly reached end of file (EOF) in input state [Data]";
        assertEquals(errorMessage, tokeniser.parseErrorList.get(0).getMessage());
    }

    @Test
    public void testTokeniser_characterReferenceError() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.consume("#x0000");
        String errorMessage = "character outside of valid range";
        assertEquals(errorMessage, tokeniser.parseErrorList.get(0).getMessage());
    }

    @Test
    public void testTokeniser_characterReferenceError_InvalidNamedReference() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.consume("invalidName;");
        String errorMessage = "invalid named referenece '%invalidName%'";
        assertEquals(errorMessage, tokeniser.parseErrorList.get(0).getMessage());
    }

    @Test
    public void testTokeniser_characterReferenceError_MissingSemicolon() throws IOException {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.consume("#x00A1");
        tokeniser.consume("name=\"value\"");
        String errorMessage = "missing semicolon";
        assertEquals(errorMessage, tokeniser.parseErrorList.get(0).getMessage());
    }
}