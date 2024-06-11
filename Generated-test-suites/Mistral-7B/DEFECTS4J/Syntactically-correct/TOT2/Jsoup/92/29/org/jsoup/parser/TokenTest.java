import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {
    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();

        // Basic functionality test
        assertEquals(TokenType.Doctype, doctype.type);

        // Edge cases and exception handling
        // Test reset() method
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());

        // Test setting and getting name, pubSysKey, publicIdentifier, and systemIdentifier
        doctype.getName("html");
        assertEquals("html", doctype.getName());
        doctype.setPubSysKey("pubSysKey");
        assertEquals("pubSysKey", doctype.getPubSysKey());
        doctype.setPublicIdentifier("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        doctype.setSystemIdentifier("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());

        // Test isForceQuirks() method
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        // Test getters and setters for self-closing, attributes, and tagName
        // (These tests should be added in separate test methods for the Tag class)
    }
}