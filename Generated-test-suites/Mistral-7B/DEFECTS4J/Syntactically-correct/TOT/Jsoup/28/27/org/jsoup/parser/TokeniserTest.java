import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacterTokens() {
 String input = "Hello World!";
 Mockito.when(reader.read()).thenReturn('H').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn(' ').thenReturn('W').thenReturn('o').thenReturn('r').thenReturn('l').thenReturn('d').thenReturn('!').thenReturn((char) 0);

 for (int i = 0; i < input.length(); i++) {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(Character.toString(token.content), String.valueOf(input.charAt(i)));
 }

 Mockito.verify(reader, Mockito.times(input.length() + 1)).read(); // one extra read for the final null character
 }

 @Test(expected = AssertionError.class)
 public void testSelfClosingFlagNotAcknowledged() {
 Mockito.when(reader.read()).thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('/').thenReturn('>');
 tokeniser.read(); // reads "html"
 tokeniser.emit(new Token.StartTag(Tag.HTML));

 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads "script"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads ">"
 tokeniser.emit(new Token.EndTag(Tag.SCRIPT));

 tokeniser.read(); // reads "!"
 tokeniser.error("Self closing flag not acknowledged");
 }

 @Test
 public void testReadStringTokens() {
 String input = "Hello &World!";
 Mockito.when(reader.read()).thenReturn('H').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn(' ').thenReturn('&').thenReturn('W').thenReturn('o').thenReturn('r').thenReturn('l').thenReturn('d').thenReturn('!').thenReturn((char) 0);

 tokeniser.emit("Hello");
 tokeniser.read(); // reads "&"
 tokeniser.emit('&');
 tokeniser.emit('W');
 tokeniser.emit('o');
 tokeniser.emit('r').emit('l').emit('d');
 tokeniser.emit("!");

 Mockito.verify(reader, Mockito.times(input.length() + 1)).read();
 }

 @Test
 public void testConsumeCharacterReference() {
 String input = "&foo; &bar;\n&foobar; &baz &qux";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("foo").thenReturn("bar").thenReturn("foobar").thenReturn("baz").thenReturn("qux");
 Mockito.when(reader.matchConsume('#')).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);

 List<Character> expected = new ArrayList<>();
 expected.add('f'); expected.add('o'); expected.add('o');
 expected.add(' ');
 expected.add('b'); expected.add('a'); expected.add('r');
 expected.add('\n');
 expected.add('f'); expected.add('o'); expected.add('o'); expected.add('b'); expected.add('a'); expected.add('r');
 expected.add(' ');
 expected.add('b'); expected.add('a'); expected.add('z');
 expected.add(' '); expected.add('q'); expected.add('u'); expected.add('x');

 for (int i = 0; i < input.length(); i++) {
 Character character = tokeniser.consumeCharacterReference(null, false);
 if (character != null) {
 assertEquals(character, expected.get(i));
 expected.remove(0);
 } else {
 assertNull(character);
 }
 }

 Mockito.verifyNoMoreInteractions(reader);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReferenceWithInvalidCharacter() {
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("foo");
 Mockito.when(reader.matchConsume('#')).thenReturn(true);

 tokeniser.consumeCharacterReference((char) 0, false);
 }

 @Test
 public void testConsumeCharacterReferenceWithIncompleteNamedEntity() {
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("abc").thenReturn(";");

 tokeniser.consumeCharacterReference("abc", false);
 }

 @Test
 public void testConsumeCharacterReferenceWithInvalidNamedEntity() {
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("xyzzy");
 Mockito.when(reader.matchConsume('#')).thenReturn(true);

 tokeniser.consumeCharacterReference("xyzzy", false);
 }

 @Test
 public void testReadEndTagToken() throws IOException {
 String input = "<html><body><script src='script.js'></script></body></html>";
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
 reader = new CharacterReader(inputStream);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);

 tokeniser.emit("html");
 tokeniser.read(); // reads "<"
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.read(); // reads "b"
 tokeniser.emit(new Token.Character("o"));
 tokeniser.read(); // reads "d"
 tokeniser.emit(new Token.Character("y"));
 tokeniser.read(); // reads "<"
 tokeniser.emit(new Token.StartTag("body"));
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads "s"
 tokeniser.read(); // reads "c"
 tokeniser.read(); // reads "r"
 tokeniser.read(); // reads "i"
 tokeniser.read(); // reads "p"
 tokeniser.read(); // reads "t"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads "<"
 tokeniser.emit(new Token.StartTag("script"));
 tokeniser.read(); // reads "s"
 tokeniser.read(); // reads "c"
 tokeniser.read(); // reads "r"
 tokeniser.read(); // reads="'
 tokeniser.read(); // reads "i"
 tokeniser.read(); // reads "p"
 tokeniser.read(); // reads=" script.js"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads "s"
 tokeniser.read(); // reads "c"
 tokeniser.read(); // reads "r"
 tokeniser.read(); // reads "i"
 tokeniser.read(); // reads "p"
 tokeniser.read(); // reads "t"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads "</"
 tokeniser.read(); // reads "b"
 tokeniser.read(); // reads "o"
 tokeniser.read(); // reads "d"
 tokeniser.read(); // reads "y"
 tokeniser.read(); // reads ">"
 tokeniser.emit(new Token.EndTag("body"));
 tokeniser.read(); // reads "</"
 tokeniser.read(); // reads "h"
 tokeniser.read(); // reads "t"
 tokeniser.read(); // reads "m"
 tokeniser.read(); // reads "l"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads ">"

 assertNotNull(tokeniser.getState());
 assertEquals(TokeniserState.End, tokeniser.getState());
 }

 @Test
 public void testReadEndTagWithMissingEndTag() {
 String input = "<html><body><script src='script.js'></script>";
 Mockito.when(reader.read()).thenReturn('H').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn(' ').thenReturn('<').thenReturn('b').thenReturn('o').thenReturn('d').thenReturn('y').thenReturn('>').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('/').thenReturn('>').thenReturn('>').thenReturn('\n').thenReturn(0);

 tokeniser.emit("html");
 tokeniser.read(); // reads "<"
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.read(); // reads "b"
 tokeniser.read(); // reads "o"
 tokeniser.read(); // reads "d"
 tokeniser.read(); // reads "y"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads "s"
 tokeniser.read(); // reads "c"
 tokeniser.read(); // reads "r"
 tokeniser.read(); // reads "i"
 tokeniser.read(); // reads "p"
 tokeniser.read(); // reads "t"
 tokeniser.read(); // reads "/"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads ">"
 tokeniser.read(); // reads "\n"
 tokeniser.error("Unexpected character '\\n' in state [BODY]");
 }
}