import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        // Test constructor
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Test getters and setters
        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.Script);
        Assert.assertEquals(TokeniserState.Script, tokeniser.getState());

        // Test read method
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('t', token.charValue());

        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('e', token.charValue());

        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('s', token.charValue());

        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndOfFile, token.type);

        // Test emit method
        tokeniser.emit(new Token.Character(' '));
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals(' ', token.charValue());

        // Test emit method with Token object
        token = new Token.Character('a');
        tokeniser.emit(token);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('a', token.charValue());

        // Test emit method with String
        tokeniser.emit("test");
        token = tokeniser.read();
        Token.Character tokenChar = (Token.Character) token;
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("test", tokenChar.getEncoded());

        // Test emit method with char
        tokeniser.emit('b');
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('b', token.charValue());

        // Test transition method
        tokeniser.transition(TokeniserState.Comment);

        // Test emit method with CharacterReference
        tokeniser.emitCharacterReference('&#123;');
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('\u007b', token.charValue());

        // Test createTagPending method
        tokeniser.createTagPending(true);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, token.type);
        Token.StartTag startTag = (Token.StartTag) token;
        Assert.assertNull(startTag.tagName);
        Assert.assertEquals(new ArrayList<>(), startTag.attributes());

        tokeniser.emitTagPending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndOfFile, token.type);

        // Test createCommentPending method
        tokeniser.createCommentPending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Comment, token.type);
        Token.Comment comment = (Token.Comment) token;
        Assert.assertEquals("", comment.getData());

        tokeniser.emitCommentPending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndOfFile, token.type);

        // Test createDoctypePending method
        tokeniser.createDoctypePending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Doctype, token.type);
        Token.Doctype doctype = (Token.Doctype) token;
        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals(new ArrayList<>(), doctype.publicId());
        Assert.assertEquals(new ArrayList<>(), doctype.systemId());

        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        Assert.assertEquals(TokenType.EndOfFile, token.type);

        // Test isAppropriateEndTagToken method
        tokeniser.transition(TokeniserState.Body);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Html);
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.createTagPending(false);
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test error methods
        tokeniser.error(TokeniserState.Data);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Error, token.type);
        ParseError parseError = (ParseError) token;
        Assert.assertEquals("Unexpected character 't' in input state [Data]", parseError.getMessage());

        tokeniser.eofError(TokeniserState.Script);
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Error, token.type);
        parseError = (ParseError) token;
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Script]", parseError.getMessage());

        tokeniser.characterReferenceError("numeric reference with no numerals");
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Error, token.type);
        parseError = (ParseError) token;
        Assert.assertEquals("Invalid character reference: invalid character reference with no numerals", parseError.getMessage());

        tokeniser.characterReferenceError("missing semicolon");
        token = tokeniser.read();
        Assert.assertEquals(TokenType.Error, token.type);
        parseError = (ParseError) token;
        Assert.assertEquals("Invalid character reference: missing semicolon", parseError.getMessage());
    }
}