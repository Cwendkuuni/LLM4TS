import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

public class TokenTest {
    // Test for Doctype token
    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with null name
    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeTokenWithNullName() {
        new Token.Doctype(null);
    }

    // Test for Doctype token with empty name
    @Test
    public void testDoctypeTokenWithEmptyName() {
        Token token = new Token.Doctype();
        token.name = "";
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with null publicIdentifier
    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeTokenWithNullPublicIdentifier() {
        new Token.Doctype(null, null, null, true);
    }

    // Test for Doctype token with empty publicIdentifier
    @Test
    public void testDoctypeTokenWithEmptyPublicIdentifier() {
        Token token = new Token.Doctype();
        token.publicIdentifier = "";
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with non-empty publicIdentifier
    @Test
    public void testDoctypeTokenWithNonEmptyPublicIdentifier() {
        Token token = new Token.Doctype();
        token.publicIdentifier = "HTML";
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with null systemIdentifier
    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeTokenWithNullSystemIdentifier() {
        new Token.Doctype("HTML", null, null, true);
    }

    // Test for Doctype token with empty systemIdentifier
    @Test
    public void testDoctypeTokenWithEmptySystemIdentifier() {
        Token token = new Token.Doctype();
        token.systemIdentifier = "";
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with non-empty systemIdentifier
    @Test
    public void testDoctypeTokenWithNonEmptySystemIdentifier() {
        Token token = new Token.Doctype();
        token.systemIdentifier = "example.html";
        assertSame(TokenType.Doctype, token.type);
    }

    // Test for Doctype token with forceQuirks set to true
    @Test
    public void testDoctypeTokenWithForceQuirksTrue() {
        Token token = new Token.Doctype();
        token.forceQuirks = true;
        assertSame(TokenType.Doctype, token.type);
    }
}