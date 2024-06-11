import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("doctype"); // Basic functionality test
        doctype.reset();
        assertEquals(null, doctype.getName());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testDoctypeNullName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name(null);
    }

    @Test
    public void testDoctypeResetWithName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    // Add test cases for other methods in Token.Doctype and Token class
}