import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeConstructorAndReset() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        Token resetDoctype = doctype.reset();
        assertTrue(resetDoctype instanceof Token.Doctype);
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
}