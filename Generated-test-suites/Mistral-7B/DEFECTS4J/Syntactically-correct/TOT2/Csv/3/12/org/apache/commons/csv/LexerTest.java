import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testNextToken() throws IOException {
        // Given
        char delimiter = ',';
        char escape = '\\';
        char quoteChar = '"';
        char commentStart = '#';
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("expected_input"));
        CSVFormat format = new CSVFormat(new CSVFormat.Builder().setDelimiter(delimiter)
                .setEscapeChar(escape).setQuoteChar(quoteChar).build());
        Lexer lexer = new Lexer(format, in);
        Token reusableToken = new Token();

        // When
        Token token = lexer.nextToken(reusableToken);

        // Then
        assertEquals(TokenType.TOKEN_TYPE_VALUE, token.getType());
        // Add more assertions for different token types and edge cases
    }
}