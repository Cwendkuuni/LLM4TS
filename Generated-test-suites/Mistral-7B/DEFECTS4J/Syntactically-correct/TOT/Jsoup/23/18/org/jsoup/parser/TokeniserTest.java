import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    @Captor
    private ArgumentCaptor<ParseError> errorCaptor;

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
    public void testReadNullCharacterReader() {
        // This test case is to check the behavior of Tokeniser when the CharacterReader is null.

        Tokeniser tokeniser = new Tokeniser(null, errors);
        tokeniser.read();

        // Check for the expected ParseError message.
        assertEquals("There is an unread token pending!", errorCaptor.getValue().getMessage());
    }

    @Test
    public void testReadEmptyCharacterStream() throws IOException {
        // This test case is to check the behavior of Tokeniser when the CharacterReader is empty.

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Mockito.when(reader.current()).thenReturn((char) 0);
        Mockito.when(reader.isEmpty()).thenReturn(true);
        Mockito.when(reader.matchConsume(Mockito.anyChar())).thenAnswer(i -> reader.current() == i.getArguments()[0]);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with an empty CharacterReader.
        Token token = tokeniser.read();

        // Check for the expected empty StringBuilder.
        assertNull(token);

        // Check for the expected ParseError message.
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF)", errorCaptor.getValue().getMessage());
    }

    @Test
    public void testReadCharacter() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads a single character.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("a".getBytes());
        Mockito.when(reader.current()).thenReturn('a');
        Mockito.when(reader.matchConsume(Mockito.anyChar())).thenAnswer(i -> reader.current() == i.getArguments()[0]);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with a single character.
        Token token = tokeniser.read();

        // Check for the expected Character token.
        assertEquals(TokenType.Character, token.getType());
        assertEquals("a", token.getData());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadMultipleCharacters() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads multiple characters.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("abc".getBytes());
        Mockito.when(reader.current()).thenReturn('a');
        Mockito.when(reader.matchConsume(Mockito.anyChar())).thenAnswer(i -> reader.current() == i.getArguments()[0]);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with multiple characters.
        Token token1 = tokeniser.read();
        Token token2 = tokeniser.read();
        Token token3 = tokeniser.read();

        // Check for the expected Character tokens.
        assertEquals(TokenType.Character, token1.getType());
        assertEquals("a", token1.getData());

        assertEquals(TokenType.Character, token2.getType());
        assertEquals("b", token2.getData());

        assertEquals(TokenType.Character, token3.getType());
        assertEquals("c", token3.getData());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadStartTag() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads a start tag.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("<div class='test'>".getBytes());
        Mockito.when(reader.current()).thenReturn('<');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("/")).thenReturn(false);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with a start tag.
        Token token = tokeniser.read();

        // Check for the expected StartTag token.
        assertEquals(TokenType.StartTag, token.getType());
        assertEquals("div", token.getData().tagName);
        assertTrue(token.getData().attributes.isEmpty());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadSelfClosingStartTag() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads a self-closing start tag.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("<img src='test.png'/>".getBytes());
        Mockito.when(reader.current()).thenReturn('<');
        Mockito.when(reader.matchConsumeIgnoreCase("img")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("/")).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with a self-closing start tag.
        Token token = tokeniser.read();

        // Check for the expected StartTag token.
        assertEquals(TokenType.StartTag, token.getType());
        assertEquals("img", token.getData().tagName);
        assertTrue(token.getData().attributes.isEmpty());
        assertFalse(token.getData().selfClosing);

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadEndTag() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads an end tag.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("</div>".getBytes());
        Mockito.when(reader.current()).thenReturn('/');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with an end tag.
        Token token = tokeniser.read();

        // Check for the expected EndTag token.
        assertEquals(TokenType.EndTag, token.getType());
        assertEquals("div", token.getData().tagName);
        assertTrue(token.getData().attributes.isEmpty());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadEndTagWithAttributes() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads an end tag with attributes.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("</div class='test'>".getBytes());
        Mockito.when(reader.current()).thenReturn('/');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with an end tag with attributes.
        Token token = tokeniser.read();

        // Check for the expected EndTag token.
        assertEquals(TokenType.EndTag, token.getType());
        assertEquals("div", token.getData().tagName);
        assertEquals(Arrays.asList("class", "test"), token.getData().attributes.keySet());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testReadInvalidCharacterReference() throws IOException {
        // This test case is to check the behavior of Tokeniser when it encounters an invalid character reference.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#x1234;".getBytes());
        Mockito.when(reader.current()).thenReturn('#');
        Mockito.when(reader.matchConsumeIgnoreCase("x")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("1234")).thenReturn(true);
        Mockito.when(reader.matchConsume(';')).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with an invalid character reference.
        Character character = tokeniser.consumeCharacterReference(null, false);

        // Check for the expected replacement character.
        assertEquals(Tokeniser.replacementChar, character);

        // Check for the expected ParseError message.
        assertTrue(errors.size() > 0);
        assertEquals("Invalid character reference: character outside of valid range", errorCaptor.getValue().getMessage());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());
    }

    @Test
    public void testReadValidCharacterReference() throws IOException {
        // This test case is to check the behavior of Tokeniser when it encounters a valid character reference.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#x20ac;".getBytes());
        Mockito.when(reader.current()).thenReturn('#');
        Mockito.when(reader.matchConsumeIgnoreCase("x")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("20")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("A")).thenReturn(true);
        Mockito.when(reader.matchConsume(';')).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with a valid character reference.
        Character character = tokeniser.consumeCharacterReference(null, false);

        // Check for the expected character.
        assertEquals('\u00A0', character);

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testCreateTagPending() {
        // This test case is to check the behavior of Tokeniser when it creates a tag pending.

        tokeniser = new Tokeniser(reader, errors);
        tokeniser.createTagPending(true);

        // Check for the expected StartTag tag pending.
        assertEquals(TokenType.StartTag, tokeniser.tagPending.getType());
        assertNull(tokeniser.tagPending.tagName);
        assertTrue(tokeniser.tagPending.selfClosing);

        // Check for the empty TagData.
        assertNull(tokeniser.tagPending.data);

        // Check for the empty TagAttributes.
        assertTrue(tokeniser.tagPending.attributes.isEmpty());
    }

    @Test
    public void testReadTagPending() throws IOException {
        // This test case is to check the behavior of Tokeniser when it reads a tag pending.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("<div class='test'>".getBytes());
        Mockito.when(reader.current()).thenReturn('<');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("/")).thenReturn(false);

        tokeniser = new Tokeniser(reader, errors);
        tokeniser.createTagPending(true);

        // Test the read method with a tag pending.
        tokeniser.read();

        // Check for the expected StartTag token.
        assertEquals(TokenType.StartTag, tokeniser.emitPending.getType());
        assertEquals("div", tokeniser.emitPending.getData().tagName);
        assertTrue(tokeniser.emitPending.getData().attributes.isEmpty());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList.
        assertFalse(errors.size() > 0);
    }

    @Test
    public void testErrorHandling() throws IOException {
        // This test case is to check the behavior of Tokeniser when it encounters errors.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("</divtest>".getBytes());
        Mockito.when(reader.current()).thenReturn('/');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with an error.
        tokeniser.read();

        // Check for the expected ParseError message.
        assertTrue(errors.size() > 0);
        assertEquals("Attributes incorrectly present on end tag", errorCaptor.getValue().getMessage());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList after the error.
        assertFalse(errors.size() > 1);
    }

    @Test
    public void testEOFErrorHandling() throws IOException {
        // This test case is to check the behavior of Tokeniser when it encounters EOF.

        ByteArrayInputStream inputStream = new ByteArrayInputStream("<div class='test'>".getBytes());
        Mockito.when(reader.current()).thenReturn('<');
        Mockito.when(reader.matchConsumeIgnoreCase("div")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase("/")).thenReturn(true);
        Mockito.when(reader.matchConsumeIgnoreCase(">"))
                .thenReturn(true)
                .thenReturn(false);

        tokeniser = new Tokeniser(reader, errors);

        // Test the read method with EOF.
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Mockito.verify(reader, Mockito.times(1)).advance(); // Verify that reader.advance() was called once when reading the last '>'.

        // Check for the expected EOF error message.
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF)", errorCaptor.getValue().getMessage());

        // Check for the empty StringBuilder.
        assertNull(tokeniser.charBuffer.toString());

        // Check for the empty ParseErrorList after the error.
        assertFalse(errors.size() > 1);
    }
}