import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() throws IOException {
        String input = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // test read method
        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            if (token == null) {
                assertEquals("Unexpected end of stream", tokeniser.getState().name(), TokeniserState.EOF.name());
                break;
            }
            System.out.println("Token type: " + token.getType());
            System.out.println("Token value: " + token.getValue());
        }

        // test emit method with character token
        tokeniser.emit(new Token.Character('T'));
        token = tokeniser.read();
        assertEquals(TokenType.Character, token.getType());
        assertEquals("T", token.getValue());

        // test emit method with start tag token
        tokeniser.emit(new Token.StartTag("h1"));
        token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.getType());
        assertEquals("h1", token.getValue().tagName);
        assertEquals(null, token.getValue().attributes);
        assertEquals(null, token.getValue().children);

        // test emit method with end tag token
        tokeniser.emit(new Token.EndTag("h1"));
        token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.getType());
        assertEquals("h1", token.getValue().tagName);
        assertEquals(null, token.getValue().attributes);

        // test emit method with comment token
        tokeniser.emit(new Token.Comment("Test comment"));
        token = tokeniser.read();
        assertEquals(TokenType.Comment, token.getType());
        assertEquals("Test comment", token.getValue().data);

        // test emit method with empty element tag token
        tokeniser.transition(TokeniserState.EmptyElement);
        tokeniser.emit(new Token.Character('i'));
        tokeniser.emit(new Token.Character('m'));
        tokeniser.emit(new Token.Character('g'));
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.EndTag(null));
        token = tokeniser.read();
        assertEquals(TokenType.StartTag, token.getType());
        assertEquals("img", token.getValue().tagName);
        assertEquals(null, token.getValue().attributes);
        assertEquals(null, token.getValue().children);
        token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.getType());
        assertEquals(null, token.getValue());

        // test emit method with doctype token
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        assertEquals(TokenType.Doctype, token.getType());
        assertEquals("<!doctype html>", token.getValue().data);
        assertEquals(null, token.getValue().publicId);
        assertEquals(null, token.getValue().systemId);

        // test error handling
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit(new Token.Character('<'));
        tokeniser.error("Unexpected character '<' in script state.");

        List<ParseError> errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        ParseError error = errorsList.get(0);
        assertEquals(17, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        assertEquals("<!doctype html>", error.getInput());
        assertEquals("Unexpected character '<' in script state.", error.getMessage());

        // test isAppropriateEndTagToken method
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.createStartTagPending("div");
        tokeniser.emitTagPending();
        token = tokeniser.read();
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.createStartTagPending("div");
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.emitEndTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());

        // test consumeCharacterReference method
        tokeniser.emit(new Token.Character('&'));
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(replacementChar, character.charValue());
        tokeniser.consumeCharacterReference('#', false);
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('a', character.charValue());
        tokeniser.consumeCharacterReference("amp;", false);
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', character.charValue());

        // test createTempBuffer method
        tokeniser.createTempBuffer();
        tokeniser.emit("Test");
        tokeniser.emit(" is a ");
        tokeniser.emit("test<script src=\"test.js\"></script>");
        token tokenString = tokeniser.read();
        String bufferValue = tokenString.getValue().toString();
        assertEquals("Test is a Test", bufferValue);

        // test error handling with consumeCharacterReference method
        tokeniser.transition(TokeniserState.Attribute);
        tokeniser.consumeCharacterReference("amp;", true);
        tokeniser.error("Invalid character reference '&amp;' in attribute state.");

        errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        error = errorsList.get(0);
        assertEquals(17, error.getLineNumber());
        assertEquals(15, error.getColumnNumber());
        assertEquals("<!doctype html>", error.getInput());
        assertEquals("Invalid character reference '&amp;' in attribute state.", error.getMessage());

        // test advanceTransition method
        tokeniser.transition(TokeniserState.Script);
        tokeniser.advanceTransition(TokeniserState.InTag);
        assertEquals(TokeniserState.InTag, tokeniser.getState());

        // test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit(new Token.Character('<'));
        tokeniser.error("Self closing flag not acknowledged.");

        errorsList = errors.getErrors();
        assertEquals(1, errorsList.size());
        error = errorsList.get(0);
        assertEquals(17, error.getLineNumber());
        assertEquals(16, error.getColumnNumber());
        assertEquals("<!doctype html>", error.getInput());
        assertEquals("Self closing flag not acknowledged.", error.getMessage());

        // test read method with empty stream
        tokeniser.transition(TokeniserState.InTag);
        reader.reset();
        token = tokeniser.read();
        assertEquals(TokeniserState.EOF, tokeniser.getState());
        assertEquals(null, token);
    }
}