import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader characterReader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        characterReader = Mockito.mock(CharacterReader.class);
        tokeniser = new Tokeniser(characterReader);
    }

    @Test
    public void testReadNormalCase() throws IOException {
        // Given
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream expectedInput = new ByteArrayInputStream(input.getBytes());

        Mockito.when(characterReader.read()).then(invocation -> expectedInput.read());
        Mockito.when(characterReader.isEmpty()).thenReturn(false);
        Mockito.when(characterReader.matchConsume(anyChar())).thenAnswer(invocation -> {
            char c = invocation.getArgument(0);
            expectedInput.read();
            return c;
        });

        // When
        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        // Then
        List<Token> expectedTokens = List.of(
                new Token.StartTag("html"),
                new Token.StartTag("head"),
                new Token.Character("<"),
                new Token.Character("t"),
                new Token.Character("i"),
                new Token.Character("t"),
                new Token.Character("l"),
                new Token.Character("e"),
                new Token.Character(">"),
                new Token.StartTag("title"),
                new Token.Character(">"),
                new Token.Character("H"),
                new Token.Character("e"),
                new Token.Character("l"),
                new Token.Character("l"),
                new Token.Character("o"),
                new Token.Character(" "),
                new Token.Character("W"),
                new Token.Character("o"),
                new Token.Character("r"),
                new Token.Character("l"),
                new Token.Character("d"),
                new Token.Character("!"),
                new Token.Character("<"),
                new Token.Character("/"),
                new Token.Character("h"),
                new Token.Character("1"),
                new Token.Character(">"),
                new Token.EndTag("title"),
                new Token.EndTag("head"),
                new Token.StartTag("body"),
                new Token.EndTag("html")
        );
        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test
    public void testReadEmptyInput() throws IOException {
        // Given
        Mockito.when(characterReader.read()).thenReturn(-1);
        Mockito.when(characterReader.isEmpty()).thenReturn(true);

        // When
        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        // Then
        assertTrue(actualTokens.isEmpty());
    }

    @Test
    public void testReadUnexpectedCharacter() throws IOException {
        // Given
        Mockito.when(characterReader.read()).then(invocation -> 0xDE);
        Mockito.when(characterReader.isEmpty()).thenReturn(false);

        // When & Then
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testReadSelfClosingTag() throws IOException {
        // Given
        String input = "<img src=\"image.png\" alt=\"image\" />";
        InputStream expectedInput = new ByteArrayInputStream(input.getBytes());

        Mockito.when(characterReader.read()).then(invocation -> expectedInput.read());
        Mockito.when(characterReader.isEmpty()).thenReturn(false);
        Mockito.when(characterReader.matchConsume(anyChar())).thenAnswer(invocation -> {
            char c = invocation.getArgument(0);
            expectedInput.read();
            return c;
        });

        // When
        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        // Then
        List<Token> expectedTokens = List.of(
                new Token.StartTag("img"),
                new Token.Attribute("src", "image.png"),
                new Token.Attribute("alt", "image"),
                new Token.EndTag("img")
        );
        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test
    public void testReadComment() throws IOException {
        // Given
        String input = "<!DOCTYPE html>\n<html>\n<!-- Test comment -->\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n";
        InputStream expectedInput = new ByteArrayInputStream(input.getBytes());

        Mockito.when(characterReader.read()).then(invocation -> expectedInput.read());
        Mockito.when(characterReader.isEmpty()).thenReturn(false);
        Mockito.when(characterReader.matchConsume(anyChar())).thenAnswer(invocation -> {
            char c = invocation.getArgument(0);
            expectedInput.read();
            return c;
        });

        // When
        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        // Then
        List<Token> expectedTokens = List.of(
                new Token.Doctype(),
                new Token.StartTag("html"),
                new Token.Character("--"),
                new Token.Comment(" Test comment "),
                new Token.EndTag("html")
        );
        assertArrayEquals(expectedTokens.toArray(), actualTokens.subList(0, 5).toArray());
    }

    @Test
    public void testReadDoctype() throws IOException {
        // Given
        String input = "<!DOCTYPE html>";
        InputStream expectedInput = new ByteArrayInputStream(input.getBytes());

        Mockito.when(characterReader.read()).then(invocation -> expectedInput.read());
        Mockito.when(characterReader.isEmpty()).thenReturn(false);
        Mockito.when(characterReader.matchConsume(anyChar())).thenAnswer(invocation -> {
            char c = invocation.getArgument(0);
            expectedInput.read();
            return c;
        });

        // When
        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        // Then
        List<Token> expectedTokens = List.of(new Token.Doctype());
        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test
    public void testReadInvalidCharacterReference() throws IOException {
        // Given
        Mockito.when(characterReader.read()).thenReturn((char) 0x10FFFF);
        Mockito.when(characterReader.isEmpty()).thenReturn(false);

        // When & Then
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testReadInvalidCharacterReferenceWithAllowedCharacter() throws IOException {
        // Given
        Mockito.when(characterReader.read()).thenReturn((char) 0xFFFD);
        Mockito.when(characterReader.isEmpty()).thenReturn(false);

        // When
        Character result = tokeniser.consumeCharacterReference(null, false);

        // Then
        assertNull(result);

        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testReadInvalidCharacterReferenceWithAllowedCharacterInAttribute() throws IOException {
        // Given
        Mockito.when(characterReader.read()).thenReturn((char) 0xFFFD);
        Mockito.when(characterReader.isEmpty()).thenReturn(false);

        // When
        Character result = tokeniser.consumeCharacterReference('&', true);

        // Then
        assertNull(result);

        tokeniser.error(TokeniserState.Data);
    }
}