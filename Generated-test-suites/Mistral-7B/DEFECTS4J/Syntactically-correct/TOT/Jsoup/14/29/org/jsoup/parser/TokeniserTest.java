import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 't', 'y', 'p', 'e', ' ', 's', 'c', 'r', 'i', 'p', 't', '>', '\n'}));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testReadValidHtml() {
 Token token;
 while ((token = tokeniser.read()) != null) {
 System.out.println(token);
 }
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.advanceTransition(TokeniserState.SelfClosingEndTag);
 tokeniser.acknowledgeSelfClosingFlag();
 Token tag = tokeniser.read();
 Assert.assertNotNull(tag);
 Assert.assertEquals(Token.TokenType.EndTag, tag.type);
 Assert.assertEquals("img", tag.tagName);
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testReadInvalidCharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(replacementChar, tokeniser.read().getData());
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testReadInvalidNamedCharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference("xyz", false);
 Assert.assertEquals(replacementChar, tokeniser.read().getData());
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testReadEmptyInput() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{}));
 tokeniser = new Tokeniser(reader);
 Token token;
 while ((token = tokeniser.read()) != null) {
 System.out.println(token);
 }
 Assert.assertEquals(0, errors.size());
 }

 @Test(expected = IOException.class)
 public void testReadInvalidInput() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 't', 'y', 'p', 'e', ' ', 's', 'c', 'r', 'i', 'p', 't', '>', 'A', 'B', 'C'}));
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 }

 @Test
 public void testReadEOF() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 't', 'y', 'p', 'e', ' ', 's', 'c', 'r', 'i', 'p', 't', '>', '\n'}));
 tokeniser.read();
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("amp", false);
 tokeniser.consume(';');
 tokeniser.consume('>');
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals(1, errors.size());
 }
}