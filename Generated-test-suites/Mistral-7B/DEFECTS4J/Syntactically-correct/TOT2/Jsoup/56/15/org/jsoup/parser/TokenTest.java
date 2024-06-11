import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctype() {
        // Create a new Doctype token
        Token doctypeToken = new Token.Doctype();

        // Check the token type
        assert (doctypeToken.type == TokenType.Doctype);

        // Test reset method
        doctypeToken.reset();
        assert (doctypeToken.type != TokenType.Doctype);

        // Test setting and getting name
        doctypeToken.name("html");
        assert (doctypeToken.getName().equals("html"));

        // Test setting and getting public identifier
        doctypeToken.publicIdentifier("_public");
        assert (doctypeToken.getPublicIdentifier().equals("_public"));

        // Test setting and getting system identifier
        doctypeToken.systemIdentifier("system.html");
        assert (doctypeToken.getSystemIdentifier().equals("system.html"));

        // Test setting and getting forceQuirks flag
        doctypeToken.forceQuirks(true);
        assert (doctypeToken.isForceQuirks());
    }
}