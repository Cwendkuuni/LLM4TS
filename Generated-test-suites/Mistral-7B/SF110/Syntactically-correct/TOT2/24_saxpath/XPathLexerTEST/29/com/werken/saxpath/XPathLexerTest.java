import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//element[@attribute='value']");
        Token token = lexer.nextToken();
        assertNotNull(token);
        assertTrue(token.getTokenType() > 0);
        assertEquals(TokenType.PIPE, lexer.nextToken().getTokenType());
        // Add more test cases as needed
    }
}