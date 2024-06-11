import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.getName(); // Use the method to initialize some state

        Token resetDoctype = doctype.reset();
        assertNotNull(resetDoctype);
        assertEquals(TokenType.Doctype, resetDoctype.tokenType());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
}