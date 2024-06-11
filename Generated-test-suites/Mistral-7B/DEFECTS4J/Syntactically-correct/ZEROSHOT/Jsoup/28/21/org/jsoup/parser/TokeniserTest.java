import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() throws IOException {
        // Test constructor
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Assert.assertNotNull(tokeniser);

        // Test getters and setters
        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.Script);
        Assert.assertEquals(TokeniserState.Script, tokeniser.getState());

        // Test read method with no pending token
        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test read method with pending character token
        tokeniser.emit(new Character("A"));
        tokeniser.transition(TokeniserState.Data);
        token = tokeniser.read();
        Assert.assertEquals("A", token.getData());
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test read method with pending tag token
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.TagStart);
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(tokeniser.tagPending.tagName, tokeniser.appropriateEndTagName());
        tokeniser.emitTagPending();
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Token.StartTag.class, tokeniser.emitPending.getClass());

        // Test read method with pending comment token
        tokeniser.createCommentPending();
        tokeniser.transition(TokeniserState.Comment);
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(Comment.class, tokeniser.emitPending.getClass());
        tokeniser.emitCommentPending();
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertTrue(tokeniser.isEmitPending());

        // Test read method with pending doctype token
        tokeniser.createDoctypePending();
        tokeniser.transition(TokeniserState.Doctype);
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(Doctype.class, tokeniser.emitPending.getClass());
        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertTrue(tokeniser.isEmitPending());

        // Test emit method
        tokeniser.emit(new Character('A'));
        tokeniser.read();
        Assert.assertEquals("A", tokeniser.charBuffer.toString());
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test emit method with pending tag token
        tokeniser.emitTagPending();
        tokeniser.read();
        Assert.assertEquals(tokeniser.tagPending.tagName, tokeniser.lastStartTag.tagName);
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Token.StartTag.class, tokeniser.emitPending.getClass());

        // Test emit method with pending comment token
        tokeniser.emitCommentPending();
        tokeniser.read();
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Comment.class, tokeniser.emitPending.getClass());

        // Test emit method with pending doctype token
        tokeniser.emitDoctypePending();
        tokeniser.read();
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Doctype.class, tokeniser.emitPending.getClass());

        // Test consumeCharacterReference method
        tokeniser.consumeCharacterReference('&', false);
        Assert.assertFalse(tokeniser.isEmitPending());
        tokeniser.consumeCharacterReference("#", false);
        Assert.assertFalse(tokeniser.isEmitPending());
        tokeniser.consumeCharacterReference("a", false);
        Assert.assertFalse(tokeniser.isEmitPending());
        tokeniser.consumeCharacterReference("amp", false);
        Assert.assertFalse(tokeniser.isEmitPending());
        tokeniser.consumeCharacterReference("apos", true);
        Assert.assertEquals(Character.class, tokeniser.emitPending.getClass());
        Assert.assertEquals(tokeniser.emitPending.getData(), '\'');

        // Test createTagPending method
        tokeniser.createTagPending(true);
        Assert.assertEquals(tokeniser.tagPending.tagName, tokeniser.lastStartTag.tagName);
        tokeniser.emitTagPending();
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test createTagPending method with end tag
        tokeniser.createTagPending(false);
        Assert.assertEquals(tokeniser.tagPending.tagName, tokeniser.lastStartTag.tagName);
        tokeniser.emitTagPending();
        Assert.assertFalse(tokeniser.isEmitPending());
        tokeniser.emit(new EndTag("div"));
        Assert.assertEquals(tokeniser.tagPending.tagName, "div");
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Token.EndTag.class, tokeniser.emitPending.getClass());

        // Test createCommentPending method
        tokeniser.createCommentPending();
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Comment.class, tokeniser.emitPending.getClass());
        tokeniser.emitCommentPending();
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test createDoctypePending method
        tokeniser.createDoctypePending();
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertEquals(Doctype.class, tokeniser.emitPending.getClass());
        tokeniser.emitDoctypePending();
        Assert.assertFalse(tokeniser.isEmitPending());

        // Test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);

        // Test isAppropriateEndTagToken method
        tokeniser.emit(new StartTag("div"));
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
        Assert.assertEquals("div", tokeniser.appropriateEndTagName());
        tokeniser.emit(new EndTag("p"));
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test error methods
        tokeniser.error(TokeniserState.Script);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character 't' in input state [Script]", errors.get(0).getMessage());

        tokeniser.eofError(TokeniserState.Comment);
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Comment]", errors.get(1).getMessage());

        tokeniser.characterReferenceError("invalid named referenece 'InvalidReference'");
        Assert.assertEquals(3, errors.size());
        Assert.assertEquals("Invalid character reference: invalid named reference 'InvalidReference'", errors.get(2).getMessage());
    }
}