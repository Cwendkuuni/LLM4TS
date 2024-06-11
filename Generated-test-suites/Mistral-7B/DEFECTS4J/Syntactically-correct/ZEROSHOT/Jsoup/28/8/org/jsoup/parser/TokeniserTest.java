import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTest;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Test
 public void testTokeniser() {
 characterReader = new CharacterReader("Test String");
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);

 // Test read method
 Token token = tokeniser.read();
 Assert.assertNotNull(token);

 // Test emit method
 tokeniser.emit(new StartTag("div"));
 token = tokeniser.read();
 Assert.assertEquals(StartTag.class, token.getClass());
 Assert.assertEquals("div", token.tagName);
 tokeniser.emit(token);

 // Test emitString method
 tokeniser.emit("Test String");
 token = tokeniser.read();
 Assert.assertEquals(Character.class, token.getClass());
 Assert.assertEquals("Test", token.data);

 // Test error methods
 tokeniser.error(TokeniserState.StartTagBegin);
 token = tokeniser.read();
 Assert.assertEquals(Error.class, token.getClass());
 Assert.assertEquals("Unexpected character 'T' in input state [StartTagBegin]", token.data);

 // Test consumeCharacterReference method
 tokeniser.consumeCharacterReference(null, true);
 token = tokeniser.read();
 Assert.assertEquals(Character.class, token.getClass());
 Assert.assertEquals(Character.replacementChar, token.data.charAt(0));

 // Test createTagPending and emitTagPending methods
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 Assert.assertEquals(StartTag.class, token.getClass());
 Assert.assertNull(tokeniser.tagPending);

 // Test createCommentPending and emitCommentPending methods
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 Assert.assertEquals(Comment.class, token.getClass());
 Assert.assertNull(tokeniser.commentPending);

 // Test createDoctypePending and emitDoctypePending methods
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 Assert.assertEquals(Doctype.class, token.getClass());
 Assert.assertNull(tokeniser.doctypePending);

 // Test isAppropriateEndTagToken method
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 tokeniser.emit(token);
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());

 // Test getState method
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 Assert.assertEquals(TokeniserState.StartTagBegin, tokeniser.getState());

 // Test acknowledgeSelfClosingFlag method
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read();
 tokeniser.emit(new StartTag("img"));
 tokeniser.read();
 Assert.assertFalse(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);

 // Test eofError method
 tokeniser.eofError(TokeniserState.Data);
 token = tokeniser.read();
 Assert.assertEquals(Error.class, token.getClass());
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", token.data);
 }
}