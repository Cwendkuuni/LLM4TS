import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() throws IOException {
 Token token;

 // Test initial state
 token = tokeniser.read();
 assertNull(token);

 // Test reading characters
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');
 token = tokeniser.read();
 assertEquals("test", token.asCharacter().toString());

 // Test reading whitespace
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit('\n');
 tokeniser.emit('\r');
 token = tokeniser.read();
 assertNull(token);

 // Test self-closing tag
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('>');
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertNull(token);

 // Test start tag
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('r');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('r');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals("start", token.asStartTag().getName());

 // Test end tag
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('r');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals("end", token.asEndTag().getName());

 // Test comment
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit('*');
 tokeniser.emit(' ');
 tokeniser.emit('c');
 tokeniser.emit('o');
 tokeniser.emit('m');
 tokeniser.emit('m');
 tokeniser.emit('e');
 tokeniser.emit('n');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('c');
 tokeniser.emit('o');
 tokeniser.emit('m');
 tokeniser.emit('m');
 tokeniser.emit('e');
 tokeniser.emit('n');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals("comment", token.asComment().getData());

 // Test CDATA
 tokeniser.transition(TokeniserState.CDATA);
 tokeniser.emit('<');
 tokeniser.emit('!');
 tokeniser.emit('[');
 tokeniser.emit('C');
 tokeniser.emit('D');
 tokeniser.emit('A');
 tokeniser.emit('T');
 tokeniser.emit('A');
 tokeniser.emit(']');
 tokeniser.emit('>');
 tokeniser.emit('t');tokeniser.emit('e');tokeniser.emit('s');tokeniser.emit('t');tokeniser.emit(' ');tokeniser.emit('<');tokeniser.emit('/');tokeniser.emit('!');tokeniser.emit('[');tokeniser.emit('C');tokeniser.emit('D');tokeniser.emit('A');tokeniser.emit('T');tokeniser.emit('A');tokeniser.emit(']');tokeniser.emit('>');token = tokeniser.read(); assertNull(token);

 // Test doctype
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emit('<');
 tokeniser.emit('!');
 tokeniser.emit('D');
 tokeniser.emit('O');
 tokeniser.emit('C');
 tokeniser.emit('T');
 tokeniser.emit('Y');
 tokeniser.emit('P');
 tokeniser.emit('E');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals("DOCTYPE", token.asDoctype().getName());

 // Test error handling
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('a');
 tokeniser.emit('b');
 tokeniser.emit('c');
 tokeniser.error("Unexpected character '%s'", reader.current());
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadAfterError() throws IOException {
 Token token;

 // Test reading after a character reference error
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.emit('#');
 tokeniser.error("numeric reference with no numerals");
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertNull(token);

 // Test reading after an invalid named reference error
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit('m');
 tokeniser.emit('p');
 tokeniser.emit(';');
 tokeniser.error("invalid named referenece '%s'", new String(reader.consumeLetterSequence().getBytes()));
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertNull(token);

 // Test reading after an eof error
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('>');
 tokeniser.eofError(TokeniserState.TagName);
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("test<div><p></p></div>".getBytes())), new ParseErrorList());
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('/');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit('>');
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithInvalidData() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("test<div><p></p></divtest".getBytes())), new ParseErrorList());
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('/');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit('>');
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("test<div><p></p></div>".getBytes())), new ParseErrorList());
 assertEquals("div", tokeniser.appropriateEndTagName());
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('/');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit('>');
 assertEquals("div", tokeniser.appropriateEndTagName());
 }

 @Test
 public void testAppropriateEndTagNameWithInvalidData() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("test<div><p></p></divtest".getBytes())), new ParseErrorList());
 assertEquals("div", tokeniser.appropriateEndTagName());
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit('/');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit('>');
 assertEquals("div", tokeniser.appropriateEndTagName());
 }
}