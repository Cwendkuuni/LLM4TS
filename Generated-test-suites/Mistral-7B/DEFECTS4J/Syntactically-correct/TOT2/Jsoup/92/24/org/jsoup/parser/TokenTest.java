import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        Doctype resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);

        doctype.name("HTML");
        assertEquals("HTML", doctype.getName());
        ResetDoctype resetNamedDoctype = doctype.reset();
        assertNull(resetNamedDoctype.getName());

        doctype.getPubSysKey("DTD XHTML 1.0");
        assertEquals("DTD XHTML 1.0", doctype.getPubSysKey());
        ResetDoctype resetPubSysKeyDoctype = doctype.reset();
        assertNull(resetPubSysKeyDoctype.getPubSysKey());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        ResetDoctype resetForceQuirksDoctype = doctype.reset();
        assertFalse(resetForceQuirksDoctype.isForceQuirks());
    }
}