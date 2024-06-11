package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

/**
 * Test class for the Tokeniser class.
 */
public class TokeniserTest {

    @org.junit.Test
    public void testTokeniser() throws IOException {
        String html = "<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        CharacterReader characterReader = new CharacterReader(inputStream);

        Tokeniser tokeniser = new Tokeniser(characterReader);

        List<Token> tokens = new ArrayList<>();

        Token token;
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        // Test read method
        assertEquals(33, tokens.size()); // Expected number of tokens in the given HTML

        // Test emit method
        tokeniser.emit(new Token.Character("h"));
        tokeniser.emit(new Token.Character("t"));
        tokeniser.emit(new Token.Character("m"));
        tokeniser.emit(new Token.Character("l"));
        assertEquals(new Token.Character("html"), tokeniser.read());

        // Test getState method
        assertEquals(TokeniserState.Data, tokeniser.getState());

        // Test transition method
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());

        // Test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);

        // Test consumeCharacterReference method
        tokeniser.consumeCharacterReference((char) 0, false);
        assertEquals(Tokeniser.replacementChar, tokeniser.read().getClass().getConstructor().newInstance());

        tokeniser.consumeCharacterReference("nbsp", false);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals('\u00A0', ((Character) tokeniser.read()).getChars()[0]);

        tokeniser.consumeCharacterReference("amp", false);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals('&', ((Character) tokeniser.read()).getChars()[0]);

        tokeniser.consumeCharacterReference("apos", false);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals('\'', ((Character) tokeniser.read()).getChars()[0]);

        tokeniser.consumeCharacterReference("quot", false);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals('"', ((Character) tokeniser.read()).getChars()[0]);

        tokeniser.consumeCharacterReference("quot", true);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals('"', ((Character) tokeniser.read()).getChars()[0]);
        assertEquals('>', tokeniser.read().getType());

        // Test createTagPending and emitTagPending methods
        tokeniser.createTagPending(true);
        assertEquals(StartTag.class, tokeniser.read().getClass());
        tokeniser.emitTagPending();
        assertEquals(EndTag.class, tokeniser.read().getClass());

        // Test createCommentPending and emitCommentPending methods
        tokeniser.createCommentPending();
        assertEquals(Comment.class, tokeniser.read().getClass());
        tokeniser.emitCommentPending();

        // Test createDoctypePending and emitDoctypePending methods
        tokeniser.createDoctypePending();
        assertEquals(Doctype.class, tokeniser.read().getClass());
        tokeniser.emitDoctypePending();

        // Test isAppropriateEndTagToken method
        tokeniser.createTagPending(true).tagName = "p";
        tokeniser.createTagPending(false).tagName = "p";
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test isTrackErrors method and error methods
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
        tokeniser.error(TokeniserState.Data);
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals(1, tokeniser.errors.size());
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(2, tokeniser.errors.size());
        tokeniser.characterReferenceError();
        assertEquals(3, tokeniser.errors.size());

        // Test currentNodeInHtmlNS method
        // Not possible to test without a real Node implementation

        // Test read method with errors
        tokeniser.setTrackErrors(true);
        tokeniser.consumeCharacterReference("notanentity", false);
        assertEquals(Character.class, tokeniser.read().getClass());
        assertEquals(TokeniserError.class, tokeniser.read().getClass());
        assertEquals("Invalid character reference", ((TokeniserError) tokeniser.read()).getMessage());
    }

}