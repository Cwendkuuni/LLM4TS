import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("validInput.html"); // replace with actual input file
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @After
 public void tearDown() {
 reader.close();
 errors.clear();
 }

 @Test
 public void testReadEmptyInput() {
 assertEquals(Token.TokenType.EndDocument, tokeniser.read().type);
 }

 @Test
 public void testReadSingleCharacter() {
 tokeniser.emit("a");
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testReadMultipleCharacters() {
 tokeniser.emit("ab");
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testReadWhitespace() {
 tokeniser.emit(" ");
 tokeniser.emit("\t");
 tokeniser.emit("\r");
 tokeniser.emit("\n");
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testReadInvalidCharacter() {
 tokeniser.emit('&');
 tokeniser.read(); // consume &
 tokeniser.emit(';');
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.emit("<img src=\"image.png\" />");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
 assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
 }

 @Test
 public void testReadEndTagWithoutStartTag() {
 tokeniser.emit("<html><body>"); // consume start tag
 tokeniser.emit("</body>"); // read end tag without corresponding start tag
 assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
 assertEquals(Token.TokenType.Error, tokeniser.errors.get(0).type);
 }

 @Test
 public void testReadInvalidEndTag() {
 tokeniser.emit("<html><body>"); // consume start tag
 tokeniser.emit("</foobar>"); // read invalid end tag
 assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
 assertEquals(Token.TokenType.Error, tokeniser.errors.get(0).type);
 }

 @Test
 public void testReadInvalidStartTag() {
 tokeniser.emit("<foobar>"); // read invalid start tag
 assertEquals(Token.TokenType.Error, tokeniser.read().type);
 assertEquals(Token.TokenType.Error, tokeniser.errors.get(0).type);
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.emit("<script>"); // consume start tag
 tokeniser.read(); // consume <
 tokeniser.read(); // consume s
 tokeniser.read(); // consume c
 tokeniser.read(); // consume r
 tokeniser.read(); // consume i
 tokeniser.read(); // consume p
 tokeniser.read(); // consume t
 tokeniser.read(); // consume >
 assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
 assertEquals(Token.TokenType.TagName, tokeniser.read().type);
 assertEquals("script", tokeniser.read().data);
 assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
 }

 @Test
 public void testReadCommentTag() {
 tokeniser.emit("<!--comment-->"); // consume <!--
 tokeniser.read(); // consume c
 tokeniser.read(); // consume o
 tokeniser.read(); // consume m
 tokeniser.read(); // consume m
 tokeniser.read(); // consume e
 tokeniser.read(); // consume n
 tokeniser.read(); // consume t
 tokeniser.read(); // consume -->
 assertEquals(Token.TokenType.Comment, tokeniser.read().type);
 }

 @Test
 public void testReadDoctypeTag() {
 tokeniser.emit("<!DOCTYPE html>"); // consume <!
 tokeniser.read(); // consume D
 tokeniser.read(); // consume O
 tokeniser.read(); // consume T
 tokeniser.read(); // consumeYPE
 tokeniser.read(); // consume space
 tokeniser.read(); // consume h
 tokeniser.read(); // consume t
 tokeniser.read(); // consume m
 tokeniser.read(); // consume l
 tokeniser.read(); // consume >
 assertEquals(Token.TokenType.Doctype, tokeniser.read().type);
 }

 @Test
 public void testReadTextNode() {
 tokeniser.emit("This is a text node.");
 assertEquals(Token.TokenType.Text, tokeniser.read().type);
 assertEquals("This is a text node.", tokeniser.read().data);
 }

 @Test
 public void testReadCdataSection() {
 tokeniser.emit("<![CDATA[This is a CDATA section]]>"); // consume <![CDATA[
 tokeniser.read(); // consume C
 tokeniser.read(); // consume D
 tokeniser.read(); // consume A
 tokeniser.read(); // consume T
 tokeniser.read(); // consume A
 tokeniser.read(); // consume S
 tokeniser.read(); // consume e
 tokeniser.read(); // consume c
 tokeniser.read(); // consume t
 tokeniser.read(); // consume i
 tokeniser.read(); // consume o
 tokeniser.read(); // consume n
 tokeniser.read(); // consume ]]
 assertEquals(Token.TokenType.Cdata, tokeniser.read().type);
 }

 @Test
 public void testReadErrorHandling() {
 tokeniser.emit("<html><body>"); // consume start tag
 tokeniser.emit("<script>"); // consume start script tag
 tokeniser.emit("<!--"); // consume comment start tag
 tokeniser.emit("-->"); // consume comment end tag
 tokeniser.emit("</script>"); // consume end script tag
 tokeniser.emit("</body>"); // consume end body tag
 tokeniser.emit("</html>"); // consume end html tag
 tokeniser.read(); // consume EOF
 assertEquals(Token.TokenType.EndDocument, tokeniser.read().type); // verify end document token
 assertEquals(3, errors.size()); // verify 3 errors were encountered
 assertEquals("Unexpected character '<' in input state [AfterStartTagName]", errors.get(0).getMessage());
 assertEquals("Unexpected character '-' in input state [AfterComment]", errors.get(1).getMessage());
 assertEquals("Unexpected character '>' in input state [AfterEndTagName]", errors.get(2).getMessage());
 }
}