import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadWithNoCharacters() {
        Mockito.when(reader.isEmpty()).thenReturn(true);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testReadWithCharacter() {
        String input = "A";
        Mockito.when(reader.current()).thenReturn(input.charAt(0));
        Mockito.when(reader.matchConsume(input.charAt(0))).thenReturn(true);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("A", token.getData());
        assertEquals("A", token.getCharacter());
    }

    @Test(expected = AssertionError.class)
    public void testReadWithSelfClosingFlagNotAcknowledged() {
        String input = "<img src=\"image.jpg\" />";
        setUpStreamWithInput(input);
        tokeniser.read();
    }

    @Test
    public void testReadWithStartTag() {
        String input = "<div>";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.getData());
        assertEquals("div", token.getTagName());
        assertEquals(Token.Tag.Div, token.getTag());
    }

    @Test
    public void testReadWithEndTag() {
        String input = "<div><p></p></div>";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("div", token.getData());
        assertEquals("div", token.getTagName());
        assertEquals(Token.Tag.Div, token.getTag());
    }

    @Test
    public void testReadWithAttributes() {
        String input = "<img src=\"image.jpg\" alt=\"Image\" title=\"Title\"/>";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData());
        assertEquals("img", token.getTagName());
        assertEquals(Token.Tag.Img, token.getTag());
        assertNotNull(token.getAttributes());
        assertEquals(3, token.getAttributes().size());
        assertTrue(token.getAttributes().containsKey("src"));
        assertTrue(token.getAttributes().containsKey("alt"));
        assertTrue(token.getAttributes().containsKey("title"));
    }

    @Test
    public void testReadWithComment() {
        String input = "<!-- This is a comment -->";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("This is a comment", token.getData());
    }

    @Test
    public void testReadWithCharacterReference() {
        String input = "&nbsp;";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u00A0", token.getData());
        assertEquals("\u00A0", token.getCharacter());
    }

    @Test
    public void testReadWithNamedCharacterReference() {
        String input = "&eacute;";
        setUpStreamWithInput(input);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("é", token.getData());
        assertEquals("é", token.getCharacter());
    }

    @Test
    public void testReadWithDoctype() {
        String input = "<!DOCTYPE html>";
        setUpStreamWithInput(input);
        tokeniser.read(); // consume <!
        tokeniser.read(); // consume DOCTYPE
        tokeniser.read(); // consume HTML
        tokeniser.read(); // consume >
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.getData());
    }

    private void setUpStreamWithInput(String input) {
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(stream);
        tokeniser.transition(TokeniserState.Data);
    }
}