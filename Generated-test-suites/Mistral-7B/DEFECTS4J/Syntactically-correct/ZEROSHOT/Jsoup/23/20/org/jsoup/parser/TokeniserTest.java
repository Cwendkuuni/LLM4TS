package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test Data");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testDefaultConstruction() {
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testTokeniserStateData() {
        TokeniserState state = TokeniserState.Data;
        Assert.assertEquals("Data", state.name());
    }

    @Test
    public void testTokeniserStateScript() {
        TokeniserState state = TokeniserState.Script;
        Assert.assertEquals("Script", state.name());
    }

    @Test
    public void testTokeniserStateScriptData() {
        TokeniserState state = TokeniserState.ScriptData;
        Assert.assertEquals("ScriptData", state.name());
    }

    @Test
    public void testTokeniserStateDoctype() {
        TokeniserState state = TokeniserState.Doctype;
        Assert.assertEquals("Doctype", state.name());
    }

    @Test
    public void testTokeniserStateComment() {
        TokeniserState state = TokeniserState.Comment;
        Assert.assertEquals("Comment", state.name());
    }

    @Test
    public void testTokeniserStateStartTag() {
        TokeniserState state = TokeniserState.StartTag;
        Assert.assertEquals("StartTag", state.name());
    }

    @Test
    public void testTokeniserStateEndTag() {
        TokeniserState state = TokeniserState.EndTag;
        Assert.assertEquals("EndTag", state.name());
    }

    @Test
    public void testTokeniserStateDefault() {
        TokeniserState state = TokeniserState.Default;
        Assert.assertEquals("Default", state.name());
    }

    @Test
    public void testRead() {
        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            Assert.assertTrue("Token is not null", token != null);
            Assert.assertFalse("Token is not emitted pending", tokeniser.isEmitPending());
        }
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("A");
        tokeniser.emit(token);
        Token emitToken = tokeniser.read();
        Assert.assertEquals("A", emitToken.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmitWithPendingToken() {
        Token token = new Token.Character("A");
        tokeniser.emit(token);
        tokeniser.read();
        tokeniser.emit(token);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("Test");
        Token token = tokeniser.read();
        Assert.assertEquals("Test", token.getText());
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('A');
        Token token = tokeniser.read();
        Assert.assertEquals("A", token.getText());
    }

    @Test
    public void testGetState() {
        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Script);
        Assert.assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.transition(TokeniserState.ScriptData);
        tokeniser.advanceTransition(TokeniserState.Script);
        Assert.assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReferenceNull() {
        Assert.assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testConsumeCharacterReferenceSingleCharacter() {
        tokeniser.emit('&');
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        Assert.assertEquals("&", token.getText());
    }

    @Test
    public void testConsumeCharacterReferenceMultiCharacter() {
        tokeniser.emit('&');
        tokeniser.emit('a');
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        Assert.assertEquals("a", token.getText());
    }

    @Test
    public void testConsumeCharacterReferenceNamed() {
        tokeniser.emit('&');
        tokeniser.emit('A');
        tokeniser.emit('m');
        tokeniser.emit('p');
        tokeniser.emit(';');
        Token token = tokeniser.read();
        Assert.assertEquals("amp", token.getText());
    }

    @Test
    public void testConsumeCharacterReferenceInvalidNamed() {
        tokeniser.emit('&');
        tokeniser.emit('X');
        tokeniser.emit('m');
        tokeniser.emit('p');
        tokeniser.emit(';');
        Assert.assertEquals(0, tokeniser.readCount());
    }

    @Test
    public void testConsumeCharacterReferenceInvalidNamedWithAttribute() {
        tokeniser.emit('&');
        tokeniser.emit('X');
        tokeniser.emit('m');
        tokeniser.emit('p');
        tokeniser.emit('/');
        tokeniser.emit('a');
        tokeniser.emit('t');
        tokeniser.emit('t');
        tokeniser.emit('r');
        tokeniser.emit('i');
        tokeniser.emit('b');
        tokeniser.emit('u');
        tokeniser.emit('e');
        tokeniser.emit(';');
        Assert.assertEquals(0, tokeniser.readCount());
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        Assert.assertNotNull(tag);
        Assert.assertEquals(Tag.TagType.START_TAG, tag.type);
    }

    @Test
    public void testCreateTagPendingEnd() {
        Token.Tag tag = tokeniser.createTagPending(false);
        Assert.assertNotNull(tag);
        Assert.assertEquals(Tag.TagType.END_TAG, tag.type);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "div";
        tag.tagNameNS = "";
        tag.attributes = new ArrayList<>();
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertEquals("div", token.getTagName());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.COMMENT, token.type);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        String commentText = "Test Comment";
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(commentText, token.getText());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.DOCTYPE, token.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        String doctypeName = "html";
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(doctypeName, token.getText());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true).tagName = "p";
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenFalse() {
        tokeniser.createTagPending(true).tagName = "div";
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true).tagName = "p";
        Assert.assertEquals("p", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceErrorWithMessage() {
        tokeniser.characterReferenceError("Invalid character reference: invalid named reference");
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testReadWithErrors() {
        tokeniser = new Tokeniser(new CharacterReader("Test Error Data"), errors);
        Assert.assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.error(TokeniserState.Data);
        tokeniser.emit('T');
        tokeniser.error(TokeniserState.Data);
        tokeniser.emit('e');
        tokeniser.error(TokeniserState.Data);
        tokeniser.emit('s');
        tokeniser.error(TokeniserState.Data);
        tokeniser.emit('t');
        tokeniser.error(TokeniserState.Data);
        Assert.assertEquals(7, tokeniser.readCount());
        Token token = tokeniser.read();
        Assert.assertEquals("Test", token.getText());
    }
}