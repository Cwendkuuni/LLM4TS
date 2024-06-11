import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private Tokeniser tokeniser;
 private Token expectedToken;

 @Before
 public void setUp() {
 mockCharacterReader = Mockito.mock(CharacterReader.class);
 tokeniser = new Tokeniser(mockCharacterReader);
 }

 @Test
 public void testDefaultInitialisation() {
 // Check default initialisation
 assertEquals(TokeniserState.Data, tokeniser.getState());
 assertFalse(tokeniser.isEmitPending());
 assertNull(tokeniser.emitPending);
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 assertNull(tokeniser.tagPending);
 assertNull(tokeniser.doctypePending);
 assertNull(tokeniser.commentPending);
 assertNull(tokeniser.lastStartTag);
 assertFalse(tokeniser.isTrackErrors());
 assertNotNull(tokeniser.errors);
 assertFalse(tokeniser.errors.isEmpty());
 }

 @Test
 public void testReadCharacter() {
 // Test reading a character
 Mockito.when(mockCharacterReader.current()).thenReturn('a');
 Mockito.when(mockCharacterReader.read()).thenReturn(true);

 Token actualToken = tokeniser.read();

 assertEquals(Token.Character.class, actualToken.getClass());
 assertEquals('a', ((Token.Character) actualToken).charValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWhenSelfClosingFlagNotAcknowledged() {
 // Test reading when selfClosingFlagNotAcknowledged is false
 Mockito.when(mockCharacterReader.current()).thenReturn('a');

 tokeniser.read();
 tokeniser.error("Self closing flag not acknowledged");

 Mockito.verify(mockCharacterReader, Mockito.times(1)).current();
 Mockito.verify(mockCharacterReader, Mockito.times(1)).mark();
 Mockito.verify(mockCharacterReader, Mockito.never()).read();
 }

 @Test
 public void testReadWhenSelfClosingFlagAcknowledged() {
 // Test reading when selfClosingFlagNotAcknowledged is true
 Mockito.when(mockCharacterReader.current()).thenReturn('a');
 Mockito.when(mockCharacterReader.read()).thenReturn(true);

 tokeniser.acknowledgeSelfClosingFlag();

 Token actualToken = tokeniser.read();

 assertEquals(Token.Character.class, actualToken.getClass());
 assertEquals('a', ((Token.Character) actualToken).charValue());
 }

 @Test
 public void testEmit() {
 // Test emitting a character
 char character = 'a';
 Token.Character token = new Token.Character(String.valueOf(character));

 tokeniser.emit(token);

 assertEquals(token, tokeniser.emitPending);
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testEmitStartTag() {
 // Test emitting a start tag
 Token.StartTag startTag = new Token.StartTag("div", null, null, null, false);

 tokeniser.emit(startTag);

 assertEquals(startTag, tokeniser.emitPending);
 assertTrue(tokeniser.isEmitPending());
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testEmitEndTag() {
 // Test emitting an end tag
 Token.EndTag endTag = new Token.EndTag("div");

 tokeniser.emit(endTag);

 assertEquals(endTag, tokeniser.emitPending);
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testTransitionToDataState() {
 // Test transitioning to Data state
 tokeniser.transition(TokeniserState.Data);

 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransitionToStartTagState() {
 // Test transitioning to StartTag state
 tokeniser.transition(TokeniserState.StartTag);

 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testTransitionToEndTagState() {
 // Test transitioning to EndTag state
 tokeniser.transition(TokeniserState.EndTag);

 assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testTransitionToScriptState() {
 // Test transitioning to Script state
 tokeniser.transition(TokeniserState.Script);

 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testTransitionToDoctypeState() {
 // Test transitioning to Doctype state
 tokeniser.transition(TokeniserState.Doctype);

 assertEquals(TokeniserState.Doctype, tokeniser.getState());
 }

 @Test
 public void testTransitionToCommentState() {
 // Test transitioning to Comment state
 tokeniser.transition(TokeniserState.Comment);

 assertEquals(TokeniserState.Comment, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference() {
 // Test consuming a character reference
 Mockito.when(mockCharacterReader.current()).thenReturn('&');
 Mockito.when(mockCharacterReader.matchConsume("#")).thenReturn(true);
 Mockito.when(mockCharacterReader.consumeHexSequence()).thenReturn("1F");
 Mockito.when(mockCharacterReader.matchConsume(";")).thenReturn(true);

 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);

 assertEquals(Character.toChar(0x1F), actualCharacter.charValue());
 }

 @Test
 public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
 // Test consuming a character reference with an additional allowed character
 Mockito.when(mockCharacterReader.current()).thenReturn('&');
 Mockito.when(mockCharacterReader.matchConsume("#")).thenReturn(true);
 Mockito.when(mockCharacterReader.consumeHexSequence()).thenReturn("1F");
 Mockito.when(mockCharacterReader.consumeLetter()).thenReturn('x');
 Mockito.when(mockCharacterReader.matchConsume(";")).thenReturn(true);

 Character actualCharacter = tokeniser.consumeCharacterReference('#x1F', true);

 assertEquals(Character.toChar(0x1F), actualCharacter.charValue());
 }

 @Test
 public void testConsumeCharacterReferenceWithNamedEntity() {
 // Test consuming a named character reference
 Mockito.when(mockCharacterReader.current()).thenReturn('&');
 Mockito.when(mockCharacterReader.consumeLetterSequence()).thenReturn("amp");
 Mockito.when(mockCharacterReader.matchConsume(";")).thenReturn(true);

 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);

 assertEquals(Character.AMPERSAND, actualCharacter.charValue());
 }

 @Test
 public void testConsumeCharacterReferenceWithInvalidCharacterReference() {
 // Test consuming an invalid character reference
 Mockito.when(mockCharacterReader.current()).thenReturn('&');
 Mockito.when(mockCharacterReader.consumeLetterSequence()).thenReturn("notexistent");
 Mockito.when(mockCharacterReader.matchConsume(";")).thenReturn(true);

 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);

 assertEquals(Tokeniser.replacementChar, actualCharacter.charValue());
 }

 @Test
 public void testCreateTagPending() {
 // Test creating a tag pending
 Token.StartTag startTag = tokeniser.createTagPending(true);

 assertEquals(Token.StartTag.class, startTag.getClass());
 assertEquals("", startTag.tagName);
 assertNull(startTag.tagArgs);
 assertNull(startTag.selfClosing);
 }

 @Test
 public void testCreateTagPendingWithTagName() {
 // Test creating a tag pending with a tag name
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.tagName = "div";

 assertEquals("div", startTag.tagName);
 }

 @Test
 public void testCreateTagPendingWithTagArgs() {
 // Test creating a tag pending with tag args
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.tagArgs = new ArrayList<Token.Attribute>() {{
 add(new Token.Attribute("class", "class1 class2"));
 add(new Token.Attribute("id", "id1"));
 }};

 assertEquals("class1 class2", startTag.tagArgs.get(0).attributeName);
 assertEquals("class1 class2", startTag.tagArgs.get(0).attributeValue);
 assertEquals(2, startTag.tagArgs.size());
 }

 @Test
 public void testCreateTagPendingWithSelfClosing() {
 // Test creating a tag pending with selfClosing flag set to true
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.selfClosing = true;

 assertTrue(startTag.selfClosing);
 }

 @Test
 public void testCreateCommentPending() {
 // Test creating a comment pending
 Token.Comment comment = tokeniser.createCommentPending();

 assertEquals(Token.Comment.class, comment.getClass());
 assertEquals("", comment.commentText);
 }

 @Test
 public void testCreateCommentPendingWithCommentText() {
 // Test creating a comment pending with comment text
 Token.Comment comment = tokeniser.createCommentPending();
 comment.commentText = "This is a comment";

 assertEquals("This is a comment", comment.commentText);
 }

 @Test
 public void testCreateDoctypePending() {
 // Test creating a doctype pending
 Token.Doctype doctype = tokeniser.createDoctypePending();

 assertEquals(Token.Doctype.class, doctype.getClass());
 assertEquals("", doctype.publicIdentifier);
 assertEquals("", doctype.systemIdentifier);
 }

 @Test
 public void testCreateDoctypePendingWithPublicIdentifier() {
 // Test creating a doctype pending with public identifier
 Token.Doctype doctype = tokeniser.createDoctypePending();
 doctype.publicIdentifier = "public";

 assertEquals("public", doctype.publicIdentifier);
 }

 @Test
 public void testCreateDoctypePendingWithSystemIdentifier() {
 // Test creating a doctype pending with system identifier
 Token.Doctype doctype = tokeniser.createDoctypePending();
 doctype.systemIdentifier = "system";

 assertEquals("system", doctype.systemIdentifier);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 // Test isAppropriateEndTagToken method
 Token.StartTag startTag = new Token.StartTag("div");
 Token.EndTag endTag = new Token.EndTag("div");

 assertTrue(tokeniser.isAppropriateEndTagToken(startTag, endTag));
 }

 @Test
 public void testIsAppropriateEndTagTokenWithDifferentTags() {
 // Test isAppropriateEndTagToken method with different tags
 Token.StartTag startTag = new Token.StartTag("div");
 Token.EndTag endTag = new Token.EndTag("p");

 assertFalse(tokeniser.isAppropriateEndTagToken(startTag, endTag));
 }

 @Test
 public void testEmitTagPending() {
 // Test emitting a tag pending
 Token.StartTag startTag = new Token.StartTag("div");

 tokeniser.emitTagPending();

 assertEquals(startTag, tokeniser.tagPending);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testEmitCommentPending() {
 // Test emitting a comment pending
 Token.Comment comment = new Token.Comment("This is a comment");

 tokeniser.emitCommentPending();

 assertEquals(comment, tokeniser.commentPending);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testEmitDoctypePending() {
 // Test emitting a doctype pending
 Token.Doctype doctype = new Token.Doctype("public", "system");

 tokeniser.emitDoctypePending();

 assertEquals(doctype, tokeniser.doctypePending);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testCreateTempBuffer() {
 // Test creating a temp buffer
 StringBuilder tempBuffer = new StringBuilder();
 tokeniser.createTempBuffer();

 assertEquals(tempBuffer, tokeniser.dataBuffer);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testIsTrackErrors() {
 // Test isTrackErrors method
 tokeniser.setTrackErrors(true);

 assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrorsWithFalse() {
 // Test isTrackErrors method with false
 tokeniser.setTrackErrors(false);

 assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError() {
 // Test error method
 tokeniser.error(TokeniserState.Data);

 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testEofError() {
 // Test eofError method
 tokeniser.eofError(TokeniserState.Data);

 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testCharacterReferenceError() {
 // Test characterReferenceError method
 tokeniser.characterReferenceError();

 assertTrue(tokeniser.errors.size() > 0);
 }
}