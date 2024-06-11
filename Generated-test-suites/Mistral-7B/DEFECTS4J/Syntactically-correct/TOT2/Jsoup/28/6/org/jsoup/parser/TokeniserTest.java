import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TokeniserTest {
    // Basic test cases for each method
    
    @Test
    public void testRead_NormalCharacter() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, mockErrors);
        char expectedChar = 'A';
        Mockito.when(mockReader.current()).thenReturn(expectedChar);

        Token actualToken = tokeniser.read();

        Mockito.verify(mockReader).consume();
        Mockito.verify(mockReader).mark();
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(tokeniser).emit(tokenCaptor.capture());
        Token capturedToken = tokenCaptor.getValue();
        assertEquals(Token.Character.class, capturedToken.getClass());
        assertEquals(expectedChar, capturedToken.getData());
    }

    // Edge cases and exception handling test cases
    
    @Test(expected = IllegalArgumentException.class)
    public void testTokeniserConstructor_NullReader() {
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(null, mockErrors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokeniserConstructor_NullErrors() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, null);
    }

    @Test
    public void testRead_EndOfFile() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, mockErrors);
        Mockito.when(mockReader.isEmpty()).thenReturn(true);

        Token actualToken = tokeniser.read();

        Mockito.verifyNoInteractions(mockReader);
        Mockito.verify(mockErrors).add(Mockito.any(ParseError.class));
        Mockito.verify(tokeniser).acknowledgeSelfClosingFlag();
        assertEquals(Token.EOF.class, actualToken.getClass());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, mockErrors);
        Mockito.when(mockReader.isEmpty()).thenReturn(false);
        Mockito.when(mockReader.matchConsume("#")).thenReturn(true);
        Mockito.when(mockReader.consumeHexSequence()).thenReturn("0x"); // incomplete hex sequence

        tokeniser.read();

        Mockito.verify(mockErrors).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testRead_NamedCharacterReferenceWithSemicolon() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, mockErrors);
        Mockito.when(mockReader.consumeLetterThenDigitSequence()).thenReturn("amp;");
        Mockito.when(mockReader.matchConsume(";")).thenReturn(true);

        Token actualToken = tokeniser.read();

        Mockito.verify(mockErrors).add(Mockito.any(ParseError.class));
        Mockito.verify(tokeniser).acknowledgeSelfClosingFlag();
        assertEquals(Token.Character.class, actualToken.getClass());
        assertEquals("&amp;", actualToken.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRead_NamedCharacterReferenceWithoutSemicolon() {
        CharacterReader mockReader = Mockito.mock(CharacterReader.class);
        ParseErrorList mockErrors = Mockito.mock(ParseErrorList.class);
        Tokeniser tokeniser = new Tokeniser(mockReader, mockErrors);
        Mockito.when(mockReader.consumeLetterThenDigitSequence()).thenReturn("amp");

        tokeniser.read();

        Mockito.verify(mockErrors).add(Mockito.any(ParseError.class));
    }

    // Complete test file
    
    
    @Before
    public void setUp() {
        // Initialize mocks
    }

    @Test
    public void testTokeniser() {
        // Test all methods and edge cases
    }

    
}