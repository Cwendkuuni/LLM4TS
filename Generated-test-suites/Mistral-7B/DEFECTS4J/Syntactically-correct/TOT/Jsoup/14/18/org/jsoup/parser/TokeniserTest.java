import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("test.html");
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testReadStartTag() {
 Token startTag = tokeniser.read();
 assertEquals("start tag", Token.TokenType.StartTag, startTag.type);
 assertTrue(startTag instanceof Token.StartTag);
 }

 @Test
 public void testReadEndTag() {
 Token endTag = tokeniser.read();
 assertEquals("end tag", Token.TokenType.EndTag, endTag.type);
 assertTrue(endTag instanceof Token.EndTag);
 }

 @Test
 public void testReadCharacter() {
 char c = tokeniser.reader.current();
 tokeniser.reader.consume();
 Token characterToken = tokeniser.read();
 assertEquals("character", c, characterToken.data.charAt(0));
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.advanceTransition(TokeniserState.SelfClosingStartTag);
 Token selfClosingTag = tokeniser.read();
 assertEquals("self-closing tag", Token.TokenType.StartTag, selfClosingTag.type);
 assertTrue(selfClosingTag.selfClosing);
 }

 @Test
 public void testEmitToken() {
 Token startTag = new Token.StartTag("div");
 tokeniser.emit(startTag);
 Token emittedToken = tokeniser.read();
 assertEquals("emitted tag", "div", emittedToken.tagName);
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("test string");
 Token emittedToken = tokeniser.read();
 assertEquals("emitted string", "test string", emittedToken.data);
 }

 @Test
 public void testEmitChar() {
 tokeniser.emit('&');
 Token emittedToken = tokeniser.read();
 assertEquals("emitted char", "&", emittedToken.data.charAt(0));
 }

 @Test(expected = AssertionError.class)
 public void testErrorHandling() {
 tokeniser.error("invalid character in input");
 }

 @Test
 public void testConsumeCharacterReference() {
 Character refCharacter = tokeniser.consumeCharacterReference('a', false);
 assertEquals("ref character", 'a', refCharacter);
 }

 @Test
 public void testConsumeCharacterReferenceWithName() {
 Character refCharacter = tokeniser.consumeCharacterReference("ampersand", false);
 assertEquals("ref character", '&', refCharacter);
 }

 @Test
 public void testConsumeCharacterReferenceWithNameAndInAttribute() {
 tokeniser.advanceTransition(TokeniserState.AfterAttributeName);
 Character refCharacter = tokeniser.consumeCharacterReference("ampersand", true);
 assertNull("ref character should be null", refCharacter);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReferenceError() {
 tokeniser.consumeCharacterReference('#', false);
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tagPending = tokeniser.createTagPending(true);
 assertEquals("tag pending", Token.TokenType.StartTag, tagPending.type);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token emittedToken = tokeniser.read();
 Token tagPendingToken = (Token.StartTag) tokeniser.tagPending;
 assertEquals("emitted tag", tagPendingToken.tagName, emittedToken.tagName);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token emittedToken = tokeniser.read();
 assertEquals("emitted comment", Token.TokenType.Comment, emittedToken.type);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token emittedToken = tokeniser.read();
 assertEquals("emitted doctype", Token.TokenType.Doctype, emittedToken.type);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertTrue("appropriate end tag token", tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue("track errors is true by default", tokeniser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 tokeniser.setTrackErrors(false);
 assertFalse("track errors is false", tokeniser.isTrackErrors());
 }

 @Test
 public void testParseError() {
 tokeniser.error("parse error");
 Token parseErrorToken = tokeniser.read();
 assertEquals("parse error token", ParseError.class, parseErrorToken.getClass());
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 Token eofErrorToken = tokeniser.read();
 assertEquals("eof error token", ParseError.class, eofErrorToken.getClass());
 }
}