import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

@RunWith(MockitoJUnitRunner.class)
public class XPathLexerTest {

    @Mock
    private XPathLexer mockedXPathLexer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Basic functionality tests

    @Test
    public void testNextToken_ValidXPath_ReturnsExpectedToken() {
        String xpath = "//a[b='c']/d[@e='f']";
        when(mockedXPathLexer.getXPath()).thenReturn(xpath);

        // Test tokens for valid characters
        Token token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

        token = mockedXpathLexer.nextToken();
        assertEquals(TokenType.A, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.PERIOD, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.D, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.PERIOD, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    // Edge case and exception handling tests

    @Test
    public void testNextToken_EmptyXPath_ReturnsEOF() {
        when(mockedXPathLexer.getXPath()).thenReturn("");

        Token token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testNextToken_InvalidXPath_ThrowsException() {
        when(mockedXPathLexer.getXPath()).thenReturn("invalidXPath");

        mockedXPathLexer.nextToken();
    }

    @Test
    public void testNextToken_InvalidCharacter_ReturnsUnknownToken() {
        String xpath = "//a[b='c']/d[@e='f'%";
        when(mockedXPathLexer.getXPath()).thenReturn(xpath);

        Token token = mockedXPathLexer.nextToken();
        assertEquals(TokenType.PERCENT, token.getTokenType());
    }

    @Test
    public void testNextToken_MultipleConsecutiveIdentifiers_ReturnsExpectedTokens() {
        String xpath = "//a[b='c' d='e']/d[@f='g']";
        when(mockedXPathLexer.getXPath()).thenReturn(xpath);

        Token token1 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token1.getTokenType());

        Token token2 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token2.getTokenType());

        Token token3 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token3.getTokenType());

        Token token4 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.A, token4.getTokenType());

        Token token5 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token5.getTokenType());

        Token token6 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token6.getTokenType());

        Token token7 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.COMMA, token7.getTokenType());

        Token token8 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token8.getTokenType());

        Token token9 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token9.getTokenType());

        Token token10 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token10.getTokenType());

        Token token11 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token11.getTokenType());

        Token token12 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token12.getTokenType());

        Token token13 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token13.getTokenType());

        Token token14 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token14.getTokenType());

        Token token15 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token15.getTokenType());

        Token token16 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EOF, token16.getTokenType());
    }

    @Test
    public void testNextToken_MultipleConsecutiveOperators_ReturnsExpectedTokens() {
        String xpath = "//a[b='c' || d='e']/d[@f='g']";
        when(mockedXPathLexer.getXPath()).thenReturn(xpath);

        Token token1 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token1.getTokenType());

        Token token2 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token2.getTokenType());

        Token token3 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token3.getTokenType());

        Token token4 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.A, token4.getTokenType());

        Token token5 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token5.getTokenType());

        Token token6 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token6.getTokenType());

        Token token7 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.OR, token7.getTokenType());

        Token token8 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token8.getTokenType());

        Token token9 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token9.getTokenType());

        Token token10 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token10.getTokenType());

        Token token11 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token11.getTokenType());

        Token token12 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.AT, token12.getTokenType());

        Token token13 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EQUALS, token13.getTokenType());

        Token token14 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.QUOTE, token14.getTokenType());

        Token token15 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.SLASH, token15.getTokenType());

        Token token16 = mockedXPathLexer.nextToken();
        assertEquals(TokenType.EOF, token16.getTokenType());
    }
}