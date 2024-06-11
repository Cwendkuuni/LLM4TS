import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 byte[] input = "test".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read();
 assertEquals("t", tokeniser.read().getText());
 assertEquals("e", tokeniser.read().getText());
 assertEquals("st", tokeniser.read().getText());
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadSelfClosingTag() {
 byte[] input = "<br/>".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // t
 tokeniser.read(); // <
 tokeniser.read(); // b
 tokeniser.read(); // r
 tokeniser.read(); // /

 Token tag = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, tag.getType());
 assertEquals("br", tag.getText());

 assertFalse(tokeniser.isEmitPending());
 }

 @Test(expected = NullPointerException.class)
 public void testReadNullReader() {
 tokeniser = new Tokeniser(null, errors);
 tokeniser.read();
 }

 @Test
 public void testReadComment() {
 byte[] input = "<!-- test -->".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // <
 tokeniser.read(); // !
 tokeniser.read(); // --
 tokeniser.read(); // >

 Token comment = tokeniser.read();
 assertEquals(Token.TokenType.Comment, comment.getType());
 assertEquals("test", comment.getText());

 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testReadEndTag() {
 byte[] input = "<p></p>".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // <
 tokeniser.read(); // p
 tokeniser.read(); // >

 tokeniser.read(); // <
 tokeniser.read(); // /
 tokeniser.read(); // p
 tokeniser.read(); // >

 Token endTag = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, endTag.getType());
 assertEquals("p", endTag.getText());

 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testReadStartTag() {
 byte[] input = "<div class='test' id='test'>test</div>".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // <
 tokeniser.read(); // d
 tokeniser.read(); // i
 tokeniser.read(); // v

 Token startTag = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, startTag.getType());
 assertEquals("div", startTag.getText());

 List<String> attributes = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.Data) {
 tokeniser.read();
 if (tokeniser.isEmitPending()) {
 Token attribute = tokeniser.emitPending;
 assertEquals(Token.TokenType.StartTag, attribute.getType());
 assertEquals("class", attribute.getAttributeName());
 assertEquals("test", attribute.getAttributeValue());

 tokeniser.emitPending = null;
 }
 if (tokeniser.isEmitPending()) {
 Token attribute = tokeniser.emitPending;
 assertEquals(Token.TokenType.StartTag, attribute.getType());
 assertEquals("id", attribute.getAttributeName());
 assertEquals("test", attribute.getAttributeValue());

 tokeniser.emitPending = null;
 }
 if (tokeniser.read().getText().equals(">")) {
 break;
 }
 }

 assertEquals("test", tokeniser.read().getText());
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadCharacterReference() {
 byte[] input = "&a;&b;&#x61;&#x41;&#x1F441;".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // &

 Character reference = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 97, reference); // 'a'

 reference = tokeniser.consumeCharacterReference('x', false);
 assertEquals((char) 97, reference); // 'a'

 reference = tokeniser.consumeCharacterReference(null, true); // in attribute
 assertNull(reference);

 tokeniser.read(); // #
 tokeniser.read(); // x
 tokeniser.read(); // 6
 tokeniser.read(); // 1

 Character hexReference = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 54913, hexReference); // '𛃵'

 tokeniser.consumeCharacterReference(';', false); // consume semicolon

 reference = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 117463, reference); // '𝄞'

 assertFalse(tokeniser.isEmitPending());
 }

 @Test(expected = RuntimeException.class)
 public void testConsumeCharacterReferenceInvalidEntityName() {
 byte[] input = "&foo;".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testReadDoctype() {
 byte[] input = "<!DOCTYPE html>".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser.read(); // <
 tokeniser.read(); // !
 tokeniser.read(); // D
 tokeniser.read(); // O
 tokeniser.read(); // C
 tokeniser.read(); // T
 tokeniser.read(); // Y
 tokeniser.read(); // P
 tokeniser.read(); // E
 tokeniser.read(); // >

 Token doctype = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, doctype.getType());
 assertEquals("html", doctype.getText());

 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testReadEmptyInput() {
 reader.reset(new ByteArrayInputStream(new byte[0]));
 tokeniser = new Tokeniser(reader, errors);

 assertFalse(tokeniser.isEmitPending());
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadInvalidInput() {
 byte[] input = "abc<xyz".getBytes();
 reader.reset(new ByteArrayInputStream(input));

 tokeniser = new Tokeniser(reader, errors);

 assertEquals("a", tokeniser.read().getText());
 assertEquals("b", tokeniser.read().getText());
 assertEquals("c", tokeniser.read().getText());
 tokeniser.error(TokeniserState.Data);

 assertEquals("Unexpected character '<' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testReadEndOfFile() {
 byte[] input = "abc".getBytes();
 reader.reset(new ByteArrayInputStream(input));
 tokeniser = new Tokeniser(reader, errors);

 assertEquals("a", tokeniser.read().getText());
 assertEquals("b", tokeniser.read().getText());
 tokeniser.error(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);

 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
 }
}