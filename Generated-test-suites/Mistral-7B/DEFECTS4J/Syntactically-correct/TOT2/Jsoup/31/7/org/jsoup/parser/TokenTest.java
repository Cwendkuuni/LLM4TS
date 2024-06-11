// DoctypeTest.java
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class DoctypeTest {
    @Test
    public void testDoctypeCreation() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.getType());
    }

    @Test
    public void testDoctypeWithName() {
        Token token = new Token.Doctype().name("html");
        assertEquals("Doctype", token.tokenType());
        assertEquals("html", token.asDoctype().getName());
    }

    @Test
    public void testDoctypeWithPublicIdentifier() {
        Token token = new Token.Doctype().name("html").publicIdentifier("_public");
        assertEquals("Doctype", token.tokenType());
        assertEquals("html", token.asDoctype().getName());
        assertEquals("_public", token.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeWithSystemIdentifier() {
        Token token = new Token.Doctype().name("html").systemIdentifier("http://example.com");
        assertEquals("Doctype", token.tokenType());
        assertEquals("html", token.asDoctype().getName());
        assertEquals("http://example.com", token.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeWithForceQuirks() {
        Token token = new Token.Doctype().name("html").forceQuirks(true);
        assertEquals("Doctype", token.tokenType());
        assertEquals("html", token.asDoctype().getName());
        assertTrue(token.asDoctype().isForceQuirks());
    }
}